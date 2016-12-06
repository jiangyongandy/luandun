package com.jy.xinlangweibo.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jy.xinlangweibo.BaseApplication;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.sinaapi.BaseObserver;
import com.jy.xinlangweibo.models.net.videoapi.RetrofitHelper;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ChildListBean;
import com.jy.xinlangweibo.models.net.videoapi.videobean.HomePageResultBean;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ListBean;
import com.jy.xinlangweibo.ui.adapter.section.VideoBannerSection;
import com.jy.xinlangweibo.ui.adapter.section.VideoCategorySection;
import com.jy.xinlangweibo.ui.fragment.base.LazyFragment;
import com.jy.xinlangweibo.widget.sectioned.SectionedRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
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
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {

            @Override
            public int getSpanSize(int position)
            {

                switch (mSectionedRecyclerViewAdapter.getSectionItemViewType(position))
                {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 3;

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
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible)
            return;
        isPrepared = false;
        loadData();
    }

    @Override
    protected void loadData() {
        RetrofitHelper.getVideoApi().getHomePage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<HomePageResultBean>(){
                    @Override
                    public void onNext(HomePageResultBean models) {
                        super.onNext(models);
                        List<ChildListBean> bannerList = models.ret.list.get(0).childList;
                        mSectionedRecyclerViewAdapter.addSection(new VideoBannerSection(bannerList));
                        for(ListBean listBean : models.ret.list) {
                            mSectionedRecyclerViewAdapter.addSection(new VideoCategorySection(BaseApplication.getInstance(),listBean));
                        }
                        updateUi();
                    }
                });
    }

    @Override
    protected void updateUi() {
        mSectionedRecyclerViewAdapter.notifyDataSetChanged();
    }
}
