package com.jy.xinlangweibo.ui.activity;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.ui.activity.base.FragmentActivity;
import com.jy.xinlangweibo.ui.fragment.DiscoverFragment;
import com.jy.xinlangweibo.ui.fragment.FragmentController;
import com.jy.xinlangweibo.ui.fragment.HomeFragment;
import com.jy.xinlangweibo.ui.fragment.MessageFragment;
import com.jy.xinlangweibo.ui.fragment.ProfileFragment;
import com.jy.xinlangweibo.ui.fragment.setting.SettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements OnCheckedChangeListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.tabcenterid)
    ImageView tabcenterid;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.navgationview)
    NavigationView navgationview;
    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.mainmenu)
    FrameLayout mainmenu;
    @BindView(R.id.tabhomeid)
    RadioButton tabhomeid;
    @BindView(R.id.tabmessageid)
    RadioButton tabmessageid;
    @BindView(R.id.tabdiscoverid)
    RadioButton tabdiscoverid;
    @BindView(R.id.tabprofileid)
    RadioButton tabprofileid;
    protected FragmentController fragmentController;
    private Fragment[] fragments = new Fragment[4];
    private PopupWindow pw;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使得侧边栏顶端不被statusbar覆盖  这里的写法是让状态栏透明  只对主页面这样处理
        // 其他activity这样处理会使状态栏设置颜色无效，但是主页面设置颜色则有效。具体原因待分析
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//  这里是为了设置popupwindow 背景半透明
        mainmenu = (FrameLayout) findViewById(R.id.mainmenu);
        mainmenu.getForeground().setAlpha(0);

        initView();

        HomeFragment fragment = (HomeFragment) fragments[0];
        drawerToggle = fragment.getDrawerToggle();
    }

    /**
     * 退出主界面不退出应用
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
        if (fragment instanceof HomeFragment) {
            fragments[0] = fragment;
        } else if (fragment instanceof MessageFragment) {
            fragments[1] = fragment;
        } else if (fragment instanceof DiscoverFragment) {
            fragments[2] = fragment;
        } else if (fragment instanceof ProfileFragment) {
            fragments[3] = fragment;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle != null && drawerToggle.onOptionsItemSelected(item))
            return true;
        switch (item.getItemId()) {
//            toolbar最右边图片按钮点击事件
            case android.R.id.home:
                if (drawer.isDrawerVisible(GravityCompat.START))
                    drawer.closeDrawers();
                else
                    drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("ResourceType")
    private void initView() {
//     Fragment 初始化。
        fragmentController = FragmentController.getInstance(this,
                R.id.contentframe, fragments);
        System.out.println("fragment 初始化。。。。完成");
        fragmentController.show(0);
//		底部按钮初始化
        rg.setOnCheckedChangeListener(this);
        navgationview.setNavigationItemSelectedListener(this);
    }

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
        MainActivity.this.intent2Activity(WriteStatusActivity.class);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        switch (itemId) {
            case R.id.navigation_sub_item3:
                FragmentActivity.launch(this, SettingFragment.class);
        }
        return true;
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    public FrameLayout getMainmenu() {
        return mainmenu;
    }
}
