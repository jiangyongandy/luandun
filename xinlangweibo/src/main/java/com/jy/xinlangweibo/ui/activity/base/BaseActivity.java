package com.jy.xinlangweibo.ui.activity.base;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jy.xinlangweibo.AppSetting;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.presenter.BasePresenter;
import com.jy.xinlangweibo.utils.ACache;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.ThemeUtils;
import com.jy.xinlangweibo.utils.ToastUtils;

import butterknife.ButterKnife;

public class BaseActivity extends SwipeBackActivity {

	protected String tag;
	protected ACache mCache;
	protected int theme;
	protected Toolbar mToolbar;
	protected BasePresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tag = this.getClass().getSimpleName();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
					| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(ThemeUtils.getSecondThemeColor());   //这里动态修改颜色
		}
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
		ToastUtils.clearToast();
		if(presenter != null)
			presenter.onDestroy();
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
