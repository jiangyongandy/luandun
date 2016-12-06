package com.jy.xinlangweibo.models.net.api;

import android.app.Activity;
import android.app.Dialog;

import com.jy.xinlangweibo.utils.Logger;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class SimpleRequestlistener implements RequestListener {
	private Activity context;
	private Dialog progressDialog;

	/**
	 * @param context
	 */
	public SimpleRequestlistener(Activity context,Dialog progressDialog) {
		this.context = context;
		this.progressDialog = progressDialog;
	}

	@Override
	public void onWeiboException(WeiboException arg0) {
		onAllDone();
		Logger.showLog(arg0.toString(), "request weiboexcetion");
	}

	@Override
	public void onComplete(String arg0) {
		onAllDone();
		Logger.showLog(arg0, "request on compelete"+context.getClass().getSimpleName());
	}
	
	protected void onAllDone() {
		if(progressDialog != null) {
			progressDialog.dismiss();
		}
	}
}