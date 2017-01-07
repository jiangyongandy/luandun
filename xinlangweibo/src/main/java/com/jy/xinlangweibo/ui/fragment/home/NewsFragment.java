package com.jy.xinlangweibo.ui.fragment.home;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.newsapi.RetrofitHelper;
import com.jy.xinlangweibo.models.net.newsapi.bean.ChannelListBean;
import com.jy.xinlangweibo.models.net.newsapi.bean.NewsChannelList;
import com.jy.xinlangweibo.ui.activity.MainActivity;
import com.jy.xinlangweibo.ui.adapter.base.BaseViewPagerAdapter;
import com.jy.xinlangweibo.ui.fragment.NewsCategoryFragment;
import com.jy.xinlangweibo.ui.fragment.base.LazySupportFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by JIANG on 2017/1/4.
 */

public class NewsFragment extends LazySupportFragment {

    @BindView(R.id.tab_news)
    TabLayout tabNews;
    @BindView(R.id.vp_news)
    ViewPager vpNews;

    private BaseViewPagerAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((MainActivity) activity).getToolbar().setVisibility(View.VISIBLE);
            ((MainActivity) activity).getNavTitle().setText("新闻资讯");
        }
    }

    @Override
    protected int CreateView() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initViewAndEvent(View rootView) {
        pagerAdapter = new BaseViewPagerAdapter(getChildFragmentManager(),fragments,titles);

        loadData();
    }

    @Override
    protected void loadData() {
        RetrofitHelper.getNewsApi().getNewsChannel().subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsChannelList>() {
                    @Override
                    public void call(NewsChannelList newsChannelList) {
                        dismissLoading();
                        boolean isFirstPage = true;
                        for(ChannelListBean bean : newsChannelList.showapi_res_body.channelList.subList(0,6)) {
                            titles.add(bean.name);
                            if(isFirstPage) {
                                fragments.add(NewsCategoryFragment.getNewsCategoryFragment(bean,true));
                                isFirstPage = false;
                            }
                            else
                                fragments.add(NewsCategoryFragment.getNewsCategoryFragment(bean));
                        }
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
    protected void updateUi() {
        vpNews.setOffscreenPageLimit(6);
        vpNews.setAdapter(pagerAdapter);
        tabNews.setupWithViewPager(vpNews);
    }

    @Override
    protected View getLoadingTargetView() {
        return vpNews;
    }

}
