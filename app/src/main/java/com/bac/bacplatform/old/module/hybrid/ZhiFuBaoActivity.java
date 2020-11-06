package com.bac.bacplatform.old.module.hybrid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.bac.bacplatform.R;
import com.bac.bacplatform.module.main.MainActivity;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.insurance.InsuranceQueryVideo;
import com.bac.bacplatform.old.module.insurance.InsuranceUploadAddress;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 支付宝
 */
public class ZhiFuBaoActivity extends SuperActivity {
    private static final String JS_METHOD_RETURN = "js_return_home";

    private WebView mWebView;
    private ProgressBar mPb;
    private boolean mNoPay;
    private TextView text;
    private TextView text_finish;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhifubao_layout);

        mWebView = (WebView) findViewById(R.id.wb);
        mPb = (ProgressBar) findViewById(R.id.pb);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new ZhiFuBaoClient());
        mWebView.setWebChromeClient(new ZhiFuBaoChromeClient());

        mWebView.loadUrl(getIntent().getStringExtra("paymentUrl"));
        mWebView.addJavascriptInterface(new JsReturnHomeObj(), JS_METHOD_RETURN);

        text = (TextView) findViewById(R.id.tv_center);
        text_finish = (TextView) findViewById(R.id.tv_finish);
        text.setText("支付");
        text_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ZhiFuBaoActivity.this,MainActivity.class));
            }
        });
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
                            subscriber.onNext(result);
                        }
                    })
                            .compose(ZhiFuBaoActivity.this.<H5PayResultModel>bindUntilEvent(ActivityEvent.DESTROY))
                            .subscribeOn(RxScheduler.RxPoolScheduler())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<H5PayResultModel>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                }

                                @Override
                                public void onNext(H5PayResultModel result) {
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

                                UIUtils.startActivityInAnim(ZhiFuBaoActivity.this, new Intent(ZhiFuBaoActivity.this,
                                        MainActivity.class));
                            }
                        }
                    });

        }
    }
}
