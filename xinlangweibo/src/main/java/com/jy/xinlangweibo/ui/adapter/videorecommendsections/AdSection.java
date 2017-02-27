package com.jy.xinlangweibo.ui.adapter.videorecommendsections;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.videoapi.RetrofitHelper;
import com.jy.xinlangweibo.models.net.videoapi.videobean.GankHttpResponse;
import com.jy.xinlangweibo.models.net.videoapi.videobean.GankItemBean;
import com.jy.xinlangweibo.ui.activity.base.FragmentToolbarActivity;
import com.jy.xinlangweibo.ui.adapter.section.StatelessSection;
import com.jy.xinlangweibo.ui.fragment.ImageBrowserFragment;
import com.jy.xinlangweibo.utils.CommonImageLoader.CustomImageLoader;
import com.jy.xinlangweibo.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by JIANG on 2016/12/17.
 */

public class AdSection extends StatelessSection {

    private Context mContext;

    private int num;



    public AdSection(Context context,int num) {
        super(R.layout.item_ad, R.layout.layout_home_recommend_empty);
        this.mContext = context;
        this.num = num;
    }

    @Override
    public int getContentItemsTotal() {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new VideoBannerSection.EmptyViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view)
    {

        return new BannerViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder)
    {
        final BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
        RetrofitHelper.getGankApis().getGirlList(0,0).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GankHttpResponse<List<GankItemBean>>>() {
                    @Override
                    public void call(final GankHttpResponse<List<GankItemBean>> listGankHttpResponse) {
                        CustomImageLoader.displayImage(mContext,
                                bannerViewHolder.mBannerView,
                                listGankHttpResponse.getResults().get(num).getUrl(),
                                R.drawable.timeline_image_loading,
                                R.drawable.timeline_image_failure,
                                0, 0);

                        final ArrayList<String> picUrls = new ArrayList<>();

                        for(GankItemBean bean:listGankHttpResponse.getResults()) {
                            picUrls.add(bean.getUrl());
                        }

                        bannerViewHolder.mBannerView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //					前往图片浏览器
                                Intent intent = new Intent(mContext, FragmentToolbarActivity.class);
                                intent.putExtra("Pic_urls", (ArrayList) picUrls);
                                intent.putExtra("Position", num);
                                FragmentToolbarActivity.launch(mContext, ImageBrowserFragment.class, intent, false);
                            }
                        });
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.showLog(throwable.getMessage(),"adsection");
                    }
                });
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.iv_ad)
        ImageView mBannerView;

        BannerViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
