package com.jy.xinlangweibo.utils;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by JIANG on 2016/8/28.
 */
public class ImageUtils {

    public static int matchView2Bitmap(View view, Bitmap loadedImage, int maxImage) {
        int CHANGTU = 1;
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        Log.i("statusadapter",
                "宽度：" + String.valueOf(loadedImage.getWidth()));
        Log.i("statusadapter",
                "高度：" + String.valueOf(loadedImage.getHeight()));
        if(loadedImage.getHeight()/loadedImage.getWidth() >= 4) {
            lp.height = Utils.dip2px(view.getContext(), maxImage);
            lp.width = Utils.dip2px(view.getContext(), maxImage/2);
            return CHANGTU;
        }
        int max = Math.max(loadedImage.getWidth(),  loadedImage.getHeight());
        if( max> Utils.dip2px(view.getContext(), maxImage) ) {
            if(loadedImage.getWidth() >= max) {
                lp.width = Utils.dip2px(view.getContext(), maxImage);
                lp.height = Utils.dip2px(view.getContext(), maxImage)*loadedImage.getHeight()/loadedImage.getWidth();
            }else {
                lp.height = Utils.dip2px(view.getContext(), maxImage);
                lp.width = Utils.dip2px(view.getContext(), maxImage)*loadedImage.getWidth()/loadedImage.getHeight();
            }
        } else {
            lp.height = loadedImage.getHeight();
            lp.width = loadedImage.getWidth();
        }
        return 0;
    }
}
