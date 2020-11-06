package com.bac.bacplatform.old.module.bihupapa;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.bihupapa.domain.CarAdvInfoBean;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.view.CanClearEditText;
import com.bac.bacplatform.view.RatioLayout;
import com.bac.bacplatform.view.capture.CaptureActivity3;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.commonlib.utils.str.AllCapTransformationMethod;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.permission.Permission;
import com.bac.rxbaclib.rx.permission.RxPermissionImpl;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.conf.Constants.URL.UPLOAD_FILE;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.adv
 * 创建人：Wjz
 * 创建时间：2017/4/13
 * 类描述：
 */

public class CarAdvCollectActivity extends SuperActivity implements View.OnClickListener {
    private static final int CARTOPCAMERACODE = 6666;
    private static final int CARBOTTOMCAMERACODE = 7777;

    private ImageView ivInsuranceUploadTop;
    private ImageView iv01;
    private ImageView ivInsuranceUploadBottom;
    private ImageView iv02;
    private Button btnPicUpload;
    private ProgressBar mPb01;
    private ProgressBar mPb02;
    private RatioLayout mRal01;
    private RatioLayout mRal02;
    private FrameLayout mFl01;
    private FrameLayout mFl02;
    private HashMap<String, Object> mTopHMCamera;
    private HashMap<String, Object> mBottomHMCamera;
    private boolean topOk;
    private boolean bottomOk;

    private AlertDialog mShowDialog;
    private CanClearEditText mCcet01;
    private CarAdvInfoBean mCarAdvInfoBean;
    private List<CarAdvInfoBean.ImageInfosBean> mImage_infos;
    private String mCarLicenseNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_adv_collect_activity);

        initToolBar("确认车身广告");

        ivInsuranceUploadTop = (ImageView) findViewById(R.id.iv_insurance_upload_top);
        iv01 = (ImageView) findViewById(R.id.iv_01);
        ivInsuranceUploadBottom = (ImageView) findViewById(R.id.iv_insurance_upload_bottom);
        iv02 = (ImageView) findViewById(R.id.iv_02);
        btnPicUpload = (Button) findViewById(R.id.btn_pic_upload);

        mCcet01 = (CanClearEditText) findViewById(R.id.ccet_01);
        mCcet01.setTransformationMethod(new AllCapTransformationMethod(true));
        mRal01 = (RatioLayout) findViewById(R.id.ral_01);
        mRal02 = (RatioLayout) findViewById(R.id.ral_02);
        mPb01 = (ProgressBar) findViewById(R.id.pb_01);
        mPb02 = (ProgressBar) findViewById(R.id.pb_02);

        mFl01 = (FrameLayout) findViewById(R.id.fl_01);
        mFl02 = (FrameLayout) findViewById(R.id.fl_02);

        ivInsuranceUploadTop.setImageResource(R.mipmap.bhpp_car_adv_upload_1);
        ivInsuranceUploadBottom.setImageResource(R.mipmap.bhpp_car_adv_upload_2);

        btnPicUpload.setText("确   认");

        mCarAdvInfoBean = getIntent().getParcelableExtra("car_adv");
        mImage_infos = mCarAdvInfoBean.getImage_infos();

        iv01.setOnClickListener(this);
        iv02.setOnClickListener(this);
        ivInsuranceUploadTop.setOnClickListener(this);
        ivInsuranceUploadBottom.setOnClickListener(this);

        btnPicUpload.setOnClickListener(this);

        mShowDialog = new AlertDialog.Builder(CarAdvCollectActivity.this)
                .setMessage("上传失败请求重新上传...")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        uploadImg();
                    }
                }).create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteAllPicFile();
    }

    private void deleteAllPicFile() {

        if (deleteHMFile(mTopHMCamera)) {
            mTopHMCamera = null;
        }
        if (deleteHMFile(mBottomHMCamera)) {
            mBottomHMCamera = null;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_01:
                // cleanImageView(ivInsuranceUploadTop);
                //清空图片
                ivInsuranceUploadTop.setImageResource(R.mipmap.bhpp_car_adv_upload_1);
                if (deleteHMFile(mTopHMCamera)) {
                    mTopHMCamera = null;
                }
                break;
            case R.id.iv_02:
                // cleanImageView(ivInsuranceUploadBottom);
                //清空图片
                ivInsuranceUploadBottom.setImageResource(R.mipmap.bhpp_car_adv_upload_2);
                if (deleteHMFile(mBottomHMCamera)) {
                    mBottomHMCamera = null;
                }
                break;
            case R.id.iv_insurance_upload_top:

                Observable.just("")
                        .compose(new RxPermissionImpl(activity).ensureEach(Manifest.permission.CAMERA))
                        .subscribe(new Action1<Permission>() {
                            @Override
                            public void call(Permission permission) {
                                if (permission.isGranted()) {
                                    //正面
                                    Intent intentTop = new Intent(CarAdvCollectActivity.this, CaptureActivity3.class);
                                    intentTop.putExtra("title", "拍摄车身广告照片");
                                    intentTop.putExtra("label1", mImage_infos.get(0).getImage_name());
                                    intentTop.putExtra("label2", "将驾驶侧广告拍摄于方框内");
                                    intentTop.putExtra("alert", 1);
                                    intentTop.putExtra("picName", String.valueOf(CARTOPCAMERACODE));
                                    startActivityForResult(intentTop, CARTOPCAMERACODE);
                                }
                            }
                        });

                break;
            case R.id.iv_insurance_upload_bottom:

                Observable.just("")
                        .compose(new RxPermissionImpl(activity).ensureEach(Manifest.permission.CAMERA))
                        .subscribe(new Action1<Permission>() {
                            @Override
                            public void call(Permission permission) {
                                if (permission.isGranted()) {
                                    //反面
                                    Intent intentBottom = new Intent(CarAdvCollectActivity.this, CaptureActivity3.class);
                                    intentBottom.putExtra("title", "拍摄车身广告照片");
                                    intentBottom.putExtra("label1", mImage_infos.get(1).getImage_name());
                                    intentBottom.putExtra("label2", "将副驾驶侧广告拍摄于方框内");
                                    intentBottom.putExtra("alert", 2);
                                    intentBottom.putExtra("picName", String.valueOf(CARBOTTOMCAMERACODE));
                                    startActivityForResult(intentBottom, CARBOTTOMCAMERACODE);
                                }
                            }
                        });

                break;
            case R.id.btn_pic_upload:
                //上传


                // 没有 苏
                mCarLicenseNo = "苏".concat(mCcet01.getText().toString().trim().toUpperCase());

                if (!StringUtil.isLicNo(mCarLicenseNo)) {
                    Toast.makeText(this, "请输入正确的车牌号码", Toast.LENGTH_SHORT).show();
                } else if (mTopHMCamera == null) {
                    Toast.makeText(this, "请拍摄" + mImage_infos.get(0).getImage_name(), Toast.LENGTH_SHORT).show();
                } else if (mBottomHMCamera == null) {
                    Toast.makeText(this, "请拍摄" + mImage_infos.get(1).getImage_name(), Toast.LENGTH_SHORT).show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(CarAdvCollectActivity.this);
                    builder.setMessage("请确认是否已按要求拍照")
                            .setNegativeButton("修改", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    uploadImg();
                                }
                            }).show();

                }


                break;
        }
    }

    private boolean deleteHMFile(HashMap<String, Object> map) {
        if (map == null) {
            return false;
        } else {
            String path = String.valueOf(map.get("path"));
            File f = new File(path);
            if (f.exists()) {
                f.delete();
            }
            return true;
        }
    }

    private void uploadImg() {
        doNetUploadPic(mTopHMCamera, mImage_infos.get(0).getImage_type(), mFl01);
        doNetUploadPic(mBottomHMCamera, mImage_infos.get(1).getImage_type(), mFl02);
    }


    private void doNetUploadPic(HashMap<String, Object> hm, final int i, final FrameLayout frameLayout) {

        String filePath = String.valueOf(hm.get("path"));
        File file = new File(filePath);
        String name = file.getName();

        String substring = name.substring(name.lastIndexOf(".") + 1);

        UUID uuid = UUID.randomUUID();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("order_id", mCarAdvInfoBean.getOrder_id());
        hashMap.put("login_phone", BacApplication.getLoginPhone());
        hashMap.put("image_type", i);
        hashMap.put("image_name", name);
        hashMap.put("image_mode", substring);
        hashMap.put("UUID", uuid.toString());
        frameLayout.setVisibility(View.VISIBLE);
        frameLayout.setClickable(true);


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
      /*  builder.addFormDataPart("FILE", name, new CountingRequestBody(RequestBody.create(MEDIA_TYPE_STREAM, file), new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(long bytesWritten, long contentLength) {
                System.out.println("bytesWritten:" + bytesWritten);
                System.out.println("contentLength:" + contentLength);
                if (bytesWritten==contentLength) {
                    System.out.println("doNetUploadPic：相等");
                }
            }
        }));
        */


     /*  builder.addFormDataPart("FILE",name, new ProgressRequestBody(MEDIA_TYPE_STREAM, file, new ProgressRequestBody.ReqProgressCallBack() {
           @Override
           public void onProgress(long total, long current, boolean complete) {
               System.out.println("total:" + total);
               System.out.println("current:" + current);
               if (complete) {
                   System.out.println("相等");
               }
           }
       }));*/


        //创建RequestBody
        RequestBody body = builder.build();
        final Request request = new Request.Builder()
                .url(UPLOAD_FILE)
                .post(body)
                .build();
        BacApplication.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                CarAdvCollectActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        frameLayout.setClickable(false);
                        frameLayout.setVisibility(View.GONE);
                        Toast.makeText(CarAdvCollectActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String string = response.body().string();
                CarAdvCollectActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("success".equals(string)) {
                            if (i == 201) {
                                topOk = true;
                            } else if (i == 202) {
                                bottomOk = true;
                            }
                            frameLayout.setClickable(false);
                            frameLayout.setVisibility(View.GONE);

                            if (topOk && bottomOk) {
                                //Toast.makeText(OrderUploadActivity2.this, "上传完成", Toast.LENGTH_SHORT).show();
                                //请求
                                submitAdvInfo();
                            }
                        } else {
                            new AlertDialog.Builder(CarAdvCollectActivity.this)
                                    .setNegativeButton("取消", null)
                                    .setMessage("上传失败请求重新上传...")
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            uploadImg();
                                        }
                                    }).show();
                        }
                    }
                });
            }
        });
    }

    private void submitAdvInfo() {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("SUBMIT_ADV_INFO")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("car_license_no", mCarLicenseNo)
                .put("order_id", mCarAdvInfoBean.getOrder_id())
        )
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
                            int code = (int) map.get("code");
                            if (code == 0) {

                                UIUtils.startActivityInAnimAndFinishSelf(activity, new Intent(CarAdvCollectActivity.this, QueryCarAdvInfoActivity.class)
                                        .putExtra("car_adv", mCarAdvInfoBean));

                            } else if (code == -2) {
                                Toast.makeText(CarAdvCollectActivity.this, StringUtil.isNullOrEmpty(map.get("msg")), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }


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
            saveCameraPic(data, mTopHMCamera, ivInsuranceUploadTop);

        } else if (requestCode == CARBOTTOMCAMERACODE) {
            // cleanImageView(ivInsuranceUploadBottom);
            mBottomHMCamera = new HashMap<>();
            saveCameraPic(data, mBottomHMCamera, ivInsuranceUploadBottom);
        }
    }

    private void saveCameraPic(Intent data, HashMap<String, Object> hm, ImageView iv) {
        String path = data.getStringExtra("path");
        //存储图片
        hm.put("path", path);
        Glide.with(this).load(path)
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(iv);
        //iv.setImageBitmap(BitmapFactory.decodeFile(path));
    }

}
