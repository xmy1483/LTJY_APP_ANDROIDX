package com.bac.bacplatform.old.module.insurance.adapter;

import android.view.View;

import com.bac.bacplatform.R;
import com.bac.bacplatform.old.module.insurance.domain.InsurancePlanBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Wjz on 2017/5/17.
 */

public class RvPlanAdapter extends BaseQuickAdapter<InsurancePlanBean.RiskKindsBean,BaseViewHolder> {

    public RvPlanAdapter(int layoutResId, List<InsurancePlanBean.RiskKindsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, InsurancePlanBean.RiskKindsBean riskKindsBean) {

        //1.is_check
        if (riskKindsBean.isIs_check()) {
            //显示
            baseViewHolder.setVisible(R.id.ll_container, true)
                    //2.risk_name
                    .setText(R.id.tv_01, riskKindsBean.getRisk_name());
            //3.is_not_deductible 是否显示不计免赔
            if (riskKindsBean.isIs_not_deductible()) {
                baseViewHolder.setVisible(R.id.tv_03, true);
            } else {
                baseViewHolder.getView(R.id.tv_03).setVisibility(View.INVISIBLE);
            }
            //4.option_type
            Integer option_type = riskKindsBean.getOption_type();
            if(option_type!=null){
                switch (option_type) {
                    case -1:
                        baseViewHolder.setVisible(R.id.tv_02, false);
                        break;
                    case 0:
                        //显示
                        baseViewHolder.setVisible(R.id.tv_02, true);
                        //处理数据
                        String option_value_str = String.valueOf(riskKindsBean.getOption_value());
                        if (option_value_str.length() > 4) {
                            option_value_str = option_value_str.substring(0, option_value_str.length() - 4).concat("万");
                        }
                        baseViewHolder.setText(R.id.tv_02, option_value_str);
                        break;
                    case 1:
                        baseViewHolder.setVisible(R.id.tv_02, true);
                        baseViewHolder.setText(R.id.tv_02, riskKindsBean.getOption_remark()==null?"投保":riskKindsBean.getOption_remark());
                        break;
                }
            }else{
                baseViewHolder.setVisible(R.id.tv_02, true)
                        .setText(R.id.tv_02,"投保");
            }
        } else {
            baseViewHolder.setVisible(R.id.ll_container, false);
        }
    }
}