package com.jy.xinlangweibo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.jy.xinlangweibo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

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

	public static DisplayImageOptions getNoDownScalingRoundIvOption(Context context) {
		DisplayImageOptions option = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.NONE)
				.displayer(new RoundedBitmapDisplayer(Utils.dip2px(context, 4)))
				.showImageForEmptyUri(R.drawable.timeline_image_failure)
				.showStubImage(R.drawable.timeline_image_loading)
				.showImageOnFail(R.drawable.timeline_image_failure).build();
		return option;
	}
	public static DisplayImageOptions getNoDownScalingIvOption(Context context) {
		DisplayImageOptions option = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.NONE)
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

	public static class CutBitmapDisplayer implements BitmapDisplayer {

		@Override
		public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
			if(!(imageAware instanceof ImageViewAware)) {
				throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
			} else {
				imageAware.setImageDrawable(new BitmapDrawable(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),1000)));
			}
		}
	}
}
