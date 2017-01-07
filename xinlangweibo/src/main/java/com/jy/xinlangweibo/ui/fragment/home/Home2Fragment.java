package com.jy.xinlangweibo.ui.fragment.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.jiang.library.ui.widget.BasePopupWindow;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.db.StatusBeanDB;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.StatusBean;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.StatusListBean;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.UserBean;
import com.jy.xinlangweibo.presenter.StatusPresenter;
import com.jy.xinlangweibo.ui.IView.HomeFragmentView;
import com.jy.xinlangweibo.ui.activity.MainActivity;
import com.jy.xinlangweibo.ui.activity.StatusDetails2Activity;
import com.jy.xinlangweibo.ui.activity.UserShowActivity;
import com.jy.xinlangweibo.ui.activity.WriteStatusActivity;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.ui.activity.base.FragmentToolbarActivity;
import com.jy.xinlangweibo.ui.adapter.RecyleViewHolder;
import com.jy.xinlangweibo.ui.fragment.ImageBrowserFragment;
import com.jy.xinlangweibo.ui.fragment.base.LazySupportFragment;
import com.jy.xinlangweibo.ui.fragment.dialog.SearchDialogFragment;
import com.jy.xinlangweibo.utils.CommonImageLoader.ImageLoadeOptions;
import com.jy.xinlangweibo.utils.DateUtils;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.Utils;
import com.jy.xinlangweibo.utils.WeiboStringUtils;
import com.jy.xinlangweibo.widget.ninephoto.BGANinePhotoLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home2Fragment extends LazySupportFragment implements OnClickListener, HomeFragmentView {
    @BindView(R.id.lv_status)
    PullToRefreshListView lvStatus;
    private View view;
    private View footView;
    private View tvLoad;
    private ImageView ivLoad;
    private ArrayList<StatusBean> publicTimeLineList = new ArrayList<StatusBean>();
    private int curPage;
    private PopupWindow pw;
    private MaterialDialog materialDialog;
    private StatusPresenter statusPresenter;

    @Override
    protected int CreateView() {
        return R.layout.fragment_home;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden) {
            isVisible = false;
        }else {
            isVisible = true;
        }
        if (!hidden) {
            ((MainActivity)activity).getToolbar().setVisibility(View.VISIBLE);
            ((MainActivity) activity).getNavTitle().setText("首页");
            ((MainActivity) activity).getNavRightIv().setOnClickListener(this);
            lazyLoad();//只会加载一次
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        StatusBeanDB.getInstance(activity).deleteStatusBeanList(Long.valueOf(activity.getAccessAccessToken().getUid()),StatusBeanDB.publicTimelineCacheType);
        Logger.showLog("存publicTimeline",""+this);
        StatusBeanDB.getInstance(activity).insertStatusBeanList(publicTimeLineList,Long.valueOf(activity.getAccessAccessToken().getUid()),StatusBeanDB.publicTimelineCacheType);
    }

//    被系统强制清除不会调用该方法，mainactivity因为home键改写所以只能被系统强制杀死
//    如果要finish则需另处理（使得mainactivity正常退出）
    @Override
    public void onDestroy() {
        super.onDestroy();
        RecyleViewHolder.clearViewHolder();
    }

    @Override
    protected void initViewAndEvent(View rootView) {
        super.initViewAndEvent(rootView);
        presenter = new StatusPresenter(activity, this);
        statusPresenter = (StatusPresenter) presenter;
        initPlv();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared)
            return;
        isPrepared = false;
        showLoading();
        loadData();
    }

    @Override
    protected void loadData() {
        super.loadData();
        statusPresenter.getHomeTimeline(1);
    }

    @Override
    protected View getLoadingTargetView() {
        return lvStatus;
    }

    private void getCache() {
        ArrayList<StatusBean> statuses = (ArrayList<StatusBean>) StatusBeanDB.getInstance(activity).queryStatusBeanList(Long.valueOf(activity.getAccessAccessToken().getUid()),StatusBeanDB.publicTimelineCacheType);
        if(statuses == null) {
            Logger.showLog("没有得到缓存publicTimeline",""+this);
        }else {
            publicTimeLineList = statuses;
            Logger.showLog("得到缓存publicTimeline",""+this);
        }
    }

    @Override
    public void showProgressDialog() {
//        materialDialog = new MaterialDialog.Builder(activity)
//                .content(R.string.please_wait)
//                .progress(true, 0)
//                .progressIndeterminateStyle(true)
//                .show();
    }

    @Override
    public void dimissProgressDialog() {
        materialDialog.dismiss();
    }

    /**
     *
     */
    private void initPlv() {
        footView = View.inflate(activity, R.layout.status_footview_loading,
                null);
        ivLoad = (ImageView) footView.findViewById(R.id.iv_status_loading);
        tvLoad = footView.findViewById(R.id.tv_status_loading);

        adapter.setData(publicTimeLineList);
        lvStatus.setAdapter(adapter);
        View headView = activity.getLayoutInflater().inflate(R.layout.layout_custom_searchview,lvStatus,false);
        headView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDialogFragment.getSearchDialogFragment().show(activity.getSupportFragmentManager(),"SearchDialogFragment");
            }
        });
        lvStatus.getmRefreshableView().addHeaderView(headView);
        lvStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StatusDetails2Activity.intent2StatusDetails(parent.getContext(),publicTimeLineList.get(position-2));
            }
        });
        lvStatus.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                statusPresenter.getHomeTimeline(1);
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
        System.out.println("请求异常");
        tvLoad.setVisibility(View.GONE);
        showErrorMessage();
    }

    @Override
    public void updateHomeTimelineList(final int page, StatusListBean statusList) {
        dismissLoading();
        if (page == 1) {
            publicTimeLineList.clear();
            curPage = 1;
        }
        curPage = page;
        if (statusList == null) {
            Logger.showLog("list null", "updatehometimeline faliure");
            return;
        }
        if (null != statusList.statuses && statusList.statuses.size() > 0) {
            List<StatusBean> statuses = statusList.statuses;
            for (StatusBean sta : statuses) {
                publicTimeLineList.add(sta);
            }
            adapter.notifyDataSetChanged();
            ListView refreshableView = lvStatus.getRefreshableView();
            if (statuses.size() < statusList.total_number) {
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
//            Logger.showLog("屏幕宽度：" + Utils.getDisplayWidthPixels(activity) + "弹窗宽度:" + View.MeasureSpec.getSize(pw.getWidth()), "计算弹窗偏移量");
        if (pw == null)
            pw = new BasePopupWindow.Builder(activity)
                    .setBackground(getResources().getDrawable(R.drawable.conversation_options_bg))
                    .setPopupWindowView(R.layout.pop_mainact_navright)
                    .setWidth(Utils.dip2px(activity, 115))
                    .build();
        pw.showAsDropDown(((MainActivity) activity).getToolbar(), Utils.getDisplayWidthPixels(activity) - View.MeasureSpec.getSize(pw.getWidth()) - Utils.dip2px(activity, 10), 0);
    }

    private ListViewDataAdapter adapter = new ListViewDataAdapter<StatusBean>(new ViewHolderCreator<StatusBean>() {
        @Override
        public ViewHolderBase<StatusBean> createViewHolder(int position) {
            return new TimeLineItemViewHolderBase();
        }
    });

    public static class TimeLineItemViewHolderBase extends ViewHolderBase<StatusBean> implements BGANinePhotoLayout.Delegate {
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
        public void showData(int position, final StatusBean status) {
            if (status == null)
                return;
            final UserBean user = status.user;

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
                    status.getPic_urls2().set(i, status.getPic_urls2().get(i).replace("thumbnail", "bmiddle"));
                }
                timelinePhotos.setData(status.getPic_urls2());
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
                        status.retweeted_status.getPic_urls2().set(i, status.retweeted_status.getPic_urls2().get(i).replace("thumbnail", "bmiddle"));
                    }
                    retweetedTimelinePhotos.setData(status.retweeted_status.getPic_urls2());
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
