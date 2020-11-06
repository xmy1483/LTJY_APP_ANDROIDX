package com.bac.bacplatform.tst;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bac.bacplatform.R;
import com.bac.bacplatform.view.RatioLayout;
import com.bac.commonlib.capture.ClipActivity;
import com.bac.rxbaclib.rx.permission.RxPermissionImpl;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;

import java.io.File;

import rx.functions.Action1;

/**
 * Created by wujiazhen on 2017/6/26.
 */

public class _7FileProviderActivity extends AppCompatActivity {
    private static final String TAG = _7FileProviderActivity.class.getSimpleName();

    private ImageView iv;
    private RatioLayout ral;
    private String picFileFullName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_acitivity);
        ral = (RatioLayout) findViewById(R.id.ral_01);
        iv = (ImageView) findViewById(R.id.iv);

        RxView.clicks(ral)
                .compose(new RxPermissionImpl(_7FileProviderActivity.this)
                        .ensure(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {

                            takePic();
                        }
                    }
                });
    }

    private void takePic() {
        // 开启系统摄像头

        // 进入公共文件夹
        String state = Environment.getExternalStorageState();//SD卡的状态

        if (state.equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//启动摄像头
            File outDir = Environment
                    .getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES);//SD路径 Pictures的路径

            if (!outDir.exists()) {
                outDir.mkdirs();//创建文件夹 Pictures
            }

            String s = outDir.getAbsolutePath();

            File outFile = new File(s, System.currentTimeMillis() + ".png");//保存图片
            picFileFullName = outFile.getAbsolutePath();//拍照的绝对路径


            Uri uriForFile = FileProvider.getUriForFile(_7FileProviderActivity.this,
                    _7FileProviderActivity.this.getPackageName()+".fileprovider",
                    outFile);
            System.out.println(uriForFile.getPath());
            System.out.println(uriForFile);
            System.out.println(outFile.getAbsolutePath());

            intent.putExtra(MediaStore.EXTRA_OUTPUT, //如果指定了目标uri，data就没有数据
                    uriForFile
                    //Uri.fromFile(outFile)
            );
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, 1000);

            // 通知媒体库更新单个文件状态
            Uri fileUri = Uri.fromFile(outFile);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));

        } else {
            Log.e(TAG, "请确认已经插入SD卡");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        System.out.println(requestCode);

        if (requestCode == 1000) {

            if (resultCode == RESULT_OK) {

                // 启动裁剪 功能
                Intent intent = new Intent(this, ClipActivity.class);
                startActivityForResult(intent.putExtra("url", picFileFullName), 3000);



            } else if (resultCode == RESULT_CANCELED) {

                // 取消
            } else {

                // 退出
            }

        } else if (requestCode == 3000) {

            Glide.with(_7FileProviderActivity.this)
                        .load(picFileFullName)
                        .into(iv);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
