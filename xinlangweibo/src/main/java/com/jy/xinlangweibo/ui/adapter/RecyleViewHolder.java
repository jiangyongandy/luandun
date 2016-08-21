package com.jy.xinlangweibo.ui.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public class RecyleViewHolder extends RecyclerView.ViewHolder {
	
	private SparseArray<View> mlistViews;
	private Context mcontext;
	private View mconvertView;
	private static  RecyleViewHolder mvh;

	
	private RecyleViewHolder(View view) {
		super(view);
		mconvertView = view;
		this.mlistViews = new SparseArray<View>();
	}
	
	public static RecyleViewHolder getViewHolder(View convertView) {
		mvh = new RecyleViewHolder(convertView);
		return mvh;
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
			} 
		}
		return (T) view;
	}
	
}
