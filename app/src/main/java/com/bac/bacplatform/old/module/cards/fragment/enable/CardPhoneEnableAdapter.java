package com.bac.bacplatform.old.module.cards.fragment.enable;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bac.bacplatform.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Wjz on 2017/5/15.
 */

public class CardPhoneEnableAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> mListMap;
    private List<Map<String, Object>> mSelectedList;

    public CardPhoneEnableAdapter(Context context, List<Map<String, Object>> mapList, List<Map<String, Object>> mSelectedList) {
        this.context = context;
        this.mListMap = mapList;
        this.mSelectedList = mSelectedList;
    }

    @Override
    public int getCount() {

        if (mListMap != null) {
            return mListMap.size();
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
        ViewHolder mViewHolder;
        if (convertView == null) {

            mViewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.cards_phone_enable_item, null);

            mViewHolder.tv_card_type = (TextView) convertView.findViewById(R.id.tv_card_type);
            mViewHolder.tv_rmb = (TextView) convertView.findViewById(R.id.tv_rmb);
            mViewHolder.tv_card_money = (TextView) convertView.findViewById(R.id.tv_card_money);
            mViewHolder.tv_card_use = (TextView) convertView.findViewById(R.id.tv_card_use);
            mViewHolder.tv_card_use_time = (TextView) convertView.findViewById(R.id.tv_card_use_time);
            mViewHolder.tv_card_recharge = (TextView) convertView.findViewById(R.id.tv_card_recharge);
            mViewHolder.tv_card_recharge_next = (TextView) convertView.findViewById(R.id.tv_card_recharge_next);

            mViewHolder.tv_card_type_title = (TextView) convertView.findViewById(R.id.tv_card_type_title);
            mViewHolder.cardUnActivity = (LinearLayout) convertView.findViewById(R.id.ll_card_click_unactive);
            mViewHolder.cardCanUse = (LinearLayout) convertView.findViewById(R.id.ll_card_click_can);
            mViewHolder.cardSelect = (LinearLayout) convertView.findViewById(R.id.ll_card_select);

            convertView.setTag(mViewHolder);

        } else {

            mViewHolder = (ViewHolder) convertView.getTag();
        }

/*
            voucher_id	字符	券号
            voucher_money	浮点	券面金额
            voucher_type	整型	券类型
            status	整型	券状态  券状态（0.未激活|1.已激活|2.已使用|3.已过期）
            recharge_money	浮点	充值金额
*/

        //显示 券的类型
        mViewHolder.tv_card_type.setText(String.valueOf(mListMap.get(position).get("name")));

        // 显示券的金额
        float voucher_money_float = Float.parseFloat(String.valueOf(mListMap.get(position).get("voucher_money")));
        int voucher_money = (int) voucher_money_float;

        mViewHolder.tv_card_money.setText(voucher_money + "");

        //判断券的状态-->status	整型	券状态  券状态（0.未激活|1.已激活|2.已使用|3.已过期）
        int status = (Integer) mListMap.get(position).get("status");

        switch (status) {

            case 1://（1.已激活|2.已使用）
                mViewHolder.cardUnActivity.setVisibility(View.GONE);
                mViewHolder.cardCanUse.setVisibility(View.GONE);
                mViewHolder.cardSelect.setVisibility(View.VISIBLE);
                mViewHolder.tv_card_money.setTextColor(context.getResources().getColor(R.color.tv_card_type_title_act));
                mViewHolder.tv_rmb.setTextColor(context.getResources().getColor(R.color.tv_card_type_title_act));
                //默认全选

                ImageView iv_card_select = (ImageView) mViewHolder.cardSelect.getChildAt(0);//选择框
                boolean contains = mSelectedList.contains(mListMap.get(position));//选中的集合包含-->选中，不包含-->被删除
                if (contains) {
                    iv_card_select.setSelected(true);
                } else {
                    iv_card_select.setSelected(false);
                }
                iv_card_select.setTag(position);

                mViewHolder.tv_card_type_title.setText("已激活");
                mViewHolder.tv_card_type_title.setBackgroundResource(R.color.tv_card_type_title_act);
                mViewHolder.tv_card_use_time.setTextColor(context.getResources().getColor(R.color.tv_card_type_title_act));
                mViewHolder.tv_card_type.setTextColor(context.getResources().getColor(R.color.black));
                mViewHolder.tv_card_recharge.setTextColor(context.getResources().getColor(R.color.black));
                mViewHolder.tv_card_use.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case 2:
                mViewHolder.cardUnActivity.setVisibility(View.GONE);
                mViewHolder.cardCanUse.setVisibility(View.GONE);
                mViewHolder.cardSelect.setVisibility(View.GONE);
                mViewHolder.tv_card_type_title.setText("已使用");
                mViewHolder.tv_card_type_title.setBackgroundResource(R.color.gray);
                mViewHolder.tv_card_money.setTextColor(context.getResources().getColor(R.color.gray));
                mViewHolder.tv_rmb.setTextColor(context.getResources().getColor(R.color.gray));
                mViewHolder.tv_card_use_time.setTextColor(context.getResources().getColor(R.color.gray));
                mViewHolder.tv_card_type.setTextColor(context.getResources().getColor(R.color.gray));
                mViewHolder.tv_card_recharge.setTextColor(context.getResources().getColor(R.color.gray));
                mViewHolder.tv_card_use.setTextColor(context.getResources().getColor(R.color.gray));
                break;
        }


        // mViewHolder.tv_card_recharge.setText(UIUtils.getString(R.string.card_discount_voucher_money, voucher_money + ""));
        // 显示券的使用说明

        String node = String.valueOf(mListMap.get(position).get("explain_text"));
        String replace = "";
        if (TextUtils.isEmpty(node)) {
        } else {
            replace = node.replace("##", "\n");
        }
        mViewHolder.tv_card_recharge.setText(replace);
        //显示券的有效期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String create_time = dateFormat.format(new Date((Long) (mListMap.get(position).get("create_time"))));

        String expired = dateFormat.format(new Date((Long) (mListMap.get(position).get("expired"))));

        mViewHolder.tv_card_use_time.setText(create_time + "~" + expired);

        return convertView;
    }


    class ViewHolder {

        TextView tv_card_type;
        TextView tv_rmb;
        TextView tv_card_money;
        TextView tv_card_use;
        TextView tv_card_use_time;
        TextView tv_card_recharge;
        TextView tv_card_recharge_next;
        TextView tv_card_type_title;
        LinearLayout cardUnActivity;
        LinearLayout cardCanUse;
        LinearLayout cardSelect;

    }
}