package com.bac.bacplatform.old.module.phone;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.center.MypswActivity;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.hybrid.ZhiFuBaoActivity;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.utils.tools.Util;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.view.CanClearEditText;
import com.bac.bacplatform.view.KeyboardPopupWindow;
import com.bac.bacplatform.view.PayView;
import com.bac.bacplatform.wxapi.WxPayReq;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.seed.Encrypt;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.permission.Permission;
import com.bac.rxbaclib.rx.permission.RxPermissionImpl;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.os.Build.SERIAL;
import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.utils.str.StringUtil.replaceBlank;

/**
 * Created by Wjz on 2016/7/20.
 */
public class ActivityPhone1 extends SuperActivity implements View.OnClickListener {


    private CanClearEditText phoneRechargeEt;

    private LinearLayout phoneRechargeErrorContainer;
    //    private TextView phoneRechargeTv000;
//    private TextView phoneRechargeTv100;
//    private TextView phoneRechargeTv200;
//    private TextView phoneRechargeTv300;
//    private TextView phoneRechargeTv500;
    private TextView phoneRechargeTvCur;

    private String mRechargeMode = "ALIPAY";

    private String myCur;
    private String mPayMoney;


    private float pay_money;
    private String mSpacePhone;

    private PayView mPv_zhifubao;

    private PayView mPv_wechat;
    private Button btn;
    private PayView mPv_kaiyoubao;
    private GridView mGridView;
    private List<Map<String, Object>> mList = new ArrayList<>();
    private GvAdapter adapter;
    private KeyboardPopupWindow keyboardPopupWindow;
    private float money;

    //activity.getIntent().getStringExtra("balance")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.phone_recharge_activity);

        initToolBar("话费充值");

        //--------------------------------------------------------------
        mGridView = (GridView) findViewById(R.id.grid_view);

        adapter = new GvAdapter(this, mList);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(activity, position + "", Toast.LENGTH_SHORT).show();

                // 还原
                for (Map<String, Object> map : mList) {
                    map.put("isSelected", false);
                }
                Map<String, Object> map = mList.get(position);
                map.put("isSelected", true);

                adapter.notifyDataSetChanged();

                mPayMoney = map.get("recharge_money").toString().trim();
                getPayMoney();
                // System.out.println(map);


            }


        });


        phoneRechargeEt = (CanClearEditText) findViewById(R.id.phone_recharge_et);


        phoneRechargeEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    isPhoneNumber();
                }
                return false;
            }
        });

        phoneRechargeEt.addTextChangedListener(watcher);


        phoneRechargeTvCur = (TextView) findViewById(R.id.phone_recharge_tv_cur);

        phoneRechargeErrorContainer = (LinearLayout) findViewById(R.id.phone_recharge_error_container);


        mPv_zhifubao = (PayView) findViewById(R.id.pv_zhifubao);
        mPv_zhifubao.setOnClickListener(this);
        mPv_wechat = (PayView) findViewById(R.id.pv_wechat);
        mPv_wechat.setOnClickListener(this);
        mPv_kaiyoubao = (PayView) findViewById(R.id.pv_kaiyoubao);
        mPv_kaiyoubao.setOnClickListener(this);

        mPv_zhifubao.setViewGone(false);
        mPv_wechat.setViewGone(true);
        mPv_kaiyoubao.setViewGone(true);


        String loginPhone = BacApplication.getLoginPhone();
        phoneRechargeEt.setText(loginPhone);

        btn = (Button) findViewById(R.id.btn);
        btn.setText("确认充值");
        RxView.clicks(btn)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        pay();
                    }
                });

        Glide.with(this).load(Constants.URL.BASEURL.concat("images/recharge.jpg"))
                .placeholder(R.mipmap.banner_default)
                .into((ImageView) findViewById(R.id.iv));
    }

    private void getPayMoney() {


        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_PHONE_RECHARGE_DISCOUNT")
                .put("recharge_phone", myCur)
                .put("recharge_money", (int) Float.parseFloat(mPayMoney))//phoneSplit[0]
                .put("login_phone", BacApplication.getLoginPhone()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {
                        if (mapList.size() > 0) {

                            pay_money = Float.valueOf(mapList.get(0).get("pay_money").toString());
                            phoneRechargeTvCur.setText("¥" + pay_money + " 元");

                        } else {
                            phoneRechargeTvCur.setText("¥" + mPayMoney);
                        }
                    }
                });
    }


    private void reqPhoneMoney() {


        //号码判断 检查输入框的号码->11位
        if (!isPhoneNumber()) {
            return;
        }

        // 异步

        // 从主线程 切换到子线程
        HttpHelper.getInstance()
                .bacNet(
                        new BacHttpBean()
                                .setActionType(_0)
                                .setMethodName("PHONE_XML.QUEYR_RECHARGE_PRODUCT")
                                .put("login_phone", BacApplication.getLoginPhone())
                )
                .compose(new RxDialog<String>().rxDialog(activity))
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

                        for (int i = 0; i < maps.size(); i++) {
                            Map<String, Object> map = maps.get(i);
                            if (i == 0) {
                                mPayMoney = map.get("recharge_money").toString().trim();
                                map.put("isSelected", true);

                                // 请求金额
                                getPayMoney();
                            } else {
                                map.put("isSelected", false);
                            }
                        }
                        mList.clear();
                        mList.addAll(maps);
                        adapter.notifyDataSetChanged();

                    }
                });


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.pv_kaiyoubao:
                mPv_kaiyoubao.setViewGone(false);
                mPv_zhifubao.setViewGone(true);
                mPv_wechat.setViewGone(true);
                mRechargeMode = "KYB";//揩油宝


                money = getIntent().getFloatExtra("balance", -1f);
                if (money == -1) {
                    // 查询揩油宝余额
                    HttpHelper.getInstance()
                            .bacNet(
                                    new BacHttpBean()
                                            .setActionType(_0)
                                            .setMethodName("CARD_XML.QUERY_ACCOUNT_BALANCE")
                                            .put("login_phone", BacApplication.getLoginPhone())

                            )
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
                                    Map<String, Object> map = maps.get(_0);//0
                                    money = Float.parseFloat(map.get("balance") + "");
                                    mPv_kaiyoubao.setLabelText("揩油宝 (当前账户余额 "+money+" 元)");
                                }
                            });
                }else{
                    mPv_kaiyoubao.setLabelText("揩油宝 (当前账户余额 "+money+" 元)");
                }
                break;

            case R.id.pv_zhifubao:
                mPv_kaiyoubao.setViewGone(true);
                mPv_zhifubao.setViewGone(false);
                mPv_wechat.setViewGone(true);
                mRechargeMode = "ALIPAY";//支付宝
                break;
            case R.id.pv_wechat:
                mPv_kaiyoubao.setViewGone(true);
                mPv_zhifubao.setViewGone(true);
                mPv_wechat.setViewGone(false);
                mRechargeMode = "WECHAT";//微信
                break;

        }
    }

    private void pay() {
        //    判断手机号
        if (!isPhoneNumber()) {
            return;
        }
        final String[] phoneSplit = mPayMoney.split("元");
        String[] tempPhone = phoneRechargeTvCur.getText().toString().trim().split("元");
        String[] tempPhone2 = tempPhone[0].trim().split("¥");

        if ("0".equals(phoneSplit[0])) {
            Toast.makeText(this, "请选择支付金额", Toast.LENGTH_SHORT).show();
            return;
        }


        if (mRechargeMode == "KYB") {
            if (money < pay_money) {
                Toast.makeText(activity, "揩油宝余额不足，请选择其他充值方式", Toast.LENGTH_SHORT).show();
            } else {


                //揩油宝冲
//                String spacePhone = "";
//
//                if (TextUtils.isEmpty(mSpacePhone)) {
//                    spacePhone = myCur;
//                } else {
//                    spacePhone = mSpacePhone;
//                }

                //显示对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPhone1.this);
                builder.setTitle("信息验证")
                        .setMessage("充值号码：" + myCur + "\n" + "充值金额：" + phoneSplit[0] + "\n" + "实际支付：" + tempPhone2[1])
                        .setPositiveButton("确认充值", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HttpHelper.getInstance().activityAutoLifeAndLoading(ActivityPhone1.this,
                                        new BacHttpBean().setMethodName("BASEXML.QUERY_KEYBOARD")
                                                .put("login_phone", BacApplication.getLoginPhone()))
                                        .observeOn(RxScheduler.RxPoolScheduler())
                                        .map(new Func1<String, List<String>>() {
                                            @Override
                                            public List<String> call(String s) {
                                                List<Map<String, String>> list = JSON.parseObject(s, new TypeReference<List<Map<String, String>>>() {
                                                }.getType());
                                                return JSON.parseObject(list.get(_0).get("keyboard_value"), new TypeReference<List<String>>() {
                                                }.getType());
                                            }
                                        })
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Action1<List<String>>() {
                                            @Override
                                            public void call(List<String> list) {
                                                createKaiYouBaoRecharge(list);
                                            }
                                        });

                            }
                        })
                        .setNegativeButton("取消", null).show();
            }
        } else {
            String spacePhone = "";

            if (TextUtils.isEmpty(mSpacePhone)) {
                spacePhone = myCur;
            } else {
                spacePhone = mSpacePhone;
            }

            //显示对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPhone1.this);
            builder.setTitle("信息验证")
                    .setMessage("充值号码：" + spacePhone + "\n" + "充值金额：" + phoneSplit[0] + "\n" + "实际支付：" + tempPhone2[1])
                    .setPositiveButton("确认充值", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            doPayment(phoneSplit[0]);

                        }
                    })
                    .setNegativeButton("取消", null).show();
        }


    }

    private void createKaiYouBaoRecharge(List<String> list) {
        // 忘记密码
        keyboardPopupWindow = new KeyboardPopupWindow(list, activity, findViewById(R.id.ll), new KeyboardPopupWindow.KeyboardCallback() {
            @Override
            public void onKeyboardCallback(String pass, PopupWindow popupWindow) {

                // 更改页面
                keyboardPopupWindow.setLoading(activity);

                HttpHelper.getInstance().activityAutoLifeAndLoading(activity, new BacHttpBean()
                                .setMethodName("VERIFICATE_PAY_PASSWORD")
                                .put("login_phone", BacApplication.getLoginPhone())
                                .put("pay_password", new Encrypt().SHA256(pass))
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showKeyboardData();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showKeyboardData();
                            }
                        }, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                showKeyboardData();
                            }
                        }
                )
                        .observeOn(RxScheduler.RxPoolScheduler())
                        .map(new JsonFunc1<String, List<Map<String, Object>>>())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<Map<String, Object>>>() {
                            @Override
                            public void call(List<Map<String, Object>> list) {

                                if (list.size() > 0) {
                                    Map<String, Object> map = list.get(_0);
                                    new AlertDialog.Builder(activity)
                                            .setTitle(getString(R.string.alert_title))
                                            .setMessage(map.get("MSG") + "")
                                            .setNegativeButton(getString(R.string.alert_cancel), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    keyboardPopupWindow.dismiss();
                                                }
                                            })
                                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                @Override
                                                public void onCancel(DialogInterface dialog) {
                                                    keyboardPopupWindow.dismiss();
                                                }
                                            })
                                            .setPositiveButton(getString(R.string.alert_confirm), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    keyboardPopupWindow.dismiss();
                                                    Observable.just(null)
                                                            .compose(new RxPermissionImpl(activity).ensureEach(android.Manifest.permission.CALL_PHONE))
                                                            .subscribe(new Action1<Permission>() {
                                                                @Override
                                                                public void call(Permission permission) {
                                                                    if (permission.isGranted()) {
                                                                        String number = "4001106262";
                                                                        Intent intent = new Intent();
                                                                        intent.setAction(Intent.ACTION_CALL);
                                                                        intent.setData(Uri.parse("tel:" + number));
                                                                        activity.startActivity(intent);
                                                                    }
                                                                }
                                                            });
                                                }
                                            }).show();

                                } else {
                                    // 校验成功

                                    // chongzhi
                                    doPayment(mPayMoney);
                                }

                            }
                        });


            }

        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 忘记密码
                UIUtils.startActivityInAnim(activity, MypswActivity.newIntent(activity));
            }
        });
    }

    private void showKeyboardData() {
        HttpHelper.getInstance().activityAutoLifeAndLoading(ActivityPhone1.this,
                new BacHttpBean().setMethodName("BASEXML.QUERY_KEYBOARD")
                        .put("login_phone", BacApplication.getLoginPhone()))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<String>>() {
                    @Override
                    public List<String> call(String s) {
                        List<Map<String, String>> list = JSON.parseObject(s, new TypeReference<List<Map<String, String>>>() {
                        }.getType());
                        return JSON.parseObject(list.get(_0).get("keyboard_value"), new TypeReference<List<String>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> list) {
                        keyboardPopupWindow.showData(list);
                    }
                });
    }

    private void doPayment(final String str) {


        HttpHelper.getInstance()
                .bacNet(new BacHttpBean()
                        .setActionType(0)
                        .setMethodName("PHONE_RECHARGE")
                        .put("recharge_phone", myCur)
                        .put("recharge_money", (int) Float.parseFloat(mPayMoney))//phoneSplit[0]
                        .put("login_phone", BacApplication.getLoginPhone())
                        .put("pay_type", mRechargeMode)
                        .put("pay_money", pay_money)
                        .put("terminalId", SERIAL.concat("##").concat(
                                Settings.Secure.getString(
                                        BacApplication.getBacApplication().getContentResolver(),
                                        Settings.Secure.ANDROID_ID))))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(Schedulers.from(AsyncTask.SERIAL_EXECUTOR))
                .concatMap(new Func1<List<Map<String, Object>>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<Map<String, Object>> listMap) {

                        Map<String, Object> map = listMap.get(0);
                        return HttpHelper.getInstance().bacNet(new BacHttpBean()
                                .setActionType(0)
                                .setMethodName("PAY")
                                .put("platform_name", "PHONE_RECHARGE_REAL_TIME")
                                .put("pay_type", map.get("pay_type"))
                                .put("order_id", map.get("order_id"))
                                .put("content", "话费直充")
                                .put("pay_money", map.get("pay_money")));
                    }
                })
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {
                        Map<String, Object> map = mapList.get(0);
                        if ("ALIPAY".equals(mRechargeMode)) {
                            String paymentUrl = map.get("paymentUrl") + "";
                            Intent intentToPay = new Intent(ActivityPhone1.this, ZhiFuBaoActivity.class);
                            intentToPay.putExtra("paymentUrl", paymentUrl);

                            UIUtils.startActivityInAnim(activity, intentToPay);
                        } else if ("WECHAT".equals(mRechargeMode)) {

                            HashMap<String, Object> hm_1 = new HashMap<>();
                            hm_1.put("id", "other");
                            HashMap<String, Object> hm_2 = new HashMap<>();
                            hm_2.put("top", "成功充值".concat(str.trim()).concat("元").concat("话费"));
                            hm_2.put("bottom", "立即到账");
                            hm_1.put("data", hm_2);

                            WxPayReq.pay(map, hm_1);

                        } else if ("KYB".equals(mRechargeMode)) {
                            if (Boolean.parseBoolean(map.get("is_kyb_succ") + "")) {
                                // 充值成功
                                keyboardPopupWindow.setShowCountDown(activity, new Util._3CountDownCallback() {
                                    @Override
                                    public void _3CountDownCallback() {
                                        // 消失
                                        keyboardPopupWindow.dismiss();
                                        //支付成功页面
                                        UIUtils.startActivityInAnim(activity, PhoneRechargeResultActivity.newIntent(activity).putExtra("recharge_money", mPayMoney + "").putExtra("phone", myCur));
                                    }
                                });
                            } else {
                                // 充值失败
                                keyboardPopupWindow.setFail(activity, new Util._3CountDownCallback() {
                                    @Override
                                    public void _3CountDownCallback() {
                                        // 消失
                                        keyboardPopupWindow.dismiss();
                                    }
                                });
                            }/**/
                        }

                    }
                });

    }

    /**
     * 判断手机号
     *
     * @return false 匹配失败
     * true  匹配成功
     */
    private boolean isPhoneNumber() {
        //号码判断 检查输入框的号码->11位
        mSpacePhone = phoneRechargeEt.getText().toString().trim();

        //去掉空格
        myCur = replaceBlank(mSpacePhone);

        //判断手机号
        boolean isPhone = StringUtil.isPhone(myCur);//开头是1,11位

        if (isPhone) {
            phoneRechargeErrorContainer.setVisibility(View.GONE);//true

            return true;
        } else {
            phoneRechargeErrorContainer.setVisibility(View.VISIBLE);//false

            return false;
        }

    }


    TextWatcher watcher = new TextWatcher() {

        /**
         * 格式化手机号码 xxx xxxx xxxx
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
                phoneRechargeEt.setText(sb.toString());
                phoneRechargeEt.setSelection(index);
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

                    //隐藏键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(phoneRechargeEt.getWindowToken(), 0); //关->开 ，开->关
                    //获取当前手机号对应的产品

                    reqPhoneMoney();
                }
            }
        }
    };


}
