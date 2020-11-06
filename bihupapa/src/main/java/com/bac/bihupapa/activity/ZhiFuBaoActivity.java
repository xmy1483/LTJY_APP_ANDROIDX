package com.bac.bihupapa.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.bac.bihupapa.R;
import com.bac.bihupapa.util.UIUtils;
import com.bac.commonlib.utils.logger.ToastUtil;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.wjz.weexlib.weex.activity.WeexActivity2;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * 支付宝
 */
public class ZhiFuBaoActivity extends AutomaticRxAppCompatActivity {
    private static final String JS_METHOD_RETURN = "js_return_home";

    private WebView mWebView;
    private ProgressBar mPb;
    private boolean mNoPay;
    private String type = "";
    private boolean isWapPay;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_zhifubao_layout);

        if (getIntent().getStringExtra("type") != null){
            type = "gift";
        }



//        ((TextView)findViewById(R.id.tv_center)).setText("支付");

        findViewById(R.id.txt_bt_back_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mWebView = findViewById(R.id.wb);
        mPb = findViewById(R.id.pb);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new ZhiFuBaoClient());
        mWebView.setWebChromeClient(new ZhiFuBaoChromeClient());

        mWebView.loadUrl(getIntent().getStringExtra("paymentUrl"));
        mWebView.addJavascriptInterface(new JsReturnHomeObj(), JS_METHOD_RETURN);
    }




    private class ZhiFuBaoChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mPb.setProgress(newProgress);
            if (newProgress == 100) {
                mPb.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    private class ZhiFuBaoClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {

            if (!(url.startsWith("http") || url.startsWith("https"))) {
                return true;
            }

            if (!mNoPay) {
                final PayTask task = new PayTask(ZhiFuBaoActivity.this);
                final String ex = task.fetchOrderInfoFromH5PayUrl(url);

                if (!TextUtils.isEmpty(ex)) {

                    Observable.create(new Observable.OnSubscribe<H5PayResultModel>() {
                        @Override
                        public void call(Subscriber<? super H5PayResultModel> subscriber) {
                            final H5PayResultModel result = task.h5Pay(ex, true);//支付宝原生支付
                            if (judge(result.getResultCode())){
                                ToastUtil.showToast(ZhiFuBaoActivity.this,"支付出错,请稍后再试！");
                            }else {
                                subscriber.onNext(result);
                            }
                        }
                    })
                            .compose(ZhiFuBaoActivity.this.<H5PayResultModel>bindUntilEvent(ActivityEvent.DESTROY))
                            .subscribeOn(RxScheduler.RxPoolScheduler())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<H5PayResultModel>() {
                                public H5PayResultModel result;

                                @Override
                                public void onCompleted() {
//                                    if (isWapPay){
//                                        if ("gift".equals(type)){
//                                            //支付成功页面  0代表京东卡  1代表苏果卡
//                                            if ("0".equals(getIntent().getStringExtra("gift_type"))){
//                                                UIUtil.startActivityInAnim(ZhiFuBaoActivity.this , new Intent(ZhiFuBaoActivity.this, WeexActivity2.class)
//                                                        .setData(Uri.parse("file://file/dist/ResultPage.js")));
//                                                finish();
//                                            }else {
//                                                UIUtil.startActivityInAnim(ZhiFuBaoActivity.this , new Intent(ZhiFuBaoActivity.this, WeexActivity2.class)
//                                                        .setData(Uri.parse("file://file/dist/PaySuccess.js")));
//                                                finish();
//                                            }
//                                        }
//                                    }
                                    if ("9000".equals(result.getResultCode()) && result.getReturnUrl() !=null){
                                        if ("gift".equals(type)){
                                            //支付成功页面  0代表京东卡  1代表苏果卡
                                            if ("0".equals(getIntent().getStringExtra("gift_type"))){
                                                UIUtil.startActivityInAnim(ZhiFuBaoActivity.this , new Intent(ZhiFuBaoActivity.this, WeexActivity2.class)
                                                        .setData(Uri.parse("https://app5.bac365.com:10443/weex/sg/SGCard/ResultPage.js")));
                                                finish();
                                            }else {
                                                UIUtil.startActivityInAnim(ZhiFuBaoActivity.this , new Intent(ZhiFuBaoActivity.this, WeexActivity2.class)
                                                        .setData(Uri.parse("https://app5.bac365.com:10443/weex/sg/SGCard/PaySuccess.js")));
                                                finish();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(H5PayResultModel result) {
                                    this.result = result;
                                    if (judge(result.getResultCode())) {
                                        //支付失败重新支付，支付宝原生不拦截
                                        mWebView.loadUrl(url);
                                        mNoPay = true;
                                    } else {
                                        finish();
                                    }
                                }
                            });

/*
启动不了原生
                    Observable.just(task.h5Pay(ex, true))

                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<H5PayResultModel>() {
                                @Override
                                public void call(H5PayResultModel result) {
                                    if (judge(result.getResultCode())) {
                                        //支付失败重新支付，支付宝原生不拦截
                                        mWebView.loadUrl(url);
                                        mNoPay = true;
                                    } else {
                                        //支付页回调，支付失败没有回调
                                        view.loadUrl(result.getReturnUrl());
                                    }
                                }
                            });*/

                  /*  new Thread(new Runnable() {
                        public void run() {
                            final H5PayResultModel result = task.h5Pay(ex, true);//支付宝原生支付
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (judge(result.getResultCode())) {
                                        //支付失败重新支付，支付宝原生不拦截
                                        mWebView.loadUrl(url);
                                        mNoPay = true;
                                    } else {
                                        //支付页回调，支付失败没有回调

                                        view.loadUrl(result.getReturnUrl());
                                    }
                                }
                            });
                        }
                    }).start();*/
                } else {
                    view.loadUrl(url);
                }
            } else {
                view.loadUrl(url);
            }
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
            view.loadUrl("file:///android_asset/NetworkError.html");
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if (url.contains("doWapPayment")){
            }
            return super.shouldInterceptRequest(view, url);
        }
    }

    private boolean judge(String code) {
        boolean is = false;
        if ("4000".equals(code)) {
            is = true;
        } else if ("6001".equals(code)) {
            is = true;
        }
        return is;
    }


    class JsReturnHomeObj {
        @JavascriptInterface
        public void returnHomeOnClick() {
            // 在该接口中添加JS可以调用的方法，用以对本地应用进行操作，本示例中的方法作用为返回充值页面。
            Observable.just("")
                    .compose(ZhiFuBaoActivity.this.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            Parcelable bean = getIntent().getParcelableExtra("bean");
                            if (bean != null) { //保险
                                Intent intent = null;
                                if (getIntent().getBooleanExtra("upload", false)) {//影像补齐
                                    intent = new Intent(ZhiFuBaoActivity.this, InsuranceQueryVideo.class);
                                } else {
                                    intent = new Intent(ZhiFuBaoActivity.this, InsuranceUploadAddress.class);
                                }

                                UIUtils.startActivityInAnim(ZhiFuBaoActivity.this, intent.putExtra("bean", bean));
                            } else {
                                // 回主页面
                                UIUtils.startActivityInAnim(ZhiFuBaoActivity.this, new Intent("com.vrphogame.thyroidapp0716.ACTION_START"));
                            }
                        }
                    });

        }

        @JavascriptInterface
        public void goBack() {
            finish();
            //isWapPay = true;
        }

        @JavascriptInterface
        public void paySuccess(){

        }
    }
}
