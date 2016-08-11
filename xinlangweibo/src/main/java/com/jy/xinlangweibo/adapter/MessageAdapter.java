package com.jy.xinlangweibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.utils.ImageLoadeOptions;
import com.jy.xinlangweibo.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {
	private ArrayList<Status> list;
	private ImageLoader imageLoader;
	private Context context;

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
		context = parent.getContext();
		ViewHolder vh;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_message, null);
			vh = ViewHolder.getViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		View messageItem = vh.getView(R.id.item_message_layout);
		TextView nickName = vh.getView(R.id.tv_pubname);
		TextView messageText = vh.getView(R.id.tv_from);
		ImageView headIv = vh.getView(R.id.iv_head);

		Status status = getItem(position);
		nickName.setText(status.user.screen_name);
		messageText.setText(StringUtils.getOnlyImageSpan(context, status.text, messageText));
		imageLoader.displayImage(status.user.avatar_hd, headIv,
				ImageLoadeOptions.getIvHeadOption());

		messageItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		return vh.getMconvertView();
	}

}
