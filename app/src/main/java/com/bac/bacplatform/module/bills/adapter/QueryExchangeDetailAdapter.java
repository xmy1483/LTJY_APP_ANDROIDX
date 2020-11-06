package com.bac.bacplatform.module.bills.adapter;


import com.bac.bacplatform.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.adapter
 * 创建人：Wjz
 * 创建时间：2016/9/12
 * 类描述：
 */
public class QueryExchangeDetailAdapter extends BaseQuickAdapter<Map<String, Object>,BaseViewHolder> {


    public QueryExchangeDetailAdapter(int layoutResId, List<Map<String, Object>> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Map<String, Object> stringObjectMap) {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("#");
            String pay_money     = decimalFormat.format(stringObjectMap.get("pay_money"));
            String charge_money  = decimalFormat.format(stringObjectMap.get("charge_money"));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

            String create_time = dateFormat.format(stringObjectMap.get("create_time"));

            int    status   = (int) (stringObjectMap.get("status"));
            String end_time = "空";
            switch (status) {
                case 1:
                case 2:
                    end_time = "预计到账时间：" + dateFormat.format(stringObjectMap.get("advance_time"));
                    break;
                case 3:
                    end_time = "油卡到账时间：" + dateFormat.format(stringObjectMap.get("arrive_time"));
                    break;
            }
            String order_id      = stringObjectMap.get("order_id").toString();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(end_time + "\n");
            stringBuilder.append("订单号码：" + order_id + "\n");
            stringBuilder.append("创建时间：" + create_time);
            baseViewHolder.setText(R.id.tv_exchange_money, "¥ " + charge_money)
                    .setText(R.id.tv_exchange_type, (status == 3) ? "已到账" : "处理中")
                    .setText(R.id.tv_exchange_num, stringObjectMap.get("charge_phone").toString())
                    .setText(R.id.tv_exchange_phone_money, "支付" + pay_money + "元话费")
                    .setText(R.id.tv_phonerecharge_detail, stringBuilder.toString());
        } catch (Exception e) {
        }
    }
}
