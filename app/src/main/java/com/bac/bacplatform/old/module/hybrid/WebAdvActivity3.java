package com.bac.bacplatform.old.module.hybrid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by guke on 2017/12/21.
 */

public class WebAdvActivity3 extends SuperActivity {

    private WebView mWebView;
    private ImageView vt;
    private ProgressBar mPb;
    private Intent intent;
    private boolean net_is_ok;
    private boolean js_is_ok;
    private String card_no;
    private boolean isAliPay;
    private TextView title_button;
    private boolean isETC;
    private static boolean b = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.web_activity);

        intent = getIntent();

        ((TextView)findViewById(R.id.tv_center)).setText(intent.getStringExtra("title"));
        title_button = (TextView)findViewById(R.id.tv_button);
        vt=(ImageView)findViewById(R.id.iv);
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
        vt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("---------------------:"+mWebView.getUrl());
                if (mWebView.getUrl().startsWith("https://m.che300.com/partner")
                        ||mWebView.getUrl().startsWith("http://app.bac365.com/wxHosted/raw/ticket/jumpEntryInfo")
                        ||mWebView.getUrl().startsWith("http://www.js.10086.cn/alife/jsp/fuelcardCharge")
                        ||mWebView.getUrl().startsWith("https://test.bac365.com:10433/life_number/servlet/sideCardIndex")){
                    onBackPressed();
                }else{
                    mWebView.goBack();
                }
            }
        });
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
                        Map<String, Object> stringObjectMap = maps.get(0);//0
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

            // 获取上下文, H5PayDemoActivity为当前页面
            final Activity context = WebAdvActivity3.this;
            // ------  对alipays:相关的scheme处理 -------
            System.out.println("============"+url);
            Log.i("webview的url——1", mWebView.getUrl());
            Log.i("webview的url——2", url);


            if(url.startsWith("https://mapi.alipay.com")) {
                try {
                    Intent intentToPay = new Intent(WebAdvActivity3.this, ZhiFuBaoActivity.class);
                    intentToPay.putExtra("paymentUrl", url);
                    UIUtils.startActivityInAnim(activity,intentToPay);
                } catch (Exception e) {
                    new AlertDialog.Builder(context)
                            .setMessage("未检测到支付宝客户端，请安装后重试。")
                            .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Uri alipayUrl = Uri.parse("https://d.alipay.com");
                                    context.startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
                                }
                            }).setNegativeButton("取消", null).show();
                }
                return true;
            }


            // ------- 处理结束 -------

            if (!(url.startsWith("http") || url.startsWith("https"))) {
                return true;
            }
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
        //返回键监听拦截首页面返回骆驼加油
        if (mWebView.getUrl().startsWith("https://test.bac365.com:10433/life_number/servlet/sideCardPage")){
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
    public void onBackPressed() {
        super.onBackPressed();
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
