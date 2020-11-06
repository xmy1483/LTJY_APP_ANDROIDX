package com.wjz.weexlib.weex.delegate;

import android.view.ViewGroup;

/**
 * Created by wujiazhen on 2017/6/13.
 */

public interface IWeexView {

    void showLoading();
    void hideLoading();
    void showError(String errorCode,String errorMsg);
    ViewGroup getContainer();

}
