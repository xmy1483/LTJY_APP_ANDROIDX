package com.bac.bihupapa.adapter;

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

public class AddressAdapter3 extends BaseQuickAdapter<InsuranceAddressBean.CitysBean.AreasBean, BaseViewHolder> {
    public AddressAdapter3(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, InsuranceAddressBean.CitysBean.AreasBean areasBean) {

        baseViewHolder.setText(android.R.id.text1, areasBean.getArea_name());

    }
}
