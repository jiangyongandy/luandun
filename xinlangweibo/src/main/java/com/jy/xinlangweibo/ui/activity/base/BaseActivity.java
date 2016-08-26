package com.jy.xinlangweibo.ui.activity.base;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.jy.xinlangweibo.AppSetting;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.utils.ACache;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.ThemeUtils;
import com.jy.xinlangweibo.utils.ToastUtils;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {

	protected String tag;
	protected ACache mCache;
	protected int theme;
	protected Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tag = this.getClass().getSimpleName();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setTheme(ThemeUtils.themeArr[AppSetting.getThemeColor()][0]);
		this.theme = AppSetting.getThemeColor();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(this.theme != AppSetting.getThemeColor()) {
			reload();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void finish() {
		super.finish();
	}

	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		this.mToolbar = (Toolbar)this.findViewById(R.id.toolbar);
		if(this.mToolbar != null) {
			this.setSupportActionBar(this.mToolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(false);
		}
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		ButterKnife.bind(this);
	}

	public Toolbar getToolbar() {
		return this.mToolbar;
	}

	public void reload() {
		Intent intent = this.getIntent();
//		重启activity,取消activity切换动画，平滑过渡
		this.overridePendingTransition(0, 0);
//		FLAG_ACTIVITY_NO_ANIMATION
		intent.addFlags(65536);
		this.finish();
		this.overridePendingTransition(0, 0);
		this.startActivity(intent);
	}

	/**
	 * 跳转到另一Activity的方法
	 * 
	 * @param tar
	 */
	public void intent2Activity(Class<?extends Activity> tar) {
		startActivity(new Intent(this, tar));
	}

	public void showToast(String text) {
		ToastUtils.show(this, text, Toast.LENGTH_SHORT);
	}

	public void showLog(String msg) {
		Logger.showLog(msg, tag);
	}
}
