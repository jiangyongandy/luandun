package com.jy.xinlangweibo.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.constant.Constants;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.utils.ClipboardUtil;

import butterknife.BindView;

/**
 * Created by JIANG on 2017/1/5.
 */

public class BrowserActivity extends BaseActivity
{

    @BindView(R.id.pgb_wv)
    ProgressBar progressBar;

    @BindView(R.id.wv)
    WebView mWebView;

    private final Handler mHandler = new Handler();

    private String url, mTitle;

    private WebViewClientBase webViewClient = new WebViewClientBase();
    private WebChromeClientBase webChromeViewClient = new WebChromeClientBase();

    public static void launch(Activity activity, String url, String title)
    {

        Intent intent = new Intent(activity, BrowserActivity.class);
        intent.putExtra(Constants.EXTRA_URL, url);
        intent.putExtra(Constants.EXTRA_TITLE, title);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initViews(savedInstanceState);
    }

    public void initViews(Bundle savedInstanceState)
    {

        Intent intent = getIntent();
        if (intent != null)
        {
            url = intent.getStringExtra(Constants.EXTRA_URL);
            mTitle = intent.getStringExtra(Constants.EXTRA_TITLE);
        }

        initToolBar();
        setupWebView();
    }

    public void initToolBar()
    {
        setTitle(TextUtils.isEmpty(mTitle) ? "详情" : mTitle);
        mToolbar.setTitle(TextUtils.isEmpty(mTitle) ? "详情" : mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu_browser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_share:
                share();
                break;

            case R.id.menu_open:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;

            case R.id.menu_copy:
                ClipboardUtil.setText(BrowserActivity.this, url);
                showToast("已复制");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void share()
    {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "来自「哔哩哔哩」的分享:" + url);
        startActivity(Intent.createChooser(intent, mTitle));
    }


    @Override
    public void onBackPressed()
    {

        if (mWebView.canGoBack() && mWebView.copyBackForwardList().getSize() > 0
                && !mWebView.getUrl().equals(mWebView.copyBackForwardList()
                .getItemAtIndex(0).getOriginalUrl()))
        {
            mWebView.goBack();
        } else
        {
            finish();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView()
    {

//        progressBar.spin();

        final WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        mWebView.getSettings().setBlockNetworkImage(true);
        mWebView.setWebViewClient(webViewClient);
        mWebView.requestFocus(View.FOCUS_DOWN);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.setWebChromeClient(webChromeViewClient);
        mWebView.loadUrl(url);
    }

    public class WebChromeClientBase extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result)
        {

            AlertDialog.Builder b2 = new AlertDialog
                    .Builder(BrowserActivity.this)
                    .setTitle(R.string.app_name)
                    .setMessage(message)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            result.confirm();
                        }
                    });

            b2.setCancelable(false);
            b2.create();
            b2.show();
            return true;
        }

        public void onProgressChanged(WebView view, int progress) {
            if(progressBar == null)
                return;
            if (progress < 100) {
                progressBar.setVisibility(View.VISIBLE);
            } else if (progress == 100) {
                progressBar.setVisibility(View.GONE);
//                    invalidateOptionsMenu();
            }
            progressBar.setProgress(progress);

            super.onProgressChanged(view, progress);
        }

    }

    public class WebViewClientBase extends WebViewClient
    {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {

            super.onPageFinished(view, url);
            mWebView.getSettings().setBlockNetworkImage(false);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
        {

            super.onReceivedError(view, request, error);
            String errorHtml = "<html><body><h2>找不到网页</h2></body></html>";
            view.loadDataWithBaseURL(null, errorHtml, "text/html", "UTF-8", null);
        }
    }

    @Override
    protected void onPause()
    {

        mWebView.reload();
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {

        mWebView.destroy();
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
