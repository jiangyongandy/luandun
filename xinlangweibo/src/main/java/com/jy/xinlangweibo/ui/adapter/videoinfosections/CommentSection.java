package com.jy.xinlangweibo.ui.adapter.videoinfosections;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.videoapi.videobean.VideoRes;
import com.jy.xinlangweibo.models.net.videoapi.videobean.VideoType;
import com.jy.xinlangweibo.ui.adapter.section.StatelessSection;

import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JIANG on 2016/12/22.
 */

public class CommentSection extends StatelessSection {


    private final Context context;
    private final VideoRes listBean;

    public CommentSection(Context context, VideoRes listBean) {
        super(R.layout.include_nav_divider, R.layout.item_comments);
        this.listBean = listBean;
        this.context = context;
    }

    @Override
    public int getContentItemsTotal() {
        return listBean.list.size();
    }

    public void addData(Collection<VideoType> collection) {
        listBean.list.addAll(collection);
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        VideoType bean = listBean.list.get(position);

        Glide.with(context)
                .load(bean.userPic)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.avatar_default)
                .dontAnimate()
                .into(viewHolder.ivHead);
        viewHolder.tvFrom.setText("");
        viewHolder.tvPubname.setText(bean.phoneNumber);
        viewHolder.tvStatusesContent.setText(bean.msg);

    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeadViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeadViewHolder viewHolder = (HeadViewHolder) holder;
        viewHolder.tvTitle.setText("评论");
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
//        R.layout.item_comments
        @BindView(R.id.iv_head)
        ImageView ivHead;
        @BindView(R.id.tv_pubname)
        TextView tvPubname;
        @BindView(R.id.tv_from)
        TextView tvFrom;
        @BindView(R.id.tv_statuses_content)
        TextView tvStatusesContent;

        public ItemViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
