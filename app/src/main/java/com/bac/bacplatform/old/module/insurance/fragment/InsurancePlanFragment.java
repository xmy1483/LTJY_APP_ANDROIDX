package com.bac.bacplatform.old.module.insurance.fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bac.bacplatform.R;
import com.bac.bacplatform.old.base.SuperFragment;
import com.bac.bacplatform.old.module.insurance.InsuranceAlter4;
import com.bac.bacplatform.old.module.insurance.adapter.RvPlanAdapter;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceHomeBean;
import com.bac.bacplatform.old.module.insurance.domain.InsurancePlanBean;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.fragment.insurance
 * 创建人：Wjz
 * 创建时间：2017/1/7
 * 类描述：
 */

public class InsurancePlanFragment extends SuperFragment {

    private RecyclerView mRv;
    private TextView mTv;
    private InsurancePlanBean mList;

    private RvPlanAdapter mRvPlanAdapter;
    private InsuranceHomeBean mBean;
    private List<InsurancePlanBean.RiskKindsBean> risk_kinds = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mBean = (InsuranceHomeBean) arguments.get("bean");
        mList = (InsurancePlanBean) arguments.get("list");
        for (InsurancePlanBean.RiskKindsBean riskKindsBean : mList.getRisk_kinds()) {
            if (riskKindsBean.isIs_check()) {
                risk_kinds.add(riskKindsBean);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insurance_choose_plan_3_fragment, container, false);
        mRv = (RecyclerView) view.findViewById(R.id.rv);
        mTv = (TextView) view.findViewById(R.id.tv_btn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initEvent();
    }

    private void initData() {
        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvPlanAdapter = new RvPlanAdapter(R.layout.insurance_choose_plan_3_item, risk_kinds);
        View header = View.inflate(getActivity(), R.layout.insurance_choose_plan_header, null);
        ImageView iv = (ImageView) header.findViewById(R.id.iv);
        LinearLayout ll_bottom_1 = (LinearLayout) header.findViewById(R.id.ll_bottom_1);
        LinearLayout ll_bottom_2 = (LinearLayout) header.findViewById(R.id.ll_bottom_2);

        Glide.with(this).load(mList.getPackage_image()).into(iv);

        //交强险
        //商业险
        if (mList.isIs_payment_efc() && mList.isIs_payment_tax()) {
            ll_bottom_1.setVisibility(View.VISIBLE);
            ll_bottom_2.setVisibility(View.VISIBLE);
        } else {
            ll_bottom_1.setVisibility(View.GONE);
            ll_bottom_2.setVisibility(View.GONE);
        }
        mRvPlanAdapter.addHeaderView(header);
        mRv.setAdapter(mRvPlanAdapter);
    }

    private void initEvent() {
        mTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UIUtils.startActivityInAnim(activity, new Intent(getActivity(), InsuranceAlter4.class).putExtra("list", mList).putExtra("bean", mBean));
            }
        });
    }


}
