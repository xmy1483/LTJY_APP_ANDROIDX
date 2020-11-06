package com.bac.bacplatform.old.module.insurance;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.module.insurance.adapter.InsuranceAlterAdapter;
import com.bac.bacplatform.old.module.insurance.domain.AlterBean;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceHomeBean;
import com.bac.bacplatform.old.module.insurance.domain.InsurancePlanBean;
import com.bac.bacplatform.utils.tools.Util;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.commonlib.utils.Util.callPhoneUs;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.insurance
 * 创建人：Wjz
 * 创建时间：2016/11/25
 * 类描述：修改投保方案
 * 1111 1111 1111 1111
 * 前8位是否选择
 * 后8位是否显示
 */

public class InsuranceAlter4 extends InsuranceActivity {

    private RecyclerView rvInsuranceChoosePlan;
    private Button btnInsuranceChoosePlan;
    private List<AlterBean> mExtra;
    private List<AlterBean> mPrimary;
    private InsurancePlanBean mList;
    private InsuranceHomeBean mBean;
    private boolean mReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView2();
        initDate();
        initEvent();
    }

    private void initView2() {
        setContentView(R.layout.insurance_choose_plan_activity);

        TextView tv = (TextView) findViewById(R.id.tv_center);
        tv.setText("请选择投保方案");

        ImageView iv = (ImageView) findViewById(R.id.iv);

        RxView.clicks(iv)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        goToChoosePlan();
                    }
                });

        rvInsuranceChoosePlan = (RecyclerView) findViewById(R.id.rv_insurance_choose_plan);

        rvInsuranceChoosePlan.setLayoutManager(new LinearLayoutManager(InsuranceAlter4.this));
        btnInsuranceChoosePlan = (Button) findViewById(R.id.btn_insurance_choose_plan);
        btnInsuranceChoosePlan.setText("确定");
    }

    //跳转到险种选择
    private void goToChoosePlan() {

        Intent intent = new Intent(InsuranceAlter4.this, InsuranceChoosePlan.class);
        intent.putExtra("reset", true);
        intent.putExtra("list", mList);
        intent.putExtra("bean", mBean);

        startActivity(intent);
        activity.overridePendingTransition(R.anim.cl_slide_left_in, R.anim.cl_slide_right_out);

    }

    @Override
    public void onBackPressed() {
        goToChoosePlan();
    }

    private void initDate() {

        mList = getIntent().getParcelableExtra("list");
        mBean = getIntent().getParcelableExtra("bean");

        if (mList == null) {

            doNetGetRiskKindsList();

        } else {

            setListData();

        }
    }

    private void setListData() {
        List<InsurancePlanBean.RiskKindsBean> risk_kinds = mList.getRisk_kinds();

        //数据
        List<AlterBean> alterList = new ArrayList<>();
        mPrimary = new ArrayList<>();
        mExtra = new ArrayList<>();
        //头
        alterList.add(new AlterBean(AlterBean.HEADER, mList.isIs_payment_efc(), mList.isIs_payment_tax()));
        mPrimary.add(new AlterBean(AlterBean.TITLE, "商业险主险"));
        mExtra.add(new AlterBean(AlterBean.TITLE, "商业险附加险"));
        for (InsurancePlanBean.RiskKindsBean risk_kind : risk_kinds) {
            switch (risk_kind.getRisk_type()) {
                case 0://主险
                    mPrimary.add(new AlterBean(AlterBean.CONTEXT, risk_kind));
                    break;
                case 1://附加
                    mExtra.add(new AlterBean(AlterBean.CONTEXT, risk_kind));
                    break;
            }
        }
        alterList.addAll(mPrimary);
        alterList.addAll(mExtra);

        InsuranceAlterAdapter adapter = new InsuranceAlterAdapter(alterList);
        rvInsuranceChoosePlan.setAdapter(adapter);
    }

    private void doNetGetRiskKindsList() {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_RISK_KINDS_LIST")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("package_type", 9)
                .put("order_id", mBean.getOrder_id()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .map(new Func1<List<Map<String, Object>>, List<InsurancePlanBean>>() {
                    @Override
                    public List<InsurancePlanBean> call(List<Map<String, Object>> mapList) {

                        String json = String.valueOf(mapList.get(0).get("packages"));
                        return parseObject(json, new TypeReference<List<InsurancePlanBean>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<InsurancePlanBean>>() {
                    @Override
                    public void call(List<InsurancePlanBean> list) {
                        if (list != null) {
                            mList = list.get(0);
                            setListData();
                        }
                    }
                });

    }

    private void initEvent() {
        rvInsuranceChoosePlan.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                AlterBean item = (AlterBean) adapter.getItem(position);
                InsurancePlanBean.RiskKindsBean data = item.getData();

                switch (view.getId()) {
                    case R.id.ll:

                        boolean is_check = !data.isIs_check(); //点击的数据取反

                        if (data.getRisk_type() == 0) {//主

                            if (data.getRisk_id() == 1) {// =1是车辆损失险

                                for (AlterBean alterBean : mExtra) { //设置附加险
                                    InsurancePlanBean.RiskKindsBean risk = alterBean.getData();
                                    if (risk != null) {
                                        risk.setIs_check(is_check);//取反
                                    }
                                }

                                data.setIs_check(is_check);//点击改变
                                adapter.notifyDataSetChanged();
                            }
                            //乘客险种和司机责任险
                            else if (data.getRisk_id() == 3 || data.getRisk_id() == 4) {
                                try {
                                    for (AlterBean alterBean : mPrimary) {
                                        InsurancePlanBean.RiskKindsBean innerData = alterBean.getData();
                                        if (innerData != null) {
                                            int risk_id = innerData.getRisk_id();
                                            if (risk_id == 3 || risk_id == 4) {
                                                innerData.setIs_check(is_check);
                                            }
                                        }

                                    }
                                    adapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                }
                            } else { //非车辆损失险
                                data.setIs_check(is_check);//点击改变
                                adapter.notifyItemChanged(position);
                            }

                        } else {//附加
                            //获取 主险中的 车辆损失险 是否勾选
                            for (AlterBean alterBean : mPrimary) {//从主险中获取 车辆损失险
                                InsurancePlanBean.RiskKindsBean risk = alterBean.getData();
                                if (risk != null) {
                                    if (risk.getRisk_id() == 1) {
                                        if (risk.isIs_check()) {
                                            data.setIs_check(is_check);//点击改变
                                            adapter.notifyItemChanged(position);
                                        } else { //未勾选 提示 退出循环
                                            Toast.makeText(InsuranceAlter4.this, "请先勾选车辆损失险", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                }
                            }
                        }

                        break;
                    case R.id.ll_mid_container:
                        data.setNot_deductible(!data.isNot_deductible());
                        adapter.notifyItemChanged(position);
                        break;
                    case R.id.ll_right_container:

                        //金额pop
                        if (data.isIs_check() && data.getOption_type() != null && data.getOption_type() == 0) {
                            doNetGetOptionValue(data, view, adapter, position);
                        }
                        break;
                    case R.id.switcher:
                        boolean payment_efc = item.is_payment_efc();
                        boolean payment_tax = item.is_payment_tax();
                        item.setIs_payment_efc(!payment_efc);
                        item.setIs_payment_tax(!payment_tax);

                        mList.setIs_payment_efc(!payment_efc);
                        mList.setIs_payment_tax(!payment_tax);
                        adapter.notifyItemChanged(position);
                        break;
                }
            }
        });
        btnInsuranceChoosePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNetSubmitRiskKinds(mList);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        mReset = intent.getBooleanExtra("reset", false);
    }

    private void doNetSubmitRiskKinds(final InsurancePlanBean insurancePlanBean) {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("SUBMIT_RISK_KINDS")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("order_id", mBean.getOrder_id())
                .put("package_id", insurancePlanBean.getPackage_id())
                .put("is_payment_efc", insurancePlanBean.isIs_payment_efc())
                .put("is_payment_tax", insurancePlanBean.isIs_payment_tax())
                .put("risk_kinds", insurancePlanBean.getRisk_kinds()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<Map<String, Object>>>() {
                    @Override
                    public List<Map<String, Object>> call(String s) {
                        List<Map<String, Object>> list = null;
                        if (!TextUtils.isEmpty(s)) {
                            list = JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                            }.getType());
                        }
                        return list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {
                        if (mapList != null) {
                            Map<String, Object> stringObjectMap = mapList.get(_0);
                            if (stringObjectMap != null) {
                                if (stringObjectMap != null) {
                                    int code = (int) stringObjectMap.get("code");
                                    if (code == 0) {
                                        new AlertDialog.Builder(InsuranceAlter4.this)
                                                .setTitle("温馨提示")
                                                .setMessage(String.valueOf(stringObjectMap.get("msg")).replace("##", "\n"))
                                                .setNegativeButton("取消", null)
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        callPhoneUs(activity);
                                                    }
                                                })
                                                .show();
                                    } else if (code == -2) {
                                        new AlertDialog.Builder(InsuranceAlter4.this)
                                                .setTitle("温馨提示")
                                                .setMessage(String.valueOf(stringObjectMap.get("msg")).replace("##", "\n"))
                                                .setNegativeButton("取消", null)
                                                .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        callPhoneUs(activity);
                                                    }
                                                })
                                                .show();
                                    }
                                }
                            }
                        }
                    }
                });


    }

    private void doNetGetOptionValue(final InsurancePlanBean.RiskKindsBean data, final View mView, final BaseQuickAdapter adapter, final int mPosition) {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_RISK_KINDS_OPTIONS")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("risk_id", data.getRisk_id()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(final List<Map<String, Object>> mapList) {
                        if (mapList != null) {

                            for (Map<String, Object> map : mapList) {
                                String option_value_str = String.valueOf(map.get("option_value"));
                                if (option_value_str.length() > 4) {
                                    option_value_str = option_value_str.substring(0, option_value_str.length() - 4).concat("万");
                                }
                                map.put("option_value", option_value_str);
                            }

                            String[] key = {"option_value"};
                            int[] id = {android.R.id.text1};
                            ListView listView = new ListView(InsuranceAlter4.this);

                            listView.setBackgroundResource(R.drawable.insurance_stroke_gray_shape);
                            SimpleAdapter simpleAdapter = new SimpleAdapter(InsuranceAlter4.this, mapList, android.R.layout.simple_spinner_dropdown_item, key, id);
                            listView.setAdapter(simpleAdapter);
                            final PopupWindow mPopupWindow = new PopupWindow(listView, mView.getWidth(), Util.getHeight(InsuranceAlter4.this) / 3);
                            // 设置获取焦点
                            mPopupWindow.setFocusable(true);
                            // 设置边缘点击收起
                            mPopupWindow.setOutsideTouchable(true);
                            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
                            mPopupWindow.showAsDropDown(mView);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Map<String, Object> map = mapList.get(position);
                                    String option_value = String.valueOf(map.get("option_value"));
                                    if (option_value.contains("万")) {
                                        String[] split = option_value.split("万");
                                        option_value = split[0].concat("0000");
                                    }
                                    data.setOption_value(Integer.valueOf(option_value));
                                    data.setOption_id((int) map.get("option_id"));
                                    adapter.notifyItemChanged(mPosition);
                                    mPopupWindow.dismiss();
                                }
                            });

                        }
                    }
                });


    }

}
