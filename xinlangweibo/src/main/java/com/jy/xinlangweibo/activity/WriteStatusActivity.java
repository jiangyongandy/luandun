package com.jy.xinlangweibo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.api.MyWeiboapi;
import com.jy.xinlangweibo.api.SimpleRequestlistener;
import com.jy.xinlangweibo.base.BaseActivity;
import com.jy.xinlangweibo.constant.AccessTokenKeeper;
import com.jy.xinlangweibo.constant.Constants;
import com.jy.xinlangweibo.utils.EmoticonsUtils;
import com.jy.xinlangweibo.utils.TitleBuilder;
import com.jy.xinlangweibo.utils.ToastUtils;
import com.jy.xinlangweibo.widget.EmoticonsEditText;
import com.jy.xinlangweibo.widget.EmoticonsEditText.OnTextChangedInterface;
import com.jy.xinlangweibo.widget.EmoticonsKeyBoardBar;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.Status;

public class WriteStatusActivity extends BaseActivity implements
		OnClickListener, OnTextChangedInterface {

	private static final String SHARE_PREFERENCE_NAME = "com.jy.xinlangweibo";
    private static final String SHARE_PREFERENCE_TAG = "soft_input_height";
	private Status retweeted_status;
	private SharedPreferences sp;
	private InputMethodManager imm;
	private ImageView iv_emotions;
	private ImageView iv_add;
	private View iv_picture;
	private View iv_at;
	private View iv_topic;
	private EmoticonsEditText et_content;
	private EmoticonsKeyBoardBar EmoticonsKeyBoardBar;
	private TitleBuilder titleBuilder;
	private ProgressDialog progressDialog;
	private View mContentView;
	private Handler handler = new Handler() ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_writestatus);

		// 获得转发原微博内容
		retweeted_status = (Status) getIntent().getSerializableExtra("status");

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		sp = this.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
		//oncreate 方法中（在这个方法中界面还未显示） 获取软键盘高度？ 
		int softInputHeight = sp.getInt(SHARE_PREFERENCE_TAG, 400);
		if(softInputHeight == 400) {
			System.out.println("默认值 键盘高度未获取到");
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					getSupportSoftInputHeight();
				}
			}, 200);
		}
		initView();
	}

	private void initView() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("微博发表中...");
		initTitle();

		initWeiboContent();

		initBottombar();

		initEmotions();
	}

	private void initEmotions() {
		EmoticonsKeyBoardBar = (EmoticonsKeyBoardBar) findViewById(R.id.EmoticonsKeyBoardBar);
		mContentView =  findViewById(R.id.content_view);
		// 设置与表情面板匹配的EmoticonsEditText
		EmoticonsKeyBoardBar.setEt_chat(et_content);
		// 从数据库中取出表情属性集（包括路径，对应的字符等）并把该List设置给EmoticonsKeyBoardBar
		EmoticonsKeyBoardBar.setBuilder(EmoticonsUtils.getBuilder(this));
	}

	private void initBottombar() {
		iv_picture = findViewById(R.id.iv_picture);
		iv_at = findViewById(R.id.iv_at);
		iv_topic = findViewById(R.id.iv_topic);
		iv_emotions = (ImageView) findViewById(R.id.iv_emotions);
		iv_add = (ImageView) findViewById(R.id.iv_add);

		iv_picture.setOnClickListener(this);
		iv_at.setOnClickListener(this);
		iv_topic.setOnClickListener(this);
		iv_emotions.setOnClickListener(this);
		iv_add.setOnClickListener(this);
	}

	private void initWeiboContent() {
		et_content = (EmoticonsEditText) findViewById(R.id.et_content);
		et_content.setOnTextChangedInterface(this);
		et_content.setOnClickListener(this);
		if (retweeted_status != null) {
			View rg_card_status = findViewById(R.id.rg_card_status);
			rg_card_status.setVisibility(View.VISIBLE);
			ImageView iv_card_status = (ImageView) findViewById(R.id.iv_card_status);
			if (TextUtils.isEmpty(retweeted_status.thumbnail_pic)) {
				iv_card_status.setVisibility(View.GONE);
			}
			TextView tv_card_status = (TextView) findViewById(R.id.tv_card_status);
			tv_card_status.setText(retweeted_status.text);
		}
	}

	private void initTitle() {
		titleBuilder = new TitleBuilder(this)
				.setTitle("发表微博")
				.setLeftimg(R.drawable.icon_navbarback_gra2ora_sel)
				.setrightButton("发送", R.drawable.timeline_card_small_button,
						R.color.bg_gray_pressed).setRightOnclickListener(this)
				.setLeftOnclickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						WriteStatusActivity.this.finish();
					}
				});
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (EmoticonsKeyBoardBar != null && EmoticonsKeyBoardBar.isShown()) {
				iv_emotions.setImageResource(R.drawable.btn_insert_emotion);
				EmoticonsKeyBoardBar.setVisibility(View.GONE);
				return true;
			} else {
				return super.dispatchKeyEvent(event);
			}
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_picture:

			break;
		case R.id.iv_at:

			break;
		case R.id.iv_topic:

			break;
		case R.id.iv_emotions:    
			if (EmoticonsKeyBoardBar.getVisibility() == View.VISIBLE) {
				// 显示表情面板时点击,将按钮图片设为笑脸按钮,同时隐藏面板
				iv_emotions.setImageResource(R.drawable.btn_insert_emotion);
//				EmoticonsKeyBoardBar.setVisibility(View.GONE);
//				getWindow().setSoftInputMode(
//						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//				imm.showSoftInput(et_content, InputMethodManager.SHOW_IMPLICIT);
				
				lockContentHeight();   //固定内容布局的位置  通过固定布局高度实现（键盘的RESIZE模式只对 内容布局不是固定高度有效？）
                hideEmotionLayout(true);
                unlockContentHeightDelayed();
				
			} else {
				// 未显示表情面板时点击,将按钮图片设为键盘,同时显示面板
				iv_emotions.setImageResource(R.drawable.btn_insert_keyboard);
//				getWindow().setSoftInputMode(
//						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//				imm.hideSoftInputFromWindow(et_content.getWindowToken(),
//						InputMethodManager.HIDE_NOT_ALWAYS); // 强制隐藏键盘
//				EmoticonsKeyBoardBar.setVisibility(View.VISIBLE);
				
                if (isSoftInputShown()) {
                    lockContentHeight();
                    showEmotionLayout();
                    unlockContentHeightDelayed();
                } else {
                    showEmotionLayout();
                }
				
			}
			break;
		case R.id.iv_add:

			break;
		case R.id.et_content:
			if (EmoticonsKeyBoardBar.getVisibility() == View.VISIBLE) {
				EmoticonsKeyBoardBar.setVisibility(View.GONE);
			}
			break;
		case R.id.nav_right_text:
			if (!TextUtils.isEmpty(et_content.getText().toString())) {
				Oauth2AccessToken readAccessToken = AccessTokenKeeper
						.readAccessToken(this);
				MyWeiboapi api = new MyWeiboapi(this, Constants.APP_KEY,
						readAccessToken);
				progressDialog.show();
				api.update(et_content.getText().toString(), null, null,
						new SimpleRequestlistener(this, progressDialog) {
							@Override
							public void onComplete(String arg0) {
								super.onComplete(arg0);
								ToastUtils.show(WriteStatusActivity.this,
										"发表微博成功", Toast.LENGTH_SHORT);
								WriteStatusActivity.this.finish();
							}
						});
			} else {
				ToastUtils.show(this, "发送内容不能为空", Toast.LENGTH_SHORT);
			}
			break;

		}
	}

    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }
	
    private void showSoftInput() {
    	et_content.requestFocus();
    	et_content.post(new Runnable() {
            @Override
            public void run() {
            	imm.showSoftInput(et_content, 0);
            }
        });
    }

    private void hideSoftInput() {
    	imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
    }
	
	
	 private void showEmotionLayout() {
	        int softInputHeight = getSupportSoftInputHeight();
	        if (softInputHeight == 0) {
	            softInputHeight = sp.getInt(SHARE_PREFERENCE_TAG, 400);
	        }
	        hideSoftInput();
	        EmoticonsKeyBoardBar.getLayoutParams().height = softInputHeight;
	        EmoticonsKeyBoardBar.setVisibility(View.VISIBLE);
	    }

	    private void hideEmotionLayout(boolean showSoftInput) {
	        if (EmoticonsKeyBoardBar.isShown()) {
	        	EmoticonsKeyBoardBar.setVisibility(View.GONE);
	            if (showSoftInput) {
	                showSoftInput();
	            }
	        }
	    }
	
	
	private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        params.height = mContentView.getHeight();
        params.weight = 0.0F;
    }
	
    private void unlockContentHeightDelayed() {
    	mContentView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
            }
        }, 200L); //延迟200  留给键盘隐藏的时间  防止表情键盘挤到界面上面去
    }
	
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        int screenHeight = this.getWindow().getDecorView().getRootView().getHeight();
        int softInputHeight = screenHeight - r.bottom;
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }
        if (softInputHeight < 0) {
            Log.w("EmotionInputDetector", "Warning: value of softInputHeight is below zero!");
        }
        if (softInputHeight > 0) {
            sp.edit().putInt(SHARE_PREFERENCE_TAG, softInputHeight).apply();
        }
        return softInputHeight;
    }

    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        	this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        	int usableHeight = metrics.heightPixels;
        	this.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        	int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }
    
	
	@Override
	public void onTextChanged(final CharSequence argo) {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				if (!TextUtils.isEmpty(argo)) {
					et_content.setTextColor(getResources().getColor(
							R.color.black));
					titleBuilder.setrightButton("发送",
							R.drawable.backgro_tabbarcenter_gra2ora_sel,
							R.color.white);
				} else {
					et_content.setTextColor(getResources().getColor(
							R.color.txt_light_gray));
					titleBuilder.setrightButton("发送",
							R.drawable.timeline_card_small_button,
							R.color.bg_gray_pressed);
				}
			}
		});
	}
}
