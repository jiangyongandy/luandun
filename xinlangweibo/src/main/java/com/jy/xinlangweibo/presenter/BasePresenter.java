package com.jy.xinlangweibo.presenter;

import android.app.Activity;

/**
 * Created by JIANG on 2016/9/1.
 */
public class BasePresenter {

    protected   Activity activity;


    public BasePresenter(Activity activity) {
        this.activity = activity;
    }

    public void onDestroy() {
        activity = null;
    }
}
