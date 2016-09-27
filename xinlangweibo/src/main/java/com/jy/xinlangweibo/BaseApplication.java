package com.jy.xinlangweibo;

import android.app.Application;
import android.content.Context;

import com.jy.xinlangweibo.constant.AccessTokenKeeper;
import com.jy.xinlangweibo.utils.CommonImageLoader.ImageLoadeOptions;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.utils.LogUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;

public class BaseApplication extends Application {

	private static BaseApplication application;
	public static BaseApplication getInstance() {
		return application;
	}

	public static RefWatcher getRefWatcher(Context context) {
		BaseApplication application = (BaseApplication) context.getApplicationContext();
		return application.refWatcher;
	}

	private RefWatcher refWatcher;

	@Override
	public void onCreate() {
		super.onCreate();
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");  
		application = this;
		initImageLoader(this);
		refWatcher = LeakCanary.install(this);

		LogUtil.enableLog();
	}

//	此方法在应用销毁时不一定会被调用，比如当程序是被内核终止以便为其他应用程序释放资源，那
//	么将不会提醒，并且不调用应用程序的对象的onTerminate方法而直接终止进程
	@Override
	public void onTerminate() {
		super.onTerminate();
		application = null;
	}

	public Oauth2AccessToken getAccessAccessToken(){
		return AccessTokenKeeper.readAccessToken(this);
	}

	// 初始化图片处理
	private void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.diskCacheFileCount(300)
				.defaultDisplayImageOptions(ImageLoadeOptions.getDefaultIvOption(getApplicationContext()))
				.build();
		// Initialize CustomImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

}
