package com.jy.xinlangweibo.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.jiang.library.ui.widget.LineLoadRecycleView;
import com.jiang.library.ui.widget.LoadMoreRecycleView;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.newsapi.RetrofitHelper;
import com.jy.xinlangweibo.models.net.newsapi.bean.ChannelListBean;
import com.jy.xinlangweibo.models.net.newsapi.bean.ContentlistBean;
import com.jy.xinlangweibo.models.net.newsapi.bean.NewsBean;
import com.jy.xinlangweibo.ui.adapter.newssections.MultiNewsSection;
import com.jy.xinlangweibo.ui.adapter.newssections.SingleNewsSection;
import com.jy.xinlangweibo.ui.adapter.section.SectionedRecyclerViewAdapter;
import com.jy.xinlangweibo.ui.fragment.base.LazySupportFragment;
import com.jy.xinlangweibo.utils.Logger;

import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by JIANG on 2017/1/4.
 */

public class NewsCategoryFragment extends LazySupportFragment {

    @BindView(R.id.rv_common)
    LoadMoreRecycleView rvCommon;
    @BindView(R.id.srl_common)
    SwipeRefreshLayout srlCommon;

    private ChannelListBean bean;
    private int pNum = 2;

    private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;

    public static NewsCategoryFragment getNewsCategoryFragment(ChannelListBean bean,boolean isFirstPage) {
        NewsCategoryFragment fragment = new NewsCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ChannelListBean", bean);
        bundle.putBoolean("isFirstPage",isFirstPage);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static NewsCategoryFragment getNewsCategoryFragment(ChannelListBean bean) {
        NewsCategoryFragment fragment = new NewsCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ChannelListBean", bean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int CreateView() {
        return R.layout.comm_recycleview;
    }

    @Override
    protected void initViewAndEvent(View rootView) {
        bean = (ChannelListBean) getArguments().getSerializable("ChannelListBean");
        boolean isFirstPage = (boolean) getArguments().getBoolean("isFirstPage",false);
        mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        rvCommon.setmListViewListener(new LineLoadRecycleView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });
        rvCommon.setAdapter(mSectionedRecyclerViewAdapter);
        srlCommon.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        if(isFirstPage)
            loadData();
    }

    //todo 延时10ms 加载等待 viewCreate(即 isPrepared = true);
    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible)
            return;
        isPrepared = false;
        loadData();
    }

    @Override
    protected void loadData() {
        RetrofitHelper.getNewsApi().getNewsList(bean.channelId, "1").subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsBean>() {
                    @Override
                    public void call(NewsBean newsBean) {
                        dismissLoading();
                        srlCommon.setRefreshing(false);
                        updateUi(newsBean, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.show(throwable.getMessage(), Log.ERROR, "getNewsList");
                        showErrorMessage();
                    }
                });
    }

    @Override
    protected void loadMoreData() {

        RetrofitHelper.getNewsApi().getNewsList(bean.channelId, "" + pNum++).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsBean>() {
                    @Override
                    public void call(NewsBean newsBean) {
                        if (newsBean.showapi_res_body.pagebean.allNum == 0 || newsBean.showapi_res_body.pagebean.allPages < pNum) {
                            showToast("没有更多新闻了哦！");
                            return;
                        }
                        updateUi(newsBean, pNum);
                    }
                });

    }

    private void refreshData() {
        RetrofitHelper.getNewsApi().getNewsList(bean.channelId, "1").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsBean>() {
                    @Override
                    public void call(NewsBean newsBean) {
                        srlCommon.setRefreshing(false);
                        updateUi(newsBean, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.show(throwable.getMessage(), Log.ERROR, "getNewsList");
                        showErrorMessage();
                    }
                });
    }

    private void updateUi(NewsBean newsBean, int pNum) {
        if (pNum == 1)
            mSectionedRecyclerViewAdapter.removeAllSections();
        List<ContentlistBean> contentlist = newsBean.showapi_res_body.pagebean.contentlist;
        for (ContentlistBean bean : contentlist) {
            if (bean.imageurls.size() < 3) {
                mSectionedRecyclerViewAdapter.addSection(new SingleNewsSection(getActivity(), bean));
            } else {
                mSectionedRecyclerViewAdapter.addSection(new MultiNewsSection(getActivity(), bean));
            }
        }
        if(pNum == 1)
            mSectionedRecyclerViewAdapter.notifyDataSetChanged();
        else
            mSectionedRecyclerViewAdapter.notifyItemRangeInserted(mSectionedRecyclerViewAdapter.getItemCount() - contentlist.size(), contentlist.size());
    }

    @Override
    protected View getLoadingTargetView() {
        return rvCommon;
    }

}
