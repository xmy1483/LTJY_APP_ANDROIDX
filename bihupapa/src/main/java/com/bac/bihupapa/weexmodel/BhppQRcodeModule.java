package com.bac.bihupapa.weexmodel;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;

import com.alibaba.fastjson.JSON;
import com.bac.bihupapa.activity.BhppCaptureActivity;
import com.bac.bihupapa.activity.CarAdvCollectActivity;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.rxbaclib.rx.permission.RxPermissionImpl;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.wjz.weexlib.weex.activity.WeexActivity2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by wujiazhen on 2017/8/28.
 */

public class BhppQRcodeModule extends WXModule {

    private static final int WEEX_QRCODE_RESULT_1 = 5678;
    private static final int WEEX_QRCODE_RESULT_2 = 6789;

    @JSMethod
    public void openQRcode(final Map<String, Object> map, final JSCallback jsCallback) {
        final WeexActivity2 context = (WeexActivity2) mWXSDKInstance.getContext();

        // 判断 是否开启定位
        LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        // 判断是否开启定位
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new AlertDialog.Builder(context)
                    .setTitle("温馨提示")
                    .setMessage("打开“定位服务”来允许“骆驼加油”确认您的位置")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            UIUtil.startActivityInAnim(context, intent);
                        }
                    }).show();
        } else {

            // 申请权限
            Observable.just("")
                    .compose(new RxPermissionImpl(context).ensure(Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (aBoolean) {
                                // 启动扫码
                                context.startActivityForResult(new Intent(context, BhppCaptureActivity.class).putExtra("picName", WEEX_QRCODE_RESULT_1 + ""), WEEX_QRCODE_RESULT_1);
                            }
                        }
                    });

            context.setWeexResultCallback(new WeexActivity2.WeexResultCallback() {
                @Override
                public void callback(int requestCode, int resultCode, Intent data) {
                    if (resultCode == Activity.RESULT_OK) {
                        if (requestCode == WEEX_QRCODE_RESULT_1) {
                            if (data != null) {
                                ArrayList<String> result = data.getStringArrayListExtra("result");
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("data", result);
                                jsCallback.invokeAndKeepAlive(hashMap);
                            }
                        }
                    }
                }
            });
        }

    }

    @JSMethod
    public void openCarAds(final Map<String, Object> map, final JSCallback jsCallback) {
        final WeexActivity2 context = (WeexActivity2) mWXSDKInstance.getContext();
        Intent intent = new Intent(context, CarAdvCollectActivity.class);
        intent.putExtra("data", JSON.toJSONString(map));
        context.startActivityForResult(intent, WEEX_QRCODE_RESULT_2);

        context.setWeexResultCallback(new WeexActivity2.WeexResultCallback() {
            @Override
            public void callback(int requestCode, int resultCode, Intent data) {
                if (resultCode == WEEX_QRCODE_RESULT_2) {
                    if (requestCode == WEEX_QRCODE_RESULT_2) {
                        if (data != null) {
//                                ArrayList<String> result = data.getStringArrayListExtra("result");
//                                HashMap<String, Object> hashMap = new HashMap<>();
//                                hashMap.put("data", result);
//                                jsCallback.invokeAndKeepAlive(hashMap);
                            jsCallback.invokeAndKeepAlive(data.getExtras().get("UUID").toString());
                        }
                    }
                }
            }
        });
        // 回调

    }


}
