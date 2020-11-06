package com.bac.bihupapa.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;

import com.bac.bihupapa.R;
import com.bac.bihupapa.bean.InsuranceAddressBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.adapter
 * 创建人：Wjz
 * 创建时间：2017/2/9
 * 类描述：
 */

public class AddressAdapter1 extends BaseQuickAdapter<InsuranceAddressBean, BaseViewHolder> {
    public AddressAdapter1(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, InsuranceAddressBean insuranceAddressBean) {
        Context context = baseViewHolder.itemView.getContext();

        baseViewHolder.setText(android.R.id.text1, insuranceAddressBean.getProvince_name());
        if (insuranceAddressBean.isSelect()) {
            baseViewHolder.setBackgroundColor(android.R.id.text1, ContextCompat.getColor(context, R.color.colorPrimary))
                    .setTextColor(android.R.id.text1, ContextCompat.getColor(context, android.R.color.white));
        } else {
            baseViewHolder.setBackgroundColor(android.R.id.text1, ContextCompat.getColor(context, android.R.color.white))
                    .setTextColor(android.R.id.text1, ContextCompat.getColor(context, android.R.color.black));
        }
    }
}
