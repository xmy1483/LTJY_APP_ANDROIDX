package com.bac.bacplatform.module.kaiyoubao.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bac.bacplatform.R;

import java.util.List;
import java.util.Map;

import static com.bac.bacplatform.utils.tools.CountDown.format_yyyy_mm_dd_hh_mm_ss1;


/**
 * Created by guke on 2017/5/22.
 */

public class KaiYouBaoRVAdapter extends RecyclerView.Adapter {
    private List<Map<String, Object>> maps;
    private Context context;

    public KaiYouBaoRVAdapter(List<Map<String, Object>> maps, Context context) {
        this.maps = maps;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.kaiyoubao_item,null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).tv01.setText(maps.get(position).get("content")+"");
        ((MyViewHolder) holder).tv02.setText(maps.get(position).get("action_money")+"");
        String s = maps.get(position).get("action_money")+"";
        float i = Float.parseFloat(s);
        if (i < 0){
            ((MyViewHolder) holder).tv02.setText(i+"");
        }else {
            ((MyViewHolder) holder).tv02.setText("+"+i+"");
          }
        try{

            String pay_time = format_yyyy_mm_dd_hh_mm_ss1.format(maps.get(position).get("create_time"));


            ((MyViewHolder) holder).tv11.setText(pay_time);


        }catch (Exception e){}


    }

    @Override
    public int getItemCount() {
        return maps==null?0:maps.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv01;
        TextView tv11;
        TextView tv02;

        MyViewHolder(View view) {
            super(view);
            tv01 = (TextView) view.findViewById(R.id.tv_01);
            tv11 = (TextView) view.findViewById(R.id.tv_11);
            tv02 = (TextView) view.findViewById(R.id.tv_02);
        }
    }
}
