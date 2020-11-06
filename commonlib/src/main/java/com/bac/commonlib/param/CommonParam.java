package com.bac.commonlib.param;

import android.app.Application;
import android.content.pm.ApplicationInfo;

import com.bac.commonlib.http.BacApi;

import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by wujiazhen on 2017/8/8.
 */

public class CommonParam {

    private static Application application;
    private static String loginPhone;
    private static String versionName;
    private static String seed;
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;
    private static BacApi bacApi;
    private static String s;
    private static String pid;

    private Map<String, String> mRsaMap;

    private String mToken;

    private String mSession;

    private String mPrivateKey;

    private boolean isDebug;
    private CommonParam() {
    }

    private static final CommonParam COMMONPARAM = new CommonParam();

    //静态工厂方法
    public static CommonParam getInstance() {
        return COMMONPARAM;
    }

    public Application getApplication() {
        return application;
    }

    public CommonParam setApplication(Application application) {
        CommonParam.application = application;

        // 判断debug | release
        isDebug = (application.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        return this;
    }

    public String getLoginPhone() {
        return loginPhone;
    }

    public CommonParam setLoginPhone(String loginPhone) {
        CommonParam.loginPhone = loginPhone;
        return this;
    }

    public String getVersionName() {
        return versionName;
    }

    public CommonParam setVersionName(String versionName) {
        CommonParam.versionName = versionName;
        return this;
    }

    public String getSeed() {
        return seed;
    }

    public CommonParam setSeed(String seed) {
        CommonParam.seed = seed;
        return this;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public CommonParam setOkHttpClient(OkHttpClient okHttpClient) {
        CommonParam.okHttpClient = okHttpClient;
        return this;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public CommonParam setRetrofit(Retrofit retrofit) {
        CommonParam.retrofit = retrofit;
        return this;
    }

    public BacApi getBacApi() {
        return bacApi;
    }

    public CommonParam setBacApi(BacApi bacApi) {
        CommonParam.bacApi = bacApi;
        return this;
    }

    public String getS() {
        return s;
    }

    public CommonParam setS(String s) {
        CommonParam.s = s;
        return this;
    }

    public String getPid() {
        return pid;
    }

    public CommonParam setPid(String pid) {
        CommonParam.pid = pid;
        return this;
    }

    public Map<String, String> getmRsaMap() {
        return mRsaMap;
    }

    public CommonParam setmRsaMap(Map<String, String> mRsaMap) {
        this.mRsaMap = mRsaMap;
        return this;
    }

    public String getmToken() {
        return mToken;
    }

    public CommonParam setmToken(String mToken) {
        this.mToken = mToken;
        return this;
    }

    public String getmSession() {
        return mSession;
    }

    public CommonParam setmSession(String mSession) {
        this.mSession = mSession;
        return this;
    }

    public String getmPrivateKey() {
        return mPrivateKey;
    }

    public CommonParam setmPrivateKey(String mPrivateKey) {
        this.mPrivateKey = mPrivateKey;
        return this;
    }

    public boolean isDebug() {
        return isDebug;
    }


}
