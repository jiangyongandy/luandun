package com.jy.xinlangweibo.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.ui.adapter.StatusesAdapter;
import com.jy.xinlangweibo.api.MyWeiboapi;
import com.jy.xinlangweibo.api.SimpleRequestlistener;
import com.jy.xinlangweibo.constant.AccessTokenKeeper;
import com.jy.xinlangweibo.constant.Constants;
import com.jy.xinlangweibo.ui.fragment.base.BaseFragment;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.TitleBuilder;
import com.jy.xinlangweibo.utils.Utils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment {
	private View view;
	private View footView;
	private View tvLoad;
	private ImageView ivLoad;
	private PullToRefreshListView lv;
	private ArrayList<Status> statusList = new ArrayList<Status>();
	private StatusesAdapter adapter;
	private int curPage;
	private ProgressDialog progressDialog;
	private PopupWindow pw;
	private String ip;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initView();
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
		mCache.clear();
		mCache.put("STATUES", statusList);
		System.out.println("缓存首页微博");
	}

	private void initView() {
		view = View.inflate(activity, R.layout.fragment_home, null);
		showProgressDialog();
		loadData(1);
		initPop();

		initTitle();

		initPlv();
	}

	private void showProgressDialog() {
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("微博加载中...");
		progressDialog.show();
	}

	/**
	 * 
	 */
	private void initPlv() {
		footView = View.inflate(activity, R.layout.status_footview_loading,
				null);
		ivLoad = (ImageView) footView.findViewById(R.id.iv_status_loading);
		tvLoad = footView.findViewById(R.id.tv_status_loading);
		lv = (PullToRefreshListView) view.findViewById(R.id.lv_status);
		lv.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData(1);
			}

			private void loadCaheData() {

			}
		});
		// lv.setOnLastItemVisibleListener(new OnLastItemVisibleListener(){
		//
		// @Override
		// public void onLastItemVisible() {
		//
		// }} );
		adapter = new StatusesAdapter(statusList);
		lv.setAdapter(adapter);
	}

	/**
	 * 
	 */
	private void initTitle() {
		new TitleBuilder(view).setLeftText("lest", 0).setTitle("首页")
				.setRightOnclickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (pw.isShowing() == false) {
							Logger.showLog(
									"屏幕宽度："
											+ Utils.getDisplayWidthPixels(getActivity())
											+ "     弹窗宽度:"
											+ MeasureSpec
													.getSize(pw.getWidth()),
									"计算弹窗偏移量");
							pw.showAsDropDown(
									view.findViewById(R.id.rl_titlebar),
									Utils.getDisplayWidthPixels(getActivity())
											- MeasureSpec.getSize(pw.getWidth())
											- Utils.dip2px(getActivity(), 10),
									0);
							backgroundAlpha(0.8f);
						} else {
							pw.dismiss();
						}

					}
				}).setLeftOnclickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				});
	}

	/**
	 * 
	 */
	private void initPop() {
		View popView = LayoutInflater.from(getActivity()).inflate(
				R.layout.pop_mainact_navright, null);
		pw = new PopupWindow(popView, Utils.dip2px(getActivity(), 115),
				LayoutParams.WRAP_CONTENT);
		// 使其聚焦
		pw.setFocusable(true);
		// 设置允许在外点击消失
		pw.setOutsideTouchable(true);
		// 刷新状态
		pw.update();
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		pw.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.conversation_options_bg));
		pw.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				backgroundAlpha(1f);
			}
		});
	}

	private void loadData(final int page) {
		Oauth2AccessToken readAccessToken = AccessTokenKeeper
				.readAccessToken(activity);
		MyWeiboapi api = new MyWeiboapi(activity, Constants.APP_KEY,
				readAccessToken);
		api.statuses_home_timeline(page, new SimpleRequestlistener(activity,
				progressDialog) {
			@Override
			public void onComplete(String response) {
				super.onComplete(response);
				// Statuses fromJson = new Gson().fromJson(response,
				// Statuses.class);
				addData(page, response);
				onAllDone();
				tvLoad.setVisibility(View.VISIBLE);
			}

			@Override
			public void onWeiboException(WeiboException arg0) {
				super.onWeiboException(arg0);
				System.out.println("请求缓存");
				ArrayList<Status> statuses = (ArrayList<Status>) mCache
						.getAsObject("STATUES");
				if(statuses != null) {
					for (Status sta : statuses) {
						statusList.add(sta);
						if(statusList.size() == 50) 
							break;
					}
					adapter.notifyDataSetChanged();
				}
				onAllDone();
				tvLoad.setVisibility(View.GONE);
			}
			
			@Override
			protected void onAllDone() {
				super.onAllDone();
				ivLoad.setVisibility(View.GONE);
				if (footView.getAnimation() != null) {
					footView.getAnimation().cancel();
				}
				lv.onRefreshComplete();
			}
		});
	}

	private void addData(final int page, String response) {
		if (page == 1) {
			statusList.clear();
			curPage = 1;
		}
		curPage = page;
		StatusList list = StatusList.parse(response);
		for (Status sta : list.statusList) {
			statusList.add(sta);
		}
		adapter.notifyDataSetChanged();
		ListView refreshableView = lv.getRefreshableView();
		if (curPage < list.total_number) {
			addFootView(refreshableView);
		} else {
			removeFootView(refreshableView);
		}
	}

	private void removeFootView(ListView refreshableView) {
		if (refreshableView.getFooterViewsCount() > 1) {
			refreshableView.removeFooterView(footView);
		}
	}

	private void addFootView(ListView refreshableView) {
		if (refreshableView.getFooterViewsCount() == 1) {
			refreshableView.addFooterView(footView);
			footView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// 开启Loading动画
					tvLoad.setVisibility(View.GONE);
					ivLoad.setVisibility(View.VISIBLE);
					ivLoad.startAnimation(AnimationUtils.loadAnimation(
							activity, R.anim.ra_loading));
					loadData(curPage + 1);
				}
			});
		}
	}

	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getActivity().getWindow()
				.getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getActivity().getWindow().setAttributes(lp);
	}
}
