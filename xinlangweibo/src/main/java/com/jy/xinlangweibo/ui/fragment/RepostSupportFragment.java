package com.jy.xinlangweibo.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiang.library.ui.adapter.recyleviewadapter.ARecycleViewItemView;
import com.jiang.library.ui.adapter.recyleviewadapter.BasicRecycleViewAdapter;
import com.jiang.library.ui.adapter.recyleviewadapter.IITemView;
import com.jiang.library.ui.adapter.recyleviewadapter.IItemViewCreator;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.constant.AccessTokenKeeper;
import com.jy.xinlangweibo.models.net.sinaapi.BaseObserver;
import com.jy.xinlangweibo.models.net.sinaapi.StatusInteraction;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.RepostBean;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.RepostListBean;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.UserBean;
import com.jy.xinlangweibo.ui.fragment.base.LazySupportFragment;
import com.jy.xinlangweibo.utils.CommonImageLoader.ImageLoadeOptions;
import com.jy.xinlangweibo.utils.DateUtils;
import com.jy.xinlangweibo.utils.ToastUtils;
import com.jy.xinlangweibo.utils.WeiboStringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action0;

/**
 * Created by JIANG on 2016/11/13.
 */

public class RepostSupportFragment extends LazySupportFragment {

    private static final String STATUSID = "status_id";
    @BindView(R.id.plrv_comment)
    RecyclerView plrvComment;

    private ArrayList<RepostBean> comments = new ArrayList();
    private BasicRecycleViewAdapter adapter;
    private long statusId;

    public static RepostSupportFragment newInstance(long StatusId) {
        RepostSupportFragment fragment = new RepostSupportFragment();
        Bundle args = new Bundle();
        args.putLong(STATUSID, StatusId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int CreateView() {
        return R.layout.fragment_comment;
    }

    @Override
    protected void initViewAndEvent(View rootView) {
        statusId = getArguments().getLong(STATUSID,0);
        if(statusId == 0) {
            ToastUtils.show(getContext(),"参数错误,尝试刷新", Toast.LENGTH_SHORT);
        }
        adapter = new CommentAdapter(new IItemViewCreator<RepostBean>() {
            @Override
            public View newContentView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
                return layoutInflater.inflate(R.layout.item_comments, parent, false);
            }

            @Override
            public IITemView<RepostBean> newItemView(View convertView, int viewType) {
                return new CommentItem(getContext(),convertView);
            }
        }, comments, getActivity());
        plrvComment.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        plrvComment.setLayoutManager(linearLayoutManager);
        super.initViewAndEvent(rootView);
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        isPrepared = false;
        loadData();
    }

    @Override
    protected void loadData() {
        StatusInteraction.getInstance(getContext()).statusesRepost_timeline(AccessTokenKeeper.readAccessToken(getContext()).getToken(),
                statusId,
                new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                },
                new BaseObserver<RepostListBean>() {
                    @Override
                    public void onNext(RepostListBean models) {
                        super.onNext(models);
                        if(models.reposts.isEmpty()) {
                            showErrorMessage();
                            return;
                        }
                        comments.clear();
                        comments.addAll(models.reposts);
                        dismissLoading();
                        updateUi();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        showErrorMessage();
                    }
                }
        );
    }

    @Override
    protected void updateUi() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected View getLoadingTargetView() {
        return plrvComment;
    }

    public static class CommentAdapter extends BasicRecycleViewAdapter {
        public CommentAdapter(IItemViewCreator itemViewCreator, List datas, Activity activity) {
            super(itemViewCreator, datas, activity);
        }
    }

    public static class CommentItem extends ARecycleViewItemView<RepostBean> {
        //        R.layout.item_comments
        @BindView(R.id.iv_head)
        ImageView ivHead;
        @BindView(R.id.tv_pubname)
        TextView tvPubname;
        @BindView(R.id.tv_from)
        TextView tvFrom;
        @BindView(R.id.tv_statuses_content)
        TextView tvStatusesContent;
        @BindView(R.id.ll_item_status)
        RelativeLayout llItemStatus;
        private ImageLoader imageLoader;

        public CommentItem(Context context, View itemView) {
            super(context,itemView);
            imageLoader = ImageLoader.getInstance();
        }

        @Override
        public void onBindData(View convertView, RepostBean comment, int position) {
            UserBean user = comment.user;

            llItemStatus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });
            if (user != null) {
                imageLoader.displayImage(user.avatar_hd, ivHead,
                        ImageLoadeOptions.getIvHeadOption());
                tvFrom.setText(DateUtils.getDate(comment.created_at) + " 来自 "
                        + Html.fromHtml(comment.source));
                tvPubname.setText(user.screen_name);
            }
            tvStatusesContent.setText(WeiboStringUtils.getKeyText(context, comment.text,
                    tvStatusesContent));
        }
    }

}
