package com.jy.xinlangweibo.ui.adapter;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.ui.activity.base.FragmentToolbarActivity;
import com.jy.xinlangweibo.ui.fragment.ImageBrowserFragment;
import com.jy.xinlangweibo.utils.CommonImageLoader.CustomImageLoader;
import com.jy.xinlangweibo.utils.DateUtils;
import com.jy.xinlangweibo.utils.WeiboStringUtils;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;

/**
 * Created by JIANG on 2016/9/10.
 */
public class HomeTimeLineAdapter extends BaseAdapter {
    private ArrayList<Status> list;
    private BaseActivity context;
    private final int MAXIMAGE = 250;

    public HomeTimeLineAdapter(ArrayList<Status> list) {
        this.list = list;
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
        context = (BaseActivity) parent.getContext();
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_status, null);
            vh = ViewHolder.getViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        //publisher
        TextView statusName = vh.getView(R.id.tv_pubname);
        TextView sourceText = vh.getView(R.id.tv_from);
        ImageView headIv = vh.getView(R.id.iv_head);
        //content
        TextView statusText = vh.getView(R.id.tv_statuses_content);
        TextView picText = vh.getView(R.id.tv_pic);
        TextView tv_retweeted_pic = vh.getView(R.id.tv_retweeted_pic);
        ImageView statusIv = vh.getView(R.id.iv_statuses_singlecontent);
        GridView statusGv = vh.getView(R.id.gv_statuses_contents);
        //retweed
        LinearLayout retweeted = vh.getView(R.id.ll_retweeted);
        TextView retweetedText = vh.getView(R.id.tv_retweeted_content);
        ImageView retweetedIv = vh.getView(R.id.iv_retweeted_singlecontent);
        GridView retweetedGv = vh.getView(R.id.gv_retweeted_contents);
        //bottomTab
        TextView bottomretweetedText = vh
                .getView(R.id.tv_statuses_bottom_reweet);
        TextView bottomcommentText = vh
                .getView(R.id.tv_statuses_bottom_comment);
        TextView bottomunlikeText = vh.getView(R.id.tv_statuses_bottom_unlike);
        // 点赞的特殊处理
        final View ll_status_unlike = vh.getView(R.id.ll_status_unlike);
        final ImageView status_unlikebtn = vh.getView(R.id.status_unlikebtn);
        ll_status_unlike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                status_unlikebtn
                        .setImageResource(R.drawable.timeline_icon_like);
                status_unlikebtn.startAnimation(AnimationUtils.loadAnimation(
                        context, R.anim.scale_unlike));
            }
        });

        // bind data
        final Status status = getItem(position);
        User user = status.user;

        //bind publisher
//        imageLoader.displayImage(user.avatar_hd, headIv,
//                ImageLoadeOptions.getIvHeadOption());
        String from = DateUtils.getDate(status.created_at) + " 来自  "
                + Html.fromHtml(status.source);
        sourceText.setText(WeiboStringUtils.get2KeyText(context, from, sourceText));
        statusName.setText(user.screen_name);
        //bind content
        statusText.setText(WeiboStringUtils.getKeyText(context, status.text,
                statusText));
        setImage(status, statusIv, statusGv, picText, tv_retweeted_pic);
        //bind bottomTab
        bottomretweetedText
                .setText((CharSequence) (status.reposts_count > 0 ? ""
                        + status.reposts_count : "转发"));
        bottomcommentText
                .setText((CharSequence) (status.comments_count > 0 ? ""
                        + status.comments_count : "评论"));
        bottomunlikeText
                .setText((CharSequence) (status.attitudes_count > 0 ? ""
                        + status.attitudes_count : "赞"));
        //bind retweeted_status
        if (status.retweeted_status != null) {

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
            retweetedText.setText(WeiboStringUtils.getKeyText(context, tempString,
                    retweetedText));
            setImage(status.retweeted_status, retweetedIv, retweetedGv, picText, tv_retweeted_pic);
        } else {
            retweeted.setVisibility(View.GONE);
        }

        return vh.getMconvertView();
    }

    private void setImage(final Status status, final ImageView iv, GridView gv, final TextView picText, final TextView tv_retweeted_pic) {
        final ArrayList<String> pic_ids = status.pic_urls;
        tv_retweeted_pic.setVisibility(View.GONE);
        picText.setVisibility(View.GONE);
        if (pic_ids != null) {
            for (int i = 0; i < pic_ids.size(); i++) {
                pic_ids.set(i, pic_ids.get(i).replace("thumbnail", "bmiddle"));
            }
            // 多图处理
            if (pic_ids.size() > 1) {
                iv.setVisibility(View.GONE);
                gv.setVisibility(View.VISIBLE);
                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
//					前往图片浏览器
                        Intent intent = new Intent(context, FragmentToolbarActivity.class);
                        intent.putExtra("Pic_urls", pic_ids);
                        intent.putExtra("Position", position);
                        FragmentToolbarActivity.launch(context, ImageBrowserFragment.class,intent,false);
                    }
                });
                gv.setAdapter(new GridIvAdapter(pic_ids));
            }
            // 单图处理
            else if (pic_ids.size() == 1 && !TextUtils.isEmpty(status.pic_urls.get(0))) {
                gv.setVisibility(View.GONE);
                iv.setVisibility(View.VISIBLE);
                if ( status.pic_urls.get(0).toLowerCase().endsWith(".gif")) {
                    switch (iv.getId()) {
                        case R.id.iv_statuses_singlecontent:
                            picText.setVisibility(View.VISIBLE);
                            picText.setText("GIF");
                        case R.id.iv_retweeted_singlecontent:
                            tv_retweeted_pic.setVisibility(View.VISIBLE);
                            tv_retweeted_pic.setText("GIF");
                    }
                }
                iv.setOnClickListener(new View.OnClickListener() {

                    //				点击跳转至图片浏览器
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, FragmentToolbarActivity.class);
                        intent.putExtra("Pic_urls", status.pic_urls);
                        intent.putExtra("Position", 0);
                        FragmentToolbarActivity.launch(context, ImageBrowserFragment.class,intent,false);
                    }
                });
                CustomImageLoader.displayImage(context,iv,status.pic_urls.get(0),R.drawable.timeline_image_loading,R.drawable.timeline_image_failure,0,0);
            }
        }
        // 没图处理
        else {
            iv.setVisibility(View.GONE);
            gv.setVisibility(View.GONE);
        }
    }
}
