package com.bac.bacinnermanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guke on 2017/8/18.
 */

public class SecondAdapter extends BaseAdapter{
    private Context context;
    private List<String> data;
    private List<String> data3;

    public SecondAdapter(Context context, List<String> data, List<String> data3) {
        this.context = context;
        this.data = data;
        this.data3 = data3;
    }


    @Override
    public int getCount() {
        return data!=null?data.size():0;
    }

    @Override
    public Object getItem(int position) {
        List<String> datalist = new ArrayList<>();
        datalist.add(data.get(position));
        datalist.add(data3.get(position));
        return datalist;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }


}
