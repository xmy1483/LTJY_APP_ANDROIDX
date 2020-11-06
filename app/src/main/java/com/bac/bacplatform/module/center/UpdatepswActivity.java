package com.bac.bacplatform.module.center;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.main.MainActivity;
import com.bac.bacplatform.utils.tools.Util;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.seed.Encrypt;
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

public class UpdatepswActivity extends AutomaticBaseActivity {
    private EditText newpsw1;
    private EditText newpsw2;
    private Button reset;

    private AlertDialog alertDialog;
    private Handler handler = new Handler();

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, UpdatepswActivity.class);
        return intent;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.updatepsw_activity);
        initToolBar("修改支付密码");
        newpsw1 = (EditText) findViewById(R.id.et_1);
        newpsw2 = (EditText) findViewById(R.id.et_2);
        reset = (Button) findViewById(R.id.btn);


        final String oldPsw = getIntent().getStringExtra("oldPsw");
//        newpsw1.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (newpsw1.equals(newpsw2) && newpsw1.getText().length() == 6) {
//                    showtext.setText("点击确定按钮更改密码");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        newpsw2.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (newpsw1.equals(newpsw2) && newpsw2.getText().length() == 6) {
//                    showtext.setText("点击确定按钮更改密码");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String psw = newpsw1.getText().toString().trim();
                if (!TextUtils.isEmpty(psw) && psw.equals(newpsw2.getText().toString().trim())) {


                    //Toast.makeText(activity,"dianjile",Toast.LENGTH_LONG);
                    HttpHelper.getInstance()
                            .bacNetWithContext(activity,
                                    new BacHttpBean()
                                            .setActionType(_0)
                                            .setMethodName("BASEXML.UPDATE_PAY_PASSWORD")
                                            .put("login_phone", BacApplication.getLoginPhone())
                                            .put("pay_password", new Encrypt().SHA256(oldPsw))
                                            .put("new_pay_password", new Encrypt().SHA256(psw)))
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
                                    if (maps.size() > 0) {


                                        Map<String, Object> map = maps.get(_0);//0


                                        if ((int)map.get("executeResult")==1) {

                                            View view = activity.getLayoutInflater().inflate(R.layout.updatepsw_success_dialog, null);
                                            final TextView tv01 = (TextView) view.findViewById(R.id.tv_01);
                                            view.findViewById(R.id.tv_02).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    UIUtils.startActivityInAnim(activity, MainActivity.newIntent(activity));
                                                }
                                            });

                                            _3CountDown(activity, tv01, new Util._3CountDownCallback() {
                                                @Override
                                                public void _3CountDownCallback() {
                                                    UpdatepswActivity.this.onBackPressed();
                                                }
                                            });

                                            alertDialog = new AlertDialog.Builder(activity)
                                                    .create();
                                            alertDialog.setView(view);
                                            alertDialog.show();


                                        }

                                    }
                                }
                            });
                }
            }
        });


    }

    @Override
    protected void initFragment() {

    }



}
