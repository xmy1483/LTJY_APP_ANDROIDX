package com.bac.bacplatform.old.module.cards;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.recharge.OilVoucherActivity2;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.preferential.PreferentialActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.wjz.weexlib.weex.activity.WeexActivity2;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.bac.bacplatform.module.main.MainActivity.QUERY_VOUCHER;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.card
 * 创建人：Wjz
 * 创建时间：2016/10/25
 * 类描述：
 */

public class ActivityCardsHome extends SuperActivity {

    private LinearLayout llCardOil;
    private ImageView ivCardOilImg;
    private TextView tvCardOilMoney;
    private LinearLayout llCardIn;
    private ImageView ivCardInImg;
    private TextView tvCardInMoney;
    private LinearLayout llCardCode;
    private LinearLayout llCardCode2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cards_home_activity);

        initToolBar("优惠");

        llCardOil = (LinearLayout) findViewById(R.id.ll_card_oil);
        ivCardOilImg = (ImageView) findViewById(R.id.iv_card_oil_img);
        tvCardOilMoney = (TextView) findViewById(R.id.tv_card_oil_money);
        llCardIn = (LinearLayout) findViewById(R.id.ll_card_in);
        ivCardInImg = (ImageView) findViewById(R.id.iv_card_in_img);
        tvCardInMoney = (TextView) findViewById(R.id.tv_card_in_money);

        llCardCode = (LinearLayout) findViewById(R.id.ll_card_code);
        llCardCode2 = (LinearLayout) findViewById(R.id.ll_card_oil2);

        llCardOil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //油券
                //UIUtils.startActivityInAnim(activity, new Intent(ActivityCardsHome.this, ActivityCardsPhone.class));
                UIUtils.startActivityInAnim(activity, new Intent(ActivityCardsHome.this, WeexActivity2.class)
                        .setData(Uri.parse("file://assets/hello.js")));
            }
        });
        llCardIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保险券
                UIUtils.startActivityInAnim(activity, new Intent(ActivityCardsHome.this, ActivityCardInsurance.class));
            }
        });

        llCardCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.startActivityInAnim(activity, new Intent(ActivityCardsHome.this, PreferentialActivity.class));
            }
        });
        llCardCode2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UIUtils.startActivityInAnim(activity, new Intent(ActivityCardsHome.this, OilVoucherActivity2.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 请求券
        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setMethodName(QUERY_VOUCHER)
                .put("login_phone", BacApplication.getLoginPhone())
                .put("status", new int[]{0, 1})
                .put("voucher_type", new int[]{0, 1, 2, 3}))
                .compose(this.<String>bindToLifecycle())
                .compose(new RxDialog<String>().rxDialog(activity))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {

                        BigDecimal insuranceMoneyTotal = new BigDecimal(0.00);
                        BigDecimal otherMoneyTotal = new BigDecimal(0.00);

                        for (Map<String, Object> map : mapList) {
                            if ((int) map.get("voucher_type") == 3) {
                                insuranceMoneyTotal = insuranceMoneyTotal.add((BigDecimal) map.get("voucher_money"));
                            } else {
                                otherMoneyTotal = otherMoneyTotal.add((BigDecimal) map.get("voucher_money"));
                            }
                        }

                        tvCardOilMoney.setText(otherMoneyTotal.toString());
                        tvCardInMoney.setText(insuranceMoneyTotal.toString());

                        //隐藏没有数据的券标签

                        if (otherMoneyTotal.toString().equals("0") || otherMoneyTotal.toString().equals(null)){
                            llCardOil.setVisibility(View.GONE);
                        }

                        if (insuranceMoneyTotal.toString().equals("0") || insuranceMoneyTotal.toString().equals(null)){
                            llCardIn.setVisibility(View.GONE);
                        }

                    }
                });
    }
}
