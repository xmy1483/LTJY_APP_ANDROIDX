package com.bac.bacplatform.old.module.insurance.adapter;

import android.content.Context;
import android.text.Html;

import com.bac.bacplatform.R;
import com.bac.bacplatform.old.module.insurance.domain.DetailRightBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Wjz on 2017/5/17.
 */

public  class DetailRightAdapter extends BaseMultiItemQuickAdapter<DetailRightBean,BaseViewHolder> {

    public DetailRightAdapter(List<DetailRightBean> data) {
        super(data);
        addItemType(DetailRightBean.HEADER, R.layout.detail_right_fg_header_item);
        addItemType(DetailRightBean.TITLE, R.layout.detail_right_fg_title_item);
        addItemType(DetailRightBean.CONTEXT, R.layout.detail_right_fg_content_item);
        addItemType(DetailRightBean.FOOTER, R.layout.detail_right_fg_footer_item);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, DetailRightBean detailRightBean) {
        try {
            switch (baseViewHolder.getItemViewType()) {
                case DetailRightBean.HEADER:
                    //头
                    baseViewHolder.setText(R.id.tv_01, "险种")
                            .setText(R.id.tv_02, "保额")
                            .setText(R.id.tv_03, "保费");
                    break;
                case DetailRightBean.TITLE:
                    baseViewHolder.setText(R.id.tv_01, detailRightBean.getTitle());
                    break;
                case DetailRightBean.CONTEXT:
                    Map<String, Object> data = (Map<String, Object>) detailRightBean.getData();
                    if ("efcInsureInfo_premium".equals(detailRightBean.getTitle())) {
                        baseViewHolder.setText(R.id.tv_01, "交强险")
                                .setText(R.id.tv_02, "投保")
                                .setText(R.id.tv_03, String.valueOf(data.get("efcInsureInfo_premium")));
                    } else if ("taxInsureInfo_taxFee".equals(detailRightBean.getTitle())) {
                        baseViewHolder.setText(R.id.tv_01, "车船税")
                                .setText(R.id.tv_02, "投保")
                                .setText(R.id.tv_03, String.valueOf(data.get("taxInsureInfo_taxFee")));
                    } else if ("不计免赔".equals(detailRightBean.getTitle())) {
                        baseViewHolder.setText(R.id.tv_01, "不计免赔")
                                .setText(R.id.tv_02, "投保")
                                .setText(R.id.tv_03, String.valueOf(data.get("bizInsureInfo_nfcPremium")));
                    } else {
                        baseViewHolder.setText(R.id.tv_01, String.valueOf(data.get("risk_name")))
                                .setText(R.id.tv_02, String.valueOf(data.get("amount")))
                                .setText(R.id.tv_03, String.valueOf(data.get("premium")));
                    }

                    break;
                case DetailRightBean.FOOTER:
                    Context context = baseViewHolder.convertView.getContext();
                    String format = context.getString(R.string.insurance_platform_detail_money);
                        /*
                       总保费 total_premium

                       平台折扣价 pay_money

                       折扣金额 discount_money

                        * */
                    Map<String, Object> map = (Map<String, Object>) detailRightBean.getData();


                    Object total_premium = map.get("total_premium");
                    Object pay_money = map.get("pay_money");

                    String tv01 = String.format(format, "总保费：", "¥"+(total_premium != null ? String.valueOf(total_premium) : "0"));
                    String tv02 = String.format(format, "平台优惠价：", "¥"+(pay_money != null ? String.valueOf(pay_money) : "0"));

                    baseViewHolder.setText(R.id.tv_01, Html.fromHtml(tv01));
                    baseViewHolder.setText(R.id.tv_02, Html.fromHtml(tv02));

                    break;
            }
        } catch (Exception e) {
        }
    }
}
