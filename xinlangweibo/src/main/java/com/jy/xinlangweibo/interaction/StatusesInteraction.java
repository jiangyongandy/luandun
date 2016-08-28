package com.jy.xinlangweibo.interaction;

import com.sina.weibo.sdk.net.RequestListener;

/**
 * Created by JIANG on 2016/8/27.
 */
public interface StatusesInteraction {
    void getStatusesHomeTimeline(int page, RequestListener listener);

    void showComments(int page, long id, RequestListener listener);

    void update(String content, String lat, String lon, RequestListener listener);

    void ip2Geo(String ip, RequestListener listener);

    void nearby_timeline(Double longtitude, Double latitude, RequestListener listener);

    void nearby_timeline(int page, String lat, String lon, RequestListener listener);
}
