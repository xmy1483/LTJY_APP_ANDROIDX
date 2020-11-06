package com.bac.rxbaclib.rx.dialog;

import android.app.Activity;
import android.content.Context;

import rx.Observable;
import rx.functions.Action0;

/**
 * Created by Wjz on 2017/5/9.
 */

public class RxDialog<T> {


    private boolean canCancel;
    private String mMessage;

    public RxDialog() {
    }

    public RxDialog(String message) {
        this(message, true);
    }

    public RxDialog(boolean canCancel) {
        this(null, canCancel);
    }

    public RxDialog(String mMessage, boolean canCancel) {
        this.canCancel = canCancel;
        this.mMessage = mMessage;
    }

    /**
     * dialog 显示/隐藏封装
     *
     * @param context
     * @return
     */
    public Observable.Transformer<T, T> rxDialog(final Context context) {

        final DialogHelper dialogHelper = new DialogHelper(context);
        /*
        * 提供一个Observable它会返回给另一个Observable，这和内联一系列操作符有着同等功效。
        * */
        return new Observable.Transformer<T, T>() {
            public Activity activity;

            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
// 网络请求提示框

                              //  dialogHelper.showProgressDialog(mMessage == null ? context.getString(R.string.loading) : mMessage, canCancel);
                            }
                        })
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                              //  dialogHelper.dismissProgressDialog();
                            }
                        });
            }
        };
    }
}
