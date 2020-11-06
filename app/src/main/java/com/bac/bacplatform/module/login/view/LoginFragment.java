//package com.bac.bacplatform.module.login.view;
//
//import android.graphics.Paint;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.provider.Settings;
//import android.text.Html;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bac.bacplatform.BacApplication;
//import com.bac.bacplatform.R;
//import com.bac.bacplatform.extended.base.components.AutomaticBaseFragment;
//import com.bac.bacplatform.module.login.contract.LoginContract;
//import com.bac.bacplatform.module.main.MainActivity;
//import com.bac.bacplatform.utils.tools.CountDown;
//import com.bac.bacplatform.utils.ui.UIUtils;
//import com.bac.bacplatform.view.CanClearEditText;
//import com.bac.commonlib.domain.BacHttpBean;
//import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
//import com.jakewharton.rxbinding.view.RxView;
//import com.jakewharton.rxbinding.widget.RxTextView;
//
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import rx.Subscriber;
//import rx.Subscription;
//import rx.functions.Action1;
//
//import static android.os.Build.SERIAL;
//import static com.bac.bacplatform.R.id.btn_login_user;
//import static com.bac.bacplatform.conf.Constants.CommonProperty._4;
//import static com.bac.bacplatform.conf.Constants.SECOND_2;
//import static com.bac.bacplatform.conf.Constants.SECOND_60;
//import static com.bac.bacplatform.utils.str.StringUtil.encode;
//import static com.bac.bacplatform.utils.str.StringUtil.isPhone;
//
//
///**
// * Created by Wjz on 2017/5/3.
// *
// */
//@Deprecated
//public class LoginFragment extends AutomaticBaseFragment implements LoginContract.View {
//
//    private LoginContract.Presenter presenter;
//
//    private String phone;
//    private Button btnloginuser;
//    private LinearLayout llcentercontainer;
//    private Button btnloginsendmsg;
//    private CanClearEditText ccetlogin02;
//    private ImageView loginlogo;
//    private CanClearEditText ccetlogin01;
//    private TextView tvloginalert;
//    private String checkNum;
//    private boolean isPhone;
//    private Subscription countDownSubscribe;
//    private boolean isSend;
//    private long customers_id;
//    private TextView call;
//
//    public static LoginFragment newInstance() {
//        return new LoginFragment();
//    }
//
//    @Override
//    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.login_fragment, container, false);
//
//        // 登录页不需要toolbar
//
//        btnloginuser = (Button) view.findViewById(btn_login_user);
//        llcentercontainer = (LinearLayout) view.findViewById(R.id.ll_center_container);
//        btnloginsendmsg = (Button) view.findViewById(R.id.btn_login_send_msg);
//        ccetlogin02 = (CanClearEditText) view.findViewById(R.id.ccet_login_02);
//        ccetlogin01 = (CanClearEditText) view.findViewById(R.id.ccet_login_01);
//        loginlogo = (ImageView) view.findViewById(R.id.login_logo);
//        tvloginalert = (TextView) view.findViewById(R.id.tv_login_alert);
//        call = (TextView) view.findViewById(R.id.call);
//        call.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
//
//        btnloginsendmsg.setText(R.string.login_send_msg);
//        ccetlogin01.setHint(R.string.login_ccet_01_hint);
//        ccetlogin02.setHint(R.string.login_ccet_02_hint);
//        btnloginuser.setText(R.string.login_begin_user);
//        RxTextView.text(tvloginalert).call(Html.fromHtml(getString(R.string.login_alter)));
//
//        initEvent();
//
//
//        return view;
//    }
//
//    private void initEvent() {
//
//        // 手机号输入框
//        RxTextView.textChanges(ccetlogin01)
//                .compose(this.<CharSequence>bindToLifecycle())
//                .subscribe(new Action1<CharSequence>() {
//                    @Override
//                    public void call(CharSequence charSequence) {
//
//                        phone = charSequence.toString();
//                        isPhone = isPhone(LoginFragment.this.phone);
//                        if (isPhone) {// 短信按钮
//                            if (countDownSubscribe != null && !countDownSubscribe.isUnsubscribed()) {
//                                countDownSubscribe.unsubscribe();
//                            }
//                            // 可以发送验证码
//                            btnloginsendmsg.setEnabled(isPhone);
//                            btnloginsendmsg.setText(R.string.login_send_msg);
//                        } else {
//                            // 不可
//                            btnloginsendmsg.setEnabled(isPhone);
//                        }
//                        boolean b = isSend && isPhone;
//                        if (b) {
//                            // 可以发送验证码
//                            btnloginuser.setEnabled(b);
//                        } else {
//                            // 不可
//                            btnloginuser.setEnabled(b);
//                        }
//                    }
//                });
//
//        // 短信验证码输入框
//        RxTextView.textChanges(ccetlogin02)
//                .compose(this.<CharSequence>bindToLifecycle())
//                .subscribe(new Action1<CharSequence>() {
//                    @Override
//                    public void call(CharSequence charSequence) {
//                        checkNum = charSequence.toString();
//                        isSend = checkNum.length() == _4;
//                        boolean b = isSend && isPhone;
//                        if (b) {
//                            // 可以发送验证码
//                            btnloginuser.setEnabled(b);
//                        } else {
//                            // 不可
//                            btnloginuser.setEnabled(b);
//                        }
//                    }
//                });
//
//        // 发送验证码按钮
//        RxView.clicks(btnloginsendmsg)
//                .compose(this.<Void>bindToLifecycle())
//                .doOnNext(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        RxView.enabled(btnloginsendmsg).call(false);
//                    }
//                })
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//
//
//                        // 获取验证码
//                        GET_CODE_LOGIN();
//
//                        // 倒计时
//                        countDownSubscribe = CountDown
//                                .countDown(SECOND_60)
//                                .compose(LoginFragment.this.bindToLifecycle())
//                                .subscribe(new Subscriber<Long>() {
//                                    @Override
//                                    public void onCompleted() {
//                                        RxTextView.text(btnloginsendmsg).call(getString(R.string.login_resend_msg));
//                                        RxView.enabled(btnloginsendmsg).call(true);
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                        RxTextView.text(btnloginsendmsg).call(getString(R.string.login_resend_msg));
//                                        RxView.enabled(btnloginsendmsg).call(true);
//                                    }
//
//                                    @Override
//                                    public void onNext(Long aLong) {
//                                        RxTextView.text(btnloginsendmsg).call(String.format(getString(R.string.login_time_count_down), (SECOND_60 - aLong) + ""));
//                                    }
//                                });
//
//                                /*countDownSubscribe = Observable.interval(XX, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
//                                        .take(SECOND_30)
//                                        .subscribeOn(RxScheduler.RxPoolScheduler())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(new Subscriber<Long>() {
//                                            @Override
//                                            public void onCompleted() {
//                                                RxTextView.text(btnloginsendmsg).call(getString(R.string.login_resend_msg));
//                                                RxView.enabled(btnloginsendmsg).call(true);
//                                            }
//
//                                            @Override
//                                            public void onError(Throwable e) {
//                                                RxTextView.text(btnloginsendmsg).call(getString(R.string.login_resend_msg));
//                                                RxView.enabled(btnloginsendmsg).call(true);
//                                            }
//
//                                            @Override
//                                            public void onNext(Long aLong) {
//                                                RxTextView.text(btnloginsendmsg).call(String.format(getString(R.string.login_time_count_down), (SECOND_30 - aLong) + ""));
//                                            }
//
//                                        });*/
//                        //getSubscriptions().add(countDownSubscribe);
//                    }
//
//
//                });
//
//
//        // 开始使用按钮
//        RxView
//                .clicks(btnloginuser)
//                .throttleFirst(SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
//                .compose(this.<Void>bindToLifecycle())
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        LOGIN();
//                    }
//                });
//
//
//    }
//
//    private void LOGIN() {
//
//        presenter.login(new BacHttpBean()
//                        .setMethodName("LOGIN")
//                        .put("login_phone", phone)
//                        .put("customers_id", customers_id)
//                        .put("verification_code", checkNum)
//                        .put("phone_id", SERIAL.concat("##").concat(
//                                Settings.Secure.getString(BacApplication.getBacApplication().getContentResolver(),
//                                        Settings.Secure.ANDROID_ID)))
//
//                , true, activity);
//
//
//    }
//
//    /**
//     * 发送验证码
//     */
//    private void GET_CODE_LOGIN() {
//        presenter.sendMsg(new BacHttpBean()
//                        .setMethodName("GET_CODE_LOGIN")
//                        .put("login_phone", phone)
//                , true);
//    }
//
//    @Override
//    public void setPresenter(LoginContract.Presenter presenter) {
//        this.presenter = presenter;
//    }
//
//
//    @Override
//    public void sendMsg(Map<String, Object> map) {
//        customers_id = Long.parseLong(map.get("customers_id") + "");
//    }
//
//    @Override
//    public void login(Map<String, Object> map) {
//
//        Object certificate = map.get("certificate");
//        Object is_login = map.get("is_login");
//        Object login_phone = map.get("login_phone");
//
//        if (certificate != null) {
//            PreferenceManager.getDefaultSharedPreferences(BacApplication.getBacApplication())
//                    .edit()
//                    .putString("certificate", encode(activity, certificate))
//                    .commit();
//        }
//
//        if (login_phone != null) {
//            BacApplication.setLoginPhone(login_phone + "");
//            // 保存登录手机号
//            PreferenceManager.getDefaultSharedPreferences(BacApplication.getBacApplication())
//                    .edit()
//                    .putString("bac_l", encode(activity, login_phone))
//                    .commit();
//        }
//
//        if (is_login != null) {
//            if ((boolean) is_login) {
//                // 去首页
//                UIUtils.startActivityInAnimAndFinishSelf(activity, MainActivity.newIntent(activity));
//            } else {
//                Toast.makeText(activity, "登录失败", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//}
