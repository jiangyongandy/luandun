package com.jy.xinlangweibo.fragment;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import cn.swu.pulltorefreshswipemenulistviewsample.PullToRefreshSwipeMenuListView;
import cn.swu.pulltorefreshswipemenulistviewsample.PullToRefreshSwipeMenuListView.IXListViewListener;
import cn.swu.pulltorefreshswipemenulistviewsample.PullToRefreshSwipeMenuListView.OnMenuItemClickListener;
import cn.swu.pulltorefreshswipemenulistviewsample.PullToRefreshSwipeMenuListView.OnSwipeListener;
import cn.swu.swipemenulistview.SwipeMenu;
import cn.swu.swipemenulistview.SwipeMenuCreator;
import cn.swu.swipemenulistview.SwipeMenuItem;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.adapter.MessageAdapter;
import com.jy.xinlangweibo.base.BaseFragment;
import com.jy.xinlangweibo.utils.TitleBuilder;
import com.jy.xinlangweibo.utils.Utils;
import com.sina.weibo.sdk.openapi.models.Status;

public class MessageFragment extends BaseFragment implements IXListViewListener {
	private View view;
	private PullToRefreshSwipeMenuListView mListView;
	private MessageAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(activity, R.layout.fragment_message, null);
		initTitle();
		initLv();
		return view;
	}

	private void initLv() {

		final ArrayList<Status> statuses = (ArrayList<Status>) mCache
				.getAsObject("STATUES");
		if(statuses == null) {
			return;
		}
		adapter = new MessageAdapter(statuses);
		mListView = (PullToRefreshSwipeMenuListView) view
				.findViewById(R.id.listView);
		mListView.setAdapter(adapter);
		mListView.setPullRefreshEnable(false);
		mListView.setPullLoadEnable(false);
		mListView.setXListViewListener(this);
		// mHandler = new Handler();

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
//				SwipeMenuItem openItem = new SwipeMenuItem(activity);
//				// set item background
//				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//						0xCE)));
//				// set item width
//				openItem.setWidth(Utils.dip2px(activity, 90));
//				// set item title
//				openItem.setTitle("Open");
//				// set item title fontsize
//				openItem.setTitleSize(18);
//				// set item title font color
//				openItem.setTitleColor(Color.WHITE);
//				// add to menu
//				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(activity);
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(Utils.dip2px(activity, 90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
					if(index == 0) {
						statuses.remove(position);
						adapter.notifyDataSetChanged();
					}
			}
		});

		// set SwipeListener
		mListView.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		// other setting
		// listView.setCloseInterpolator(new BounceInterpolator());

		// test item long click
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(activity, position + " long click",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		});

	}

	private void initTitle() {
		new TitleBuilder(view).setTitle("消息");
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mListView.setSelection(0);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}
}
