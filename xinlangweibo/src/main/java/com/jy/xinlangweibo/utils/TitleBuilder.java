package com.jy.xinlangweibo.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jy.xinlangweibo.R;

/**
 * @author JIANG
 * 
 */
public class TitleBuilder {
	private TextView leftText;
	private ImageView leftImage;
	private TextView title;
	private ImageView rightImage;
	private Button rightButton;
	private Context context;
	private RelativeLayout rl_titlebar;
	

	public TitleBuilder(View context) {
		this.context = context.getContext();
		rl_titlebar = (RelativeLayout) context.findViewById(R.id.rl_titlebar);
		leftText = (TextView) context.findViewById(R.id.nav_left_text);
		leftImage = (ImageView) context.findViewById(R.id.nav_left_iv);
		title = (TextView) context.findViewById(R.id.nav_title);
		rightImage = (ImageView) context
				.findViewById(R.id.nav_right_iv);
		rightButton = (Button) context.findViewById(R.id.nav_right_text);
	}
	public TitleBuilder(Activity context) {
		this.context = context;
		leftText = (TextView) context.findViewById(R.id.nav_left_text);
		leftImage = (ImageView) context.findViewById(R.id.nav_left_iv);
		title = (TextView) context.findViewById(R.id.nav_title);
		rightImage = (ImageView) context
				.findViewById(R.id.nav_right_iv);
		rightButton = (Button) context.findViewById(R.id.nav_right_text);
	}

//	public static void setCustomActionBar(Activity context, View view) {
//		// 返回箭头（默认不显示）  
//		context.getActionBar().setDisplayHomeAsUpEnabled(false);  
//       // 左侧图标点击事件使能  
//		context.getActionBar().setHomeButtonEnabled(false);  
//       // 使左上角图标(系统)是否显示  
//		context.getActionBar().setDisplayShowHomeEnabled(false);  
//       // 显示标题  这里要为TRUE否则布局只能显示左边三分之一。
//		context.getActionBar().setDisplayShowTitleEnabled(true);  
//       //显示自定义视图  
//		context.getActionBar().setDisplayShowCustomEnabled(true);
//		context.getActionBar().setCustomView(view);
//	}

	public TitleBuilder setTitle(String text) {
		title.setText(text);
		return this;
	}
	public TitleBuilder setLeftText(String text,int res) {
		if (leftImage.getVisibility() == View.VISIBLE) {
			leftImage.setVisibility(View.GONE);
		}
		leftText.setVisibility(TextUtils.isEmpty(text) ? View.GONE
				: View.VISIBLE);
		leftText.setText(text);
		if(res != 0) {
			leftText.setBackgroundResource(res);
		}
		return this;
	}
	public TitleBuilder setLeftimg(int id) {
		if (leftText.getVisibility() == View.VISIBLE) {
			leftText.setVisibility(View.GONE);
		}
		leftImage.setImageResource(id);
		leftImage.setVisibility(View.VISIBLE);
		return this;
	}
	public TitleBuilder setRightimg(int id) {
		if (rightButton.getVisibility() == View.VISIBLE) {
			rightButton.setVisibility(View.GONE);
		}
		rightImage.setImageResource(id);
		rightImage.setVisibility(View.VISIBLE);
		return this;
	}

	public TitleBuilder setrightButton(String text,int bacres,int textcolor) {
		if (rightImage.getVisibility() == View.VISIBLE) {
			rightImage.setVisibility(View.GONE);
		}
		rightButton.setVisibility(TextUtils.isEmpty(text) ? View.GONE
				: View.VISIBLE);
		rightButton.setText(text);
		if(bacres != 0) {
			rightButton.setBackgroundResource(bacres);
		} else {
			rightButton.setBackgroundDrawable(null);
		}
		if(textcolor != 0) {
			rightButton.setTextColor(context.getResources().getColor(textcolor));
		} else {
			rightButton.setTextColor(context.getResources().getColor(R.color.black));
		}
		return this;
	}

	public TitleBuilder setLeftOnclickListener(OnClickListener listener) {
		if (leftText.getVisibility() == View.VISIBLE) {
			leftText.setOnClickListener(listener);
		} else {
			leftImage.setOnClickListener(listener);
		}
		return this;
	}

	public TitleBuilder setRightOnclickListener(OnClickListener listener) {
		if (rightButton.getVisibility() == View.VISIBLE) {
			rightButton.setOnClickListener(listener);
		} else {
			rightImage.setOnClickListener(listener);
		}
		return this;
	}
	public TitleBuilder setRightClickable( boolean b) {
		if (rightButton.getVisibility() == View.VISIBLE) {
			rightButton.setClickable(b);
		} else {
			rightImage.setClickable(b);
		}
		return this;
	}
	public View getRl_titlebar() {
		return rl_titlebar;
	}
	
	public TitleBuilder getTitleBuilder() {
		return this;
	}
}
