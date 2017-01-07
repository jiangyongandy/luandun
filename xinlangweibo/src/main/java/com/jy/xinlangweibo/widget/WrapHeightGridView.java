package com.jy.xinlangweibo.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class WrapHeightGridView extends GridView {

	public WrapHeightGridView(Context context) {
		super(context);
	}

	public WrapHeightGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WrapHeightGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
