package com.bac.bihupapa.activity;


import android.Manifest;
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

import com.alibaba.fastjson.JSON;
import com.bac.bihupapa.R;
import com.bac.commonlib.capture.ClipActivity;
import com.bac.commonlib.param.CommonParam;
import com.bac.commonlib.utils.glideTransform.GlideRoundTransform;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.commonlib.view.RatioLayout;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
import com.bac.rxbaclib.rx.permission.Permission;
import com.bac.rxbaclib.rx.permission.RxPermissionImpl;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding.view.RxView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * 调用系统相机
 * 保存到 data/data/xxx
 * 保存文件名当前 RequestCode
 */
public class CaptureActivity extends AutomaticRxAppCompatActivity {

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
    private CaptureActivity activity;
    private int tag;
    private boolean is_succ = false;
    private String image_url;

    public String getImage_id() {
        return image_id;
    }

    private String image_id;

    private PhotoBean photoBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bhpp_capture_activity);
        activity = this;
        mIntent = getIntent();
        tag = mIntent.getIntExtra("tag", -1);
        UIUtil.initToolBar(this, mIntent.getStringExtra("title"), null);
        activityMain = findViewById(R.id.activity_main);

        view = findViewById(R.id.view);
        iv01 = findViewById(R.id.iv_01);
        tv01 = findViewById(R.id.tv_01);
        tv02 = findViewById(R.id.tv_02);
        tv03 = findViewById(R.id.tv_03);
        btn = findViewById(R.id.btn);
        btn.setText("确   认");
        btn.setEnabled(false);

        RxView.clicks(btn)
                .throttleFirst(1, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        if (tag == -1) {
                            Toast.makeText(activity, "未知错误，请联系客服", Toast.LENGTH_SHORT).show();
                        } else if (tag == 3 || tag ==6) {
                            // 上传图片
                            doNetUploadPic();
                        }else {
                            setResult(RESULT_OK, getIntent().putExtra("path", path));
                            activity.finish();
                        }
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


            tv03.setVisibility(View.GONE);
        }
        tv03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UIUtil.startActivityInAnim(CaptureActivity.this, new Intent(CaptureActivity.this, CarAdvCaptureSample.class)
                        .putExtra("label1", mIntent.getStringExtra("label1"))
                        .putExtra("label2", mIntent.getStringExtra("label2"))
                        .putExtra("alert", alert));

            }
        });

        RxView.clicks(view)
                .throttleFirst(1, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .compose(new RxPermissionImpl(this).ensureEach(Manifest.permission.CAMERA))
                .map(new Func1<Permission, Boolean>() {
                    @Override
                    public Boolean call(Permission permission) {
                        return permission.isGranted();
                    }
                })
                .filter(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        return aBoolean;
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aVoid) {
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


                                data = FileProvider.getUriForFile(activity, CaptureActivity.this.getPackageName() + ".fileprovider", f);
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
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
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

    private void doNetUploadPic() {
        //页面返回传给weex数据创建intent
        final Intent intent = getIntent();

        String filePath = path;
        File file = new File(filePath);
        String name = file.getName();

        String substring = name.substring(name.lastIndexOf(".") + 1);

        UUID uuid = UUID.randomUUID();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("login_phone", CommonParam.getInstance().getLoginPhone());
        hashMap.put("image_type", tag);
        hashMap.put("image_name", name);
        hashMap.put("image_mode", substring);
        hashMap.put("UUID", uuid.toString());


       /*
        // retrofit  文件上传  后台返回 success
       RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("FILE", name, photoRequestBody);

        BacApplication.getBacApi().upload(RequestBody.create(null, JSON.toJSONString(hashMap)),photo)
                .subscribeOn(RxScheduler.RxPoolScheduler())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {

                        System.out.println("s:" + s);
                    }
                });
*/


        // okHttp 3  文件上传
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);
        MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");
        //追加参数
        builder.addFormDataPart("JSON", String.valueOf(JSON.toJSON(hashMap)));

        builder.addFormDataPart("FILE", name, RequestBody.create(MEDIA_TYPE_STREAM, file));

        //9.14

//        builder.addFormDataPart("FILE", name, new CountingRequestBody(RequestBody.create(MEDIA_TYPE_STREAM, file), new CountingRequestBody.Listener() {
//            @Override
//            public void onRequestProgress(long bytesWritten, long contentLength) {
//                System.out.println("bytesWritten:" + bytesWritten);
//                System.out.println("contentLength:" + contentLength);
//                if (bytesWritten==contentLength) {
//                    System.out.println("doNetUploadPic：相等");
//                }
//            }
//        }));
//
//
//       builder.addFormDataPart("FILE",name, new ProgressRequestBody(MEDIA_TYPE_STREAM, file, new ProgressRequestBody.ReqProgressCallBack() {
//           @Override
//           public void onProgress(long total, long current, boolean complete) {
//               System.out.println("total:" + total);
//               System.out.println("current:" + current);
//               if (complete) {
//                   System.out.println("相等");
//               }
//           }
//       }));

        //9.14

        //图片上传地址
        //创建RequestBody
        RequestBody body = builder.build();
        final Request request = new Request.Builder()
                .url("https://app5.bac365.com:10443/app.pay/UploadFileServer")
                //.url("https://121.43.172.16:88/app.pay/UploadFileServer")
                .post(body)
                .build();
        CommonParam.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                CaptureActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CaptureActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String string = response.body().string();
                System.out.println(string);
//                HashMap<String, Object> hashMap = new HashMap<>();
//                HashMap<String, Object> hashMap = new HashMap<>();
//                                hashMap.put("data", result);                hashMap.put("data", result);Gson gson = new Gson();
//                photoBean = new PhotoBean();
//                photoBean  = gson.fromJson(string, PhotoBean.class);
                JSONObject json = null;
                try {
                    json = new JSONObject(string);
                    is_succ = json.getBoolean("is_succ");
                    image_id = json.getString("image_id");
                    image_url = json.getString("image_url");

                    Map map = new HashMap();
                    map.put("UUID", image_id);
                    map.put("url", image_url);
                    //转成arraylist格式返回
                    ArrayList list = new ArrayList();
                    list.add(0,image_id);
                    list.add(1,image_url);
                    if (tag == 6){
                        setResult(1,getIntent().putExtra("data",list));
                        finish();
                    }


                    // System.out.println(map1);
                 //   BhppHttpModule.setresult(map);
//                 intent.putExtra("data",map.toString());
//                    setResult(123,intent);
//                    finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                CaptureActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (is_succ) {
                            Toast.makeText(activity, "上传成功", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {

                            Toast.makeText(activity, "上传失败，请重新上传", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }
                });
            }

        });
    }


}

