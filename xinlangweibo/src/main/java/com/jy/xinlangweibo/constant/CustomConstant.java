package com.jy.xinlangweibo.constant;

import android.content.Context;

public class CustomConstant {
	private static Context context;
	
	public  static  Context getContext() {
		return context;
	}

	public static void setContext(Context con) {
		context = con;
	}

	public static final boolean ifShowLog = true;
}
