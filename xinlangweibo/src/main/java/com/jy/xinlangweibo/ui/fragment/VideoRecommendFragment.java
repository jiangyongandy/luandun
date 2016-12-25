package com.jy.xinlangweibo.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jy.xinlangweibo.BaseApplication;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.videoapi.RetrofitHelper;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ChildListBean;
import com.jy.xinlangweibo.models.net.videoapi.videobean.HomePageResultBean;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ListBean;
import com.jy.xinlangweibo.ui.activity.MainActivity;
import com.jy.xinlangweibo.ui.adapter.videorecommendsections.AdSection;
import com.jy.xinlangweibo.ui.adapter.videorecommendsections.VideoBannerSection;
import com.jy.xinlangweibo.ui.adapter.videorecommendsections.VideoCategorySection;
import com.jy.xinlangweibo.ui.fragment.base.LazyFragment;
import com.jy.xinlangweibo.ui.adapter.section.SectionedRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by JIANG on 2016/12/4.
 */

public class VideoRecommendFragment extends LazyFragment {
    @BindView(R.id.rv_video_recommend)
    RecyclerView rvVideoRecommend;
    private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;

    @Override
    protected int CreateView() {
        return R.layout.fragment_video_recommend;
    }

    @Override
    protected void initViewAndEvent(View rootView) {
        mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {

            @Override
            public int getSpanSize(int position)
            {

                switch (mSectionedRecyclerViewAdapter.getSectionItemViewType(position))
                {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 2;
                    case 5:
                        return 2;

                    default:
                        return 1;
                }
            }
        });

        rvVideoRecommend.setHasFixedSize(true);
        rvVideoRecommend.setNestedScrollingEnabled(true);
        rvVideoRecommend.setLayoutManager(mGridLayoutManager);
        rvVideoRecommend.setAdapter(mSectionedRecyclerViewAdapter);

    }

    @Override
    protected void finishCreateView(Bundle state) {
        isPrepared = true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden) {
            isVisible = false;
        }else {
            isVisible = true;
        }
        if (!hidden) {
            ((MainActivity) activity).getNavTitle().setText("在线视频");
            ((MainActivity)activity).getToolbar().setVisibility(View.VISIBLE);
        }
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared)
            return;
        isPrepared = false;
        loadData();
    }

    @Override
    protected void loadData() {
        RetrofitHelper.getVideoApi().getHomePage()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HomePageResultBean>() {
                    @Override
                    public void call(HomePageResultBean models) {
                        List<ChildListBean> bannerList = models.ret.list.get(0).childList;
                        mSectionedRecyclerViewAdapter.addSection(new VideoBannerSection(bannerList));
                        models.ret.list.remove(0);
                        for (ListBean listBean : models.ret.list) {
                            if (listBean.showType.equals("adv")) {
                                mSectionedRecyclerViewAdapter.addSection(new AdSection(BaseApplication.getInstance(), BaseApplication.getInstance().random.nextInt(50)));
                            } else {
                                mSectionedRecyclerViewAdapter.addSection(new VideoCategorySection(getActivity(), listBean));
                            }
                        }
                        restore();
                        updateUi();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        showErrorMessage();
                    }
                });
    }

    @Override
    protected View getLoadingTargetView() {
        return rvVideoRecommend;
    }

    @Override
    protected void updateUi() {
        mSectionedRecyclerViewAdapter.notifyDataSetChanged();
    }
}