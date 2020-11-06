package com.bac.bacplatform.old.module.insurance;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.insurance.adapter.InsuranceFragmentPageAdapter;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceHomeBean;
import com.bac.bacplatform.old.module.insurance.domain.InsurancePlanBean;
import com.bac.bacplatform.old.module.insurance.fragment.InsurancePlanFragment;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.view.ZoomOutPageTransformer;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.commonlib.utils.Util.callPhoneUs;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.insurance
 * 创建人：Wjz
 * 创建时间：2016/12/16
 * 类描述：
 */

public class InsuranceChoosePlan extends SuperActivity {

    private Button btnInsuranceChoosePlan;

    private ViewPager gViewPager;
    private View gRedPoint;
    private LinearLayout gLlGrayPointsContainer;
    private int distance;
    private InsuranceHomeBean mBean;
    private List<InsurancePlanBean> mList;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private InsuranceFragmentPageAdapter mFragmentPageAdapter;
    private int mCurPosition;
    private boolean mReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView2();
        initData();
        initEvent();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mReset = intent.getBooleanExtra("reset", false);
    }

    private void initView2() {
        setContentView(R.layout.insurance_choose_plan_2_activity);

        initToolBar("请选择投保方案");

        btnInsuranceChoosePlan = (Button) findViewById(R.id.btn_insurance_choose_plan);
        btnInsuranceChoosePlan.setText("确定");

        // viewpager组件
        gViewPager = (ViewPager) findViewById(R.id.vp_guide);
        // 红点
        gRedPoint = findViewById(R.id.v_guide_redPoint);
        // 灰点容器
        gLlGrayPointsContainer = (LinearLayout) findViewById(R.id.ll_gray_points);

    }

    private void backPreActivity() {
        startActivity(new Intent(InsuranceChoosePlan.this, InsuranceQuerySuccess.class));
        overridePendingTransition(R.anim.cl_slide_left_in, R.anim.cl_slide_right_out);
        finish();
    }


    private void initEvent() {

        btnInsuranceChoosePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNetSubmitRiskKinds(mList.get(mCurPosition));
            }
        });


        // 监听ViewPager页码改变，当改变到最后一个是显示体验按钮
        gViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // 显示当前ViewPager的页码，当显示到最后一个是显示按钮
               /* if (position == gListDatas.size() - 1) {
                    //gButtonExp.setVisibility(View.VISIBLE);
                } else {
                    //gButtonExp.setVisibility(View.GONE);
                }*/

                mCurPosition = position;

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {


                float pointsMargin = distance * (position + positionOffset);
                RelativeLayout.LayoutParams marginParams = (RelativeLayout.LayoutParams) gRedPoint.getLayoutParams();
                marginParams.leftMargin = Math.round(pointsMargin);
                // 重新设置布局
                gRedPoint.setLayoutParams(marginParams);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void doNetSubmitRiskKinds(final InsurancePlanBean insurancePlanBean) {


        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("SUBMIT_RISK_KINDS")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("order_id", mBean.getOrder_id())
                .put("package_id", insurancePlanBean.getPackage_id())
                .put("is_payment_efc", insurancePlanBean.isIs_payment_efc())
                .put("is_payment_tax", insurancePlanBean.isIs_payment_tax())
                .put("risk_kinds", insurancePlanBean.getRisk_kinds()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<Map<String, Object>>>() {
                    @Override
                    public List<Map<String, Object>> call(String s) {
                        List<Map<String, Object>> list = null;
                        if (!TextUtils.isEmpty(s)) {
                            list = JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                            }.getType());
                        }
                        return list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {
                        if (mapList != null) {
                            Map<String, Object> stringObjectMap = mapList.get(_0);
                            if (stringObjectMap != null) {
                                if (stringObjectMap != null) {
                                    int code = (int) stringObjectMap.get("code");
                                    if (code == 0) {
                                        new AlertDialog.Builder(InsuranceChoosePlan.this)
                                                .setTitle("温馨提示")
                                                .setMessage(String.valueOf(stringObjectMap.get("msg")).replace("##", "\n"))
                                                .setNegativeButton("取消", null)
                                                .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        callPhoneUs(activity);
                                                    }
                                                })
                                                .show();
                                    } else if (code == -2) {
                                        new AlertDialog.Builder(InsuranceChoosePlan.this)
                                                .setTitle("温馨提示")
                                                .setMessage(String.valueOf(stringObjectMap.get("msg")).replace("##", "\n"))
                                                .setNegativeButton("取消", null)
                                                .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        callPhoneUs(activity);
                                                    }
                                                })
                                                .show();
                                    }
                                }
                            }
                        }
                    }
                });


    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("bean", mBean);
        hashMap.put("list", mList);
        String json   = String.valueOf(JSON.toJSON(hashMap));
        String curMd5 = md5(json);

        if (!curMd5.equals(cacheBeanListMd5)) {
            //更新内容
            mDbDao.replaceContent(this.getClass().getName(), json, BacApplication.getLoginPhone(), curMd5);
            //更新索引
            refreshIndex();
        }

    }*/

    private void initData() {
        mBean = getIntent().getParcelableExtra("bean");


        mFragmentPageAdapter = new InsuranceFragmentPageAdapter(getSupportFragmentManager(), mFragmentList) {
        };
        //获取本地缓存
        //doCacheGetRiskKindsList();

        //获取服务器上的套餐数据
        doNetGetRiskKindsList();

        // 创建viewPager的适配器
        //vpAdapter = new VpAdapter();

        //设置viewpage预加载
        gViewPager.setOffscreenPageLimit(3);
        //设置viewpage的滑动效果
        gViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        //gViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.margin_dp));
        gViewPager.setAdapter(mFragmentPageAdapter);

    }

   /* private void doCacheGetRiskKindsList() {
        String value  = null;
        Cursor cursor = mDbDao.queryContent(this.getClass().getName(), BacApplication.getLoginPhone());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            value = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ContentTable.VALUE));
            cacheBeanListMd5 = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ContentTable._MD5));//bean+list
            LogUtils.sf("value:" + value);
            LogUtils.sf("cacheMd5:" + cacheBeanListMd5);
        }
        if (!TextUtils.isEmpty(value)) {
            //显示界面
            HashMap hm = parseObject(value, new TypeReference<HashMap<String, String>>() {
            }.getType());
            if (mBean == null) {
                mBean = JSON.parseObject(String.valueOf(hm.get("bean")), new TypeReference<InsuranceHomeBean>() {
                }.getType());
            }
            if (hm.get("list") != null) {
                String listJson = (String) hm.get("list");
                mListJsonMd5 = md5(listJson);
                mList = JSON.parseObject(listJson, new TypeReference<List<InsurancePlanBean>>() {
                }.getType());

                refreshViewPager(mList);
            }
        }
    }*/

    private void doNetGetRiskKindsList() {


        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_RISK_KINDS_LIST")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("package_type", 9)
                .put("order_id", mBean.getOrder_id()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .map(new Func1<List<Map<String, Object>>, List<InsurancePlanBean>>() {
                    @Override
                    public List<InsurancePlanBean> call(List<Map<String, Object>> mapList) {
                        String json = String.valueOf(mapList.get(0).get("packages"));
                        return JSON.parseObject(json, new TypeReference<List<InsurancePlanBean>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<InsurancePlanBean>>() {
                    @Override
                    public void call(List<InsurancePlanBean> list) {
                        if (list != null) {
                            refreshViewPager(list);
                            mList = list;
                        }
                    }
                });

    }

    private void refreshViewPager(List<InsurancePlanBean> list) {

        if (list != null && list.size() > 0) {

            for (int i = 0; i < list.size(); i++) {
            /*
            View view = View.inflate(InsuranceChoosePlan.this, R.layout.item_insurance_choose_plan_2, null);
            View tv_btn = view.findViewById(R.id.tv_btn);
            tv_btn.setOnClickListener(v -> startActivityIn(
                    new Intent(InsuranceChoosePlan.this, InsuranceAlter4.class)
                            .putParcelableArrayListExtra("listBean", (ArrayList<? extends Parcelable>) mList)
                            .putExtra("bean",mBean)));
            // 将ImageView加入集合
            gListDatas.add(view);
            */
                InsurancePlanFragment fragment = new InsurancePlanFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("list", list.get(i));
                bundle.putParcelable("bean", mBean);
                fragment.setArguments(bundle);
                mFragmentList.add(fragment);


                // 动态的给容器添加灰色的点
                // 1.创建view
                View gPoint = new View(InsuranceChoosePlan.this);
                // 2.指定颜色
                gPoint.setBackgroundResource(R.drawable.white_point);
                // 3.设置大小
                int dp = 10;

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dp2px(dp), UIUtils.dp2px(dp));
                if (i != 0) {
                    params.leftMargin = 30;
                }
                gPoint.setLayoutParams(params);

                gLlGrayPointsContainer.addView(gPoint);
            }
            mFragmentPageAdapter.notifyDataSetChanged();

            if (list.size() > 1) {
                // 监听LinearLayout事件完成
                gLlGrayPointsContainer.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {

                            @Override
                            public void onGlobalLayout() {
                                // 取消监听
                                gLlGrayPointsContainer.getViewTreeObserver()
                                        .removeGlobalOnLayoutListener(this);

                                // 获得两点之间的距离
                                distance = gLlGrayPointsContainer.getChildAt(1)
                                        .getLeft()
                                        - gLlGrayPointsContainer.getChildAt(0)
                                        .getLeft();
                            }
                        }
                );
                gViewPager.setCurrentItem(1);
            }
        }
    }
}
