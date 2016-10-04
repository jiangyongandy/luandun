package com.jy.xinlangweibo.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiang.library.ui.adapter.listviewadapter.ListViewDataAdapter;
import com.jiang.library.ui.adapter.listviewadapter.ViewHolderBase;
import com.jiang.library.ui.adapter.listviewadapter.ViewHolderCreator;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.presenter.StatusPresenter;
import com.jy.xinlangweibo.ui.IView.HomeFragmentView;
import com.jy.xinlangweibo.ui.activity.MainActivity;
import com.jy.xinlangweibo.ui.activity.StatusDetailsActivity;
import com.jy.xinlangweibo.ui.activity.UserShowActivity;
import com.jy.xinlangweibo.ui.activity.WriteStatusActivity;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.ui.activity.base.FragmentToolbarActivity;
import com.jy.xinlangweibo.ui.adapter.RecyleViewHolder;
import com.jy.xinlangweibo.ui.fragment.base.BaseCacheFragment;
import com.jy.xinlangweibo.utils.CommonImageLoader.ImageLoadeOptions;
import com.jy.xinlangweibo.utils.DateUtils;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.Utils;
import com.jy.xinlangweibo.utils.WeiboStringUtils;
import com.jy.xinlangweibo.widget.ninephoto.BGANinePhotoLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home2Fragment extends BaseCacheFragment implements OnClickListener, HomeFragmentView {
    @BindView(R.id.lv_status)
    PullToRefreshListView lvStatus;
    private View view;
    private View footView;
    private View tvLoad;
    private ImageView ivLoad;
    private ArrayList<Status> statusList = new ArrayList<Status>();
    private int curPage;
    private PopupWindow pw;
    private MaterialDialog materialDialog;
    private StatusPresenter statusPresenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.fragment_home, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((MainActivity) activity).getToolbar().setVisibility(View.VISIBLE);
            ((MainActivity) activity).getNavTitle().setText("首页");
            ((MainActivity) activity).getNavRightIv().setOnClickListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mCache.clear();
//		缓存数据（IO操作，新开线程）耗时操作
        mCache.put("STATUES", statusList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RecyleViewHolder.clearViewHolder();
    }

    private void initView() {
        presenter = new StatusPresenter(activity, this);
        statusPresenter = (StatusPresenter) presenter;
        initPlv();
        initPop();
        showProgressDialog();
        statusPresenter.getHomeTimeline(1);
    }

    @Override
    public void showProgressDialog() {
        materialDialog = new MaterialDialog.Builder(activity)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();
    }

    @Override
    public void dimissProgressDialog() {
        materialDialog.dismiss();
    }

    /**
     *
     */
    private void initPop() {
        View popView = LayoutInflater.from(activity).inflate(
                R.layout.pop_mainact_navright, null);
        pw = new PopupWindow(popView, Utils.dip2px(activity, 115),
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置允许在外点击消失
        pw.setOutsideTouchable(true);
        // 刷新状态
        pw.update();
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        pw.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.conversation_options_bg));
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ((MainActivity) activity).getMainmenu().getForeground().setAlpha(0); // restore
            }
        });
    }


    /**
     *
     */
    private void initPlv() {
        footView = View.inflate(activity, R.layout.status_footview_loading,
                null);
        ivLoad = (ImageView) footView.findViewById(R.id.iv_status_loading);
        tvLoad = footView.findViewById(R.id.tv_status_loading);
        adapter.setData(statusList);
        lvStatus.setAdapter(adapter);
        lvStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //前往微博详情界面
                Intent intent = new Intent(parent.getContext(), StatusDetailsActivity.class);
                //这里的position是从1开始
                intent.putExtra("Status", statusList.get(position - 1));
                parent.getContext().startActivity(intent);
            }
        });
        lvStatus.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                statusPresenter.getHomeTimeline(1);
            }

            private void loadCaheData() {

            }
        });
    }

    @Override
    public void onGetTimeLineDone() {
        if (footView.getAnimation() != null) {
            footView.getAnimation().cancel();
        }
        ivLoad.setVisibility(View.GONE);
        lvStatus.onRefreshComplete();
    }

    @Override
    public void onExecptionComplete() {
        System.out.println("请求缓存");
        ArrayList<Status> statuses = (ArrayList<Status>) mCache
                .getAsObject("STATUES");
        if (statuses != null) {
            for (Status sta : statuses) {
                statusList.add(sta);
                if (statusList.size() == 50)
                    break;
            }
            adapter.notifyDataSetChanged();
        }
        tvLoad.setVisibility(View.GONE);
    }

    @Override
    public void updateHomeTimelineList(final int page, String response) {
        if (page == 1) {
            statusList.clear();
            curPage = 1;
        }
        curPage = page;
        StatusList list = StatusList.parse(response);
        if (list == null) {
            Logger.showLog("list null", "updatehometimeline faliure");
            return;
        }
        if (null != list.statusList) {
            for (Status sta : list.statusList) {
                statusList.add(sta);
            }
            adapter.notifyDataSetChanged();
            ListView refreshableView = lvStatus.getRefreshableView();
            if (list.statusList.size() < list.total_number) {
                addFootView(refreshableView);
            } else {
                removeFootView(refreshableView);
            }
        } else {
            ((BaseActivity) getActivity()).showToast(activity.getString(R.string.CONTINUE_REFRASH));
        }
        tvLoad.setVisibility(View.VISIBLE);
    }

    private void removeFootView(ListView refreshableView) {
        if (refreshableView.getFooterViewsCount() > 1) {
            refreshableView.removeFooterView(footView);
        }
    }

    private void addFootView(ListView refreshableView) {
        if (refreshableView.getFooterViewsCount() == 1) {
            refreshableView.addFooterView(footView);
            footView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    // 开启Loading动画
                    tvLoad.setVisibility(View.GONE);
                    ivLoad.setVisibility(View.VISIBLE);
                    ivLoad.startAnimation(AnimationUtils.loadAnimation(
                            activity, R.anim.ra_loading));
                    statusPresenter.getHomeTimeline(curPage + 1);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_right_iv:
                showPopupWindow();
        }
    }

    public void showPopupWindow() {
        if (!pw.isShowing()) {
//            Logger.showLog("屏幕宽度：" + Utils.getDisplayWidthPixels(activity) + "弹窗宽度:" + View.MeasureSpec.getSize(pw.getWidth()), "计算弹窗偏移量");
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            pw.showAsDropDown(((MainActivity) activity).getToolbar(), Utils.getDisplayWidthPixels(activity) - View.MeasureSpec.getSize(pw.getWidth()) - Utils.dip2px(activity, 10), 0);
            ((MainActivity) activity).getMainmenu().getForeground().setAlpha(50); // dim
        } else {
            pw.dismiss();
        }
    }

    private ListViewDataAdapter adapter = new ListViewDataAdapter<Status>(new ViewHolderCreator<Status>() {
        @Override
        public ViewHolderBase<Status> createViewHolder(int position) {
            return new TimeLineItemViewHolderBase();
        }
    });

    public static class TimeLineItemViewHolderBase extends ViewHolderBase<Status> implements BGANinePhotoLayout.Delegate {
        //
        private static final int REQUEST_REPOST = 1;
        private static final int REQUEST_COMMENT = 2;
        @BindView(R.id.iv_head)
        ImageView ivHead;
        @BindView(R.id.tv_pubname)
        TextView tvPubname;
        @BindView(R.id.tv_from)
        TextView tvFrom;
        @BindView(R.id.tv_statuses_content)
        TextView tvStatusesContent;
        @BindView(R.id.tv_retweeted_content)
        TextView tvRetweetedContent;
        @BindView(R.id.ll_retweeted)
        LinearLayout llRetweeted;
        @BindView(R.id.tv_statuses_bottom_reweet)
        TextView tvStatusesBottomReweet;
        @BindView(R.id.tv_statuses_bottom_comment)
        TextView tvStatusesBottomComment;
        @BindView(R.id.status_unlikebtn)
        ImageView statusUnlikebtn;
        @BindView(R.id.tv_statuses_bottom_unlike)
        TextView tvStatusesBottomUnlike;
        @BindView(R.id.ll_status_unlike)
        LinearLayout llStatusUnlike;
        @BindView(R.id.ll_item_status)
        LinearLayout llItemStatus;
        @BindView(R.id.timeline_photos)
        BGANinePhotoLayout timelinePhotos;
        @BindView(R.id.retweeted_timeline_photos)
        BGANinePhotoLayout retweetedTimelinePhotos;
        @BindView(R.id.ll_btn_retweeted)
        LinearLayout llBtnRetweeted;
        @BindView(R.id.ll_btn_comment)
        LinearLayout llBtnComment;
        private ImageLoader imageLoader = ImageLoader.getInstance();
        private Context context;

        @Override
        public View createView(LayoutInflater layoutInflater) {
            View rootView = layoutInflater.inflate(R.layout.item_status2, null);
            ButterKnife.bind(this, rootView);
            context = rootView.getContext();
            return rootView;
        }

        @Override
        public void showData(int position, final Status status) {
            if (status == null)
                return;
            final User user = status.user;

            //bind publisher
            imageLoader.displayImage(user.avatar_hd, ivHead,
                    ImageLoadeOptions.getIvHeadOption());
            String from = DateUtils.getDate(status.created_at) + " 来自  "
                    + Html.fromHtml(status.source);
            tvFrom.setText(WeiboStringUtils.get2KeyText(context, from, tvFrom));
            tvPubname.setText(user.screen_name);
            ivHead.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserShowActivity.launch(context, user.screen_name);
                }
            });
            //bind content
            tvStatusesContent.setText(WeiboStringUtils.getKeyText(context, status.text,
                    tvStatusesContent));
            timelinePhotos.init((Activity) context);
            if (status.pic_urls != null) {
                for (int i = 0; i < status.pic_urls.size(); i++) {
                    status.pic_urls.set(i, status.pic_urls.get(i).replace("thumbnail", "bmiddle"));
                }
                timelinePhotos.setData(status.pic_urls);
                timelinePhotos.setDelegate(this);
            } else {
                ArrayList<String> strings = new ArrayList<>();
                timelinePhotos.setData(strings);
            }
            //bind bottomTab
            tvStatusesBottomReweet
                    .setText((CharSequence) (status.reposts_count > 0 ? ""
                            + status.reposts_count : "转发"));
            tvStatusesBottomComment
                    .setText((CharSequence) (status.comments_count > 0 ? ""
                            + status.comments_count : "评论"));
            tvStatusesBottomUnlike
                    .setText((CharSequence) (status.attitudes_count > 0 ? ""
                            + status.attitudes_count : "赞"));

            llBtnRetweeted.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    WriteStatusActivity.intentToRepost((Activity) context, status, REQUEST_REPOST);
                }
            });

            llBtnComment.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    WriteStatusActivity.intentToComment((Activity) context, status, REQUEST_COMMENT);
                }
            });

            // 点赞的特殊处理
            llStatusUnlike.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    statusUnlikebtn
                            .setImageResource(R.drawable.timeline_icon_like);
                    statusUnlikebtn.startAnimation(AnimationUtils.loadAnimation(
                            context, R.anim.scale_unlike));
                }
            });

            //bind retweeted_status
            if (status.retweeted_status != null) {

                llRetweeted.setVisibility(View.VISIBLE);
                String retweetedname = "作者已删除该微博";
                // 这里要判断 转发的微博作者是否为空 即转发的微博是否被作者删除。
                if (status.retweeted_status.user != null) {
                    if (status.retweeted_status.user.screen_name != null) {
                        retweetedname = status.retweeted_status.user.screen_name;
                    }
                }
                String tempString = "@" + retweetedname + ":"
                        + status.retweeted_status.text;
                tvRetweetedContent.setText(WeiboStringUtils.getKeyText(context, tempString,
                        tvRetweetedContent));
                retweetedTimelinePhotos.init((Activity) context);
                if (status.retweeted_status.pic_urls != null) {
                    for (int i = 0; i < status.retweeted_status.pic_urls.size(); i++) {
                        status.retweeted_status.pic_urls.set(i, status.retweeted_status.pic_urls.get(i).replace("thumbnail", "bmiddle"));
                    }
                    retweetedTimelinePhotos.setData(status.retweeted_status.pic_urls);
                    retweetedTimelinePhotos.setDelegate(this);
                } else {
                    ArrayList<String> strings = new ArrayList<>();
                    retweetedTimelinePhotos.setData(strings);
                }
//                setImage(status.retweeted_status, retweetedIv, retweetedGv, picText, tv_retweeted_pic);
            } else {
                llRetweeted.setVisibility(View.GONE);
            }

        }

        @Override
        public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
            //					前往图片浏览器
            Intent intent = new Intent(view.getContext(), FragmentToolbarActivity.class);
            intent.putExtra("Pic_urls", (ArrayList) models);
            intent.putExtra("Position", position);
            FragmentToolbarActivity.launch(view.getContext(), ImageBrowserFragment.class, intent, false);
        }

        @Override
        public boolean onLongClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
            return false;
        }
    }
}
