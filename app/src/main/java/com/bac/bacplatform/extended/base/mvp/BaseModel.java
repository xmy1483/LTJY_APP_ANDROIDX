package com.bac.bacplatform.extended.base.mvp;

import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;

import com.bac.commonlib.domain.BacHttpBean;

import rx.Observable;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.base
 * 创建人：Wjz
 * 创建时间：2017/4/12
 * 类描述：
 * model的接口的抽取
 * 被对应的model接口继承
 */

public interface BaseModel {
    Observable<String> getData(BacHttpBean bean, boolean refreshData);

    // 请求数据
    Observable<String> getData(BacHttpBean bean, boolean refreshData, AppCompatActivity activity);

    Observable<String> getData(BacHttpBean bean, boolean refreshData, AppCompatActivity activity,
                               DialogInterface.OnClickListener positive);

    Observable<String> getData(BacHttpBean bean, boolean refreshData, AppCompatActivity activity,
                               DialogInterface.OnClickListener positive,
                               DialogInterface.OnClickListener negative,
                               DialogInterface.OnCancelListener cancel);
}
