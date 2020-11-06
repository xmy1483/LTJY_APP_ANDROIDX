package com.bac.bihupapa.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bac.bihupapa.R;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.commonlib.view.RatioLayout;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;


/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.adv
 * 创建人：Wjz
 * 创建时间：2017/4/14
 * 类描述：
 */

public class CarAdvCaptureSample extends AutomaticRxAppCompatActivity {


    private RatioLayout view;
    private TextView tv01;
    private TextView tv02;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bhpp_capture_sample_activity);

        UIUtil.initToolBar(this, "拍摄车身广告照片", null);
        view = findViewById(R.id.view);
        tv01 = findViewById(R.id.tv_01);
        tv02 = findViewById(R.id.tv_02);
        btn = findViewById(R.id.btn);

        btn.setText("开始拍照");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarAdvCaptureSample.this.onBackPressed();
            }
        });

        Intent intent = getIntent();

        tv01.setText(intent.getStringExtra("label1"));
        tv02.setText(intent.getStringExtra("label2"));

        switch (intent.getIntExtra("alert", -1)) {
            case 1:
                view.setBackgroundResource(R.mipmap.bhpp_car_adv_carfront);
                break;
            case 2:
                view.setBackgroundResource(R.mipmap.bhpp_car_adv_carback);
                break;
        }

    }


}
