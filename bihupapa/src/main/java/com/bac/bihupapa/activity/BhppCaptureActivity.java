package com.bac.bihupapa.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;

import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.google.zxing.Result;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by wujiazhen on 2017/8/29.
 */

public class BhppCaptureActivity extends CaptureActivity {

    private CompositeSubscription compositeSubscription;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getData(byte[] data, List<Result> resultList, final Camera camera) {
        final Intent intent = getIntent();
        ArrayList<String> lis = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++) {
            lis.add(resultList.get(i).getText());
        }
        intent.putStringArrayListExtra("result", lis);

        // 保存图片
        Subscription subscribe = Observable.just(data)
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<byte[], String>() {
                    @Override
                    public String call(byte[] data) {
                        // 压缩图片
                        //处理图片

                        /*BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        BitmapFactory.decodeByteArray(data, 0, data.length, options);//根据Path读取资源图片
                        int reqHeight = 675;
                        options.inSampleSize = calculateInSampleSize(options, (int) (reqHeight * 1.6), reqHeight);
                        options.inJustDecodeBounds = false;
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);*/

                        Bitmap bitmap1 = y2R(data, camera);
                        // Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        return saveBitmap(bitmap1, intent.getStringExtra("picName"));
                    }
                })
                .compose(new RxDialog<String>().rxDialog(BhppCaptureActivity.this))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        intent.putExtra("path", s);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

        compositeSubscription.add(subscribe);
    }

    private int calculateInSampleSize(BitmapFactory.Options op, int reqWidth,
                                      int reqHeight) {
        int originalWidth = op.outWidth;
        int originalHeight = op.outHeight;
        int inSampleSize = 1;
        if (originalWidth > reqWidth || originalHeight > reqHeight) {
            int halfWidth = originalWidth / 2;
            int halfHeight = originalHeight / 2;
            while ((halfWidth / inSampleSize > reqWidth)
                    && (halfHeight / inSampleSize > reqHeight)) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    @NonNull
    private String saveBitmap(Bitmap bitmap, String name) {

        File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!externalFilesDir.exists()) {
            externalFilesDir.mkdirs();
        }
        File f = new File(externalFilesDir, name + ".png");

        /*// 7.0
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(this,  this.getPackageName()+".fileprovider", f);
        } else {
            data = Uri.fromFile(f);
        }*/

        if (f.exists()) {
            f.delete();
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(f);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return f.getAbsolutePath();
    }

    public static void decodeYUV420SPrgb565(int[] rgb, byte[] yuv420sp, int width,
                                            int height) {

        final int frameSize = width * height;
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }
                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);
                if (r < 0)
                    r = 0;
                else if (r > 262143)
                    r = 262143;
                if (g < 0)
                    g = 0;
                else if (g > 262143)
                    g = 262143;
                if (b < 0)
                    b = 0;
                else if (b > 262143)
                    b = 262143;
                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
                        | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
    }


    public Bitmap y2R(byte[] data, Camera camera) {
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, previewSize.width, previewSize.height, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 50, baos);
        byte[] jdata = baos.toByteArray();
        return BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.clear();
    }
}