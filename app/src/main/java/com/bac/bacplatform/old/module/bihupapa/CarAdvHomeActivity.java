package com.bac.bacplatform.old.module.bihupapa;


import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.bihupapa.adapter.CarAdvHomeAdapter;
import com.bac.bacplatform.old.module.bihupapa.domain.CarAdvHomeBean;
import com.bac.bacplatform.old.module.bihupapa.domain.CarAdvInfoBean;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.bac.bacplatform.conf.Constants.CommonProperty._0;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.adv
 * 创建人：Wjz
 * 创建时间：2017/4/13
 * 类描述：
 */

public class CarAdvHomeActivity extends SuperActivity {

    private RecyclerView rv;
    private HashMap<String, String> mCarAdvMap;
    private TextView mTv01;
    private TextView mTv02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_adv_home_activity);

        initToolBar("领取酬金");

        rv = (RecyclerView) findViewById(R.id.rv);

        mTv01 = (TextView) findViewById(R.id.tv_01);
        mTv02 = (TextView) findViewById(R.id.tv_02);

        rv.setLayoutManager(new LinearLayoutManager(CarAdvHomeActivity.this));
        rv.setAdapter(new CarAdvHomeAdapter(R.layout.car_home_item, null));

        initData();

        initEvent();
    }

    private void initEvent() {
        rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                CarAdvHomeBean carAdvHomeBean = (CarAdvHomeBean) baseQuickAdapter.getData().get(position);
                getQueryAdvInfo(carAdvHomeBean.getAdv_id(), carAdvHomeBean.getAdv_title());
            }
        });
    }

    private void initData() {

        String query_page_info = PreferenceManager.getDefaultSharedPreferences(this).getString("QUERY_PAGE_INFO", "[]");

        List<HashMap<String, String>> mapList = parseObject(query_page_info, new TypeReference<List<HashMap<String, String>>>() {
        }.getType());

        for (HashMap<String, String> hashMap : mapList) {
            if ("ADV_INFO".equals(hashMap.get("page_name"))) {
                String page_json = hashMap.get("page_json");
                mCarAdvMap = parseObject(page_json, new TypeReference<HashMap<String, String>>() {
                }.getType());

                mTv01.setText(mCarAdvMap.get("total_title"));
                mTv02.setText(mCarAdvMap.get("total_info_remark"));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getQueryAdvs();
    }

    private void getQueryAdvInfo(long adv_id, String adv_title) {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_ADV_INFO")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("adv_id", adv_id)
                .put("adv_title", adv_title))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<CarAdvInfoBean>>() {
                    @Override
                    public List<CarAdvInfoBean> call(String s) {
                        return JSON.parseObject(s, new TypeReference<List<CarAdvInfoBean>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<CarAdvInfoBean>>() {
                    @Override
                    public void call(List<CarAdvInfoBean> list) {
                        CarAdvInfoBean bean = list.get(_0);
                        if (bean != null) {

                            int code = bean.getCode();
                            if (code == 0) {

                                if (bean.isIs_into_upload()) {
                                    // 上传
                                    UIUtils.startActivityInAnim(activity, new Intent(CarAdvHomeActivity.this, CarAdvCollectActivity.class).putExtra("car_adv", bean));
                                } else {
                                    // 查询页面
                                    UIUtils.startActivityInAnim(activity, new Intent(CarAdvHomeActivity.this, QueryCarAdvInfoActivity.class).putExtra("car_adv", bean));
                                }

                            }else if(code == 1){
                                // 查询页面
                                UIUtils.startActivityInAnim(activity, new Intent(CarAdvHomeActivity.this, QueryCarAdvInfoActivity.class).putExtra("car_adv", bean));

                            } else if (code == -2) {
                                Toast.makeText(CarAdvHomeActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void getQueryAdvs() {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_ADVS")
                .put("login_phone", BacApplication.getLoginPhone()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .map(new Func1<List<Map<String, Object>>, List<CarAdvHomeBean>>() {
                    @Override
                    public List<CarAdvHomeBean> call(List<Map<String, Object>> mapList) {
                        return JSON.parseObject(String.valueOf(mapList.get(0).get("advs")), new TypeReference<List<CarAdvHomeBean>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<CarAdvHomeBean>>() {
                    @Override
                    public void call(List<CarAdvHomeBean> carAdvHomeBeanList) {
                        if (carAdvHomeBeanList != null && carAdvHomeBeanList.size() > 0) {
                            CarAdvHomeAdapter adapter = (CarAdvHomeAdapter) rv.getAdapter();
                            adapter.getData().clear();
                            adapter.addData(carAdvHomeBeanList);
                        }
                    }
                });
    }
}
