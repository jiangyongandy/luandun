package com.jy.xinlangweibo.activity;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.base.BaseActivity;
import com.jy.xinlangweibo.fragment.DiscoverFragment;
import com.jy.xinlangweibo.fragment.FragmentController;
import com.jy.xinlangweibo.fragment.HomeFragment;
import com.jy.xinlangweibo.fragment.MessageFragment;
import com.jy.xinlangweibo.fragment.ProfileFragment;
import com.jy.xinlangweibo.utils.Logger;

public class MainActivity extends BaseActivity {

	private RadioGroup rg;
	private ImageView iv;
	protected FragmentController fragmentController;
	private  Fragment[] fragments = new Fragment[4]  ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.showLog("mainactivity oncreate", tag);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		initView();
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(false);
	}

	@Override
	protected void onDestroy() {
		Logger.showLog("mainactivity ondestroy", "com.jy.xinlangweibo");
		FragmentController.onDestroy();
		super.onDestroy();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		try {
			return super.dispatchTouchEvent(ev);
		} catch (Exception e) {
			return false;
		}
	}

@Override
public void onAttachFragment(Fragment fragment) {
	super.onAttachFragment(fragment);
    if (fragment instanceof HomeFragment) {  
    	fragments[0] = fragment;
    }
    else if (fragment instanceof MessageFragment) {  
    	fragments[1] = fragment;
    }else if (fragment instanceof DiscoverFragment) {  
    	fragments[2] = fragment;
    }else if (fragment instanceof ProfileFragment) {
    	fragments[3] = fragment;
    }
}

	private void initView() {
////		自定义actiobar初始化
//	       View actionbarLayout = LayoutInflater.from(this).inflate(  
//	               R.layout.custom_actbar, null);  
//	       TitleBuilder.setCustomActionBar(this, actionbarLayout);
//     Fragment 初始化。
		fragmentController = FragmentController.getInstance(this,
				R.id.contentframe,fragments);
		System.out.println("fragment 初始化。。。。完成");
		fragmentController.show(0);
//		底部按钮初始化
		rg = (RadioGroup) findViewById(R.id.rg);
		rg.setBackgroundColor(0xffffffff);
		iv = (ImageView) findViewById(R.id.tabcenterid);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				fragmentController.hide();
				switch (checkedId) {
				case R.id.tabhomeid:
					fragmentController.show(0);
					break;
				case R.id.tabmessageid:
					fragmentController.show(1);
					break;
				case R.id.tabdiscoverid:
					fragmentController.show(2);
					break;
				case R.id.tabprofileid:
					fragmentController.show(03);
					break;

				default:
					break;
				}
			}
		});
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ToastUtils.show(MainActivity.this, "add", Toast.LENGTH_SHORT);
				MainActivity.this.intent2Activity(WriteStatusActivity.class);
			}
		});
	}

}
