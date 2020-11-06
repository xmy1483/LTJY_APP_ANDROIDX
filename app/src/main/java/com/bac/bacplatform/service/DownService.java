package com.bac.bacplatform.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

import static com.bac.bacplatform.utils.tools.Util.getDiskCacheDir;

/**
 * Created by Wjz on 2017/5/23.
 */

public class DownService extends IntentService {

    private static final String TAG = DownService.class.getSimpleName();
    public static final String BAC_APK = "bac.apk";
    private boolean notify;

    public static Intent newIntent(Context context) {
        return new Intent(context, DownService.class);
    }

    public DownService() {

        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        notify = intent.getBooleanExtra("notify", false);

        // 获取文件路径
        HashMap<String, Object> diskCacheDir = getDiskCacheDir(DownService.this);
        final Object path = diskCacheDir.get("path");
        final Object isAvail = diskCacheDir.get("isAvail");

        if (path != null && isAvail != null) {

            // 下载文件
            Request request = new Request.Builder().url(intent.getStringExtra("url")).build();
            BacApplication.getOkHttpClient().newCall(request)
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            // 空间
                            if ((long) isAvail - response.body().contentLength() > 0) {

                                File file = new File( path.toString(), BAC_APK);
                                file.delete();
                                Sink sink = Okio.sink(file);
                                BufferedSink buffer = Okio.buffer(sink);
                                buffer.write(response.body().bytes());
                                buffer.flush();
                                buffer.close();
                                sink.close();

                                // 启动安装器
                                Intent intentUpload = new Intent();

                                Uri data;
                                // 判断版本大于等于7.0
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    data = FileProvider.getUriForFile(DownService.this,  DownService.this.getPackageName()+".fileprovider", file);
                                    // 给目标应用一个临时授权
                                    intentUpload.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                } else {
                                    data = Uri.fromFile(file);
                                }

                                installApk(intentUpload, data);

                                // notify
                                if (notify) {

                                    NotificationManager mNotifyManager = (NotificationManager) DownService.this.getSystemService(Context.NOTIFICATION_SERVICE);
                                    //获取PendingIntent
                                    PendingIntent mainPendingIntent = PendingIntent.getActivity(DownService.this, 0, intentUpload, PendingIntent.FLAG_UPDATE_CURRENT);
                                    //创建 Notification.Builder 对象
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(DownService.this)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            //点击通知后自动清除
                                            .setAutoCancel(true)
                                            .setContentTitle("温馨提示")
                                            .setContentText("骆驼加油更新包已经下载完成，请更新安装")
                                            .setContentIntent(mainPendingIntent)
                                            //ledARGB 表示灯光颜色、 ledOnMS 亮持续时间、ledOffMS 暗的时间
                                            .setLights(0x00FF00, 1500, 1500);
                                    Notification notify = builder.build();
                                    // 呼吸灯
                                    notify.flags = Notification.FLAG_SHOW_LIGHTS;
                                    // 震动
                                    notify.defaults = Notification.DEFAULT_VIBRATE;
                                    //发送通知
                                    mNotifyManager.notify(3000, notify);

                                }else{
                                    startActivity(intentUpload);
                                }

                            }
                        }
                    });
        }

    }

    private void installApk(Intent intentUpload, Uri data) {
        intentUpload.setAction("android.intent.action.VIEW");
        intentUpload.addCategory("android.intent.category.DEFAULT");
        intentUpload.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentUpload.setDataAndType(data, "application/vnd.android.package-archive");
    }
}
