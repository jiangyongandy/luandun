package com.jy.xinlangweibo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jy.xinlangweibo.BaseApplication;

/**
 * Created by JIANG on 2016/8/20.
 */
public class PreferencesUtil {

    private static Context mContext = BaseApplication.getInstance();

    public static int getIntShareData(String key, int defValue) {
        SharedPreferences sp = mContext.getSharedPreferences("com.jy.xinlangweibo", 0);
        return sp.getInt(key, defValue);
    }

    public static void putIntShareData(String key, int value) {
        SharedPreferences sp = mContext.getSharedPreferences("com.jy.xinlangweibo", 0);
        SharedPreferences.Editor et = sp.edit();
        et.putInt(key, value);
        et.commit();
    }
}
