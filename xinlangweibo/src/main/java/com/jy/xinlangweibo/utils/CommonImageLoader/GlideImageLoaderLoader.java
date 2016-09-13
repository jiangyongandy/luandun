package com.jy.xinlangweibo.utils.CommonImageLoader;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.jiang.library.Component.ImageLoader.ImageLoader;

/**
 * Created by JIANG on 2016/9/10.
 */
public class GlideImageLoaderLoader extends ImageLoader {

    @Override
    public void displayImage(Activity activity, final ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height) {
        final String finalPath = getPath(path);
        Glide.with(activity).load(finalPath).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(loadingResId).error(failResId).override(width, height).into(imageView);
    }
    public void displayGifImage(Activity activity, final ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height) {
        final String finalPath = getPath(path);
        Glide.with(activity).load(finalPath).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(loadingResId).error(failResId).override(width, height).into(imageView);
    }

    @Override
    public void downloadImage(Context context, String path) {

    }

    public <Y extends Target> void downloadImage(Context context, String path,Y target) {
        final String finalPath = getPath(path);
        Glide.with(context.getApplicationContext()).load(finalPath).asBitmap().into(target);
    }
}
