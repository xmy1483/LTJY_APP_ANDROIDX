package com.bac.bacplatform.old.module.insurance.domain;

import android.content.Context;
import android.text.Html;
import android.widget.ImageView;

import com.bac.bacplatform.R;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Wjz on 2017/5/17.
 */

public class InsuranceQueryCostAdapter extends BaseMultiItemQuickAdapter<InsuranceQueryCostBean, BaseViewHolder> {
    public InsuranceQueryCostAdapter(List<InsuranceQueryCostBean> data) {
        super(data);
        addItemType(InsuranceQueryCostBean.ONE, R.layout.insurance_query_cost_one_item);
        addItemType(InsuranceQueryCostBean.TWO, R.layout.insurance_query_cost_two_item);
        addItemType(InsuranceQueryCostBean.THREE, R.layout.insurance_query_cost_three_item);
        addItemType(InsuranceQueryCostBean.FOUR, R.layout.insurance_query_cost_four_item);
        addItemType(InsuranceQueryCostBean.FIVE, R.layout.insurance_query_cost_five_item);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, InsuranceQueryCostBean insuranceQueryCostBean) {
        try {
            Context context = baseViewHolder.itemView.getContext();
            switch (baseViewHolder.getItemViewType()) {
                case InsuranceQueryCostBean.ONE:
                    //tv的中划线
                    baseViewHolder
                            .setText(R.id.tv_in_query_cost_one_right, "查看报价详情")
                            .setText(R.id.tv_in_query_cost_one, insuranceQueryCostBean.getStr1())
                            .addOnClickListener(R.id.ll_01);

                    ImageView iv = baseViewHolder.getView(R.id.iv_in_query_cost_one);
                    Glide.with(context).load(insuranceQueryCostBean.getStr3()).into(iv);
                    break;
                case InsuranceQueryCostBean.TWO:
                    String str = String.format(mContext.getString(R.string.insurance_query_two_text), insuranceQueryCostBean.getStr1());
                    baseViewHolder.setText(R.id.tv_in_query_cost_two_right, Html.fromHtml(str));
                    break;
                case InsuranceQueryCostBean.THREE:
                    Map<String, Object> mapThree = (Map<String, Object>) insuranceQueryCostBean.getData();

                    // 2017/3/28  增加字段
                    baseViewHolder.setText(R.id.tv_platform_price, "¥" + StringUtil.isNullOrEmpty(mapThree.get("total_premium")))
                            .setText(R.id.tv_01, "¥" + StringUtil.isNullOrEmpty(mapThree.get("pay_money")))
                            .setText(R.id.tv_03, "¥" + StringUtil.isNullOrEmpty(mapThree.get("discount_money")));
                    String voucher_total_money = mapThree.get("voucher_total_money").toString();

                    //券金额是否显示
                    if (Float.valueOf(voucher_total_money) > 0) {
                        baseViewHolder.setVisible(R.id.ll_01, true)
                                .setText(R.id.tv_02, "- ¥" + voucher_total_money);
                    } else {
                        baseViewHolder.setVisible(R.id.ll_01, false);
                    }

                    if (Integer.valueOf(mapThree.get("total_present_price").toString()) > 0) {
                        baseViewHolder.setVisible(R.id.tv_in_query_cost_three_left, true);
                    } else {
                        baseViewHolder.setVisible(R.id.tv_in_query_cost_three_left, false);
                    }

                    break;
                case InsuranceQueryCostBean.FOUR:
                    Map<String, String> map = (Map<String, String>) insuranceQueryCostBean.getData();
                    if (map != null) {
                        baseViewHolder
                                .setText(R.id.tv_01, "赠品名称：".concat(map.get("present_name")))
                                .setText(R.id.tv_02, "赠品数量：".concat(map.get("num")))
                                .setText(R.id.tv_03, "市场价格：¥".concat(map.get("market_price")))
                                .addOnClickListener(R.id.ll);
                        ImageView iv4 = baseViewHolder.getView(R.id.iv);

                        Glide.with(context).load(map.get("present_image")).into(iv4);
                    }
                    break;
                case InsuranceQueryCostBean.FIVE:
                    baseViewHolder.addOnClickListener(R.id.ll_img_upload);
                    break;
            }
        } catch (Exception e) {
        }
    }
}