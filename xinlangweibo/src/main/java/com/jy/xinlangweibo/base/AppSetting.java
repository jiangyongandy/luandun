package com.jy.xinlangweibo.base;


import com.jy.xinlangweibo.utils.PreferencesUtil;

/**
 * Created by JIANG on 2016/8/20.
 */
public class AppSetting {

    public static int getThemeColor() {
       return PreferencesUtil.getIntShareData("ThemeColorIndex",0);
    }

    public static void setThemeColor(int value) {
        PreferencesUtil.putIntShareData("ThemeColorIndex",value);
    }

}
