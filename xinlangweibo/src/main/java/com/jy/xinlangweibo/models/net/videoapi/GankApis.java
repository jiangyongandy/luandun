package com.jy.xinlangweibo.models.net.videoapi;


import com.jy.xinlangweibo.models.net.videoapi.videobean.GankHttpResponse;
import com.jy.xinlangweibo.models.net.videoapi.videobean.GankItemBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by codeest on 16/8/19.
 */

public interface GankApis {

    String HOST = "http://gank.io/api/";

    /**
     * 福利列表
     */
    @GET("data/福利/{num}/{page}")
    Observable<GankHttpResponse<List<GankItemBean>>> getGirlList(@Path("num") int num, @Path("page") int page);

}
