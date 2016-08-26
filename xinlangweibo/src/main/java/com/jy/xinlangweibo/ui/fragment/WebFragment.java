package com.jy.xinlangweibo.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.ui.fragment.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by JIANG on 2016/8/26.
 */
public class WebFragment extends BaseFragment {

    @BindView(R.id.pgb_wv)
    ProgressBar pgbWv;
    @BindView(R.id.wv)
    WebView wv;
    private String website;

    @Override
    protected int CreateView() {
        return R.layout.activity_web;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity.getSupportActionBar().setTitle("浏览器");
        // 提取链接
        website = activity.getIntent().getStringExtra("Website");
        activity.showLog("" + website);
        if (null == website) {
            activity.showToast("网址：" + website + "   出错了");
            activity.finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        setWebView();
        wv.loadUrl(website);
        return rootView;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
//		rootLayout.removeView(webView);
        wv.destroy();
    }

    private void setWebView() {
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
        if (Build.VERSION.SDK_INT < 18) {
            webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            webSettings.setOffscreenPreRaster(true);
        }
        wv.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    pgbWv.setVisibility(View.VISIBLE);
                } else if (progress == 100) {
                    pgbWv.setVisibility(View.GONE);
//                    invalidateOptionsMenu();
                }
                pgbWv.setProgress(progress);

                super.onProgressChanged(view, progress);
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
                activity.showToast("打开网址：" + website + "   出错了");
            }
        });
        /**
         * setAllowFileAccess 启用或禁止WebView访问文件数据 setBlockNetworkImage 是否显示网络图像
         * setBuiltInZoomControls 设置是否支持缩放 setCacheMode 设置缓冲的模式
         * setDefaultFontSize 设置默认的字体大小 setDefaultTextEncodingName 设置在解码时使用的默认编码
         * setFixedFontFamily 设置固定使用的字体 setJavaSciptEnabled 设置是否支持Javascript
         * setLayoutAlgorithm 设置布局方式 setLightTouchEnabled 设置用鼠标激活被选项
         * setSupportZoom 设置是否支持变焦
         * */
    }
}
