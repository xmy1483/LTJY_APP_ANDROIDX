package com.bac.bacplatform.old.module.insurance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.insurance.adapter.InsuranceChoosePlanAdapter;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceCarTypeBean;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceHomeBean;
import com.bac.bacplatform.view.CanClearEditText;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.baidu.location.g.a.i;


/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.insurance
 * 创建人：Wjz
 * 创建时间：2016/11/25
 * 类描述：
 */

public class InsuranceChooseCar2 extends SuperActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    public static final int REQUEST_CHOOSE_CAR = 1904;
    private RecyclerView rvInsuranceChoosePlan;
    private Button btnInsuranceChoosePlan;

    private InsuranceChoosePlanAdapter mInsuranceChoosePlanAdapter;
    private InsuranceHomeBean mBean;

    private LinearLayout llChooseCar2Top;
    private LinearLayout llChooseCar2Bottom;

    private CanClearEditText searchCar;
    private TextView searchCarConfirm;
    private List<InsuranceCarTypeBean.CarModelInfosBean> carModelInfos = new ArrayList<>();
    private String mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBean = getIntent().getParcelableExtra("bean");
        initView2();
        initData();
        initEvent();
    }

    private void initView2() {
        setContentView(R.layout.insurance_choose_car_2_activity);
        initToolBar("请选择投保方案");

        searchCar = (CanClearEditText) findViewById(R.id.search_car);
        searchCarConfirm = (TextView) findViewById(R.id.search_car_confirm);

        llChooseCar2Top = (LinearLayout) findViewById(R.id.ll_choose_car_2_top);
        llChooseCar2Bottom = (LinearLayout) findViewById(R.id.ll_choose_car_2_bottom);
        llChooseCar2Top.setVisibility(View.VISIBLE);
        llChooseCar2Bottom.setVisibility(View.GONE);

        rvInsuranceChoosePlan = (RecyclerView) findViewById(R.id.rv_insurance_choose_plan);
        btnInsuranceChoosePlan = (Button) findViewById(R.id.btn_insurance_choose_plan);

        btnInsuranceChoosePlan.setText("确定");
    }

    private void initData() {

        rvInsuranceChoosePlan.setLayoutManager(new LinearLayoutManager(InsuranceChooseCar2.this));
        mInsuranceChoosePlanAdapter = new InsuranceChoosePlanAdapter(R.layout.insurance_choose_plan_item, carModelInfos);
        mInsuranceChoosePlanAdapter.openLoadAnimation();
        mInsuranceChoosePlanAdapter.setOnLoadMoreListener(this, rvInsuranceChoosePlan);
        mCurrentCounter = mInsuranceChoosePlanAdapter.getData().size();
        mInsuranceChoosePlanAdapter.setOnLoadMoreListener(this);
        rvInsuranceChoosePlan.setAdapter(mInsuranceChoosePlanAdapter);

    }

    private void doNetGetCarType(String search, int page) {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_CAR_MODEL_INFO")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("page_size", PAGE_SIZE)
                .put("page_num", page)
                .put("vehicle_name", search))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<InsuranceCarTypeBean>>() {
                    @Override
                    public List<InsuranceCarTypeBean> call(String s) {
                        return JSON.parseObject(s, new TypeReference<List<InsuranceCarTypeBean>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<InsuranceCarTypeBean>>() {
                    @Override
                    public void call(List<InsuranceCarTypeBean> list) {
                        InsuranceCarTypeBean bean = list.get(0);

                        if (bean != null) {
                            TOTAL_COUNTER = bean.getTotal_size();
                            if (TOTAL_COUNTER > 0) {
                                llChooseCar2Top.setVisibility(View.GONE);
                                llChooseCar2Bottom.setVisibility(View.VISIBLE);

                                mInsuranceChoosePlanAdapter.addData(bean.getCarModelInfos());

                                mCurrentCounter = mInsuranceChoosePlanAdapter.getData().size();
                                mInsuranceChoosePlanAdapter.loadMoreComplete();

                            } else {
                                Toast.makeText(InsuranceChooseCar2.this, "查询失败", Toast.LENGTH_SHORT).show();
                                llChooseCar2Top.setVisibility(View.VISIBLE);
                                llChooseCar2Bottom.setVisibility(View.GONE);
                            }
                        }
                    }
                });

    }


    private void initEvent() {

        RxView.clicks(searchCarConfirm)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS,RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mSearch = searchCar.getText().toString().trim();

                        if (TextUtils.isEmpty(mSearch)) {
                            Toast.makeText(InsuranceChooseCar2.this, "请输入车型关键字", Toast.LENGTH_SHORT).show();
                        } else {
                            // 还原界面
                            llChooseCar2Top.setVisibility(View.VISIBLE);
                            llChooseCar2Bottom.setVisibility(View.GONE);
                            //还原数据
                            pageCounter = 1;
                            mInsuranceChoosePlanAdapter.getData().clear();
                            doNetGetCarType(mSearch, pageCounter);
                            //隐藏键盘
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(searchCar.getWindowToken(), 0); //关->开 ，开->关
                        }
                    }
                });

        rvInsuranceChoosePlan.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArrayList<InsuranceCarTypeBean.CarModelInfosBean> data = (ArrayList<InsuranceCarTypeBean.CarModelInfosBean>) baseQuickAdapter.getData();
                for (InsuranceCarTypeBean.CarModelInfosBean insuranceChoosePlanBean : data) {
                    insuranceChoosePlanBean.setSelect(0);
                }
                data.get(i).setSelect(1);
                baseQuickAdapter.notifyDataSetChanged();
            }
        });
        btnInsuranceChoosePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<InsuranceCarTypeBean.CarModelInfosBean> data = (ArrayList<InsuranceCarTypeBean.CarModelInfosBean>) mInsuranceChoosePlanAdapter.getData();
                for (InsuranceCarTypeBean.CarModelInfosBean insuranceChoosePlanBean : data) {
                    if (insuranceChoosePlanBean.getSelect() == 1) {
                        Intent intent = getIntent();
                        intent.putExtra("carBean", insuranceChoosePlanBean);
                        setResult(9877, intent);
                        activity.onBackPressed();
                        return;
                    }
                }
            }
        });
    }


    private int TOTAL_COUNTER;

    private static final int PAGE_SIZE = 20;

    private int pageCounter;

    private int mCurrentCounter = 0;


    @Override
    public void onLoadMoreRequested() {
        if (mInsuranceChoosePlanAdapter.getData().size() < PAGE_SIZE) {
            mInsuranceChoosePlanAdapter.loadMoreEnd(true);
        } else {
            if (mCurrentCounter >= TOTAL_COUNTER) {
//                    pullToRefreshAdapter.loadMoreEnd();//default visible
                mInsuranceChoosePlanAdapter.loadMoreEnd(true);//true is gone,false is visible
            } else {
                //mInsuranceChoosePlanAdapter.loadMoreEnd(true);//true is gone,false is visible
                pageCounter++;
                doNetGetCarType(mSearch,pageCounter);
            }
        }
    }
}
