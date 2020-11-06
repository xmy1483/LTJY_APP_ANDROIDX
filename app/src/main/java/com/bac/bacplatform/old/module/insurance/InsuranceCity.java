package com.bac.bacplatform.old.module.insurance;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bac.bacplatform.R;
import com.bac.bacplatform.old.base.SuperActivity;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.insurance
 * 创建人：Wjz
 * 创建时间：2016/12/6
 * 类描述：
 */

public class InsuranceCity extends SuperActivity implements View.OnClickListener {
    private RelativeLayout titleLayout;
    private RelativeLayout rvBackContainer;
    private ImageView imgBack;
    private TextView textBack;
    private RelativeLayout titleCenter;
    private TextView centerText;
    private TextView tvRight;
    private TextView txtNanjing;
    private TextView txtWuxi;
    private TextView txtXuzhou;
    private TextView txtChangzhou;
    private TextView txtSuzhou;
    private TextView txtNantong;
    private TextView txtLianyungang;
    private TextView txtHuaian;
    private TextView txtYancheng;
    private TextView txtYangzhou;
    private TextView txtZhenjiang;
    private TextView txtTaizhou;
    private TextView txtSuqian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insurance_city_activity);

        initToolBar("选择城市");


        txtNanjing = (TextView) findViewById(R.id.txt_nanjing);
        txtWuxi = (TextView) findViewById(R.id.txt_wuxi);
        txtXuzhou = (TextView) findViewById(R.id.txt_xuzhou);
        txtChangzhou = (TextView) findViewById(R.id.txt_changzhou);
        txtSuzhou = (TextView) findViewById(R.id.txt_suzhou);
        txtNantong = (TextView) findViewById(R.id.txt_nantong);
        txtLianyungang = (TextView) findViewById(R.id.txt_lianyungang);
        txtHuaian = (TextView) findViewById(R.id.txt_huaian);
        txtYancheng = (TextView) findViewById(R.id.txt_yancheng);
        txtYangzhou = (TextView) findViewById(R.id.txt_yangzhou);
        txtZhenjiang = (TextView) findViewById(R.id.txt_zhenjiang);
        txtTaizhou = (TextView) findViewById(R.id.txt_taizhou);
        txtSuqian = (TextView) findViewById(R.id.txt_suqian);

        txtNanjing.setOnClickListener(this);
        txtWuxi.setOnClickListener(this);
        txtXuzhou.setOnClickListener(this);
        txtChangzhou.setOnClickListener(this);
        txtSuzhou.setOnClickListener(this);
        txtNantong.setOnClickListener(this);
        txtLianyungang.setOnClickListener(this);
        txtHuaian.setOnClickListener(this);
        txtYancheng.setOnClickListener(this);
        txtYangzhou.setOnClickListener(this);
        txtZhenjiang.setOnClickListener(this);
        txtTaizhou.setOnClickListener(this);
        txtSuqian.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        String txt_str = "";
        switch (v.getId()) {
            case R.id.txt_nanjing:
                txt_str = txtNanjing.getText().toString().trim();
                break;
            case R.id.txt_wuxi:
                txt_str = txtWuxi.getText().toString().trim();
                break;
            case R.id.txt_xuzhou:
                txt_str = txtXuzhou.getText().toString().trim();
                break;
            case R.id.txt_changzhou:
                txt_str = txtChangzhou.getText().toString().trim();
                break;
            case R.id.txt_suzhou:
                txt_str = txtSuzhou.getText().toString().trim();
                break;
            case R.id.txt_nantong:
                txt_str = txtNantong.getText().toString().trim();
                break;
            case R.id.txt_lianyungang:
                txt_str = txtLianyungang.getText().toString().trim();
                break;
            case R.id.txt_huaian:
                txt_str = txtHuaian.getText().toString().trim();
                break;
            case R.id.txt_yancheng:
                txt_str = txtYancheng.getText().toString().trim();
                break;
            case R.id.txt_yangzhou:
                txt_str = txtYangzhou.getText().toString().trim();
                break;
            case R.id.txt_zhenjiang:
                txt_str = txtZhenjiang.getText().toString().trim();
                break;
            case R.id.txt_taizhou:
                txt_str = txtTaizhou.getText().toString().trim();
                break;
            case R.id.txt_suqian:
                txt_str = txtSuqian.getText().toString().trim();
                break;
        }
        Intent intent = new Intent();
        intent.putExtra("city", txt_str);
        setResult(1000, intent);

        InsuranceCity.this.onBackPressed();
    }
}
