package com.jy.xinlangweibo.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.utils.CommonImageLoader.ImageLoadeOptions;
import com.jy.xinlangweibo.utils.WeiboStringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {
	private ArrayList<Status> list;
	private ImageLoader imageLoader;
	private Activity context;

	public MessageAdapter(ArrayList<Status> list) {
		this.list = list;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Status getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		context = (Activity) parent.getContext();
		ViewHolder vh;
		if (convertView == null) {
			convertView = context.getLayoutInflater().inflate( R.layout.item_message, null);
			vh = ViewHolder.getViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		TextView nickName = vh.getView(R.id.tv_pubname);
		TextView messageText = vh.getView(R.id.tv_from);
		ImageView headIv = vh.getView(R.id.iv_head);

		Status status = getItem(position);
		nickName.setText(status.user.screen_name);
		messageText.setText(WeiboStringUtils.getOnlyImageSpan(context, status.text, messageText));
		imageLoader.displayImage(status.user.avatar_hd, headIv,
				ImageLoadeOptions.getIvHeadOption());
		return vh.getMconvertView();
	}

}
