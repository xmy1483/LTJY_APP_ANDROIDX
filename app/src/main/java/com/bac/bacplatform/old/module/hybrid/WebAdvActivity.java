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
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;

/**
 * Created by Wjz on 2017/5/9.
 */

public class WebAdvActivity extends SuperActivity {

    private WebView mWebView;
    private ProgressBar mPb;
    private Intent intent;
    private boolean net_is_ok;
    private boolean js_is_ok;
    private String card_no;
    private boolean isAliPay;
    private TextView title_button;
    private boolean isETC;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.web_activity);

        findViewById(R.id.iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        intent = getIntent();

        ((TextView)findViewById(R.id.tv_center)).setText(intent.getStringExtra("title"));
        title_button = (TextView)findViewById(R.id.tv_button);

        mWebView = (WebView) findViewById(R.id.wb);
        mWebView.setWebViewClient(new BacWebClient());
        mWebView.setWebChromeClient(new BacWebChromeClient());

        WebSettings webSettings = mWebView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadsImagesAutomatically(true);//支持自动加载图片
        webSettings.setBlockNetworkImage(false);

        mPb = (ProgressBar) findViewById(R.id.pb);

        initLoadUrl();
        if (getIntent().getStringExtra("ads_url").contains("wxHosted/raw/ticket/jumpEntryInfo")){
            isETC = true;
            setJSCode();
        }else {
            getData();
        }
    }

    private void getData() {
        HttpHelper.getInstance()
                .bacNetWithContext(activity,
                        new BacHttpBean()
                                .setMethodName("BASEXML.QUERY_RAND_CARD")
                                .put("charge_mobile", BacApplication.getLoginPhone() + "")
                                .put("login_phone", BacApplication.getLoginPhone() + "")
                ).compose(activity.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(activity))
                .observeOn(RxScheduler.RxPoolScheduler())
                // .map(new JsonFunc1<String,List<Map<String,Object>>>())
                .map(new Func1<String, List<Map<String, Object>>>() {
                    @Override
                    public List<Map<String, Object>> call(String s) {
                        return JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>(){
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        Map<String, Object> stringObjectMap = maps.get(_0);//0
                        card_no = stringObjectMap.get("card_no").toString();
                        net_is_ok = true;
                        setJSCode();
                    }
                });
    }

    private void setJSCode() {
        if(net_is_ok && js_is_ok){
            String js = "(function() {var item = document.getElementById(\"card_no_input\");";
            js += "item.value = ";
            js +=  "\""+ card_no +"\";"; //xxx()代表js中某方法
            js += "item.style.display=\"none\";})()";
            mWebView.loadUrl("javascript:" + js);
        }
        if (isAliPay && js_is_ok){
            String js = "(function() {var item = document.getElementById(\"androidSubmit\"); item.style.display = \"none\";})()";
            mWebView.loadUrl("javascript:" + js);
            title_button.setVisibility(View.VISIBLE);
            title_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        if (isETC && js_is_ok){
            String js = "(function() {var item = document.getElementById(\"tel\");";
            js += "item.value = ";
            js += "\""+ BacApplication.getLoginPhone() + "\";})()";
            mWebView.loadUrl("javascript:" + js);
        }
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
            js_is_ok = false;

            if (newProgress == 100) {
                mPb.setVisibility(View.GONE);
                js_is_ok = true;
                setJSCode();
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
            System.out.println("当前的地址是————————"+url);
            System.out.println("获取到的地址是————————"+mWebView.getUrl());
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

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if (url.contains("doWapPayment")){
                isAliPay = true;
                setJSCode();
            }
            return super.shouldInterceptRequest(view, url);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //System.out.println("页面当前的url"+mWebView.getUrl());
//12.26 拦截洗车第三方首页，点击回退按钮销毁
        if (mWebView.getUrl().startsWith("http://dhwap.mlocso.com:8888/carwash/html/carwash.html?app=01325011&p=cwash_main")){
            finish();
            return true;
        }else {
            if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                mWebView.goBack();// 返回前一个页面
                return true;
            }

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
