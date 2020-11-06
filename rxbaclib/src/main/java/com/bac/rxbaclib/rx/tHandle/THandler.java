package com.bac.rxbaclib.rx.tHandle;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Wjz on 2017/5/27.
 * <p>
 * 使用方式：
 * THandler tHandler = new THander<Object>(new Handler);
 * tHandler.start();
 * tHandler.getLooper();
 * tHandler.setHanderListener(new THanderListener);
 * tHandler.clearQueue();
 * tHandler.quit();
 */

public class THandler<T> extends HandlerThread {
    private static final String NAME = THandler.class.getSimpleName();


    private static final int ARG_1 = 0;

    private ConcurrentMap<T, String> cMap = new ConcurrentHashMap<>();
    private Handler handler;
    private Handler mResponseHandler;

    public THandler(Handler responseHandler) {
        super(NAME);
        mResponseHandler = responseHandler;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == ARG_1) {
                    // 处理数据
                    handleRequest((T) msg.obj);
                }
            }
        };
    }

    /**
     * 子线程handler
     *
     * @param obj
     */
    private void handleRequest(final T obj) {
        try {
            final String url = cMap.get(obj);
            if (TextUtils.isEmpty(url)) {
                return;
            }

            // 逻辑处理
            byte[] urlBytes = getUrlBytes(url);
            final String s = new String(urlBytes, StandardCharsets.UTF_8);

            // 主线程handler
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {

                    if (cMap.get(obj) != url) {
                        return;
                    }

                    cMap.remove(obj);
                    tHanderListener.onTHanderListener(obj, s);

                }
            });

        } catch (Exception e) {
        }
    }

    private byte[] getUrlBytes(String url) throws IOException {

        URL u = new URL(url);
        HttpURLConnection openConnection = (HttpURLConnection) u.openConnection();
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = openConnection.getInputStream();
            if (openConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(openConnection.getResponseMessage() + ": with" + url);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            return outputStream.toByteArray();
        } finally {
            openConnection.disconnect();
        }
    }


    public void queueThumbnail(T target, String url) {

        if (TextUtils.isEmpty(url)) {
            cMap.remove(target);
        } else {
            cMap.put(target, url);
            handler.obtainMessage(ARG_1, target)
                    .sendToTarget();
        }
    }

    public void clearQueue() {
        handler.removeMessages(ARG_1);
    }

    private THanderListener tHanderListener;

    public interface THanderListener<T, R extends Object> {
        void onTHanderListener(T target, R source);
    }

    public void setHanderListener(THanderListener tHanderListener) {
        this.tHanderListener = tHanderListener;
    }
}
