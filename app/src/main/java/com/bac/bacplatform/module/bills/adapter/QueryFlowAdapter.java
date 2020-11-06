package com.bac.bacplatform.module.bills.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bac.bacplatform.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Wjz on 2017/5/25.
 */

public class QueryFlowAdapter extends BaseAdapter {

    private List<Map<String,Object>> query_phone_listMap = new ArrayList<>();

    public List<Map<String, Object>> getQuery_phone_listMap() {
        return query_phone_listMap;
    }

    @Override
    public int getCount() {
        if (query_phone_listMap != null) {
            return query_phone_listMap.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {

            convertView = View.inflate(convertView.getContext(), R.layout.query_flow_recharge_item, null);

            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);

            viewHolder.tvPhonerechargeMoney = (TextView) convertView.findViewById(R.id.tv_phonerecharge_money);
            viewHolder.tvPhonerechargeType = (TextView) convertView.findViewById(R.id.tv_phonerecharge_type);
            viewHolder.tvPhonerechargeNum = (TextView) convertView.findViewById(R.id.tv_phonerecharge_num);
            viewHolder.tvPhonerechargeDate = (TextView) convertView.findViewById(R.id.tv_phonerecharge_date);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

            /*
            {
                "advance_time": 1477389504000, 预计到账时间
                "arrive_time": 1477384732000,  到账时间
                "create_time": 1477380730000,  创建时间
                "login_phone": "13675155768",
                "order_id": "1610251532105086796",
                "pay_type": "",
                "price": 8.50,
                "product_id": "1",
                "product_name": "100M",
                "recharge_phone": "13675155768",
                "status": 0

            "channel_order_no": "20161110144329944763",
            "create_time": 1478760197000,
            "login_phone": "13641536078",
            "order_id": "1611101443175955742",
            "pay_time": 1478760209000,
            "pay_type": "ALIPAY",
            "price": 66.50,
            "product_id": 5,
            "product_name": "2G",
            "recharge_phone": "13641536078",
            "status": 3,
            "terminal_id": "Nexus 6/355455060516116",
            "trade_no": "1478760209089858"
            }
            */

        try {

            Map<String, Object> queryHm = query_phone_listMap.get(position);

            //付款金额
            String recharge_money = String.valueOf(queryHm.get("price"));
            viewHolder.tvPhonerechargeMoney.setText("¥" + recharge_money);//金额

            //产品
            String product_name = String.valueOf(queryHm.get("product_name"));
            viewHolder.tvPhonerechargeType.setText(product_name.concat("流量包"));

            //日期处理
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd  HH:mm");
            int status = (Integer) queryHm.get("status");
            String date = "";
            switch (status) {
                case 1://1.已支付
                    date = format.format(queryHm.get("advance_time")).concat("  届时生效");
                    break;
                case 3://3.正在处理
                    date = format.format(queryHm.get("advance_time")).concat("  届时生效");
                    break;
                case 2://2.已到账
                    date = format.format(queryHm.get("arrive_time")).concat("  已经生效");
                    break;
                case 5://5.交易失败
                    date = format.format(queryHm.get("arrive_time")).concat("  充值失败");
                    break;
            }

            viewHolder.tvPhonerechargeDate.setText(date);//data+生效日期

            //手机号码
            String card_no = String.valueOf(queryHm.get("recharge_phone"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < card_no.length(); i++) {
                if (i != 3 && i != 8 && card_no.charAt(i) == ' ') {
                    continue;
                } else {
                    sb.append(card_no.charAt(i));
                    if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                        sb.insert(sb.length() - 1, ' ');
                    }
                }
            }
            viewHolder.tvPhonerechargeNum.setText(sb.toString());
        } catch (Exception e) {
        }
        return convertView;
    }

    class ViewHolder {

        private TextView tvPhonerechargeMoney;
        private TextView tvPhonerechargeType;
        private TextView tvPhonerechargeNum;
        private TextView tvPhonerechargeDate;

        private TextView tvPhonerechargeOther;

    }
}