package com.jy.xinlangweibo.utils.CommonImageLoader;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiang.library.Component.ImageLoader.DisplayImage;

/**
 * Created by JIANG on 2016/9/10.
 */
public class GlideImageLoader extends DisplayImage {

    @Override
    public void displayImage(Activity activity, ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height) {
        final String finalPath = getPath(path);
        Glide.with(activity).load(finalPath).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(loadingResId).error(failResId).override(width, height).into(imageView);
    }
}
