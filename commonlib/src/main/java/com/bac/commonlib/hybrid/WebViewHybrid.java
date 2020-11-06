package com.bac.commonlib.hybrid;

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

import com.bac.commonlib.R;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;

/**
 * Created by wujiazhen on 2017/8/28.
 */

public class WebViewHybrid extends AutomaticRxAppCompatActivity {
    private WebView mWebView;
    private ProgressBar mPb;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cl_webview_activity);

        intent = getIntent();

        UIUtil.initToolBar(this,intent.getStringExtra("title"),null);

        mWebView = findViewById(R.id.wb);
        mWebView.setWebViewClient(new BacWebClient());
        mWebView.setWebChromeClient(new BacWebChromeClient());

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadsImagesAutomatically(true) ;//支持自动加载图片
        webSettings.setBlockNetworkImage(false);

        mPb = findViewById(R.id.pb);

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

//    @NonNull
//    @Override
//    public <T> LifecycleTransformer<T> bindUntilEvent() {
//        return null;
//    }

    /**
     * 处理Javascript的对话框、网站图标、网站title、加载进度等
     */
    private class BacWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mPb.setProgress(newProgress);
            if (newProgress == 100) {
                mPb.setVisibility(View.GONE);
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

        ((ViewGroup)mWebView.getParent()).removeAllViews();
    }
}
