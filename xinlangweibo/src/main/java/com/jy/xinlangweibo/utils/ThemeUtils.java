package com.jy.xinlangweibo.utils;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.base.AppSetting;
import com.jy.xinlangweibo.base.BaseApplication;


/**
 * Created by wangdan on 15/4/30.
 */
public class ThemeUtils {

    public static int[][] themeArr = {
            { R.style.appTheme_Red, R.style.appTheme_Main_Red, R.style.appTheme_Profile_Red, R.style.appTheme_Search_Red },
            { R.style.appTheme_Pink, R.style.appTheme_Main_Pink, R.style.appTheme_Profile_Pink, R.style.appTheme_Search_Pink },
            { R.style.appTheme_Purple, R.style.appTheme_Main_Purple, R.style.appTheme_Profile_Purple, R.style.appTheme_Search_Purple },
            { R.style.appTheme_DeepPurple, R.style.appTheme_Main_DeepPurple, R.style.appTheme_Profile_DeepPurple, R.style.appTheme_Search_DeepPurple },
            { R.style.appTheme_Indigo, R.style.appTheme_Main_Indigo, R.style.appTheme_Profile_Indigo, R.style.appTheme_Search_Indigo },
            { R.style.appTheme_Blue, R.style.appTheme_Main_Blue, R.style.appTheme_Profile_Blue, R.style.appTheme_Search_Blue },
            { R.style.appTheme_LightBlue, R.style.appTheme_Main_LightBlue, R.style.appTheme_Profile_LightBlue, R.style.appTheme_Search_LightBlue },
            { R.style.appTheme_Cyan, R.style.appTheme_Main_Cyan, R.style.appTheme_Profile_Cyan, R.style.appTheme_Search_Cyan },
            { R.style.appTheme_Teal, R.style.appTheme_Main_Teal, R.style.appTheme_Profile_Teal, R.style.appTheme_Search_Teal },
            { R.style.appTheme_Green, R.style.appTheme_Main_Green, R.style.appTheme_Profile_Green, R.style.appTheme_Search_Green },
            { R.style.appTheme_LightGreen, R.style.appTheme_Main_LightGreen, R.style.appTheme_Profile_LightGreen, R.style.appTheme_Search_LightGreen },
            { R.style.appTheme_Lime, R.style.appTheme_Main_Lime, R.style.appTheme_Profile_Lime, R.style.appTheme_Search_Lime },
            { R.style.appTheme_Yellow, R.style.appTheme_Main_Yellow, R.style.appTheme_Profile_Yellow, R.style.appTheme_Search_Yellow },
            { R.style.appTheme_Amber, R.style.appTheme_Main_Amber, R.style.appTheme_Profile_Amber, R.style.appTheme_Search_Amber },
            { R.style.appTheme_Orange, R.style.appTheme_Main_Orange, R.style.appTheme_Profile_Orange, R.style.appTheme_Search_Orange },
            { R.style.appTheme_DeepOrange, R.style.appTheme_Main_DeepOrange, R.style.appTheme_Profile_DeepOrange, R.style.appTheme_Search_DeepOrange },
            { R.style.appTheme_Brown, R.style.appTheme_Main_Brown, R.style.appTheme_Profile_Brown, R.style.appTheme_Search_Brown },
            { R.style.appTheme_Grey, R.style.appTheme_Main_Grey, R.style.appTheme_Profile_Grey, R.style.appTheme_Search_Grey },
            { R.style.appTheme_BlueGrey, R.style.appTheme_Main_BlueGrey, R.style.appTheme_Profile_BlueGrey, R.style.appTheme_Search_BlueGrey }
    };

    public static int[][] themeColorArr = {
            { R.color.md_red_500, R.color.md_red_700 },
            { R.color.md_pink_500, R.color.md_pink_700 },
            { R.color.md_purple_500, R.color.md_purple_700 },
            { R.color.md_deep_purple_500, R.color.md_deep_purple_700 },
            { R.color.md_indigo_500, R.color.md_indigo_700 },
            { R.color.md_blue_500, R.color.md_blue_700 },
            { R.color.md_light_blue_500, R.color.md_light_blue_700 },
            { R.color.md_cyan_500, R.color.md_cyan_700 },
            { R.color.md_teal_500, R.color.md_teal_700 },
            { R.color.md_green_500, R.color.md_green_700 },
            { R.color.md_light_green_500, R.color.md_light_green_700 },
            { R.color.md_lime_500, R.color.md_lime_700 },
            { R.color.md_yellow_500, R.color.md_yellow_700 },
            { R.color.md_amber_500, R.color.md_amber_700 },
            { R.color.md_orange_500, R.color.md_orange_700 },
            { R.color.md_deep_orange_500, R.color.md_deep_orange_700 },
            { R.color.md_brown_500, R.color.md_brown_700 },
            { R.color.md_grey_500, R.color.md_grey_700 },
            { R.color.md_blue_grey_500, R.color.md_blue_grey_700 }
    };

        public static int getThemeColor() {
                return BaseApplication.getInstance().getResources().getColor(themeColorArr[AppSetting.getThemeColor()][0]);
        }
}
