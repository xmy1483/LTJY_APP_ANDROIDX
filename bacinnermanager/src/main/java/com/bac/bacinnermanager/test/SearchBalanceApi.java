package com.bac.bacinnermanager.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by wujiazhen on 2017/8/18.
 */

public interface SearchBalanceApi {
    @GET("/tools/servlet/phoneServlet")
    Observable<List<Map<String,Object>>> getPhonePrefix(
            @QueryMap HashMap<String, Object> maps);

    @GET("/tools/servlet/phoneNumSaveServlet")
    Observable<String> savePhoneBalance(
            @QueryMap HashMap<String, Object> maps);


    @POST("http://www.js.10086.cn/alife/v1/user/userBalance")
    Observable<Map<String,Object>> getPhoneBalance(@QueryMap HashMap<String, Object> maps);
}
