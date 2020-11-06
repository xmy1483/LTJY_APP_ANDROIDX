package com.bac.bacplatform.old.module.cards;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.cards.fragment.active.CardBean;
import com.bac.bacplatform.old.module.hybrid.ZhiFuBaoActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.view.PayView;
import com.bac.bacplatform.wxapi.WxPayReq;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.os.Build.SERIAL;
import static com.bac.bacplatform.conf.Constants.CommonProperty._0;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.coupon
 * 创建人：Wjz
 * 创建时间：2017/2/24
 * 类描述：
 */

public class CouponActiveActivity extends SuperActivity implements View.OnClickListener {


    private TextView tv01;
    private TextView tv02;
    private TextView tv03;
    private TextView tv04;
    private TextView tv05;
    private TextView tv06;
    private PayView pvZhifubao;
    private PayView pvWechat;
    private Button btn;
    private String mRechargeMode = "ALIPAY";
    private int mActive;
    private int mIsType;
    private CardBean mCardBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_active_activity);

        initToolBar("优惠券");


        tv01 = (TextView) findViewById(R.id.tv_01);
        tv02 = (TextView) findViewById(R.id.tv_02);
        tv03 = (TextView) findViewById(R.id.tv_03);
        tv04 = (TextView) findViewById(R.id.tv_04);
        tv05 = (TextView) findViewById(R.id.tv_05);
        tv06 = (TextView) findViewById(R.id.tv_06);
        pvZhifubao = (PayView) findViewById(R.id.pv_zhifubao);
        pvWechat = (PayView) findViewById(R.id.pv_wechat);
        btn = (Button) findViewById(R.id.btn);
        btn.setText("确认充值");

        pvZhifubao.setViewGone(false);
        pvWechat.setViewGone(true);

        pvZhifubao.setOnClickListener(this);
        pvWechat.setOnClickListener(this);
        btn.setOnClickListener(this);

        initData();
    }

    private void initData() {
        mActive = getIntent().getIntExtra("active", -1);
        mIsType = getIntent().getIntExtra("isType", -1);//非-1 话费券
        mCardBean = getIntent().getParcelableExtra("cardBean");

        tv01.setText("油费");
        tv02.setText(mCardBean.getVoucher_money().concat("元"));

        tv06.setText("¥ ".concat(mCardBean.getRecharge_money()).concat("元"));
        if (isPhoneRecharge()) {//!=-1
            //话费券
            tv03.setText("话费");
            tv04.setText(mCardBean.getRecharge_money().concat("元"));

        } else {
            //流量券
            tv03.setText("流量");
            tv04.setText(mCardBean.getProduct_name());
        }

        tv05.setText(mCardBean.getUse_method().replace("##", "\n"));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.pv_zhifubao:
                pvZhifubao.setViewGone(false);
                pvWechat.setViewGone(true);
                mRechargeMode = "ALIPAY";//支付宝
                break;
            case R.id.pv_wechat:
                pvZhifubao.setViewGone(true);
                pvWechat.setViewGone(false);
                mRechargeMode = "WECHAT";//微信
                break;
            case R.id.btn:


                showAlertD(isPhoneRecharge());

                break;
        }
    }

    private void showAlertD(final boolean b) {
        String title = "流量充值";
        String msg = "\n充值流量：".concat(mCardBean.getProduct_name());
        if (b) {
            title = "话费充值";
            msg = "\n充值话费：".concat(mCardBean.getRecharge_money()).concat("元");
        }

        new AlertDialog.Builder(CouponActiveActivity.this)
                .setTitle(title)
                .setMessage("充值号码：".concat(BacApplication.getLoginPhone()) +
                        msg +
                        "\n实际支付：" + mCardBean.getRecharge_money().concat("元"))
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        pay(b);
                    }
                }).show();
    }

    private void pay(final boolean b) {

        //查询券
        BacHttpBean bean = new BacHttpBean().setActionType(0);

        if (b) {//话费
            bean.setMethodName("ACTIVATE_VOUCHER");
        } else {//流量
            bean.setMethodName("ACTIVATE_FLUX_VOUCHER");
        }
        bean
                .put("voucher_id", mCardBean.getVoucher_id())
                .put("voucher_money", mCardBean.getVoucher_money())
                .put("voucher_type", mCardBean.getVoucher_type())
                .put("recharge_money", mCardBean.getRecharge_money())
                .put("login_phone", BacApplication.getLoginPhone())
                .put("recharge_phone", BacApplication.getLoginPhone())
                .put("terminal_id", SERIAL.concat("##").concat(
                        Settings.Secure.getString(
                                BacApplication.getBacApplication().getContentResolver(),
                                Settings.Secure.ANDROID_ID)))
                .put("pay_money", mCardBean.getPay_money())
                .put("pay_type", mRechargeMode);

        HttpHelper.getInstance().bacNet(bean)
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())

                .map(new Func1<String, BacHttpBean>() {
                    @Override
                    public BacHttpBean call(String s) {

                        List<Map<String, Object>> cardListMap = JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                        }.getType());

                        String order_id = String.valueOf(cardListMap.get(0).get("order_id"));
                        int money = (int) Float.parseFloat(mCardBean.getRecharge_money());
                        BacHttpBean httpBean = new BacHttpBean()
                                .setActionType(0)
                                .setMethodName("PAY")
                                .put("pay_type", cardListMap.get(0).get("pay_type"))
                                .put("order_id", order_id)
                                .put("pay_money", cardListMap.get(0).get("pay_money"));
                        if (b) {
                            httpBean.put("platform_name", "PHONE_RECHARGE_VOUCHER");
                            httpBean.put("content", "话费充值");
                        } else {
                            httpBean.put("platform_name", "VOUCHER_FLUX_ACTIVATE");
                            httpBean.put("content", "流量充值");
                        }

                        return httpBean;
                    }
                })
                .observeOn(Schedulers.from(AsyncTask.SERIAL_EXECUTOR))
                .concatMap(new Func1<BacHttpBean, Observable<String>>() {
                    @Override
                    public Observable<String> call(BacHttpBean bacHttpBean) {
                        return HttpHelper.getInstance().bacNet(bacHttpBean);
                    }
                })
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        Map<String, Object> stringObjectMap = maps.get(_0);

                        if (stringObjectMap != null) {

                            if (mIsType == 1) {
                                if ((Boolean) stringObjectMap.get("activate_free")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CouponActiveActivity.this);
                                    builder.setTitle("提示")
                                            .setMessage(String.valueOf(stringObjectMap.get("activate_succ_hint")))
                                            .setPositiveButton("确定", null)
                                            .setNegativeButton("取消", null)
                                            .setCancelable(false)
                                            .show();
                                } else {
                                    CouponActiveActivity.this.onBackPressed();
                                }
                            } else {
                                if ("WECHAT".equals(mRechargeMode)) {

                                    HashMap<String, Object> hm_1 = new HashMap<>();
                                    hm_1.put("id", "other");
                                    HashMap<String, Object> hm_2 = new HashMap<>();

                                    if (b) {
                                        hm_2.put("top", "成功充值".concat(mCardBean.getRecharge_money()).concat("元").concat("话费"));
                                        hm_2.put("bottom", "话费预计两个工作日到账");
                                    } else {
                                        hm_2.put("top", "成功充值".concat(String.valueOf(mCardBean.getProduct_name())).concat("流量"));
                                        hm_2.put("bottom", mCardBean.getProduct_remark());
                                    }

                                    hm_1.put("data", hm_2);

                                    WxPayReq.pay(stringObjectMap, hm_1);


                                } else if ("ALIPAY".equals(mRechargeMode)) {

                                    String paymentUrl = String.valueOf(stringObjectMap.get("paymentUrl"));
                                    Intent intentToPay = new Intent(CouponActiveActivity.this, ZhiFuBaoActivity.class);
                                    intentToPay.putExtra("paymentUrl", paymentUrl);
                                    UIUtils.startActivityInAnim(activity, intentToPay);
                                }
                            }

                        }
                    }
                });

    }

    private boolean isPhoneRecharge() {
        return mIsType != -1;
    }

}
