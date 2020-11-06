package com.bac.commonlib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;

import com.bac.rxbaclib.rx.permission.Permission;
import com.bac.rxbaclib.rx.permission.RxPermissionImpl;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by wujiazhen on 2017/7/17.
 *
 */

public class Util {
    public static void callPhoneUs(final Context context) {
        Observable.just("")
                .compose(new RxPermissionImpl((AppCompatActivity) context).ensureEach(android.Manifest.permission.CALL_PHONE))
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.isGranted()) {
                            String number = "4001106262";
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + number));
                            context.startActivity(intent);
                        }
                    }
                });
    }

}
