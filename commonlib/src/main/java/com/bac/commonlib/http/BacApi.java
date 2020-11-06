package com.bac.commonlib.http;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.commonlib.http
 * 创建人：Wjz
 * 创建时间：2017/4/7
 * 类描述：
 */

public interface BacApi {
    @POST("HttpServer")
    @FormUrlEncoded
    Observable<HashMap<String, Object>> get(@FieldMap Map<String, String> map);

    @Multipart
    @POST("UploadFileServer")
    Observable<String> upload(@Part("JSON") RequestBody string, @Part MultipartBody.Part file);

}
