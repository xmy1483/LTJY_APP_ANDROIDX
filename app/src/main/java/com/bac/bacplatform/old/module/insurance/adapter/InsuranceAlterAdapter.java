package com.bac.bacplatform.old.module.insurance.adapter;

import android.view.View;
import android.widget.LinearLayout;

import com.bac.bacplatform.R;
import com.bac.bacplatform.old.module.insurance.domain.AlterBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Wjz on 2017/5/17.
 */

public   class InsuranceAlterAdapter extends BaseMultiItemQuickAdapter<AlterBean,BaseViewHolder> {

    public InsuranceAlterAdapter(List<AlterBean> data) {
        super(data);
        addItemType(AlterBean.HEADER, R.layout.insurance_alter_header_item);
        addItemType(AlterBean.TITLE, R.layout.insurance_alter_title_item);
        addItemType(AlterBean.CONTEXT, R.layout.insurance_alter_context_item);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, AlterBean alterBean) {
        switch (baseViewHolder.getItemViewType()) {
            case AlterBean.HEADER:
                baseViewHolder.setChecked(R.id.switcher, alterBean.is_payment_efc() && alterBean.is_payment_tax)
                        .addOnClickListener(R.id.switcher);
                break;
            case AlterBean.TITLE:
                baseViewHolder.setText(R.id.tv_title, alterBean.getHeader());
                break;
            case AlterBean.CONTEXT:

                baseViewHolder.setText(R.id.tv_left, alterBean.data.getRisk_name())//name
                        .addOnClickListener(R.id.ll)
                        .addOnClickListener(R.id.ll_mid_container);
                //checkbox
                if (alterBean.data.isIs_check()) {
                    baseViewHolder.setChecked(R.id.cb_alter, alterBean.data.isIs_check())//选择checkbox
                            .setTextColor(R.id.tv_left, baseViewHolder.getConvertView().getResources().getColor(R.color.black))
                            //mid显示
                            .setVisible(R.id.ll_mid_container, true)
                            //right隐藏
                            .setVisible(R.id.rl_right_container, true);

                    LinearLayout ll_mid = baseViewHolder.getView(R.id.ll_mid_container);
                    if (alterBean.data.isIs_not_deductible()) {
                        ll_mid.setVisibility(View.VISIBLE);

                        //是否选择不计免赔 not_deductible
                        if (alterBean.data.isNot_deductible()) {
                            //保赔的字体颜色
                            baseViewHolder.setTextColor(R.id.tv_mid, baseViewHolder.getConvertView().getResources().getColor(R.color.black))
                                    //保赔的图片
                                    .setImageResource(R.id.iv_mid, R.mipmap.bujimianpei_yes);
                        } else {
                            //保赔的字体颜色
                            baseViewHolder.setTextColor(R.id.tv_mid, baseViewHolder.getConvertView().getResources().getColor(R.color.gray_cc))
                                    //保赔的图片
                                    .setImageResource(R.id.iv_mid, R.mipmap.bujimianpei_no);
                        }

                    } else {
                        ll_mid.setVisibility(View.INVISIBLE);
                    }

                    Integer option_type = alterBean.data.getOption_type();
                    if (option_type != null) {
                        switch (option_type) {
                            case 0:
                                //显示spinner
                                String option_value_str = String.valueOf(alterBean.data.getOption_value());
                                if (option_value_str.length() > 4) {
                                    option_value_str = option_value_str.substring(0, option_value_str.length() - 4).concat("万");
                                }
                                baseViewHolder.setVisible(R.id.tv_right, false)
                                        .setVisible(R.id.ll_right_container, true)
                                        .setText(R.id.tv, option_value_str)
                                        .addOnClickListener(R.id.ll_right_container);
                                break;
                            case 1://文字
                                baseViewHolder.setVisible(R.id.tv_right, true)
                                        .setVisible(R.id.ll_right_container, false)
                                        .setText(R.id.tv_right, alterBean.data.getOption_remark() == null ? "" : alterBean.data.getOption_remark());
                                break;
                        }
                    } else {//文字
                        baseViewHolder.setVisible(R.id.tv_right, true)
                                .setVisible(R.id.ll_right_container, false)
                                .setText(R.id.tv_right, "投保");
                    }

                } else {
                    //选择checkbox
                    baseViewHolder.setChecked(R.id.cb_alter, false)
                            //字体颜色
                            .setTextColor(R.id.tv_left, baseViewHolder.getConvertView().getResources().getColor(R.color.gray_cc))
                            //mid隐藏
                            .setVisible(R.id.ll_mid_container, false)
                            //right隐藏
                            .setVisible(R.id.rl_right_container, false);
                }
                break;
        }
    }
}
