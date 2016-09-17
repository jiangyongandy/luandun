package com.jy.xinlangweibo.widget.ninephoto;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.jy.xinlangweibo.utils.CommonImageLoader.CustomImageLoader;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/9/8 下午1:05
 * 描述:
 */
public class BGARVOnScrollListener extends RecyclerView.OnScrollListener {
    private Activity mActivity;

    public BGARVOnScrollListener(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            CustomImageLoader.resume(mActivity);
        } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            CustomImageLoader.pause(mActivity);
        }
    }
}
