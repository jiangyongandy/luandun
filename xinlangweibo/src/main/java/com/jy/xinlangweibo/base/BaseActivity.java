package com.jy.xinlangweibo.base;


import com.jy.xinlangweibo.utils.ACache;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends Activity {

	protected String tag;
	protected ACache mCache;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tag = this.getClass().getSimpleName();
		
//		得到缓存
		mCache = ACache.get(this);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * 跳转到另一Activity的方法
	 * 
	 * @param tar
	 */
	protected void intent2Activity(Class<?extends Activity> tar) {
		startActivity(new Intent(this, tar));
	}

	protected void showToast(String text) {
		ToastUtils.show(this, text, Toast.LENGTH_SHORT);
	}
	protected void showLog(String msg) {
		Logger.showLog(msg, tag);
	}
}
