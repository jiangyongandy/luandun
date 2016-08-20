package com.jy.xinlangweibo.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AdvertizeView extends RelativeLayout {

	private int oldPosition;
	private ViewPager vp;
	private Context context;
	private AdIndicatorView indicator;
	private ArrayList<View> views;
	private boolean isAutoPlay = true;

	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == WHAT_AUTO_PLAY) {
				vp.setCurrentItem(vp.getCurrentItem() + 1, true);
				handler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY,
						autoPlayDuration);
			}
			return false;
		}
	});

	private int WHAT_AUTO_PLAY = 1000;
	private long autoPlayDuration = 3000;
	private int scrollDuration = 1100;

	public AdvertizeView(Context context) {
		super(context);
		this.context = context;
	}

	public AdvertizeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public AdvertizeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public void setDataSource(ArrayList<View> views) {
		this.views = views;
		if (views != null) {

			initView();
		}
	}

	private void initView() {
		vp = new ViewPager(getContext());
		setSliderTransformDuration(scrollDuration);
		vp.setAdapter(new ADViewPagerAdapter());
		vp.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 % views.size() == 0) {
					if (oldPosition == 1) {
						indicator.playBy(1, 0);
						oldPosition = 0;
					} else {
						indicator.playBy(views.size() - 1, 0);
						oldPosition = 0;
					}
				} else {
					indicator.playBy(oldPosition, arg0 % views.size());
					oldPosition = arg0 % views.size();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		RelativeLayout.LayoutParams vplp = new LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		addView(vp, vplp);
		indicator = new AdIndicatorView(context, null);
		indicator.init(views.size());
		RelativeLayout.LayoutParams indicatorlp = new LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		indicatorlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		indicatorlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		addView(indicator, indicatorlp);
		int targetItemPosition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
				% views.size();
		vp.setCurrentItem(targetItemPosition);
	}

	public void setSliderTransformDuration(int duration) {
		try {
			Field mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(
					vp.getContext(), null, duration);
			mScroller.set(vp, scroller);
		} catch (Exception e) {

		}
	}

	/**
	 * 开始自动轮播
	 */
	public void startAutoPlay() {
		stopAutoPlay(); // 避免重复消息
		if (isAutoPlay) {
			handler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration);
		}
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if (visibility == VISIBLE) {
			startAutoPlay();
		} else {
			stopAutoPlay();
		}
	}

	/**
	 * 停止自动轮播
	 */
	public void stopAutoPlay() {
		if (isAutoPlay) {
			handler.removeMessages(WHAT_AUTO_PLAY);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			stopAutoPlay();
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			startAutoPlay();
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	private class ADViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// Integer.MAX_VALUE = 2147483647
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if (views.size() > 0) {
				// position % view.size()是指虚拟的position会在[0，view.size()）之间循环
				ImageView view = (ImageView) views.get(position % views.size());
				if (container.equals(view.getParent())) {
					container.removeView(view);
				}
				view.setScaleType(ImageView.ScaleType.CENTER_CROP);
				container.addView(view,
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT);
				return view;
			}
			return null;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		}
	}

	public class FixedSpeedScroller extends Scroller {

		private int mDuration = 1000;

		public FixedSpeedScroller(Context context) {
			super(context);
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator,
				int duration) {
			this(context, interpolator);
			mDuration = duration;
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy,
				int duration) {
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}
	}
}
