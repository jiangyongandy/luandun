package com.jy.xinlangweibo.models.net.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.jy.xinlangweibo.constant.Constants;
import com.jy.xinlangweibo.models.net.StatusesInteraction;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.AbsOpenAPI;

public class StatusesInteractionImpl extends AbsOpenAPI implements StatusesInteraction {

    public  interface URL_Constant {
        //	https://api.weibo.com/2/statuses/home_timeline.json
        String BASE_URL = "https://api.weibo.com/2/";
        String STATUES_HOMETIMELINE_URL = BASE_URL+"statuses/home_timeline.json";
        String SHOWCOMMENTS_URL = BASE_URL+"comments/show.json";
        String UPDATE_URL = BASE_URL+"statuses/update.json";
        String UPLOADE_URL = BASE_URL+"statuses/upload.json";
        String UPLOADE_URL_TEXT_URL = BASE_URL+"statuses/upload_url_text.json";
        String ip_to_geo_URL = BASE_URL+"location/geo/ip_to_geo.json";
        String place_nearby_timeline_URL= BASE_URL+"place/nearby_timeline.json";
        String user_show_url = "https://api.weibo.com/2/users/show.json";
        String comments_create_url = "https://api.weibo.com/2/comments/create.json";
        String statuses_repost_url = "https://api.weibo.com/2/statuses/repost.json";

    }

	public StatusesInteractionImpl(Context context,Oauth2AccessToken accessToken) {
		super(context, Constants.APP_KEY, accessToken);
	}

	@Override
    public void getStatusesHomeTimeline(int page, RequestListener listener) {
		WeiboParameters params = new WeiboParameters(mAppKey);
		params.put("page", page);
		requestAsync(URL_Constant.STATUES_HOMETIMELINE_URL, params,
				HTTPMETHOD_GET, listener);
	}

	@Override
    public void showComments(int page, long id, RequestListener listener) {
		WeiboParameters params = new WeiboParameters(mAppKey);
		params.put("page", page);
		params.put("id", id);
		requestAsync(URL_Constant.SHOWCOMMENTS_URL, params, HTTPMETHOD_GET,
				listener);
	}
    
    /**
     * 发表微博
     * @param content
     * @param lat
     * @param lon
     * @param listener
     */
    @Override
    public void update(String content, String lat, String lon, RequestListener listener) {
        WeiboParameters params = buildUpdateParams(content, lat, lon);
        requestAsync(URL_Constant.UPDATE_URL , params, HTTPMETHOD_POST, listener);
    }

    @Override
    public void updateTextAImage(String content, Bitmap bitmap,String lat, String lon, RequestListener listener) {
        WeiboParameters params = buildUpdateParams(content, lat, lon);
        params.put("",bitmap);
        requestAsync(URL_Constant.UPLOADE_URL , params, HTTPMETHOD_POST, listener);
    }

    @Override
    public void updateTextAImage(String status, String imageUrl, String pic_id,String lat, String lon, RequestListener listener) {
        WeiboParameters params = buildUpdateParams(status, lat, lon);
        params.put("url", imageUrl);
        params.put("pic_id", pic_id);
        requestAsync(URL_Constant.UPLOADE_URL_TEXT_URL , params, HTTPMETHOD_POST, listener);
    }

    /**
     * 根据IP请求地理经纬度
     * @param ip
     * @param listener
     */
    @Override
    public void ip2Geo(String ip, RequestListener listener) {
    	WeiboParameters params = new WeiboParameters(mAppKey);
    	params.put("ip", ip);
        requestAsync(URL_Constant.ip_to_geo_URL, params, HTTPMETHOD_GET, listener);
    }
    
    
    /**
     * 根据经纬度获取附近的微博
     * @param longtitude
     * @param latitude
     * @param listener
     */
    @Override
    public void nearby_timeline(Double longtitude, Double latitude, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        String coordinate = longtitude + "," + latitude;
        params.put("coordinate", coordinate);    	
    	requestAsync(URL_Constant.place_nearby_timeline_URL, params, HTTPMETHOD_GET, listener);
    }
    @Override
    public void nearby_timeline(int page, String lat, String lon, RequestListener listener) {
    	WeiboParameters params = new WeiboParameters(mAppKey);
    	params.put("page", page);
        params.put("lat", lat);
        params.put("long", lon);
    	requestAsync(URL_Constant.place_nearby_timeline_URL, params, HTTPMETHOD_GET, listener);
    }

    @Override
    public void repostStatuses(int userId,RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("id",userId);
        requestAsync(URL_Constant.UPDATE_URL , params, HTTPMETHOD_POST, listener);
    }

    @Override
    public void createComments(int userId, String commentContent, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("id",userId);
        params.put("comment",commentContent);
        requestAsync(URL_Constant.UPDATE_URL , params, HTTPMETHOD_POST, listener);
    }

    @Override
    public void usersShow(int userId, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("id",userId);
        requestAsync(URL_Constant.UPDATE_URL , params, HTTPMETHOD_GET, listener);
    }


    // 组装微博请求参数
    private WeiboParameters buildUpdateParams(String content, String lat, String lon) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("status", content);
        if (!TextUtils.isEmpty(lon)) {
            params.put("long", lon);
        }
        if (!TextUtils.isEmpty(lat)) {
            params.put("lat", lat);
        }
        return params;
    }

}
