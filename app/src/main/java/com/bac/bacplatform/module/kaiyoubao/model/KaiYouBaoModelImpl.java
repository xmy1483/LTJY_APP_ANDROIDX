package com.bac.bacplatform.module.kaiyoubao.model;

import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;

import com.bac.commonlib.domain.BacHttpBean;
import com.bac.bacplatform.module.kaiyoubao.contract.KaiYouBaoContract;
import com.bac.bacplatform.repository.Repository;

import rx.Observable;

/**
* Created by maibenben on 2017/05/04
*/

public class KaiYouBaoModelImpl implements KaiYouBaoContract.Model{



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
        return Repository.getInstance().getData(bean, refreshData, activity,positive);
    }

    @Override
    public Observable<String> getData(BacHttpBean bean, boolean refreshData, AppCompatActivity activity, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative, DialogInterface.OnCancelListener cancel) {
        return Repository.getInstance().getData(bean, refreshData, activity,positive,negative,cancel);
    }
}