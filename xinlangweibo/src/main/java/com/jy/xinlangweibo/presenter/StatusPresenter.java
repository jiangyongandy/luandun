package com.jy.xinlangweibo.presenter;

import android.app.Activity;

import com.jy.xinlangweibo.api.SimpleRequestlistener;
import com.jy.xinlangweibo.constant.AccessTokenKeeper;
import com.jy.xinlangweibo.interaction.StatusesInteraction;
import com.jy.xinlangweibo.interaction.impl.StatusesInteractionImpl;
import com.jy.xinlangweibo.ui.IView.HomeFragmentView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * Created by JIANG on 2016/8/27.
 */
public class StatusPresenter {

    private final Activity activity;
    private  HomeFragmentView homeFragmentView;
    private  StatusesInteraction api;


    public StatusPresenter(Activity activity,HomeFragmentView homeFragmentView) {
        this.activity = activity;
        this.homeFragmentView = homeFragmentView;
        Oauth2AccessToken readAccessToken = AccessTokenKeeper
                .readAccessToken(activity);
        api = new StatusesInteractionImpl(activity, readAccessToken);
    }

    public void getHomeTimeline(final int page) {
        api.getStatusesHomeTimeline(page, new SimpleRequestlistener(activity,
                null) {
            @Override
            public void onComplete(String response) {
                super.onComplete(response);
                // Statuses fromJson = new Gson().fromJson(response,
                // Statuses.class);
                homeFragmentView.updateHomeTimelineList(1,response);
                onAllDone();
            }

            @Override
            public void onWeiboException(WeiboException arg0) {
                super.onWeiboException(arg0);
                homeFragmentView.onExecptionComplete();
                onAllDone();
            }

            @Override
            protected void onAllDone() {
                super.onAllDone();
                homeFragmentView.dimissProgressDialog();
                homeFragmentView.onGetTimeLineDone();
            }
        });
    }


}
