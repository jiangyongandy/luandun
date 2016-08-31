package com.jy.xinlangweibo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.opengl.GLES10;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jy.xinlangweibo.utils.Logger;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by JIANG on 2016/8/31.
 */
public class CutImageView extends ImageView {
    private BitmapRegionDecoder mDecoder;
    private Rect bsrc;

    public CutImageView(Context context) {
        super(context);
        init();
    }

    public CutImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        bsrc = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    @Override
    public void setImageBitmap(final Bitmap bm) {
            Logger.showLog("------------"+bm.getHeight(),"setimageBitmap-----");
            super.setImageBitmap(bm);
    }

    @Override
    public void setImageDrawable(final Drawable drawable) {
            Logger.showLog("------------"+drawable.getIntrinsicHeight(),"setImageDrawable");
            super.setImageDrawable(drawable);
    }

    public static class ImageUtils {
        private static final int DEFAULT_MAX_BITMAP_DIMENSION = 2048;
        private static int maxHeight = 0;

        static {
            int[] maxTextureSize = new int[1];
            GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
            maxHeight = Math.max(maxTextureSize[0], DEFAULT_MAX_BITMAP_DIMENSION);
        }

        public static int getMaxHeight() {
            return maxHeight;
        }
    }
}
