package com.wjz.weexlib.weex.delegate.impl;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.wjz.weexlib.weex.delegate.BaseWeexDelegateCallback;
import com.wjz.weexlib.weex.delegate.FragmentWeexDelegate;

/**
 * Created by wujiazhen on 2017/6/14.
 */

public class FragmentWeexDelegateImpl implements FragmentWeexDelegate {

    private BaseWeexDelegateCallback baseWeexDelegateCallback;

    private WeexInternalDelegate weexInternalDelegate;

    public FragmentWeexDelegateImpl(BaseWeexDelegateCallback baseWeexDelegateCallback) {
        this.baseWeexDelegateCallback = baseWeexDelegateCallback;
    }

    private WeexInternalDelegate getWeexInternalDelegate(){
        if (weexInternalDelegate==null) {
            weexInternalDelegate = new WeexInternalDelegate(baseWeexDelegateCallback);
        }
        return weexInternalDelegate;
    }

    @Override
    public void onCreate(Bundle saved) {
        getWeexInternalDelegate().destroyInstance();
        getWeexInternalDelegate().createInstance();
        // hook activity lifecycle
        getWeexInternalDelegate().getInstance().onActivityCreate();
    }

    @Override
    public void onDestroy() {

        getWeexInternalDelegate().getInstance().onActivityDestroy();
        getWeexInternalDelegate().destroyInstance();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        getWeexInternalDelegate().attachView();

    }

    @Override
    public void onDestroyView() {

        getWeexInternalDelegate().detachView();
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
        getWeexInternalDelegate().getInstance().onActivityStop();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    }

    @Override
    public void onAttach(Activity activity) {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public boolean onBackPressed() {
        return getWeexInternalDelegate().getInstance().onActivityBack();
    }
}
