package com.bac.bacplatform.old.module.insurance;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.bac.bacplatform.old.module.insurance.dao.InsuranceDataBaseDao;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceCarTypeBean;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceHomeBean;
import com.bac.bacplatform.utils.tools.Util;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.view.CanClearEditText;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.str.AllCapTransformationMethod;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.old.module.insurance.InsuranceChooseCar2.REQUEST_CHOOSE_CAR;
import static com.bac.bacplatform.utils.str.StringUtil.isPhone;
import static com.bac.bacplatform.utils.str.StringUtil.md5;
import static com.bac.bacplatform.utils.tools.CountDown.format2;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.insurance
 * 创建人：Wjz
 * 创建时间：2016/11/23
 * 类描述：
 */

public class InsuranceQueryFail extends InsuranceActivity {

    private Button mBtn_insurance_home;

    public static final int UPLOAD_ID = 10001; // 选择
    public static final int UPLOAD_DRIVE = 10002; // 选择


    private TextView etInsuranceHomeCardNum;
    private TextView tvAlert1;
    private CanClearEditText vinCode;
    private TextView tvAlert2;
    private CanClearEditText etInsuranceHomeEngineId;
    private TextView tvAlert3;

    private TextView tvRegisterDate;
    private TextView etInsuranceHomeEngineName;
    private TextView etInsuranceHomeId;

    private InsuranceHomeBean mBean;
    private String mMd5;
    private CanClearEditText mEtInsuranceHomeEnginePhone;
    private CanClearEditText mCet;
    private PopupWindow mMPopupWindow;
    private ImageView mIv;
    private LinearLayout llNewCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView2();
        initData();
        initEvent();
    }

    @Override
    protected void onDestroy() {

        //获取文字
        mBean.setVehicle_name(etInsuranceHomeCardNum.getText().toString().trim());
        mBean.setVin_code(vinCode.getText().toString().trim());
        mBean.setEngine_no(etInsuranceHomeEngineId.getText().toString().trim());
        mBean.setPhone(mEtInsuranceHomeEnginePhone.getText().toString().trim());

        Calendar instance = Calendar.getInstance();
        if (y > 0 && m >= 0 && d > 0) {
            instance.set(y, m, d);
            mBean.setRegist_date(instance.getTimeInMillis());
        }

        /*保存数据*/
        String json = String.valueOf(JSON.toJSON(mBean));
        String curMd5 = md5(json);

        if (!curMd5.equals(mMd5)) {
            //更新内容
            InsuranceDataBaseDao.replaceContent(writableDatabase, this.getClass().getName(), json, BacApplication.getLoginPhone(), curMd5);
            //更新索引
            refreshIndex();
        }
        super.onDestroy();
    }


    @Override
    protected void initAbleDatabase() {
        super.initAbleDatabase();
        readableDatabase = localDataDbHelper.getReadableDatabase();
        writableDatabase = localDataDbHelper.getWritableDatabase();
    }

    private void initView2() {
        setContentView(R.layout.insurance_query_fail_activity);

        mBean = getIntent().getParcelableExtra("bean");

        initToolBar(mBean.getCar_license_no());

        llNewCar = (LinearLayout) findViewById(R.id.ll_new_car);


        mBtn_insurance_home = (Button) findViewById(R.id.btn_insurance_home);

        //请对照您的《车辆行驶证》确认信息一致

        etInsuranceHomeCardNum = (TextView) findViewById(R.id.et_insurance_home_card_num);
        tvAlert1 = (TextView) findViewById(R.id.tv_alert_1);
        vinCode = (CanClearEditText) findViewById(R.id.vin_code);
        //大写
        vinCode.setTransformationMethod(new AllCapTransformationMethod(true));

        tvAlert2 = (TextView) findViewById(R.id.tv_alert_2);
        etInsuranceHomeEngineId = (CanClearEditText) findViewById(R.id.et_insurance_home_engine_id);
        //大写
        etInsuranceHomeEngineId.setTransformationMethod(new AllCapTransformationMethod(true));

        tvAlert3 = (TextView) findViewById(R.id.tv_alert_3);

        tvRegisterDate = (TextView) findViewById(R.id.tv_register_date);
        etInsuranceHomeEngineName = (TextView) findViewById(R.id.et_insurance_home_engine_name);
        etInsuranceHomeId = (TextView) findViewById(R.id.et_insurance_home_id);

        mEtInsuranceHomeEnginePhone = (CanClearEditText) findViewById(R.id.et_insurance_home_engine_phone);

        mCet = (CanClearEditText) findViewById(R.id.cet);

        //提示框
        View failDialog = View.inflate(InsuranceQueryFail.this, R.layout.insurance_fail_dialog, null);
        mIv = (ImageView) failDialog.findViewById(R.id.iv);
        mMPopupWindow = new PopupWindow(failDialog, Util.getWidth(InsuranceQueryFail.this), Util.getHeight(InsuranceQueryFail.this) / 3);
        // 设置获取焦点
        mMPopupWindow.setFocusable(true);
        // 设置边缘点击收起
        mMPopupWindow.setOutsideTouchable(true);
        mMPopupWindow.setBackgroundDrawable(new ColorDrawable());

    }

    private void getCacheData() {
        String value = null;
        Cursor cursor = InsuranceDataBaseDao.queryContent(readableDatabase, this.getClass().getName(), BacApplication.getLoginPhone());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            value = cursor.getString(cursor.getColumnIndex(Constants.ContentTable.VALUE));
            mMd5 = cursor.getString(cursor.getColumnIndex(Constants.ContentTable._MD5));

        }
        cursor.close();

        if (!TextUtils.isEmpty(value)) {
            mBean = JSON.parseObject(value, InsuranceHomeBean.class);
        }
    }

    private void initData() {


        if (mBean == null) {
            getCacheData();
        }

        if (!TextUtils.isEmpty(mBean.getVehicle_id())) {
            etInsuranceHomeCardNum.setText(mBean.getVehicle_name());
        }

        vinCode.setText(mBean.getVin_code());
        etInsuranceHomeEngineId.setText(mBean.getEngine_no());

        mEtInsuranceHomeEnginePhone.setText(mBean.getPhone());

        etInsuranceHomeEngineName.setText(mBean.getCar_owner_name());

        vinCode.setText(mBean.getVin_code_2());
        etInsuranceHomeId.setText(mBean.getIdcard_no());

        long regist_date = mBean.getRegist_date();
        if (regist_date <= 0) {
            regist_date = new Date().getTime();
        }
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(regist_date);
        d = instance.get(Calendar.DAY_OF_MONTH);
        m = instance.get(Calendar.MONTH);
        y = instance.get(Calendar.YEAR);
        tvRegisterDate.setText(format2.format(regist_date));
    }

    private int y;
    private int m;
    private int d;

    private void initEvent() {
        etInsuranceHomeCardNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(InsuranceQueryFail.this, InsuranceChooseCar2.class), REQUEST_CHOOSE_CAR);
                overridePendingTransition(R.anim.cl_slide_right_in, R.anim.cl_slide_left_out);
            }
        });

        tvAlert1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIv.setImageResource(R.mipmap.regist_date);
                mMPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            }
        });

        tvAlert2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIv.setImageResource(R.mipmap.vin_code);
                mMPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            }
        });

        tvAlert3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIv.setImageResource(R.mipmap.engine_no);
                mMPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            }
        });

        tvRegisterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(InsuranceQueryFail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        y = year;
                        m = monthOfYear;
                        d = dayOfMonth;

                        StringBuilder sb = new StringBuilder();
                        sb.append(y).append("/").append(m + 1).append("/").append(d);
                        tvRegisterDate.setText(sb.toString());
                    }
                }, y, m, d);
                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(InsuranceQueryFail.this, "请选择日期", Toast.LENGTH_SHORT).show();
                    }
                });

                datePickerDialog.show();


                /*CalendarView calendarView = new CalendarView(InsuranceQueryFail.this);
                Calendar     instance     = Calendar.getInstance();
                if (y > 0 && m >= 0 && d > 0) {
                    instance.set(y, m, d);
                    long timeInMillis = instance.getTimeInMillis();
                    LogUtils.sf("时间timeInMillis   " + timeInMillis);
                    calendarView.setDate(timeInMillis);
                }

                calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                    y = year;
                    m = month;
                    d = dayOfMonth;
                    LogUtils.sf(y + "/" + m + "/" + d);
                });

                calendarView.setMaxDate(new Date().getTime() + 24 * 60 * 60 * 1000);
                //日期选择器
                new AlertDialog.Builder(InsuranceQueryFail.this)
                        .setView(calendarView)
                        .setCancelable(false)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", (dialog, which) -> {

                            if (y == 0 && m == 0 & d == 0) {
                                d = instance.get(Calendar.DAY_OF_MONTH);
                                m = instance.get(Calendar.MONTH);
                                y = instance.get(Calendar.YEAR);
                            }

                            StringBuilder sb = new StringBuilder();
                            sb.append(y).append("/").append(m + 1).append("/").append(d);
                            tvRegisterDate.setText(sb.toString());
                        })
                        .show();*/
            }

        });

        mBtn_insurance_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoNextActivity();

            }
        });

    }

    private void gotoNextActivity() {

        //获取文字
        String vehicle_name = etInsuranceHomeCardNum.getText().toString().trim();
        String vin_code = vinCode.getText().toString().trim().toUpperCase();
        String engine_no = etInsuranceHomeEngineId.getText().toString().trim().toUpperCase();

        String newCar = mCet.getText().toString().trim();

        //手机
        String phone = mEtInsuranceHomeEnginePhone.getText().toString().trim();


        if (TextUtils.isEmpty(vehicle_name)) {
            Toast.makeText(this, "请选择品牌型号", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(vin_code)) {
            Toast.makeText(this, "请填写车辆识别代码", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(engine_no)) {
            Toast.makeText(this, "请填写发动机号码", Toast.LENGTH_SHORT).show();
        } else if (!isPhone(phone)) {
            Toast.makeText(this, "请输入车主手机号", Toast.LENGTH_SHORT).show();
        } else {

            if (!TextUtils.isEmpty(newCar)) {
                mBean.setPrice(Double.parseDouble(newCar));
            }


            if (y > 0 && m >= 0 && d > 0) {//选择注册时间
                Calendar instance = Calendar.getInstance();
                instance.set(y, m, d);
                long chooseTime = instance.getTimeInMillis();
                long todayTime = new Date().getTime();
                if (chooseTime > todayTime) {
                    Toast.makeText(InsuranceQueryFail.this, "注册日期大于当前日期，请重新选择", Toast.LENGTH_SHORT).show();
                } else {
                    //品牌型号
                    mBean.setVehicle_name(vehicle_name);
                    //车辆识别代码
                    mBean.setVin_code(vin_code);
                    //发动机号码
                    mBean.setEngine_no(engine_no);
                    //获取过户时间
                    mBean.setRegist_date(instance.getTimeInMillis());

                    mBean.setPhone(phone);

                    doNet();

                }
            } else {
                Toast.makeText(this, "请选择注册时间", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void doNet() {
        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("CREATE_CAR_INFO")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("order_id", mBean.getOrder_id())
                .put("car_owner_id", mBean.getCar_owner_id())
                .put("car_id", mBean.getCar_id())
                .put("car_license_no", mBean.getCar_license_no())
                .put("vehicle_name", mBean.getVehicle_name())
                .put("vin_code", mBean.getVin_code())
                .put("engine_no", mBean.getEngine_no())
                .put("regist_date", mBean.getRegist_date())
                .put("vehicle_id", mBean.getVehicle_id())
                .put("is_new", mBean.is_new())
                .put("price", mBean.getPrice())
                .put("is_transfer_car", mBean.is_transfer_car())
                .put("transfer_date", mBean.getTransfer_date())
                .put("car_owner_name", mBean.getCar_owner_name())
                .put("idcard_type", 0)
                .put("phone", Long.parseLong(mBean.getPhone()))
                .put("idcard_no", mBean.getIdcard_no()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, ArrayList<InsuranceHomeBean>>() {
                    @Override
                    public ArrayList<InsuranceHomeBean> call(String s) {
                        return JSON.parseObject(s, new TypeReference<ArrayList<InsuranceHomeBean>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<InsuranceHomeBean>>() {
                    @Override
                    public void call(ArrayList<InsuranceHomeBean> list) {

                        InsuranceHomeBean insuranceHomeBean = list.get(_0);

                        if (insuranceHomeBean != null) {
                            if (insuranceHomeBean.getCode() == -2) {
                                Toast.makeText(InsuranceQueryFail.this, insuranceHomeBean.getMsg(), Toast.LENGTH_SHORT).show();
                                llNewCar.setVisibility(View.VISIBLE);
                            } else if (insuranceHomeBean.getCode() == 0) {
                                UIUtils.startActivityInAnimAndFinishSelf(activity, new Intent(InsuranceQueryFail.this, InsuranceChoosePlan.class)
                                        .putExtra("bean", mBean));
                            }
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_CAR && null != data) {
            InsuranceCarTypeBean.CarModelInfosBean carBean = data.getParcelableExtra("carBean");
            mBean.setVehicle_id(carBean.getVehicle_id());
            etInsuranceHomeCardNum.setText(carBean.getVehicle_name());
        }
    }

}
