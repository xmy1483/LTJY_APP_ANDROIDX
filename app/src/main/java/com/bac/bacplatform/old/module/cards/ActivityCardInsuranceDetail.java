package com.bac.bacplatform.old.module.cards;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.R;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.insurance.InsuranceHomeActivity;
import com.bac.bacplatform.utils.ui.UIUtils;

import java.util.Map;

import static com.bac.bacplatform.utils.tools.CountDown.format2;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.card
 * 创建人：Wjz
 * 创建时间：2016/12/12
 * 类描述：
 */

public class ActivityCardInsuranceDetail extends SuperActivity {

    private TextView tv001;
    private TextView tv002;
    private TextView tv003;
    private TextView tv004;

    private Button btnUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView2();
        initDate();
        initEvent();
    }

    private void initView2() {
        setContentView(R.layout.insurance_card_detail_activity);

        initToolBar("保险抵用券");
        tv001 = (TextView) findViewById(R.id.tv_001);
        tv002 = (TextView) findViewById(R.id.tv_002);
        tv003 = (TextView) findViewById(R.id.tv_003);
        tv004 = (TextView) findViewById(R.id.tv_004);

        btnUse = (Button) findViewById(R.id.btn);
        btnUse.setText("确认使用");

    }

    private void initDate() {
        String string = getIntent().getStringExtra("string");
        Map<String, Object> map = JSON.parseObject(string, new TypeReference<Map<String, Object>>() {
        }.getType());

        tv001.setText("抵用券号：");
        tv001.append(String.valueOf(map.get("voucher_id")));

        String create_time = format2.format(map.get("create_time"));
        String expired = format2.format(map.get("expired"));
        tv002.setText("使用期限：");
        tv002.append(create_time);
        tv002.append(" 至 ");
        tv002.append(expired);

        String use_method = String.valueOf(map.get("use_method")).replace("##", "\n");
        tv003.setText(use_method);

        String use_explain = String.valueOf(map.get("use_explain")).replace("##", "\n");
        tv004.setText(use_explain);

    }

    private void initEvent() {

        btnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.startActivityInAnimAndFinishSelf(ActivityCardInsuranceDetail.this, new Intent(ActivityCardInsuranceDetail.this, InsuranceHomeActivity.class));
            }
        });
    }
}
