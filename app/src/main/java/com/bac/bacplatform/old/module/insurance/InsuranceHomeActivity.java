package com.bac.bacplatform.old.module.insurance;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.main.adapter.NetworkImageHolderView;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity;
import com.bac.bacplatform.old.module.insurance.dao.InsuranceDataBaseDao;
import com.bac.bacplatform.old.module.insurance.domain.AdsBean;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceCityBean;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceHomeBean;
import com.bac.bacplatform.utils.logger.LogUtil;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.view.CanClearEditText;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.services.LocationService;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.commonlib.utils.str.AllCapTransformationMethod;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.permission.RxPermissionImpl;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.bac.bacplatform.R.id.switch_insurance_home_car;
import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.utils.str.StringUtil.md5;
import static com.bac.commonlib.utils.str.StringUtil.isCarCode;


/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.insurance
 * 创建人：Wjz
 * 创建时间：2016/11/22
 * 类描述：_1界面
 */

public class InsuranceHomeActivity extends InsuranceActivity {
    private LocationManager lm;
    private LocationService locationService;
    private int locCount;
    private String mMd5;


    private LinearLayout llInsuranceHomeLoc;
    private TextView tvInsuranceHomeLoc;
    private CanClearEditText etInsuranceHomeCardNum;
    private Switch switchInsuranceHomeNum;
    private CanClearEditText etInsuranceHomeCardName;
    private Switch switchInsuranceHomeCar;
    private RelativeLayout rlInsuranceHomeDate;
    private TextView tvInsuranceHomeDate;
    private LinearLayout mLl_insurance_home_label_4;
    private CanClearEditText etInsuranceHomeCardID;
    private CanClearEditText mCcet05;
    private AppCompatCheckBox mCb_insurance_home;
    private TextView mTv_insurance_home_deal;
    private TextView mTvCity;

    private Button mBtn_insurance_home;

    private List<InsuranceCityBean> mInsuranceCityBeanList;

    private int y;
    private int m;
    private int d;

    private ArrayList<String> adsStrList = new ArrayList<>();
    private List<AdsBean> mAdsList;
    private ConvenientBanner mBanner;
    private LinearLayout chejiahao;

    private boolean isLocal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.insurance_home_activity);
        chejiahao = (LinearLayout) findViewById(R.id.chejiahao);
        chejiahao.setVisibility(View.GONE);
        initToolBar("保险首页");



        String adsString = PreferenceManager.getDefaultSharedPreferences(activity).getString("insuranceString", getResources().getString(R.string.ads_string));
        mAdsList = JSON.parseArray(adsString, AdsBean.class);
        if (mAdsList != null) {
            for (AdsBean adsBean : mAdsList) {
                adsStrList.add(adsBean.getImage_url());
            }
        }

        mBanner = (ConvenientBanner) findViewById(R.id.banner);
        mBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, adsStrList)

                .setPageIndicator(new int[]{R.mipmap.home_banner_next_dot, R.mipmap.home_banner_current_dot});


        mBanner.setOnItemClickListener(new com.bigkoo.convenientbanner.listener.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String http_url = mAdsList.get(position).getHttp_url();
                if (!TextUtils.isEmpty(http_url)) {

                    // 跳转广告详情
                    Intent intentToAgree = new Intent(InsuranceHomeActivity.this,
                            WebAdvActivity.class);
                    intentToAgree.putExtra("title", "活动详情");
                    intentToAgree.putExtra("ads_url", http_url);
                    UIUtils.startActivityInAnim(activity, intentToAgree);
                }
            }
        });


        llInsuranceHomeLoc = (LinearLayout) findViewById(R.id.ll_insurance_home_loc);

        tvInsuranceHomeLoc = (TextView) findViewById(R.id.tv_insurance_home_loc);
        etInsuranceHomeCardNum = (CanClearEditText) findViewById(R.id.et_insurance_home_card_num);
        /*大写*/
        etInsuranceHomeCardNum.setTransformationMethod(new AllCapTransformationMethod(true));

        switchInsuranceHomeNum = (Switch) findViewById(R.id.switch_insurance_home_num);
        etInsuranceHomeCardName = (CanClearEditText) findViewById(R.id.et_insurance_home_card_name);
        etInsuranceHomeCardID = (CanClearEditText) findViewById(R.id.et_insurance_home_card_id);

        etInsuranceHomeCardID.setTransformationMethod(new AllCapTransformationMethod(true));

        switchInsuranceHomeCar = (Switch) findViewById(switch_insurance_home_car);
        rlInsuranceHomeDate = (RelativeLayout) findViewById(R.id.rl_insurance_home_date);

        mCcet05 = (CanClearEditText) findViewById(R.id.ccet_05);
        mCcet05.setTransformationMethod(new AllCapTransformationMethod(true));

        mLl_insurance_home_label_4 = (LinearLayout) findViewById(R.id.ll_insurance_home_label_4);
        tvInsuranceHomeDate = (TextView) findViewById(R.id.tv_insurance_home_date);
        mCb_insurance_home = (AppCompatCheckBox) findViewById(R.id.cb_insurance_home);
        mTv_insurance_home_deal = (TextView) findViewById(R.id.tv_insurance_home_deal);
        mTvCity = (TextView) findViewById(R.id.tv_city);

        mBtn_insurance_home = (Button) findViewById(R.id.btn_insurance_home);
        mBtn_insurance_home.setText("为爱车投保");
        // 判断是否开启定位
        //得到系统的位置服务，判断GPS是否激活
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.alert_title)
                    .setMessage("打开“定位服务”来允许“骆驼加油”确认您的位置")
                    .setNegativeButton(getString(R.string.alert_cancel), null)
                    .setPositiveButton(getString(R.string.alert_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            UIUtils.startActivityInAnim(activity, intent);
                        }
                    }).show();
        }

        initData();
        initEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 百度定位
        // 获取权限
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)&&!isLocal) {

            Observable.just("")
                    .compose(new RxPermissionImpl(this).ensure(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean b) {

                            LogUtil.sf(activity, "b:  " + b);
                            if (b) {
                                // 百度定位
                                locationService = new LocationService(activity);
                                locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                                locationService.registerListener(mListener);
                                locationService.start();// 定位SDK
                            }
                        }
                    });
        }
    }

    @Override
    public void onResume() {
        if (mBanner != null) {
            mBanner.startTurning(7000);
        }

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mBanner != null) {
            //停止翻页
            mBanner.stopTurning();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationService();
    }

    @Override
    protected void onDestroy() {
        /*保存数据*/
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cardNum", etInsuranceHomeCardNum.getText().toString().trim().toUpperCase());
        hashMap.put("cardName", etInsuranceHomeCardName.getText().toString().trim());
        hashMap.put("cardID", etInsuranceHomeCardID.getText().toString().trim());
        hashMap.put("vinCode", mCcet05.getText().toString().trim().toUpperCase());
        String json = String.valueOf(JSON.toJSON(hashMap));
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

    private void initEvent() {
        //是否上牌
        switchInsuranceHomeNum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etInsuranceHomeCardNum.setVisibility(View.INVISIBLE);
                } else {
                    etInsuranceHomeCardNum.setVisibility(View.VISIBLE);
                }
            }
        });

        //折叠过户时间选项
        switchInsuranceHomeCar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mLl_insurance_home_label_4.setVisibility(View.VISIBLE);
                } else {
                    mLl_insurance_home_label_4.setVisibility(View.GONE);
                }
            }
        });

        //日期选择
        rlInsuranceHomeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (y == 0 && m == 0 && d == 0) {

                    Calendar instance = Calendar.getInstance();
                    y = instance.get(Calendar.YEAR);
                    m = instance.get(Calendar.MONTH);
                    d = instance.get(Calendar.DAY_OF_MONTH);
                }

                //日期选择
                rlInsuranceHomeDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (y == 0 && m == 0 && d == 0) {

                            Calendar instance = Calendar.getInstance();
                            y = instance.get(Calendar.YEAR);
                            m = instance.get(Calendar.MONTH);
                            d = instance.get(Calendar.DAY_OF_MONTH);
                        }

                        DatePickerDialog datePickerDialog = new DatePickerDialog(InsuranceHomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                y = year;
                                m = monthOfYear;
                                d = dayOfMonth;

                                StringBuilder sb = new StringBuilder();
                                sb.append(y).append("/").append(m + 1).append("/").append(d);
                                tvInsuranceHomeDate.setText(sb.toString());
                            }
                        }, y, m, d);
                        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                Toast.makeText(InsuranceHomeActivity.this, "请选择日期", Toast.LENGTH_SHORT).show();
                            }
                        });
                        datePickerDialog.show();
                    }
                });
            }
        });

        RxView.clicks(mTv_insurance_home_deal)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        insureAgreement();
                    }
                });

        RxView.clicks(mBtn_insurance_home)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        gotoInsuranceQueryConfirm();
                    }
                });
        RxView.clicks(llInsuranceHomeLoc)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //选择定位城市
                        chooseLocCity();
                    }
                });
    }

    /**
     * 定位失败，或非当地，手动选择城市
     */
    private void chooseLocCity() {

        Intent intent = new Intent(InsuranceHomeActivity.this, InsuranceCity.class);
        startActivityForResult(intent, 1111);
        overridePendingTransition(R.anim.cl_slide_right_in, R.anim.cl_slide_left_out);
    }

    private void initData() {
        getCacheData();
        queryAreas();
    }

    private void gotoInsuranceQueryConfirm() {
        if (!mCb_insurance_home.isChecked()) {
            Toast.makeText(this, "请勾选骆驼加油车险服务协议", Toast.LENGTH_SHORT).show();
        } else {
            //定位
            String city = tvInsuranceHomeLoc.getText().toString().trim();
            String car_license_no = etInsuranceHomeCardNum.getText().toString().trim().toUpperCase();
            String car_owner_name = etInsuranceHomeCardName.getText().toString().trim();
            String idcard_no = etInsuranceHomeCardID.getText().toString().trim().toUpperCase();
            String vin_code = mCcet05.getText().toString().trim().toUpperCase();
            String cityAreaCode = mTvCity.getText().toString().trim();

            boolean is_transfer_car = switchInsuranceHomeCar.isChecked();
            long transfer_date = 0;

            if (TextUtils.isEmpty(city) || !city.contains("市")) {
                Toast.makeText(this, "请选择投保城市", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(car_license_no) || !isCarCode(car_license_no)) {
                //车牌号
                Toast.makeText(this, "请输入正确的车牌号码", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(car_owner_name)) {
                //姓名
                Toast.makeText(this, "请输入正确的车主姓名", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(idcard_no) || idcard_no.length() < 18) {
                //车主身份证
                Toast.makeText(this, "请输入正确的车主身份证", Toast.LENGTH_SHORT).show();
            }
            /*else if (TextUtils.isEmpty(vin_code) || vin_code.length() < 17) {
                Toast.makeText(this, "请输入正确的车架号", Toast.LENGTH_SHORT).show();
            } */
            else {
                if (is_transfer_car) {//过户标志
                    if (y > 0 && m >= 0 && d > 0) {//选择过户时间
                        Calendar instance = Calendar.getInstance();
                        long today = instance.getTimeInMillis();
                        instance.set(y, m, d);
                        transfer_date = instance.getTimeInMillis();
                        if (transfer_date > today) {
                            Toast.makeText(InsuranceHomeActivity.this, "过户日期大于当前日期，请重新选择", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(this, "请选择过户时间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                doNet(city, cityAreaCode.concat(car_license_no), car_owner_name, idcard_no, is_transfer_car, transfer_date, vin_code);
            }
        }
    }

    private void doNet(String city, String car_license_no, String car_owner_name, String idcard_no, boolean is_transfer_car, long transfer_date, final String vin_code) {

        String cityCode = null;
        if (mInsuranceCityBeanList != null) {
            for (InsuranceCityBean insuranceCityBean : mInsuranceCityBeanList) {
                InsuranceCityBean.CitysBean citysBean = insuranceCityBean.getCitys().get(0);
                if (citysBean.getCityName().equals(city)) {
                    cityCode = citysBean.getCity();
                    break;
                }
            }
        }

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_CAR_INFO")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("city", cityCode)
                .put("car_license_no", car_license_no)
                .put("car_owner_name", car_owner_name)
                .put("is_transfer_car", is_transfer_car)
                .put("transfer_date", transfer_date)
                .put("idcard_type", 0)
                .put("idcard_no", idcard_no)
                .put("vin_code", vin_code))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<InsuranceHomeBean>>() {
                    @Override
                    public List<InsuranceHomeBean> call(String s) {

                        return JSON.parseObject(s, new TypeReference<List<InsuranceHomeBean>>() {
                        }.getType());

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<InsuranceHomeBean>>() {
                    @Override
                    public void call(List<InsuranceHomeBean> insuranceHomeBeen) {
                        InsuranceHomeBean insuranceBean = insuranceHomeBeen.get(_0);
                        if (insuranceBean != null) {
                            insuranceBean.setVin_code_2(vin_code);

                            /*insuranceBean.setCar_owner_name(car_owner_name);
                                insuranceBean.setIs_transfer_car(is_transfer_car);
                                insuranceBean.setCity(cityCode);
                                insuranceBean.setTransfer_date(transfer_date);
                            */
                            if (insuranceBean.getCode() == 0 && insuranceBean.getTask_id() != null) {


                                UIUtils.startActivityInAnim(activity, new Intent(InsuranceHomeActivity.this, InsuranceChoosePlan.class)
                                        .putExtra("bean", insuranceBean));


                            } else if (insuranceBean.getCode() == -2) {

                                Toast.makeText(InsuranceHomeActivity.this, insuranceBean.getMsg(), Toast.LENGTH_SHORT).show();
/*

                                UIUtils.startActivityInAnim(activity,new Intent(InsuranceHomeActivity.this, InsuranceQueryFail.class)
                                        .putExtra("bean", insuranceBean));
*/


                            }

                        }

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 1111 && resultCode == 1000) {
                String city = data.getStringExtra("city");
                if (!TextUtils.isEmpty(city)) {
                    tvInsuranceHomeLoc.setText(city.concat("市"));
                    mTvCity.setText(getCityAreaCode(city.concat("市")));
                }
            }
        }
    }


    private void insureAgreement() {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_START_PARAM")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("param_type", "INSURE_AGREEMENT"))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        if (maps != null) {
                            for (Map<String, Object> map : maps) {
                                if ("AGREEMENT_URL".equals(map.get("param_key"))) {
                                    // 跳转广告详情
                                    Intent intentToAgree = new Intent(InsuranceHomeActivity.this,
                                            WebAdvActivity.class);
                                    intentToAgree.putExtra("title", "骆驼加油车险服务协议");
                                    intentToAgree.putExtra("ads_url", map.get("param_value") + "");
                                    UIUtils.startActivityInAnim(activity, intentToAgree);
                                }
                            }
                        } else {
                            Toast.makeText(InsuranceHomeActivity.this, "《骆驼加油车险服务协议》", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void queryAreas() {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_AREAS")
                .put("login_phone", BacApplication.getLoginPhone()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<InsuranceCityBean>>() {
                    @Override
                    public List<InsuranceCityBean> call(String s) {

                        List<Map<String, Object>> mapList = parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                        }.getType());
                        return parseObject(String.valueOf(mapList.get(0).get("agreement_areas")), new TypeReference<List<InsuranceCityBean>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<InsuranceCityBean>>() {
                    @Override
                    public void call(List<InsuranceCityBean> cityBeen) {
                        if (cityBeen != null) {

                            mInsuranceCityBeanList = cityBeen;

                        }
                    }
                });

    }

    /**
     * 获取缓存数据
     */
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
            HashMap<String, String> hm = parseObject(value, new TypeReference<HashMap<String, String>>() {
            }.getType());

            String cardNum = hm.get("cardNum");
            String cardName = hm.get("cardName");
            String cardID = hm.get("cardID");
            String vinCode = hm.get("vinCode");

            etInsuranceHomeCardNum.setText(cardNum);
            etInsuranceHomeCardName.setText(cardName);
            etInsuranceHomeCardID.setText(cardID);
            mCcet05.setText(vinCode);

        }
    }

    /**
     * 停止定位
     */
    private void stopLocationService() {

        if (locationService != null) {
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
            LogUtil.sf(this, "停止定位");
        }
    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法，
     */
    private BDLocationListener mListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {

            Observable.just(location)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<BDLocation>() {
                        @Override
                        public void call(BDLocation bdLocation) {
                            isLocal=true;
                            if (null != bdLocation && bdLocation.getLocType() != BDLocation.TypeServerError&&bdLocation.getCity()!=null) {
                                String city = bdLocation.getCity();
                                LogUtil.sf(activity, "定位城市：" + city);

                                tvInsuranceHomeLoc.setText(city);
                                mTvCity.setText(getCityAreaCode(city));

                                stopLocationService();
                            } else {
                                if (locCount < 3) {
                                    locCount++;
                                    locationService.start();// 定位SDK
                                } else {
                                    // 定位3次失败自动设置为南京市
                                    tvInsuranceHomeLoc.setText("南京市");
                                    mTvCity.setText(getCityAreaCode("南京市"));
                                    locCount = 0;
                                    stopLocationService();
                                }
                            }
                        }
                    });
        }

    };

    private String getCityAreaCode(String city) {

        String areaCode = "苏";

       /* if ("南京市".equals(city)) {
            areaCode = "苏A";
        } else if ("无锡市".equals(city)) {
            areaCode = "苏B";
        } else if ("徐州市".equals(city)) {
            areaCode = "苏C";
        } else if ("常州市".equals(city)) {
            areaCode = "苏D";
        } else if ("苏州市".equals(city)) {
            areaCode = "苏E";
        } else if ("南通市".equals(city)) {
            areaCode = "苏F";
        } else if ("连云港市".equals(city)) {
            areaCode = "苏G";
        } else if ("淮安市".equals(city)) {
            areaCode = "苏H";
        } else if ("盐城市".equals(city)) {
            areaCode = "苏J";
        } else if ("扬州市".equals(city)) {
            areaCode = "苏K";
        } else if ("镇江市".equals(city)) {
            areaCode = "苏L";
        } else if ("泰州市".equals(city)) {
            areaCode = "苏M";
        } else if ("宿迁市".equals(city)) {
            areaCode = "苏N";
        }
*/
        return areaCode;
    }
}
