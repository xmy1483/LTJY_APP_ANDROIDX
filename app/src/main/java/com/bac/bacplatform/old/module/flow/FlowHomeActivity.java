package com.bac.bacplatform.old.module.flow;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.hybrid.ZhiFuBaoActivity;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.view.CanClearEditText;
import com.bac.bacplatform.view.PayView;
import com.bac.bacplatform.wxapi.WxPayReq;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static android.os.Build.SERIAL;
import static com.bac.bacplatform.conf.Constants.CommonProperty._0;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.flow
 * 创建人：Wjz
 * 创建时间：2016/10/18
 * 类描述：
 */

public class FlowHomeActivity extends SuperActivity implements View.OnClickListener {

    private RecyclerView mRv_Flow;

    private List<ItemFlowBean> mList = new ArrayList<>();
    private FlowHomeAdapter mFlowLabelAdapter;
    private CanClearEditText mEt_flow_phone;
    private LinearLayout mLl_flow_error;

    private TextView tvFlowNum;
    private TextView tvFlowDiscountKyb;
    private TextView tvFlowMoney;
    private TextView tvFlowDiscountMoney;
    private Button btn;
    private ProgressDialog mProgressDialog;
    private FlowBean mFlowBean = new FlowBean();
    private AlertDialog mAlertDialog;
    private TextView mTv_flow_home_label;
    private Boolean mIs_area;
    private String mError_area;

    private PayView mPv_zhifubao;
    private PayView mPv_wechat;

    private String mRechargeMode = "ALIPAY";
    private String mProduct_remark;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.flow_home_activity);

        initToolBar("流量充值");

        //修改
        mRv_Flow = (RecyclerView) findViewById(R.id.rv);
        mRv_Flow.setLayoutManager(new GridLayoutManager(this, 3));
        mProgressDialog = new ProgressDialog(FlowHomeActivity.this);

        btn = (Button) findViewById(R.id.btn);
        btn.setText("确认充值");
        btn.setEnabled(false);
        initData();
        initEvent();

    }


    private void initData() {

        mFlowLabelAdapter = new FlowHomeAdapter(R.layout.flow_home_item, mList);
        mFlowLabelAdapter.addHeaderView(getHeadLabelView());
        mFlowLabelAdapter.addFooterView(getFootLabelView());
        mRv_Flow.setAdapter(mFlowLabelAdapter);

    }

    private void getPhoneFlow(String phone) {


        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_FLUX_PRODUCT")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("recharge_phone", phone))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        Map<String, Object> o = maps.get(0);
                        if (o != null) {

                            mIs_area = (Boolean) o.get("is_area");
                            if (mIs_area) {//江苏移动

                                if ((Boolean) o.get("is_hint")) {
                                    mTv.setVisibility(View.VISIBLE);
                                    mTv.setText(StringUtil.isNullOrEmpty(o.get("hint_msg")));
                                } else {
                                    mTv.setVisibility(View.GONE);
                                    mList.clear();
                                    mList.addAll(JSON.parseArray(String.valueOf(JSON.toJSON(o.get("products"))), ItemFlowBean.class));

                                    for (int i = 0; i < mList.size(); i++) {

                                        ItemFlowBean itemFlowBean = mList.get(i);
                                        itemFlowBean.setId(i);

                                        //默认选中第0个
                                        if (i == 0) {
                                            itemFlowBean.setBg(R.drawable.flow_choice);
                                            itemFlowBean.setLabel(1);
                                            if (tvFlowNum != null) {

                                                mFlowBean.setProduct_id(itemFlowBean.getProduct_id());
                                                mFlowBean.setProduct_name(itemFlowBean.getProduct_name());
                                                mFlowBean.setMarket_price(itemFlowBean.getMarket_price());
                                                mFlowBean.setDonate_money(itemFlowBean.getDonate_money());
                                                mFlowBean.setPrice(itemFlowBean.getPrice());

                                                alterUI(itemFlowBean);//修改产品解释说明

                                                //处理按钮是否可用
                                                //处理是否弹框
                                                if (itemFlowBean.is_enable()) {
                                                    btn.setEnabled(true);
                                                } else {
                                                    btn.setEnabled(false);
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(FlowHomeActivity.this);
                                                    builder.setTitle("提示")
                                                            .setMessage(itemFlowBean.getEnable_remark())
                                                            .setPositiveButton("确定", null)
                                                            .setNegativeButton("取消", null)
                                                            .setCancelable(false)
                                                            .show();
                                                }
                                            }
                                        } else {
                                            itemFlowBean.setLabel(0);
                                            //其他，如果有优惠显示优惠背景
                                            double donate_money = itemFlowBean.getDonate_money();
                                            if (donate_money > 0) {
                                                itemFlowBean.setBg(R.drawable.flow_favorable);
                                            }
                                        }
                                    }
                                    mFlowLabelAdapter.notifyDataSetChanged();
                                }
                            } else {
                                mError_area = String.valueOf(o.get("error_area"));
                                new AlertDialog.Builder(FlowHomeActivity.this)
                                        .setTitle(getString(R.string.alert_title))
                                        .setPositiveButton(R.string.alert_confirm, null)
                                        .setMessage(mError_area)
                                        .show();

                            }
                        }
                    }
                });

    }

    private void alterUI(ItemFlowBean itemFlowBean) {
        tvFlowNum.setText(itemFlowBean.getProduct_name());
        //市场价market_price
        tvFlowMoney.setText("市场价：" + String.valueOf(itemFlowBean.getMarket_price()) + "元");

        //实际支付
        tvFlowDiscountMoney.setText("¥ " + String.valueOf(itemFlowBean.getPrice()) + "元");

        mProduct_remark = itemFlowBean.getProduct_remark();
        mTv_flow_home_label.setText(mProduct_remark);
        //揩油币
        double donate_money = itemFlowBean.getDonate_money();
        if (donate_money > 0) {
            tvFlowDiscountKyb.setVisibility(View.VISIBLE);
            tvFlowDiscountKyb.setText(String.format(getResources().getString(R.string.flow_discount_kyb), String.valueOf(donate_money)));
        } else {
            tvFlowDiscountKyb.setVisibility(View.GONE);
        }
    }

    private View getHeadLabelView() {
        View header = getLayoutInflater().inflate(R.layout.flow_home_header_item, null);
        mLl_flow_error = (LinearLayout) header.findViewById(R.id.ll_flow_error);

        mEt_flow_phone = (CanClearEditText) header.findViewById(R.id.et_flow_phone);
        mEt_flow_phone.addTextChangedListener(watcher);
        mEt_flow_phone.setText(BacApplication.getLoginPhone());

        mTv = (TextView) header.findViewById(R.id.tv);

        return header;
    }


    TextWatcher watcher = new TextWatcher() {

        /**
         * 格式化手机号码 xxx xxxx xxxx
         * 输入空格有问题
         * @param s
         * @param start
         * @param before
         * @param count
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s == null || s.length() == 0) {
                return;
            }
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < s.length(); i++) {
                if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                    continue;
                } else {
                    sb.append(s.charAt(i));
                    if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                        sb.insert(sb.length() - 1, ' ');
                    }
                }
            }
            if (!sb.toString().equals(s.toString())) {
                int index = start + 1;
                if (sb.charAt(start) == ' ') {
                    if (before == 0) {
                        index++;
                    } else {
                        index--;
                    }
                } else {
                    if (before == 1) {
                        index--;
                    }
                }

                mEt_flow_phone.setText(sb.toString());
                mEt_flow_phone.setSelection(index);

            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 13) {
                String phone = StringUtil.replaceBlank(s.toString());
                if (StringUtil.isPhone(phone)) {
                    mLl_flow_error.setVisibility(View.GONE);
                    //隐藏键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEt_flow_phone.getWindowToken(), 0); //关->开 ，开->关
                    //获取当前手机号对应的产品
                    getPhoneFlow(phone);
                } else {
                    mLl_flow_error.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    private View getFootLabelView() {

        View foot = getLayoutInflater().inflate(R.layout.flow_home_foot_item, null);

        tvFlowNum = (TextView) foot.findViewById(R.id.tv_flow_num);
        tvFlowDiscountKyb = (TextView) foot.findViewById(R.id.tv_flow_discount_kyb);
        tvFlowMoney = (TextView) foot.findViewById(R.id.tv_flow_money);
        tvFlowMoney.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tvFlowMoney.getPaint().setAntiAlias(true);// 抗锯齿

        tvFlowDiscountMoney = (TextView) foot.findViewById(R.id.tv_flow_discount_money);

        mTv_flow_home_label = (TextView) foot.findViewById(R.id.tv_flow_home_label);


        mPv_zhifubao = (PayView) foot.findViewById(R.id.pv_zhifubao);
        mPv_zhifubao.setOnClickListener(this);
        mPv_wechat = (PayView) foot.findViewById(R.id.pv_wechat);
        mPv_wechat.setOnClickListener(this);

        mPv_zhifubao.setViewGone(false);
        mPv_wechat.setViewGone(true);


        return foot;
    }

    private void initEvent() {

        mRv_Flow.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int i) {
                if (mIs_area) {
                    List<ItemFlowBean> data = baseQuickAdapter.getData();
                    for (ItemFlowBean itemFlowBean : data) {
                        if (itemFlowBean.getId() == i) {
                            itemFlowBean.setBg(R.drawable.flow_choice);
                            itemFlowBean.setLabel(1);

                            mFlowBean.setProduct_id(itemFlowBean.getProduct_id());
                            mFlowBean.setProduct_name(itemFlowBean.getProduct_name());
                            mFlowBean.setMarket_price(itemFlowBean.getMarket_price());
                            mFlowBean.setDonate_money(itemFlowBean.getDonate_money());
                            mFlowBean.setPrice(itemFlowBean.getPrice());

                            alterUI(itemFlowBean);
                            //处理按钮是否可用
                            //处理是否弹框
                            if (itemFlowBean.is_enable()) {
                                btn.setEnabled(true);
                            } else {
                                btn.setEnabled(false);
                                AlertDialog.Builder builder = new AlertDialog.Builder(FlowHomeActivity.this);
                                builder.setTitle("提示")
                                        .setMessage(itemFlowBean.getEnable_remark())
                                        .setPositiveButton("确定", null)
                                        .setNegativeButton("取消", null)
                                        .setCancelable(false)
                                        .show();
                            }
                        } else {
                            itemFlowBean.setLabel(0);
                            if (itemFlowBean.getDonate_money() > 0) {
                                itemFlowBean.setBg(R.drawable.flow_favorable);
                            } else {
                                itemFlowBean.setBg(Color.TRANSPARENT);
                            }
                        }
                    }
                    baseQuickAdapter.notifyDataSetChanged();
                } else {

                    new AlertDialog.Builder(activity)
                            .setTitle(getString(R.string.alert_title))
                            .setMessage(mError_area)
                            .setPositiveButton(getString(R.string.alert_confirm), null)
                            .show();

                }
            }

        });


        RxView.clicks(btn)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        String et_text = mEt_flow_phone.getText().toString().trim();
                        String phone = StringUtil.replaceBlank(et_text);
                        if (TextUtils.isEmpty(phone) && !StringUtil.isPhone(phone)) {
                            Toast.makeText(activity, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        FLUX_RECHARGE(phone);

                    }
                });
    }

    private void FLUX_RECHARGE(String phone) {


        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("FLUX_RECHARGE")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("recharge_phone", phone)
                .put("product_id", mFlowBean.getProduct_id())
                .put("product_name", mFlowBean.getProduct_name())
                .put("market_price", mFlowBean.getMarket_price())
                .put("donate_money", mFlowBean.getDonate_money())
                .put("price", mFlowBean.getPrice())
                .put("terminal_id", SERIAL.concat("##").concat(
                        Settings.Secure.getString(
                                BacApplication.getBacApplication().getContentResolver(),
                                Settings.Secure.ANDROID_ID)))
                .put("pay_type", mRechargeMode)
        )
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        final Map<String, Object> stringObjectMap = maps.get(_0);

                        if (stringObjectMap != null) {

                            //充值对话框
                            new AlertDialog.Builder(FlowHomeActivity.this).setTitle("流量充值")
                                    .setMessage("充值号码：" + stringObjectMap.get("recharge_phone") +
                                            "\n充值流量：" + stringObjectMap.get("product_name") +
                                            "\n实际支付：" + stringObjectMap.get("price"))//充值号码，充值流量，实际支付
                                    .setNegativeButton("取消", null)
                                    .setPositiveButton("确认充值", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            PAY(stringObjectMap);

                                        }
                                    }).show();
                        } else {
                            btn.setEnabled(true);
                        }
                    }
                });

    }

    private void PAY(Map<String, Object> stringObjectMap) {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("PAY")
                .put("platform_name", "RECHARGE_FLUX")
                .put("pay_type", stringObjectMap.get("pay_type"))
                .put("order_id", stringObjectMap.get("order_id"))
                .put("content", "流量直充")
                .put("pay_money", stringObjectMap.get("price")))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {

                        Map<String, Object> map = maps.get(_0);

                        if (map != null) {

                            if ("WECHAT".equals(mRechargeMode)) {


                                HashMap<String, Object> hm_1 = new HashMap<>();
                                hm_1.put("id", "other");
                                HashMap<String, Object> hm_2 = new HashMap<>();

                                hm_2.put("top", "成功充值".concat(String.valueOf(map.get("product_name"))).concat("流量"));
                                hm_2.put("bottom", mProduct_remark);
                                hm_1.put("data", hm_2);

                                WxPayReq.pay(map, hm_1);


                            } else if ("ALIPAY".equals(mRechargeMode)) {
                                UIUtils.startActivityInAnimAndFinishSelf(activity, new Intent(FlowHomeActivity.this, ZhiFuBaoActivity.class).putExtra("paymentUrl", String.valueOf(map.get("paymentUrl"))));
                            }

                        }


                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.pv_zhifubao:
                mPv_zhifubao.setViewGone(false);
                mPv_wechat.setViewGone(true);
                mRechargeMode = "ALIPAY";//支付宝
                break;
            case R.id.pv_wechat:
                mPv_zhifubao.setViewGone(true);
                mPv_wechat.setViewGone(false);
                mRechargeMode = "WECHAT";//微信
                break;
        }
    }

}