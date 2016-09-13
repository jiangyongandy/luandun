package com.jy.xinlangweibo.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.ImageUtils;
import com.blankj.utilcode.utils.SDCardUtils;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.ui.fragment.base.BaseFragment;
import com.jy.xinlangweibo.utils.CommonImageLoader.CustomImageLoader;
import com.jy.xinlangweibo.utils.CommonImageLoader.GlideImageLoaderLoader;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.Utils;
import com.jy.xinlangweibo.widget.FitViewPager;
import com.jy.xinlangweibo.widget.ninephoto.BGABrowserPhotoViewAttacher;
import com.jy.xinlangweibo.widget.ninephoto.BGAImageView;
import com.jy.xinlangweibo.widget.photoview.LongPhotoViewAttacher;
import com.jy.xinlangweibo.widget.photoview.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by JIANG on 2016/9/1.
 */
public class ImageBrowserFragment extends BaseFragment implements ImageLoadingListener,LongPhotoViewAttacher.CutImageFinishListenner{
    private int layout = R.layout.activity_image_browse;
    @BindView(R.id.iv_save)
    ImageView ivSave;
    @BindView(R.id.vp_imagebrowse)
    FitViewPager vpImagebrowse;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_ivbrowse)
    TextView tvIvbrowse;
    @BindView(R.id.imagebrowse_top)
    RelativeLayout imagebrowseTop;
    @BindView(R.id.imagebrowse_bottom)
    RelativeLayout imagebrowseBottom;
    @BindView(R.id.rl_vpcontainer)
    RelativeLayout rlVpcontainer;
    private ArrayList<String> pic_urls;
    // 自动隐藏顶部和底部View的时间
    private static final int HIDE_TIME = 5000;
    //	第一次进入应该显示的当前页码
    private int position;
    private ImageLoader imageLoader;
    private ImageBrowseAdapter imageBrowseAdapter;
    private String basePath;

    @Override
    protected int CreateView() {
        return layout;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);

        pic_urls = (ArrayList<String>) activity.getIntent().getSerializableExtra("Pic_urls");
        for (int i = 0; i < pic_urls.size(); i++) {
            pic_urls.set(i, pic_urls.get(i).replace("bmiddle", "large"));
        }
        position = activity.getIntent().getIntExtra("Position", 0);
        imageLoader = ImageLoader.getInstance();

        //		第一次进入界面 隐藏浮动栏
        showOrHide();
        imageBrowseAdapter = new ImageBrowseAdapter();
        vpImagebrowse.setAdapter(imageBrowseAdapter);
        vpImagebrowse.setCurrentItem(position);
        tvIvbrowse.setText(position + 1 + "/" + (pic_urls.size()));
        vpImagebrowse.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                tvIvbrowse.setText((arg0 + 1) + "/" + (pic_urls.size()));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        return rootView;
    }

    @OnClick(R.id.iv_save)
    public void onClick() {
        if(!SDCardUtils.isSDCardEnable()) {
            activity.showToast("SD CARD NO EXIT!");
            return;
        }
        ((GlideImageLoaderLoader)CustomImageLoader.getImageLoader()).downloadImage(activity, pic_urls.get(vpImagebrowse.getCurrentItem()), new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(final Bitmap resource, GlideAnimation glideAnimation) {
                final String path ;
                basePath = SDCardUtils.getSDCardPath() + "luandunFile/";
                path = basePath+pic_urls.get(vpImagebrowse.getCurrentItem()).split("/")[4];
                Logger.showLog(""+path,"文件路径");
                if(!FileUtils.isFileExists(basePath)) {
                    File file = new File(basePath);
                    file.mkdir();
                }
                File file = new File(basePath,pic_urls.get(vpImagebrowse.getCurrentItem()).split("/")[4]);
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(final Subscriber<? super Boolean> subscriber) {
                        subscriber.onNext(ImageUtils.save(resource,path, Bitmap.CompressFormat.JPEG));
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
                    @Override
                    public void onNext(Boolean isSuccess) {
                        if(isSuccess) {
                            activity.showToast("保存成功路径："+basePath);
                        }
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.showLog(""+e,"RXJAVA 接受事件出错");
                        Toast.makeText(activity, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void cutImageFinish(Bitmap bitmap, String s, ImageView imageView) {
        imageLoader.getMemoryCache().put(MemoryCacheUtils.generateKey(s,new ImageSize(imageView.getWidth(),imageView.getHeight())),bitmap);
        try {
            imageLoader.getDiskCache().save(s,bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ImageBrowseAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pic_urls.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final BGAImageView imageView = new BGAImageView(container.getContext());
//            imageLoader.displayImage(pic_urls.get(position), imageView, ImageLoadeOptions.getNoDownScalingIvOption(imageView.getContext()), ImageBrowserFragment.this);
            container.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            final BGABrowserPhotoViewAttacher photoViewAttacher = new BGABrowserPhotoViewAttacher(imageView);
            photoViewAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    showOrHide();
                }
            });
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
            ((GlideImageLoaderLoader)CustomImageLoader.getImageLoader()).displayGifImage(activity, imageView, pic_urls.get(position), R.drawable.timeline_image_loading,R.drawable.timeline_image_failure, Utils.getDisplayWidthPixels(activity),Utils.getDisplayHeightPixelsPixels(activity));
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void hide() {
        imagebrowseTop.clearAnimation();
        Animation animation = AnimationUtils.loadAnimation(activity,
                R.anim.option_leave_from_top);
        animation.setAnimationListener(new AnimationImp() {
            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);
                imagebrowseTop.setVisibility(View.GONE);
            }
        });
        imagebrowseTop.startAnimation(animation);

        imagebrowseBottom.clearAnimation();
        Animation animation1 = AnimationUtils.loadAnimation(activity,
                R.anim.option_leave_from_bottom);
        animation1.setAnimationListener(new AnimationImp() {
            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);
                imagebrowseBottom.setVisibility(View.GONE);
            }
        });
        imagebrowseBottom.startAnimation(animation1);
    }

    private void showOrHide() {
        if (imagebrowseTop.getVisibility() == View.VISIBLE) {
            imagebrowseTop.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(activity,
                    R.anim.option_leave_from_top);
            animation.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    imagebrowseTop.setVisibility(View.GONE);
                }
            });
            imagebrowseTop.startAnimation(animation);

            imagebrowseBottom.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(activity,
                    R.anim.option_leave_from_bottom);
            animation1.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    imagebrowseBottom.setVisibility(View.GONE);
                }
            });
            imagebrowseBottom.startAnimation(animation1);
        } else {
            imagebrowseTop.setVisibility(View.VISIBLE);
            imagebrowseTop.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(activity,
                    R.anim.option_entry_from_top);
            imagebrowseTop.startAnimation(animation);

            imagebrowseBottom.setVisibility(View.VISIBLE);
            imagebrowseBottom.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(activity,
                    R.anim.option_entry_from_bottom);
            imagebrowseBottom.startAnimation(animation1);
        }
    }

    @Override
    public void onLoadingStarted(String s, View view) {

    }

    @Override
    public void onLoadingFailed(String s, View view, FailReason failReason) {
    }

    @Override
    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
        if (com.jy.xinlangweibo.utils.ImageUtils.isLargeScreenImage(bitmap,view.getContext()) == 1) {
            Logger.showLog("" + bitmap.getHeight()+"------"+bitmap.getWidth(), "ImageBrowserFragment--onLoadingComplete");
            ((ImageView)view).setImageResource(R.drawable.timeline_image_loading);
            ((PhotoView)view).setLongBitmap(bitmap,s);
            ((PhotoView)view).setCutImageFinishListenner(ImageBrowserFragment.this);
        } else if (com.jy.xinlangweibo.utils.ImageUtils.isLargeScreenImage(bitmap, view.getContext()) == 2) {

        }
    }

    @Override
    public void onLoadingCancelled(String s, View view) {

    }

    private class AnimationImp implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

    }
}
