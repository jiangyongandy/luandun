package com.jy.xinlangweibo.models.retrofitservice;

import com.jy.xinlangweibo.models.retrofitservice.bean.UploadPicResultBean;
import com.sina.weibo.sdk.openapi.models.Status;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by JIANG on 2016/9/22.
 */
public interface StatusService {
    // new code for multiple files
    @Multipart
    @POST("statuses/upload_pic.json")
    HttpResult<UploadPicResultBean> uploadImageForPicId(
            @Part("access_token") RequestBody access_token,
            @Part MultipartBody.Part pic);

    @Multipart
    @POST("statuses/upload.json")
    Observable<HttpResult<Status>> uploadFiles(
            @Part("access_token") RequestBody access_token,
            @Part("visible") RequestBody visible,
            @Part("status") RequestBody status,
            @Part MultipartBody.Part pic);

    @FormUrlEncoded
    @POST("statuses/update.json")
    Observable<HttpResult<Status>> update(@Field("access_token" ) String access_token,
                              @Field(value = "visible", encoded = true) String visible,
                              @Field(value = "status",encoded = true) String status);

/*    @FormUrlEncoded
    @POST("statuses/update.json")
    Call<HttpResult<Status>> update(@Field("access_token" ) String access_token,
                                    @Field(value = "visible", encoded = true) String visible,
                                    @Field(value = "status",encoded = true) String status);*/
}
