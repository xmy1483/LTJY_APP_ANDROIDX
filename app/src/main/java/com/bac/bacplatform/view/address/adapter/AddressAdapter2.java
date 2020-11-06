package com.bac.bacplatform.view.address.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;

import com.bac.bacplatform.R;
import com.bac.bacplatform.view.address.InsuranceAddressBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.adapter
 * 创建人：Wjz
 * 创建时间：2017/2/9
 * 类描述：
 */

public class AddressAdapter2 extends BaseQuickAdapter<InsuranceAddressBean.CitysBean, BaseViewHolder> {
    public AddressAdapter2(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, InsuranceAddressBean.CitysBean citysBean) {
        Context context = baseViewHolder.itemView.getContext();

        baseViewHolder.setText(android.R.id.text1, citysBean.getCity_name());

        if (citysBean.isSelect()) {
            baseViewHolder.setBackgroundColor(android.R.id.text1, ContextCompat.getColor(context, R.color.colorPrimary))
                    .setTextColor(android.R.id.text1, ContextCompat.getColor(context, android.R.color.white));
        } else {
            baseViewHolder.setBackgroundColor(android.R.id.text1, ContextCompat.getColor(context, android.R.color.white))
                    .setTextColor(android.R.id.text1, ContextCompat.getColor(context, android.R.color.black));
        }

    }
}
