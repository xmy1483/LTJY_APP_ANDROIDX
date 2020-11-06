package com.wjz.weexlib.weex;

import android.app.Application;

import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.adapter.DefaultWXHttpAdapter;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.wjz.weexlib.weex.adapter.ImageAdapter;
import com.wjz.weexlib.weex.adapter.WeexOkHttpAdapter;

/**
 * Created by wujiazhen on 2017/6/12.
 */

public class WeexManager {

    private DefaultWXHttpAdapter adapter;

    private WeexManager() {

        adapter = new DefaultWXHttpAdapter();
    }

    public IWXHttpAdapter getDefaultHttpAdapter() {
        return adapter;
    }

    private static final WeexManager WEEX_MANAGER = new WeexManager();

    //静态工厂方法
    public static WeexManager getInstance() {
        return WEEX_MANAGER;
    }

    public static void register(Application application) {

        // 注册自定以组建
        WXSDKEngine.initialize(application,
                new InitConfig.Builder()
                        .setHttpAdapter(new WeexOkHttpAdapter())
                        .setImgAdapter(new ImageAdapter())
                        .build()
        );

    }

    public static void register(Application application, InitConfig build) {
        WXSDKEngine.initialize(application,
                build
        );
    }

}
