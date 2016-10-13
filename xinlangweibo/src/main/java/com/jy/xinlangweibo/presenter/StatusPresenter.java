package com.jy.xinlangweibo.presenter;

import android.app.Activity;

import com.jy.xinlangweibo.constant.AccessTokenKeeper;
import com.jy.xinlangweibo.models.StatusesInteraction;
import com.jy.xinlangweibo.models.bean.StatusListBean;
import com.jy.xinlangweibo.models.retrofitservice.BaseObserver;
import com.jy.xinlangweibo.models.retrofitservice.StatusInteraction;
import com.jy.xinlangweibo.ui.IView.HomeFragmentView;

/**
 * Created by JIANG on 2016/8/27.
 */
public class StatusPresenter extends BasePresenter{

    private final HomeFragmentView homeFragmentView;
    private final String token;
    private  StatusesInteraction api;


    public StatusPresenter(Activity activity,HomeFragmentView homeFragmentView) {
        super(activity);
        this.homeFragmentView = homeFragmentView;
        token = AccessTokenKeeper.readAccessToken(activity).getToken();
    }

    public void getHomeTimeline(final int page) {
        StatusInteraction.getInstance(activity).statusesPublic_timeline(token, String.valueOf(page),new BaseObserver<StatusListBean>() {
            @Override
            public void onNext(StatusListBean models) {
                super.onNext(models);
                homeFragmentView.updateHomeTimelineList(page,models);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
//                homeFragmentView.dimissProgressDialog();
                homeFragmentView.onGetTimeLineDone();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                homeFragmentView.onExecptionComplete();
            }
        });

    }


}
