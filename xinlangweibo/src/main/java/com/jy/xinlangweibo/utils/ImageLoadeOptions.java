package com.jy.xinlangweibo.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.jy.xinlangweibo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ImageLoadeOptions {
	public static DisplayImageOptions getIvHeadOption() {
		DisplayImageOptions option = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showImageForEmptyUri(R.drawable.avatar_default)
				.showStubImage(R.drawable.avatar_default)
				.showImageOnFail(R.drawable.avatar_default)
				.displayer(new RoundedBitmapDisplayer(999)).build();
		return option;
	}

	public static DisplayImageOptions getDefaultIvOption(Context context) {
		DisplayImageOptions option = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showImageForEmptyUri(R.drawable.timeline_image_failure)
				.showStubImage(R.drawable.timeline_image_loading)
				.showImageOnFail(R.drawable.timeline_image_failure).build();
		return option;
	}

	public static DisplayImageOptions getCommonIvOption(Context context) {
		DisplayImageOptions option = new DisplayImageOptions.Builder()
				.cacheInMemory()
				.cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showImageForEmptyUri(R.drawable.timeline_image_failure)
				.showStubImage(R.drawable.timeline_image_loading)
				.showImageOnFail(R.drawable.timeline_image_failure)
				.displayer(new RoundedBitmapDisplayer(Utils.dip2px(context, 4)))
				.build();
		return option;
	}
}
