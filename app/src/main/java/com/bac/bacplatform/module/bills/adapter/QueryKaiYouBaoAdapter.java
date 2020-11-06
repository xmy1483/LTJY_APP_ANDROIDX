package com.bac.bacplatform.module.bills.adapter;

import androidx.annotation.LayoutRes;

import com.bac.bacplatform.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Map;

import static com.bac.bacplatform.utils.tools.CountDown.format2;

/**
 * Created by Wjz on 2017/5/25.
 */

public class QueryKaiYouBaoAdapter extends BaseQuickAdapter<Map<String, Object>, BaseViewHolder> {
    public QueryKaiYouBaoAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Map<String, Object> item) {

        try{
            helper.setText(R.id.tv_01, item.get("content") + "")
                    .setText(R.id.tv_11, format2.format(item.get("create_time")));

            String s = item.get("action_money")+"";
            float i = Float.parseFloat(s);
            if (i < 0){
                helper.setText(R.id.tv_02, i+"");
            }else {
                helper.setText(R.id.tv_02, "+"+i+"");
            }


        }catch (Exception e){}


    }
}
