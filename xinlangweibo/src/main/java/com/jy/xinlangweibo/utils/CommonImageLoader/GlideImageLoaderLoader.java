package com.jy.xinlangweibo.utils.CommonImageLoader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jiang.library.Component.ImageLoader.ImageLoader;
import com.jy.xinlangweibo.AppSetting;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.utils.Logger;

/**
 * Created by JIANG on 2016/9/10.
 */
public class GlideImageLoaderLoader extends ImageLoader {

    @Override
    public void displayImage(Context activity, final ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height) {
        final String finalPath = getPath(path);
//todo 处理本地路径
        if(AppSetting.isNoImage()) {

            Glide.with(activity)
                    .load(R.drawable.timeline_image_loading)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .dontAnimate()
                    .into(imageView);

        }else {

            if(width == 0|| height == 0) {

                Glide.with(activity).load(finalPath).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(loadingResId).error(failResId)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        Logger.showLog("图片加载异常","图片加载异常");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Logger.showLog("图片加载成功","图片加载成功");
                        return false;
                    }
                }).into(imageView);
                return;
            }
            Glide.with(activity).load(finalPath).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(loadingResId).error(failResId).override(width, height).into(imageView);
        }

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

    @Override
    public void pause(Activity activity) {
        Glide.with(activity).pauseRequests();
    }

    @Override
    public void resume(Activity activity) {
        Glide.with(activity).resumeRequestsRecursive();
    }
}
