package com.jy.xinlangweibo.adapter;

import java.util.ArrayList;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.utils.DateUtils;
import com.jy.xinlangweibo.utils.ImageLoadeOptions;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.User;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentsStatusAdapter extends BaseAdapter {
	private ArrayList<Comment> list;
	private ImageLoader imageLoader;
	private Context context;
	private OnLastItemVisibleListenner listener;

	public CommentsStatusAdapter(ArrayList<Comment> list,
			OnLastItemVisibleListenner listener) {
		this.list = list;
		this.listener = listener;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Comment getItem(int position) {
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
			convertView = View.inflate(context, R.layout.item_comments, null);
			vh = ViewHolder.getViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		TextView statusName = vh.getView(R.id.tv_pubname);
		TextView sourceText = vh.getView(R.id.tv_from);
		ImageView headIv = vh.getView(R.id.iv_head);

		TextView statusText = vh.getView(R.id.tv_statuses_content);

		// bind data
		final Comment comment = getItem(position);
		User user = comment.user;

		View itemStatus = vh.getView(R.id.ll_item_status);
		itemStatus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		if (user != null) {
			imageLoader.displayImage(user.avatar_hd, headIv,
					ImageLoadeOptions.getIvHeadOption());
			sourceText.setText(DateUtils.getDate(comment.created_at) + " 来自 "
					+ Html.fromHtml(comment.source));
			statusName.setText(user.screen_name);
		} else {
			return convertView = View.inflate(context, R.layout.status_details_default_item, null);
		}
		statusText.setText(StringUtils.getKeyText(context, comment.text,
				statusText));

		return vh.getMconvertView();
	}

}
