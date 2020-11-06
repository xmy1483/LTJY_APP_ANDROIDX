package com.bac.bacplatform.module.recharge;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.recharge.adapter.OilVoucherAdapter2;
import com.bac.bacplatform.module.recharge.adapter.OilVoucherBean;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.weex_activities.WeexOilRechargeActivity;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by wujiazhen on 2017/7/31.
 * <p>
 * 显示优惠券
 */

public class OilVoucherActivity2 extends AutomaticBaseActivity {
    private RecyclerView rv;
    private OilVoucherAdapter2 oilVoucherAdapter;

    public static Intent newIntent(Context context) {
        return new Intent(context, OilVoucherActivity2.class);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.oil_voucher_activity);

        initToolBar(getString(R.string.voucher_title));

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(activity));
        oilVoucherAdapter = new OilVoucherAdapter2(R.layout.oil_voucher_item_2);
        oilVoucherAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 去充值页面
//                UIUtils.startActivityInAnimAndFinishSelf(activity, new Intent(activity, OilActivity.class));
                UIUtils.startActivityInAnimAndFinishSelf(activity, new Intent(activity, WeexOilRechargeActivity.class));
            }
        });
        rv.setAdapter(oilVoucherAdapter);
        QUERY_USE_VOUCHER();
    }

    @Override
    protected void initFragment() {

    }

    private void QUERY_USE_VOUCHER() {
        HttpHelper.getInstance().activityAutoLifeAndLoading(this, new BacHttpBean()
                .setMethodName("QUERY_USE_VOUCHER")
                .put("login_phone", BacApplication.getLoginPhone())
        )
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<OilVoucherBean>>() {
                    @Override
                    public List<OilVoucherBean> call(String s) {
                        return JSON.parseObject(s, new TypeReference<List<OilVoucherBean>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<OilVoucherBean>>() {
                    @Override
                    public void call(List<OilVoucherBean> list) {
                        if (list != null && list.size() > 0) {

                            oilVoucherAdapter.addData(list);

                        } else {
                            new AlertDialog.Builder(activity)
                                    .setTitle(getString(R.string.alert_title))
                                    .setMessage("当前无可使用的加油券")
                                    .setNegativeButton(getString(R.string.alert_cancel), null)
                                    .setPositiveButton(getString(R.string.alert_confirm), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.onBackPressed();
                                        }
                                    })
                                    .show();
                        }

                    }
                });
    }


}
