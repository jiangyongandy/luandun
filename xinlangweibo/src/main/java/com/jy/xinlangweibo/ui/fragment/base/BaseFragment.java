package com.jy.xinlangweibo.ui.fragment.base;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jy.xinlangweibo.BaseApplication;
import com.jy.xinlangweibo.presenter.BasePresenter;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.squareup.leakcanary.RefWatcher;

public class BaseFragment extends Fragment {
	protected BaseActivity activity;
	protected BasePresenter presenter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (BaseActivity) getActivity();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(CreateView(),container,false);
		return rootView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(presenter != null)
			presenter.onDestroy();
		RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
		refWatcher.watch(this);
	}

	protected int CreateView() {
		return 0;
	}

	protected void intent2Activity(Class<? extends BaseActivity> tarActivity) {
		Intent intent = new Intent(activity,tarActivity);
		startActivity(intent);
	}
}
