package com.jy.xinlangweibo.models.net.newsapi;

import com.jy.xinlangweibo.models.net.newsapi.bean.NewsBean;
import com.jy.xinlangweibo.models.net.newsapi.bean.NewsChannelList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by JIANG on 2017/1/4.
 */

public interface NewsApi {

    String HOST = "http://route.showapi.com/";

    /**
     * 新闻查询
     */
    @GET("109-35")
    Observable<NewsBean> getNewsList(@Query("channelId" ) String channelId,
                                     @Query("page" ) String page);

    /**
     * 新闻频道
     */
    @GET("109-34")
    Observable<NewsChannelList> getNewsChannel();



}
