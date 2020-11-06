package com.bac.bacinnermanager.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.bac.bacinnermanager.R;
import com.bac.bacinnermanager.utils.DataBean;

import java.util.List;

/**
 * Created by guke on 2017/8/16.
 */

public class Lvadapter extends BaseAdapter {
    private Context context;
    private List<DataBean> mList;

    public Lvadapter(Context context, List<DataBean> list) {
        this.context = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList!=null?mList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DataBean dataBean = mList.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {


            convertView = View.inflate(context, R.layout.item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }


        viewHolder.tv1.setText(mList.get(position).getMethod_id()+"");
        viewHolder.tv2.setText(mList.get(position).getParam_key()+"");
        viewHolder.tv3.setText(mList.get(position).getParam_value()+"");
        viewHolder.tv4.setText(mList.get(position).getParam_type()+"");
        viewHolder.tv5.setText(mList.get(position).getRemark()+"");

        viewHolder.tv3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String s1 = s.toString();
                dataBean.setParam_value(s1);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tv1;
        TextView tv2;
        EditText tv3;
        TextView tv4;
        TextView tv5;

        public ViewHolder(View view) {
            tv1 = (TextView) view.findViewById(R.id.text_1);
            tv2 = (TextView) view.findViewById(R.id.text_2);
            tv3 = (EditText) view.findViewById(R.id.text_3);
            tv4 = (TextView) view.findViewById(R.id.text_4);
            tv5 = (TextView) view.findViewById(R.id.text_5);

        }
    }
}