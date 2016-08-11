package com.jy.xinlangweibo.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.base.BaseActivity;
import com.jy.xinlangweibo.constant.AccessTokenKeeper;
import com.jy.xinlangweibo.utils.EmoticonsUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class FirstActivity extends BaseActivity {
	protected static final int What_Intent2Login = 0;
	protected static final int What_Intent2Main = 1;
//	处理消息的间隔  即开始页的持续时间
	private static final long Duration = 1000; 
	
	private Oauth2AccessToken accessToken;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case What_Intent2Login:
				intent2Activity(LoginActivity.class);
//				intent2Activity(MainActivity.class);
				finish();
				break;
			case What_Intent2Main:
				intent2Activity(MainActivity.class);
				finish();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		
//		初始化表情数据库（把表情包创建成表存储在数据库中）这个操作只会执行一次 除非清空数据（shareference）
		EmoticonsUtils.initEmoticonsDB(getApplicationContext());
		
		accessToken = AccessTokenKeeper.readAccessToken(this);
//		这里只能验证shareference 中是否存在token 不能验证token 有效性
		if(accessToken.isSessionValid()) {
			handler.sendEmptyMessageDelayed(What_Intent2Main, Duration);
		}else {
			handler.sendEmptyMessageDelayed(What_Intent2Login, Duration);
		}
	}
}
