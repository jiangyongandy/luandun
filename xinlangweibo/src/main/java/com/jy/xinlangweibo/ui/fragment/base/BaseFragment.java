package com.jy.xinlangweibo.ui.fragment.base;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.utils.ACache;
import com.jy.xinlangweibo.utils.Logger;

import butterknife.ButterKnife;

public class BaseFragment extends Fragment {
	protected BaseActivity activity;
	protected ACache mCache;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.showLog("得到缓存", "BaseFragment");
//		得到缓存
		mCache = ACache.get(this.getActivity());
		
		activity = (BaseActivity) getActivity();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(CreateView(),container,false);
		ButterKnife.bind(this, rootView);
		return rootView;
	}

	protected int CreateView() {
		return 0;
	}

	protected void intent2Activity(Class<? extends BaseActivity> tarActivity) {
		Intent intent = new Intent(activity,tarActivity);
		startActivity(intent);
	}
}
