package com.bac.bacplatform.old.module.wash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.BuildConfig;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.bac.bacplatform.utils.ui.UIUtils.startActivityInAnim;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.wash
 * 创建人：Wjz
 * 创建时间：2017/3/24
 * 类描述：
 */

public class WashCarActivity extends SuperActivity {

    private ImageView iv;
    private Button btn;
    private TextView tvBottom;
    private HashMap<String, String> mHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wash_car_activity);

        initToolBar("洗车");

        iv = (ImageView) findViewById(R.id.iv);
        btn = (Button) findViewById(R.id.btn);
        tvBottom = (TextView) findViewById(R.id.tv_bottom);

        initEvent();

        String clean_car = getDefaultSharedPreferences(this).getString("QUERY_PAGE_INFO", "");

        List<HashMap<String, String>> mapList = JSON.parseObject(clean_car, new TypeReference<List<HashMap<String, String>>>() {
        }.getType());

        for (HashMap<String, String> hashMap : mapList) {
            if ("CLEAN_CAR".equals(hashMap.get("page_name"))) {
                String page_json = hashMap.get("page_json");
                mHashMap = JSON.parseObject(page_json, new TypeReference<HashMap<String, String>>() {
                }.getType());
                tvBottom.setText(mHashMap.get("remark"));

                Glide.with(this)
                        .load(mHashMap.get("top_image"))
                        .placeholder(R.mipmap.wash_car_loading)
                        .error(R.mipmap.wash_car_loading)
                        .into(iv);

            }
        }
    }

    private void initEvent() {
        RxView.clicks(btn)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        RECORD_CLICK();
                    }
                });
    }

    private void RECORD_CLICK() {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("RECORD_CLICK")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("version", BuildConfig.VERSION_NAME)
                .put("click_id", 1)
                .put("click_name", "android_clean_car"))
                .compose(this.<String>bindToLifecycle())
                .compose(new RxDialog<String>().rxDialog(WashCarActivity.this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> call(String s) {
                        return ((List<Map<String, Object>>)
                                JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                                }.getType()))
                                .get(0);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Action1<Map<String, Object>>() {
                    @Override
                    public void call(Map<String, Object> stringObjectMap) {
                        Intent intentToAgree = new Intent(WashCarActivity.this,
                                WebAdvActivity.class);
                        intentToAgree.putExtra("title", "10元洗车");
                        intentToAgree.putExtra("urltype", "wash_car");
                        intentToAgree.putExtra("ads_url", mHashMap.get("clean_car_url"));
                        startActivityInAnim(WashCarActivity.this, intentToAgree);
                    }
                });
    }
}
