package com.bac.bacplatform.old.module.insurance;


import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.module.insurance.adapter.CompAdapter;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceHomeBean;
import com.bac.bacplatform.old.module.insurance.domain.InsurancePlanBean;
import com.bac.bacplatform.utils.tools.Util;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.baidu.location.g.a.i;


/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.insurance
 * 创建人：Wjz
 * 创建时间：2016/12/7
 * 类描述：
 */

public class InsuranceChooseComp3 extends InsuranceActivity {

    private RecyclerView mRv;
    private InsuranceHomeBean mBean;
    private CompAdapter mAdapter;
    private InsurancePlanBean mList;
    private Button btnInsuranceChoosePlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView2();
        initDate();
        initEvent();

    }

    @Override
    protected void onDestroy() {
        if (mAdapter != null) {
            mAdapter.onDestroyCallback();
        }
        super.onDestroy();
    }


    private void initEvent() {
        //mRv.addOnItemTouchListener(new OnItemClickListener() {
        //    @Override
        //    public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        //        startActivityIn(new Intent(InsuranceChooseComp2.this, InsuranceQueryCost.class));
        //    }
        //});


        mRv.addOnItemTouchListener(new OnItemChildClickListener() {
                                       @Override
                                       public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                           Map<String, Object> map = (Map<String, Object>) baseQuickAdapter.getData().get(position);

                                           Object flag = map.get("flag");
                                           if (flag != null) {

                                               //邮寄地址
                    /*Intent intent = new Intent(InsuranceChooseComp3.this, InsuranceUploadAddress.class);
                    startActivityIn(intent.putExtra("bean", mBean));*/
                                               //电话
                                               Util.callUs(InsuranceChooseComp3.this);

                                           } else {
                                               Object inner = map.get("map");

                                               if (inner != null) {
                                                   Map<String, Object> mapInner = (Map<String, Object>) inner;
                                                   int status = (int) mapInner.get("status");
                                                   switch (view.getId()) {
                                                       case R.id.iv_comp_right:
                                                           if (status != 2) {
                                                               InsuranceChooseComp3.this.onBackPressed();
                                                           }
                                                           break;
                                                       case R.id.ll_bottom:
                                                           if (status == 2) {

                                                               // TODO: 2017/5/17 InsuranceQueryCost3.class


                                    UIUtils.startActivityInAnim(InsuranceChooseComp3.this, new Intent(InsuranceChooseComp3.this, InsuranceQueryCost3.class)
                                            .putExtra("json", String.valueOf(JSON.toJSON(mapInner)))
                                            .putExtra("bean", mBean)
                                            .putExtra("list", mList));
                                                           }
                                                           break;
                                                   }
                                               } else {
                                                   Object label = map.get("label");
                                                   if (label != null) {
                                                       if ((int) label == 0000) {
                                                           baseQuickAdapter.notifyItemChanged(i);//重新刷新
                                                       }
                                                   }
                                               }
                                           }


                                       }
                                   }
        );
    }


    private void initDate() {
        mBean = getIntent().getParcelableExtra("bean");

        mList = getIntent().getParcelableExtra("list");


        List<Map<String, Object>> mapList = new ArrayList<>();

        mAdapter = new CompAdapter(R.layout.insurance_choose_comp_2_item, mapList);
        mRv.setAdapter(mAdapter);

        doNetGetProvider();
    }

    private void doNetGetProvider() {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_PROVIDER")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("order_id", mBean.getOrder_id()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<Map<String, Object>>>() {
                    @Override
                    public List<Map<String, Object>> call(String s) {
                        return JSON.parseObject(((List<Map<String, Object>>) JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                        }.getType())).get(0).get("providers") + "", new TypeReference<List<HashMap<String, Object>>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {
                        if (mapList == null) {
                            mapList = new ArrayList<>();
                        }
                        //增加 重要提示
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("flag", 1234);
                        mapList.add(hashMap);
                        mAdapter.addData(mapList);
                    }
                });
    }

    private void initView2() {

        setContentView(R.layout.insurance_choose_comp_3_activity);

        TextView tv = (TextView) findViewById(R.id.tv_center);
        tv.setText("选择企业");
        ImageView iv = (ImageView) findViewById(R.id.iv);
        RxView.clicks(iv)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        goToAlert4();
                    }
                });


        btnInsuranceChoosePlan = (Button) findViewById(R.id.btn_insurance_choose_plan);
        mRv = (RecyclerView) findViewById(R.id.rv_normal);
        mRv.setLayoutManager(new LinearLayoutManager(this));
    }

    //跳转到险种修改页
    private void goToAlert4() {
        Intent intent = new Intent(InsuranceChooseComp3.this, InsuranceAlter4.class)
                .putExtra("reset", true)
                .putExtra("list", mList)
                .putExtra("bean", mBean);

        startActivity(intent);
        activity.overridePendingTransition(R.anim.cl_slide_left_in, R.anim.cl_slide_right_out);
        activity.finish();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goToAlert4();
    }
}
