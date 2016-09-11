package com.jiang.library.Component.ImageLoader;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

/**
 * Created by JIANG on 2016/9/10.
 */
public abstract class DisplayImage {

    protected String getPath(String path) {
        if (path == null) {
            path = "";
        }

        if (!path.startsWith("http") && !path.startsWith("file")) {
            path = "file://" + path;
        }
        return path;
    }

    public abstract void displayImage(Activity activity, ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height);

}
