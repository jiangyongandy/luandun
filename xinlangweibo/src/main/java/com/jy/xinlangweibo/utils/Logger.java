package com.jy.xinlangweibo.utils;

import android.util.Log;

import com.jy.xinlangweibo.constant.CustomConstant;

public class Logger {
	public static void showLog(String msg, String tag) {
		if (!CustomConstant.ifShowLog) {
			return;
		}
		show(msg, Log.INFO, tag);
	}

	private static void show(String msg, int level, String tag) {
		switch (level) {
		case Log.VERBOSE:
			Log.v(tag, msg);
			break;
		case Log.DEBUG:
			Log.d(tag, msg);
			break;
		case Log.WARN:
			Log.w(tag, msg);
			break;
		case Log.ERROR:
			Log.e(tag, msg);
			break;
		default:
			Log.i(tag, msg);
			break;
		}
	}
}
