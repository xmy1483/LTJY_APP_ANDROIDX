package com.bac.bihupapa.weexmodel;

import android.content.Context;

import com.bac.bihupapa.BuildConfig;
import com.bac.bihupapa.R;
import com.bac.bihupapa.conf.Constants;
import com.bac.bihupapa.util.StringUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static com.taobao.weex.WXEnvironment.getApplication;

/**
 * Created by guke on 2017/9/13.
 */

public class Bhpputil {
    //设立静态变量
    private static Bhpputil mBhpputil = null;

    private static IWXAPI iwxapi ;

    private Bhpputil(){
        iwxapi = WXAPIFactory.createWXAPI(getApplication(), null);
        byte seed = (byte) Integer.parseInt(getApplication().getResources().getString(R.string.seed_num) + Constants.XX + BuildConfig.appKeySeed);
        String sPrefix = StringUtil.localDeCode(BuildConfig.wxPrefix, seed);
        String sPostfix = StringUtil.localDeCode(BuildConfig.wxPostfix, seed);
        String s = sPrefix + getApplication().getString(R.string.wx_mid) + sPostfix;
        iwxapi.registerApp(s);
    }

    //开放一个公有方法，判断是否已经存在实例，有返回，没有新建一个在返回
    public static Bhpputil getInstance(){
        if(mBhpputil == null){
            mBhpputil = new Bhpputil();
        }
        return mBhpputil;
    }

    private static String loginphone;
    private static String customersID;

    public String getLoginphone() {
        return loginphone;
    }

    public void setLoginphone(String loginphone) {
        Bhpputil.loginphone = loginphone;
    }


    public  String getCustomersID(){
       return customersID;
   }
    public  void setCustomersID(String customersID){
       Bhpputil.customersID = customersID;
   }

    public  IWXAPI getIwxapi() {
        return iwxapi;
    }

    public  void setIwxapi(IWXAPI iwxapi) {
        Bhpputil.iwxapi = iwxapi;
    }
}
