package com.bac.bacplatform.module.recharge.adapter;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.widget.TextView;

import com.bac.bacplatform.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.bac.bacplatform.conf.Constants.CommonProperty._10;
import static com.bac.bacplatform.conf.Constants.CommonProperty._11;

/**
 * Created by Wjz on 2017/5/8.
 */

public class KaiYouBaoRechargeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public KaiYouBaoRechargeAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String string) {
        Context context = helper.itemView.getContext();
        TextView tv = helper.getView(R.id.tv_01);
        helper.getView(R.id.fl).setTag(string);

        if (string.equals(_10 + "")) {
            tv.setText("");
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            helper.setBackgroundColor(R.id.fl,ContextCompat.getColor(context, R.color.line));
        } else if (string.equals(_11 + "")) {
            tv.setText("");

            tv.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context,R.mipmap.key_delete), null, null, null);
            helper.setBackgroundColor(R.id.fl,ContextCompat.getColor(context, R.color.line));
        } else {
            tv.setText(string);
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            helper.setBackgroundColor(R.id.fl,ContextCompat.getColor(context, R.color.white));
        }
    }
}
