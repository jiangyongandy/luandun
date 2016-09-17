package com.jy.xinlangweibo.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class FitViewPager extends ViewPager {

	public FitViewPager(Context context) {
		super(context);
	}

	public FitViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		 try {
			return super.onInterceptTouchEvent(arg0);
		} catch (IllegalArgumentException  e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
