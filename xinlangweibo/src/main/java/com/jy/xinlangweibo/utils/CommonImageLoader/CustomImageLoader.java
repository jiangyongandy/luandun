package com.jy.xinlangweibo.utils.CommonImageLoader;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.jiang.library.Component.ImageLoader.DisplayImage;

/**
 * Created by JIANG on 2016/9/10.
 */
public class CustomImageLoader {
    private static DisplayImage displayImage;

    public static DisplayImage getImageLoader() {
        if(displayImage == null)
        synchronized (CustomImageLoader.class){
            displayImage = new GlideImageLoader();
        }
        return displayImage;
    }

    public static void displayImage(Activity activity, ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height) {
        getImageLoader().displayImage(activity, imageView, path, loadingResId, failResId, width, height);
    }
}
