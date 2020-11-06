package com.bac.bacplatform.old.module.phone;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bac.bacplatform.R;

import java.util.List;
import java.util.Map;

/**
 * Created by guke on 2017/6/22.
 */

public class GvAdapter extends BaseAdapter {
    private Context mContext;
    private List<Map<String, Object>> mList ;

    public GvAdapter(Context mContext, List<Map<String, Object>> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.phone_gv_item1, null);
            viewHolder = new ViewHolder();
            viewHolder.text_view = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        // 框
        Map<String, Object> map = mList.get(position);
        viewHolder.text_view.setSelected((boolean)map.get("isSelected"));

        viewHolder.text_view.setText(map.get("recharge_money")+"元");

        return convertView;
    }

    class ViewHolder {
        TextView text_view;
    }
}
