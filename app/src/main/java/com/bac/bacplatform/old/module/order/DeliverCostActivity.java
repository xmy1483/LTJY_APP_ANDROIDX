package com.bac.bacplatform.old.module.order;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.hybrid.ZhiFuBaoActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.view.PayView;
import com.bac.bacplatform.view.address.CardInfoBean;
import com.bac.bacplatform.wxapi.WxPayReq;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.bac.bacplatform.conf.Constants.CommonProperty.__1;
import static com.bac.bacplatform.utils.tools.CountDown.format2;
import static com.bac.bacplatform.utils.tools.CountDown.getDayOfWeek;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.voucher
 * 创建人：Wjz
 * 创建时间：2017/2/17
 * 类描述：
 */

public class DeliverCostActivity extends SuperActivity implements View.OnClickListener {

    private TextView tv;
    private PayView pvZhifubao;
    private PayView pvWechat;
    private Button btn;
    private CardInfoBean mCardInfo;

    private String mRechargeMode = "ALIPAY";
    private Map<String, Object> mMap;
    private ProgressDialog mProgressDialog;
    private float mPay_money;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_deliver_cost);

        mCardInfo = getIntent().getParcelableExtra("cardInfo");

        initToolBar("收取邮费");

        tv = (TextView) findViewById(R.id.tv);
        pvZhifubao = (PayView) findViewById(R.id.pv_zhifubao);
        pvWechat = (PayView) findViewById(R.id.pv_wechat);
        btn = (Button) findViewById(R.id.btn);

        btn.setText("确   认");

        pvZhifubao.setOnClickListener(this);
        pvWechat.setOnClickListener(this);
        btn.setOnClickListener(this);
        pvZhifubao.setViewGone(false);
        pvWechat.setViewGone(true);

        mProgressDialog = new ProgressDialog(DeliverCostActivity.this);

        mMap = JSON.parseObject(getIntent().getStringExtra("json"), new TypeReference<Map<String, Object>>() {
        }.getType());
        if (mMap != null) {
            mPay_money = Float.parseFloat(String.valueOf(mMap.get("pay_money")));
            if (mPay_money > 0) {
                tv.setText("邮寄费用 " + mPay_money + "元");
            }
        }
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

                doPay();

                break;
        }
    }

    private void doPay() {
        if (mMap != null) {

            if (mPay_money > 0) {
                //日期
                Calendar instance = Calendar.getInstance();
                int i = instance.get(Calendar.DAY_OF_WEEK);
                final String dateStr = format2.format(instance.getTime()).concat(" 星期" + getDayOfWeek(i));


                new AlertDialog.Builder(DeliverCostActivity.this).setTitle("邮寄费用")
                        .setMessage("您需要支付" + mPay_money + "元邮寄费用")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确认",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PAY(mPay_money, dateStr);
                                    }
                                }).show();
            }
        }
    }

    private void PAY(float pay_money, final String day) {


        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("PAY")
                .put("platform_name", mMap.get("platform_name") + "")
                .put("pay_type", mRechargeMode)
                .put("order_id", mMap.get("order_id") + "")
                .put("content", "邮寄费用:" + pay_money + "元")
                .put("pay_money", pay_money))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        Map<String, Object> stringObjectMap = maps.get(0);
                        if (stringObjectMap != null) {

                            if ("WECHAT".equals(mRechargeMode)) {

                                HashMap<String, Object> hm_1 = new HashMap<>();
                                hm_1.put("id", "deliver");
                                    HashMap<String, Object> hm_2 = new HashMap<>();
                                    hm_2.put("top", day);
                                    hm_2.put("bottom", "办卡资料提交成功");
                                hm_1.put("data", hm_2);

                                WxPayReq.pay(stringObjectMap,hm_1);

                            } else if ("ALIPAY".equals(mRechargeMode)) {
                                String paymentUrl = String.valueOf(stringObjectMap.get("paymentUrl"));
                                Intent intentToPay = new Intent(DeliverCostActivity.this, ZhiFuBaoActivity.class);
                                intentToPay.putExtra("paymentUrl", paymentUrl);
                                intentToPay.putExtra("amount", "");
                                intentToPay.putExtra("source", __1);
                                intentToPay.putExtra("order_id", "");
                                UIUtils.startActivityInAnim(DeliverCostActivity.this, intentToPay);
                            }
                        }
                    }
                });
    }

}
