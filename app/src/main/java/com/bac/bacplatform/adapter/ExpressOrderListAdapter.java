package com.bac.bacplatform.adapter;

import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bac.bacplatform.R;
import com.bac.bacplatform.activity.expressprogress.ExpressActivity;
import com.bac.bacplatform.bean.ExpressOrderBean;
import com.bac.bacplatform.utils.ui.UIUtils;
import java.util.List;

public class ExpressOrderListAdapter extends RecyclerView.Adapter<ExpressOrderListAdapter.MsgViewHolder>{
    private List<ExpressOrderBean> listData;
    private AppCompatActivity context;

    public ExpressOrderListAdapter(List<ExpressOrderBean> listData, AppCompatActivity context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_express_order_layout,viewGroup,false);
        return new MsgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder holder, int i) {
        final ExpressOrderBean bean = listData.get(i);
        TextView txtQuery = (TextView) holder.getViewById(R.id.txt_query);
        TextView txtStatus = (TextView) holder.getViewById(R.id.txt_status);
        TextView txtExpNo = (TextView) holder.getViewById(R.id.txt_express_no); //快递单号：2323849374665
        TextView txtCardName = (TextView) holder.getViewById(R.id.txt_product_name); // 加油卡
        TextView txtBuyCount = (TextView) holder.getViewById(R.id.txt_buy_count); // 数量：2涨
        TextView txtOid = (TextView) holder.getViewById(R.id.txt_order_id); // 订单号:2999299342638565
        TextView txtTime = (TextView) holder.getViewById(R.id.txt_time);

        txtStatus.setText(bean.getHadPost());
        txtExpNo.setText("快递单号："+bean.getPost_no());
        txtCardName.setText(bean.getType_name());
        txtBuyCount.setText("数量："+bean.getCnums()+"张");
        txtOid.setText("订单号："+bean.getOut_trade_no());
        txtTime.setText("时间："+bean.getCreate_time());


        if(bean.getHadPost().equals("未发货")) {
            txtQuery.setTextColor(Color.parseColor("#888888"));
        } else {
            txtQuery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(context, ExpressActivity.class);
                    it.putExtra("orderId",bean.getOut_trade_no());
                    UIUtils.startActivityInAnim(context,it);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MsgViewHolder extends RecyclerView.ViewHolder {
        MsgViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public View getViewById(int id) {
            return itemView.findViewById(id);
        }
    }
}
