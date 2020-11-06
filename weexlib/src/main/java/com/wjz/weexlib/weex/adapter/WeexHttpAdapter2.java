package com.wjz.weexlib.weex.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.common.WXResponse;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wujiazhen on 2017/6/12.
 * <p>
 * weex 官方提供
 */

public class WeexHttpAdapter2 implements IWXHttpAdapter {
    private static final IEventReporterDelegate DEFAULT_DELEGATE = new NOPEventReportDelegate();
    private final Context context;
    private ExecutorService mExecutorService;

    public WeexHttpAdapter2(Context context) {
        this.context = context;
    }

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
        System.out.println("---    sendRequest    -----");
        // 子线程
        execute(new Runnable() {
            @Override
            public void run() {
                /*
                {"paramMap":{"user-agent":"MI 4LTE(Android/6.0.1) (com.bac.bacplatform/2.5.0018) Weex/0.11.0 1080x1920"},
                "timeoutMs":3000,"url":"http://file/dist/main/main.js"}
                */

                System.out.println("---    sendRequest  run  -----");
                WXResponse response = new WXResponse();

                String url = request.url;
                System.out.println("http url :"+url);
                Uri uri = Uri.parse(url);
                if (TextUtils.equals(uri.getEncodedAuthority(), "file")) {
                    String path = uri.getEncodedPath();
                    File dir = context.getExternalFilesDir(null);
                    File file = new File(dir, path);
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(file);
                        response.originalData = readInputStreamAsBytes(fileInputStream, listener);
                        response.statusCode = "200";
                    } catch (Exception e) {
                    } finally {
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                    if (listener != null) {
                        // 完成
                        listener.onHttpFinish(response);
                    }
                    return;
                }


                System.out.println("===========http============");

                IEventReporterDelegate reporter = getEventReporterDelegate();
                try {

                    // http 请求
                    HttpURLConnection connection = openConnection(request, listener);


                    // do nothing
                    reporter.preConnect(connection, request.body);


                    // connection head
                    Map<String, List<String>> headers = connection.getHeaderFields();

                    // code
                    int responseCode = connection.getResponseCode();

                    if (listener != null) {
                        listener.onHeadersReceived(responseCode, headers);
                    }

                    // do nothing
                    reporter.postConnect();


                    response.statusCode = String.valueOf(responseCode);

                    if (responseCode >= 200 && responseCode <= 299) {

                        InputStream rawStream = connection.getInputStream();

                        rawStream = reporter.interpretResponseStream(rawStream);


                        response.originalData = readInputStreamAsBytes(rawStream, listener);

                    } else {

                        response.errorMsg = readInputStream(connection.getErrorStream(), listener);
                    }


                    if (listener != null) {
                        // 完成
                        listener.onHttpFinish(response);
                    }

                } catch (IOException | IllegalArgumentException e) {
                    e.printStackTrace();
                    response.statusCode = "-1";
                    response.errorCode = "-1";
                    response.errorMsg = e.getMessage();
                    if (listener != null) {
                        listener.onHttpFinish(response);
                    }
                    if (e instanceof IOException) {
                        reporter.httpExchangeFailed((IOException) e);
                    }
                }
            }
        });
    }


    /**
     * Opens an {@link HttpURLConnection} with parameters.
     *
     * @param request
     * @param listener
     * @return an open connection
     * @throws IOException
     */
    private HttpURLConnection openConnection(WXRequest request, OnHttpListener listener) throws IOException {

        System.out.println("url： " + JSON.toJSONString(request));

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

        URL url = new URL(request.url);
        // 创建 HttpURLConnection
        HttpURLConnection connection = createConnection(url);
        connection.setConnectTimeout(request.timeoutMs);
        connection.setReadTimeout(request.timeoutMs);
        connection.setUseCaches(false);
        connection.setDoInput(true);

        // header
        if (request.paramMap != null) {
            Set<String> keySets = request.paramMap.keySet();
            for (String key : keySets) {
                connection.addRequestProperty(key, request.paramMap.get(key));
            }
        }

        // method
        if ("POST".equals(request.method) || "PUT".equals(request.method) || "PATCH".equals(request.method)) {

            connection.setRequestMethod(request.method);

            if (request.body != null) {

                if (listener != null) {
                    listener.onHttpUploadProgress(0);
                }

                connection.setDoOutput(true);

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                //TODO big stream will cause OOM; Progress callback is meaningless
                out.write(request.body.getBytes());
                out.close();

                if (listener != null) {
                    listener.onHttpUploadProgress(100);
                }
            }

        } else if (!TextUtils.isEmpty(request.method)) {

            connection.setRequestMethod(request.method);
        } else {
            // 默认 get
            connection.setRequestMethod("GET");
        }

        return connection;
    }

    private byte[] readInputStreamAsBytes(InputStream inputStream, OnHttpListener listener) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        int readCount = 0;
        byte[] data = new byte[2048];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
            readCount += nRead;
            if (listener != null) {
                listener.onHttpResponseProgress(readCount);
            }
        }

        buffer.flush();

        return buffer.toByteArray();
    }

    private String readInputStream(InputStream inputStream, OnHttpListener listener) throws IOException {
        if (inputStream == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        char[] data = new char[2048];
        int len;
        while ((len = localBufferedReader.read(data)) != -1) {
            builder.append(data, 0, len);
            if (listener != null) {
                listener.onHttpResponseProgress(builder.length());
            }
        }
        localBufferedReader.close();
        return builder.toString();
    }

    /**
     * Create an {@link HttpURLConnection} for the specified {@code url}.
     */
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }


    public
    @NonNull
    IEventReporterDelegate getEventReporterDelegate() {
        return DEFAULT_DELEGATE;
    }

    public interface IEventReporterDelegate {
        void preConnect(HttpURLConnection connection, @Nullable String body);

        void postConnect();

        InputStream interpretResponseStream(@Nullable InputStream inputStream);

        void httpExchangeFailed(IOException e);
    }

    private static class NOPEventReportDelegate implements IEventReporterDelegate {
        @Override
        public void preConnect(HttpURLConnection connection, @Nullable String body) {
            //do nothing
        }

        @Override
        public void postConnect() {
            //do nothing
        }

        @Override
        public InputStream interpretResponseStream(@Nullable InputStream inputStream) {
            return inputStream;
        }

        @Override
        public void httpExchangeFailed(IOException e) {
            //do nothing
        }
    }
}
