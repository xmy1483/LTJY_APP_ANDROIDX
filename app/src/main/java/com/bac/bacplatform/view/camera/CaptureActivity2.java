package com.bac.bacplatform.view.camera;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bac.bacplatform.R;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.bihupapa.CarAdvCaptureSample;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.view.RatioLayout;
import com.bac.commonlib.camera.CameraManager3;
import com.bac.commonlib.camera.CameraMeasureBean;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

@Deprecated
public class CaptureActivity2 extends SuperActivity implements SurfaceHolder.Callback {

    private boolean hasSurface;

    private RelativeLayout activityMain;
    private SurfaceView sv;

    private RatioLayout view;
    private TextView tv01;
    private TextView tv02;
    private TextView tv03;
    private Button btn;

    private ImageView mIv;
    private Intent mIntent;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bhpp_capture_activity);


        mIntent = getIntent();
        initToolBar(mIntent.getStringExtra("title"));

        mProgressDialog = new ProgressDialog(CaptureActivity2.this);
        mProgressDialog.setTitle("温馨提示");
        mProgressDialog.setMessage("正在处理...");

        //初始化相机管理器
        CameraManager3.init(getApplication());
        hasSurface = false;

        activityMain = (RelativeLayout) findViewById(R.id.activity_main);
        sv = (SurfaceView) findViewById(R.id.sv);

        view = (RatioLayout) findViewById(R.id.view);
        tv01 = (TextView) findViewById(R.id.tv_01);
        tv02 = (TextView) findViewById(R.id.tv_02);
        tv03 = (TextView) findViewById(R.id.tv_03);
        btn = (Button) findViewById(R.id.btn);
        btn.setText("确   认");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                mProgressDialog.show();

                CameraManager3.get().takePic(mJpegCallBack, new CameraManager3.OnAutoFocusCallback() {
                    @Override
                    public void onAutoFocusCallback(boolean b,Camera camera) {
                        if (!b) {
                            Toast.makeText(activity, "对焦失败，请重新拍摄...", Toast.LENGTH_LONG).show();
                            CaptureActivity2.this.onBackPressed();
                        }
                    }
                });
            }
        });

        mIv = (ImageView) findViewById(R.id.iv);

        tv01.setText(mIntent.getStringExtra("label1"));
        tv02.setText(mIntent.getStringExtra("label2"));


        final int alert = mIntent.getIntExtra("alert", -1);
        if (alert > 0) {
            tv03.setVisibility(View.VISIBLE);
        } else {
            tv03.setVisibility(View.GONE);
        }
        tv03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UIUtils.startActivityInAnim(CaptureActivity2.this, new Intent(CaptureActivity2.this, CarAdvCaptureSample.class)
                        .putExtra("label1", mIntent.getStringExtra("label1"))
                        .putExtra("label2", mIntent.getStringExtra("label2"))
                        .putExtra("alert", alert));

            }
        });

    }


    boolean flag = true;

    /**
     * 开启关闭闪光灯
     */
    protected void light() {
        if (flag == true) {
            flag = false;
            CameraManager3.get().openLight();
        } else {
            flag = true;
            CameraManager3.get().offLight();
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();

        SurfaceHolder surfaceHolder = sv.getHolder();

        if (hasSurface) {//默认false
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        CameraManager3.get().stopPreview();
        CameraManager3.get().closeDriver();
    }

    @Override
    protected void onDestroy() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;

        CameraManager3.get().stopPreview();
        CameraManager3.get().closeDriver();



        super.onDestroy();
    }


    /**
     * 初始化相机
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager3.get().openDriver(surfaceHolder);
            CameraManager3.get().startPreview();

        } catch (IOException ioe) {
        } catch (RuntimeException e) {
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        CameraManager3.get().startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    private Camera.PictureCallback mJpegCallBack = new Camera.PictureCallback() {

        public void onPictureTaken(final byte[] data, final Camera camera) {

            Observable.just(new CameraMeasureBean(
                    sv.getWidth(),
                    sv.getHeight(),
                    view.getWidth(),
                    view.getHeight(),
                    view.getX(),
                    view.getY()
            ))
                    .compose(CaptureActivity2.this.<CameraMeasureBean>bindUntilEvent(ActivityEvent.DESTROY))

                    .map(new Func1<CameraMeasureBean, String>() {
                        @Override
                        public String call(CameraMeasureBean measureBean) {

                            //处理图片
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            options.inPreferredConfig = Bitmap.Config.RGB_565;
                            BitmapFactory.decodeByteArray(data, 0, data.length, options);//根据Path读取资源图片
                            int reqHeight = 675;
                            options.inSampleSize = calculateInSampleSize(options, (int) (reqHeight * 1.6), reqHeight);
                            options.inJustDecodeBounds = false;
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                            Matrix m = new Matrix();
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            m.setRotate(90, width, height);
                            /*图片裁剪*/
                            float rateW = (float) height / measureBean.getmSvW();
                            float rateH = (float) width / measureBean.getmSvH();
                            float x = rateW * measureBean.getmX() + .5f;
                            float y = rateH * measureBean.getmY() + .5f;
                            float width1 = rateW * measureBean.getmWidth() + .5f;
                            float height1 = rateH * measureBean.getmHeight() + .5f;
                            Bitmap bitmap1 = null;
                            try {
                                bitmap1 = Bitmap.createBitmap(bitmap, (int) y, (int) x, (int) height1, (int) width1, m, true);
                            } catch (Exception e) {
                            }
                            return saveBitmap(CaptureActivity2.this, bitmap1, mIntent.getStringExtra("picName"));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(RxScheduler.RxPoolScheduler())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            btn.setClickable(true);
                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onNext(String s) {
                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                            if (TextUtils.isEmpty(s)) {
                                Toast.makeText(CaptureActivity2.this, "请重新拍照", Toast.LENGTH_SHORT).show();
                                camera.stopPreview();
                                camera.startPreview();
                            } else {
                                //保存地址
                                setResult(1000, getIntent().putExtra("path", s));
                                CaptureActivity2.this.onBackPressed();
                            }
                        }
                    });
        }
    };

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
    private String saveBitmap(Context context, Bitmap bitmap, String name) {

        File externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null) {
            externalFilesDir = context.getFilesDir();
        }
        File f = new File(externalFilesDir, name.concat(".png"));

        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(f);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
        }
        return f.getAbsolutePath();
    }
}
