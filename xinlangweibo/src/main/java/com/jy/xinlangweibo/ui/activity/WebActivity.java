package com.jy.xinlangweibo.ui.activity;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;

public class WebActivity extends BaseActivity {
	// webview
	private WebView wv;
	private ProgressBar pgb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		// 提取链接
		final String website = getIntent().getStringExtra("Website");
		System.out.println("" + website);
		if (null == website) {
			showToast("网址：" + website + "   出错了");
			finish();
		}

		pgb = (ProgressBar) findViewById(R.id.pgb_wv);
		wv = (WebView) findViewById(R.id.wv);
		WebSettings webSettings = wv.getSettings();
//		webSettings.setDefaultTextEncodingName("UTF-8");
//		 //设置WebView 可以加载更多格式页面
		webSettings.setLoadWithOverviewMode(true);
////		设置WebView支持ViewPort标签
		webSettings.setUseWideViewPort(true);
////		支持手势
//		webSettings.setBuiltInZoomControls(true);
////		支持JS
		webSettings.setJavaScriptEnabled(true);

//		//告诉webview启用应用程序缓存api。
		webSettings.setAppCacheEnabled(true);
//		//设置是否启用了DOM storage API。
		webSettings.setDomStorageEnabled(true);
//		//自动打开窗口
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//		// 排版适应屏幕
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//		设置不加载网络图片
//		webSettings.setBlockNetworkImage(true);
		if (VERSION.SDK_INT < 18) {
			webSettings.setPluginState(PluginState.ON_DEMAND);
			webSettings.setRenderPriority(RenderPriority.HIGH);
        } 
		if (VERSION.SDK_INT >= 23) {
            webSettings.setOffscreenPreRaster(true);
        } 
//
//		DisplayMetrics metrics = new DisplayMetrics();  
//		  getWindowManager().getDefaultDisplay().getMetrics(metrics);  
//		  int mDensity = metrics.densityDpi;  
//		  Log.d("maomao", "densityDpi = " + mDensity);  
//		  if (mDensity == 240) {   
//		   webSettings.setDefaultZoom(ZoomDensity.FAR);  
//		  } else if (mDensity == 160) {  
//		     webSettings.setDefaultZoom(ZoomDensity.MEDIUM);  
//		  } else if(mDensity == 120) {  
//		   webSettings.setDefaultZoom(ZoomDensity.CLOSE);  
//		  }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){  
//		   webSettings.setDefaultZoom(ZoomDensity.FAR);   
//		  }else if (mDensity == DisplayMetrics.DENSITY_TV){  
//		   webSettings.setDefaultZoom(ZoomDensity.FAR);   
//		  }else{  
//		      webSettings.setDefaultZoom(ZoomDensity.MEDIUM);  
//		  }  
//		
		/**
		 * setAllowFileAccess 启用或禁止WebView访问文件数据 setBlockNetworkImage 是否显示网络图像
		 * setBuiltInZoomControls 设置是否支持缩放 setCacheMode 设置缓冲的模式
		 * setDefaultFontSize 设置默认的字体大小 setDefaultTextEncodingName 设置在解码时使用的默认编码
		 * setFixedFontFamily 设置固定使用的字体 setJavaSciptEnabled 设置是否支持Javascript
		 * setLayoutAlgorithm 设置布局方式 setLightTouchEnabled 设置用鼠标激活被选项
		 * setSupportZoom 设置是否支持变焦
		 * */

		wv.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				pgb.setProgress(progress);
			}
			
			
		});

		// 此段程序防止自动打开默认浏览器
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				showToast("打开网址：" + website + "   出错了");
				;
			}
		});
		wv.loadUrl(website);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		rootLayout.removeView(webView);  
		wv.destroy();  
	}
}
