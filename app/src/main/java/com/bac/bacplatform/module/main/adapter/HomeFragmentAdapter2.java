package com.bac.bacplatform.module.main.adapter;

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
public class HomeFragmentAdapter2 extends BaseItemDraggableAdapter<HomeContentIcon,BaseViewHolder> {

    public HomeFragmentAdapter2(int layoutResId, List<HomeContentIcon> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, HomeContentIcon homeContentIcon) {
        switch (homeContentIcon.getLabel()) {
            case 1:
                baseViewHolder.setImageResource(R.id.iv_home_item_left, R.mipmap.oil_3_icon)
                        .setText(R.id.iv_home_item_label, homeContentIcon.getContent())
                        .setBackgroundColor(R.id.ll_main_item_bg, baseViewHolder.getConvertView().getResources().getColor(R.color.white));

                String money = homeContentIcon.getMoney();
                float moneyF = Float.parseFloat(money);
                if (moneyF > 0) {
                    baseViewHolder.setVisible(R.id.iv_home_item_money, true)
                            .setVisible(R.id.iv_home_item_rmb, true)
                            .setText(R.id.iv_home_item_money, money);
                } else {
                    baseViewHolder.setVisible(R.id.iv_home_item_money, false)
                            .setVisible(R.id.iv_home_item_rmb, false);
                }

                break;
            case 2:
                baseViewHolder.setImageResource(R.id.iv_home_item_left, R.mipmap.vip_3_icon)
                        .setText(R.id.iv_home_item_label, homeContentIcon.getContent())
                        .setVisible(R.id.iv_home_item_money, false)
                        .setVisible(R.id.iv_home_item_rmb, false)
                        .setBackgroundColor(R.id.ll_main_item_bg, baseViewHolder.getConvertView().getResources().getColor(R.color.white));
                break;
        }
    }
}
