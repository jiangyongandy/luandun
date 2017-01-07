package com.jy.xinlangweibo.widget.searchview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.widget.emotionkeyboard.EmoticonsEditText;

/**
 * Created by JIANG on 2017/1/3.
 */

public class SearchView extends LinearLayout {

    private EmoticonsEditText mEtSearch;
    private ImageView mIvSearch;;
    private TextView mTvCancel;
    private OnSearchSubmitListener onSearchSubmitListener;
    private OnCancelSearchListener onCancelSearchListener;

    public SearchView(Context context) {
        super(context);
        init();
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_input_searchview2,this);
        mEtSearch = (EmoticonsEditText) findViewById(R.id.et_search);
        mIvSearch = (ImageView) findViewById(R.id.iv_search);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel);
        mIvSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSearchSubmitListener != null) {
                    onSearchSubmitListener.onSearchSubmit();
                }
            }
        });
        mTvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCancelSearchListener != null)
                    onCancelSearchListener.onSearchCancel();
            }
        });
    }

    public void setOnSearchSubmitListener(OnSearchSubmitListener onSearchSubmitListener) {
        this.onSearchSubmitListener = onSearchSubmitListener;
    }

    public void setOnCancelSearchListener(OnCancelSearchListener onCancelSearchListener) {
        this.onCancelSearchListener = onCancelSearchListener;
    }

    public interface OnSearchSubmitListener {
        void onSearchSubmit();
    }

    public interface OnCancelSearchListener {
        void onSearchCancel();
    }

}
