package com.jy.xinlangweibo.widget;

import com.jy.xinlangweibo.R;

import cn.swu.pulltorefresh.PullToRefreshListFooter;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class PulltorefreshRecyclerView extends RecyclerView {

	private static final int TOUCH_STATE_NONE = 0;
	private static final int TOUCH_STATE_Y = 1;

	private int MAX_Y = 5;
	private float mDownX;
	private float mDownY;
	private int mTouchState;

	private float mLastY = -1; // save event y
	private Scroller mScroller; // used for scroll back

	// the interface to trigger refresh and load more.
	private IXListViewListener mListViewListener;

	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private RelativeLayout mHeaderViewContent;
	private TextView mHeaderTimeView;

	// -- header view
	private View mheadBackground;
	private View mheadView;
	private int mHeaderViewHeight; // header view's height
	private int mHeadeBackgroundMAXHeight; // mheadBackground's MAXheight
	
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false; // is refreashing.

	// -- footer view
	private PullToRefreshListFooter mFooterView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;
	private int mScrollBack;
	private LinearLayoutManager manager;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;

	private final static int SCROLL_DURATION = 400;
	private final static int PULL_LOAD_MORE_DELTA = 50;
	private final static float OFFSET_RADIO = 0.7f;
	private final static int DEFHeaderViewHeight = 270;
	private final static int DEFHeaderBACKViewHeight = 370;

	public PulltorefreshRecyclerView(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
		init(arg0);
	}

	public PulltorefreshRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PulltorefreshRecyclerView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// init header view
		mheadBackground = findViewById(R.id.headBackground);

		// init footer view
		mFooterView = new PullToRefreshListFooter(context);

		MAX_Y = dp2px(MAX_Y);
		mTouchState = TOUCH_STATE_NONE;
		mHeaderViewHeight = dp2px(DEFHeaderViewHeight);
		mHeadeBackgroundMAXHeight = dp2px(DEFHeaderBACKViewHeight);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();

			mDownX = ev.getX();
			mDownY = ev.getY();
			mTouchState = TOUCH_STATE_NONE;

			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;

			float dy = Math.abs((ev.getY() - mDownY));
			float dx = Math.abs((ev.getX() - mDownX));
			mLastY = ev.getRawY();

			if (Math.pow(dx, 2) / Math.pow(dy, 2) <= 3) { // 满足条件判断为// y方向上的滑动
				manager = (LinearLayoutManager) this.getLayoutManager();
				if (manager.findFirstVisibleItemPosition() == 0&& (mheadView.getLayoutParams().height >= mHeaderViewHeight || deltaY>0)) {
					// the first item is showing, header has shown or pull down.
					// 即判断为到顶下拉
					updateHeaderHeight(deltaY * OFFSET_RADIO);
				} else if ((mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
					if(mFooterView.getBottomMargin() > 0 ) {
						System.out.println("footview 底 间距大于零");
					} else if(deltaY < 0) {
						System.out.println("deltay 小于零  deltay  值为：" +deltaY+"findFirstVisibleItemPosition值为："+manager.findFirstVisibleItemPosition());
					}
					// last item, already pulled up or want to pull up. 即判断为到底上拉
					// updateFooterHeight(-deltaY / 1.8f);
					System.out.println("到底上拉。。。。。。。。。。");
				}
			}
			if (mTouchState == TOUCH_STATE_NONE) { // 还未确定方向
				// 与自定义距离判断 是x方向滑动 还是y方向滑动
				if (Math.abs(dy) > MAX_Y) {
					mTouchState = TOUCH_STATE_Y;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			mLastY = -1; // reset
			if (mEnablePullLoad && mFooterView.getHeight() > 0
					&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) { // 到底上拉释放
				System.out.println("到底上拉释放。。。。。。");
				System.out.println("加载更多。。。。。。");
				startLoadMore();
				resetFooterHeight();
				new ResetHeaderHeightTask().execute();
			} else if (manager.findFirstVisibleItemPosition() == 0) { // 到顶下拉释放
				System.out.println("到顶下拉释放。。。。。。");
				// invoke refresh
				if (mEnablePullRefresh
						&& mheadView.getLayoutParams().height - mHeaderViewHeight > dp2px(60)) {
					System.out.println("刷新。。。。。。。。。");
					mPullRefreshing = true;
					if (mListViewListener != null) {
						mListViewListener.onRefresh(); // 注意这里为异步操作
					}
				}
				resetHeaderHeight();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	class ResetHeaderHeightTask extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			mPullRefreshing = false;
			// mHeaderView.setState(PullToRefreshListHeader.STATE_NORMAL);
			resetHeaderHeight();

		}
	}

	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * enable or disable pull up load more feature.
	 * 
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(PullToRefreshListFooter.STATE_NORMAL);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(PullToRefreshListFooter.STATE_NORMAL);
		}
	}

	/**
	 * set last refresh time
	 * 
	 * @param time
	 */
	public void setRefreshTime(String time) {
		mHeaderTimeView.setText(time);
	}

	/**
	 * 设置header最外层linearlayout 的布局高度 relativelayout的布局高度未变（即原始高度）
	 * 
	 * @param delta
	 */
	private void updateHeaderHeight(float delta) {
		android.view.ViewGroup.LayoutParams layoutParams = mheadView
				.getLayoutParams();
		layoutParams.height = (int) delta + layoutParams.height;
		if (layoutParams.height >mHeadeBackgroundMAXHeight ) {
			android.view.ViewGroup.LayoutParams headBackgroundlp = mheadBackground.getLayoutParams();
			headBackgroundlp.height = mHeadeBackgroundMAXHeight;
			mheadBackground.setLayoutParams(headBackgroundlp);
		}
		mheadView.setLayoutParams(layoutParams);
	}

	/**
	 * reset header view's height.
	 */
	private void resetHeaderHeight() {
		int height = mheadView.getLayoutParams().height;
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, mHeaderViewHeight - height,
				SCROLL_DURATION);
		System.out.println("开始滚动");
		// trigger computeScroll
		invalidate();
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
													// more.
				mFooterView.setState(PullToRefreshListFooter.STATE_READY);
			} else {
				mFooterView.setState(PullToRefreshListFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(PullToRefreshListFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				android.view.ViewGroup.LayoutParams layoutParams = mheadView
						.getLayoutParams();
				layoutParams.height = mScroller.getCurrY();
				mheadView.setLayoutParams(layoutParams);
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
		}
		super.computeScroll();
	}

	public int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getContext().getResources().getDisplayMetrics());
	}

	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}

	public View getMheadBackground() {
		return mheadBackground;
	}

	public void setMheadBackground(View mheadBackground) {
		this.mheadBackground = mheadBackground;
	}

	public View getMheadView() {
		return mheadView;
	}

	public void setMheadView(View mheadView) {
		this.mheadView = mheadView;
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface IXListViewListener {
		public void onRefresh();

		public void onLoadMore();
	}

}
