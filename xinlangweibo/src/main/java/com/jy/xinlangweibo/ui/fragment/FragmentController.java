package com.jy.xinlangweibo.ui.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jy.xinlangweibo.ui.activity.MainActivity;
import com.jy.xinlangweibo.ui.fragment.home.Home2Fragment;
import com.jy.xinlangweibo.ui.fragment.home.NewsFragment;
import com.jy.xinlangweibo.ui.fragment.home.PersonalFragment;
import com.jy.xinlangweibo.ui.fragment.home.VideoRecommendFragment;

//单例设计模式
public class FragmentController {
	private static FragmentController controller;
	private FragmentManager fm;
	private int containerId;
	private MainActivity mainActivity;
	private Fragment[] fragments;


	private FragmentController(Activity activity,int id,Fragment[] fragments) {
		mainActivity = (MainActivity) activity;
		fm = ((MainActivity) activity).getSupportFragmentManager();
		this.containerId = id;
		this.fragments = fragments;
		initView();
	}

	private void initView() {
		FragmentTransaction ft;
		ft = fm.beginTransaction();
		if (fragments == null) {
			System.out.println("fragments 空");
		} else {
			for (int i = 0;i<4;i++) {
					switch (i) {
					case 0:
						if (!(fragments[0] instanceof VideoRecommendFragment)) {
							fragments[0] = new VideoRecommendFragment();
							ft.add(containerId, fragments[0]);
						}
						break;
					case 1:
						if (!(fragments[1] instanceof NewsFragment)) {
							fragments[1] = new NewsFragment();
							ft.add(containerId, fragments[1]);
						}
						break;
					case 2:
						if (!(fragments[2] instanceof Home2Fragment)) {
							fragments[2] = new Home2Fragment();
							ft.add(containerId, fragments[2]);
						}
						break;
					case 3:
						if (!(fragments[3] instanceof PersonalFragment)) {
							fragments[3] = new PersonalFragment();
							ft.add(containerId, fragments[3]);
						}
						break;
						
					default:
						break;
					}
			}
		}

//		for (Fragment fg: fragments) {
//			ft.add(containerId, fg);
//		}
		ft.commit();
	}
	public static void onDestroy() {
		controller = null;
	}
	public static FragmentController getInstance(Activity activity, int containerid,Fragment[] fragments) {
		synchronized (FragmentController.class) {
			if (controller == null) {
				controller = new FragmentController(activity, containerid, fragments);
			}
		}
		return controller;
	}

	public void hide() {
		FragmentTransaction ft = fm.beginTransaction();
		for(Fragment fg:fragments) {
			if(ft != null) {
			ft.hide(fg);
			}
		}
		if (ft != null) {
			ft.commit();
		}
	}
	public void show (int position) {
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fg = fragments[position];
		hide();
		ft.show(fg);
		ft.commit();
	}
}
