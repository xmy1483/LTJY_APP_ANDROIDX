package com.bac.bacplatform.module.bills;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.jakewharton.rxbinding.view.RxView;
import com.wjz.weexlib.weex.activity.WeexActivity2;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

import static com.bac.bacplatform.utils.ui.UIUtils.startActivityInAnim;

/**
 * Created by Wjz on 2017/5/25.
 */

public class BillsMoreActivity extends AutomaticBaseActivity {


    @Override
    protected void initView() {
        setContentView(R.layout.bills_more_activity);
        initToolBar("账单");
//话费账单

        findViewById(R.id.rl_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityInAnim(activity,
                        new Intent(activity, WebAdvActivity.class)
                                .putExtra("title", "车险订单")
                                .putExtra("ads_url", "http://wechat.bac365.com:8081/carRisk/car_risk/jumpCarRisk/jumpPay_details?phone=" + BacApplication.getLoginPhone() + "&type=not"));
            }
        });

     /*   Observable<Void> phone = RxView.clicks(findViewById(R.id.rl_02))
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        UIUtils.startActivityInAnim(activity, new Intent(activity, QueryPhoneActivity.class));
                    }
                });*/

//流量账单
        Observable<Void> flow = RxView.clicks(findViewById(R.id.rl_03))
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UIUtils.startActivityInAnim(activity, new Intent(activity, QueryFlowActivity.class));
                    }
                });
//京东卡账单
        Observable<Void> card = RxView.clicks(findViewById(R.id.rl_card))
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UIUtil.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                                .setData(Uri.parse("https://app5.bac365.com:10443/weex/sg/SGCard/transactionDetail.js")));
                    }
                });
//油卡账单
        Observable<Void> oil = RxView.clicks(findViewById(R.id.rl_oil))
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UIUtils.startActivityInAnim(activity, new Intent(activity, QueryOilActivity.class));
                    }
                });
//话费换油账单
        Observable<Void> oil2 = RxView.clicks(findViewById(R.id.rl_oil2))
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UIUtils.startActivityInAnim(activity, new Intent(activity, QueryExchangeDetailActivity.class));
                    }
                });
//保险账单
        Observable<Void> car = RxView.clicks(findViewById(R.id.rl_car))
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UIUtils.startActivityInAnim(activity,
                                new Intent(activity, WebAdvActivity.class)
                                        .putExtra("title", "车险订单")
                                        .putExtra("ads_url", "http://wechat.bac365.com:8081/carRisk/car_risk/jumpCarRisk/jumpPay_details?phone=" + BacApplication.getLoginPhone() + "&type=not"));
                        //UIUtils.startActivityInAnim(activity, new Intent(activity, QueryInsuranceAccount.class));
                    }
                });



        Observable.merge(flow, card, oil,
                oil2,
                car)
                .subscribe();


    }

    @Override
    protected void initFragment() {

    }


}
