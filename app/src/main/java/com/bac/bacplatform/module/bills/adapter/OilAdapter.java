package com.bac.bacplatform.module.bills.adapter;

import androidx.annotation.LayoutRes;

import com.bac.bacplatform.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Map;

import static com.bac.bacplatform.utils.tools.CountDown.format_yyyy_mm_dd_hh_mm_ss1;

/**
 * Created by guke on 2017/12/22.
 */


public class OilAdapter extends BaseQuickAdapter<Map<String, Object>, BaseViewHolder> {
    public OilAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Map<String, Object> item) {

        try{
//            helper.setText(R.id.tv_01, item.get("pay_type") + "充值" + item.get("recharge_money") + "元")
//                    .setText(R.id.tv_02, item.get("recharge_money") + "")
//                    .setText(R.id.tv_11, format2.format(item.get("pay_time")));
            String aa = item.get("card_no")+"";
            helper.setText(R.id.tv_01, item.get("recharge_money") + "元")
                    .setText(R.id.tv_02, item.get("status") + "")
                    .setText(R.id.tv_03, aa.substring(aa.length()-4,aa.length()) + "("+item.get("card_name")+")")
                    .setText(R.id.tv_04, item.get("pay_type") + "")
                    .setText(R.id.tv_05, item.get("pay_money") + "")
                    .setText(R.id.tv_11, format_yyyy_mm_dd_hh_mm_ss1.format(item.get("pay_time")));
        }catch (Exception e){}


    }
}
