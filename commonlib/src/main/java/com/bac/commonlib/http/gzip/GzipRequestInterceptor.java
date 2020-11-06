package com.bac.commonlib.http.gzip;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.GzipSink;
import okio.Okio;

/**
 * Created by wujiazhen on 2017/8/10.
 */

public class GzipRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
            return chain.proceed(originalRequest);
        }

        Request compressedRequest = originalRequest.newBuilder()
                .header("Content-Encoding", "gzip")
                //.method(originalRequest.method(), requestBodyWithContentLength(gzip(originalRequest.body())))
                //.method(originalRequest.method(), gzip(originalRequest.body()))
                .build();
        Headers headers = compressedRequest.headers();
        //System.out.println(headers.toString());
        return chain.proceed(compressedRequest);
    }


    /**
     * gzip 压缩方法
     * @param body
     * @return
     */
    private RequestBody gzip(final RequestBody body) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }


            @Override
            public long contentLength() {
                return -1; // We don't know the compressed length in advance!
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }

    private RequestBody requestBodyWithContentLength(final RequestBody body) throws IOException {

        final Buffer buffer = new Buffer();
        body.writeTo(buffer);

        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() {
                return buffer.size();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                ByteString snapshot = buffer.snapshot();
                sink.write(snapshot);
            }
        };
    }
}
