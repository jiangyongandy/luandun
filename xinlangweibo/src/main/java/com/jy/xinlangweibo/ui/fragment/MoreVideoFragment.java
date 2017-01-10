package com.jy.xinlangweibo.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiang.library.ui.widget.LineLoadRecycleView;
import com.jiang.library.ui.widget.LoadMoreRecycleView;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.videoapi.RetrofitHelper;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ChildListBean;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ListBean;
import com.jy.xinlangweibo.models.net.videoapi.videobean.VideoListBean;
import com.jy.xinlangweibo.ui.adapter.section.SectionedRecyclerViewAdapter;
import com.jy.xinlangweibo.ui.adapter.videorecommendsections.VideoCategorySection;
import com.jy.xinlangweibo.ui.fragment.base.LazyFragment;
import com.jy.xinlangweibo.ui.fragment.dialog.SearchDialogFragment;
import com.jy.xinlangweibo.utils.Logger;

import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by JIANG on 2016/12/23.
 */

public class MoreVideoFragment extends LazyFragment {
    @BindView(R.id.rv_video_recommend)
    LoadMoreRecycleView rvMoreVideo;
    @BindView(R.id.tv_search_hint)
    TextView tvSearchHint;
    @BindView(R.id.card_search)
    RelativeLayout cardSearch;
    private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;
    private ListBean bean;
    private int videoPnum = 2;
    private VideoCategorySection categorySection;
    private boolean isFromSearch;

    @Override
    protected int CreateView() {
        return R.layout.fragment_video_recommend;
    }

    @Override
    protected View getLoadingTargetView() {
        return rvMoreVideo;
    }

    @Override
    protected void initViewAndEvent(View rootView) {
        mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {

                switch (mSectionedRecyclerViewAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 2;

                    default:
                        return 1;
                }
            }
        });

        rvMoreVideo.setHasFixedSize(true);
        rvMoreVideo.setNestedScrollingEnabled(true);
        rvMoreVideo.setLayoutManager(mGridLayoutManager);
        rvMoreVideo.setAdapter(mSectionedRecyclerViewAdapter);
        rvMoreVideo.setmListViewListener(new LineLoadRecycleView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        tvSearchHint.setText("搜索在线视频");
        cardSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDialogFragment.getSearchDialogFragment().show(activity.getSupportFragmentManager(),"SearchDialogFragment");
            }
        });

        bean = (ListBean) getActivity().getIntent().getSerializableExtra("list_bean");
    }

    @Override
    protected void finishCreateView(Bundle state) {
        isPrepared = true;
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

        RetrofitHelper.getVideoApi().getVideoList(getCatalogId(bean.moreURL), "" + 1)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<VideoListBean>() {
                    @Override
                    public void call(VideoListBean models) {
                        ListBean listBean = new ListBean();
                        listBean.title = bean.title;
                        listBean.childList = models.ret.list;
                        categorySection = new VideoCategorySection(getActivity(), listBean);
                        mSectionedRecyclerViewAdapter.addSection(categorySection);
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
    protected void loadMore() {
        RetrofitHelper.getVideoApi().getVideoList(getCatalogId(bean.moreURL), "" + videoPnum++).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<VideoListBean>() {
                    @Override
                    public void call(VideoListBean models) {
                        if (models.ret.totalRecords == 0 || models.ret.totalPnum < videoPnum) {
                            showToast("没有更多视频了哦！");
                            return;
                        }
                        List<ChildListBean> childList = models.ret.list;
                        categorySection.addData(childList);
                        mSectionedRecyclerViewAdapter.notifyItemRangeInserted(mSectionedRecyclerViewAdapter.getItemCount() - childList.size(), childList.size());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.show(throwable.getMessage(), Log.WARN, "" + this);
                    }
                });
    }

    @Override
    protected void updateUi() {
        mSectionedRecyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * 根据Url获取catalogId
     *
     * @param url
     * @return
     */
    private String getCatalogId(String url) {
        String catalogId = "";
        if (!TextUtils.isEmpty(url) && url.contains("="))
            catalogId = url.substring(url.lastIndexOf("=") + 1);
        return catalogId;
    }

}
