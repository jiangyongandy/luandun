package com.jy.xinlangweibo.models.retrofitservice;

import android.content.Context;
import android.widget.Toast;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.NetworkUtils;
import com.google.gson.Gson;
import com.jy.xinlangweibo.models.bean.StatusBean;
import com.jy.xinlangweibo.models.bean.StatusListBean;
import com.jy.xinlangweibo.models.bean.UidBean;
import com.jy.xinlangweibo.models.bean.UserBean;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.ToastUtils;
import com.sina.weibo.sdk.openapi.models.Status;

import java.io.File;
import java.util.Map;

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
import rx.functions.Func1;
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

    public static StatusInteraction getInstance(Context context) {
        if(!NetworkUtils.isAvailable(context)) {
            ToastUtils.show(context,"网络似乎有问题", Toast.LENGTH_SHORT);
        }
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

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getResultCode() != 0) {
                throw new ApiException(httpResult.getResultCode());
            }
            return httpResult.getData();
        }
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
        RequestBody requestFile;
//        if( file.length() >= 5*1000000) {
//            Logger.showLog("图片太大，压缩前的大小========"+file.length(),"图片压缩");
//            Bitmap bitmap = ImageUtils.getBitmap(file); 获取位图不能直接用此方法，图片太大会导致00M.
//            byte[] bytes = ImageUtils.bitmap2Bytes(ImageUtils.compressByQuality(bitmap, (long)5 * 1000000, true), Bitmap.CompressFormat.PNG);
//            Logger.showLog("图片太大，压缩后的大小========"+bytes.length,"图片压缩");
//            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
//        }else {
            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        }
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("pic", file.getName(), requestFile);

        // add another part within the multipart request
        RequestBody tokenBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), access_token);

        RequestBody visibleBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), "0");

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
        Observable<HttpResult<Status>> observable =  service.update(access_token,"0",status);
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

    public void userShow(String access_token,String screen_name,Observer<UserBean> observer) {
        Observable<UserBean> observable =  service.userShow(access_token,screen_name);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public void userShow(final String access_token, Observer<UserBean> observer) {
        Observable<UidBean> tokenObservable = service.accountGet_uid(access_token);
        tokenObservable.flatMap(new Func1<UidBean, Observable<UserBean>>() {
            @Override
            public Observable<UserBean> call(UidBean uidBean) {
                Logger.showLog("fun1================"+uidBean.uid,"flatmap");
                // 返回 Observable<UserBean>，先请求uid，并在响应后发送uid到usershow
                return service.userShow2(access_token, String.valueOf(uidBean.uid));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public void statusesUser_timeline(String access_token,String screen_name,Observer<StatusListBean  > observer) {
        Observable<StatusListBean> observable =  service.statusesUser_timeline(access_token,screen_name);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public void statusesUser_timeline2(Map<String, String> params, Observer<StatusListBean  > observer) {
        Observable<StatusListBean> observable =  service.statusesUser_timeline2(params);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public void statusesPublic_timeline(String access_token,String page,Observer<StatusListBean> observer) {
        Observable<StatusListBean> observable =  service.statusesPublic_timeline(access_token,page);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public void statusesRepost(String access_token,long id,String status,Observer<StatusBean> observer) {
        Observable<StatusBean> observable = service.statusesRepost(access_token, String.valueOf(id), status);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public void commentsCreate(String access_token,long id,String comment,Observer<StatusBean > observer) {
        Observable<StatusBean> observable = service.commentsCreate(access_token, String.valueOf(id), comment);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

}
