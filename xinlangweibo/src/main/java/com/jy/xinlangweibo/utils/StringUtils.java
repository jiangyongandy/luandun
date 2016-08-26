package com.jy.xinlangweibo.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.xinlangweibo.ui.activity.base.FragmentToolbarActivity;
import com.jy.xinlangweibo.ui.fragment.WebFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static SpannableString getKeyText(final Context context,
			String source, TextView tv) {
		SpannableString spannableString = new SpannableString(source);
		String name = "@[\u4e00-\u9fa5\\w-]+";
		String topic = "#[\u4e00-\u9fa5\\w]+#";
		String emoji = "\\[[\u4e00-\u9fa5\\w]+\\]";
		String http= "http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]";
		String pattern = "(" + name + ")|(" + topic + ")|(" + emoji + ")|("+http+")";
		Pattern pa = Pattern.compile(pattern);
		Matcher matcher = pa.matcher(source);
		if (matcher.find()) {
			tv.setMovementMethod(LinkMovementMethod.getInstance());
			matcher.reset();
		}
		while (matcher.find()) {
			final String na = matcher.group(1);
			final String to = matcher.group(2);
			final String emo = matcher.group(3);
			final String htt = matcher.group(4);
			if (!TextUtils.isEmpty(na)) {
				int start = matcher.start(1);
				int end = matcher.end(1);
				spannableString.setSpan(new MyClickableSpan(context) {
					@Override
					public void onClick(View arg0) {
						ToastUtils.show(context, "用户："+na, Toast.LENGTH_SHORT);
					}
				}, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if (!TextUtils.isEmpty(to)) {
				int start = matcher.start(2);
				int end = matcher.end(2);
				spannableString.setSpan(new MyClickableSpan(context) {
					@Override
					public void onClick(View arg0) {
						ToastUtils.show(context, "话题："+to, Toast.LENGTH_SHORT);
					}
				}, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if (!TextUtils.isEmpty(emo)) {
				int start = matcher.start(3);
				int end = matcher.end(3);
				int imgByName = EmotionUtils.getImgByName(emo);
				Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), imgByName);
				if(decodeResource!=null) {
					int size = (int) tv.getTextSize();
					decodeResource = Bitmap.createScaledBitmap(decodeResource, size, size, true);
				}
				spannableString.setSpan(new ImageSpan(context, decodeResource), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if (!TextUtils.isEmpty(htt)) {
				int start = matcher.start(4);
				int end = matcher.end(4);
				spannableString.setSpan(new MyClickableSpan(context) {
					@Override
					public void onClick(View arg0) {
//						点击进入WEBVIEW显示网页
						ToastUtils.show(context, "链接："+htt, Toast.LENGTH_SHORT);
						Intent intent = new Intent(arg0.getContext(),FragmentToolbarActivity.class);
						intent.putExtra("Website",htt );
						FragmentToolbarActivity.launch(arg0.getContext(), WebFragment.class,intent);
					}
				}, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}

		}
		return spannableString;
	}
	
	public static SpannableString get2KeyText(final Context context,
			String source, TextView tv) {
		SpannableString spannableString = new SpannableString(source);
		String pattern = "\\s\\s[\u4e00-\u9fa5\\w\\s\\S]+";
		Pattern pa = Pattern.compile(pattern);
		Matcher matcher = pa.matcher(source);
		if (matcher.find()) {
			tv.setMovementMethod(LinkMovementMethod.getInstance());
			matcher.reset();
		}
		while (matcher.find()){
			final String phone = matcher.group(0);
			if (!TextUtils.isEmpty(phone)) {
				int start = matcher.start(0);
				int end = matcher.end(0);
				spannableString.setSpan(new MyClickableSpan(context) {
					@Override
					public void onClick(View arg0) {
						ToastUtils.show(context, "好牛逼的"+phone, Toast.LENGTH_SHORT);
					}
				}, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannableString;
	}

	public static SpannableString getOnlyImageSpan(final Context context,
			String source, TextView tv) {
		SpannableString spannableString = new SpannableString(source);
		String emoji = "\\[[\u4e00-\u9fa5\\w]+\\]";
		Pattern pa = Pattern.compile(emoji);
		Matcher matcher = pa.matcher(source);
		if (matcher.find()) {
			tv.setMovementMethod(LinkMovementMethod.getInstance());
			matcher.reset();
		}
		while (matcher.find()){
			final String emoji1 = matcher.group(0);
			if (!TextUtils.isEmpty(emoji1)) {
				int start = matcher.start(0);
				int end = matcher.end(0);
				int imgByName = EmotionUtils.getImgByName(emoji1);
				Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), imgByName);
				if(decodeResource!=null) {
					int size = (int) tv.getTextSize();
					decodeResource = Bitmap.createScaledBitmap(decodeResource, size, size, true);
				}
				spannableString.setSpan(new ImageSpan(context, decodeResource), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannableString;
	}
	
	static class MyClickableSpan extends ClickableSpan {
		private Context context;

		public MyClickableSpan(Context context) {
			this.context = context;
		}

		@Override
		public void onClick(View arg0) {

		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(ThemeUtils.getThemeColor());
			ds.setUnderlineText(false);
		}
	}

}
