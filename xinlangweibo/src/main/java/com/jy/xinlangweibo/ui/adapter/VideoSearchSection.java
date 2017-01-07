package com.jy.xinlangweibo.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ChildListBean;
import com.jy.xinlangweibo.models.net.videoapi.videobean.VideoListBean;
import com.jy.xinlangweibo.ui.activity.VideoInfoActivity;
import com.jy.xinlangweibo.ui.adapter.section.StatelessSection;

import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JIANG on 2016/12/6.
 */

public class VideoSearchSection extends StatelessSection {

    private Context mContext;

    private VideoListBean.RetBean videoListBean;


    public VideoSearchSection(Context context, VideoListBean.RetBean videoListBean) {
//todo 将圆角实现用shapeDrawable 替换cardView
        super(R.layout.section_item_video_relations);
        this.mContext = context;
        this.videoListBean = videoListBean;
    }

    @Override
    public int getContentItemsTotal() {
        return videoListBean.list.size();
    }

    public void addData(Collection<ChildListBean> collection) {
        videoListBean.list.addAll(collection);
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {

        return new ItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        final ChildListBean bean;
        bean = videoListBean.list.get(position);

        Glide.with(mContext)
                .load(bean.pic)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.timeline_image_loading)
                .dontAnimate()
                .into(itemViewHolder.ivVideoPic);

        itemViewHolder.tvVideoTitle.setText(bean.title);
        itemViewHolder.tvDescription.setText(bean.description);
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoInfoActivity.launch((Activity) mContext, bean);
            }
        });
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_video_pic)
        ImageView ivVideoPic;
        @BindView(R.id.tv_video_title)
        TextView tvVideoTitle;
        @BindView(R.id.tv_description)
        TextView tvDescription;


        public ItemViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
