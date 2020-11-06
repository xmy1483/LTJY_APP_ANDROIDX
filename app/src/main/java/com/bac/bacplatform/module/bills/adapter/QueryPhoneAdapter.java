package com.bac.bacplatform.module.bills.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bac.bacplatform.R;
import com.bac.bacplatform.module.bills.QueryPhoneActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Wjz on 2017/5/25.
 */

public class QueryPhoneAdapter extends BaseAdapter {
    private final QueryPhoneActivity queryPhoneActivity;
    private List<Map<String, Object>> query_phone_listMap = new ArrayList<>();

    public QueryPhoneAdapter(QueryPhoneActivity queryPhoneActivity) {
        this.queryPhoneActivity = queryPhoneActivity;
    }

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

            convertView = View.inflate(queryPhoneActivity, R.layout.query_phone_recharge_item, null);

            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);

            viewHolder.tvPhonerechargeMoney = (TextView) convertView.findViewById(R.id.tv_phonerecharge_money);
            viewHolder.tvPhonerechargeType = (TextView) convertView.findViewById(R.id.tv_phonerecharge_type);
            viewHolder.tvPhonerechargeNum = (TextView) convertView.findViewById(R.id.tv_phonerecharge_num);
            viewHolder.tvPhonerechargeDate = (TextView) convertView.findViewById(R.id.tv_phonerecharge_date);
            viewHolder.tvPhonerechargeDomain = (TextView) convertView.findViewById(R.id.tv_phonerecharge_domain);
            viewHolder.tvPhonerechargePay = (TextView) convertView.findViewById(R.id.tv_phonerecharge_pay);

            viewHolder.tvPhonerechargeDomain.setVisibility(View.GONE);
            viewHolder.tvPhonerechargeNum.addTextChangedListener(new TextWatcher() {
                /**
                 * 格式化手机号码 xxx xxxx xxxx
                 * @param s
                 * @param start
                 * @param before
                 * @param count
                 */
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (s == null || s.length() == 0) {
                        return;
                    }
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < s.length(); i++) {
                        if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                            continue;
                        } else {
                            sb.append(s.charAt(i));
                            if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                                sb.insert(sb.length() - 1, ' ');
                            }
                        }
                    }
                    if (!sb.toString().equals(s.toString())) {
                        int index = start + 1;
                        if (sb.charAt(start) == ' ') {
                            if (before == 0) {
                                index++;
                            } else {
                                index--;
                            }
                        } else {
                            if (before == 1) {
                                index--;
                            }
                        }
                        viewHolder.tvPhonerechargeNum.setText(sb.toString());

                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        Map<String, Object> queryHm = query_phone_listMap.get(position);


        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd  HH:mm");
        String date = format.format(queryHm.get("create_time"));

        viewHolder.tvPhonerechargeDate.setText(date);

        viewHolder.tvPhonerechargeMoney.setText("¥" + queryHm.get("recharge_money") + "");
        viewHolder.tvPhonerechargeNum.setText(queryHm.get("recharge_phone") + "");

            /*
                            "business_status": 1,
                            "business_time": 1470729476000,
                            "create_time": 1470729412000,
                            "login_phone": "13641536078",
                            "order_id": 160833131,
                            "pay_money": 29.99,
                            "pay_status": 1,
                            "pay_time": 1470729475000,
                            "recharge_money": 30,
                            "recharge_phone": "13641536078"


                            {"business_status":0,"create_time":1472642650000,"login_phone":"13641536078",
                            "call_back_status":0,"pay_money":0.01,"pay_time":1472642665000,
                            "recharge_phone":"13641536078","pay_status":1,"recharge_money":0,
                            "voucher_type":1,"voucher_id":1608311813447982857,"pay_type":"ALIPAY",
                            "order_id":1608311924107700720,"voucher_money":1.00,"order_type":1,
                            "terminal_id":"Nexus 6/355455060516116"}
            * */
        return convertView;
    }

    class ViewHolder {

        private TextView tvPhonerechargeMoney;
        private TextView tvPhonerechargeType;
        private TextView tvPhonerechargeNum;
        private TextView tvPhonerechargeDate;
        private TextView tvPhonerechargeDomain;
        private TextView tvPhonerechargePay;
        private TextView tvPhonerechargeOther;

    }


}