package com.jy.xinlangweibo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiang.library.ui.widget.LineLoadRecycleView;
import com.jiang.library.ui.widget.LoadMoreRecycleView;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.cache.LocaleHistory;
import com.jy.xinlangweibo.models.net.videoapi.RetrofitHelper;
import com.jy.xinlangweibo.models.net.videoapi.VideoHttpResponse;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ChildListBean;
import com.jy.xinlangweibo.models.net.videoapi.videobean.VideoRes;
import com.jy.xinlangweibo.models.net.videoapi.videobean.VideoType;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.ui.adapter.section.SectionedRecyclerViewAdapter;
import com.jy.xinlangweibo.ui.adapter.videoinfosections.CommentSection;
import com.jy.xinlangweibo.ui.adapter.videoinfosections.RelationsVideoSection;
import com.jy.xinlangweibo.ui.adapter.videoinfosections.VideoInfoBean;
import com.jy.xinlangweibo.ui.adapter.videoinfosections.VideoInfoSection;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by JIANG on 2016/12/22.
 */
//todo 增加缓存处理
public class VideoInfoActivity extends BaseActivity {
    @BindView(R.id.video_player)
    JCVideoPlayerStandard videoPlayer;
    @BindView(R.id.rv_video_info)
    LoadMoreRecycleView rvVideoInfo;
    private int layoutRes = R.layout.activity_videoinfo;
    private ChildListBean childListBean;
    private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;
    private int commentPnum = 2;
    private CommentSection commentSection;

    private JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;
    private SensorManager sensorManager;

    public static void launch(Activity from, ChildListBean bean) {
        Intent intent = new Intent(from, VideoInfoActivity.class);
        intent.putExtra("child_list_bean", bean);
        from.startActivity(intent);
    }

    @Override
    protected View getLoadingTargetView() {
        return rvVideoInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        setContentView(layoutRes);
        childListBean = (ChildListBean) getIntent().getSerializableExtra("child_list_bean");
        if (childListBean == null) {
            ToastUtils.show(this, "请求错误，在尝试一次", Toast.LENGTH_SHORT);
            this.finish();
        }

        videoPlayer.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        videoPlayer.backButton.setVisibility(View.GONE);
        videoPlayer.titleTextView.setVisibility(View.GONE);
        videoPlayer.tinyBackImageView.setVisibility(View.GONE);
        mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        rvVideoInfo.setHasFixedSize(true);
        rvVideoInfo.setNestedScrollingEnabled(true);
        rvVideoInfo.setAdapter(mSectionedRecyclerViewAdapter);
        rvVideoInfo.setmListViewListener(new LineLoadRecycleView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                RetrofitHelper.getVideoApi().getCommentList(childListBean.dataId, ""+commentPnum++).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<VideoHttpResponse<VideoRes>>() {
                            @Override
                            public void call(VideoHttpResponse<VideoRes> videoResVideoHttpResponse) {
                                if(videoResVideoHttpResponse.getRet().totalRecords == 0 || videoResVideoHttpResponse.getRet().totalPnum < commentPnum) {
                                    showToast("没有更多评论了哦！");
                                    return;
                                }
                                List<VideoType> list = videoResVideoHttpResponse.getRet().list;
                                commentSection.addData(list);
                                mSectionedRecyclerViewAdapter.notifyItemRangeInserted(mSectionedRecyclerViewAdapter.getItemCount()-list.size(),list.size());
                                rvVideoInfo.resumeLoadMore();
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                rvVideoInfo.resumeLoadMore();
                                Logger.show(throwable.getMessage(), Log.WARN,""+this);
                            }
                        });
            }
        });

        loadData();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
        sensorManager.unregisterListener(sensorEventListener);
        LocaleHistory.getInstance().addVideoPlayHistory(childListBean);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void loadData() {
        Observable<VideoHttpResponse<VideoRes>> observable = RetrofitHelper.getVideoApi().getVideoInfo(childListBean.dataId).subscribeOn(Schedulers.io());
        Observable<VideoHttpResponse<VideoRes>> observable2 = RetrofitHelper.getVideoApi().getCommentList(childListBean.dataId, "1").subscribeOn(Schedulers.io());
        Observable.zip(observable, observable2, new Func2<VideoHttpResponse<VideoRes>, VideoHttpResponse<VideoRes>, VideoInfoBean>() {
            @Override
            public VideoInfoBean call(VideoHttpResponse<VideoRes> videoResVideoHttpResponse, VideoHttpResponse<VideoRes> videoResVideoHttpResponse2) {
                VideoInfoBean bean = getVideoInfoBean(videoResVideoHttpResponse.getRet());
                bean.relationsList = videoResVideoHttpResponse.getRet().list;
                bean.comment = videoResVideoHttpResponse2.getRet();
                return bean;
            }
        })
        .doOnSubscribe(new Action0() {
            @Override
            public void call() {
                showLoading();
            }
        })
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<VideoInfoBean>() {
            @Override
            public void call(VideoInfoBean bean) {
                restoreLayout();
                mSectionedRecyclerViewAdapter.addSection(new VideoInfoSection(VideoInfoActivity.this, bean));
                for (VideoType videoType : bean.relationsList) {
                    mSectionedRecyclerViewAdapter.addSection(new RelationsVideoSection(VideoInfoActivity.this, videoType));
                }
                commentSection = new CommentSection(VideoInfoActivity.this, bean.comment);
                mSectionedRecyclerViewAdapter.addSection(commentSection);
                mSectionedRecyclerViewAdapter.notifyDataSetChanged();

                if (!TextUtils.isEmpty(childListBean.pic)) {
                    Glide.with(VideoInfoActivity.this)
                            .load(childListBean.pic)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.timeline_image_loading)
                            .dontAnimate()
                            .into(videoPlayer.thumbImageView);
                }
                if (!TextUtils.isEmpty(bean.getVideoUrl())) {
                    videoPlayer.setUp(bean.getVideoUrl()
                            , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, bean.videoInfo.title);
                    //todo 判断网络环境
//                    videoPlayer.onClick(videoPlayer.thumbImageView);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                showErrorMessage();
                Logger.show(throwable.getMessage(), Log.WARN,""+this);
            }
        });
    }

    private VideoInfoBean getVideoInfoBean(VideoRes videoRes) {
        VideoInfoBean bean = new VideoInfoBean();
        bean.videoInfo = videoRes;
        return bean;
    }
}
