package com.bac.bacplatform.old.module.cards;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.cards.adapter.InsuranceCardAdapter;
import com.bac.bacplatform.old.module.insurance.InsuranceHomeActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.card
 * 创建人：Wjz
 * 创建时间：2016/12/12
 * 类描述：
 */

public class ActivityCardInsurance extends SuperActivity {
    private RecyclerView rvNormal;
    private InsuranceCardAdapter mCardInAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView2();
        initDate();
        initEvent();
    }

    private void initView2() {
        setContentView(R.layout.insurance_card_activity);

        initToolBar("保险抵用券");

        rvNormal = (RecyclerView) findViewById(R.id.rv_normal);
        rvNormal.setLayoutManager(new LinearLayoutManager(this));
        mCardInAdapter = new InsuranceCardAdapter(R.layout.insurance_card_item, null);
        rvNormal.setAdapter(mCardInAdapter);
    }

    private void initDate() {

        //请求数据
        ArrayList<Integer> intParam = new ArrayList<>();
        intParam.add(3);

        ArrayList<Integer> intParam2 = new ArrayList<>();
        intParam2.add(1);

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setMethodName("QUERY_VOUCHER")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("status", intParam2)
                .put("voucher_type", intParam))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {
                        if (mapList != null) {

                            if (mapList.size() > 0) {

                                mCardInAdapter.addData(mapList);

                            } else {
                                new AlertDialog.Builder(ActivityCardInsurance.this)
                                        .setTitle("提示")
                                        .setMessage("没有可激活券")
                                        .setPositiveButton("确定", null)
                                        .setNegativeButton("取消", null)
                                        .show();
                            }
                        }
                    }
                });

    }

    private void initEvent() {

        rvNormal.addOnItemTouchListener(
                new OnItemClickListener() {
                    @Override
                    public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                        UIUtils.startActivityInAnim(ActivityCardInsurance.this, new Intent(ActivityCardInsurance.this, InsuranceHomeActivity.class));

                    }

                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        switch (view.getId()) {
                            case R.id.tv_card:
                                //打开详情页
                                UIUtils.startActivityInAnim(ActivityCardInsurance.this, new Intent(ActivityCardInsurance.this, ActivityCardInsuranceDetail.class)
                                        .putExtra("string", String.valueOf(JSON.toJSON(baseQuickAdapter.getData().get(position)))));
                                break;
                        }
                    }
                }
        );

    }


}
