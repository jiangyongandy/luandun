package com.jy.xinlangweibo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	// "created_at": "Wed Jun 17 14:26:24 +0800 2015"

	public static final int ONE_MINUTE_MILLIONS = 60 * 1000;
	public static final int ONE_HOUR_MILLIONS = 60 * ONE_MINUTE_MILLIONS;
	public static final int ONE_DAY_MILLIONS = 24 * ONE_HOUR_MILLIONS;

	public static String getDate(String creatTime) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
		try {
			Date cre = sdf.parse(creatTime);
			Date cur = new Date();
			int dval =  (int) (cur.getTime()-cre.getTime());
			if(cur.getTime()-cre.getTime()<ONE_MINUTE_MILLIONS) {
				str = "刚刚";
			} else if (dval < ONE_HOUR_MILLIONS) {
				str = dval/ONE_MINUTE_MILLIONS+"分钟之前";
			} else if (dval < ONE_DAY_MILLIONS) {
				str = dval/ONE_HOUR_MILLIONS+"小时之前";
			} else if (isSameYear(cre,cur)) {
				sdf.applyLocalizedPattern("MM月d日");
				str = sdf.format(cre);
			}else {
				sdf.applyLocalizedPattern("yyyy-MM-d");
				str = sdf.format(cre);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	private static boolean isSameYear(Date cre, Date cur) {
		Calendar creCal = Calendar.getInstance();
		Calendar curCal = Calendar.getInstance();
		creCal.setTime(cre);
		curCal.setTime(cur);
		if (creCal.get(Calendar.YEAR) == curCal.get(Calendar.YEAR)) {
			return true;
		}
		else 
		return false;
	}

	public static String formatDate(String creatTime) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
		SimpleDateFormat bartDateFormat_1 = new SimpleDateFormat("yyyy-MM-dd" );
		try {
			Date cre = sdf.parse(creatTime);
			str = bartDateFormat_1.format(cre);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}
}
