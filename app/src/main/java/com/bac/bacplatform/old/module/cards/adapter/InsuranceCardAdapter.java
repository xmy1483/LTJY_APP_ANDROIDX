package com.bac.bacplatform.old.module.cards.adapter;

import com.bac.bacplatform.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.Map;

import static com.bac.bacplatform.utils.tools.CountDown.format2;

/**
 * Created by Wjz on 2017/5/25.
 */

public class InsuranceCardAdapter extends BaseQuickAdapter<Map<String, Object>,BaseViewHolder> {

    public InsuranceCardAdapter(int layoutResId, List<Map<String, Object>> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Map<String, Object> map) {
        String name = String.valueOf(map.get("name"));
        String explain_text = String.valueOf(map.get("explain_text"));
        String str = name.concat("\n").concat(explain_text);

        String create_time = format2.format(map.get("create_time"));
        String expired = format2.format(map.get("expired"));
        String data = create_time.concat(" ~ ").concat(expired);

        baseViewHolder.setText(R.id.tv_insurance_card_money, String.valueOf(map.get("voucher_money")))
                .setText(R.id.tv_001, str)
                .setText(R.id.tv_002, data)
                .addOnClickListener(R.id.tv_card);
    }
}
