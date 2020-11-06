package com.bac.bihupapa.activity;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bac.bihupapa.R;
import com.bac.commonlib.utils.ui.CircleProgressbar;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
import com.bumptech.glide.Glide;
import com.wjz.weexlib.weex.activity.WeexActivity2;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.module.splash
 * 创建人：Wjz
 * 创建时间：2017/4/12
 * 类描述：
 * app的启动页
 * fragment的托管页面
 * 管理启动页的生命周期
 */

public class BhppSplashActivity extends AutomaticRxAppCompatActivity {

    private CircleProgressbar mCircleProgressbar;
    private ImageView iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.bhpp_splash_acitivity);

        iv = findViewById(R.id.ivb);

        mCircleProgressbar = findViewById(R.id.tv_red_skip);
        mCircleProgressbar.setOutLineColor(Color.TRANSPARENT);
        mCircleProgressbar.setInCircleColor(Color.parseColor("#505559"));
        mCircleProgressbar.setProgressColor(Color.parseColor("#1BB079"));
        mCircleProgressbar.setProgressLineWidth(5);
        mCircleProgressbar.setTimeMillis(5000);
        mCircleProgressbar.reStart();

        mCircleProgressbar.setCountdownProgressListener(1, progressListener);

        // 点击跳转
        mCircleProgressbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBhppWeexActivity();
            }
        });

        Glide.with(this).load(R.mipmap.bhpp_bhpp1).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBhppWeexActivity();
            }
        });

    }

    private CircleProgressbar.OnCountdownProgressListener progressListener = new CircleProgressbar.OnCountdownProgressListener() {
        @Override
        public void onProgress(int what, int progress) {

            // 5s 结束跳转
            if (what == 1 && progress == 100) {
                startBhppWeexActivity();
            }

        }
    };

    /**
     * 启动壁虎啪啪 的weex 页面
     */
    private void startBhppWeexActivity() {
        mCircleProgressbar.stop();
        UIUtil.startActivityInAnimAndFinishSelf(this, new Intent(this, WeexActivity2.class)
               .setData(Uri.parse("file://file/dist/index.js")));
    }


}
