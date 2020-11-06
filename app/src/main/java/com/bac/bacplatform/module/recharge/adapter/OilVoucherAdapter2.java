package com.bac.bacplatform.module.recharge.adapter;

import androidx.annotation.LayoutRes;

import com.bac.bacplatform.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by wujiazhen on 2017/7/31.
 */

public class OilVoucherAdapter2 extends BaseQuickAdapter<OilVoucherBean,BaseViewHolder> {

    public OilVoucherAdapter2(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, OilVoucherBean item) {

        System.out.println(item);

        helper.setText(R.id.tv_01, item.getVoucher_money() + "")
                .setText(R.id.tv_02, item.getName() + "\n" + item.getExplain_text().replace("##", "\n"))
               ;

    }
}