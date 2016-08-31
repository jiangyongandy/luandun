package com.jy.xinlangweibo.ui.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.ui.activity.ImageBrowseActivity;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.utils.DateUtils;
import com.jy.xinlangweibo.utils.ImageLoadeOptions;
import com.jy.xinlangweibo.utils.ImageUtils;
import com.jy.xinlangweibo.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;

public class StatusesAdapter extends BaseAdapter {
    private ArrayList<Status> list;
    private ImageLoader imageLoader;
    private BaseActivity context;
    private final int MAXIMAGE = 250;

    public StatusesAdapter(ArrayList<Status> list) {
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
        ll_status_unlike.setOnClickListener(new OnClickListener() {

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
        imageLoader.displayImage(user.avatar_hd, headIv,
                ImageLoadeOptions.getIvHeadOption());
        String from = DateUtils.getDate(status.created_at) + " 来自  "
                + Html.fromHtml(status.source);
        sourceText.setText(StringUtils.get2KeyText(context, from, sourceText));
        statusName.setText(user.screen_name);
        //bind content
        statusText.setText(StringUtils.getKeyText(context, status.text,
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
            retweetedText.setText(StringUtils.getKeyText(context, tempString,
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
                gv.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
//					前往图片浏览器
                        Intent intent = new Intent(context, ImageBrowseActivity.class);
                        intent.putExtra("Pic_urls", pic_ids);
                        intent.putExtra("Position", position);
                        context.startActivity(intent);
                    }
                });
                gv.setAdapter(new GridIvAdapter(pic_ids));
            }
            // 单图处理 此处判断的thumbnail_pic为空字符串，并非空引用！所以不能使用thumbnail_pic！=null来判断
            else if (pic_ids.size() == 1) {
                gv.setVisibility(View.GONE);
                iv.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(status.pic_urls.get(0)) && status.pic_urls.get(0).toLowerCase().endsWith(".gif")) {
                    switch (iv.getId()) {
                        case R.id.iv_statuses_singlecontent:
                            picText.setVisibility(View.VISIBLE);
                            picText.setText("GIF");
                        case R.id.iv_retweeted_singlecontent:
                            tv_retweeted_pic.setVisibility(View.VISIBLE);
                            tv_retweeted_pic.setText("GIF");
                    }
                }
                imageLoader.displayImage(status.pic_urls.get(0), iv, ImageLoadeOptions
                                .getNoDownScalingIvOption(iv.getContext()),
                        new ImageLoadingListener() {

                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view,
                                                        FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String imageUri,
                                                          View view, Bitmap loadedImage) {
                                if (ImageUtils.matchView2Bitmap(status.pic_urls.get(0),view, loadedImage, MAXIMAGE, imageLoader) == 1) {
                                    switch (view.getId()) {
                                        case R.id.iv_statuses_singlecontent:
                                            picText.setVisibility(View.VISIBLE);
                                            picText.setText("长图");
                                        case R.id.iv_retweeted_singlecontent:
                                            tv_retweeted_pic.setVisibility(View.VISIBLE);
                                            tv_retweeted_pic.setText("长图");
                                    }
                                }
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri,
                                                           View view) {

                            }
                        }, new ImageLoadingProgressListener() {

                            @Override
                            public void onProgressUpdate(String imageUri,
                                                         View view, int current, int total) {

                            }
                        });
                iv.setOnClickListener(new OnClickListener() {

                    //				点击跳转至图片浏览器
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ImageBrowseActivity.class);
                        intent.putExtra("Pic_urls", status.pic_urls);
                        intent.putExtra("Position", 0);
                        context.startActivity(intent);
                    }
                });
            }
        }
        // 没图处理
        else {
            iv.setVisibility(View.GONE);
            gv.setVisibility(View.GONE);
        }
    }
}
