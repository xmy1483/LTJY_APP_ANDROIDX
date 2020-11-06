package com.bac.bacplatform.adapter;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.bean.JdBean;
import com.bac.bacplatform.bean.MsgBean;
import com.bac.bacplatform.http.GrpcTask;
import com.bac.bacplatform.module.login.LoginActivity;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bihupapa.bean.Method;
import com.bac.bihupapa.grpc.TaskPostExecute;
import com.bumptech.glide.Glide;
import java.util.List;

import io.flutter.embedding.android.FlutterActivity;

public class HomeFragmentJdCardListViewAdapter extends RecyclerView.Adapter<HomeFragmentJdCardListViewAdapter.MsgViewHolder>{
    private List<JdBean> listData;
    private Context context;

    public HomeFragmentJdCardListViewAdapter(List<JdBean> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_jd_card_item,viewGroup,false);
        return new MsgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder msgViewHolder, int i) {
        JdBean bean = listData.get(i);
        ImageView img = (ImageView) msgViewHolder.getViewById(R.id.img_card);
        TextView txtName = (TextView) msgViewHolder.getViewById(R.id.id_name);
        TextView txtNowPrice= (TextView) msgViewHolder.getViewById(R.id.txt_price_now);
        TextView txtOriginPrice = (TextView) msgViewHolder.getViewById(R.id.txt_price_origin);
        txtName.setText(bean.getName());
        txtNowPrice.setText(bean.getSalePrice());
        txtOriginPrice.setText(bean.getOriginPrice());
        if(!TextUtils.isEmpty(bean.getImgUrl())) {
            Glide.with(context).load(bean.getImgUrl()).into(img);
        }
        msgViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toFlutterPage();
           /*     if(getIsLogin()) {
                    UIUtil.startActivityInAnim((AppCompatActivity) context, new Intent(context, WeexActivity2.class)
                            .setData(Uri.parse("https://app5.bac365.com:10443/app.pay/photo/weex/Card/dist/giftcardsPurchase.js")));
                }*/
            }
        });
    }

    private void toFlutterPage() {
        Intent it = FlutterActivity
                .withNewEngine()
//                .initialRoute("pageSec")
                .build(context);
        context.startActivity(it);
    }

    private boolean getIsLogin() {
        if (!PreferenceManager.getDefaultSharedPreferences(context).contains("certificate")) {
            UIUtils.startActivityInAnim((AppCompatActivity) context, new Intent(context, LoginActivity.class));
            return false;
        }
        return true;
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
