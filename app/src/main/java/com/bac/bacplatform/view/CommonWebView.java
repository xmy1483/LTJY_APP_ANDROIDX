package com.bac.bacplatform.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;
import com.bac.bacplatform.R;

public class CommonWebView extends AppCompatActivity {
    private TextView txtTitle;
    private WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_web_view);
        initView();
        getDate();
    }

    private void getDate(){
        Intent intent = getIntent();
        final String targetUrl = intent.getStringExtra("target_url");
        String title = intent.getStringExtra("title");
        if(TextUtils.isEmpty(targetUrl)){
            Toast.makeText(this, "网页不存在", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            webView.loadUrl(targetUrl);
            WebSettings set = webView.getSettings();
            set.setJavaScriptEnabled(true);
            webView.reload();
        }

        if(!TextUtils.isEmpty(title)){
            txtTitle.setText(title);
        }
    }

    private void initView(){
        webView = findViewById(R.id.web_view_id);
        txtTitle = findViewById(R.id.txt_title);
        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
