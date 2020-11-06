package com.bac.bacplatform.module.center;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.utils.tools.Util;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.seed.Encrypt;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.utils.tools.Util._3CountDown;

/**
 * Created by guke on 2017/5/23.
 */

public class SetMypswActivity extends AutomaticBaseActivity {
    private EditText setpsw1;
    private EditText setpsw2;
    private EditText msg;
    private TextView hint;
    private TextView send;
    private Button set;
    private String psw1;
    private String psw2;
    private TextView tv;
    private AlertDialog dialog;
    private Handler handler = new Handler();
    private AlertDialog alertDialog;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SetMypswActivity.class);
        return intent;
    }

    public static SetMypswActivity newInstance() {
        return new SetMypswActivity();
    }

    @Override
    protected void initView() {
        tv = new TextView(activity);
        setContentView(R.layout.setpsw_activity);
        initToolBar("设置支付密码");
        setpsw1 = (EditText) findViewById(R.id.et_1);
        setpsw2 = (EditText) findViewById(R.id.et_2);
        msg = (EditText) findViewById(R.id.et_3);

        hint = (TextView) findViewById(R.id.tv_01);
        send = (TextView) findViewById(R.id.tv_02);
        set = (Button) findViewById(R.id.btn);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                psw1 = setpsw1.getText().toString().trim();
                psw2 = setpsw2.getText().toString().trim();


                if (psw1.equals(psw2) && psw1.length() == 6) {
                    //发送验证码
                    HttpHelper.getInstance()
                            .bacNet(
                                    new BacHttpBean()
                                            .setActionType(_0)
                                            .setMethodName("GET_CODE_PAY_PWD")
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
                                    Map<String, Object> stringObjectMap = maps.get(_0);//0

                                    if (!(Boolean) stringObjectMap.get("is_send_succ")) {
                                    } else {
                                        hint.setText("验证码已发送到" + BacApplication.getLoginPhone());
                                        set.setBackgroundColor(ContextCompat.getColor(activity, R.color.blue));
                                        set.setTextColor(ContextCompat.getColor(activity, R.color.white));
                                        set.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                HttpHelper.getInstance().bacNet(new BacHttpBean()
                                                        .setActionType(_0)
                                                        .setMethodName("SET_PAY_PASSWORD")
                                                        .put("login_phone", BacApplication.getLoginPhone())
                                                        .put("pay_password", new Encrypt().SHA256(setpsw1.getText().toString().trim()))
                                                        .put("verification_code", msg.getText().toString().trim()))
                                                        .compose(new RxDialog<String>().rxDialog(activity))
                                                        .observeOn(RxScheduler.RxPoolScheduler())
                                                        // .map(new JsonFunc1<String,List<Map<String,Object>>>())
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
                                                                // 弹框倒计时
                                                                View view = activity.getLayoutInflater().inflate(R.layout.set_my_psw_dialog, null);
                                                                final TextView tv01 = (TextView) view.findViewById(R.id.tv_01);
                                                                view.findViewById(R.id.tv_02).setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        SetMypswActivity.this.onBackPressed();
                                                                    }
                                                                });

                                                                _3CountDown(activity, tv01, new Util._3CountDownCallback() {
                                                                    @Override
                                                                    public void _3CountDownCallback() {
                                                                        SetMypswActivity.this.onBackPressed();
                                                                    }
                                                                });

                                                                alertDialog = new AlertDialog.Builder(activity)
                                                                        .create();
                                                                alertDialog.setView(view);
                                                                alertDialog.show();


                                                            }
                                                        });
                                            }
                                        });
                                    }
                                }
                            });
                } else {
                    Toast.makeText(activity, "请确认两次输入六位密码且相同", Toast.LENGTH_SHORT).show();
                }
            }
        });


// 按钮设置可点击，设置点击事件

//        if (hint.getText().toString().trim().equals("验证码已发送到" + BacApplication.getLoginPhone()) && msg.getText().toString().trim().length() == 4) {
//            set.setBackgroundColor(Color.parseColor("@color/blue"));
//            set.setClickable(true);
//            set.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    HttpHelper.getInstance().bacNet(new BacHttpBean()
//                            .setActionType(_0)
//                            .setMethodName("SET_PAY_PASSWORD")
//                            .put("login_phone", BacApplication.getLoginPhone())
//                            .put("pay_password", setpsw1.getText().toString())
//                            .put("SET_PAY_PASSWORD", msg.getText().toString()))
//                            .compose(new RxDialog<String>().rxDialog(activity))
//                            .observeOn(RxScheduler.RxPoolScheduler())
//                            // .map(new JsonFunc1<String,List<Map<String,Object>>>())
//                            .map(new Func1<String, List<Map<String, Object>>>() {
//                                @Override
//                                public List<Map<String, Object>> call(String s) {
//                                    return JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
//                                    }.getType());
//                                }
//                            })
//                            .observeOn(AndroidSchedulers.mainThread())
//
//                            .subscribe(new Action1<List<Map<String, Object>>>() {
//                                @Override
//                                public void call(List<Map<String, Object>> maps) {
//                                    Map<String, Object> stringObjectMap = maps.get(_0);//0
//                                    String s = JSON.toJSONString(stringObjectMap);
//                                    LogUtil.sf(activity, s);
//                                    if (stringObjectMap.get("erroId").toString().equals("0")) {
//                                        //dialog
//                                        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//                                        builder.setTitle("揩油宝支付密码成功")
//                                                .setView(tv)
//                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(
//                                                            DialogInterface dialog,
//                                                            int which) {
//                                                    }
//                                                });
//                                        dialog = builder.create();
//                                        dialog.show();
//                                        CountDownTimer timer = new CountDownTimer(4000, 1000) {
//                                            @Override
//                                            public void onTick(long millisUntilFinished) {
//                                                tv.setText(millisUntilFinished / 1000 + "S后退出");
////                        Toast.makeText(getApplicationContext(),millisUntilFinished/1000 + "S后退出",Toast.LENGTH_SHORT).show();
//
//                                            }
//
//                                            @Override
//                                            public void onFinish() {
//
//                                            }
//                                        };
//                                        timer.start();
//                                        handler.postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                dialog.cancel();
//                                                dialog.dismiss();
////                        dialog.dismiss();
//                                                //  Toast.makeText(getApplicationContext(),"我要消失啦。。。。。",Toast.LENGTH_SHORT).show();
//                                            }
//                                        }, 3000);
//
//                                    } else if (stringObjectMap.get("erroId").toString().equals("-2")) {
//                                        Toast.makeText(activity, stringObjectMap.get("msg").toString(), Toast.LENGTH_SHORT);
//                                    }
//                                }
//                            });
//                }
//            });
//        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    @Override
    protected void initFragment() {

    }



}
