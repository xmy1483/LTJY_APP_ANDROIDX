package com.bac.bacplatform.old.module.hybrid;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.bac.bacplatform.R;
import com.bac.bacplatform.old.base.SuperActivity;


/**
 * Created by Wjz on 2017/5/9.
 */

public class WebAdvActivity2 extends SuperActivity {

    private WebView mWebView;
    private ProgressBar mPb;
    private Intent intent;

    // 需要隐藏的dom元素id
    private static final String[] HIDE_DOM_IDS = {"card_no_input"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.web_activity);

        intent = getIntent();

        initToolBar(intent.getStringExtra("title"));

        mWebView = (WebView) findViewById(R.id.wb);
        mWebView.setWebViewClient(new BacWebClient());
        mWebView.setWebChromeClient(new BacWebChromeClient());

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadsImagesAutomatically(true);//支持自动加载图片
        webSettings.setBlockNetworkImage(false);

        mPb = (ProgressBar) findViewById(R.id.pb);

        initLoadUrl();
    }

    private void initLoadUrl() {

        mWebView.loadUrl(intent.getStringExtra("ads_url"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWebView.stopLoading();
    }

    /**
     * 处理Javascript的对话框、网站图标、网站title、加载进度等
     */
    private class BacWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mPb.setProgress(newProgress);
            if (newProgress == 100) {
                // 进度 100 表示 dom 树加载完成
                mPb.setVisibility(View.GONE);
                String domOperationStatements = getDomOperationStatements(HIDE_DOM_IDS);
                System.out.println(domOperationStatements);
                mWebView.loadUrl(domOperationStatements);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    /**
     * 处理各种通知、请求事件
     */
    private class BacWebClient extends WebViewClient {

        // 在当前webview中加载
        @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

            System.out.println("拦截地址"+url);

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            handler.proceed();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            // 加载本地页面，进行出错处理
            view.loadUrl("file:///android_asset/NetworkError.html");
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.loadUrl("about:blank");
        mWebView.removeAllViews();
        mWebView.destroy();

        ((ViewGroup) mWebView.getParent()).removeAllViews();
    }


    public static String getDomOperationStatements(String[] hideDomIds) {
        StringBuilder builder = new StringBuilder();
        // add javascript prefix
        builder.append("javascript:(function() { ");
//        for (String domId : hideDomIds) {
//            builder.append("var item = document.getElementById('")
//                    .append(domId).append("');");
//            builder.append("item.parentNode.removeChild(item);");
//        }

       /* builder.append(
                "var reg = new RegExp(\"(^|&)card_no=([^&]*)(&|$)\");\n" +
                " var card_no = window.location.search.substr(1).match(reg)[2];\n" +
                "$(\"#card_no_input\").val(\"100 011 320 001 804 643 4\");");*/

        // add javascript suffix
        
        builder.append("var reg = new RegExp(\"(^|&)card_no=([^&]*)(&|$)\");" +
                "var card = window.location.search.substr(1).match(reg)[2];" +
                "var card_no = \"\";" +
                "" +
                "var length = card.length-card.length%3;" +
                "for (var i = 0; i < length; i++) {" +
                "if(i%3==0){" +
                "card_no += card.substring(i, i + 3) + \" \";" +
                "}" +
                "" +
                "}" +
                "if(card.length%3!=0){" +
                "card_no+= card.substring(card.length-card.length%3-1, card.length-1);" +
                "}" +
                "" +
                "$(\"#card_no_input\").val(card_no);" +
                "requestCardInfo(card_no);");
        
        builder.append("})()");
        return builder.toString();
    }
    
}
