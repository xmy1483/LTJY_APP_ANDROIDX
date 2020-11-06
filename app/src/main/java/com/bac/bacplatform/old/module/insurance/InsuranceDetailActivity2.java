package com.bac.bacplatform.old.module.insurance;


import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bac.bacplatform.R;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.base.SuperFragment;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceHomeBean;
import com.bac.bacplatform.old.module.insurance.fragment.InsuranceDetailLeftFragment;
import com.bac.bacplatform.old.module.insurance.fragment.InsuranceDetailRightFragment;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.account
 * 创建人：Wjz
 * 创建时间：2017/1/4
 * 类描述：
 */

public class InsuranceDetailActivity2 extends SuperActivity {
    private TextView tv01;
    private TextView tv02;
    private Button btnInsurance;

    private InsuranceHomeBean mBean;
    private InsuranceDetailLeftFragment mInsuranceDetailLeftFragment;
    private InsuranceDetailRightFragment mInsuranceDetailRightFragment;
    private int mStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView2();
        initData();
        initEvent();

    }

    private void initView2() {
        setContentView(R.layout.insurance_detail_2_activity);

        initToolBar("报价详情");

        tv01 = (TextView) findViewById(R.id.tv_01);
        tv02 = (TextView) findViewById(R.id.tv_02);

        btnInsurance = (Button) findViewById(R.id.btn_insurance);

    }

    private void initData() {

        mBean = getIntent().getParcelableExtra("bean");
        mStatus = getIntent().getIntExtra("status", -1);


        mInsuranceDetailLeftFragment = new InsuranceDetailLeftFragment();
        mInsuranceDetailRightFragment = new InsuranceDetailRightFragment();
        Bundle args = new Bundle();
        args.putParcelable("bean", mBean);
        args.putBoolean("isShow", getIntent().getBooleanExtra("isShow", true));
        args.putInt("status", getIntent().getIntExtra("status", 1));
        args.putBoolean("is_upload_image", getIntent().getBooleanExtra("is_upload_image", false));
        args.putBoolean("is_upload_address", getIntent().getBooleanExtra("is_upload_address", false));

        mInsuranceDetailLeftFragment.setArguments(args);
        mInsuranceDetailRightFragment.setArguments(args);

        selectedColorAndBg(tv01);

        selectedFragment(mInsuranceDetailLeftFragment);
    }

    private void selectedFragment(SuperFragment fm) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_replace, fm);
        fragmentTransaction.commit();

    }


    private void restColorAndBg() {
        tv01.setBackgroundResource(R.drawable.shape_stroke_blue_solid_white);
        tv02.setBackgroundResource(R.drawable.shape_stroke_blue_solid_white);
        tv01.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv02.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void selectedColorAndBg(TextView tv) {
        tv.setBackgroundResource(R.drawable.shape_stroke_blue_solid_blue);
        tv.setTextColor(getResources().getColor(R.color.white));
    }

    private void initEvent() {
        tv01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restColorAndBg();
                selectedColorAndBg(tv01);
                selectedFragment(mInsuranceDetailLeftFragment);
            }
        });
        tv02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restColorAndBg();
                selectedColorAndBg(tv02);
                selectedFragment(mInsuranceDetailRightFragment);
            }
        });


        btnInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStatus == 2 || mStatus == 13) {
                    Toast.makeText(InsuranceDetailActivity2.this, "支付", Toast.LENGTH_SHORT).show();
                } else {
                    InsuranceDetailActivity2.this.onBackPressed();
                }
            }
        });
    }

}
