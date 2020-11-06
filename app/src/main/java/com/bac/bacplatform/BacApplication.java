package com.bac.bacplatform;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import androidx.multidex.MultiDex;
import android.text.TextUtils;

import com.android.recharge.ObuInterface;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.module.login.weiixnmodule.WeinxinModule;
import com.bac.bacplatform.old.module.etc.weexmodule.XybModule;
import com.bac.bacplatform.repository.database.LocalDataDbHelper;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.weex.ActiviteTicketModel;
import com.bac.bacplatform.weex.VoucherModel;
import com.bac.bacplatform.weex_modules.OilRechargeModule;
import com.bac.bihupapa.weexmodel.BhppHttpModule;
import com.bac.bihupapa.weexmodel.BhppLocalModule;
import com.bac.bihupapa.weexmodel.BhppQRcodeModule;
import com.bac.bihupapa.weexmodel.JDKDWeexModule;
import com.bac.bihupapa.weexmodel.SGWeexModule;
import com.bac.commonlib.http.BacApi;
import com.bac.commonlib.param.CommonParam;
import com.bac.commonlib.services.MoveAssertsService;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wjz.weexlib.weex.WeexManager;
import com.wjz.weexlib.weex.adapter.ImageAdapter;
import com.wjz.weexlib.weex.adapter.WeexHttpAdapter2;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import static com.bac.bacplatform.BuildConfig.VERSION_NAME;
import static com.bac.bacplatform.conf.Constants.APP.DEBUG;
import static com.bac.commonlib.http.HttpManager.okHttpInit;
import static com.bac.commonlib.http.HttpManager.retrofitInit;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform
 * 创建人：Wjz
 * 创建时间：2017/4/7
 * 类描述：
 */

public class BacApplication extends Application {
    private static OkHttpClient sOkHttpClient;
    private static Retrofit sRetrofit;
    private static BacApi sBacApi;
    private  static String sSeed;
    private static BacApplication bacApplication;


    //微信支付核心api
    private static IWXAPI mWXApi;
    private LocalDataDbHelper localDataDbHelper;

    private static String loginPhone;

    private final String TAG = getClass().getSimpleName();

    public static ObuInterface obuInterface = null;


    @Override
    public void onCreate() {
        super.onCreate();
        //设置百度定位key值
        try {
            ApplicationInfo appi = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            System.out.println("=============================================原先的key"+appi.metaData.getString("com.baidu.lbsapi.API_KEY"));
            appi.metaData.putString("com.baidu.lbsapi.API_KEY", "p7okZklStXoyVjvfg5lcnPvcKzgG9UyB");
            System.out.println("=============================================修改后的key"+appi.metaData.getString("com.baidu.lbsapi.API_KEY"));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //蓝牙初始化
//        Log.e(TAG, "蓝牙初始化");
        obuInterface = new ObuInterface(this);
        obuInterface.initialize();

        // 将weex 的文件从 asserts 复制到 files 中
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(MoveAssertsService.newIntent(this));
//        } else {
        startService(MoveAssertsService.newIntent(this));
//        }
        bacApplication = this;

        /* 获取隐藏在本地的AES 秘钥*/
        sSeed = doSeedKey();

        /*初始化 网络请求*/
        sOkHttpClient = okHttpInit(this,DEBUG);
        sRetrofit = retrofitInit(sOkHttpClient, Constants.URL.BASEURL);
        sBacApi = sRetrofit.create(BacApi.class);

        // 微信
        mWXApi = WXAPIFactory.createWXAPI(bacApplication, null);
        // mWXApi.handleIntent(mActivity.getIntent(), this);
        byte seed = (byte) Integer.parseInt(getResources().getString(R.string.seed_num) + Constants.XX + BuildConfig.appKeySeed);
        String sPrefix = StringUtil.localDeCode(BuildConfig.wxPrefix, seed);
        String sPostfix = StringUtil.localDeCode(BuildConfig.wxPostfix, seed);
        String s = sPrefix + getString(R.string.wx_mid) + sPostfix;

        CommonParam.getInstance()
                .setApplication(this)
                .setS(this.getString(R.string.seed_num) + Constants.XX + BuildConfig.appKeySeed + BuildConfig.appKeySeed2)
                .setSeed(sSeed)
                .setVersionName(VERSION_NAME)
                .setPid("bacplatform")
                .setBacApi(sBacApi)
                .setOkHttpClient(sOkHttpClient)
                .setRetrofit(sRetrofit);

        mWXApi.registerApp(s);

        // 初始化db
        localDataDbHelper = new LocalDataDbHelper(this);

        // weex 组建初始化
        WeexManager.register(this,
                new InitConfig.Builder()
                        .setHttpAdapter(new WeexHttpAdapter2(this))
                        .setImgAdapter(new ImageAdapter())
                        .build()
        );
        try {
            WXSDKEngine.registerModule("getTicket", VoucherModel.class);
            WXSDKEngine.registerModule("activeTicket", ActiviteTicketModel.class);
            // bhpp 注册 模块
            WXSDKEngine.registerModule("httpModule", BhppHttpModule.class);
            WXSDKEngine.registerModule("QRcodeModule", BhppQRcodeModule.class);
            WXSDKEngine.registerModule("LocalModule", BhppLocalModule.class);
            //jindong 注册 模块
            WXSDKEngine.registerModule("weexModule", JDKDWeexModule.class);
            //苏果卡  注册模块
            WXSDKEngine.registerModule("weexSGModule", SGWeexModule.class);
            //ETC 注册模块
            WXSDKEngine.registerModule("etcModule", XybModule.class);
            //微信登录 注册模块
            WXSDKEngine.registerModule("weixinModule", WeinxinModule.class);
            //油卡充值界面
            WXSDKEngine.registerModule("OilRechargeModule", OilRechargeModule.class);

        } catch (WXException e) {
        }

/*
     debugCompile 'com.squareup.leakcanary:leakcanary-android:1.5'
        releaseCompile 'com.squareup.leakcanary:leakcanary-android-no-op:1.5'
         if (DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return;
            }
            LeakCanary.install(this);
        }*/
        /*Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                ex.printStackTrace();
            }
        });*/
    }
    private static boolean isEnabled(Activity activity) {
        String pkgName = activity.getPackageName();
        final String flat = Settings.Secure.getString(activity.getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void checkNotify(final Activity activity) {
        if (!isEnabled(activity) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            new AlertDialog.Builder(activity)
                    .setMessage("是否开启通知使用权，确保行程管理正常运行，从而改善您的驾驶行为？")
                    .setTitle("通知使用权")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            activity.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create().show();
        }
    }

    /**
     * 获取本地隐藏的seed
     *
     * @return
     * @throws Exception
     */
    private String doSeedKey() {
        return "abcd".concat(StringUtil.localDeCode(getResources().getString(R.string.sec_prefix)
                        + BuildConfig.appKeyMid + Constants.SECODELAST,
                (byte) Integer.parseInt(getResources().getString(R.string.seed_num) + Constants.XX + BuildConfig.appKeySeed)));
    }

    public static OkHttpClient getOkHttpClient() {
        return sOkHttpClient;
    }


    public static Retrofit getRetrofit() {
        return sRetrofit;
    }

    public static BacApi getBacApi() {
        return sBacApi;
    }

    public static String getSeed() {
        return sSeed;
    }

    public static BacApplication getBacApplication() {
        return bacApplication;
    }

    public static IWXAPI getmWXApi() {
        return mWXApi;
    }

    public LocalDataDbHelper getLocalDataDbHelper() {
        return localDataDbHelper;
    }


    public static String getLoginPhone() {
        return loginPhone;
    }
    //
    public static void setLoginPhone(String loginPhone) {
        BacApplication.loginPhone = loginPhone;
        CommonParam.getInstance().setLoginPhone(loginPhone);
    }

    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
