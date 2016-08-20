package com.jy.xinlangweibo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

public class InternetConnectUtils {

	/**
	 * 是否连接了网络
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean isConnectingToInternet(Activity mContext) {
		ConnectivityManager connectivity = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	/**
	 * 当前activity是什么网络类型
	 * 
	 * @param mContext
	 * @return
	 */
	public static int whatInternet(Activity mContext) {
		ConnectivityManager connectivity = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null) {
				// 一、判断网络是否是wifi，在判断之前一定要进行的非空判断，如果没有任何网络
				// 连接info ==null
				if (info.getType() == ConnectivityManager.TYPE_WIFI) {
					return ConnectivityManager.TYPE_WIFI;
				}

				// 二、判断是否是手机网络
				if (info != null
						&& info.getType() == ConnectivityManager.TYPE_MOBILE) {
					return ConnectivityManager.TYPE_MOBILE;
				}
			}

		}
		return ConnectivityManager.DEFAULT_NETWORK_PREFERENCE;
	}

	/**
	 * 连接网络
	 * 
	 * @param mContext
	 */
	public static void connectInternet(final Activity mContext) {
		boolean networkState = isConnectingToInternet(mContext);
		if (!networkState) {
			DialogUtils.showConfirmDialog(mContext, "网络连接", "网络不可用，是否现在设置网络？",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mContext.startActivityForResult(new Intent(
									Settings.ACTION_WIRELESS_SETTINGS), 0);
						}
					}).show();
		}
	}

	/**
	 * 获取外网的IP(要访问Url，要放到后台线程里处理)
	 * 
	 * @Title: GetNetIp
	 * @Description:
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String GetNetIp() {
		String IP = "";
		try {
			String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
			URL url = new URL(address);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setUseCaches(false);

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream in = connection.getInputStream();

				// 将流转化为字符串
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));

				String tmpString = "";
				StringBuilder retJSON = new StringBuilder();
				while ((tmpString = reader.readLine()) != null) {
					retJSON.append(tmpString + "\n");
				}

				JSONObject jsonObject = new JSONObject(retJSON.toString());
				String code = jsonObject.getString("code");
				if (code.equals("0")) {
					JSONObject data = jsonObject.getJSONObject("data");
					IP = data.getString("ip") 
//							+ "(" + data.getString("country")
//							+ data.getString("area") + "区"
//							+ data.getString("region") + data.getString("city")
//							+ data.getString("isp") + ")"
							;

					Log.e("提示", "您的IP地址是：" + IP);
				} else {
					IP = "";
					Log.e("提示", "IP接口异常，无法获取IP地址！");
				}
			} else {
				IP = "";
				Log.e("提示", "网络连接异常，无法获取IP地址！");
			}
		} catch (Exception e) {
			IP = "";
			Log.e("提示", "获取IP地址时出现异常，异常信息是：" + e.toString());
		}
		return IP;
	}

	/**
	 * 内网IP
	 * 
	 * @param mContext
	 * @return
	 */
	public static String getIp(Activity mContext) {
		int type = whatInternet(mContext);
		switch (type) {
		case ConnectivityManager.TYPE_WIFI:
			WifiManager wifiManager = (WifiManager) mContext
					.getSystemService(Context.WIFI_SERVICE);
			// 判断wifi是否开启
			if (!wifiManager.isWifiEnabled()) {
				wifiManager.setWifiEnabled(true);
			}
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			String ip = intToIp(ipAddress);
			System.out.println("wifi wangluo" + ip);
			return ip;
		case ConnectivityManager.TYPE_MOBILE:
			try {
				for (Enumeration<NetworkInterface> en = NetworkInterface
						.getNetworkInterfaces(); en.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf
							.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							System.out.println("数据 wangluo"
									+ inetAddress.getHostAddress().toString());
							return inetAddress.getHostAddress().toString();
						}
					}
				}
			} catch (SocketException ex) {
				Log.e("WifiPreference IpAddress", ex.toString());
			}
			break;
		default:
			break;
		}
		return null;
	}

	private static String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

}
