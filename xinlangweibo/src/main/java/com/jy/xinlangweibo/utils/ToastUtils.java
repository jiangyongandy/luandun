package com.jy.xinlangweibo.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	public static Toast mtoast;
	/**
	 * 显示Toast.
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void show(Context context, CharSequence text, int duration) {
		if (mtoast == null) {
			mtoast = Toast.makeText (context, text, duration);
		}else {
			mtoast.setText(text);
			mtoast.setDuration(duration);
		}
		mtoast.show();
	}

	public static void clearToast() {
		mtoast = null;
	}
}
