package com.bac.bacplatform.module.recharge.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.extended.base.components.AutomaticBaseFragment;
import com.bac.bacplatform.module.recharge.OilVoucherActivity;
import com.bac.bacplatform.module.recharge.adapter.OilVoucherAdapter;
import com.bac.bacplatform.module.recharge.adapter.OilVoucherBean;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.weex_activities.WeexOilRechargeActivity;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by Wjz on 2017/5/22.
 */

public class OilVoucherFragment extends AutomaticBaseFragment {

    public static final int RESULT_CODE = 21110;
    private RecyclerView rv;
    private OilVoucherAdapter oilVoucherAdapter;
    private OilVoucherBean oilVoucherBean;
    private ArrayList<OilVoucherBean> oilVoucherBeanArrayList;

    public static OilVoucherFragment newInstance(Bundle bundle) {
        OilVoucherFragment oilVoucherFragment = new OilVoucherFragment();
        oilVoucherFragment.setArguments(bundle);
        return oilVoucherFragment;
    }
      final int REQUEST_CODE = 2111;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        oilVoucherBeanArrayList = arguments.getParcelableArrayList(OilVoucherActivity.VOUCHER);
        View view = inflater.inflate(R.layout.oil_voucher_fragment, container, false);

        initToolBar(view, getString(R.string.voucher_title));

        rv = (RecyclerView) view.findViewById(R.id.rv);

        Button btn = (Button) view.findViewById(R.id.btn);
        btn.setText("确认");
        RxView.clicks(btn)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        if (activity.getIntent().getIntExtra("REQUEST_CODE", -1) == REQUEST_CODE) {
                            activity.setResult(RESULT_CODE, new Intent().putExtra(OilVoucherActivity.VOUCHER, oilVoucherBean));
                            activity.onBackPressed();
                            activity.overridePendingTransition(R.anim.cl_slide_left_in, R.anim.cl_slide_right_out);
                        } else {

//                            UIUtils.startActivityInAnimAndFinishSelf(activity, new Intent(activity, OilActivity.class));
                            UIUtils.startActivityInAnimAndFinishSelf(activity, new Intent(activity, WeexOilRechargeActivity.class));
                        }
                    }
                });

        if (oilVoucherBeanArrayList != null && oilVoucherBeanArrayList.size() > 0) {
            oilVoucherBean = oilVoucherBeanArrayList.get(0);
            oilVoucherBean.setSelected(true);
            initAdapter(oilVoucherBeanArrayList);
        }

        return view;
    }


    private void initAdapter(ArrayList<OilVoucherBean> oilVoucherBeanArrayList) {

        oilVoucherAdapter = new OilVoucherAdapter(R.layout.oil_voucher_item, oilVoucherBeanArrayList);
        rv.setAdapter(oilVoucherAdapter);
        rv.setLayoutManager(new LinearLayoutManager(activity));
        oilVoucherAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<OilVoucherBean> data = adapter.getData();
                switch (view.getId()) {
                    case R.id.ll:

                       /* for (OilVoucherBean oilVoucherBean : data) {
                            oilVoucherBean.setSelected(false);
                        }*/

                        OilVoucherBean oilVoucherBean = data.get(position);

                        if (oilVoucherBean.isSelected()) {
                            OilVoucherFragment.this.oilVoucherBean = null;
                            oilVoucherBean.setSelected(false);
                        } else {
                            for (OilVoucherBean o : data) {
                                o.setSelected(false);
                            }
                            OilVoucherFragment.this.oilVoucherBean=oilVoucherBean;
                            oilVoucherBean.setSelected(true);
                        }

                        /*// 是否选择
                        if (oilVoucherBean.isSelected()) {
                            oilVoucherBean=null;
                            oilVoucherBean.setSelected(false);
                        }else{
                            OilVoucherFragment.this.oilVoucherBean=oilVoucherBean;
                            oilVoucherBean.setSelected(true);
                        }*/

                        break;
                }

                adapter.notifyDataSetChanged();
            }
        });
    }


}
