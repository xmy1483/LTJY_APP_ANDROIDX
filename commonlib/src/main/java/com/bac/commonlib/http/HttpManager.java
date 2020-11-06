package com.bac.commonlib.http;

import android.content.Context;

import com.bac.commonlib.http.cookie.CookieJarImpl;
import com.bac.commonlib.http.cookie.store.MemoryCookieStore;
import com.bac.commonlib.http.fastjson.FastJsonConverterFactory;
import com.bac.commonlib.http.https.HttpsUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by Wjz on 2017/7/6.
 */

public class HttpManager {

    public static OkHttpClient sOkHttpClient;
    public static Retrofit sRetrofit;

    public static OkHttpClient okHttpInit(Context context, boolean isLog) {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        CookieJarImpl cookieJar = new CookieJarImpl(new MemoryCookieStore());
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (isLog) {
            builder.addInterceptor(loggingInterceptor);
        }
        sOkHttpClient = builder
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                //.addNetworkInterceptor(new UnzippingInterceptor())
                //.addNetworkInterceptor(new GzipRequestInterceptor())
                //.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cache(new Cache(new File(context.getExternalCacheDir(), "cache.tmp"), 10 * 1024 * 1024))// 10 MiB
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .retryOnConnectionFailure(true)
                .build();
        return sOkHttpClient;
    }

    public static Retrofit retrofitInit(OkHttpClient okHttpClient, String url) {
        //Constants.URL.BASEURL
        sRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .build();
        return sRetrofit;
    }

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .header("Cache-Control", "max-age=60")
                    .build();
        }
    };

}
