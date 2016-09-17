package com.jy.xinlangweibo.ui.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.utils.CommonImageLoader.CustomImageLoader;
import com.jy.xinlangweibo.utils.Utils;
import com.jy.xinlangweibo.widget.ninephoto.BGABrowserPhotoViewAttacher;
import com.jy.xinlangweibo.widget.ninephoto.BGAImageView;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/27 下午6:35
 * 描述:
 */
public class BGAPhotoPageAdapter extends PagerAdapter {
    private ArrayList<String> mPreviewImages;
    private PhotoViewAttacher.OnViewTapListener mOnViewTapListener;
    private Activity mActivity;

    public BGAPhotoPageAdapter(Activity activity, PhotoViewAttacher.OnViewTapListener onViewTapListener, ArrayList<String> previewImages) {
        mOnViewTapListener = onViewTapListener;
        mPreviewImages = previewImages;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return mPreviewImages == null ? 0 : mPreviewImages.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        final BGAImageView imageView = new BGAImageView(container.getContext());
        container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final BGABrowserPhotoViewAttacher photoViewAttacher = new BGABrowserPhotoViewAttacher(imageView);
        photoViewAttacher.setOnViewTapListener(mOnViewTapListener);
        imageView.setDelegate(new BGAImageView.Delegate() {
            @Override
            public void onDrawableChanged(Drawable drawable) {
                if (drawable != null && drawable.getIntrinsicHeight() > drawable.getIntrinsicWidth() && drawable.getIntrinsicHeight() > Utils.getDisplayHeightPixelsPixels(imageView.getContext())) {
                    photoViewAttacher.setIsSetTopCrop(true);
                    photoViewAttacher.setUpdateBaseMatrix();
                } else {
                    photoViewAttacher.update();
                }
            }
        });

        CustomImageLoader.displayImage(mActivity, imageView, mPreviewImages.get(position), R.drawable.bga_pp_ic_holder_dark, R.drawable.bga_pp_ic_holder_dark, Utils.getDisplayWidthPixels(imageView.getContext()), Utils.getDisplayHeightPixelsPixels(imageView.getContext()));

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public String getItem(int position) {
        return mPreviewImages == null ? "" : mPreviewImages.get(position);
    }
}