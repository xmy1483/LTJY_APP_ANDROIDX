package com.bac.bacplatform.old.module.insurance.adapter;

import android.content.Context;

import com.bac.bacplatform.R;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceCarTypeBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Wjz on 2017/5/16.
 */

public class InsuranceChoosePlanAdapter extends BaseQuickAdapter<InsuranceCarTypeBean.CarModelInfosBean,BaseViewHolder> {

    public InsuranceChoosePlanAdapter(int layoutResId, List<InsuranceCarTypeBean.CarModelInfosBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, InsuranceCarTypeBean.CarModelInfosBean insuranceChoosePlanBean) {
        Context context = baseViewHolder.itemView.getContext();
        baseViewHolder.setText(R.id.tv_choose_plan_top, insuranceChoosePlanBean.getVehicle_name())
                .setText(R.id.tv_choose_plan_bottom, "新车参考报价:".concat(String.valueOf(insuranceChoosePlanBean.getPrice())));

        switch (insuranceChoosePlanBean.getSelect()) {
            case 0://不勾选
                baseViewHolder.setVisible(R.id.iv_choose_plan_choice, false)
                        .setTextColor(R.id.tv_choose_plan_top, context.getResources().getColor(R.color.gray_8f))
                        .setTextColor(R.id.tv_choose_plan_bottom, context.getResources().getColor(R.color.gray_8f));
                break;
            case 1://勾选
                baseViewHolder.setVisible(R.id.iv_choose_plan_choice, true)
                        .setTextColor(R.id.tv_choose_plan_top, context.getResources().getColor(R.color.colorPrimary))
                        .setTextColor(R.id.tv_choose_plan_bottom, context.getResources().getColor(R.color.colorPrimary));
                break;
        }
    }
}