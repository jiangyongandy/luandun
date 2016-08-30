package com.jy.xinlangweibo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by JIANG on 2016/8/28.
 */
public class ImageUtils {

    /**
     * 使VIEW的宽度或高度不超过maxImage，并且保持原图宽高比例
     * @param url
     * @param mview
     * @param loadedImage
     * @param maxImage
     * @param imageLoader
     * @return
     */
    public static int matchView2Bitmap(String url, final View mview, Bitmap loadedImage, int maxImage, ImageLoader imageLoader) {
        Log.i("statusadapter",
                "宽度：" + String.valueOf(loadedImage.getWidth()));
        Log.i("statusadapter",
                "高度：" + String.valueOf(loadedImage.getHeight()));
        int CHANGTU = 1;
        ViewGroup.LayoutParams lp = mview.getLayoutParams();
        if(loadedImage.getHeight()>1024||loadedImage.getWidth() > 1024) {
            float scale = 1.5f;
            lp.width = Utils.dip2px(mview.getContext(), maxImage);
            lp.height = (int) (Utils.dip2px(mview.getContext(), maxImage)*scale+0.5f);
            ImageSize mImageSize = new ImageSize(loadedImage.getWidth(), (int)(loadedImage.getWidth()*scale+0.5f));
            ImageLoader.getInstance().loadImage(url, mImageSize, new SimpleImageLoadingListener(){

                @Override
                public void onLoadingComplete(String imageUri, View view,
                                              Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    ((ImageView)mview).setImageBitmap(loadedImage);
                }

            });
            return CHANGTU;
        }
        int max = Math.max(loadedImage.getWidth(),  loadedImage.getHeight());
        if( max> Utils.dip2px(mview.getContext(), maxImage) ) {
            if(loadedImage.getWidth() >= max) {
                lp.width = Utils.dip2px(mview.getContext(), maxImage);
                lp.height = Utils.dip2px(mview.getContext(), maxImage)*loadedImage.getHeight()/loadedImage.getWidth();
            }else {
                lp.height = Utils.dip2px(mview.getContext(), maxImage);
                lp.width = Utils.dip2px(mview.getContext(), maxImage)*loadedImage.getWidth()/loadedImage.getHeight();
            }
        } else {
            lp.height = loadedImage.getHeight();
            lp.width = loadedImage.getWidth();
        }
        return 0;
    }

    public static int isLargeImage(Bitmap loadedImage) {
        int CHANGTU = 1;
        if(loadedImage.getHeight()>1024||loadedImage.getWidth() > 1024) {
            return CHANGTU;
        }
        return 0;
    }

    public static int isLargeScreenImage(Bitmap loadedImage, Context context) {
        int largeHeight = 1;
        int largeWidth = 2;
        if(loadedImage.getHeight()> Utils.getDisplayHeightPixelsPixels(context)) {
            Logger.showLog(""+loadedImage.getHeight()+"      "+Utils.getDisplayHeightPixelsPixels(context),"onLoadingComplete");
            return largeHeight;
        }else if(loadedImage.getWidth() > Utils.getDisplayWidthPixels(context)) {
            return largeWidth;
        }
        return 0;
    }

    /**
     * 按照一定的宽高比例裁剪图片
     * @param bitmap 要裁剪的图片
     * @param num1 长边的比例
     * @param num2 短边的比例
     * @param isRecycled 是否回收原图片
     * @return 裁剪后的图片
     */
    public static Bitmap  imageCrop(Bitmap bitmap, int num1, int num2, boolean isRecycled){
        if (bitmap == null){
            return null;
        }
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int retX, retY;
        int nw, nh;
        if (w > h){
            if (h > w * num2 / num1){
                nw = w;
                nh = w * num2 / num1;
                retX = 0;
                retY = (h - nh) / 2;
            } else{
                nw = h * num1 / num2;
                nh = h;
                retX = (w - nw) / 2;
                retY = 0;
            }
        } else{
            if (w > h * num2 / num1){
                nh = h;
                nw = h * num2 / num1;
                retY = 0;
                retX = (w - nw) / 2;
            } else{
                nh = w * num1 / num2;
                nw = w;
                retY = (h - nh) / 2;
                retX = 0;}
        }
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,false);
        if (isRecycled && bitmap != null && !bitmap.equals(bmp)&& !bitmap.isRecycled()){
            bitmap.recycle();//回收原图片
            bitmap = null;
        }
        return bmp;
    }
}
