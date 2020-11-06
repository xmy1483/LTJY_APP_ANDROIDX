package com.bac.bacplatform.old.module.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.main.MainActivity;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.bac.bacplatform.utils.ui.UIUtils.startActivityInAnim;

public class ExchangeResultActivity extends SuperActivity {

    private TextView tvExchangeTime1;
    private TextView tvExchangeTime2;
    private TextView tvExchangeTime3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_result_activity);

        // 处理toolbar  设置监听，返回主页
        initToolBar(getString(R.string.exchange_result_title), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityInAnim(ExchangeResultActivity.this, new Intent(ExchangeResultActivity.this,
                        MainActivity.class));
            }
        });

        tvExchangeTime1 = (TextView) findViewById(R.id.tv_exchange_time_1);
        tvExchangeTime2 = (TextView) findViewById(R.id.tv_exchange_time_2);
        tvExchangeTime3 = (TextView) findViewById(R.id.tv_exchange_time_3);
        initData();

    }

    private void initData() {
        String phone = getIntent().getStringExtra("phone");
        String order_id = getIntent().getStringExtra("order_id");
        QUERY_CHARGE_ORDER_DETAIL(phone, order_id);
    }

    private void QUERY_CHARGE_ORDER_DETAIL(String phone, String order_id) {

        HttpHelper.getInstance().bacNet(new BacHttpBean().setActionType(0).
                setMethodName("QUERY_CHARGE_ORDER_DETAIL")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("order_id", order_id))
                .compose(this.<String>bindToLifecycle())
                .compose(new RxDialog<String>().rxDialog(ExchangeResultActivity.this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> call(String s) {
                        return ((List<Map<String, Object>>) JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                        }.getType())).get(0);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Map<String, Object>>() {
                    @Override
                    public void call(Map<String, Object> stringObjectMap) {
                        if (stringObjectMap != null) {

                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
                            String create_time = dateFormat.format(stringObjectMap.get("create_time"));

                            tvExchangeTime1.setText(create_time);
                            tvExchangeTime2.setText(String.format(getResources().getString(R.string.exchange_result_01), dateFormat.format(stringObjectMap.get("advance_time"))));
                            tvExchangeTime3.setText(String.format(getResources().getString(R.string.exchange_result_02), stringObjectMap.get("pay_money")));

                        }
                    }
                });
    }

}
