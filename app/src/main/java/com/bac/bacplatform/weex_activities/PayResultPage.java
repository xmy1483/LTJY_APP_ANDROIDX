package com.bac.bacplatform.weex_activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bac.bacplatform.R;

public class PayResultPage extends AppCompatActivity {
    private TextView txtOrderInfo,txtCardNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        bindView();
        loadData();
    }

    private void bindView() {
        txtOrderInfo = findViewById(R.id.field_info);
        txtCardNum = findViewById(R.id.field_card_no);
    }

    private void loadData() {
        Intent it = getIntent();
        String cn = it.getStringExtra("cardNo");
        String rm =it.getStringExtra("rechargeMoney");
        if(!TextUtils.isEmpty(cn)) {
            txtCardNum.setText(cn);
        }

        if(!TextUtils.isEmpty(rm)) {
            if(rm.contains(".")) {
                rm = rm.substring(0,rm.indexOf("."));
            }
            rm = "加油卡充值"+rm+"元";
            txtOrderInfo.setText(rm);
        }
    }

    public void onConfirmClick(View view) {
        finish();
    }
}
