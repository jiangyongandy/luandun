package com.jy.xinlangweibo.adapter;

import java.util.ArrayList;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.constant.CustomConstant;
import com.jy.xinlangweibo.utils.ImageLoadeOptions;
import com.jy.xinlangweibo.utils.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class GridIvAdapter extends BaseAdapter {
	private ArrayList<Object> pic_ids;
	private ImageLoader imageLoader;

	public GridIvAdapter(ArrayList<Object> pic_ids) {
		this.pic_ids = pic_ids;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return pic_ids.size();
	}

	@Override
	public Object getItem(int position) {
		return pic_ids.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.item_grid_statusimage, null);
			vh = ViewHolder.getViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		ImageView iv = vh.getView(R.id.iv_item_grid_status);
		GridView gv = (GridView) parent;
		

		int itemWidth = gv.getMeasuredWidth() / 3;
//		Logger.showLog(""+itemWidth, "IV宽");
//		Logger.showLog(""+gv.getHorizontalSpacing(), "水平间距");
		LayoutParams layoutParams = new LayoutParams(itemWidth, itemWidth);
		iv.setLayoutParams(layoutParams);
		
		imageLoader.displayImage((String) getItem(position), iv);
		return vh.getMconvertView();
	}

}
