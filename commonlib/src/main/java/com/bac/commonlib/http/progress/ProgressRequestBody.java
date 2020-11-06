package com.bac.commonlib.http.progress;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by Wjz on 2017/5/12.
 */

public class ProgressRequestBody extends RequestBody {

    private MediaType contentType;
    private File file;
    private ReqProgressCallBack callBack;

    public ProgressRequestBody(MediaType contentType, File file, ReqProgressCallBack callBack) {
        this.contentType = contentType;
        this.file = file;
        this.callBack = callBack;
    }

    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public long contentLength() {
        return file.length();
    }

private int count=0;
    @Override
    public void writeTo(BufferedSink sink) throws IOException {

        Source source = null;
        try {
            source = Okio.source(file);
            Buffer buf = new Buffer();
            long remaining = contentLength();
            long current = 0;
            for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                sink.write(buf, readCount);
                current += readCount;
                if (callBack != null) {
                    callBack.onProgress(remaining, current, remaining == current);
                }
            }
        } catch (Exception e) {
        } finally {
            if (source != null) {
                source.close();
            }
        }
    }


    /**
     * 响应进度更新
     */
    public interface ReqProgressCallBack {
        void onProgress(long total, long current, boolean complete);
    }

}
