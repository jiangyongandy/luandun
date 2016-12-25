package com.jy.xinlangweibo.ui.adapter.videoinfosections;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.ui.adapter.section.StatelessSection;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JIANG on 2016/12/22.
 */

public class VideoInfoSection extends StatelessSection {
    private VideoInfoBean infoBean;
    private Context mContext;

    public VideoInfoSection(Context context, VideoInfoBean infoBean) {
        super(R.layout.section_video_info);
        this.infoBean = infoBean;
    }

    @Override
    public int getContentItemsTotal() {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        viewHolder.tvDescription.setText("\u3000\u3000"+infoBean.videoInfo.description);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
//        R.layout.section_video_info

        @BindView(R.id.tv_description)
        TextView tvDescription;


        public ItemViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
