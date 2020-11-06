package com.wjz.weexlib.weex.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.common.WXResponse;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wujiazhen on 2017/6/12.
 *
 * 自定义
 */

public class WeexOkHttpAdapter implements IWXHttpAdapter {
    private ExecutorService mExecutorService;

    private void execute(Runnable runnable) {
        if (mExecutorService == null) {
            mExecutorService = Executors.newFixedThreadPool(3);
        }
        mExecutorService.execute(runnable);
    }

    @Override
    public void sendRequest(final WXRequest request, final OnHttpListener listener) {
        if (listener != null) {
            listener.onHttpStart();
        }
        System.out.println("WeexOkHttpAdapter sendRequest");


        /*
         {
            "paramMap": {
                "user-agent": "Android SDK built for x86_64(Android/7.1.1) (com.bac.bacplatform/2.5.0012) Weex/0.11.0 1440x2392"
            },
            "timeoutMs": 3000,
            "url": "http://10.0.2.2:13456/dist/test.js"
        }
          */

        /*
       {
            "method": "GET",
            "paramMap": {
                "Content-Type": "application/json",
                "user-agent": "Android SDK built for x86_64(Android/7.1.1) (com.bac.bacplatform/2.5.0012) Weex/0.11.0 1440x2392",
                "X-LC-Key": " XfkYkvCvsJ1FkhEqzdTsMnNC",
                "X-LC-Id": "zksrg6fpR18GjAsv0eHPs4Kz-gzGzoHsz"
            },
            "timeoutMs": 3000,
            "url": "https://api.leancloud.cn/1.1/classes/joke?limit=10&&order=-createdAt&&"
        }
        */


        /*
            1. 判断请求方式
                post 请求 需要走本地加密
                无请求方式 默认 get 不需要加密
         */

        final WXResponse response = new WXResponse();
        if ("POST".equals(request.method)) {

            System.out.println("---------------------");
            System.out.println(JSON.toJSONString(request));

            String body = request.body;
            Map<String, String> map = JSON.parseObject(body, new TypeReference<Map<String, String>>() {
            }.getType());

            // 无
           /* HttpHelper.getInstance().bacNet(new BacHttpBean().setMethodName(map.get("methodName")).put("version",map.get("version")))
                    .observeOn(RxScheduler.RxPoolScheduler())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            response.statusCode="200";
                           // response.data = s;
                            response.originalData=s.getBytes();
                            listener.onHttpFinish(response);
                        }
                    });*/


        } else {

            // openConnection

            execute(new Runnable() {
                @Override
                public void run() {


                    try {
                        URL url = new URL(request.url);

                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        int responseCode = connection.getResponseCode();
                        response.statusCode = String.valueOf(responseCode);

                        InputStream inputStream = connection.getInputStream();

                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                        int nRead;
                        int readCount = 0;
                        byte[] data = new byte[2048];

                        int length = data.length;
                        while ((nRead = inputStream.read(data, 0, length)) != -1) {
                            buffer.write(data, 0, nRead);
                            readCount += nRead;
                            System.out.println(readCount);
                            if (listener != null) {
                                listener.onHttpResponseProgress(readCount);
                            }
                        }

                        buffer.flush();

                        response.originalData = buffer.toByteArray();

                        listener.onHttpFinish(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }


}
