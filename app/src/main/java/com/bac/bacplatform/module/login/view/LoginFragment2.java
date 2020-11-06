package com.bac.bacplatform.module.login.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.extended.base.components.AutomaticBaseFragment;
import com.bac.bacplatform.module.login.contract.LoginContract;
import com.bac.bacplatform.module.login.model.LoginModelImpl;
import com.bac.bacplatform.module.login.presenter.LoginPresenterImpl;
import com.bac.bacplatform.module.main.MainActivity;
import com.bac.bacplatform.utils.tools.CountDown;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.view.PolicyActivity;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.keyboard.BacInputTextView;
import com.bac.commonlib.keyboard.KeyboardUtil;
import com.bac.commonlib.utils.Util;
import com.bac.commonlib.utils.str.StringUtil;
import com.bac.commonlib.utils.ui.BacTextWatcher;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.bac.rxbaclib.rx.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

import static android.os.Build.SERIAL;
import static com.bac.bacplatform.R.id.btn_login_user;
import static com.bac.bacplatform.conf.Constants.SECOND_60;
import static com.bac.bacplatform.utils.str.StringUtil.encode;


/**
 * Created by Wjz on 2017/5/3.
 */

public class LoginFragment2 extends AutomaticBaseFragment implements LoginContract.View {
    public static final String WX_LOGIN = "wx_login";
    private Observable<BaseResp> registerWx;
    private String openid = null;
    private String errcode = null;
    private String access_token = null;
    private String sex = null;
    private String province;
    private String nickname;
    private String unionid;
    private String city;
    private String headimgurl;
    private String country;


    private ProgressDialog progressDialog;
    private LoginContract.Presenter presenter;
    private Button btnloginuser;

    private ImageView loginlogo;
    //    private TextView problem;
    private TextView tvPhone;
    private TextView tvloginalert;
    private KeyboardView kv;
    private KeyboardUtil keyboardUtil;
    private String phoneNum;
    private Subscription countDownSubscribe;
    private TextView show;
    private TextView weilogin;

    public static boolean getTorefresh() {
        return torefresh;
    }

    public static void setTorefresh(boolean torefresh) {
        LoginFragment2.torefresh = torefresh;
    }

    private static boolean torefresh = false;

    public static long getCustomers_id() {
        return customers_id;
    }

    public static void setCustomers_id(long customers_id) {
        LoginFragment2.customers_id = customers_id;
    }

    private static long customers_id;
    private BacInputTextView bacInputTextView;
    private TextView call;
    // private TextView problem

    public static LoginFragment2 newInstance() {
        return new LoginFragment2();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_2, container, false);


        // 登录页不需要toolbar
        weilogin = (TextView) view.findViewById(R.id.weixin_login);
        btnloginuser = (Button) view.findViewById(btn_login_user);
        tvPhone = (TextView) view.findViewById(R.id.tv);
        loginlogo = (ImageView) view.findViewById(R.id.login_logo);
        tvloginalert = (TextView) view.findViewById(R.id.tv_login_alert);
        kv = (KeyboardView) view.findViewById(R.id.kv);

        //新增联系登录客服
        call = (TextView) view.findViewById(R.id.call);
//        problem = (TextView) view.findViewById(R.id.problem1);
        call.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //重新打开验证码输入框
        show = (TextView) view.findViewById(R.id.textshow);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bacInputTextView = new BacInputTextView(activity, getView().findViewById(R.id.fl), 4, new BacInputTextView.BacInputTextViewCallback() {
                    @Override
                    public void callback(String text) {
                        presenter.login(new BacHttpBean()
                                        .setMethodName("LOGIN")
                                        .put("login_phone", phoneNum)
                                        .put("customers_id", customers_id)
                                        .put("verification_code", text)
                                        .put("phone_id", SERIAL.concat("##").concat(
                                                Settings.Secure.getString(BacApplication.getBacApplication().getContentResolver(),
                                                        Settings.Secure.ANDROID_ID)))
                                , true, activity);
                    }
                });
            }
        });


//        problem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!BacApplication.getmWXApi().isWXAppInstalled()) {
//                   System.out.print("没有安装微信！");
//                    return;
//                }
//                final SendAuth.Req req = new SendAuth.Req();
//                req.scope = "snsapi_userinfo";
//                req.state = "wechat_sdk_demo_test";
//                BacApplication.getmWXApi().sendReq(req);
//            }
//        });
//微信登录

        weilogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
                if (!BacApplication.getmWXApi().isWXAppInstalled()) {
                    System.out.print("没有安装微信！");
                    return;
                }
                ProgressDialog.show(activity, "",
                        "授权加载中...", true, true);
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk_demo_test";
                BacApplication.getmWXApi().sendReq(req);
                mBottomSheetDialog.dismiss();


                registerWx = RxBus.get().register(WX_LOGIN);
                registerWx.subscribe(new Action1<BaseResp>() {
                    @Override
                    public void call(BaseResp baseResp) {
                        int errCode = baseResp.errCode;
                        switch (errCode) {
                            case 0:
                                System.out.println("-----------------------------------'0'");
                                String code = ((SendAuth.Resp) baseResp).code;
                                getAccessToken1(code);
                                break;
                            default:
//                                progressDialog.dismiss();
                                Toast.makeText(activity, "授权失败", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
//                Thread t = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//
//                            Thread.sleep(5000);//让他显示10秒后，取消ProgressDialog
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
////                        UIUtils.startActivityInAnimAndFinishSelf(LoginFragment2.this.activity, MainActivity.newIntent(LoginFragment2.this.activity));
//                    }
//                });
//                t.start();


//                progressDialog.setMessage("授权加载中...");    //设置内容
//                progressDialog.setCancelable(false);//点击屏幕和按返回键都不能取消加载框
//                progressDialog.show();

//                Thread t = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(2000);//让他显示10秒后，取消ProgressDialog
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        progressDialog.dismiss();
//                    }
//                });
//                t.start();
//                UIUtils.startActivityInAnimAndFinishSelf(activity, MainActivity.newIntent(activity));
//                registerWx = RxBus.get().register(WX_LOGIN);
//                registerWx.subscribe(new Action1<BaseResp>() {
//                    @Override
//                    public void call(BaseResp baseResp) {
//                        int errCode = baseResp.errCode;
//                        switch (errCode) {
//                            case 0:
//                                System.out.println("-----------------------------------'0'");
//                                String code = ((SendAuth.Resp) baseResp).code;
//                                getAccessToken1(code);
//                                progressDialog.setMessage("授权加载中...");    //设置内容
//                                progressDialog.setCancelable(false);//点击屏幕和按返回键都不能取消加载框
//                                progressDialog.show();
//                                break;
//                            default:
//                                progressDialog.dismiss();
//                                Toast.makeText(activity, "授权失败", Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//
//                    }
//                });
//                ss("o-CTGv96D7gTReWw8ezicA8Zklv0");
//              mBottomSheetDialog.dismiss();
            }

        });


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.callPhoneUs(activity);
                /*if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //进入到这里代表没有权限.
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
                } else {
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:4001006262")));                }*/
                //Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4001006262"));

            }
        });
//        problem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4001006262"));
//                startActivity(intent);
//            }
//        });

        tvPhone.setHint(R.string.login_ccet_01_hint);
        tvPhone.addTextChangedListener(new BacTextWatcher(tvPhone));
        btnloginuser.setText("下一步");
        keyboardUtil = new KeyboardUtil(tvPhone, kv);
        RxTextView.text(tvloginalert).call(Html.fromHtml(getString(R.string.login_alter)));

        initEvent();


        return view;
    }

    private void initEvent() {
        RxTextView.textChanges(tvPhone)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        btnloginuser.setEnabled(false);
                        if (charSequence.length() == 13) {
                            phoneNum = StringUtil.replaceBlank(charSequence.toString());
                            if (StringUtil.isPhone(phoneNum)) {// 正确的手机号
                                keyboardUtil.hideKeyboard();
                                btnloginuser.setEnabled(true);
                                RxTextView.text(btnloginuser).call("下一步");
                                if (countDownSubscribe != null && !countDownSubscribe.isUnsubscribed()) {
                                    countDownSubscribe.unsubscribe();
                                }

                            } else {
                                Toast.makeText(activity, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        RxView.clicks(tvPhone)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Keyboard keyboard = null;
                        int i = new Random().nextInt(2);
                        if ((i % 2 == 0)) {
                            keyboard = new Keyboard(activity, R.xml.cl_number_1);
                        } else {
                            keyboard = new Keyboard(activity, R.xml.cl_number_2);
                        }
                        keyboardUtil.setKeyboard(keyboard);
                        keyboardUtil.showKeyboard();
                    }
                });

        RxView.clicks(btnloginuser)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // 验证手机号
                        if (StringUtil.isPhone(StringUtil.replaceBlank(phoneNum))) {
                            RxView.enabled(btnloginuser).call(false);
                            // 获取短信验证码 ，倒计时
                            GET_CODE_LOGIN();
                            // 倒计时
                            countDownSubscribe = CountDown
                                    .countDown(SECOND_60)
                                    .compose(LoginFragment2.this.bindToLifecycle())
                                    .subscribe(new Subscriber<Long>() {
                                        @Override
                                        public void onCompleted() {
                                            RxTextView.text(btnloginuser).call("下一步");
                                            RxView.enabled(btnloginuser).call(true);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            RxTextView.text(btnloginuser).call("下一步");
                                            RxView.enabled(btnloginuser).call(true);
                                        }

                                        @Override
                                        public void onNext(Long aLong) {
                                            RxTextView.text(btnloginuser).call(String.format(getString(R.string.login_time_count_down), (SECOND_60 - aLong) + ""));
                                        }
                                    });


                        } else {
                            Toast.makeText(activity, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        tvloginalert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, PolicyActivity.class));
            }
        });
    }

    /**
     * 发送验证码GET_CODE_LOGIN()
     */
    private void GET_CODE_LOGIN() {
        presenter.sendMsg(new BacHttpBean()
                        .setMethodName("GET_CODE_LOGIN")
                        .put("login_phone", phoneNum)
                , true);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void sendMsg(Map<String, Object> map) {
        customers_id = Long.parseLong(map.get("customers_id") + "");
        // 显示输入短信验证码的pop
        bacInputTextView = new BacInputTextView(activity, getView().findViewById(R.id.fl), 4, new BacInputTextView.BacInputTextViewCallback() {
            @Override
            public void callback(String text) {
                presenter.login(new BacHttpBean()
                                .setMethodName("LOGIN")
                                .put("login_phone", phoneNum)
                                .put("customers_id", customers_id)
                                .put("verification_code", text)
                                .put("phone_id", SERIAL.concat("##").concat(
                                        Settings.Secure.getString(BacApplication.getBacApplication().getContentResolver(),
                                                Settings.Secure.ANDROID_ID)))

                        , true, activity);
            }
        });
        show.setVisibility(View.VISIBLE);
    }

    @Override
    public void login(Map<String, Object> map) {
        bacInputTextView.dismiss();
        Object certificate = map.get("certificate");
        Object is_login = map.get("is_login");
        Object login_phone = map.get("login_phone");
        Object customers_id = map.get("customers_id");


        if (customers_id != null) {
            PreferenceManager.getDefaultSharedPreferences(BacApplication.getBacApplication())
                    .edit()
                    .putString("customers_id", encode(activity, customers_id))
                    .commit();
        }

        if (certificate != null && !"".equals(certificate)) {
            PreferenceManager.getDefaultSharedPreferences(BacApplication.getBacApplication())
                    .edit()
                    .putString("certificate", encode(activity, certificate))
                    .commit();

        }

        if (login_phone != null && !"".equals(login_phone)) {

            BacApplication.setLoginPhone(login_phone + "");
            // 保存登录手机号
            PreferenceManager.getDefaultSharedPreferences(BacApplication.getBacApplication())
                    .edit()
                    .putString("bac_l", encode(activity, login_phone))
                    .commit();
            //9.13
        }
        //String certificate1 = StringUtil.decode(CommonParam.getInstance().getApplication(), "certificate", CommonParam.getInstance().getS());
        if (is_login != null) {
            if ((boolean) is_login) {
                BacHttpBean bean = new BacHttpBean();
                bean.setMethodName("LOGIN");
                bean.put("login_phone", login_phone)
                        .put("certificate", certificate);
                LoginPresenterImpl loginPresenter = new LoginPresenterImpl(this, new LoginModelImpl());
                loginPresenter.loginOv(bean, false, activity);
                System.out.println("-----------------------跳转首页1");
                UIUtils.startActivityInAnimAndFinishSelf(activity, MainActivity.newIntent(activity));
                System.out.println("-----------------------跳转首页2");
            } else {
                Toast.makeText(activity, "登录失败", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bacInputTextView != null) {
            bacInputTextView.dismiss();
        }
    }

    //测试
    public void ss(String openid) {
        LoginPresenterImpl loginPresenter = new LoginPresenterImpl(LoginFragment2.this, new LoginModelImpl());
        loginPresenter.WeiXinLogin(openid);
    }

    //微信授权调用接口获取用户信息
    public void getAccessToken(String code) {
        //获取授权access_token 和openid
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx065ccc865fef7a20&secret=facec36d0a1fe8a599a91326510085ee&code=" + code + "&grant_type=authorization_code")
                .build();
        BacApplication.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                System.out.println("------------------------------------------授权登录成功后返回：" + string);
                JSONObject json = null;
                try {
                    json = new JSONObject(string);
                    access_token = json.getString("access_token");
                    openid = json.getString("openid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("------------------------------------------:access_token:" + access_token);
                System.out.println("------------------------------------------:openid" + openid);

                //判断access_token是否失效
                Request request2 = new Request.Builder()
                        .url(" https://api.weixin.qq.com/sns/auth?access_token=" + access_token + "&openid=" + openid + "")
                        .build();

                BacApplication.getOkHttpClient().newCall(request2).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        //获取用户信息
                        Request request2 = new Request.Builder()
                                .url(" https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid + "")
                                .build();

                        BacApplication.getOkHttpClient().newCall(request2).enqueue(new Callback() {

                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String string = response.body().string();
                                openid = null;
                                System.out.println("------------------------------------------获取用户信息：" + string);
                                JSONObject json = null;
                                try {
                                    json = new JSONObject(string);
                                    sex = json.getString("sex");
                                    nickname = json.getString("nickname");
                                    openid = json.getString("openid");
                                    province = json.getString("province");
                                    city = json.getString("city");
                                    country = json.getString("country");
                                    headimgurl = json.getString("headimgurl");
                                    unionid = json.getString("unionid");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("-----openid" + openid + "----nickname" + nickname + "----province" + province + "------------city" + city + "--------headimgurl" + headimgurl + "---------获取用户信息：" + string);
                                if (openid != null) {
                                    LoginPresenterImpl loginPresenter = new LoginPresenterImpl(LoginFragment2.this, new LoginModelImpl());
                                    loginPresenter.WeiXinLogin(openid);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    // 微信授权调用接口获取用户信息
    public void getAccessToken1(String code) {
        System.out.println("----------------------------------code:" + code);
        //获取授权
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx065ccc865fef7a20&secret=facec36d0a1fe8a599a91326510085ee&code=" + code + "&grant_type=authorization_code")
                .build();
        BacApplication.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                System.out.println("------------------------------------------授权登录成功后返回：" + string);
                JSONObject json = null;
                access_token = null;
                openid = null;
                errcode = null;
                try {
                    json = new JSONObject(string);
                    access_token = json.getString("access_token");
                    openid = json.getString("openid");
                    errcode = json.getString("errcode");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("------------------------------------------:errcode:" + errcode);
                System.out.println("------------------------------------------:access_token:" + access_token);
                System.out.println("------------------------------------------:openid" + openid);
                if (access_token != null && openid != null && errcode == null) {//判断请求是否成功

                    System.out.println("------------------------------------------:errcode:" + errcode);
                    System.out.println("------------------------------------------:access_token:" + access_token);
                    System.out.println("------------------------------------------:openid" + openid);

                    Request request2 = new Request.Builder()
                            .url(" https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid + "")
                            .build();
                    BacApplication.getOkHttpClient().newCall(request2).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String string = response.body().string();
                            System.out.println("------------------------------------------获取用户信息：" + string);
                            JSONObject json = null;
                            errcode = null;
                            try {
                                json = new JSONObject(string);
                                errcode = json.getString("errcode");
//                                    nickname = json.getString("nickname");
                                openid = json.getString("openid");
//                                    province = json.getString("province");
//                                    city = json.getString("city");
//                                    country = json.getString("country");
//                                    headimgurl = json.getString("headimgurl");
//                                    unionid = json.getString("unionid");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            System.out.println("------------------------------------------:errcode:" + errcode);
                            System.out.println("------------------------------------------:openid:" + openid);
                            if (openid != null && errcode == null) {
                                LoginPresenterImpl loginPresenter = new LoginPresenterImpl(LoginFragment2.this, new LoginModelImpl());
                                loginPresenter.WeiXinLogin(openid);
                            }
                        }
                    });


                }
            }
        });
    }
}
