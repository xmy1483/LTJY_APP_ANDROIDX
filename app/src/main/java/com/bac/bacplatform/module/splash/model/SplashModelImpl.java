package com.bac.bacplatform.module.splash.model;

import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;

import com.bac.commonlib.domain.BacHttpBean;
import com.bac.bacplatform.module.splash.contract.SplashContract;
import com.bac.bacplatform.repository.Repository;

import rx.Observable;

/**
 * Created by Wjz on 2017/04/12
 * 从 Repository 获取数据，其他逻辑交给 Repository ，只是一层封装，可抽取父类
 */
public class SplashModelImpl implements SplashContract.Model {

    @Override
    public Observable<String> getData(BacHttpBean bean, boolean refreshData) {
        return Repository.getInstance().getData(bean, refreshData);
    }

    @Override
    public Observable<String> getData(BacHttpBean bean, boolean refreshData, AppCompatActivity activity) {
        return Repository.getInstance().getData(bean, refreshData, activity);
    }

    @Override
    public Observable<String> getData(BacHttpBean bean, boolean refreshData, AppCompatActivity activity, DialogInterface.OnClickListener positive) {
        return Repository.getInstance().getData(bean, refreshData, activity, positive);
    }

    @Override
    public Observable<String> getData(BacHttpBean bean, boolean refreshData, AppCompatActivity activity, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative, DialogInterface.OnCancelListener cancel) {
        return Repository.getInstance().getData(bean, refreshData, activity, positive, negative, cancel);
    }
}