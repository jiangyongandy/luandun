package com.jy.xinlangweibo.ui.activity.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.jiang.library.R;

import java.lang.reflect.Method;

public class FragmentToolbarActivity extends BaseActivity  {

    public static final String FRAGMENT_TAG = "FRAGMENT_CONTAINER";

    private int contentId;
    private int overrideTheme = -1;

    /**
     * 启动一个带fragment的activity
     *
     * @param activity
     * @param clazz
     */
    public static void launch(Activity activity, Class<? extends Fragment> clazz) {
        Intent intent = new Intent(activity, FragmentToolbarActivity.class);
        intent.putExtra("className", clazz.getName());
        activity.startActivity(intent);
    }

    public static void launch(Context activity, Class<? extends Fragment> clazz, Intent intent) {
        intent.putExtra("className", clazz.getName());
        activity.startActivity(intent);
    }

    public static void launchForResult(Fragment fragment, Class<? extends Fragment> clazz,int requestCode) {
        if (fragment.getActivity() == null)
            return;
        Activity activity = fragment.getActivity();

        Intent intent = new Intent(activity, FragmentToolbarActivity.class);
        intent.putExtra("className", clazz.getName());
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void launchForResult(Activity from, Class<? extends Fragment> clazz,int requestCode) {
        Intent intent = new Intent(from, FragmentToolbarActivity.class);
        intent.putExtra("className", clazz.getName());
        from.startActivityForResult(intent, requestCode);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contentId = savedInstanceState == null ? R.layout.comm_ui_fragment_container
                                               : savedInstanceState.getInt("contentId");
        overrideTheme = savedInstanceState == null ? -1
                                                   : savedInstanceState.getInt("overrideTheme");

        Fragment fragment = null;
        if (savedInstanceState == null) {
            try {
                String className = getIntent().getStringExtra("className");
                if (TextUtils.isEmpty(className)) {
                    super.onCreate(savedInstanceState);
                    finish();
                    return;
                }
                Class clazz = Class.forName(className);
                fragment = (Fragment) clazz.newInstance();
                // 重写Activity的主题
                try {
                    Method method = clazz.getMethod("setActivityTheme");
                    if (method != null) {
                        int theme = Integer.parseInt(method.invoke(fragment).toString());
                        if (theme > 0) {
                            overrideTheme = theme;
                        }
                    }
                } catch (Exception e) {
                }
                // 重写Activity的contentView
                try {
                    Method method = clazz.getMethod("inflateActivityContentView");
                    if (method != null) {
                        int fragmentConfigId = Integer.parseInt(method.invoke(fragment).toString());
                        if (fragmentConfigId > 0) {
                            contentId = fragmentConfigId;
                        }
                    }
                } catch (Exception e) {
                }
            } catch (Exception e) {
                e.printStackTrace();
                super.onCreate(savedInstanceState);
                finish();
                return;
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(contentId);

        if (fragment != null) {
//            if (!(fragment instanceof ABaseFragment) || ((ABaseFragment) fragment).inflateContentView() > 0) {
                getFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment, FRAGMENT_TAG).commit();
//            }
//            else {
//                getFragmentManager().beginTransaction().add(fragment, FRAGMENT_TAG).commit();
//            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("contentId", contentId);
        outState.putInt("overrideTheme", overrideTheme);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            toolbar最右边图片按钮点击事件
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
