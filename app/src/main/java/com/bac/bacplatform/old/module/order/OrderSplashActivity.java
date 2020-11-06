package com.bac.bacplatform.old.module.order;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.bac.bacplatform.R;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity;
import com.bac.bacplatform.utils.ui.UIUtils;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.order
 * 创建人：Wjz
 * 创建时间：2017/3/23
 * 类描述：
 */

public class OrderSplashActivity extends SuperActivity implements View.OnClickListener {

    private CheckBox cb;
    private TextView tv;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_splash_activity);

        initToolBar("办卡章程");
        cb = (CheckBox) findViewById(R.id.cb);
        tv = (TextView) findViewById(R.id.tv);

        btn = (Button) findViewById(R.id.btn);


        btn.setText("同意办理");

        tv.setOnClickListener(this);

        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv:
                //打开广告
                String url = "https://mp.weixin.qq.com/s?__biz=MzA3ODk0MzI3Nw==&mid=503204108&idx=1&sn=b78b0fce021e5401cbd7e6ccb44d422f&chksm=07b1ec4e30c66558095ccfc637e8f2b03e35a0bae618d1556e4a0d72df861ee48ad297d8fab1&scene=0&key=3c01354203bd85a6cba88ae63d783639ded26d37cb1510141727164d1a81c73138643e3777a0772ae1ce087547f239c1311ac22b53a04e50cad03dd69bc28e442473de29feaa9c0306b8b3460f013b0c&ascene=0&uin=NTIyODQ0MzQw&devicetype=iMac+MacBookPro9%2C1+OSX+OSX+10.12.3+build(16D32)&version=11020201&pass_ticket=zdQb%2B8uIWtBVkwDIxFWpHttP%2FruPD2hiyOWhz%2F61ojci2qF8%2BYKSS8a9Pq8n4zqD";

                Intent intentToWash = new Intent(OrderSplashActivity.this,
                        WebAdvActivity.class);
                intentToWash.putExtra("title", "办卡章程");
                intentToWash.putExtra("urltype", "orderCard");
                intentToWash.putExtra("ads_url", url);
                UIUtils.startActivityInAnim(OrderSplashActivity.this, intentToWash);
                break;
            case R.id.btn:
                goToNext();
                break;
        }
    }


    private void goToNext() {
        if (!cb.isChecked()) {
            Toast.makeText(this, "请勾选《中国石化加油卡章程》", Toast.LENGTH_SHORT).show();
            return;
        }

        UIUtils.startActivityInAnim(this,new Intent(OrderSplashActivity.this, OrderHomeActivity.class));
    }
}
