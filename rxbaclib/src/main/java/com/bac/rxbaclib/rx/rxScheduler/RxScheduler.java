package com.bac.rxbaclib.rx.rxScheduler;

import android.os.AsyncTask;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by wujiazhen on 2017/8/9.
 */

public class RxScheduler {
    public static Scheduler RxSerialScheduler(){
        return Schedulers.from(AsyncTask.SERIAL_EXECUTOR);
    }
    public static Scheduler RxPoolScheduler(){
        return Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
