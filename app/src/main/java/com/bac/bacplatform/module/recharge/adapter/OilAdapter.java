package com.bac.bacplatform.module.recharge.adapter;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;

import com.bac.bacplatform.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Wjz on 2017/5/8.
 */

public class OilAdapter extends BaseQuickAdapter<OilBean, BaseViewHolder> {


    public OilAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    public int getItemCount() {
        System.out.println(super.getItemCount()+"数据长度..");
        return super.getItemCount();

    }

    @Override
    protected void convert(BaseViewHolder helper, OilBean item) {
        Context context = helper.itemView.getContext();
        boolean b = true;
        boolean isSelected = item.isIsSelected();
        int sale_money = (int) item.getSale_money();


        if (sale_money > 0) {
            helper.setVisible(R.id.tv_02, true);
           // 顾科   显示自定义金额到金额标签 helper.setText(R.id.tv_01,)
        } else {
            helper.setVisible(R.id.tv_02, false);
        }
        System.out.println("nnnnnnnn" + item.getDiscount());
        helper
                .setText(R.id.tv_01, item.getProduct_name().replace("##","\n"))
                .setText(R.id.tv_02, sale_money > 0 ? "售价：" + sale_money + "元" : "")
                .setTextColor(R.id.tv_01, isSelected ? ContextCompat.getColor(context, R.color.white) : ContextCompat.getColor(context, R.color.blue))
                .setTextColor(R.id.tv_02, isSelected ? ContextCompat.getColor(context, R.color.white) : ContextCompat.getColor(context, R.color.blue))
                .setBackgroundRes(R.id.fl_container_01, isSelected ? R.drawable.oil_stroke_solid_corners_blue : R.drawable.oil_stroke_solid_corners_white)
                .setBackgroundRes(R.id.ll_container_02,
                       // 取消惠
                      item.getDiscount()>0 ? context.getResources().getIdentifier("discount_"+item.getDiscount(),"mipmap",context.getPackageName()) :
                                android.R.color.transparent)


        ;
    }

}
