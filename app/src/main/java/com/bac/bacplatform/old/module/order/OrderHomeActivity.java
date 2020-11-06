package com.bac.bacplatform.old.module.order;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.view.CanClearEditText;
import com.bac.bacplatform.view.address.AddressPopupView;
import com.bac.bacplatform.view.address.CardInfoBean;
import com.bac.bacplatform.view.address.InsuranceAddressBean;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.bac.bacplatform.utils.ui.UIUtils.startActivityInAnim;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.voucher
 * 创建人：Wjz
 * 创建时间：2017/2/7
 * 类描述：办卡主页
 */

public class OrderHomeActivity extends SuperActivity {
    private CanClearEditText ccet01;
    private TextView ccet02;
    private CanClearEditText ccet03;
    private TextView tv;
    private CanClearEditText ccet04;
    private Button btn;
    private SharedPreferences mSp;

    private HashMap<String, String> mHashMap = new HashMap<>();
    private String mCacheMd5 = "";
    private AddressPopupView popupView;

    @Override
    protected void initView() {
        setContentView(R.layout.vourcher_home_activity);

        initToolBar("我要办卡");

        ccet01 = (CanClearEditText) findViewById(R.id.ccet_01);
        ccet02 = (TextView) findViewById(R.id.ccet_02);
        ccet03 = (CanClearEditText) findViewById(R.id.ccet_03);
        tv = (TextView) findViewById(R.id.tv);
        ccet04 = (CanClearEditText) findViewById(R.id.ccet_04);
        btn = (Button) findViewById(R.id.btn);

        btn.setText("确  认");
        ccet02.setText(BacApplication.getLoginPhone());
        //初始化popup
        popupView = new AddressPopupView(this, mHashMap);

        initData();
        initEvent();
    }

    private void initData() {

        /* 本地地址*/
        Observable.just(getString(R.string.areas))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .map(new Func1<String, List<InsuranceAddressBean>>() {
                    @Override
                    public List<InsuranceAddressBean> call(String areas) {

                        List<InsuranceAddressBean> list = JSON.parseObject(areas, new TypeReference<List<InsuranceAddressBean>>() {
                        }.getType());

                        return sortData(list);
                    }
                })
                .subscribeOn(RxScheduler.RxPoolScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<InsuranceAddressBean>>() {
                    @Override
                    public void call(List<InsuranceAddressBean> insuranceAddressBeenList) {
                        popupView.setPopupData(insuranceAddressBeenList);
                    }
                });


       /*
       地址不变
       HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_ALL_AREAS")
                .put("login_phone", BacApplication.getLoginPhone()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .map(new Func1<List<Map<String, Object>>, String>() {
                    @Override
                    public String call(List<Map<String, Object>> maps) {

                        return md5(maps.get(0).get("areas")+"");
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return !mCacheMd5.equals(s);
                    }
                })
                .map(new JsonFunc1<String, List<InsuranceAddressBean>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<InsuranceAddressBean>>() {
                    @Override
                    public void call(List<InsuranceAddressBean> insuranceAddressBeen) {
                        if (insuranceAddressBeen != null) {
                            mAddressAdapter01.getData().clear();
                            mAddressAdapter01.addData(insuranceAddressBeen);
                        }
                    }
                });
*/

    }

    private List<InsuranceAddressBean> sortData(List<InsuranceAddressBean> list) {
        List<InsuranceAddressBean> insuranceAddressList = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            if ("江苏省".equals(list.get(i).getProvince_name())) {
                index = i;
                break;
            }
        }
        insuranceAddressList.add(list.get(index));
        list.remove(index);
        insuranceAddressList.addAll(list);
        return insuranceAddressList;
    }


    private void initEvent() {

        popupView.setOnShowAddress(new AddressPopupView.OnShowAddress() {
            @Override
            public void onShowAddress() {
                tv.setText(mHashMap.get("province_name"));
                tv.append(mHashMap.get("city_name"));
                tv.append(mHashMap.get("area_name"));
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //网络请求
                doNetSubmitCardInfo();

            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("");
                //隐藏键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(tv.getWindowToken(), 0); //强制隐藏键盘
                popupView.showPopup(findViewById(R.id.ll), Gravity.BOTTOM, 0, 0);
            }
        });


    }

    private void doNetSubmitCardInfo() {

        String card_owner_name = ccet01.getText().toString().trim();

        String card_owner_phone = ccet02.getText().toString().trim();

        String owner_idcard = ccet03.getText().toString().trim();

        String check = tv.getText().toString().trim();

        String address = ccet04.getText().toString().trim();

        if (TextUtils.isEmpty(card_owner_name)) {
            Toast.makeText(this, "请填写收件人姓名", Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isPhone(card_owner_phone)) {
            Toast.makeText(this, "请填写收件人手机号", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(owner_idcard)) {
            Toast.makeText(this, "请填写收件人身份证", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(check)) {
            Toast.makeText(this, "请选择收件人地址", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请填写收件人详细地址", Toast.LENGTH_SHORT).show();
        } else {

            SUBMIT_CARD_INFO(card_owner_name, card_owner_phone, owner_idcard, check, address);

        }
    }

    private void SUBMIT_CARD_INFO(String card_owner_name, String card_owner_phone, String owner_idcard, String check, String address) {


        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("SUBMIT_CARD_INFO")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("card_owner_name", card_owner_name)
                .put("card_owner_phone", card_owner_phone)
                .put("owner_idcard", owner_idcard)
                .put("province", mHashMap.get("province"))
                .put("province_name", mHashMap.get("province_name"))
                .put("city", mHashMap.get("city"))
                .put("city_name", mHashMap.get("city_name"))
                .put("area", mHashMap.get("area"))
                .put("area_name", mHashMap.get("area_name"))
                .put("address", check.concat(address)))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<CardInfoBean>>() {
                    @Override
                    public List<CardInfoBean> call(String s) {
                        return JSON.parseObject(s, new TypeReference<List<CardInfoBean>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<CardInfoBean>>() {
                    @Override
                    public void call(List<CardInfoBean> list) {
                        CardInfoBean cardInfoBean = list.get(0);
                        int code = cardInfoBean.getCode();
                        if (code == 0) {
                            cardInfoBean.setProvince(mHashMap.get("province"));
                            cardInfoBean.setCity(mHashMap.get("city"));
                            cardInfoBean.setArea(mHashMap.get("area"));
                            Intent intent = new Intent(OrderHomeActivity.this, OrderUploadActivity2.class);
                            intent.putExtra("cardInfo", cardInfoBean);
                            startActivityInAnim(OrderHomeActivity.this, intent);
                            OrderHomeActivity.this.onBackPressed();
                        } else if (code == -2) {
                            Toast.makeText(OrderHomeActivity.this, cardInfoBean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}
