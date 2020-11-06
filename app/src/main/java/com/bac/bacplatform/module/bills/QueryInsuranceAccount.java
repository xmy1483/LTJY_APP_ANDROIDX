package com.bac.bacplatform.module.bills;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.bills.adapter.QueryInsuranceAccountAdapter;
import com.bac.bacplatform.module.main.MainActivity;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.insurance.InsuranceDetailActivity2;
import com.bac.bacplatform.old.module.insurance.InsuranceQueryCost3;
import com.bac.bacplatform.old.module.insurance.InsuranceQueryVideo;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceHomeBean;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.account
 * 创建人：Wjz
 * 创建时间：2016/12/7
 * 类描述：
 */

public class QueryInsuranceAccount extends SuperActivity {

    private RecyclerView rvInsuranceAccount;
    private QueryInsuranceAccountAdapter mQueryInsuranceAccountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rv_toolbar);

        initToolBar("交易明细");
        MainActivity.setIsShow2(true);

       // ((ToolBarUtil.ViewHolder) toolBarUtil.getList().get(_3).getTag()).setCount(0);

        Constants.is_open=true;
        rvInsuranceAccount = (RecyclerView) findViewById(R.id.rv);
        rvInsuranceAccount.setLayoutManager(new LinearLayoutManager(QueryInsuranceAccount.this));
        rvInsuranceAccount.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
        mQueryInsuranceAccountAdapter = new QueryInsuranceAccountAdapter(R.layout.query_insurance_account_item, null);
        rvInsuranceAccount.setAdapter(mQueryInsuranceAccountAdapter);

        mQueryInsuranceAccountAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Map<String, Object> map = (Map<String, Object>) adapter.getData().get(position);
                InsuranceHomeBean insuranceHomeBean = new InsuranceHomeBean();
                insuranceHomeBean.setOrder_id(Long.valueOf(StringUtil.isNullOrEmpty(map.get("order_id"))));
                insuranceHomeBean.setPrv_id(StringUtil.isNullOrEmpty(map.get("prv_id")));
                int status = Integer.valueOf(StringUtil.isNullOrEmpty(map.get("status")));

                if (status == 3 || status == 4) {
                    //失败
                    Toast.makeText(activity, "处理失败", Toast.LENGTH_SHORT).show();
                } else if (status == 14) {

                    //影像
                    UIUtils.startActivityInAnim(activity, new Intent(QueryInsuranceAccount.this, InsuranceQueryVideo.class)
                            .putExtra("bean", insuranceHomeBean)
                            .putExtra("status", status));
                } else if (status == 2) {

                    UIUtils.startActivityInAnim(activity, new Intent(QueryInsuranceAccount.this, InsuranceQueryCost3.class)
                            .putExtra("bean", insuranceHomeBean));

                } else {
                    UIUtils.startActivityInAnim(activity, new Intent(QueryInsuranceAccount.this, InsuranceDetailActivity2.class)
                            .putExtra("bean", insuranceHomeBean)
                            .putExtra("status", status)
                            .putExtra("is_upload_image", (boolean) map.get("is_upload_image"))
                            .putExtra("is_upload_address", (boolean) map.get("is_upload_address")));
                }


            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        doNetGetOrderList();
    }

    private void doNetGetOrderList() {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setMethodName("QUERY_ORDER_LIST")
                .put("login_phone", BacApplication.getLoginPhone()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {

                        Map<String, Object> map = mapList.get(_0);
                        if (map.containsKey("order")) {

                            List<Map<String, Object>> order = JSON.parseObject(map.get("order") + "", new TypeReference<List<Map<String, Object>>>() {
                            }.getType());

                            mQueryInsuranceAccountAdapter.getData().clear();
                            mQueryInsuranceAccountAdapter.addData(order);

                        } else {
                            new AlertDialog.Builder(QueryInsuranceAccount.this)
                                    .setTitle("骆驼加油").setMessage("暂无账单")
                                    .setNegativeButton("取消", null)
                                    .setPositiveButton("确定", null)
                                    .show();
                        }
                    }
                });
    }

}
