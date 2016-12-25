package com.jy.xinlangweibo.ui.adapter.videorecommendsections;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ChildListBean;
import com.jy.xinlangweibo.widget.banner.BannerView;
import com.jy.xinlangweibo.ui.adapter.section.StatelessSection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JIANG on 2016/12/5.
 */

public class VideoBannerSection extends StatelessSection  {

    private List<ChildListBean> banners;

    public VideoBannerSection(List<ChildListBean> banners)
    {

        super(R.layout.layout_banner, R.layout.layout_home_recommend_empty);
        this.banners = banners;
    }

    @Override
    public int getContentItemsTotal()
    {

        return 1;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view)
    {

        return new EmptyViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
    {

    }


    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view)
    {

        return new BannerViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder)
    {

        BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
        bannerViewHolder.mBannerView.delayTime(5).build(banners);
    }


    public static class EmptyViewHolder extends RecyclerView.ViewHolder
    {

        EmptyViewHolder(View itemView)
        {
            super(itemView);
        }
    }

        static class BannerViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.home_recommended_banner)
        BannerView mBannerView;

        BannerViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
