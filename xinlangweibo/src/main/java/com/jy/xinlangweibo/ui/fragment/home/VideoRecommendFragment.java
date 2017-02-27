package com.jy.xinlangweibo.ui.fragment.home;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jy.xinlangweibo.BaseApplication;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.videoapi.RetrofitHelper;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ChildListBean;
import com.jy.xinlangweibo.models.net.videoapi.videobean.HomePageResultBean;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ListBean;
import com.jy.xinlangweibo.ui.activity.MainActivity;
import com.jy.xinlangweibo.ui.adapter.section.Section;
import com.jy.xinlangweibo.ui.adapter.section.SectionedRecyclerViewAdapter;
import com.jy.xinlangweibo.ui.adapter.videorecommendsections.AdSection;
import com.jy.xinlangweibo.ui.adapter.videorecommendsections.GoodVideoCategorySection;
import com.jy.xinlangweibo.ui.adapter.videorecommendsections.VideoBannerSection;
import com.jy.xinlangweibo.ui.adapter.videorecommendsections.VideoCategorySection;
import com.jy.xinlangweibo.ui.fragment.base.LazySupportFragment;
import com.jy.xinlangweibo.ui.fragment.dialog.VideoSearchDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by JIANG on 2016/12/4.
 */

public class VideoRecommendFragment extends LazySupportFragment {
    @BindView(R.id.rv_video_recommend)
    RecyclerView rvVideoRecommend;
    @BindView(R.id.tv_search_hint)
    TextView tvSearchHint;
    @BindView(R.id.card_search)
    RelativeLayout cardSearch;
    private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;

    @Override
    protected int CreateView() {
        return R.layout.fragment_video_recommend;
    }

    @Override
    protected void initViewAndEvent(View rootView) {
        mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 6);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {

                switch (mSectionedRecyclerViewAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:

                        return 6;
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_SECTION_ITEM_LAST:

                        return 6;
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_ITEM_LOADED:

                        Integer spanSize = mSectionedRecyclerViewAdapter.getSectionForPosition(position).getItemSpanSize();
                        return spanSize;

                    default:
                        return 1;
                }
            }
        });

        rvVideoRecommend.setHasFixedSize(true);
        rvVideoRecommend.setNestedScrollingEnabled(true);
        rvVideoRecommend.setLayoutManager(mGridLayoutManager);
        rvVideoRecommend.setAdapter(mSectionedRecyclerViewAdapter);

        tvSearchHint.setText("搜索在线视频");

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            isVisible = false;
        } else {
            isVisible = true;
        }
        if (!hidden) {
            ((MainActivity) activity).getNavTitle().setText("在线视频");
            ((MainActivity) activity).getToolbar().setVisibility(View.VISIBLE);
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
                        dismissLoading();
                        updateUi(models);
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

    private void updateUi(HomePageResultBean models) {
        List<ChildListBean> bannerList = models.ret.list.get(0).childList;
        mSectionedRecyclerViewAdapter.addSection(new VideoBannerSection(bannerList));
        models.ret.list.remove(0);
        for (ListBean listBean : models.ret.list) {
            if (listBean.showType.equals("adv")) {
                mSectionedRecyclerViewAdapter.addSection(new AdSection(getActivity(), BaseApplication.getInstance().random.nextInt(50)));
            } else {
                Section section;
                if(listBean.title.equals("精彩推荐")) {
                    section = new GoodVideoCategorySection(getActivity(), listBean);
                    section.setItemSpanSize(2);
                }else {

                    section = new VideoCategorySection(getActivity(), listBean);
                    section.setItemSpanSize(3);
                }
                mSectionedRecyclerViewAdapter.addSection(section);
            }
        }
        mSectionedRecyclerViewAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.card_search)
    public void onClick() {
        VideoSearchDialogFragment.getVideoSearchDialogFragment().show(((AppCompatActivity) getActivity()).getSupportFragmentManager(), "VideoSearchDialogFragment");
    }
}
