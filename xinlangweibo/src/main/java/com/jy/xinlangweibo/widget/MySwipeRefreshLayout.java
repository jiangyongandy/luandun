package com.jy.xinlangweibo.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by JIANG on 2016/7/28.
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(visibility == INVISIBLE) {
            System.out.println("当前状态不可见");
            this.setRefreshing(false);
        }else if(visibility == VISIBLE) {
            String simpleName = getChildAt(0).getClass().getSimpleName();
            System.out.println("当前状态可见----------"+simpleName);
//            this.setRefreshing(true);
        }else if(visibility == GONE) {
            System.out.println("已经GONE");
            this.setRefreshing(false);
        }
    }
}

