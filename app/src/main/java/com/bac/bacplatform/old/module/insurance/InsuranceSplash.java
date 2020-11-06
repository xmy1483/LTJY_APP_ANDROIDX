package com.bac.bacplatform.old.module.insurance;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.insurance
 * 创建人：Wjz
 * 创建时间：2017/3/26
 * 类描述：
 */

public class InsuranceSplash extends SuperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.insurance_splash_activity);

        initToolBar("保险");

        ImageView iv = (ImageView) findViewById(R.id.iv);
        RxView.clicks(iv)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UIUtils.startActivityInAnimAndFinishSelf(InsuranceSplash.this, new Intent(InsuranceSplash.this, InsuranceHomeActivity.class));
                    }
                });


        Glide.with(this).load(getIntent().getStringExtra("url"))
                .placeholder(R.mipmap.load2)
                //不缓存加载
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(iv);

    }

}
