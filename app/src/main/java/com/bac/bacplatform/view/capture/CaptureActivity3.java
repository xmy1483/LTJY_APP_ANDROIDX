package com.bac.bacplatform.view.capture;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.view.View;
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
import com.bac.commonlib.capture.ClipActivity;
import com.bac.commonlib.utils.glideTransform.GlideRoundTransform;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding.view.RxView;

import java.io.File;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.bac.bacplatform.conf.Constants.SECOND_2;

/**
 * 调用系统相机
 * 保存到 data/data/xxx
 * 保存文件名当前 RequestCode
 */
public class CaptureActivity3 extends SuperActivity {

    private static final int CAMERA = 2000;
    private static final int CLIP = 3000;

    private RelativeLayout activityMain;

    private RatioLayout view;
    private TextView tv01;
    private TextView tv02;
    private TextView tv03;
    private Button btn;
    private Intent mIntent;
    private String path;
    private ImageView iv01;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_activity_2);

        mIntent = getIntent();
        initToolBar(mIntent.getStringExtra("title"));

        activityMain = (RelativeLayout) findViewById(R.id.activity_main);

        view = (RatioLayout) findViewById(R.id.view);
        iv01 = (ImageView) findViewById(R.id.iv_01);
        tv01 = (TextView) findViewById(R.id.tv_01);
        tv02 = (TextView) findViewById(R.id.tv_02);
        tv03 = (TextView) findViewById(R.id.tv_03);
        btn = (Button) findViewById(R.id.btn);
        btn.setText("确   认");
        btn.setEnabled(false);

        RxView.clicks(btn)
                .throttleFirst(SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        setResult(RESULT_OK, getIntent().putExtra("path", path));

                        activity.onBackPressed();
                    }
                });

        tv01.setText(mIntent.getStringExtra("label1"));
        tv02.setText(mIntent.getStringExtra("label2"));

        url = mIntent.getStringExtra("picName");
        final int alert = mIntent.getIntExtra("alert", -1);
        if (alert > 0) {
            // 设置图片

            if (url.equals("6666")) {
                iv01.setImageResource(R.mipmap.bhpp_car_adv_upload_1);
            } else {

                iv01.setImageResource(R.mipmap.bhpp_car_adv_upload_2);
            }


            tv03.setVisibility(View.VISIBLE);
        } else {
            // 设置图片,办卡
            if (url.equals("8888")) {
                iv01.setImageResource(R.mipmap.id_first_icon);
            } else {

                iv01.setImageResource(R.mipmap.id_second_icon);
            }

            tv03.setVisibility(View.GONE);
        }
        tv03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UIUtils.startActivityInAnim(CaptureActivity3.this, new Intent(CaptureActivity3.this, CarAdvCaptureSample.class)
                        .putExtra("label1", mIntent.getStringExtra("label1"))
                        .putExtra("label2", mIntent.getStringExtra("label2"))
                        .putExtra("alert", alert));

            }
        });

        RxView.clicks(view)
                .throttleFirst(SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        try {
                            File externalFilesDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                            if (!externalFilesDir.exists()) {
                                externalFilesDir.mkdirs();
                            }

                            File f = new File(externalFilesDir, url + ".png");
                            path = f.getAbsolutePath();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//启动摄像头

                            // 7.0
                            Uri data;
                            // 判断版本大于等于7.0
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {



                                data = FileProvider.getUriForFile(activity,  CaptureActivity3.this.getPackageName()+".fileprovider", f);
                            } else {
                                data = Uri.fromFile(f);
                            }

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, //如果指定了目标uri，data就没有数据
                                    data);
                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                            startActivityForResult(intent, CAMERA);
                        } catch (Exception e) {
                            Toast.makeText(activity, "启动摄像头失败，请联系客服", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAMERA:

                if (resultCode == RESULT_OK) {

                    // 启动裁剪
                    Intent intent = new Intent(this, ClipActivity.class);
                    startActivityForResult(intent.putExtra("url", path), CLIP);

                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(activity, "取消拍摄", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "拍摄失败", Toast.LENGTH_SHORT).show();
                }

                break;
            case CLIP:

                if (resultCode == RESULT_OK) {

                    Glide.with(activity)
                            .load(path)
                            .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .transform(new GlideRoundTransform(activity, 15))
                            .into(iv01);
                    btn.setEnabled(true);
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(activity, "裁剪图片取消，请重新裁剪图片", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "裁剪图片失败", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
