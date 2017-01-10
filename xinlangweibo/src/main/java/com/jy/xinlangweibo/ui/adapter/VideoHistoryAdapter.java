package com.jy.xinlangweibo.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ChildListBean;
import com.jy.xinlangweibo.ui.activity.VideoInfoActivity;
import com.jy.xinlangweibo.ui.adapter.base.BaseRecycleViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by JIANG on 2017/1/7.
 */

public class VideoHistoryAdapter extends BaseRecycleViewAdapter<ChildListBean> {

    private ImageLoader imageLoader;

    public VideoHistoryAdapter(List<ChildListBean> list) {
        super(list);
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    protected int createView() {
        return R.layout.item_grid_video_history;
    }

    @Override
    protected RecyclerView.ViewHolder createViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder,position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        imageLoader.displayImage( getItem(position).pic, viewHolder.ivVideoPic);
        viewHolder.tvTitle.setText(getItem(position).title);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoInfoActivity.launch(activity,getItem(position));
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_video_pic)
        ImageView ivVideoPic;
        @BindView(R.id.tv_title)
        TextView tvTitle;
//        R.layout.item_grid_video_history

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
