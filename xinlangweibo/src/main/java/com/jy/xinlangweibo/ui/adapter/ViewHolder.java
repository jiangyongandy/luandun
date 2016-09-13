package com.jy.xinlangweibo.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.View;

public class ViewHolder {
	private SparseArray<View> mlistViews;
	private View mconvertView;
	private Context mcontext;
	private static ViewHolder mvh;

	private ViewHolder(View convertView) {
		this.mlistViews = new SparseArray<View>();
		this.mconvertView = convertView;
	}

	private ViewHolder(Context context) {
		this.mlistViews = new SparseArray<View>();
		this.mcontext = context;
	}

	public static ViewHolder getViewHolder(View convertView) {
		mvh = new ViewHolder(convertView);
		return mvh;
	}

	public static ViewHolder getViewHolder(Context context) {
		mvh = new ViewHolder(context);
		return mvh;
	}

	public static void clearViewHolder() {
		mvh = null;
	}

	/**
	 * 使用该方法减少代码行数，但使用条件为ConvertView的ViewID 要唯一
	 * 
	 * @param id
	 *            资源文件生成的ID
	 * @return 返回通过ID找到的VIEW
	 */
	public <T extends View> T getView(int id) {
		View view;
		view = mlistViews.get(id);
		if (view == null) {
			if (mconvertView != null) {
				view = mconvertView.findViewById(id);
				mlistViews.put(id, view);
			} else {
				view = ((Activity) mcontext).findViewById(id);
				mlistViews.put(id, view);
			}
		}
		return (T) view;
	}

	public View getMconvertView() {
		return mconvertView;
	}
}
