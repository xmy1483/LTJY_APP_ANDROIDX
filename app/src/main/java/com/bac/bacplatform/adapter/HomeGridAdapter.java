package com.bac.bacplatform.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bac.bacplatform.R;
import com.bac.bacplatform.bean.FuncBean;
import com.bac.bacplatform.module.main.view.HomeFragment;

import java.util.List;

public class HomeGridAdapter extends RecyclerView.Adapter<HomeGridAdapter.MsgViewHolder>{
    private List<FuncBean> listData;
    private Context context;
    private HomeFragment fragment;

    public HomeGridAdapter(List<FuncBean> listData,HomeFragment fragment) {
        this.listData = listData;
        this.fragment = fragment;
        this.context = fragment.activity;
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_function,viewGroup,false);
        return new MsgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder msgViewHolder, int i) {
        final FuncBean bean = listData.get(i);
        TextView txt = (TextView) msgViewHolder.getViewById(R.id.txt_func);
        bean.setTxtBt(txt,fragment);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MsgViewHolder extends RecyclerView.ViewHolder {
        MsgViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        View getViewById(int id) {
            return itemView.findViewById(id);
        }
    }
}
