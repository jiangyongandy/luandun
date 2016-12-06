package com.jy.xinlangweibo.ui.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.api.SimpleRequestlistener;
import com.jy.xinlangweibo.constant.AccessTokenKeeper;
import com.jy.xinlangweibo.models.net.StatusesInteraction;
import com.jy.xinlangweibo.models.net.impl.StatusesInteractionImpl;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.ui.adapter.CommentsStatusAdapter;
import com.jy.xinlangweibo.ui.adapter.GridIvAdapter;
import com.jy.xinlangweibo.ui.adapter.ViewHolder;
import com.jy.xinlangweibo.utils.CommonImageLoader.ImageLoadeOptions;
import com.jy.xinlangweibo.utils.DateUtils;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.TitleBuilder;
import com.jy.xinlangweibo.utils.WeiboStringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;

public class StatusDetailsActivity extends BaseActivity {

	private Status status;
	private View footView;
	private long id;
	private ArrayList<Comment> list = new ArrayList<Comment>();
	private ViewHolder vh;
	private CommentsStatusAdapter adapter;
	private ImageLoader imageLoader;
	private PullToRefreshListView pl2relv;
	private int curPage;
	private ImageView ivLoad;
	private View headView;
	private View hiddentab;
	private TextView hidecomments;
	private TextView hidelikes;
	private TextView hideretweets;
	private TextView comments;
	private TextView likes;
	private TextView retweets;
	private CommentList commentList;
	private ArrayList<Comment> tempcomments;
	private View head2View;
	private View footview_nocomments;
	private TextView nocomments;
	private TextView tvLoad;
	private View footView2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status_details);
		status = (Status) getIntent().getSerializableExtra("Status");
		id = status.id;
		vh = ViewHolder.getViewHolder(this);
		imageLoader = ImageLoader.getInstance();
		// 初始化原文微博 及给 评论列表设置适配器
		initView();
		// 加载评论
		loadData(1, id);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ViewHolder.clearViewHolder();
	}

	private void loadData(final int page, long id) {
		moreComments();
		Oauth2AccessToken readAccessToken = AccessTokenKeeper
				.readAccessToken(this);
		StatusesInteraction api = new StatusesInteractionImpl(this, readAccessToken);
		api.showComments(page, id, new SimpleRequestlistener(this, null) {
			@Override
			public void onComplete(String response) {
				super.onComplete(response);
				addData(page, response);
				pl2relv.onRefreshComplete();
				if (footView.getAnimation() != null) {
					footView.getAnimation().cancel();
				}
			}
		});
	}

	private void moreComments() {
		tvLoad.setVisibility(View.VISIBLE);
		ivLoad.setVisibility(View.VISIBLE);
		footview_nocomments.setVisibility(View.GONE);
		nocomments.setVisibility(View.GONE);
		ivLoad.startAnimation(AnimationUtils.loadAnimation(
				StatusDetailsActivity.this, R.anim.ra_loading));
	}

	private void addData(final int page, String response) {
		if (page == 1) {
			list.clear();
			curPage = 1;
		}
		curPage = page;
		commentList = CommentList.parse(response);
		tempcomments = commentList.commentList;
		if (tempcomments != null) {
			for (Comment sta : tempcomments) {
				list.add(sta);
			}
			adapter.notifyDataSetChanged();
			if (tempcomments.size() != 0
					&& tempcomments.size() < commentList.total_number) {
				addLoadFootView(pl2relv.getRefreshableView());
			} else if (tempcomments.size() == commentList.total_number) {
				noMoreComments();
			}
		} else if (commentList.total_number == 0) {
			// 这里用默认布局填充adapter无数据时的BUG（界面显示异常。。）
			Comment defaultcomment = new Comment();
			list.add(defaultcomment);
			noComments();
		} else if (commentList.total_number != 0) {
			noMoreComments();
		}
	}

	private void noComments() {
		tvLoad.setVisibility(View.GONE);
		ivLoad.setVisibility(View.GONE);
		footview_nocomments.setVisibility(View.VISIBLE);
		nocomments.setVisibility(View.VISIBLE);
		nocomments.setText("还没有人发表评论哦。");
	}

	private void noMoreComments() {
		tvLoad.setVisibility(View.GONE);
		ivLoad.setVisibility(View.GONE);
		footview_nocomments.setVisibility(View.VISIBLE);
		nocomments.setVisibility(View.VISIBLE);
		nocomments.setText("没有更多评论了。");
		// removeFootView(pl2relv.getRefreshableView());
	}

	private void initView() {
		initTitle();
		initplv();
		initStatusandBottemTab();
	}

	private void initplv() {
		adapter = new CommentsStatusAdapter(list, null);
		pl2relv = vh.getView(R.id.lv_status_details);
		footView = View.inflate(this, R.layout.status_details_footview_loading,
				null);
		headView = View.inflate(this, R.layout.status_details_head, null);
		head2View = View
				.inflate(this, R.layout.include_status_detail_tab, null);
		hiddentab = findViewById(R.id.hidden_details_tab);
//		解决在某些安卓版本 机器  上背景透明。。。
		hiddentab.setBackgroundColor(getResources().getColor(R.color.white));
		
		comments = (TextView) head2View.findViewById(R.id.rb_comments);
		likes = (TextView) head2View.findViewById(R.id.rb_likes);
		retweets = (TextView) head2View.findViewById(R.id.rb_retweets);
		hidecomments = (TextView) hiddentab.findViewById(R.id.rb_comments);
		hidelikes = (TextView) hiddentab.findViewById(R.id.rb_likes);
		hideretweets = (TextView) hiddentab.findViewById(R.id.rb_retweets);
		ivLoad = (ImageView) footView.findViewById(R.id.iv_loading);
		tvLoad = (TextView) footView.findViewById(R.id.tv_loading);
		footview_nocomments = footView.findViewById(R.id.footview_nocomments);
		nocomments = (TextView) footView
				.findViewById(R.id.tv_footview_nocomments);
		Logger.showLog("" + pl2relv.getRefreshableView().getFooterViewsCount(),
				"detailsactivity");
		addLoadFootView(pl2relv.getRefreshableView());
		pl2relv.getRefreshableView().addHeaderView(headView);
		pl2relv.getRefreshableView().addHeaderView(head2View);
		pl2relv.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		pl2relv.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData(1, id);
			}
		});
		pl2relv.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				if (commentList != null&&list.size() < commentList.total_number ) {
					loadData(curPage + 1, id);
				}
			}

		});
		pl2relv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				hiddentab.setVisibility(firstVisibleItem >= 2 ? View.VISIBLE
						: View.GONE);
			}
		});
	}

	private void removeFootView(ListView refreshableView) {
		// 取消LOADING动画
		if (footView.getAnimation() != null) {
			footView.getAnimation().cancel();
		}
		if (refreshableView.getFooterViewsCount() > 1) {
			refreshableView.removeFooterView(footView);
		}
	}

	private void addLoadFootView(ListView refreshableView) {
		if (refreshableView.getFooterViewsCount() == 0) {
			refreshableView.addFooterView(footView);
		}
	}

	private void initStatusandBottemTab() {
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
		TextView bottomunlikeText = vh.getView(R.id.tv_statuses_bottom_unlike);

		// bind data
		User user = status.user;

		View itemStatus = vh.getView(R.id.ll_item_status);
		itemStatus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new
				// Intent(StatusDetailsActivity.this,StatusDetailsActivity.class);
				// intent.putExtra("Status",status);
				// StatusDetailsActivity.this.startActivity(intent);
			}
		});

		imageLoader.displayImage(user.avatar_hd, headIv,
				ImageLoadeOptions.getIvHeadOption());
		sourceText.setText(DateUtils.getDate(status.created_at) + " 来自 "
				+ Html.fromHtml(status.source));
		statusName.setText(user.screen_name);

		statusText.setText(WeiboStringUtils
				.getKeyText(this, status.text, statusText));
		setImage(status, statusIv, statusGv);

		retweets.setText((CharSequence) (status.reposts_count > 0 ? "转发 "
				+ status.reposts_count : "转发"));
		comments.setText((CharSequence) (status.comments_count > 0 ? "评论 "
				+ status.comments_count : "评论"));
		likes.setText((CharSequence) (status.attitudes_count > 0 ? "赞 "
				+ status.attitudes_count : "赞"));

		hideretweets.setText((CharSequence) (status.reposts_count > 0 ? "转发 "
				+ status.reposts_count : "转发"));
		hidecomments.setText((CharSequence) (status.comments_count > 0 ? "评论 "
				+ status.comments_count : "评论"));
		hidelikes.setText((CharSequence) (status.attitudes_count > 0 ? "赞 "
				+ status.attitudes_count : "赞"));
		// 点赞的特殊处理
		final View ll_status_unlike = vh.getView(R.id.ll_status_unlike);
		final ImageView status_unlikebtn = vh.getView(R.id.status_unlikebtn);
		ll_status_unlike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				status_unlikebtn
						.setImageResource(R.drawable.timeline_icon_like);
				status_unlikebtn.startAnimation(AnimationUtils.loadAnimation(
						StatusDetailsActivity.this, R.anim.scale_unlike));
			}
		});

		if (status.retweeted_status != null) {
			LinearLayout retweeted = vh.getView(R.id.ll_retweeted);
			TextView retweetedText = vh.getView(R.id.tv_retweeted_content);
			ImageView retweetedIv = vh.getView(R.id.iv_retweeted_singlecontent);
			GridView retweetedGv = vh.getView(R.id.gv_retweeted_contents);

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
			retweetedText.setText(WeiboStringUtils.getKeyText(this, tempString,
					retweetedText));
			setImage(status.retweeted_status, retweetedIv, retweetedGv);
		} else {
			LinearLayout retweeted = vh.getView(R.id.ll_retweeted);
			retweeted.setVisibility(View.GONE);
		}
	}

	private void initTitle() {
		new TitleBuilder(this).setTitle("微博详情")
				.setLeftimg(R.drawable.icon_navbarback_gra2ora_sel)
				.setLeftOnclickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						StatusDetailsActivity.this.finish();
					}
				});
	}

	private void setImage(Status status, ImageView iv, GridView gv) {
		ArrayList<String> pic_ids = status.pic_urls;
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
			imageLoader.displayImage(thumbnail_pic, iv);
		}
		// 没图处理
		else {
			iv.setVisibility(View.GONE);
			gv.setVisibility(View.GONE);

		}
	}
}
