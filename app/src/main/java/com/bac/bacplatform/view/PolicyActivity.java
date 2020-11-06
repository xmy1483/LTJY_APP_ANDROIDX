package com.bac.bacplatform.view;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import com.bac.bacplatform.R;

public class PolicyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.policy_layout);
        initView();
    }

    private void initView(){
        WebView webView = findViewById(R.id.web_view_id);
        webView.loadUrl("file:///android_asset/policy.html");
        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
