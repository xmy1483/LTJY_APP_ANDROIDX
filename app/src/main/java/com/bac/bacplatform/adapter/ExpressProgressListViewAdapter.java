package com.bac.bacplatform.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bac.bacplatform.R;
import com.bac.bacplatform.bean.ExpressBean;
import com.bac.bacplatform.bean.ExpressUserInfoBean;
import com.bac.bacplatform.utils.ui.UIUtils;
import java.util.List;

public class ExpressProgressListViewAdapter extends RecyclerView.Adapter<ExpressProgressListViewAdapter.MsgViewHolder>{
    private List<ExpressBean> listData;
    private Context context;
    private ExpressUserInfoBean headBean;

    public void setHeadBean(ExpressUserInfoBean headBean) {
        this.headBean = headBean;
    }

    public ExpressProgressListViewAdapter(List<ExpressBean> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if(i != 0) {
            view = LayoutInflater.from(context).inflate(R.layout.item_express_layout,viewGroup,false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.header_express_list,viewGroup,false);
        }
        return new MsgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder holder, int i) {
        if(i == 0) {
            if(headBean!=null){
                String str1 = headBean.getUserName()+" "+ headBean.getUserPhone();
                ((TextView) holder.getViewById(R.id.txt_status)).setText(str1);
                ((TextView) holder.getViewById(R.id.txt_progress)).setText(headBean.getUserAddress());
                String expStr = "快递单号："+headBean.getPost_no();
                ((TextView) holder.getViewById(R.id.txt_compress_company)).setText(expStr);
            }
            return;
        }
        if(i == listData.size()) {
            ViewGroup.LayoutParams lm = holder.itemView.getLayoutParams();
            lm.height = UIUtils.dp2px(50);
            holder.itemView.setLayoutParams(lm);
        }
        if(listData.size() == 0)return;
        ExpressBean bean = listData.get(i-1);
        TextView txtYm = (TextView) holder.getViewById(R.id.txt_ym);
        txtYm.setText(bean.getTime_ymd());
        TextView txtHms = (TextView) holder.getViewById(R.id.txt_hms);
        txtHms.setText(bean.getTime_hms());
        TextView txtProgress = (TextView) holder.getViewById(R.id.txt_progress);
        txtProgress.setText(bean.getSituation());
        TextView txtStatus = (TextView) holder.getViewById(R.id.txt_status);
        txtStatus.setText(bean.getStatus());
        ImageView imgTag = (ImageView) holder.getViewById(R.id.img_tag);
        imgTag.setImageResource(bean.getIconId());

        if(i == 1) {
            txtYm.setTextColor(Color.parseColor("#666666"));
            txtHms.setTextColor(Color.parseColor("#666666"));
            txtStatus.setTextColor(Color.parseColor("#666666"));
            txtProgress.setTextColor(Color.parseColor("#666666"));
            txtYm.setText(bean.getTime_ymd());
            txtHms.setText(bean.getTime_hms());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listData.size()+1;
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
