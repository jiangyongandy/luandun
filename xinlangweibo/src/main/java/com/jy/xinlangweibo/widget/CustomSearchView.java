package com.jy.xinlangweibo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.jy.xinlangweibo.R;

/**
 * Created by JIANG on 2016/12/26.
 */

public class CustomSearchView extends RelativeLayout {

    public CustomSearchView(Context context) {
        super(context);
        init(context);
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_input_searchview, this);

    }
}
