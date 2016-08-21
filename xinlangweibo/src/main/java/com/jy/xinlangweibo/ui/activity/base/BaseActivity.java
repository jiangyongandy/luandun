package com.jy.xinlangweibo.ui.activity.base;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.jy.xinlangweibo.AppSetting;
import com.jy.xinlangweibo.utils.ACache;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.ThemeUtils;
import com.jy.xinlangweibo.utils.ToastUtils;

public class BaseActivity extends Activity {

	protected String tag;
	protected ACache mCache;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tag = this.getClass().getSimpleName();

		setTheme(ThemeUtils.themeArr[AppSetting.getThemeColor()][0]);

//		得到缓存
		mCache = ACache.get(this);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//使得侧边栏顶端不被statusbar覆盖  这里的写法是让状态栏透明
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
			localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
		}
	}

	public void reload() {
		Intent intent = this.getIntent();
		this.overridePendingTransition(0, 0);
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
