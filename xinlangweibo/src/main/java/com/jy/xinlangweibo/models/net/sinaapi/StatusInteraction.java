package com.jy.xinlangweibo.models.net.sinaapi;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.blankj.utilcode.utils.FileUtils;
import com.google.gson.Gson;
import com.jy.xinlangweibo.BaseApplication;
import com.jy.xinlangweibo.constant.Constants;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.CommentListBean;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.RepostListBean;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.StatusBean;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.StatusListBean;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.UidBean;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.UserBean;
import com.jy.xinlangweibo.utils.InternetConnectUtils;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.ToastUtils;
import com.sina.weibo.sdk.openapi.models.Status;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
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
    private OkHttpClient client;

    public static StatusInteraction getInstance(Context context) {
        synchronized (StatusInteraction.class) {
            if(instance == null) {
                instance = new StatusInteraction();
            }
        }
        return instance;
    }

    private StatusInteraction() {
        initOkHttp();

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client).build();

        service = retrofit.create(StatusService.class);
    }

    private void initOkHttp() {
        if (client == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
//            }
            File cacheFile = new File(Constants.PATH_CACHE);
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
            Interceptor cacheInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    if (!InternetConnectUtils.isConnectingToInternet(BaseApplication.getInstance())) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.show(BaseApplication.getInstance(),"网络似乎有问题", Toast.LENGTH_SHORT);
                            }
                        });
                        request = request.newBuilder()
                                .cacheControl(CacheControl.FORCE_CACHE)
                                .build();
                    }
                    int tryCount = 0;
                    Response response = chain.proceed(request);
                    while (!response.isSuccessful() && tryCount < 3) {

//                        KL.d(RetrofitHelper.class, "interceptRequest is not successful - :{}", tryCount);
                        Logger.showLog("interceptRequest is not successful","response");
                        tryCount++;

                        // retry the request
                        response = chain.proceed(request);
                    }


                    if (InternetConnectUtils.isConnectingToInternet(BaseApplication.getInstance())) {
                        int maxAge = 0;
                        // 有网络时, 不缓存, 最大保存时长为0
                        response.newBuilder()
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .removeHeader("Pragma")
                                .build();
                    } else {
                        // 无网络时，设置超时为4周
                        int maxStale = 60 * 60 * 24 * 28;
                        response.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                .removeHeader("Pragma")
                                .build();
                    }
                    return response;
                }
            };
            //设置缓存
            builder.addNetworkInterceptor(cacheInterceptor);
            builder.addInterceptor(cacheInterceptor);
            builder.cache(cache);
            //设置超时
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);
            //错误重连
            builder.retryOnConnectionFailure(true);
            client = builder.build();
        }
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

    public void statusesHome_timeline(String access_token,String page,Action0 action0,Observer<StatusListBean> observer) {
        Observable<StatusListBean> observable =  service.statusesHome_timeline(access_token,page);
        observable.subscribeOn(Schedulers.io()).doOnSubscribe(action0).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public void statusesRepost(String access_token,long id,String status,Observer<StatusBean> observer) {
        Observable<StatusBean> observable = service.statusesRepost(access_token, String.valueOf(id), status);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public void commentsCreate(String access_token,long id,String comment,Observer<StatusBean > observer) {
        Observable<StatusBean> observable = service.commentsCreate(access_token, String.valueOf(id), comment);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public void commentsShow(String access_token, long id, Action0 action0, Observer<CommentListBean > observer) {
        Observable<CommentListBean> observable = service.commentsShow(access_token, id);
        observable.subscribeOn(Schedulers.io()).doOnSubscribe(action0).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public void statusesRepost_timeline(String access_token,long id,Action0 action0,Observer<RepostListBean> observer) {
        Observable<RepostListBean> observable = service.statusesRepost_timeline(access_token, id);
        observable.subscribeOn(Schedulers.io()).doOnSubscribe(action0).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

}
