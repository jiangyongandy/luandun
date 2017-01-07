package com.jy.xinlangweibo.ui.adapter.videorecommendsections;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ChildListBean;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ListBean;
import com.jy.xinlangweibo.ui.activity.VideoInfoActivity;
import com.jy.xinlangweibo.ui.activity.base.FragmentToolbarActivity;
import com.jy.xinlangweibo.ui.adapter.section.StatelessSection;
import com.jy.xinlangweibo.ui.fragment.MoreVideoFragment;
import com.jy.xinlangweibo.utils.CommonImageLoader.CustomImageLoader;

import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JIANG on 2016/12/6.
 */

public class VideoCategorySection extends StatelessSection {

    private Context mContext;

    private ListBean videoListBean;


    public VideoCategorySection(Context context, ListBean videoListBean)
    {
//todo 将圆角实现用shapeDrawable 替换cardView
        super(R.layout.layout_video_category_head, R.layout.item_section_video);
        this.mContext = context;
        this.videoListBean = videoListBean;
    }

    @Override
    public int getContentItemsTotal()
    {
        return videoListBean.childList.size();
    }

    public void addData(Collection<ChildListBean> collection) {
        videoListBean.childList.addAll(collection);
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view)
    {

        return new ItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
    {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        final ChildListBean bean;
        bean = videoListBean.childList.get(position);

        CustomImageLoader.displayImage(mContext,
                itemViewHolder.mImage,
                bean.pic,
                R.drawable.timeline_image_loading,
                R.drawable.timeline_image_failure,
                0, 0);

        itemViewHolder.mTitle.setText(bean.title);
        itemViewHolder.mPlay.setText(String.valueOf(bean.airTime));
        itemViewHolder.mUpdate.setText(bean.description);
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoInfoActivity.launch((Activity) mContext,bean);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view)
    {

        return new VideoCategorySection.HeaderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder)
    {

        VideoCategorySection.HeaderViewHolder headerViewHolder = (VideoCategorySection.HeaderViewHolder) holder;
        headerViewHolder.videoCateroy.setText(videoListBean.title);
        if(videoListBean.moreURL != null && !videoListBean.moreURL.isEmpty()) {
            headerViewHolder.mAllSerial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FragmentToolbarActivity.class);
                    intent.putExtra("list_bean", videoListBean);
                    FragmentToolbarActivity.launch((Activity) mContext, MoreVideoFragment.class, intent,false);
                }
            });
        }else {
            headerViewHolder.mAllSerial.setVisibility(View.GONE);
        }

    }


         static class HeaderViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.tv_all_serial)
        TextView mAllSerial;
        @BindView(R.id.tv_video_category)
        TextView videoCateroy;


        HeaderViewHolder(View itemView)
        {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

      static class ItemViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.card_view)
        LinearLayout mCardView;

        @BindView(R.id.item_img)
        ImageView mImage;

        @BindView(R.id.item_title)
        TextView mTitle;

        @BindView(R.id.item_play)
        TextView mPlay;

        @BindView(R.id.item_update)
        TextView mUpdate;


        public ItemViewHolder(View itemView)
        {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
