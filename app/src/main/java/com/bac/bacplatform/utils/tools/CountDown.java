package com.bac.bacplatform.utils.tools;

import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Wjz on 2017/5/9.
 */

public class CountDown {

    public static SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd");
    public static SimpleDateFormat format_yyyy_mm_dd_hh_mm_ss1 = new SimpleDateFormat("yyyy-MM-dd"+"  "+"hh:mm:ss");


    public static String getDayOfWeek(int i) {
        String dateStr = "";
        switch (i) {
            case 1:
                dateStr = "日";
                break;
            case 2:
                dateStr = "一";
                break;
            case 3:
                dateStr = "二";
                break;
            case 4:
                dateStr = "三";
                break;
            case 5:
                dateStr = "四";
                break;
            case 6:
                dateStr = "五";
                break;
            case 7:
                dateStr = "六";
                break;

        }
        return dateStr;
    }

    public static Observable countDown(final int time) {

        return Observable.interval(1, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .take(time)
                .subscribeOn(RxScheduler.RxPoolScheduler())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
