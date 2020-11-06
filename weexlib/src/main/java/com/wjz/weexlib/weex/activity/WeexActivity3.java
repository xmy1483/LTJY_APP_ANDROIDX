package com.wjz.weexlib.weex.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.wjz.weexlib.R;

/**
 * Created by wujiazhen on 2017/8/29.
 * 阿里提供
 */

public class WeexActivity3 extends AbsWeexActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_weex_activity);
        mContainer = findViewById(R.id.fl);
        // loadUrl
        String url = "http://172.16.124.157:8080/dist/index.js";
        url = getIntent().getStringExtra("url");
        loadUrl(url);

    }



}