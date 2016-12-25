package com.jy.xinlangweibo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.StatusBean;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.UserBean;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.ui.activity.base.FragmentToolbarActivity;
import com.jy.xinlangweibo.ui.fragment.CommentSupportFragment;
import com.jy.xinlangweibo.ui.fragment.ImageBrowserFragment;
import com.jy.xinlangweibo.ui.fragment.RepostSupportFragment;
import com.jy.xinlangweibo.utils.CommonImageLoader.ImageLoadeOptions;
import com.jy.xinlangweibo.utils.DateUtils;
import com.jy.xinlangweibo.utils.WeiboStringUtils;
import com.jy.xinlangweibo.widget.ninephoto.BGANinePhotoLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by JIANG on 2016/10/24.
 */
public class StatusDetails2Activity extends BaseActivity implements BGANinePhotoLayout.Delegate {

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_pubname)
    TextView tvPubname;
    @BindView(R.id.tv_from)
    TextView tvFrom;
    @BindView(R.id.tv_statuses_content)
    TextView tvStatusesContent;
    @BindView(R.id.timeline_photos)
    BGANinePhotoLayout timelinePhotos;
    @BindView(R.id.tv_retweeted_content)
    TextView tvRetweetedContent;
    @BindView(R.id.retweeted_timeline_photos)
    BGANinePhotoLayout retweetedTimelinePhotos;
    @BindView(R.id.ll_retweeted)
    LinearLayout llRetweeted;
    @BindView(R.id.ll_item_status)
    LinearLayout llItemStatus;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    private StatusBean status;
    private ImageLoader imageLoader;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    public static void intent2StatusDetails(Context context,StatusBean status) {
        //前往微博详情界面
        Intent intent = new Intent(context, StatusDetails2Activity.class);
        intent.putExtra("Status",status);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo 改写为listView 实现 coordinateLayout 存在少许BUG 滑动体验不流畅
        setContentView(R.layout.activity_status_details2);
        imageLoader = ImageLoader.getInstance();
        status = (StatusBean) getIntent().getSerializableExtra("Status");
        initViewAndEvents();
    }

    private void initViewAndEvents() {
        CommentSupportFragment commentFragment = CommentSupportFragment.newInstance(status.id);
        RepostSupportFragment repostFragment = RepostSupportFragment.newInstance(status.id);
        fragments.add(commentFragment);
        fragments.add(repostFragment);
        titles.add("评论");
        titles.add("转发");
        StatusDetailsPagerAdapter pagerAdapter = new StatusDetailsPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setViewPager(viewPager);
        measureTabLayoutTextWidth(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {

                measureTabLayoutTextWidth(position);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
        bindHeadData(status);
    }

    private void measureTabLayoutTextWidth(int position)
    {

        String title = titles.get(position);
        TextView titleView = tabLayout.getTitleView(position);
        TextPaint paint = titleView.getPaint();
        float textWidth = paint.measureText(title);
        tabLayout.setIndicatorWidth(textWidth / 3);
    }

    private void bindHeadData(final StatusBean status) {
        if (status == null)
            return;
        final UserBean user = status.user;

        //bind publisher
        imageLoader.displayImage(user.avatar_hd, ivHead,
                ImageLoadeOptions.getIvHeadOption());
        String from = DateUtils.getDate(status.created_at) + " 来自  "
                + Html.fromHtml(status.source);
        tvFrom.setText(WeiboStringUtils.get2KeyText(this, from, tvFrom));
        tvPubname.setText(user.screen_name);
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserShowActivity.launch(StatusDetails2Activity.this, user.screen_name);
            }
        });
        //bind content
        tvStatusesContent.setText(WeiboStringUtils.getKeyText(this, status.text,
                tvStatusesContent));
        timelinePhotos.init(this);
        if (status.pic_urls != null) {
            for (int i = 0; i < status.pic_urls.size(); i++) {
                status.getPic_urls2().set(i, status.getPic_urls2().get(i).replace("thumbnail", "bmiddle"));
            }
            timelinePhotos.setData(status.getPic_urls2());
            timelinePhotos.setDelegate(this);
        } else {
            ArrayList<String> strings = new ArrayList<>();
            timelinePhotos.setData(strings);
        }
        //bind bottomTab
/*        tvStatusesBottomReweet
                .setText((CharSequence) (status.reposts_count > 0 ? ""
                        + status.reposts_count : "转发"));
        tvStatusesBottomComment
                .setText((CharSequence) (status.comments_count > 0 ? ""
                        + status.comments_count : "评论"));
        tvStatusesBottomUnlike
                .setText((CharSequence) (status.attitudes_count > 0 ? ""
                        + status.attitudes_count : "赞"));

        llBtnRetweeted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteStatusActivity.intentToRepost((Activity) context, status, REQUEST_REPOST);
            }
        });

        llBtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteStatusActivity.intentToComment((Activity) context, status, REQUEST_COMMENT);
            }
        });

        // 点赞的特殊处理
        llStatusUnlike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                statusUnlikebtn
                        .setImageResource(R.drawable.timeline_icon_like);
                statusUnlikebtn.startAnimation(AnimationUtils.loadAnimation(
                        StatusDetails2Activity.this, R.anim.scale_unlike));
            }
        });*/

        //bind retweeted_status
        if (status.retweeted_status != null) {

            llRetweeted.setVisibility(View.VISIBLE);
            String retweetedname = "作者已删除该微博";
            // 这里要判断 转发的微博作者是否为空 即转发的微博是否被作者删除。
            if (status.retweeted_status.user != null) {
                if (status.retweeted_status.user.screen_name != null) {
                    retweetedname = status.retweeted_status.user.screen_name;
                }
            }
            String tempString = "@" + retweetedname + ":"
                    + status.retweeted_status.text;
            tvRetweetedContent.setText(WeiboStringUtils.getKeyText(this, tempString,
                    tvRetweetedContent));
            retweetedTimelinePhotos.init(this);
            if (status.retweeted_status.pic_urls != null) {
                for (int i = 0; i < status.retweeted_status.pic_urls.size(); i++) {
                    status.retweeted_status.getPic_urls2().set(i, status.retweeted_status.getPic_urls2().get(i).replace("thumbnail", "bmiddle"));
                }
                retweetedTimelinePhotos.setData(status.retweeted_status.getPic_urls2());
                retweetedTimelinePhotos.setDelegate(this);
            } else {
                ArrayList<String> strings = new ArrayList<>();
                retweetedTimelinePhotos.setData(strings);
            }
//                setImage(status.retweeted_status, retweetedIv, retweetedGv, picText, tv_retweeted_pic);
        } else {
            llRetweeted.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        //					前往图片浏览器
        Intent intent = new Intent(view.getContext(), FragmentToolbarActivity.class);
        intent.putExtra("Pic_urls", (ArrayList) models);
        intent.putExtra("Position", position);
        FragmentToolbarActivity.launch(view.getContext(), ImageBrowserFragment.class, intent, false);
    }

    @Override
    public boolean onLongClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        return false;
    }

    public static class StatusDetailsPagerAdapter extends FragmentStatePagerAdapter
    {

        private List<Fragment> fragments;

        private List<String> titles;

        StatusDetailsPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles)
        {

            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position)
        {

            return fragments.get(position);
        }

        @Override
        public int getCount()
        {

            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {

            return titles.get(position);
        }
    }
}
