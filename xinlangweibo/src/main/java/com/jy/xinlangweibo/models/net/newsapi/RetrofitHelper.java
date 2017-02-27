package com.jy.xinlangweibo.models.net.newsapi;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.jy.xinlangweibo.BaseApplication;
import com.jy.xinlangweibo.constant.Constants;
import com.jy.xinlangweibo.models.net.BasicParamsInterceptor;
import com.jy.xinlangweibo.utils.InternetConnectUtils;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description: RetrofitHelper
 * Creator: jiang
 * date: 2016/9/21 10:03
 */
public class RetrofitHelper {

    private static OkHttpClient okHttpClient = null;
    private static NewsApi newsApi;
    //todo 对rx引起的内存泄漏进行处理
    public static NewsApi getNewsApi() {
        initOkHttp();
        if (newsApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(NewsApi.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            newsApi = retrofit.create(NewsApi.class);
        }
        return newsApi;
    }

    private static void initOkHttp() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
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
                    Response response = null;
                    try {
                        response = chain.proceed(request); //there are socket connect timeout exception
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    while ((response == null || !response.isSuccessful()) && tryCount < 3) {

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

            BasicParamsInterceptor basicParamsInterceptor =
                    new BasicParamsInterceptor.Builder()
                            .addQueryParam("showapi_appid", "30069")
                            .addQueryParam("showapi_sign", "5ea862ca90c348ea82f298f566fc0fb4")
                            .build();

            //设置缓存
            builder.addNetworkInterceptor(cacheInterceptor);
            builder.addInterceptor(cacheInterceptor);
            builder.addInterceptor(basicParamsInterceptor);
            builder.cache(cache);
            //设置超时
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);
            //错误重连
            builder.retryOnConnectionFailure(true);
            okHttpClient = builder.build();
        }
    }
}
