package com.jy.xinlangweibo.ui.adapter.videoinfosections;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.videoapi.videobean.VideoInfo;
import com.jy.xinlangweibo.models.net.videoapi.videobean.VideoType;
import com.jy.xinlangweibo.ui.adapter.section.StatelessSection;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JIANG on 2016/12/22.
 */

public class RelationsVideoSection extends StatelessSection {

    private final VideoType listBean;
    private final Context context;

    public RelationsVideoSection(Context context,VideoType listBean) {
        super(R.layout.include_nav_divider, R.layout.section_item_video_relations);
        this.listBean = listBean;
        this.context = context;
    }

    @Override
    public int getContentItemsTotal() {
        return listBean.childList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        VideoInfo bean = listBean.childList.get(position);

        Glide.with(context)
                .load(bean.pic)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.timeline_image_loading)
                .dontAnimate()
                .into(viewHolder.ivVideoPic);

        viewHolder.tvVideoTitle.setText(bean.title);
        viewHolder.tvDescription.setText(bean.description);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeadViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeadViewHolder viewHolder = (HeadViewHolder) holder;
        viewHolder.tvTitle.setText(listBean.title);
    }

    static class HeadViewHolder extends RecyclerView.ViewHolder {
        //        R.layout.include_nav_divider
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public HeadViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
//        R.layout.section_item_video_relations
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
