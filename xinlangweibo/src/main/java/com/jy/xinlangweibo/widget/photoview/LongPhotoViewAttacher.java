package com.jy.xinlangweibo.widget.photoview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.jy.xinlangweibo.utils.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;


/**
 * Created by JIANG on 2016/9/2.
 */
public class LongPhotoViewAttacher extends PhotoViewAttacher {

    private final WeakReference<ImageView> mImageView;
    private Bitmap longBitmap;
    private PointF downPoint = new PointF();
    private PointF lastMovePoint = new PointF();
    private float deltay;
    private Rect bsrc = new Rect();
    private BitmapRegionDecoder mDecoder;
    private float imageScale;

    public LongPhotoViewAttacher(ImageView imageView) {
        super(imageView);
        mImageView = new WeakReference<>(imageView);
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        if (longBitmap != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downPoint.x = ev.getX();
                    downPoint.y = ev.getY();
                    lastMovePoint.y = ev.getY();
                case MotionEvent.ACTION_MOVE:
                    float dy = Math.abs((ev.getY() - downPoint.y));
                    float dx = Math.abs((ev.getX() - downPoint.x));
                    deltay = ev.getY() - lastMovePoint.y;
//                    if (Math.pow(dx, 2) / Math.pow(dy, 2) <= 3) { // 满足条件判断为// y方向上的滑动
//                        Logger.showLog("deltay--------------+"+deltay,"onTouch");
                        mImageView.get().setImageBitmap(cutBitmap(longBitmap,-deltay));
//                    }
                    lastMovePoint.y = ev.getY();
                case MotionEvent.ACTION_UP:
                    downPoint.set(0,0);
            }
        }
        return super.onTouch(v, ev);
    }

    public Bitmap cutBitmap(Bitmap longBitmap,float deltay) {
        bsrc.top += Math.round(deltay);
        bsrc.bottom += Math.round(deltay);
        if(bsrc.top <= 0) {
            bsrc.top = 0;
            bsrc.bottom = Math.round(longBitmap.getWidth() *imageScale);
        }
        if(bsrc.bottom >= longBitmap.getHeight()) {
            bsrc.top = longBitmap.getHeight()-Math.round(longBitmap.getWidth() *imageScale);
            bsrc.bottom = longBitmap.getHeight();
        }
        return mDecoder.decodeRegion(bsrc, null);
    }

    public void setLongBitmap(final Bitmap longBitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LongPhotoViewAttacher.this.longBitmap = longBitmap;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                longBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                Logger.showLog("longBitmap.bottom------"+longBitmap.getHeight()+"  longBitmap.right"+longBitmap.getWidth(),"setLongBitmap");
                InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
                mDecoder = null;
                try {
                    mDecoder = BitmapRegionDecoder.newInstance(isBm, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bsrc.left = 0;
                bsrc.right = longBitmap.getWidth();
//                lp获取不到控件宽高///因为没有在XML中指明具体大小
//                ViewGroup.LayoutParams lp = getLayoutParams();
                imageScale = mImageView.get().getHeight()*1.0f/mImageView.get().getWidth();
                bsrc.bottom = Math.round(longBitmap.getWidth() *imageScale);
                final BitmapRegionDecoder finalMDecoder = mDecoder;
                ((Activity)mImageView.get().getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.get().setImageBitmap(finalMDecoder.decodeRegion(bsrc, null));
                    }
                });
            }
        }).start();
    }
}
