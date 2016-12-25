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

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseFragment extends Fragment {
	protected BaseActivity activity;
	protected BasePresenter presenter;
	private Unbinder bind;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (BaseActivity) getActivity();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(CreateView(),container,false);
		bind = ButterKnife.bind(this, rootView);
		initViewAndEvent(rootView);
		return rootView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(presenter != null)
			presenter.onDestroy();
		if(bind != null)
			bind.unbind();
		RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
		refWatcher.watch(this);
	}

	protected int CreateView() {
		return 0;
	}

	protected void initViewAndEvent(View rootView) {
	}

	protected void intent2Activity(Class<? extends BaseActivity> tarActivity) {
		Intent intent = new Intent(activity,tarActivity);
		startActivity(intent);
	}


}
