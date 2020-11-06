package com.wjz.weexlib.weex.delegate.impl;

import android.os.Bundle;

import com.wjz.weexlib.weex.delegate.ActivityWeexDelegate;
import com.wjz.weexlib.weex.delegate.BaseWeexDelegateCallback;

/**
 * Created by wujiazhen on 2017/6/13.
 */

public class ActivityWeexDelegateImpl implements ActivityWeexDelegate {

    private BaseWeexDelegateCallback baseWeexDelegateCallback;

    private WeexInternalDelegate weexInternalDelegate;

    public ActivityWeexDelegateImpl(BaseWeexDelegateCallback baseWeexDelegateCallback) {
        this.baseWeexDelegateCallback = baseWeexDelegateCallback;
    }

    /**
     * 获取 WXSDKInstance
     *
     * @return
     */
    public WeexInternalDelegate getWeexInternalDelegate() {

        if (weexInternalDelegate == null) {
            weexInternalDelegate = new WeexInternalDelegate(baseWeexDelegateCallback);
        }

        return weexInternalDelegate;
    }


    // 被 Activity 调用的 生命周期方法
    @Override
    public void onCreate(Bundle bundle) {

        getWeexInternalDelegate().destroyInstance();
        getWeexInternalDelegate().createInstance();
        getWeexInternalDelegate().getInstance().onActivityCreate();

    }

    @Override
    public void onDestroy() {

        getWeexInternalDelegate().getInstance().onActivityDestroy();
        getWeexInternalDelegate().destroyInstance();
    }

    @Override
    public void onPause() {

        getWeexInternalDelegate().getInstance().onActivityPause();

    }

    @Override
    public void onResume() {

        getWeexInternalDelegate().getInstance().onActivityResume();
    }

    @Override
    public void onStart() {

        getWeexInternalDelegate().getInstance().onActivityStart();
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onContentChanged() {

        getWeexInternalDelegate().attachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public boolean onBackPressed() {
        return getWeexInternalDelegate().getInstance().onActivityBack();
    }
}
