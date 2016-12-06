package com.jy.xinlangweibo.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.jy.xinlangweibo.ui.activity.MainActivity;

//单例设计模式
public class FragmentController {
	private static FragmentController controller;
	private FragmentManager fm;
	private int containerId;
	private MainActivity mainActivity;
	private Fragment[] fragments;


	private FragmentController(Activity activity,int id,Fragment[] fragments) {
		mainActivity = (MainActivity) activity;
		fm = ((MainActivity) activity).getFragmentManager();
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
						if (!(fragments[0] instanceof Home2Fragment)) {
							fragments[0] = new Home2Fragment();
							ft.add(containerId, fragments[0]);
						}
						break;
					case 1:
						if (!(fragments[1] instanceof VideoRecommendFragment)) {
							fragments[1] = new VideoRecommendFragment();
							ft.add(containerId, fragments[1]);
						}
						break;
					case 2:
						if (!(fragments[2] instanceof DiscoverFragment)) {
							fragments[2] = new DiscoverFragment();
							ft.add(containerId, fragments[2]);
						}
						break;
					case 3:
						if (!(fragments[3] instanceof Profile2Fragment)) {
							fragments[3] = new Profile2Fragment();
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
