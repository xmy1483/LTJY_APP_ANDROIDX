package com.bac.bacplatform.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.bean.MsgBean;
import com.bac.bacplatform.http.GrpcTask;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity;
import com.bac.bihupapa.bean.Method;
import com.bac.bihupapa.grpc.TaskPostExecute;
import com.bumptech.glide.Glide;

import java.util.List;

public class HomeFragmentMsgListViewAdapter extends RecyclerView.Adapter<HomeFragmentMsgListViewAdapter.MsgViewHolder>{
    private List<MsgBean> listData;
    private Context context;

    public HomeFragmentMsgListViewAdapter(List<MsgBean> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_msg,viewGroup,false);
        return new MsgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder msgViewHolder, int i) {
        final MsgBean bean = listData.get(i);
        ImageView icon = (ImageView) msgViewHolder.getViewById(R.id.item_img);
        TextView txtTitle = (TextView) msgViewHolder.getViewById(R.id.txt_title);
        TextView txtDate = (TextView) msgViewHolder.getViewById(R.id.txt_date);
        TextView txtDesc = (TextView) msgViewHolder.getViewById(R.id.txt_desc);
        View redDot= msgViewHolder.getViewById(R.id.msg_red_point);
        Glide.with(context).load(bean.getIcon_url()).into(icon);
        txtTitle.setText(bean.getType_name());
        txtDesc.setText(bean.getBrief_desc());
        txtDate.setText(bean.getNotify_time());
        if(bean.getRead_status().equals("0")) {
            redDot.setVisibility(View.VISIBLE);
        } else {
            redDot.setVisibility(View.INVISIBLE);
        }

        msgViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnToWebView(bean);
                markMsgRead(bean);
            }
        });
    }

    private void turnToWebView(MsgBean bean) {
        Intent it = new Intent(context, WebAdvActivity.class)
                .putExtra("title", bean.getType_name())
                .putExtra("ads_url", bean.getTarget_url());
        context.startActivity(it);
    }

    private void markMsgRead(final MsgBean bean) {
        if(bean.getRead_status().equals("1")){
            return;
        }
        Method m = new Method();
        m.setMethodName("MARK_NOTIFICATION_READ");
        m.put("id",bean.getId());
        m.put("login_phone", BacApplication.getLoginPhone());
        new GrpcTask(null, m, null, new TaskPostExecute() {
            @Override
            public void onPostExecute(Method result) {
                if(result.getErrorId() == 0) {
                    bean.setRead_status("1");
                    notifyDataSetChanged();
                }
            }
        }).execute();
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
