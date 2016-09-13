package com.jy.xinlangweibo.utils.CommonImageLoader;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.jiang.library.Component.ImageLoader.ImageLoader;

/**
 * Created by JIANG on 2016/9/10.
 */
public class CustomImageLoader {
    private static ImageLoader imageLoader;

    public static ImageLoader getImageLoader() {
        if(imageLoader == null)
        synchronized (CustomImageLoader.class){
            imageLoader = new GlideImageLoaderLoader();
        }
        return imageLoader;
    }

    public static void displayImage(Activity activity, ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height) {
        getImageLoader().displayImage(activity, imageView, path, loadingResId, failResId, width, height);
    }

    public static void downloadImage(Context context, String path) {
        getImageLoader().downloadImage(context, path);
    }
}
