package com.jy.xinlangweibo.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiang.library.ui.adapter.recyleviewadapter.AHeaderItemViewCreator;
import com.jiang.library.ui.adapter.recyleviewadapter.ARecycleViewItemView;
import com.jiang.library.ui.adapter.recyleviewadapter.BasicRecycleViewAdapter;
import com.jiang.library.ui.adapter.recyleviewadapter.IITemView;
import com.jiang.library.ui.adapter.recyleviewadapter.IItemViewCreator;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.retrofitservice.StatusInteraction;
import com.jy.xinlangweibo.models.retrofitservice.bean.StatusListBean;
import com.jy.xinlangweibo.models.retrofitservice.bean.StatusesBean;
import com.jy.xinlangweibo.models.retrofitservice.bean.UsersShowBean;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.ui.activity.base.FragmentToolbarActivity;
import com.jy.xinlangweibo.ui.fragment.ImageBrowserFragment;
import com.jy.xinlangweibo.utils.CommonImageLoader.CustomImageLoader;
import com.jy.xinlangweibo.utils.CommonImageLoader.ImageLoadeOptions;
import com.jy.xinlangweibo.utils.DateUtils;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.ToastUtils;
import com.jy.xinlangweibo.utils.Utils;
import com.jy.xinlangweibo.utils.WeiboStringUtils;
import com.jy.xinlangweibo.widget.PulltorefreshRecyclerView;
import com.jy.xinlangweibo.widget.ninephoto.BGANinePhotoLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

/**
 * Created by JIANG on 2016/9/30.
 */
public class UserShowActivity extends BaseActivity {
    private int layoutRes = R.layout.fragment_profile;

    private PulltorefreshRecyclerView mRecyclerView;
    private BasicRecycleViewAdapter<StatusesBean> adapter;
    private List<StatusesBean> timeLineDataList = new ArrayList<>();
    private ProfileTimelineHeaderItemView headerItemView;


    public static void launch(Context from, String screen_name) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("com.jy.xinlangweibo.usershow://"));
        intent.putExtra("screen_name", screen_name);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutRes);
        String screen_name = (String) getIntent().getSerializableExtra("screen_name");
        if (screen_name == null) {
            ToastUtils.show(this, "请求错误，在尝试一次", Toast.LENGTH_SHORT);
            this.finish();
        }
        mRecyclerView = (PulltorefreshRecyclerView) findViewById(R.id.id_recyclerview);
        adapter = new BasicRecycleViewAdapter<StatusesBean>(new IItemViewCreator<StatusesBean>() {
            @Override
            public View newContentView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
                return layoutInflater.inflate(R.layout.item_card_profile, parent, false);
            }

            @Override
            public IITemView<StatusesBean> newItemView(View convertView, int viewType) {
                return new ProfileTimeLineItemView(UserShowActivity.this, convertView);
            }
        }, timeLineDataList);
        adapter.setHeaderItemViewCreator(new AHeaderItemViewCreator<UsersShowBean>() {
            @Override
            public int[][] createHeaders() {
                return new int[][]{{R.layout.profile_head, 100}};
            }

            @Override
            public IITemView<UsersShowBean> newItemView(View convertView, int viewType) {
                return headerItemView = new ProfileTimelineHeaderItemView(UserShowActivity.this, convertView);
            }
        });
        mRecyclerView.setAdapter(adapter);
        StatusInteraction.getInstance().userShow(getAccessAccessToken().getToken(), screen_name, new Observer<UsersShowBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.showLog("ON ERROR-------------"+e.toString(),"userShow");
                ToastUtils.show(UserShowActivity.this, "请求错误，在尝试一次", Toast.LENGTH_SHORT);
            }

            @Override
            public void onNext(UsersShowBean usersShowBean) {
                Logger.showLog("ON NEXT-------------"+new Gson().toJson(usersShowBean),"userShow");
                headerItemView.onBindData(null,usersShowBean,0);
            }
        });
        StatusInteraction.getInstance().statusesUser_timeline(getAccessAccessToken().getToken(), screen_name, new Observer<StatusListBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.showLog("ON ERROR-------------"+e.toString(),"userShow");
                ToastUtils.show(UserShowActivity.this, "请求错误，在尝试一次", Toast.LENGTH_SHORT);
            }

            @Override
            public void onNext(StatusListBean statusList) {
                Logger.showLog("ON NEXT-------------"+new Gson().toJson(statusList),"userShow");
                if(statusList == null)
                    return;
                if(statusList.getStatuses() != null) {
                    for (StatusesBean sta : statusList.getStatuses()) {
                        timeLineDataList.add(sta);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public static class ProfileTimeLineItemView extends ARecycleViewItemView<StatusesBean> implements BGANinePhotoLayout.Delegate {
        @BindView(R.id.iv_head)
        ImageView ivHead;
        @BindView(R.id.tv_pubname)
        TextView tvPubname;
        @BindView(R.id.tv_from)
        TextView tvFrom;
        @BindView(R.id.tv_statuses_content)
        TextView tvStatusesContent;
        @BindView(R.id.timeline_photos)
        BGANinePhotoLayout timelinePhotos;
        @BindView(R.id.tv_retweeted_content)
        TextView tvRetweetedContent;
        @BindView(R.id.retweeted_timeline_photos)
        BGANinePhotoLayout retweetedTimelinePhotos;
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
        @BindView(R.id.card)
        CardView card;
        private ImageLoader imageLoader = ImageLoader.getInstance();

        public ProfileTimeLineItemView(Context context, View itemView) {
            super(context, itemView);
        }

        @Override
        public void onBindData(View convertView, StatusesBean status, int position) {
            if (status == null)
                return;
            User user = status.user;

            //bind publisher
            imageLoader.displayImage(user.avatar_hd, ivHead,
                    ImageLoadeOptions.getIvHeadOption());
            String from = DateUtils.getDate(status.created_at) + " 来自  "
                    + Html.fromHtml(status.source);
            tvFrom.setText(WeiboStringUtils.get2KeyText(context, from, tvFrom));
            tvPubname.setText(user.screen_name);
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
//            setImage(status, statusIv, statusGv, picText, tv_retweeted_pic);
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

            // 点赞的特殊处理
            llStatusUnlike.setOnClickListener(new View.OnClickListener() {

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

    public class ProfileTimelineHeaderItemView extends ARecycleViewItemView<UsersShowBean> {
        @BindView(R.id.iv_head)
        ImageView ivHead;
        @BindView(R.id.tv_screen_name)
        TextView tvScreenName;
        @BindView(R.id.tv_location)
        TextView tvLocation;
        @BindView(R.id.tv_created_at)
        TextView tvCreatedAt;


        public ProfileTimelineHeaderItemView(Context context, View itemView) {
            super(context, itemView);
            ButterKnife.bind(this,itemView);
            View head = itemView.findViewById(R.id.head);
            View headBackground = itemView.findViewById(R.id.headBackground);
            mRecyclerView.setMheadView(head);
            mRecyclerView.setMheadBackground(headBackground);
        }

        @Override
        public void onBindData(View convertView, UsersShowBean model, int position) {
            CustomImageLoader.displayImage(UserShowActivity.this,
            ivHead,
            model.getAvatar_large(),
            R.drawable.avatar_default,
            R.drawable.avatar_default,
            Utils.dip2px(UserShowActivity.this,50),Utils.dip2px(UserShowActivity.this,50));
            tvScreenName.setText(model.getScreen_name());
            tvLocation.setText(model.getLocation());
            tvCreatedAt.setText(model.getCreated_at());
        }
    }

}
