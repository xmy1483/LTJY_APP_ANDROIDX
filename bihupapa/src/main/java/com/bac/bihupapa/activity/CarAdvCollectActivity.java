package com.bac.bihupapa.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bihupapa.R;
import com.bac.commonlib.param.CommonParam;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding.view.RxView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.adv
 * 创建人：Wjz
 * 创建时间：2017/4/13
 * 类描述：
 */

public class CarAdvCollectActivity extends AutomaticRxAppCompatActivity {

    private Intent intent;
    private TextView tv01;
    private TextView tv02;
    private ImageView iv01;
    private ImageView iv02;
    private FrameLayout fl01;
    private FrameLayout fl02;
    private Button btn;
    private Intent mIntent;
    private LinearLayout ll01;
    private LinearLayout ll02;
    private Map<String, Object> map1;
    private Map<String, Object> map2;
    private static final int CARTOPCAMERACODE = 6666;
    private static final int CARBOTTOMCAMERACODE = 7777;
    private String title;
    private static String id1;

    public static String getId1() {
        return id1;
    }

    public static String getId2() {
        return id2;
    }

    private static String id2;

    private boolean is_succ;
    private String image_id;
    private static String path1;
    private static String path2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bhpp_car_adv_collect_activity);
        intent = getIntent();

        String data = intent.getStringExtra("data");

        Map<String, Object> map = JSON.parseObject(data, new TypeReference<Map<String, Object>>() {
        }.getType());

        String images = map.get("images") + "";
        // title
        title = "拍照上传";
        UIUtil.initToolBar(this, title, null);
        List<Map<String, Object>> list = JSON.parseObject(images, new TypeReference<List<Map<String, Object>>>() {
        }.getType());
        map1 = list.get(0);
        map2 = list.get(1);

        tv01 = findViewById(R.id.tv_01);
        tv02 = findViewById(R.id.tv_02);
        iv01 = findViewById(R.id.iv_01);
        iv02 = findViewById(R.id.iv_02);
        fl01 = findViewById(R.id.fl_01);
        fl02 = findViewById(R.id.fl_02);
        ll01 = findViewById(R.id.ll_01);
        ll02 = findViewById(R.id.ll_02);
        btn = findViewById(R.id.btn_pic_upload);

        tv01.setText(map1.get("image_name") + "");
        tv02.setText(map2.get("image_name") + "");

        btn.setText("确   认");

        initEvent();
    }

    private void initEvent() {
        ll01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTop = new Intent(CarAdvCollectActivity.this, CaptureActivity.class);
                intentTop.putExtra("title", title);
                intentTop.putExtra("label1", map1.get("image_name") + "");
                intentTop.putExtra("label2", "将驾驶侧车身放入方框内");
                intentTop.putExtra("alert", 1);
                intentTop.putExtra("picName", CARTOPCAMERACODE + "");
                intentTop.putExtra("tag", Integer.parseInt(map1.get("image_type") + ""));
                startActivityForResult(intentTop, CARTOPCAMERACODE);
            }
        });
        ll02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTop = new Intent(CarAdvCollectActivity.this, CaptureActivity.class);
                intentTop.putExtra("title", title);
                intentTop.putExtra("label1", map2.get("image_name") + "");
                intentTop.putExtra("label2", "将驾驶侧车身放入方框内");
                intentTop.putExtra("alert", 2);
                intentTop.putExtra("picName", CARBOTTOMCAMERACODE + "");
                intentTop.putExtra("tag", Integer.parseInt(map2.get("image_type") + ""));
                startActivityForResult(intentTop, CARBOTTOMCAMERACODE);
            }
        });
        RxView.clicks(btn)
                .throttleFirst(1, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        // 上传图片
                        doNetUploadPic();

                    }

                });
    }

    private void doNetUploadPic() {
        if (!path1.equals(null) && !path1.equals("") && !path2.equals(null) && !path2.equals("")) {
            doload(path1);
            id1 = image_id;
            doload(path2);
            id2 = image_id;

            // okHttp 3  文件上传
            // 弹框显示照片已上传审核中
        }
    }

    public void doload(String path) {
        String filePath = path;
        File file = new File(filePath);
        String name = file.getName();

        String substring = name.substring(name.lastIndexOf(".") + 1);

        UUID uuid = UUID.randomUUID();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("login_phone", CommonParam.getInstance().getLoginPhone());
        hashMap.put("image_type", 3);
        hashMap.put("image_name", name);
        hashMap.put("image_mode", substring);
        hashMap.put("UUID", uuid.toString());


        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);
        MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");
        //追加参数
        builder.addFormDataPart("JSON", String.valueOf(JSON.toJSON(hashMap)));

        builder.addFormDataPart("FILE", name, RequestBody.create(MEDIA_TYPE_STREAM, file));
        RequestBody body = builder.build();
        final Request request = new Request.Builder()
                //上传图片地址1
                .url("https://app5.bac365.com:10443/app.pay/UploadFileServer")
//                .url("http://121.43.172.16:88/app.pay/UploadFileServer")
                .post(body)
                .build();
        CommonParam.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                CarAdvCollectActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CarAdvCollectActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String string = response.body().string();
                System.out.println(string);
//                Gson gson = new Gson();
//                photoBean = new PhotoBean();
//                photoBean  = gson.fromJson(string, PhotoBean.class);
                JSONObject json = null;
                try {
                    json = new JSONObject(string);
                    is_succ = json.getBoolean("is_succ");
                    image_id = json.getString("image_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CarAdvCollectActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (is_succ) {
                            Toast.makeText(CarAdvCollectActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            setResult(6789, new Intent().putExtra("UUID", id1 + "," + id2));
                            finish();

                        } else {
                            Toast.makeText(CarAdvCollectActivity.this, "上传失败，请重新上传", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }
                });
            }

        });
    }

    private HashMap<String, Object> mTopHMCamera;
    private HashMap<String, Object> mBottomHMCamera;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED)
            return;
        if (data == null)
            return;

        if (requestCode == CARTOPCAMERACODE) {
            // cleanImageView(ivInsuranceUploadTop);
            mTopHMCamera = new HashMap<>();
            saveCameraPic(data, mTopHMCamera, iv01, CARTOPCAMERACODE);

        } else if (requestCode == CARBOTTOMCAMERACODE) {
            // cleanImageView(ivInsuranceUploadBottom);
            mBottomHMCamera = new HashMap<>();
            saveCameraPic(data, mBottomHMCamera, iv02, CARBOTTOMCAMERACODE);
        }
    }

    private void saveCameraPic(Intent data, HashMap<String, Object> hm, ImageView iv, int flag) {
        switch (flag) {
            case 6666:
                path1 = data.getStringExtra("path");
                break;
            case 7777:
                path2 = data.getStringExtra("path");
                break;
        }
        String path = data.getStringExtra("path");

        //存储图片
        hm.put("path", path);
        Glide.with(this).load(path)
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(iv);
        //iv.setImageBitmap(BitmapFactory.decodeFile(path));
    }


}
