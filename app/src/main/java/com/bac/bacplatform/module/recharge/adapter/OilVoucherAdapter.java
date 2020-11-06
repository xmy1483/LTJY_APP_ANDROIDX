package com.bac.bacplatform.module.recharge.adapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bac.bacplatform.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Wjz on 2017/5/22.
 */

public class OilVoucherAdapter extends BaseQuickAdapter<OilVoucherBean, BaseViewHolder> {


    public OilVoucherAdapter(@LayoutRes int layoutResId, @Nullable List<OilVoucherBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OilVoucherBean item) {

        helper.setText(R.id.tv_01, item.getVoucher_money() + "")
                .setText(R.id.tv_02, item.getName() + "\n" + item.getExplain_text().replace("##", "\n"))
                .setImageResource(R.id.iv_01, item.isSelected() ? R.mipmap.card_selecte_on : R.mipmap.card_selecte_off)
                .addOnClickListener(R.id.ll);
    }
}
