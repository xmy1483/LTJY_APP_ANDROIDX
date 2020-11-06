package com.bac.bacplatform.utils.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import androidx.appcompat.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.weex_activities.GenericWeexActivity;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.bac.commonlib.utils.Util.callPhoneUs;

/**
 * Created by Wjz on 2017/5/15.
 */

public class Util {

    public interface _3CountDownCallback {
        void _3CountDownCallback();
    }

    public static void _3CountDown(final AutomaticBaseActivity activity, final TextView tv01, final _3CountDownCallback callback) {
        Observable.interval(1, TimeUnit.SECONDS)
                .compose(activity.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(RxScheduler.RxPoolScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .take(2)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (aLong == 1) {
                            if (callback != null) {
                                callback._3CountDownCallback();
                            }
                        }
                        tv01.setText(String.format(activity.getString(R.string.kaiyoubao_pwd_dialog), (2 - aLong) + ""));
                    }
                });
    }


    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }


    public static HashMap<String, Object> getDiskCacheDir(Context context) {

        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String cachePath = null;
            long isAvail = -1;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {

                cachePath = context.getExternalCacheDir().getAbsolutePath();

                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                isAvail = availableBlocks * blockSize;
            }
            hashMap.put("path", cachePath);
            hashMap.put("isAvail", isAvail);
        } catch (Exception e) {
        }
        return hashMap;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取设备的id
     *
     * @param context 上下文
     * @return 设备id
     */
    public static String getDeviceID(Context context) {
        TelephonyManager tManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tManager.getDeviceId();
        return deviceId;
    }

    /**
     * 获取设备宽
     *
     * @param context
     * @return
     */
    public static int getWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取设备高
     *
     * @param context
     * @return
     */
    public static int getHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

        return dm.heightPixels;
    }

    public static void callUs(final Context context) {

        Calendar calendar = Calendar.getInstance();
        //7:00-22:00  不启动加油页面
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        boolean isTime = (hour > 7) && (hour < 22);

        if (!isTime) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("电话客服")
                    .setMessage("客服电话：400-110-6262 \n客服工作时间 8:00-22:00，请您谅解！")
                    .setNegativeButton("取消",null)
                    .show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("电话客服")
                .setMessage("客服电话：400-110-6262 \n客服工作时间：8:00-22:00，请您谅解！")
                .setPositiveButton("拨打",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //拨打电话

                                callPhoneUs(context);
                            }
                        })
                .setNegativeButton("取消", null)
                .show();
    }


    public static void skipToWeex(Context context,String url){
        Intent it = new Intent(context, GenericWeexActivity.class);
        it.putExtra("URL",url);
        context.startActivity(it);
    }


}
