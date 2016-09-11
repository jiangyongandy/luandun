package com.jy.xinlangweibo.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.utils.CommonImageLoader.ImageLoadeOptions;
import com.jy.xinlangweibo.utils.ImageUtils;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.widget.photoview.PhotoView;
import com.jy.xinlangweibo.widget.photoview.PhotoViewAttacher;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;


public class ImageBrowseActivity extends BaseActivity implements ImageLoadingListener {

	private ViewPager vp_imagebrowse;
	private ArrayList<String> pic_urls ;
	private ImageLoader imageLoader;
	private View mTopView;
	private View mBottomView;
	private View iv_back;
	private View rl_vpcontainer;
	private TextView tv_ivbrowse;
	// 自动隐藏顶部和底部View的时间
	private static final int HIDE_TIME = 5000;
//	第一次进入应该显示的当前页码
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_browse);
		pic_urls = (ArrayList<String>) getIntent().getSerializableExtra("Pic_urls");
		for (int i = 0; i < pic_urls.size(); i++) {
			pic_urls.set(i, pic_urls.get(i).replace("bmiddle", "large"));
		}
		position = getIntent().getIntExtra("Position",0);
		imageLoader = ImageLoader.getInstance();
		
		vp_imagebrowse = (ViewPager) findViewById(R.id.vp_imagebrowse);
		mTopView = findViewById(R.id.imagebrowse_top);
		mBottomView = findViewById(R.id.imagebrowse_bottom);
		iv_back = findViewById(R.id.iv_back);
		rl_vpcontainer = findViewById(R.id.rl_vpcontainer);
		tv_ivbrowse = (TextView) findViewById(R.id.tv_ivbrowse);
		
		
//		第一次进入界面 隐藏浮动栏
		showOrHide();
		vp_imagebrowse.setAdapter(new ImageBrowseAdapter());
		vp_imagebrowse.setCurrentItem(position);
		tv_ivbrowse.setText(position+1+"/"+(pic_urls.size()));
		vp_imagebrowse.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				tv_ivbrowse.setText((arg0+1)+"/"+(pic_urls.size()));
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ImageBrowseActivity.this.finish();
			}
		});
	}

	@Override
	public void onLoadingStarted(String s, View view) {

	}

	@Override
	public void onLoadingFailed(String s, View view, FailReason failReason) {
	}

	@Override
	public void onLoadingComplete(String s, View view, Bitmap bitmap) {
		if(ImageUtils.isLargeImage(bitmap) == 1) {
			Logger.showLog(""+bitmap.getHeight()+s,"onLoadingComplete");
//			ViewGroup.LayoutParams layoutParams = ((ImageView) view).getLayoutParams();
//			layoutParams.height = bitmap.getHeight();
//			((ImageView)view).setLayoutParams(layoutParams);
//			Matrix mSuppMatrix = new Matrix();
//			float scale;
//			scale = Utils.getDisplayWidthPixels(view.getContext())/ bitmap.getWidth();
//			mSuppMatrix.postScale(scale,scale);
//			((ImageView)view).setImageMatrix(mSuppMatrix);
		} else if(ImageUtils.isLargeScreenImage(bitmap,view.getContext()) == 2) {
//			Matrix mSuppMatrix = new Matrix();
//			float scale;
//			scale = Utils.getDisplayWidthPixels(view.getContext())/ bitmap.getWidth();
//			mSuppMatrix.postScale(scale,scale);
//			((ImageView)view).setImageMatrix(mSuppMatrix);
		}
	}

	@Override
	public void onLoadingCancelled(String s, View view) {

	}

	class ImageBrowseAdapter extends PagerAdapter {

		private PhotoViewAttacher mAttacher;

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
			PhotoView imageView = new PhotoView(ImageBrowseActivity.this);
			imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
				@Override
				public void onViewTap(View view, float x, float y) {
					showOrHide();
				}
			});
			imageLoader.displayImage( pic_urls.get(position), imageView, ImageLoadeOptions.getDefaultIvOption(imageView.getContext()),ImageBrowseActivity.this);
			container.addView(imageView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) );
			return imageView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	private void hide() {
		mTopView.clearAnimation();
		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.option_leave_from_top);
		animation.setAnimationListener(new AnimationImp() {
			@Override
			public void onAnimationEnd(Animation animation) {
				super.onAnimationEnd(animation);
				mTopView.setVisibility(View.GONE);
			}
		});
		mTopView.startAnimation(animation);

		mBottomView.clearAnimation();
		Animation animation1 = AnimationUtils.loadAnimation(this,
				R.anim.option_leave_from_bottom);
		animation1.setAnimationListener(new AnimationImp() {
			@Override
			public void onAnimationEnd(Animation animation) {
				super.onAnimationEnd(animation);
				mBottomView.setVisibility(View.GONE);
			}
		});
		mBottomView.startAnimation(animation1);
	}
	private void showOrHide() {
		if (mTopView.getVisibility() == View.VISIBLE) {
			mTopView.clearAnimation();
			Animation animation = AnimationUtils.loadAnimation(this,
					R.anim.option_leave_from_top);
			animation.setAnimationListener(new AnimationImp() {
				@Override
				public void onAnimationEnd(Animation animation) {
					super.onAnimationEnd(animation);
					mTopView.setVisibility(View.GONE);
				}
			});
			mTopView.startAnimation(animation);

			mBottomView.clearAnimation();
			Animation animation1 = AnimationUtils.loadAnimation(this,
					R.anim.option_leave_from_bottom);
			animation1.setAnimationListener(new AnimationImp() {
				@Override
				public void onAnimationEnd(Animation animation) {
					super.onAnimationEnd(animation);
					mBottomView.setVisibility(View.GONE);
				}
			});
			mBottomView.startAnimation(animation1);
		} else {
			mTopView.setVisibility(View.VISIBLE);
			mTopView.clearAnimation();
			Animation animation = AnimationUtils.loadAnimation(this,
					R.anim.option_entry_from_top);
			mTopView.startAnimation(animation);

			mBottomView.setVisibility(View.VISIBLE);
			mBottomView.clearAnimation();
			Animation animation1 = AnimationUtils.loadAnimation(this,
					R.anim.option_entry_from_bottom);
			mBottomView.startAnimation(animation1);
		}
	}
	

	private class AnimationImp implements AnimationListener {

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
