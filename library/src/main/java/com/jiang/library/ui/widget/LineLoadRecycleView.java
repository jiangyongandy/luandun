package com.jiang.library.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.jiang.library.R;
import com.jiang.library.ui.adapter.recyleviewadapter.ARecycleViewItemView;
import com.jiang.library.ui.adapter.recyleviewadapter.BasicRecycleViewAdapter;
import com.jiang.library.ui.widget.pulltorefresh.PullToRefreshListFooter;
import com.jiang.library.ui.widget.pulltorefresh.PullToRefreshListHeader;

/**
 * Created by JIANG on 2016/10/27.
 */
public class LineLoadRecycleView extends RecyclerView {

    private final static int PULL_REFRESH_DELTA = 50;
    private final static int PULL_LOAD_MORE_DELTA = 50;

    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;
    private final static int SCROLL_DURATION = 400;
    private int mScrollBack;

    private boolean mEnablePullLoad = true;
    private boolean mPullLoading;
    private boolean mEnableRefresh = true;
    private boolean mPullRefreshing;

    private LinearLayoutManager manager;
    private BasicRecycleViewAdapter madapter;
    private LineLoadFooterItemView lineLoadFooterItemView;
    private LineLoadHeaderItemView lineLoadHeadererItemView;
    private PullToRefreshListHeader mHeaderView;
    private PullToRefreshListFooter mFooterView;
    private RelativeLayout mHeaderViewContent;
    private Scroller mScroller;

    private int mHeaderViewHeight;

    // the interface to trigger refresh and load more.
    private IXListViewListener mListViewListener;

    public LineLoadRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public LineLoadRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LineLoadRecycleView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        //		default linearLayout
        mScroller = new Scroller(context, new DecelerateInterpolator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(linearLayoutManager);
        lineLoadFooterItemView = new LineLoadFooterItemView(new PullToRefreshListFooter(context));
        lineLoadHeadererItemView = new LineLoadHeaderItemView(new PullToRefreshListHeader(context));
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
        manager = (LinearLayoutManager) this.getLayoutManager();
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                if(dy > 0 && manager.findLastCompletelyVisibleItemPosition() == totalItemCount - 1) {
                    //上拉到最后一个view
                    updateFooterHeight(-dy);
                }else if(dy < 0 && manager.findFirstCompletelyVisibleItemPosition() == 0) {
                    //下拉到第一个view
                    updateHeaderHeight(dy);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }
        });
        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeaderViewHeight = mHeaderViewContent.getHeight();  //headerview 正常布局高度
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(mHeaderView.getmState() == PullToRefreshListHeader.STATE_READY) {
                        startRefresh();
                        resetHeaderHeight();
                    }
                    if(mFooterView.getmState() == PullToRefreshListFooter.STATE_READY) {
                        startLoadMore();
                        resetFooterHeight();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if(!(adapter instanceof BasicRecycleViewAdapter))
            throw new RuntimeException("lineLoadRecycleView's adapter need is BasicRecycleViewAdapter");
        madapter = (BasicRecycleViewAdapter) adapter;
        madapter.addFooterView(lineLoadFooterItemView);
        madapter.addHeadView(lineLoadHeadererItemView);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
        }
        super.computeScroll();
    }

    private void updateFooterHeight(float delta) {
//		Logger.showLog("======"+delta,"updateFooterHeight");
        int height = mFooterView.getBottomMargin() + (int) delta;
        //上拉边界
        if(height >= PULL_LOAD_MORE_DELTA+100)
            return;
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

    private void updateHeaderHeight(float delta) {
        android.view.ViewGroup.LayoutParams layoutParams = mHeaderView
                .getLayoutParams();
        layoutParams.height = (int) delta + layoutParams.height;
        if (layoutParams.height > PULL_REFRESH_DELTA ) {
            mHeaderView.setState(PullToRefreshListHeader.STATE_READY);
        }else {
            mHeaderView.setState(PullToRefreshListHeader.STATE_NORMAL);
        }
        mHeaderView.setLayoutParams(layoutParams);
//        setSelection(0); // scroll to top each time
    }

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
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
        } else {
            mPullLoading = false;
            mFooterView.show();
            mFooterView.setState(PullToRefreshListFooter.STATE_NORMAL);
        }
    }

    private void startLoadMore() {
        if (mEnablePullLoad ) {
            mPullLoading = true;
            mFooterView.setState(PullToRefreshListFooter.STATE_LOADING);
            if (mListViewListener != null) {
                mListViewListener.onLoadMore();
            }
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
     * enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void setRefreshEnable(boolean enable) {
        mEnableRefresh = enable;
        if (!mEnableRefresh) {
            mHeaderView.hide();
        } else {
            mHeaderView.show();
            mHeaderView.setState(PullToRefreshListFooter.STATE_NORMAL);
        }
    }

    public void startRefresh() {
        if (mEnableRefresh ) {
            mPullRefreshing = true;
            mHeaderView.setState(PullToRefreshListHeader.STATE_REFRESHING);
            if (mListViewListener != null) {
                mListViewListener.onRefresh();  //注意这里为异步操作
            }
        }
    }

    /**
     * stop refresh, reset header view.
     */
    public void stopRefresh() {
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            mHeaderView.setState(PullToRefreshListHeader.STATE_NORMAL);
            resetHeaderHeight();
        }
    }

    public void setmFooterView(PullToRefreshListFooter mFooterView) {
        this.mFooterView = mFooterView;
    }

    public void setmHeaderView(PullToRefreshListHeader mHeaderView) {
        this.mHeaderView = mHeaderView;
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface IXListViewListener {
        public void onRefresh();

        public void onLoadMore();
    }

    public void setmListViewListener(IXListViewListener mListViewListener) {
        this.mListViewListener = mListViewListener;
    }


    public class LineLoadFooterItemView extends ARecycleViewItemView {
        public LineLoadFooterItemView(View itemView) {
            super(itemView);
            setmFooterView((PullToRefreshListFooter)itemView);
        }
    }

    public class LineLoadHeaderItemView extends ARecycleViewItemView {
        public LineLoadHeaderItemView(View itemView) {
            super(itemView);
            setmHeaderView((PullToRefreshListHeader)itemView);
        }
    }
}
