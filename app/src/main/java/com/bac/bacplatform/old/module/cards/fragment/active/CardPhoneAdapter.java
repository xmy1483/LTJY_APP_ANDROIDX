package com.bac.bacplatform.old.module.cards.fragment.active;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bac.bacplatform.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Wjz on 2017/5/15.
 */
public class CardPhoneAdapter extends BaseAdapter {

    private List<Map<String, Object>> mListMap;

    private Context context;

    public CardPhoneAdapter(Context context, List<Map<String, Object>> mListMap) {
        this.mListMap = mListMap;
        this.context = context;
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
        CardPhoneAdapter.ViewHolder mViewHolder;
        if (convertView == null) {

            mViewHolder = new CardPhoneAdapter.ViewHolder();
            convertView = View.inflate(context, R.layout.cards_phone_active_item, null);

            mViewHolder.tv_card_type = (TextView) convertView.findViewById(R.id.tv_card_type);

            mViewHolder.tv_rmb = (TextView) convertView.findViewById(R.id.tv_rmb);
            mViewHolder.tv_card_money = (TextView) convertView.findViewById(R.id.tv_card_money);

            mViewHolder.tv_card_use = (TextView) convertView.findViewById(R.id.tv_card_use);
            mViewHolder.iv_card_active = (ImageView) convertView.findViewById(R.id.iv_card_active);

            mViewHolder.tv_active_top = (TextView) convertView.findViewById(R.id.tv_active_top);
            mViewHolder.tv_active_bottom = (TextView) convertView.findViewById(R.id.tv_active_bottom);

            mViewHolder.tv_card_use_time = (TextView) convertView.findViewById(R.id.tv_card_use_time);
            mViewHolder.tv_card_recharge = (TextView) convertView.findViewById(R.id.tv_card_recharge);
            mViewHolder.tv_card_recharge_next = (TextView) convertView.findViewById(R.id.tv_card_recharge_next);
            mViewHolder.tv_card_type_title = (TextView) convertView.findViewById(R.id.tv_card_type_title);
            mViewHolder.leftContent = (LinearLayout) convertView.findViewById(R.id.ll_left_content);
            mViewHolder.cardUnActivity = (LinearLayout) convertView.findViewById(R.id.ll_card_click_unactive);
            mViewHolder.cardCanUse = (LinearLayout) convertView.findViewById(R.id.ll_card_click_can);
            mViewHolder.cardSelect = (LinearLayout) convertView.findViewById(R.id.ll_card_select);

            convertView.setTag(mViewHolder);

        } else {

            mViewHolder = (CardPhoneAdapter.ViewHolder) convertView.getTag();
        }

/*
            voucher_id	字符	券号
            voucher_money	浮点	券面金额
            voucher_type	整型	券类型
            status	整型	券状态  券状态（0.未激活|1.已激活|2.已使用|3.已过期）
            recharge_money	浮点	充值金额
*/
        //券种类
        Object obj = mListMap.get(position).get("voucher_type");
        int voucher_type = 3;
        if (obj instanceof Integer) {
            voucher_type = (Integer) obj;
        }

        mViewHolder.cardUnActivity.setVisibility(View.VISIBLE);
        mViewHolder.cardCanUse.setVisibility(View.GONE);
        mViewHolder.cardSelect.setVisibility(View.GONE);
        switch (voucher_type) {
            case 1://话费
                //金额文字
                //mViewHolder.tv_rmb.setTextColor(getResources().getColor(R.color.tv_card_type_title_un));
                //mViewHolder.tv_card_money.setTextColor(getResources().getColor(R.color.tv_card_type_title_un));

                //title 背景
                mViewHolder.tv_card_type_title.setBackgroundResource(R.color.tv_card_type_title_un);
                //label
                mViewHolder.cardUnActivity.setBackgroundResource(R.color.tv_card_type_title_un);
                //背景
                //mViewHolder.leftContent.setBackgroundResource(R.mipmap.block_phone_icon);
                mViewHolder.iv_card_active.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.phone_active_label));
                mViewHolder.tv_card_use_time.setTextColor(context.getResources().getColor(R.color.tv_card_type_title_un));
                mViewHolder.tv_card_type_title.setText("待激活");
                mViewHolder.tv_active_top.setText("立即");
                mViewHolder.tv_active_bottom.setText("激活");
                break;
            case 2://流量
                //金额文字
                //mViewHolder.tv_rmb.setTextColor(getResources().getColor(R.color.green_ticket));
                //mViewHolder.tv_card_money.setTextColor(getResources().getColor(R.color.green_ticket));

                mViewHolder.tv_card_type_title.setBackgroundResource(R.color.green_ticket);
                mViewHolder.cardUnActivity.setBackgroundResource(R.color.green_ticket);
                //mViewHolder.leftContent.setBackgroundResource(R.mipmap.block_flow_icon);
                mViewHolder.iv_card_active.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.flux_active_label));
                mViewHolder.tv_card_use_time.setTextColor(context.getResources().getColor(R.color.green_ticket));

                Integer status = (Integer) mListMap.get(position).get("status");
                switch (status) {
                    case 0:
                        mViewHolder.tv_active_top.setText("立即");
                        mViewHolder.tv_active_bottom.setText("激活");
                        mViewHolder.tv_card_type_title.setText("待激活");
                        break;
                    case 4:
                        mViewHolder.tv_active_top.setText("正在");
                        mViewHolder.tv_active_bottom.setText("处理");
                        mViewHolder.tv_card_type_title.setText("处理中");
                        break;
                }

                break;
        }

        mViewHolder.tv_card_type.setText(mListMap.get(position).get("name") + "");

/*
        mViewHolder.tv_card_use.setText("有效期:");
        mViewHolder.tv_card_use_time.setVisibility(View.VISIBLE);

        //日期处理
        Long create_time = (Long) mListMap.get(position).get("create_time");
        Long expired = (Long) mListMap.get(position).get("expired");
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(new Date(create_time));
        cal2.setTime(new Date(expired));


        int create_time_Y = cal1.get(Calendar.DAY_OF_YEAR);
        int expired_Y = cal2.get(Calendar.DAY_OF_YEAR);

        int i = expired_Y - create_time_Y;

        String date_time = "";
        if (i != 0) {
            //今日
            date_time = "仅限今日激活生效";
        } else {
            //全日期
            SimpleDateFormat a = new SimpleDateFormat("yyyy.MM.dd");
            String create_time_format = a.format(new Date(create_time));
            String expired_format = a.format(new Date(expired));
            date_time = create_time_format + "~" + expired_format;
        }


        mViewHolder.tv_card_use_time.setText(date_time);
*/
        // 显示券的金额
        float voucher_money_float = Float.parseFloat(mListMap.get(position).get("voucher_money") + "");
        int voucher_money = (int) voucher_money_float;

        mViewHolder.tv_card_money.setText(voucher_money + "");

        Object explain_text = mListMap.get(position).get("explain_text");
        String replace = "";
        if (TextUtils.isEmpty((CharSequence) explain_text)) {
        } else {
            replace = (explain_text + "").replace("##", "\n");
        }
        mViewHolder.tv_card_recharge.setText(replace);

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

        TextView tv_active_top;
        TextView tv_active_bottom;

        TextView tv_card_type_title;
        ImageView iv_card_active;
        LinearLayout cardUnActivity;
        LinearLayout cardCanUse;
        LinearLayout cardSelect;
        LinearLayout leftContent;

    }
}