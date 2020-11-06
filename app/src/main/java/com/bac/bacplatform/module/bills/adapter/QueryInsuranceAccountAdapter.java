package com.bac.bacplatform.module.bills.adapter;

import com.bac.bacplatform.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.Map;

import static com.bac.bacplatform.utils.tools.CountDown.format3;


/**
 * Created by Wjz on 2017/5/25.
 */

public class QueryInsuranceAccountAdapter extends BaseQuickAdapter<Map<String, Object>, BaseViewHolder> {

    public QueryInsuranceAccountAdapter(int layoutResId, List<Map<String, Object>> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Map<String, Object> map) {

        String labelStr = "";
        Object status = map.get("status");
        if (status != null) {
            if (Integer.valueOf(status.toString()) == 1) {

                if ((boolean) map.get("is_upload_image") || (boolean) map.get("is_upload_address")) {
                    labelStr = "(需补齐资料)";
                }

            }
        }

        baseViewHolder.setVisible(R.id.tv_01, false)
                .setText(R.id.tv_02, checkStatus(status))
                .setText(R.id.tv_04, String.valueOf(map.get("car_license_no")).concat(labelStr));


        Object advance_time = map.get("advance_time");
        if (advance_time != null) {
            baseViewHolder.setText(R.id.tv_05, "完成时间：".concat(format3.format(advance_time)))
                    .setVisible(R.id.tv_05, true);
        } else {
            baseViewHolder
                    .setVisible(R.id.tv_05, false);
        }
        baseViewHolder.setText(R.id.tv_06, "订单号码：".concat(String.valueOf(map.get("order_id"))))
                .setText(R.id.tv_07, "创建时间：".concat(format3.format(map.get("create_time"))));

    }


    public String checkStatus(Object status) {
        String str = "未知错误";
        if (status != null) {
            switch (Integer.valueOf(status.toString())) {
                case 0:
                    str = "未支付";
                    break;
                case 1:
                    str = "已支付";
                    break;
                case 2:
                    str = "报价成功";
                    break;
                case 3:
                    str = "报价失败";
                    break;
                case 4:
                    str = "核保失败";
                    break;
                case 5:
                    str = "待配送";
                    break;
                case 6:
                    str = "订单完成";
                    break;
                case 7:
                    str = "承保政策限制";
                    break;
                case 8:
                    str = "待退回保费";
                    break;
                case 9:
                    str = "退费成功";
                    break;
                case 10:
                    str = "订单取消";
                    break;
                case 11:
                    str = "报价中";
                    break;
                case 12:
                    str = "订单关闭";
                    break;
                case 13:
                    str = "核保成功";
                    break;
                case 14:
                    str = "影像补齐";
                    break;
                case 15:
                    str = "核保中";
                    break;

            }
        }
        return str;
    }
}