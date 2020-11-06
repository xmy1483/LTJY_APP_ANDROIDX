package com.wjz.weexlib.weex.delegate;

import com.taobao.weex.WXSDKInstance;

import java.util.Map;

/**
 * Created by wujiazhen on 2017/6/13.
 *
 * Activity / Fragment 实现
 */

public interface BaseWeexDelegateCallback {

    WXSDKInstance createInstance();

    WXSDKInstance getInstance();

    void setInstance(WXSDKInstance instance);


    String getIndexUrl();

    Map<String,Object> getInitData();

    String getPagerName();


    IWeexView getWeexView();
}
