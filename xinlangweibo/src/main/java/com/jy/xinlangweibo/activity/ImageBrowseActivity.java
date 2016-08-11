package com.jy.xinlangweibo.activity;

import android.app.Activity;
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
import com.jy.xinlangweibo.utils.ImageLoadeOptions;
import com.jy.xinlangweibo.widget.PhotoView.PhotoView;
import com.jy.xinlangweibo.widget.PhotoView.PhotoViewAttacher;
import com.jy.xinlangweibo.widget.PhotoView.PhotoViewAttacher.OnViewTapListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class ImageBrowseActivity extends Activity {

	private ViewPager vp_imagebrowse;
	private ArrayList<Object> pic_urls ;
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
		pic_urls = (ArrayList) getIntent().getSerializableExtra("Pic_urls");
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
		vp_imagebrowse.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				tv_ivbrowse.setText((arg0+1)+"/"+(pic_urls.size()));
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ImageBrowseActivity.this.finish();
			}
		});
	}
	
	class ImageBrowseAdapter extends PagerAdapter {

		private PhotoViewAttacher mAttacher;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pic_urls.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			PhotoView imageView = new PhotoView(ImageBrowseActivity.this.getBaseContext());
			mAttacher = new PhotoViewAttacher(imageView);
			mAttacher.setOnViewTapListener(new OnViewTapListener() {
				
				@Override
				public void onViewTap(View view, float x, float y) {
					showOrHide();
				}
			});
//			imageView.setScaleType( ImageView.ScaleType.CENTER_INSIDE);
			imageLoader.displayImage((String) pic_urls.get(position), imageView, ImageLoadeOptions.getDefaultIvOption(ImageBrowseActivity.this.getBaseContext()));
			container.addView(imageView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) );
			return imageView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
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
