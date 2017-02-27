package com.jy.xinlangweibo;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatDelegate;

import com.jy.xinlangweibo.constant.AccessTokenKeeper;
import com.jy.xinlangweibo.share.shareutil.ShareConfig;
import com.jy.xinlangweibo.share.shareutil.ShareManager;
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
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import static com.jy.xinlangweibo.constant.Constants.FILE_STORAGE;

public class BaseApplication extends Application {

	private static final String QQ_ID = "1105005431";
	private static final String WX_ID = "";
	private static final String WEIBO_ID = "3269135043";
	private static BaseApplication application;
	public Random random;

	public static BaseApplication getInstance() {
		return application;
	}

	public static RefWatcher getRefWatcher(Context context) {
		BaseApplication application = (BaseApplication) context.getApplicationContext();
		return application.refWatcher;
	}

	private RefWatcher refWatcher;

	static
	{
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");  
		application = this;
		initImageLoader(this);
		initShare();
		MobclickAgent.setScenarioType(application ,MobclickAgent.EScenarioType.E_UM_NORMAL );
		refWatcher = LeakCanary.install(this);

		LogUtil.enableLog();
		random = new Random();
		//todo 正式环境应该加上
//		Thread.setDefaultUncaughtExceptionHandler(restartHandler);
	}

	private void initShare() {
		ShareConfig config = ShareConfig.instance()
				.qqId(QQ_ID)
				.wxId(WX_ID)
				.weiboId(WEIBO_ID);
				// 下面两个，如果不需要登录功能，可不填写
//				.weiboRedirectUrl(REDIRECT_URL)
//				.wxSecret(WX_ID);
		ShareManager.init(config);
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

	private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {

		@Override
		public void uncaughtException(Thread arg0, Throwable arg1) {
			//report.record(arg1);
//			new WriteUncaughtExceptionReport().execute(arg1);
			PackageInfo info = null;
			try {
				info =getPackageManager().getPackageInfo(getPackageName(), 0);
			} catch (PackageManager.NameNotFoundException e1) {
				e1.printStackTrace();
			}
			if (info != null) {
				String versionName = info.versionName;
				StringBuilder sb = new StringBuilder();
				Calendar calendar = new GregorianCalendar();
				Date date = new Date();
				calendar.setTime(date);
				sb.append(calendar.get(Calendar.YEAR));
				sb.append("-");
				sb.append(calendar.get(Calendar.MONTH) + 1);
				sb.append("-");
				sb.append(calendar.get(Calendar.DAY_OF_MONTH));
				sb.append("\t");
				sb.append(calendar.get(Calendar.HOUR_OF_DAY));
				sb.append(":");
				sb.append(calendar.get(Calendar.MINUTE));
				sb.append(":");
				sb.append(calendar.get(Calendar.SECOND));

				File errorFile = new File(FILE_STORAGE,"error_report.txt");
				BufferedOutputStream os = null;
				try {
					os = new BufferedOutputStream(new FileOutputStream(errorFile,true));
					os.write("/*****************************************************************/\r\n".getBytes());//每条记录的头部
					//应用的版本
					os.write("Application Version:\t".getBytes());
					os.write(versionName.getBytes());
					os.write("\r\n".getBytes());
					//应用崩溃时间
					os.write("Application Crash Date:\t".getBytes());
					os.write(sb.toString().getBytes());
					os.write("\r\n".getBytes());
//					//应用崩溃的具体记录
//					String detail = stackTrace.replace("\n", "\r\n");//剪linux下的换行换成window下的换行
					os.write("Crash Detail:\r\n".getBytes());
					//os.write(detail.getBytes());
					PrintStream ps = new PrintStream(os);
					arg1.printStackTrace(ps);
					ps.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally {
					if (os != null)
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
			arg1.printStackTrace();
		}
	};

}
