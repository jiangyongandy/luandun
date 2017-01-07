package com.jy.xinlangweibo;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jy.xinlangweibo.utils.PreferencesUtil;

/**
 * Created by JIANG on 2016/8/20.
 */
public class AppSetting {

    public static final boolean ifShowLog = true;

    public static final boolean ifImageLoad = true;

    public static int getThemeColor() {
       return PreferencesUtil.getIntShareData("ThemeColorIndex",0);
    }

    public static void setThemeColor(int value) {
        PreferencesUtil.putIntShareData("ThemeColorIndex",value);
    }

    /**
     * 无图模式
     *
     * @return
     */
    public static boolean isNoImage() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
        return prefs.getBoolean("pNoImage", false);
    }

}
