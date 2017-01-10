package com.jiang.library.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by JIANG on 2016/12/22.
 */

public class LoadMoreRecycleView extends RecyclerView {
    private LayoutManager manager;
    private boolean mEnablePullLoad = true;
    private boolean mPullLoading;
    private LineLoadRecycleView.IXListViewListener mListViewListener;

    public LoadMoreRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public LoadMoreRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreRecycleView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(linearLayoutManager);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = manager.getItemCount();
                if(dy > 0 && manager.findLastCompletelyVisibleItemPosition() == totalItemCount - 1) {
                    //上拉到最后一个view
                        startLoadMore();
                }else if(dy < 0 && manager.findFirstCompletelyVisibleItemPosition() == 0) {
                    //下拉到第一个view
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }
        });
    }

    private void startLoadMore() {
        if (mEnablePullLoad ) {
            if (mListViewListener != null) {
                mListViewListener.onLoadMore();
            }
        }
    }

    public void setmListViewListener(LineLoadRecycleView.IXListViewListener mListViewListener) {
        this.mListViewListener = mListViewListener;
    }

}
