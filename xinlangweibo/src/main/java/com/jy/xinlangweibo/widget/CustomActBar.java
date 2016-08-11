package com.jy.xinlangweibo.widget;

import com.jy.xinlangweibo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomActBar extends RelativeLayout {

	private View view;
	private TextView leftText;
	private ImageView leftImage;
	private TextView title;
	private ImageView rightImage;
	private Button rightButton;

	public CustomActBar(Context context) {
		super(context);
		init(context);
	}


	public CustomActBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CustomActBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}


	private void init(Context context) {
//		View.inflate(context, R.layout.include_profile_actbar, this);
		view = LayoutInflater.from(context).inflate(R.layout.include_profile_actbar, this, true);  
		leftText = (TextView) view.findViewById(R.id.nav_left_text);
		leftImage = (ImageView) view.findViewById(R.id.nav_left__iv);
		title = (TextView) view.findViewById(R.id.nav_title);
		rightImage = (ImageView) view
				.findViewById(R.id.nav_right__iv);
		rightButton = (Button) view.findViewById(R.id.nav_right_text);
	}


	public View getView() {
		return view;
	}


	public void setView(View view) {
		this.view = view;
	}

	public TextView getLeftText() {
		return leftText;
	}


	public void setLeftText(TextView leftText) {
		this.leftText = leftText;
	}


	public ImageView getLeftImage() {
		return leftImage;
	}


	public void setLeftImage(ImageView leftImage) {
		this.leftImage = leftImage;
	}


	public TextView getTitle() {
		return title;
	}


	public void setTitle(TextView title) {
		this.title = title;
	}


	public ImageView getRightImage() {
		return rightImage;
	}


	public void setRightImage(ImageView rightImage) {
		this.rightImage = rightImage;
	}


	public Button getRightButton() {
		return rightButton;
	}


	public void setRightButton(Button rightButton) {
		this.rightButton = rightButton;
	}
	
}
