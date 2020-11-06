package com.bac.bacplatform.tst;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bac.bacplatform.R;
import com.bac.commonlib.param.CommonParam;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by wujiazhen on 2017/7/31.
 * <p>
 * okhttp 测试 http/2
 */

public class Http2Activity extends AppCompatActivity {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_rv);

       /* Observable.interval(1, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .flatMap(new Func1<Long, Observable<?>>() {
                    @Override
                    public Observable<?> call(Long aLong) {
                        System.out.println(aLong);
                        return HttpHelper.getInstance()
                                .net(Http2Activity.this, new BacHttpBean().setMethodName("BASEXML.QUERY_ANDROID_DOWNLOAD")
                                                .put("versioncode", Constants.APP.VERSION_CODE)
                                        , null, null, null);
                    }
                })
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        System.out.println("HttpHelper");
                        System.out.println(">>>>>>>>>>>>>>" + o);
                        System.out.println("subscribeThread:" + Thread.currentThread().getName());
                        System.out.println("============================");
                    }
                });
                */
        /*HttpHelper.getInstance()
                .net(Http2Activity.this,new BacHttpBean().setMethodName("BASEXML.QUERY_ANDROID_DOWNLOAD")
                                .put("versioncode", Constants.APP.VERSION_CODE)
                        , null, null,null).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("s:" + s);
            }
        });*/
         test();

    }

    private void test() {
        Request request = new Request.Builder()

                .url("http://www.publicobject.com/helloworld.txt")
                //.post(RequestBody.create(JSON, bowlingJson("Jesse", "Jake")))
                .build();

        CommonParam.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String response1Body = response.body().string();
                System.out.println("Response 1 response:          " + response);
                System.out.println("Response 1 cache response:    " + response.cacheResponse());
                System.out.println("Response 1 network response:  " + response.networkResponse());
                //System.out.println("response1Body " + response1Body);
            }
        });
    }

    private void get() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        client = builder.addInterceptor(loggingInterceptor).build();
        System.out.println("------------------get------------------");
        Request request = new Request.Builder()
                //.url("https://raw.github.com/square/okhttp/master/README.md")
                .url("http://httpbin.org/get")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("---------------get---------------");

            }
        });

        System.out.println("------------------post------------------");
    }

    /**
     * String json = bowlingJson("Jesse", "Jake");
     * post("http://www.roundsapp.com/post", json);
     *
     * @param url
     * @param json
     */
    public void post(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("---------------post---------------");

            }
        });
    }

    String bowlingJson(String player1, String player2) {
        return "{'winCondition':'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                + "]}";
    }


}
