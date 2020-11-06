package com.bac.bacplatform.module.center.model;

import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;

import com.bac.commonlib.domain.BacHttpBean;
import com.bac.bacplatform.module.center.contract.UserCenterContract;
import com.bac.bacplatform.repository.Repository;

import rx.Observable;

/**
 * Created by maibenben on 2017/05/04
 */

public class UserCenterModelImpl implements UserCenterContract.Model {

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