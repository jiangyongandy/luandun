package com.jy.xinlangweibo.models.retrofitservice;

import com.jy.xinlangweibo.models.retrofitservice.bean.StatusListBean;
import com.jy.xinlangweibo.models.retrofitservice.bean.UidBean;
import com.jy.xinlangweibo.models.retrofitservice.bean.UploadPicResultBean;
import com.jy.xinlangweibo.models.retrofitservice.bean.UserBean;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by JIANG on 2016/9/22.
 */
public interface StatusService {
    /**
     * 上传一张图片获取UID
     * @param access_token
     * @param pic
     * @return
     */
    // new code for multiple files
    @Multipart
    @POST("statuses/upload_pic.json")
    HttpResult<UploadPicResultBean> uploadImageForPicId(
            @Part("access_token") RequestBody access_token,
            @Part MultipartBody.Part pic);

    /**
     * 发表带图微博
     * @param access_token
     * @param visible
     * @param status
     * @param pic
     * @return
     */
    @Multipart
    @POST("statuses/upload.json")
    Observable<HttpResult<Status>> uploadFiles(
            @Part("access_token") RequestBody access_token,
            @Part("visible") RequestBody visible,
            @Part("status") RequestBody status,
            @Part MultipartBody.Part pic);

    /**
     * 发表文字微博
     * @param access_token
     * @param visible
     * @param status
     * @return
     */
    @FormUrlEncoded
    @POST("statuses/update.json")
    Observable<HttpResult<Status>> update(@Field("access_token" ) String access_token,
                              @Field(value = "visible",encoded = true ) String visible,
                              @Field(value = "status",encoded = true) String status);

    /**
     * 展示用户信息
     * @param access_token
     * @param screen_name
     * @return
     */
    @GET("users/show.json")
    Observable<UserBean> userShow(@Query("access_token" ) String access_token,
                                  @Query("screen_name") String screen_name);

    /**
     * 展示用户信息
     * @return
     */
    @GET("users/show.json")
    Observable<com.sina.weibo.sdk.openapi.models.User> userShow(@QueryMap Map<String, String> params);

    /**
     * 展示用户信息
     * @return
     */
    @GET("users/show.json")
    Observable<UserBean> userShow2(@Query("access_token" ) String access_token,
                                   @Query(value = "uid") String uid);

    /**
     * 返回指定某个用户的微博
     * @param access_token
     * @param screen_name
     * @return
     */
    @GET("statuses/user_timeline.json")
    Observable<StatusListBean>statusesUser_timeline(@Query("access_token" ) String access_token,
                                                             @Query("screen_name") String screen_name);

    /**
     * 转发微博
     * @param access_token
     * @param id
     * @param status
     * @return
     */
    @FormUrlEncoded
    @POST("statuses/repost.json")
    Observable<Status> statusesRepost(@Field("access_token" ) String access_token,
                                          @Field(value = "id",encoded = true ) String id,
                                          @Field(value = "status",encoded = true) String status);

    /**
     * 评论某条微博
     * @param access_token
     * @param id
     * @param comment
     * @return
     */
    @FormUrlEncoded
    @POST("comments/create.json")
    Observable<Status> commentsCreate(@Field("access_token" ) String access_token,
                                          @Field(value = "id",encoded = true ) String id,
                                          @Field(value = "comment",encoded = true) String comment);

    /**
     * 返回授权用户的uid
     * @param access_token
     * @return
     */
    @GET("account/get_uid.json")
    Observable<UidBean> accountGet_uid(@Query("access_token" ) String access_token);
}
