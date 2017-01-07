package com.jy.xinlangweibo.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.jiang.library.ui.BaseViewHolder;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.UserBean;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.ui.fragment.FragmentController;
import com.jy.xinlangweibo.ui.fragment.home.Home2Fragment;
import com.jy.xinlangweibo.ui.fragment.home.NewsFragment;
import com.jy.xinlangweibo.ui.fragment.home.PersonalFragment;
import com.jy.xinlangweibo.ui.fragment.home.VideoRecommendFragment;
import com.jy.xinlangweibo.utils.CommonImageLoader.CustomImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements OnCheckedChangeListener{

    private static final int REQUSET_UPDATE = 1;
    @BindView(R.id.tabcenterid)
    ImageView tabcenterid;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.tabhomeid)
    RadioButton tabhomeid;
    @BindView(R.id.tabmessageid)
    RadioButton tabmessageid;
    @BindView(R.id.tabdiscoverid)
    RadioButton tabdiscoverid;
    @BindView(R.id.tabprofileid)
    RadioButton tabprofileid;
    @BindView(R.id.nav_title)
    TextView navTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_right_iv)
    ImageView navRightIv;
    private Fragment[] fragments = new Fragment[4];
    protected FragmentController fragmentController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使得侧边栏顶端不被statusbar覆盖  这里的写法是让状态栏透明  只对主页面这样处理
        // 其他activity这样处理会使状态栏设置颜色无效，但是主页面设置颜色则有效。具体原因待分析
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//        }
        getSwipeBackLayout().setEnableGesture(false);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 按back键不销毁activity 参数 nonroot 为false时仅当activity在栈底生效
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FragmentController.onDestroy();

    }

    //    因为FragmentController为单例模式 finish以后ondestroy（验证是新activity oncreate后执行）
//    并不会立即执行，所以在oncreate 之前需要将FragmentController instance置为空,保证FragmentController
//    与activity的生命周期一致
    @Override
    public void finish() {
        super.finish();
        FragmentController.onDestroy();
    }

    //    这里捕获分发事件产生的一个异常（为messagefragment view的侧滑 产生）
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof Home2Fragment) {
            fragments[0] = fragment;
        } else if (fragment instanceof VideoRecommendFragment) {
            fragments[1] = fragment;
        } else if (fragment instanceof NewsFragment) {
            fragments[2] = fragment;
        } else if (fragment instanceof PersonalFragment) {
            fragments[3] = fragment;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (drawerToggle != null && drawerToggle.onOptionsItemSelected(item))
//            return true;
//        switch (item.getItemId()) {
////            toolbar最右边图片按钮点击事件
//            case android.R.id.home:
//                if (drawer.isDrawerVisible(GravityCompat.START))
//                    drawer.closeDrawers();
//                else
//                    drawer.openDrawer(GravityCompat.START);
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        if (drawerToggle != null)
//            drawerToggle.syncState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case WriteStatusActivity.RESULT_UPDATE:
                showToast("微博已发表");
                break;
            case WriteStatusActivity.RESULT_REPOST:
                showToast("已转发");
                break;
            case WriteStatusActivity.RESULT_COMMENT:
                showToast("已评论");
                break;
        }
    }

    private void initView() {
//     Fragment 初始化。
        fragmentController = FragmentController.getInstance(this,
                R.id.contentframe, fragments);
        System.out.println("fragment 初始化。。。。完成");
        fragmentController.show(0);
//		底部按钮初始化
        rg.setOnCheckedChangeListener(this);
        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(null);
    }

//    private void setupDrawer() {
//        drawerToggle = new ActionBarDrawerToggle(this, drawer,
//                toolbar, R.string.draw_open, R.string.draw_close) {
//
//            public void onDrawerClosed(View view) {
//                super.onDrawerClosed(view);
//
//            }
//
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//            }
//
//        };
//        drawer.addDrawerListener(drawerToggle);
//    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        fragmentController.hide();
        switch (checkedId) {
            case R.id.tabhomeid:
                fragmentController.show(0);
                break;
            case R.id.tabmessageid:
                fragmentController.show(1);
                break;
            case R.id.tabdiscoverid:
                fragmentController.show(2);
                break;
            case R.id.tabprofileid:
                fragmentController.show(3);
                break;

            default:
                break;
        }
    }

    @OnClick(R.id.tabcenterid)
    public void onClick() {
        WriteStatusActivity.intentToUpdate(this, REQUSET_UPDATE);
    }

//    @Override
//    public boolean onNavigationItemSelected(MenuItem menuItem) {
//        int itemId = menuItem.getItemId();
//        switch (itemId) {
//            case R.id.navigation_sub_item3:
//                FragmentToolbarActivity.launch(this, SettingFragment.class);
//        }
//        return true;
//    }

    public TextView getNavTitle() {
        return navTitle;
    }

    public ImageView getNavRightIv() {
        return navRightIv;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    class NavHeadViewHolder extends BaseViewHolder<UserBean> {

        @BindView(R.id.profile_image)
        ImageView profileImage;
        @BindView(R.id.tv_screen_name)
        TextView tvScreenName;
        @BindView(R.id.tv_description)
        TextView tvLocation;
        @BindView(R.id.tv_followers_count)
        TextView tvFollowersCount;
        @BindView(R.id.tv_friends_count)
        TextView tvFriendsCount;

        public NavHeadViewHolder(Context context,View convertView) {
            super(context,convertView);
        }

        @Override
        public int getLayout() {
            return R.layout.nv_header;
        }

        @Override
        public void onBindView(View convertView) {
            ButterKnife.bind(this,convertView);
        }

        @Override
        public void bindData(final UserBean model) {
            CustomImageLoader.displayImage((Activity) context,profileImage,model.avatar_large,
                    R.drawable.avatar_default,
                    R.drawable.avatar_default,0,0);
            tvScreenName.setText(model.screen_name);
            tvFollowersCount.setText("粉丝："+String.valueOf(model.followers_count));
            tvFriendsCount.setText("关注："+String.valueOf(model.friends_count));
            tvLocation.setText(model.location);
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserShowActivity.launch(context, model.screen_name);
                }
            });
        }
    }
}
