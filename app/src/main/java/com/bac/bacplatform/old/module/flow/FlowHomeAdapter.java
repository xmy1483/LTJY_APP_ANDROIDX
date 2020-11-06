package com.bac.bacplatform.old.module.flow;


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
public class FlowHomeAdapter extends BaseItemDraggableAdapter<ItemFlowBean,BaseViewHolder> {


    public FlowHomeAdapter(int layoutResId, List<ItemFlowBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ItemFlowBean itemFlowBean) {
        int label = itemFlowBean.getLabel();
        baseViewHolder.setText(R.id.btn_exchange_home, itemFlowBean.getProduct_name());
        if (label == 1) {//被选中
            baseViewHolder.setTextColor(R.id.btn_exchange_home, baseViewHolder.convertView.getContext().getResources().getColor(R.color.colorPrimary))
                    .setBackgroundRes(R.id.btn_exchange_home, itemFlowBean.getBg());
        } else {//灰色
            baseViewHolder.setTextColor(R.id.btn_exchange_home, baseViewHolder.convertView.getContext().getResources().getColor(R.color.gray_8f))
                    .setBackgroundRes(R.id.btn_exchange_home, itemFlowBean.getBg());
        }
    }

}
