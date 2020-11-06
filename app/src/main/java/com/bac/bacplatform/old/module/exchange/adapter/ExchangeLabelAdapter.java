package com.bac.bacplatform.old.module.exchange.adapter;


import com.bac.bacplatform.R;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.adapter
 * 创建人：Wjz
 * 创建时间：2016/9/12
 * 类描述：
 */
public class ExchangeLabelAdapter extends BaseItemDraggableAdapter<ItemExchangeBean,BaseViewHolder> {
    public ExchangeLabelAdapter(int layoutResId, List<ItemExchangeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ItemExchangeBean itemExchangeBean) {

        baseViewHolder.setText(R.id.btn_exchange_home, itemExchangeBean.getProduct_name())
                .setBackgroundRes(R.id.btn_exchange_home, itemExchangeBean.getBg());
    }

}
