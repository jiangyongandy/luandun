package com.jy.xinlangweibo.ui.adapter.newssections;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.newsapi.bean.ContentlistBean;
import com.jy.xinlangweibo.ui.activity.BrowserActivity;
import com.jy.xinlangweibo.ui.adapter.section.StatelessSection;
import com.jy.xinlangweibo.utils.CommonImageLoader.CustomImageLoader;
import com.zhy.android.percent.support.PercentLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JIANG on 2017/1/4.
 */

public class MultiNewsSection extends StatelessSection {

    private Context mContext;

    private ContentlistBean bean;


    public MultiNewsSection(Context context, ContentlistBean bean) {
        super(R.layout.item_section_news_multi);
        this.mContext = context;
        this.bean = bean;
    }

    @Override
    public int getContentItemsTotal() {
        return 1;
    }


    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {

        return new ItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        for(int i = 0;i < itemViewHolder.perllPics.getChildCount();i++){
            ImageView imageView = (ImageView) itemViewHolder.perllPics.getChildAt(i);

            CustomImageLoader.displayImage(mContext,
                    imageView,
                    bean.imageurls.get(0).url,
                    R.drawable.timeline_image_loading,
                    R.drawable.timeline_image_failure,
                    0, 0);

        }
        itemViewHolder.tvNewsTitle.setText(bean.title);
        itemViewHolder.tvNewsFrom.setText(bean.source);
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowserActivity.launch((Activity) mContext , bean.link ,"新闻资讯");
            }
        });
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_news_title)
        TextView tvNewsTitle;
        @BindView(R.id.tv_news_from)
        TextView tvNewsFrom;
        @BindView(R.id.perll_pics)
        PercentLinearLayout perllPics;
//        R.layout.item_section_news_multi


        public ItemViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
