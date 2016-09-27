package com.jy.xinlangweibo.models.retrofitservice;

import com.blankj.utilcode.utils.FileUtils;
import com.google.gson.Gson;
import com.sina.weibo.sdk.openapi.models.Status;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JIANG on 2016/9/22.
 */
public class StatusInteraction {

    private static final String BASE_URL = "https://upload.api.weibo.com/2/";
//    private static final String BASE_URL = "https://api.douban.com/v2/movie/";

    private static StatusInteraction instance;
    private final Retrofit retrofit;
    private final StatusService service;

    public static StatusInteraction getInstance() {
        synchronized (StatusInteraction.class) {
            if(instance == null) {
                instance = new StatusInteraction();
            }
        }
        return instance;
    }

    private StatusInteraction() {
        OkHttpClient client = new OkHttpClient();
        int cacheSize = 10 * 1024 * 1024; // 10 MiBCache
//		Cache cache = new Cache(this.getBaseContext().getCacheDir(), cacheSize);
//		client.setCache(cache);


        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client).build();

        service = retrofit.create(StatusService.class);
    }

    public String uploadImageForPicId(String access_token,String fileUri) {
        File file = FileUtils.getFileByPath(fileUri);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("pic", file.getName(), requestFile);

        // add another part within the multipart request
        RequestBody tokenBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), access_token);
         return service.uploadImageForPicId(tokenBody, body).getData().getPic_id();
    }

    public void uploadFile(String access_token,String status,String fileUri) {
        // create upload service client

        File file = FileUtils.getFileByPath(fileUri);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("pic", file.getName(), requestFile);

        // add another part within the multipart request
        RequestBody tokenBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), access_token);

        RequestBody visibleBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), "2");

        RequestBody weiboBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), status);
        // finally, execute the request
        Observable<HttpResult<Status>> observable = service.uploadFiles(tokenBody,visibleBody,weiboBody, body);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HttpResult<Status>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("ON ERROR--file-----------"+e.toString());
            }

            @Override
            public void onNext(HttpResult<Status> movieEntity) {
                System.out.println("ON NEXT-----file--------"+new Gson().toJson(movieEntity));
            }
        });
    }

    public void update(String access_token,String status) {
        // finally, execute the request
        Observable<HttpResult<Status>> observable =  service.update(access_token,"1",status);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HttpResult<Status>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("ON ERROR-------------"+e.toString());
            }

            @Override
            public void onNext(HttpResult<Status> movieEntity) {
                System.out.println("ON NEXT-------------"+new Gson().toJson(movieEntity));
            }
        });

/*        Call<HttpResult<Status>> call = service.update(access_token, "2", status);
        call.enqueue(new Callback<HttpResult<Status>>() {
            @Override
            public void onResponse(Call<HttpResult<Status>> call, Response<HttpResult<Status>> response) {
                System.out.println("ON NEXT-------------"+response.body()+"  "+response.message()+"  "+response.code());
            }

            @Override
            public void onFailure(Call<HttpResult<Status>> call, Throwable t) {
                System.out.println("onFailure-------------"+t.toString());
            }
        });*/

    }
}
