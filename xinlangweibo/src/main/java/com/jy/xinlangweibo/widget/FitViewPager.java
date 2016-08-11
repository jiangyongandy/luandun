package com.jy.xinlangweibo.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class FitViewPager extends ViewPager {

	public FitViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public FitViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		 try {
			return super.onInterceptTouchEvent(arg0);
		} catch (IllegalArgumentException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
}
