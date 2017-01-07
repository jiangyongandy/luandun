package com.jy.xinlangweibo.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ChildListBean;
import com.jy.xinlangweibo.ui.activity.VideoInfoActivity;
import com.jy.xinlangweibo.widget.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class VideoHistoryIvAdapter extends BaseAdapter {
	private ArrayList<ChildListBean> list;
	private ImageLoader imageLoader;

	public VideoHistoryIvAdapter(ArrayList<ChildListBean> list) {
		this.list = list;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ChildListBean getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.item_grid_video_history, null);
			vh = ViewHolder.getViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		ImageView iv = vh.getView(R.id.iv_video_pic);
		TextView tv = vh.getView(R.id.tv_title);
		HorizontalListView gv = (HorizontalListView) parent;
		

//		int itemWidth = gv.getMeasuredWidth() / 3;
////		Logger.showLog(""+itemWidth, "IV宽");
////		Logger.showLog(""+gv.getHorizontalSpacing(), "水平间距");
//		LayoutParams layoutParams = new LayoutParams(itemWidth, itemWidth);
//		iv.setLayoutParams(layoutParams);
		
		imageLoader.displayImage( getItem(position).pic, iv);
		tv.setText(getItem(position).title);
		final View finalConvertView = convertView;
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				VideoInfoActivity.launch((Activity) finalConvertView.getContext(),getItem(position));
			}
		});


		return vh.getMconvertView();
	}

}
