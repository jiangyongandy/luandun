package com.jy.xinlangweibo.ui.activity.base;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.xinlangweibo.AppSetting;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.constant.AccessTokenKeeper;
import com.jy.xinlangweibo.presenter.BasePresenter;
import com.jy.xinlangweibo.utils.ACache;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.ThemeUtils;
import com.jy.xinlangweibo.utils.ToastUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseActivity extends SwipeBackActivity {

	protected String tag;
	protected ACache mCache;
	protected int theme;
	protected Toolbar mToolbar;
	protected BasePresenter presenter;
	protected Oauth2AccessToken accessToken;

	private View loadingView;
	private ViewGroup parentView;
	private int viewIndex;
	private ViewGroup.LayoutParams params;
	private View currentView;
	private Unbinder bind;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tag = this.getClass().getSimpleName();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setTheme(ThemeUtils.themeArr[AppSetting.getThemeColor()][0]);
		this.theme = AppSetting.getThemeColor();
		changeTheme();
		//		得到缓存组件
		mCache = ACache.get(this);
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
		if(bind != null)
			bind.unbind();
		Logger.showLog(""+this,"activity onDestroy");
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
			getSupportActionBar().setDisplayShowTitleEnabled(false);
		}
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if(layoutResID == R.layout.bga_pp_toolbar_viewstub)
			return;
		bind = ButterKnife.bind(this);
		loadingView = getLoadingTargetView();
	}

	public void changeTheme() {
		//		此段代码修复引入侧滑结束activity 状态栏主题换色的问题
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

	public Oauth2AccessToken getAccessAccessToken(){
		accessToken = AccessTokenKeeper.readAccessToken(this);
		return accessToken;
	}

	/**
	 * 跳转到另一Activity的方法
	 * 
	 * @param tar
	 */
	public void intent2Activity(Class<?extends Activity> tar) {
		startActivity(new Intent(this, tar));
	}

	public void startActivityForResult(Class<?extends Activity> tar,int requsetCode) {
		startActivityForResult(new Intent(this,tar),requsetCode);
	}

	public void finishForResult(int resultCode) {
		Intent intent = new Intent();
		setResult(resultCode, intent);
		finish();
	}

	public void showToast(String text) {
		ToastUtils.show(this, text, Toast.LENGTH_SHORT);
	}

	public void showLog(String msg) {
		Logger.showLog(msg, tag);
	}

	protected void loadData() {}

	protected View getLoadingTargetView() {
		return null;
	}

	protected void restoreLayout() {
		showLayout(loadingView);
	}

	protected void showLoading() {
		String msg = "";
		View layout = LayoutInflater.from(loadingView.getContext()).inflate(R.layout.loading, null);
		if (!msg.isEmpty()) {
			TextView textView = (TextView) layout.findViewById(R.id.loading_msg);
			textView.setText(msg);
		}
		showLayout(layout);
	}

	protected void showErrorMessage() {
		String msg = "";
		View layout = LayoutInflater.from(loadingView.getContext()).inflate(R.layout.message, null);
		if (!msg.isEmpty()) {
			TextView textView = (TextView) layout.findViewById(R.id.message_info);
			textView.setText(msg);
		}
		showLayout(layout);
		layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadData();
			}
		});
	}

	private void showLayout(View layout) {
		if (parentView == null) {
			params = loadingView.getLayoutParams();
			if (loadingView.getParent() != null) {
				parentView = (ViewGroup) loadingView.getParent();
			} else {
				//android.R.id.content is frameLayout
				parentView = (ViewGroup) loadingView.getRootView().findViewById(android.R.id.content);
			}
			int count = parentView.getChildCount();
			for (int index = 0; index < count; index++) {
				if (loadingView == parentView.getChildAt(index)) {
					viewIndex = index;
					break;
				}
			}
			currentView = loadingView;
		}
		this.currentView = layout;
		// 如果已经是那个view，那就不需要再进行替换操作了
		if (parentView.getChildAt(viewIndex) != layout) {
			ViewGroup parent = (ViewGroup) layout.getParent();
			if (parent != null) {
				parent.removeView(layout);
			}
			parentView.removeViewAt(viewIndex);
			parentView.addView(layout, viewIndex, params);
		}
	}
}
