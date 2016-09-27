package com.jy.xinlangweibo.models;

import android.graphics.Bitmap;

import com.sina.weibo.sdk.net.RequestListener;

/**
 * Created by JIANG on 2016/8/27.
 */
public interface StatusesInteraction {
    void getStatusesHomeTimeline(int page, RequestListener listener);

    void showComments(int page, long id, RequestListener listener);

    void update(String content, String lat, String lon, RequestListener listener);

    void updateTextAImage(String content, Bitmap bitmap,String lat, String lon, RequestListener listener);

    void updateTextAImage(String status, String imageUrl, String pic_id,String lat, String lon, RequestListener listener);

    void ip2Geo(String ip, RequestListener listener);

    void nearby_timeline(Double longtitude, Double latitude, RequestListener listener);

    void nearby_timeline(int page, String lat, String lon, RequestListener listener);

    void repostStatuses(int userId,RequestListener listener);

    void createComments(int userId,String commentContent,RequestListener listener);

    void usersShow(int userId,RequestListener listener);
}
