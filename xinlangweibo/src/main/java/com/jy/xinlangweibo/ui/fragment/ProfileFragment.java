package com.jy.xinlangweibo.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.constant.CustomConstant;
import com.jy.xinlangweibo.ui.activity.StatusDetailsActivity;
import com.jy.xinlangweibo.ui.adapter.GridIvAdapter;
import com.jy.xinlangweibo.ui.adapter.ParallaxRecyclerAdapter;
import com.jy.xinlangweibo.ui.adapter.ParallaxRecyclerAdapter.OnParallaxScroll;
import com.jy.xinlangweibo.ui.adapter.RecyleViewHolder;
import com.jy.xinlangweibo.ui.fragment.base.BaseFragment;
import com.jy.xinlangweibo.utils.DateUtils;
import com.jy.xinlangweibo.utils.ImageLoadeOptions;
import com.jy.xinlangweibo.utils.StringUtils;
import com.jy.xinlangweibo.utils.TitleBuilder;
import com.jy.xinlangweibo.widget.CustomActBar;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;

public class ProfileFragment extends BaseFragment {
	private View view;
	private RecyclerView mRecyclerView;
	private TitleBuilder titleBuilder;
	private CustomActBar actbar;
	private ContextThemeWrapper context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(activity, R.layout.fragment_profile, null);
		init();
		return view;
	}

	private void init() {
		initTitle();
		mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview);
		createCardAdapter(mRecyclerView);
	}

	private void initTitle() {
		actbar = (CustomActBar) view.findViewById(R.id.profile_actbar);
	}

	private void createCardAdapter(RecyclerView recyclerView) {
		final ArrayList<Status> statuses = (ArrayList<Status>) mCache
				.getAsObject("STATUES");
		if(statuses == null) {
			return;
		}
		final ParallaxRecyclerAdapter<Status> adapter = new ParallaxRecyclerAdapter<Status>(
				statuses) {
			@Override
			public void onBindViewHolderImpl(
					RecyclerView.ViewHolder viewHolder,
					ParallaxRecyclerAdapter<Status> adapter, int i) {
				RecyleViewHolder vh = (RecyleViewHolder) viewHolder;
				TextView statusName = vh.getView(R.id.tv_pubname);
				TextView sourceText = vh.getView(R.id.tv_from);
				ImageView headIv = vh.getView(R.id.iv_head);

				TextView statusText = vh.getView(R.id.tv_statuses_content);
				ImageView statusIv = vh.getView(R.id.iv_statuses_singlecontent);
				GridView statusGv = vh.getView(R.id.gv_statuses_contents);

				TextView bottomretweetedText = vh
						.getView(R.id.tv_statuses_bottom_reweet);
				TextView bottomcommentText = vh
						.getView(R.id.tv_statuses_bottom_comment);
				TextView bottomunlikeText = vh
						.getView(R.id.tv_statuses_bottom_unlike);
				// 点赞的特殊处理
				final View ll_status_unlike = vh.getView(R.id.ll_status_unlike);
				final ImageView status_unlikebtn = vh
						.getView(R.id.status_unlikebtn);
				ll_status_unlike.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						status_unlikebtn
								.setImageResource(R.drawable.timeline_icon_like);
						status_unlikebtn.startAnimation(AnimationUtils
								.loadAnimation(activity, R.anim.scale_unlike));
					}
				});

				// bind data
				final Status status = adapter.getData().get(i);
				User user = status.user;

				View itemStatus = vh.getView(R.id.ll_item_status);
				itemStatus.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(activity,
								StatusDetailsActivity.class);
						intent.putExtra("Status", status);
						activity.startActivity(intent);
					}
				});

				imageLoader.displayImage(user.avatar_hd, headIv,
						ImageLoadeOptions.getIvHeadOption());
				String from = DateUtils.getDate(status.created_at) + " 来自  "
						+ Html.fromHtml(status.source);
				sourceText.setText(StringUtils.get2KeyText(activity, from,
						sourceText));
				statusName.setText(user.screen_name);

				statusText.setText(StringUtils.getKeyText(activity,
						status.text, statusText));
				setImage(status, statusIv, statusGv);

				bottomretweetedText
						.setText((CharSequence) (status.reposts_count > 0 ? ""
								+ status.reposts_count : "转发"));
				bottomcommentText
						.setText((CharSequence) (status.comments_count > 0 ? ""
								+ status.comments_count : "评论"));
				bottomunlikeText
						.setText((CharSequence) (status.attitudes_count > 0 ? ""
								+ status.attitudes_count
								: "赞"));

				if (status.retweeted_status != null) {
					LinearLayout retweeted = vh.getView(R.id.ll_retweeted);
					TextView retweetedText = vh
							.getView(R.id.tv_retweeted_content);
					ImageView retweetedIv = vh
							.getView(R.id.iv_retweeted_singlecontent);
					GridView retweetedGv = vh
							.getView(R.id.gv_retweeted_contents);

					retweeted.setVisibility(View.VISIBLE);
					String retweetedname = "作者已删除该微博";
					// 这里要判断 转发的微博作者是否为空 即转发的微博是否被作者删除。
					if (status.retweeted_status.user != null) {
						if (status.retweeted_status.user.screen_name != null) {
							retweetedname = status.retweeted_status.user.screen_name;
						}
					}
					String tempString = "@" + retweetedname + ":"
							+ status.retweeted_status.text;
					retweetedText.setText(StringUtils.getKeyText(activity,
							tempString, retweetedText));
					setImage(status.retweeted_status, retweetedIv, retweetedGv);
				} else {
					LinearLayout retweeted = vh.getView(R.id.ll_retweeted);
					retweeted.setVisibility(View.GONE);
				}

			}

			@Override
			public RecyclerView.ViewHolder onCreateViewHolderImpl(
					ViewGroup viewGroup,
					final ParallaxRecyclerAdapter<Status> adapter, int i) {
//				return RecyleViewHolder.getViewHolder(View.inflate(activity,
//						R.layout.item_card_profile, null));
				return RecyleViewHolder.getViewHolder(activity.getLayoutInflater().inflate(R.layout.item_card_profile,viewGroup, false));
			}

			@Override
			public int getItemCountImpl(ParallaxRecyclerAdapter<Status> adapter) {
				return statuses.size();
			}
			
			private void setImage(Status status, ImageView iv, GridView gv) {
				ArrayList<Object> pic_ids = status.pic_urls;
				String thumbnail_pic = status.original_pic;
				// 多图处理
				if (pic_ids != null && pic_ids.size() > 1) {
					iv.setVisibility(View.GONE);
					gv.setVisibility(View.VISIBLE);
					gv.setAdapter(new GridIvAdapter(pic_ids));
				}
				// 单图处理 此处判断的thumbnail_pic为空字符串，并非空引用！所以不能使用thumbnail_pic！=null来判断
				else if (!TextUtils.isEmpty(thumbnail_pic)) {
					gv.setVisibility(View.GONE);
					iv.setVisibility(View.VISIBLE);
					imageLoader.displayImage(thumbnail_pic, iv,ImageLoadeOptions.getCommonIvOption(CustomConstant.getContext()));
				}
				// 没图处理
				else {
					iv.setVisibility(View.GONE);
					gv.setVisibility(View.GONE);

				}

			}
			
		};

		adapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {
			@Override
			public void onClick(View v, int position) {
				Toast.makeText(activity, "You clicked '" + position + "'",
						Toast.LENGTH_SHORT).show();
			}
		});

		recyclerView.setLayoutManager(new LinearLayoutManager(activity));
		View header = View.inflate(activity, R.layout.profile_head,
				null);
		adapter.setParallaxHeader(header, recyclerView);
		adapter.setData(statuses);
		adapter.setOnParallaxScroll(new OnParallaxScroll() {
     
			@Override
			public void onParallaxScroll(float percentage, float offset,
					View parallax) {
				  Drawable c = actbar.getBackground();
				  c.setAlpha(Math.round(percentage * 255));
				  if(percentage > 0.5) {
					  actbar.getLeftImage().setImageResource(R.drawable.navigationbar_back_highlighted);
					  actbar.getRightImage().setImageResource(R.drawable.userinfo_navigationbar_more_highlighted);
					  actbar.getTitle().setVisibility(View.VISIBLE);
				  } else {
					  actbar.getTitle().setVisibility(View.GONE);
					  actbar.getLeftImage().setImageResource(R.drawable.redpacket_navigationbar_back);
					  actbar.getRightImage().setImageResource(R.drawable.userinfo_buttonicon_more);
				  }
			}
		});
		recyclerView.setAdapter(adapter);
	}

}
