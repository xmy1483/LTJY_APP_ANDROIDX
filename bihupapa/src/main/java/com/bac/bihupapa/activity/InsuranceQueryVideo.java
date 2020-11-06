package com.bac.bihupapa.activity;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bihupapa.BuildConfig;
import com.bac.bihupapa.R;
import com.bac.bihupapa.adapter.VideoAdapter;
import com.bac.bihupapa.bean.InsuranceHomeBean;
import com.bac.bihupapa.util.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.http.HttpHelperLib;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.commonlib.utils.str.StringUtil;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.bac.bihupapa.conf.Constants.APP.DEBUG;
import static com.bac.bihupapa.conf.Constants.CommonProperty._0;
import static com.bac.bihupapa.conf.Constants.URL.UPLOAD_FILE;
import static com.bac.bihupapa.util.UIUtils.callbackBitmap_3;
import static com.bac.commonlib.http.HttpManager.okHttpInit;
import static com.baidu.location.g.a.i;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.insurance
 * 创建人：Wjz
 * 创建时间：2017/1/14
 * 类描述：
 */

public class InsuranceQueryVideo extends AutomaticRxAppCompatActivity {

    private RecyclerView rvInsuranceVideo;
    private InsuranceHomeBean mBean;
    private VideoAdapter mVideoAdapter;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView2();
        initData();
        initEvent();
    }

    private void initView2() {
        setContentView(R.layout.insurance_video_activity);
        ((TextView)findViewById(R.id.tv_center)).setText("影像补齐");

        rvInsuranceVideo = findViewById(R.id.rv_insurance_video);

        mBtn = findViewById(R.id.btn_insurance);
    }

    private void initData() {
        mBean = getIntent().getParcelableExtra("bean");

        mVideoAdapter = new VideoAdapter(R.layout.insurance_video_item, null);
        rvInsuranceVideo.setLayoutManager(new LinearLayoutManager(this));
        rvInsuranceVideo.setAdapter(mVideoAdapter);
        doNetGetVideoList();

    }

    private void doNetGetVideoList() {



        HttpHelperLib.getInstance().net(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_UPLOAD")
                .put("login_phone", StringUtil.decode(getApplication(),"bac_l",getApplication().getString(R.string.seed_num) + Integer.valueOf(BuildConfig.XX) + BuildConfig.appKeySeed + BuildConfig.appKeySeed2))
                .put("order_id", mBean.getOrder_id()),null,InsuranceQueryVideo.class,false,null)
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<Map<String, Object>>>() {
                    @Override
                    public List<Map<String, Object>> call(String s) {
                        List<Map<String, Object>>  mapList = parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                        }.getType());
                        mapList = JSON.parseObject(String.valueOf(mapList.get(0).get("upload_images")),
                                new TypeReference<List<Map<String, Object>>>() {
                                }.getType());
                        return JSON.parseObject(String.valueOf(mapList.get(0).get("image_infos")),
                                new TypeReference<List<Map<String, Object>>>() {
                                }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {
                        if (mapList != null) {
                            mVideoAdapter.addData(mapList);
                        }
                    }
                });
    }

    private void initEvent() {
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNetAuditinginsure();
            }
        });

        rvInsuranceVideo.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Map<String, Object> map = (Map<String, Object>) baseQuickAdapter.getData().get(position);
                int i1 = view.getId();
                if (i1 == R.id.btn) {
                    FrameLayout parent = (FrameLayout) view.getParent();

                    doNetUploadImage(map, parent);


                } else if (i1 == R.id.iv) {//打开相册
                    Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                    albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                    startActivityForResult(albumIntent, i);


                }
            }
        });
    }

    private void doNetAuditinginsure() {
        List<Map<String, Object>> mapList = mVideoAdapter.getData();
        for (Map<String, Object> map : mapList) {
            Object insurance_upload = map.get("insurance_upload");
            if (insurance_upload == null || !(Boolean) insurance_upload) {
                Toast.makeText(this, "请上传需要的影像资料", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (getIntent().getIntExtra("status", 0) != 14) {
            startActivity(new Intent(InsuranceQueryVideo.this, InsuranceUploadAddress.class).putExtra("bean", mBean));
        } else {
            doNet();
        }

    }

    private void doNet() {

        HttpHelperLib.getInstance().net( new BacHttpBean()
                .setActionType(0)
                .setMethodName("AUDITINGINSURE")
                .put("login_phone", com.bac.commonlib.utils.str.StringUtil.decode(getBaseContext(),"bac_l",getBaseContext().getString(R.string.seed_num) + Integer.valueOf(BuildConfig.XX) + BuildConfig.appKeySeed + BuildConfig.appKeySeed2))
                .put("order_id", mBean.getOrder_id()),new AlertDialog.Builder(this).create(),null,true,null)
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {
                        Map<String, Object> map = mapList.get(_0);
                        if (map != null) {
                            Integer code = (Integer) map.get("code");
                            if (code == -2) {
                                Toast.makeText(InsuranceQueryVideo.this, String.valueOf(map.get("msg")), Toast.LENGTH_SHORT).show();
                            } else if (code == 0) {
                               UIUtils.startActivityInAnim(InsuranceQueryVideo.this,new Intent(InsuranceQueryVideo.this, InsuranceUploadAddress.class).putExtra("bean", mBean));
                            }
                        }
                    }
                });

    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private void doNetUploadImage(final Map<String, Object> mHashMap, FrameLayout parent) {

        Object path = mHashMap.get("path");
        if (path == null) {
            return;
        }
        final File file = new File(path.toString());
        final String name = file.getName();

        String substring = name.substring(name.lastIndexOf(".") + 1);

        final UUID uuid = UUID.randomUUID();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("image_type", mHashMap.get("image_type"));
        hashMap.put("image_name", name);
        hashMap.put("image_mode", substring);
        hashMap.put("is_pay_pre", mHashMap.get("is_pay_pre"));
        hashMap.put("order_id", mBean.getOrder_id());
        hashMap.put("login_phone", Long.parseLong(StringUtil.decode(getBaseContext(),"bac_l",getBaseContext().getString(R.string.seed_num) + Integer.valueOf(BuildConfig.XX) + BuildConfig.appKeySeed + BuildConfig.appKeySeed2)));
        final String s = String.valueOf(JSON.toJSON(hashMap));


        Button button      = null;
        ProgressBar progressBar = null;
        int         childCount  = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);

            if (view instanceof Button) {
                button = (Button) view;
            } else if (view instanceof ProgressBar) {
                progressBar = (ProgressBar) view;
            }
        }

        final Button finalButton      = button;
        final ProgressBar finalProgressBar = progressBar;
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                finalButton.setVisibility(View.INVISIBLE);
                finalProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                finalButton.setVisibility(View.VISIBLE);
                finalProgressBar.setVisibility(View.INVISIBLE);
                if ("success".equals(string)) {
                    finalButton.setText("上传成功");
                    mHashMap.put("insurance_upload", true);
                } else {
                    finalButton.setText("上传失败请重新上传");
                    mHashMap.put("insurance_upload", false);
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                String string = null;
                try {

                    // okHttp 3  文件上传
                    MultipartBody.Builder builder = new MultipartBody.Builder();
                    //设置类型
                    builder.setType(MultipartBody.FORM);
                    MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");
                    //追加参数
                    builder.addFormDataPart("JSON", s);
                    builder.addFormDataPart("UUID", uuid.toString());

                    builder.addFormDataPart("FILE", name, RequestBody.create(MEDIA_TYPE_STREAM, file));

                    RequestBody body = builder.build();
                    final Request request = new Request.Builder()
                            .url(UPLOAD_FILE)
                            .post(body)
                            .build();
                    OkHttpClient sOkHttpClient = okHttpInit(InsuranceQueryVideo.this,DEBUG);
                    Response execute = sOkHttpClient.newCall(request)
                            .execute();
                    string = execute.body().string();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return string;
            }
        }.execute();

    }



    private static final int NONE = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == NONE)
            return;
        if (data == null)
            return;

        List<Map<String, Object>> mapList = mVideoAdapter.getData();
        Map<String, Object> map     = mapList.get(requestCode);
        /* 将Bitmap设定到ImageView */
        String path = callbackBitmap_3(InsuranceQueryVideo.this, data.getData());
        if (path != null) {
            map.put("path", path);
        }
        mVideoAdapter.notifyItemChanged(requestCode);

    }


}
