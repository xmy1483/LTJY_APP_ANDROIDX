package com.bac.bihupapa.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bihupapa.BuildConfig;
import com.bac.bihupapa.R;
import com.bac.bihupapa.bean.InsuranceAddressBean;
import com.bac.bihupapa.bean.InsuranceHomeBean;
import com.bac.bihupapa.util.StringUtil;
import com.bac.bihupapa.view.AddressPopupView;
import com.bac.bihupapa.view.CanClearEditText;
import com.bac.bihupapa.view.RecycleViewDivider;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.http.HttpHelperLib;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.bac.bihupapa.conf.Constants.CommonProperty._0;


/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.insurance
 * 创建人：Wjz
 * 创建时间：2016/12/6
 * 类描述：
 */
public class InsuranceUploadAddress extends AutomaticRxAppCompatActivity {

    private CanClearEditText etDialog01;
    private CanClearEditText etDialog02;
    private TextView tv;
    private CanClearEditText etDialog03;
    private InsuranceHomeBean mBean;

    private HashMap<String, String> mHashMap = new HashMap<>();

    private Button mBtn;

    private View mLatestLayout;
    private RecyclerView mLatestRv;
    private LatestAdapter mLatestAdapter;
    private TextView mTvAddress;
    private boolean mAlter;
    private LinearLayout mLl;

    private Button mBtnRv;
    private boolean isOther;
    private AddressPopupView addressPopupView;
    private PopupWindow mPopupWindow2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insurance_delivery_activity);

        ((TextView)findViewById(R.id.tv_center)).setText("填写收件地址");

        etDialog01 = findViewById(R.id.et_dialog_01);
        etDialog02 = findViewById(R.id.et_dialog_02);
        tv = findViewById(R.id.tv);
        etDialog03 = findViewById(R.id.et_dialog_03);
        mLl = findViewById(R.id.ll);


        mBtn = findViewById(R.id.btn);
        mBtn.setText("确认");
        mTvAddress = findViewById(R.id.tv_address);

        addressPopupView = new AddressPopupView(this, mHashMap);

        mLatestLayout = View.inflate(InsuranceUploadAddress.this, R.layout.layout_rv, null);
        mBtnRv = mLatestLayout.findViewById(R.id.btn);
        mBtnRv.setText("确认");
        mLatestRv = mLatestLayout.findViewById(R.id.rv);
        mLatestRv.setLayoutManager(new LinearLayoutManager(InsuranceUploadAddress.this));
        mLatestRv.addItemDecoration(new RecycleViewDivider(InsuranceUploadAddress.this, LinearLayoutManager.HORIZONTAL, 1, R.color.black));


        mHashMap.put("province", "320000");
        mHashMap.put("province_name", "江苏省");

        initData();

        initEvent();
    }


    private class LatestAdapter extends BaseQuickAdapter {
        public LatestAdapter(int layoutResId, List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, Object o) {

            try {
                Map<String, Object> map = (Map<String, Object>) o;

                TextView tv01 = baseViewHolder.getView(R.id.tv_choose_plan_top);
                TextView tv02 = baseViewHolder.getView(R.id.tv_choose_plan_bottom);
                TextView tv03 = baseViewHolder.getView(R.id.tv_other);
                ImageView iv = baseViewHolder.getView(R.id.iv_choose_plan_choice);

                Object isSelect = map.get("isSelect");
                if (isSelect != null) {
                    if ((boolean) isSelect) {
                        iv.setVisibility(View.VISIBLE);
                        if (map.get("flag") == null) {//历史
                            isOther = false;
                            //选中刷新
                            mHashMap.put("address", StringUtil.isNullOrEmpty(map.get("address")));
                            mHashMap.put("province", StringUtil.isNullOrEmpty(map.get("province")));
                            mHashMap.put("province_name", StringUtil.isNullOrEmpty(map.get("province_name")));
                            mHashMap.put("city", StringUtil.isNullOrEmpty(map.get("city")));
                            mHashMap.put("city_name", StringUtil.isNullOrEmpty(map.get("city_name")));
                            mHashMap.put("area", StringUtil.isNullOrEmpty(map.get("area")));
                            mHashMap.put("area_name", StringUtil.isNullOrEmpty(map.get("area_name")));
                            mHashMap.put("name", StringUtil.isNullOrEmpty(map.get("name")));
                            mHashMap.put("phone", StringUtil.isNullOrEmpty(map.get("phone")));
                        } else {//其他
                            isOther = true;
                        }
                    } else {
                        iv.setVisibility(View.GONE);
                    }
                }


                if (map.get("flag") != null) {
                    tv01.setVisibility(View.INVISIBLE);
                    tv02.setVisibility(View.INVISIBLE);
                    tv03.setVisibility(View.VISIBLE);
                    //其他
                    tv03.setText("填写其他邮寄地址");
                } else {
                    tv01.setVisibility(View.VISIBLE);
                    tv02.setVisibility(View.VISIBLE);
                    tv03.setVisibility(View.GONE);
                    tv01.setText(
                            StringUtil.isNullOrEmpty(map.get("name"))
                                    + "\n" + StringUtil.isNullOrEmpty(map.get("phone")));
                    tv02.setText(StringUtil.isNullOrEmpty(map.get("address")));
                }
            } catch (Exception e) {
            }
        }
    }

    private void initEvent() {

        addressPopupView.setOnShowAddress(new AddressPopupView.OnShowAddress() {
            @Override
            public void onShowAddress() {
                tv.setText(mHashMap.get("province_name"));
                tv.append(mHashMap.get("city_name"));
                tv.append(mHashMap.get("area_name"));
            }
        });

        mTvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //隐藏键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(tv.getWindowToken(), 0); //强制隐藏键盘


                if (mPopupWindow2 != null) {
                    mPopupWindow2.showAtLocation(mLl, Gravity.BOTTOM, 0, 0);
                }

            }
        });

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNetForDelivery();
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("");

                //隐藏键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(tv.getWindowToken(), 0); //强制隐藏键盘
                addressPopupView.showPopup(findViewById(R.id.ll), Gravity.BOTTOM, 0, 0);

            }
        });

        mLatestRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<Map<String, Object>> list = baseQuickAdapter.getData();

                for (int i1 = 0; i1 < list.size(); i1++) {
                    Map<String, Object> map = list.get(i1);
                    if (i1 == position) {
                        map.put("isSelect", true);
                    } else {
                        map.put("isSelect", false);
                    }
                }
                baseQuickAdapter.notifyDataSetChanged();
            }
        });

        mBtnRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOther) {//其他
                    mPopupWindow2.dismiss();
                    Toast.makeText(InsuranceUploadAddress.this, "请填写邮寄地址", Toast.LENGTH_SHORT).show();
                } else {
                    createDelivery();
                }
            }
        });

    }

    private void doNetForDelivery() {

        String name = etDialog01.getText().toString().trim();
        String phone = etDialog02.getText().toString().trim();
        String address = etDialog03.getText().toString().trim();

        String addressProvince = tv.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请选择邮寄地址", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请填写收件人姓名", Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isPhone(phone)) {
            Toast.makeText(this, "请填写收件人正确的手机号", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请填写收件人详细地址", Toast.LENGTH_SHORT).show();
        } else {
            mHashMap.put("address", addressProvince.concat(address));
            mHashMap.put("name", name);
            mHashMap.put("phone", phone);
            createDelivery();
        }
    }

    private void createDelivery() {

        HttpHelperLib.getInstance().net(new BacHttpBean()
                .setActionType(0)
                .setMethodName("CREATE_DELIVERY")
                .put("login_phone", com.bac.commonlib.utils.str.StringUtil.decode(getBaseContext(),"bac_l",getBaseContext().getString(R.string.seed_num) + Integer.valueOf(BuildConfig.XX) + BuildConfig.appKeySeed + BuildConfig.appKeySeed2))
                .put("order_id", mBean.getOrder_id())
                .put("name", mHashMap.get("name"))
                .put("phone", mHashMap.get("phone"))
                .put("address", mHashMap.get("address"))
                .put("province", mHashMap.get("province"))
                .put("province_name", mHashMap.get("province_name"))
                .put("city", mHashMap.get("city"))
                .put("city_name", mHashMap.get("city_name"))
                .put("area", mHashMap.get("area"))
                .put("area_name", mHashMap.get("area_name")),new AlertDialog.Builder(this).create(),null,true,null)
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {
                        Map<String, Object> map = mapList.get(_0);
                        if (map != null) {
                            int code = (int) map.get("code");
                            if (code == -2) {
                                Toast.makeText(InsuranceUploadAddress.this, StringUtil.isNullOrEmpty(map.get("msg")), Toast.LENGTH_SHORT).show();
                            } else if (code == 0) {
                                //支付
                                new AlertDialog.Builder(InsuranceUploadAddress.this).setTitle("提示")
                                        .setMessage("信息提交成功！")
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (mAlter) {
                                                    InsuranceUploadAddress.this.onBackPressed();
                                                } else {
                                                    goToHome();
                                                }

                                            }
                                        })
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // 回主页面
                                                if (mAlter) {
                                                    InsuranceUploadAddress.this.onBackPressed();
                                                } else {
                                                    goToHome();
                                                }
                                            }

                                        })
                                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                if (mAlter) {
                                                    InsuranceUploadAddress.this.onBackPressed();
                                                } else {
                                                    goToHome();
                                                }
                                            }
                                        })
                                        .show();
                            }
                        }
                    }
                });
    }

    private void goToHome() {
        Intent intent = new Intent("com.vrphogame.thyroidapp0716.ACTION_START");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void initData() {
        mBean = getIntent().getParcelableExtra("bean");
        mAlter = getIntent().getBooleanExtra("alter", false);

        getLatestAddress();
        getAddressCode();

    }

    private void getLatestAddress() {

        HttpHelperLib.getInstance().net(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_DELIVERY_INFO")
                .put("login_phone", com.bac.commonlib.utils.str.StringUtil.decode(getBaseContext(),"bac_l",getBaseContext().getString(R.string.seed_num) + Integer.valueOf(BuildConfig.XX) + BuildConfig.appKeySeed + BuildConfig.appKeySeed2)),new AlertDialog.Builder(this).create(),null,true,null)
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        if (maps != null) {
                            if (maps.size() > 0) {
                                //历史地址
                                //附加
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("flag", 1234);

                                maps.add(hashMap);

                                //默认选中
                                for (int i = 0; i < maps.size(); i++) {
                                    Map<String, Object> map = maps.get(i);
                                    if (i == 0) {
                                        map.put("isSelect", true);
                                    } else {
                                        map.put("isSelect", false);
                                    }
                                }

                                mLatestAdapter = new LatestAdapter(R.layout.insurance_choose_plan_item, maps);
                                mLatestRv.setAdapter(mLatestAdapter);

                                //显示
                                mPopupWindow2 = new PopupWindow(mLatestLayout,
                                        ViewGroup.LayoutParams.MATCH_PARENT, mLl.getHeight());
                                // 设置获取焦点
                                mPopupWindow2.setFocusable(true);
                                // 设置边缘点击收起
                                mPopupWindow2.setOutsideTouchable(true);
                                mPopupWindow2.setBackgroundDrawable(new ColorDrawable());
                                mPopupWindow2.showAtLocation(mLl, Gravity.BOTTOM, 0, 0);
                                mTvAddress.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    private void getAddressCode() {


        HttpHelperLib.getInstance().net(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_ALL_AREAS")
                .put("login_phone",  com.bac.commonlib.utils.str.StringUtil.decode(getBaseContext(),"bac_l",getBaseContext().getString(R.string.seed_num) + Integer.valueOf(BuildConfig.XX) + BuildConfig.appKeySeed + BuildConfig.appKeySeed2))
                .put("province_code", "320000"),new AlertDialog.Builder(this).create(),null,true,null)
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .map(new Func1<List<Map<String, Object>>, List<InsuranceAddressBean>>() {
                    @Override
                    public List<InsuranceAddressBean> call(List<Map<String, Object>> mapList) {
                        String areas = StringUtil.isNullOrEmpty(mapList.get(0).get("areas"));
                        return JSON.parseObject(areas, new TypeReference<List<InsuranceAddressBean>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<InsuranceAddressBean>>() {
                    @Override
                    public void call(List<InsuranceAddressBean> insuranceAddressBeenList) {
                        addressPopupView.setPopupData(insuranceAddressBeenList);
                    }
                });

    }


}
