package com.bac.bacplatform.old.module.exchange;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.utils.tools.CountDown;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._2;
import static com.bac.bacplatform.conf.Constants.SECOND_60;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.exchange
 * 创建人：Wjz
 * 创建时间：2016/9/19
 * 类描述：
 */
public class ExchangeMsgActivity extends SuperActivity {

    private Button mBtn_exchange;
    private String mPhone;
    private float mMoney;
    private TextView mTv_msg_phone;
    private Button mBtn_retry_msg;
    private int countdown = 60;
    private Handler mHandler = new Handler();
    private String mProduct_id;
    private ProgressDialog mProgressDialog;
    private String mOrder_id;
    private EditText mEt_msg;
    private int countBack = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.exchange_msg_activity);

        initToolBar("输入验证码");

        Intent intent = getIntent();
        mPhone = intent.getStringExtra("phone");
        mMoney = intent.getFloatExtra("money", 0);
        mProduct_id = intent.getStringExtra("product_id");

        TextView tv_msg_can_time = (TextView) findViewById(R.id.tv_msg_can_time);
        tv_msg_can_time.setText("验证码 5 分钟内输入有效。");

        mTv_msg_phone = (TextView) findViewById(R.id.tv_msg_phone);
        mTv_msg_phone.setText("发送验证码到 手机 " + mPhone);
        mBtn_retry_msg = (Button) findViewById(R.id.btn_retry_msg);
        mBtn_exchange = (Button) findViewById(R.id.btn);
        mEt_msg = (EditText) findViewById(R.id.et_msg);

        RxTextView.text(mBtn_exchange).call(getString(R.string.exchange_msg_confirm));

        initEvent();
    }

    private void sendMsg() {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("MERCHANT")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("product_id", mProduct_id)
                .put("charge_mobile", mPhone)
                .put("fee", mMoney))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<Map<String, Object>>>() {
                    @Override
                    public List<Map<String, Object>> call(String s) {
                        return JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {

                        mOrder_id = maps.get(0).get("order_id") + "";
                    }
                });
    }


    private void initEvent() {

        RxView.clicks(mBtn_retry_msg)
                .compose(this.<Void>bindToLifecycle())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        RxView.enabled(mBtn_retry_msg).call(false);
                    }
                })
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        getNewMsg();
                        // 倒计时
                        CountDown.countDown(SECOND_60)
                                .compose(ExchangeMsgActivity.this.<Long>bindToLifecycle())
                                .subscribe(new Subscriber<Long>() {
                                    @Override
                                    public void onCompleted() {
                                        RxTextView.text(mBtn_retry_msg).call(getString(R.string.login_resend_msg));
                                        RxView.enabled(mBtn_retry_msg).call(true);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        RxTextView.text(mBtn_retry_msg).call(getString(R.string.login_resend_msg));
                                        RxView.enabled(mBtn_retry_msg).call(true);
                                    }

                                    @Override
                                    public void onNext(Long aLong) {
                                        RxTextView.text(mBtn_retry_msg).call(String.format(getString(R.string.login_time_count_down), (SECOND_60 - aLong) + ""));
                                    }
                                });
                    }
                });

        RxView.clicks(mBtn_exchange)
                .compose(ExchangeMsgActivity.this.<Void>bindToLifecycle())
                .throttleFirst(_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String msg = mEt_msg.getText().toString().trim();
                        if (TextUtils.isEmpty(mOrder_id)) {
                            Toast.makeText(ExchangeMsgActivity.this, "请获取短信验证码", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(msg)) {
                            Toast.makeText(ExchangeMsgActivity.this, "请填写短信验证码", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // 验证短信
                        VERIFY_SMS_CODE(msg);
                    }
                });
    }

    /**
     * 验证短信
     *
     * @param msg
     */
    private void VERIFY_SMS_CODE(String msg) {

        HttpHelper.getInstance().bacNet(
                new BacHttpBean()
                        .setActionType(0)
                        .setMethodName("VERIFY_SMS_CODE")
                        .put("login_phone", BacApplication.getLoginPhone())
                        .put("order_id", mOrder_id)
                        .put("sms_code", msg)
                        .put("charge_mobile", mPhone))
                .compose(new RxDialog<String>().rxDialog(ExchangeMsgActivity.this))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
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
                        if (stringObjectMap != null && (boolean) stringObjectMap.get("is_success")) {

                            Intent intent = new Intent(ExchangeMsgActivity.this, ExchangeResultActivity.class);
                            intent.putExtra("phone", mPhone);
                            intent.putExtra("order_id", mOrder_id);
                            UIUtils.startActivityInAnimAndFinishSelf(activity,intent);

                        } else {
                            mBtn_exchange.setEnabled(true);
                            //错误提示
                            if (countBack <= 0) {
                                onBackPressed();
                                return;
                            }
                            Snackbar.make(findViewById(android.R.id.content), String.format(getResources().getString(R.string.exchange_msg_count), String.valueOf(countBack)), Snackbar.LENGTH_LONG)
                                    .show();
                            countBack--;
                        }
                    }
                });
    }

    /**
     * 获取短信验证码
     */
    private void getNewMsg() {
        sendMsg();
    }

}