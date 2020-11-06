package com.bac.bacplatform.old.module.phone;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.module.main.MainActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;


/**
 * Created by Wjz on 2017/5/26.
 */

public class PhoneRechargeResultActivity extends AutomaticBaseActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, PhoneRechargeResultActivity.class);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.phone_recharge_result_activity);
        ImageView iv = (ImageView) findViewById(R.id.iv);
        TextView center = (TextView) findViewById(R.id.tv_center);
        center.setText("结果详情");
        Button btn = (Button) findViewById(R.id.btn);
        btn.setText("关闭");

        Intent intent = getIntent();

        TextView money = (TextView) findViewById(R.id.tv_12);
        TextView cardNo = (TextView) findViewById(R.id.tv_22);
        money.setText(String.format(getString(R.string.phone_recharge_result), intent.getStringExtra("recharge_money")));
        cardNo.setText(intent.getStringExtra("phone"));

        Observable<Void> btnClick = RxView.clicks(btn);
        Observable<Void> ivClick = RxView.clicks(iv);
        Observable.merge(btnClick, ivClick)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        // 去首页
                        UIUtils.startActivityInAnim(activity, MainActivity.newIntent(activity));
                    }
                });
    }

    @Override
    public void onBackPressed() {
        UIUtils.startActivityInAnim(activity, MainActivity.newIntent(activity));
        PhoneRechargeResultActivity.this.finish();
    }

    @Override
    protected void initFragment() {

    }


}
