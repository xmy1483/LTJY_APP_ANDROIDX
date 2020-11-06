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
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.module.insurance.dao.InsuranceDataBaseDao;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceCarTypeBean;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceHomeBean;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.utils.tools.Util;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.view.CanClearEditText;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.commonlib.utils.str.AllCapTransformationMethod;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.bac.bacplatform.old.module.insurance.InsuranceChooseCar2.REQUEST_CHOOSE_CAR;
import static com.bac.bacplatform.utils.str.StringUtil.md5;
import static com.bac.bacplatform.utils.tools.CountDown.format2;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.insurance
 * 创建人：Wjz
 * 创建时间：2016/11/23
 * 类描述：
 */

public class InsuranceQuerySuccess extends InsuranceActivity {


    private Button mBtn_insurance_home;
    private TextView tvInsuranceHomeCardNum;

    private CanClearEditText vinCode;
    private TextView tvAlert2;
    private TextView tvAlert1;
    private CanClearEditText etInsuranceHomeEngineId;
    private TextView tvAlert3;

    private TextView tvRegisterDate;
    private TextView etInsuranceHomeEngineName;
    private TextView etInsuranceHomeId;
    private InsuranceHomeBean mBean;
    private String mMd5;

    private PopupWindow mMPopupWindow;
    private ImageView mIv;
    private CanClearEditText mCcetNewCar;
    private LinearLayout mLlNewCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBean = getIntent().getParcelableExtra("bean");
        initView2();
        initData();
        initEvent();
    }

    @Override
    protected void onDestroy() {
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

        setContentView(R.layout.insurance_query_success_activity);

        initToolBar(mBean.getCar_license_no());

        mBtn_insurance_home = (Button) findViewById(R.id.btn_insurance_home);
        //请对照您的《车辆行驶证》确认信息一致
        tvInsuranceHomeCardNum = (TextView) findViewById(R.id.tv_insurance_home_card_num);


        vinCode = (CanClearEditText) findViewById(R.id.vin_code);
        vinCode.setTransformationMethod(new AllCapTransformationMethod(true));
        tvAlert2 = (TextView) findViewById(R.id.tv_alert_2);
        tvAlert1 = (TextView) findViewById(R.id.tv_alert_1);
        etInsuranceHomeEngineId = (CanClearEditText) findViewById(R.id.et_insurance_home_engine_id);
        etInsuranceHomeEngineId.setTransformationMethod(new AllCapTransformationMethod(true));
        tvAlert3 = (TextView) findViewById(R.id.tv_alert_3);
        tvRegisterDate = (TextView) findViewById(R.id.tv_register_date);
        etInsuranceHomeEngineName = (TextView) findViewById(R.id.et_insurance_home_engine_name);
        etInsuranceHomeId = (TextView) findViewById(R.id.et_insurance_home_id);

        mLlNewCar = (LinearLayout) findViewById(R.id.ll_new_car);
        mCcetNewCar = (CanClearEditText) findViewById(R.id.cet);


        //提示框
        View failDialog = View.inflate(InsuranceQuerySuccess.this, R.layout.insurance_fail_dialog, null);
        mIv = (ImageView) failDialog.findViewById(R.id.iv);

        mMPopupWindow = new PopupWindow(failDialog, Util.getWidth(InsuranceQuerySuccess.this), Util.getHeight(InsuranceQuerySuccess.this) / 3);
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
        /*正常开启*/

        if (mBean == null) {
            getCacheData();
        }

        tvInsuranceHomeCardNum.setText(mBean.getVehicle_name());
        vinCode.setText(mBean.getVin_code());
        etInsuranceHomeEngineId.setText(mBean.getEngine_no());


        long regist_date = mBean.getRegist_date();
        if (regist_date <= 0) {
            regist_date = new Date().getTime();
        }
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(regist_date);
        d = instance.get(Calendar.DAY_OF_MONTH);
        m = instance.get(Calendar.MONTH);
        y = instance.get(Calendar.YEAR);

        StringBuilder sb = new StringBuilder();
        sb.append(y).append("/").append(m + 1).append("/").append(d);

        tvRegisterDate.setText(format2.format(mBean.getRegist_date()));

        etInsuranceHomeEngineName.setText(mBean.getCar_owner_name());
        etInsuranceHomeId.setText(mBean.getIdcard_no());

    }

    private int y;
    private int m;
    private int d;

    private void initEvent() {

        tvInsuranceHomeCardNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(InsuranceQuerySuccess.this, InsuranceChooseCar2.class), REQUEST_CHOOSE_CAR);
                overridePendingTransition(R.anim.cl_slide_right_in, R.anim.cl_slide_left_out);
            }
        });

        tvRegisterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(InsuranceQuerySuccess.this, new DatePickerDialog.OnDateSetListener() {
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
                        Toast.makeText(InsuranceQuerySuccess.this, "请选择日期", Toast.LENGTH_SHORT).show();
                    }
                });

                datePickerDialog.show();
            }
        });

        /*tvRegisterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarView calendarView = new CalendarView(InsuranceQuerySuccess.this);
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
                new AlertDialog.Builder(InsuranceQuerySuccess.this)
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
                        .show();
            }

        });*/


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

        mBtn_insurance_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoNextActivity();

            }
        });
    }

    private void gotoNextActivity() {

        //获取文字
        String vehicle_name = tvInsuranceHomeCardNum.getText().toString().trim();
        String vin_code = vinCode.getText().toString().trim().toUpperCase();
        String engine_no = etInsuranceHomeEngineId.getText().toString().trim().toUpperCase();
        String newCar = mCcetNewCar.getText().toString().trim();
        if (TextUtils.isEmpty(vehicle_name)) {
            Toast.makeText(this, "请填写品牌型号", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(vin_code)) {
            Toast.makeText(this, "请填写车辆识别代码", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(engine_no)) {
            Toast.makeText(this, "请填写发动机号码", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(InsuranceQuerySuccess.this, "注册日期大于当前日期，请重新选择", Toast.LENGTH_SHORT).show();
                } else {
                    mBean.setVin_code(vin_code);
                    mBean.setEngine_no(engine_no);
                    mBean.setRegist_date(instance.getTimeInMillis());
                    //修改车辆信息接口
                    doNetForUpdate();
                }
            } else {
                Toast.makeText(this, "请选择注册时间", Toast.LENGTH_SHORT).show();
            }

           /* if (mBean.getVin_code().equals(vin_code) && mBean.getEngine_no().equals(engine_no)) {

                Intent intent = new Intent(InsuranceQuerySuccess.this, InsuranceChoosePlan.class);
                intent.putExtra("bean", mBean);

                startActivityIn(intent);
            } else {
                mBean.setVin_code(vin_code);
                mBean.setEngine_no(engine_no);
                //修改车辆信息接口
                doNetForUpdate();
            }*/
            /*if (y > 0 && m >= 0 && d > 0) {//选择注册时间
                Calendar instance  = Calendar.getInstance();
                long     todayTime = instance.getTimeInMillis();
                instance.set(y, m, d);
                long chooseTime = instance.getTimeInMillis();
                if (chooseTime > todayTime) {
                    Toast.makeText(InsuranceQuerySuccess.this, "注册日期大于当前日期，请重新选择", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                }
            } else {
                Toast.makeText(this, "请选择注册时间", Toast.LENGTH_SHORT).show();
                return;
            }*/
        }
    }

    private void doNetForUpdate() {


        HttpHelper.getInstance().bacNet(
                new BacHttpBean()
                        .setActionType(0)
                        .setMethodName("UPDATE_CAR_INFO")
                        .put("login_phone", BacApplication.getLoginPhone())
                        .put("order_id", mBean.getOrder_id())
                        .put("car_id", mBean.getCar_id())
                        .put("task_id", mBean.getTask_id())
                        .put("vin_code", mBean.getVin_code())
                        .put("engine_no", mBean.getEngine_no())
                        .put("car_license_no", mBean.getCar_license_no())
                        .put("regist_date", mBean.getRegist_date())
                        .put("vehicle_name", mBean.getVehicle_name())
                        .put("vehicle_id", mBean.getVehicle_id())
                        //新车发票
                        .put("price", mBean.getPrice()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {
                        Map<String, Object> map = mapList.get(0);
                        if (map != null) {
                            int code = (int) map.get("code");
                            if (code == 0) {

                                boolean is_history = (boolean) map.get("is_history");
                                mBean.setIs_history(is_history);
                                UIUtils.startActivityInAnimAndFinishSelf(activity,new Intent(InsuranceQuerySuccess.this, InsuranceChoosePlan.class).putExtra("bean", mBean));
                       /* if (is_history) {
                            startActivityIn(new Intent(InsuranceQuerySuccess.this, InsuranceAlter4.class).putExtra("bean", mBean));
                        } else {
                            startActivityIn(new Intent(InsuranceQuerySuccess.this, InsuranceChoosePlan.class).putExtra("bean", mBean));
                        }*/
                            } else if (code == -2) {
                                mLlNewCar.setVisibility(View.VISIBLE);
                                Toast.makeText(InsuranceQuerySuccess.this, StringUtil.isNullOrEmpty(map.get("msg")), Toast.LENGTH_SHORT).show();
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
            tvInsuranceHomeCardNum.setText(carBean.getVehicle_name());
        }
    }
}
