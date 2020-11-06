package com.bac.bacplatform.old.module.insurance;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity;
import com.bac.bacplatform.old.module.hybrid.ZhiFuBaoActivity;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceHomeBean;
import com.bac.bacplatform.old.module.insurance.domain.InsurancePlanBean;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceQueryCostAdapter;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceQueryCostBean;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.insurance
 * 创建人：Wjz
 * 创建时间：2016/12/7
 * 类描述：
 */

public class InsuranceQueryCost3 extends InsuranceActivity {

    private LinearLayout llBottom;
    private RecyclerView rv;
    private Button btnInsurance;
    private InsuranceHomeBean mBean;
    private InsurancePlanBean mList;
    private Map<String, Object> mMap;
    ArrayList<InsuranceQueryCostBean> list = new ArrayList<>();
    private InsuranceQueryCostAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insurance_query_cost_activity);

        initToolBar("查看报价");

        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        rv = (RecyclerView) findViewById(R.id.rv);
        btnInsurance = (Button) findViewById(R.id.btn_insurance);

        mBean = getIntent().getParcelableExtra("bean");
        mList = getIntent().getParcelableExtra("list");

        rv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new InsuranceQueryCostAdapter(list);
        rv.setAdapter(mAdapter);

        initData();

        initEvent();
    }

    private void initEvent() {
        btnInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //邮寄地址->支付
               /* startActivityIn(new Intent(InsuranceQueryCost3.this, InsuranceUploadAddress.class)
                        .putExtra("bean", mBean));*/
/*
                StringBuilder sb = new StringBuilder();
                sb.append("总保费：¥").append(mMap.get("total_premium"))
                        .append("\n")
                        .append("平台折扣价：¥".concat(StringUtil.isNullOrEmpty(mMap.get("discount_money"))))
                        .append("\n")
                        .append("支付金额：¥".concat(StringUtil.isNullOrEmpty(mMap.get("pay_money"))))
                        .append("\n")
                        .append(StringUtil.isNullOrEmpty(mMap.get("present_remark")).replace("##", "\n"));

                new AlertDialog.Builder(InsuranceQueryCost3.this)
                        .setTitle("温馨提示")
                       *//*
                       * 保险金额
                       * 平台折扣价
                       * 加赠
                       * *//*
                        .setMessage(sb.toString())
                        .setPositiveButton("确认", (dialog, which) -> {
                            //支付订单

                            createPayInsuranceOrder();
                        })
                        .setNegativeButton("取消", null)
                        .show();*/

                PAY_INSURE_ORDER();
            }
        });

        rv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                InsuranceQueryCostBean queryCostBean = (InsuranceQueryCostBean) baseQuickAdapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.ll_01:
                        UIUtils.startActivityInAnim(activity,new Intent(InsuranceQueryCost3.this, InsuranceDetailActivity2.class)
                                .putExtra("bean", mBean)
                                .putExtra("isShow", false));
                        break;
                    case R.id.ll:
                        Map<String, String> map = (Map<String, String>) queryCostBean.getData();
                        Intent commonIssue = new Intent(InsuranceQueryCost3.this,
                                WebAdvActivity.class);
                        commonIssue.putExtra("title", "赠品详情");
                        commonIssue.putExtra("ads_url", map.get("present_url"));
                        UIUtils.startActivityInAnim(activity,commonIssue);
                        break;
                  /*  case R.id.ll_img_upload:
                        //影像补齐
                        startActivityIn(new Intent(InsuranceQueryCost3.this, InsuranceQueryVideo.class).putExtra("bean", mBean));
                        break;*/
                }
            }
        });
    }

    private void PAY_INSURE_ORDER() {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("PAY_INSURE_ORDER")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("order_id", mMap.get("order_id"))
                .put("prv_id", mMap.get("prv_id"))
                .put("presents", mMap.get("presents"))
                .put("vouchers", mMap.get("vouchers"))
                .put("pay_money", mMap.get("pay_money"))
                .put("pay_type", "ALIPAY"))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(Schedulers.from(AsyncTask.SERIAL_EXECUTOR))// 切换到http请求线程
                .flatMap(new Func1<List<Map<String, Object>>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<Map<String, Object>> mapList) {
                        Map<String, Object> payMap = mapList.get(_0);
                        return HttpHelper.getInstance().bacNet(new BacHttpBean()
                                .setActionType(0)
                                .setMethodName("PAY")
                                .put("login_phone", BacApplication.getLoginPhone())
                                .put("platform_name", payMap.get("platform_name"))
                                .put("pay_type", payMap.get("pay_type"))
                                .put("order_id", payMap.get("order_id"))
                                .put("content", "骆驼加油车险服务")
                                .put("pay_money", payMap.get("pay_money")));
                    }
                })
                .map(new Func1<String, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> call(String s) {
                        List<Map<String, Object>> maps = JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                        }.getType());
                        Map<String, Object> map = maps.get(_0);
                        map.put("code", 0);
                        return map;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Map<String, Object>>() {
                    @Override
                    public void call(Map<String, Object> map) {
                        if (map != null) {

                            int code = (int) map.get("code");

                            if (code == 0) {
                                String paymentUrl = StringUtil.isNullOrEmpty(map.get("paymentUrl"));
                                Intent intentToPay = new Intent(InsuranceQueryCost3.this, ZhiFuBaoActivity.class);
                                intentToPay.putExtra("paymentUrl", paymentUrl);
                                intentToPay.putExtra("upload", (boolean) mMap.get("is_upload_image"));
                                intentToPay.putExtra("bean", mBean);
                                UIUtils.startActivityInAnim(activity, intentToPay);
                            } else if (code == -2) {
                                new AlertDialog.Builder(InsuranceQueryCost3.this)
                                        .setTitle("温馨提示")
                                        .setMessage(StringUtil.isNullOrEmpty(map.get("msg")))
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                InsuranceQueryCost3.this.onBackPressed();
                                            }
                                        })
                                        .setNegativeButton("取消", null).show();
                            }

                        }
                    }
                });


    }


    private void initData() {
        llBottom.setVisibility(View.VISIBLE);
        String json = getIntent().getStringExtra("json");
        if (json != null && json.length() > 0) {
            mMap = JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
            }.getType());
            mBean.setPrv_id(StringUtil.isNullOrEmpty(mMap.get("prv_id")));
            refreshListData();
        } else {
            //账单过来的数据
            fromAccountGetData();
        }

    }

    private void refreshListData() {
        try {

            list.add(new InsuranceQueryCostBean(InsuranceQueryCostBean.ONE,
                    StringUtil.isNullOrEmpty(mMap.get("prv_name")),
                    StringUtil.isNullOrEmpty(mMap.get("total_premium")),
                    StringUtil.isNullOrEmpty(mMap.get("prv_image"))));

           /* Object voucher_total_money_object = mMap.get("voucher_total_money");
            if (voucher_total_money_object != null) {
                String voucher_total_money = StringUtil.isNullOrEmpty(voucher_total_money_object);
                if (Float.valueOf(voucher_total_money) > 0) {
                    list.add(new InsuranceQueryCostBean(InsuranceQueryCostBean.TWO,
                            voucher_total_money,
                            null));
                }
            }*/


        /*if (!(Boolean) mMap.get("is_upload_image")) {
            data.add(new InsuranceQueryCostBean(InsuranceQueryCostBean.FIVE,
                    null,
                    null));
        }*/

            list.add(new InsuranceQueryCostBean(InsuranceQueryCostBean.THREE, mMap));


            Object presents = mMap.get("presents");
            if (presents != null) {
                List<HashMap<String, String>> mapList = JSON.parseObject(StringUtil.isNullOrEmpty(presents),
                        new TypeReference<List<HashMap<String, String>>>() {
                        }.getType());
                for (HashMap<String, String> hashMap : mapList) {
                    list.add(new InsuranceQueryCostBean<>(InsuranceQueryCostBean.FOUR,
                            hashMap));
                }
            }
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    private void fromAccountGetData() {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_ORDER_INFO")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("order_id", mBean.getOrder_id())
                .put("prv_id", mBean.getPrv_id()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String,List<Map<String,Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {
                        Map<String, Object> map = mapList.get(_0);
                        if (map != null) {
                            if ((int) map.get("code") == 0) {
                                mMap = map;
                                refreshListData();
                            }
                        }
                    }
                });

    }
}
