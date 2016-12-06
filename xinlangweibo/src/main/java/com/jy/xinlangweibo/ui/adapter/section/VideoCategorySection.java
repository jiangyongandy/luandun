package com.jy.xinlangweibo.ui.adapter.section;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ChildListBean;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ListBean;
import com.jy.xinlangweibo.widget.sectioned.StatelessSection;

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

        super(R.layout.layout_video_category_head, R.layout.item_section_video);
        this.mContext = context;
        this.videoListBean = videoListBean;
    }

    @Override
    public int getContentItemsTotal()
    {

        return videoListBean.childList.size();
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
        ChildListBean bean = videoListBean.childList.get(position);

        Glide.with(mContext)
                .load(bean.pic)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.timeline_image_loading)
                .dontAnimate()
                .into(itemViewHolder.mImage);

        itemViewHolder.mTitle.setText(bean.title);
        itemViewHolder.mPlay.setText(String.valueOf(bean.airTime));
        itemViewHolder.mUpdate.setText(bean.description);

//        itemViewHolder.mCardView.setOnClickListener(v -> BangumiDetailsActivity.launch(
//                (Activity) mContext, serializingBean.getSeason_id()));
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
//        headerViewHolder.mAllSerial.setOnClickListener(v -> mContext.startActivity(
//                new Intent(mContext, NewBangumiSerialActivity.class)));
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
