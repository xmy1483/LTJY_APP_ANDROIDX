package com.bac.bacplatform.module.center;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.utils.tools.Util;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.view.KeyboardPopupWindow;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.seed.Encrypt;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;

/**
 * Created by guke on 2017/5/22.
 */

public class MypswActivity extends AutomaticBaseActivity {
    private RelativeLayout rl1;
    private RelativeLayout rl2;
    private RelativeLayout rl3;
    private Boolean havepsw;
    private String oldpsw;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MypswActivity.class);
        return intent;
    }

    @Override
    protected void onStart() {
        super.onStart();
        HttpHelper.getInstance()
                .bacNet(
                        new BacHttpBean()
                                .setActionType(_0)
                                .setMethodName("CARD_XML.QUERY_ACCOUNT_BALANCE")
                                .put("login_phone", BacApplication.getLoginPhone())
                )
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
                        Map<String, Object> stringObjectMap = maps.get(_0);//0

                        if (Boolean.parseBoolean(stringObjectMap.get("is_have_pay_pw") + "")) {// 设置
                            rl1.setVisibility(View.GONE);
                            rl2.setVisibility(View.VISIBLE);
                            rl3.setVisibility(View.VISIBLE);
                        } else {// 没设置
                            rl1.setVisibility(View.VISIBLE);
                            rl2.setVisibility(View.GONE);
                            rl3.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    protected void initView() {


        setContentView(R.layout.mypsw_activity);
        initToolBar("我的密码");
        rl1 = (RelativeLayout) findViewById(R.id.rl_01);
        rl2 = (RelativeLayout) findViewById(R.id.rl_02);
        rl3 = (RelativeLayout) findViewById(R.id.rl_03);


        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.startActivityInAnim(activity, new Intent(activity, SetMypswActivity.class));
            }
        });

        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                HttpHelper.getInstance().activityAutoLifeAndLoading(activity,
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
                                createKeyboard(list);
                            }
                        });

            }
        });

        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String load = ("");
//                Intent intentToAgree = new Intent(activity,
//                        WebAdvActivity.class);
//                intentToAgree.putExtra("title", "找回支付密码");
//                intentToAgree.putExtra("ads_url", load);
//                UIUtils.startActivityInAnim(activity, intentToAgree);
                new AlertDialog.Builder(activity)
                        .setTitle("400-110-6262")
                        .setMessage("客户热线服务时间：07：00-22：00")
                        .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (ActivityCompat.checkSelfPermission(MypswActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4001106262")));
                            }
                        })
                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        })
                        .show();
            }
        });

    }

    private void createKeyboard(List<String> list) {
        new KeyboardPopupWindow(list,MypswActivity.this, findViewById(R.id.ll), new KeyboardPopupWindow.KeyboardCallback() {
             @Override
             public void onKeyboardCallback(final String oldpsw, final PopupWindow popupWindow) {

                 popupWindow.dismiss();

                 HttpHelper.getInstance()
                         .bacNetWithContext(activity,
                                 new BacHttpBean().setActionType(_0)
                                         .setMethodName("VERIFICATE_PAY_PASSWORD")
                                         .put("login_phone", BacApplication.getLoginPhone())
                                         .put("pay_password", new Encrypt().SHA256(oldpsw))
                         )
                         .compose(activity.<String>bindUntilEvent(ActivityEvent.DESTROY))
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
                         .doOnNext(new Action1<List<Map<String, Object>>>() {
                                       @Override
                                       public void call(List<Map<String, Object>> maps) {

                                           if (maps.size()>0) {
                                               // 失败
                                               Log.d("输入结果", "call:失败 ");

                                               Map<String, Object> map = maps.get(_0);
                                               new AlertDialog.Builder(activity)
                                                       .setTitle(getString(R.string.alert_title))
                                                       .setMessage(map.get("MSG")+"")
                                                       .setPositiveButton(getString(R.string.alert_confirm), new DialogInterface.OnClickListener() {
                                                           @Override
                                                           public void onClick(DialogInterface dialog, int which) {
                                                               Util.callUs(activity);
                                                           }
                                                       })
                                                       .setNegativeButton(getString(R.string.alert_cancel),null)
                                                       .show();

                                           }else{
                                               // 成功
                                               Log.d("输入结果", "call:成功 ");
                                               UIUtils.startActivityInAnim(activity,new Intent(activity,UpdatepswActivity.class)
                                                       .putExtra("oldPsw",oldpsw)
                                               );

                                           }

                                           //Map<String, Object> stringObjectMap = maps.get(_0);//0

// [{"MSG":"支付密码已被锁定，请联系客服","ID":"","AES":"N","cl_COUNT":-1,"TOKEN":"8400536853490386700","ERROR":10130005}]


//                                                  if (stringObjectMap.get("errorId").equals("0")) {
//                                                      Intent intent = new Intent(activity, UpdatepswActivity.class);
//                                                      intent.putExtra("oldpsw", oldpsw);
//                                                      startActivityInAnim(activity, UpdatepswActivity.newIntent(activity));
//                                                      popupWindow.dismiss();
//                                                  } else if (stringObjectMap.get("errorId").equals("-2")) {
//                                                      Toast.makeText(activity, stringObjectMap.get("msg").toString(), Toast.LENGTH_SHORT).show();
//                                                  }
                                       }
                                   }


                         ).subscribe();


             }
         }, new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 // 忘记密码
             }
         });
    }

    @Override
    protected void initFragment() {

    }


}
