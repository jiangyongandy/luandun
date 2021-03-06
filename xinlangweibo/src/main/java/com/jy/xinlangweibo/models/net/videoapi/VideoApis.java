// (c)2016 Flipboard Inc, All Rights Reserved.

package com.jy.xinlangweibo.models.net.videoapi;


import com.jy.xinlangweibo.models.net.videoapi.videobean.HomePageResultBean;
import com.jy.xinlangweibo.models.net.videoapi.videobean.VideoListBean;
import com.jy.xinlangweibo.models.net.videoapi.videobean.VideoRes;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description: VideoApis
 * Creator: yxc
 * date: 2016/9/8 14:05
 */

public interface VideoApis {
    String HOST = "http://api.svipmovie.com/front/";

    /**
     * 首页
     *
     * @return
     */
    @GET("homePageApi/homePage.do")
    Observable<HomePageResultBean> getHomePage();

    /**
     * 影片详情
     *
     * @param mediaId 影片id
     * @return
     */
    @GET("videoDetailApi/videoDetail.do")
    Observable<VideoHttpResponse<VideoRes>> getVideoInfo(@Query("mediaId") String mediaId);

    /**
     * 影片分类列表
     * @param catalogId
     * @param pnum
     * @return
     */
    @GET("columns/getVideoList.do")
    Observable<VideoListBean> getVideoList(@Query("catalogId") String catalogId, @Query("pnum") String pnum);

    /**
     * 影片搜索
     *
     * @param pnum
     * @return
     */
    @GET("searchKeyWordApi/getVideoListByKeyWord.do")
    Observable<VideoListBean> getVideoListByKeyWord(@Query("keyword") String keyword, @Query("pnum") String pnum);

    /**
     * 获取评论列表
     * @param mediaId
     * @param pnum
     * @return
     */
    @GET("Commentary/getCommentList.do")
    Observable<VideoHttpResponse<VideoRes>> getCommentList(@Query("mediaId") String mediaId, @Query("pnum") String pnum);
}
