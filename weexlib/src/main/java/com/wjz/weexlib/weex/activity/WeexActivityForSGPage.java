package com.wjz.weexlib.weex.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.ViewGroup;

import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
import com.taobao.weex.WXSDKInstance;
import com.wjz.weexlib.R;
import com.wjz.weexlib.weex.delegate.ActivityWeexDelegate;
import com.wjz.weexlib.weex.delegate.BaseWeexDelegateCallback;
import com.wjz.weexlib.weex.delegate.IWeexView;
import com.wjz.weexlib.weex.delegate.impl.ActivityWeexDelegateImpl;

import java.util.Map;

/**
 * Created by wujiazhen on 2017/6/12.
 * <p>
 * 代理实现
 */

public class WeexActivityForSGPage extends AutomaticRxAppCompatActivity implements BaseWeexDelegateCallback, IWeexView {

    private WXSDKInstance instance;
    private ActivityWeexDelegate activityWeexDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_weex_activity);
        getActivityWeexDelegate().onCreate(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getActivityWeexDelegate().onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getActivityWeexDelegate().onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getActivityWeexDelegate().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getActivityWeexDelegate().onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getActivityWeexDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getActivityWeexDelegate().onDestroy();
    }

    /**
     * 初始化 代理
     * @return
     */
    protected ActivityWeexDelegate getActivityWeexDelegate() {
        if (activityWeexDelegate == null) {
            activityWeexDelegate = new ActivityWeexDelegateImpl(this);
        }
        return activityWeexDelegate;
    }

    @Override
    public WXSDKInstance createInstance() {
       destoryWeexInstance();
        if (instance==null) {
            instance = new WXSDKInstance(this);
        }
        return instance;
    }

    @Override
    public WXSDKInstance getInstance() {
        return instance;
    }

    @Override
    public void setInstance(WXSDKInstance instance) {
        this.instance = instance;
    }

    @Override
    public String getIndexUrl() {

        Uri data = getIntent().getData();

        String s = "";
        // 适配 7.0
        if (data != null){
            s = data.toString();
            System.out.println("========s:"+s);
        }

        return s;
    }

    @Override
    public Map<String, Object> getInitData() {
        return null;
    }

    @Override
    public String getPagerName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public IWeexView getWeexView() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String errorCode, String errorMsg) {

    }

    @Override
    public ViewGroup getContainer() {
        return (ViewGroup) findViewById(R.id.fl);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        getActivityWeexDelegate().onContentChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getActivityWeexDelegate().onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {
        getActivityWeexDelegate().onBackPressed();
        super.onBackPressed();
    }

    @Override
    public void startActivity(Intent intent) {
        // 7.0
        Uri data = intent.getData();
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (data != null && data.getAuthority().equals("assets")) {
            String authority = data.getAuthority();
            String path = data.getPath();

            StringBuilder sb = new StringBuilder();
            sb.append("bac://")
                    .append(authority)
                    .append(path);
            intent.setData(Uri.parse(sb.toString()));
        }
        super.startActivity(intent);
    }

//hahah
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 接口
        if (weexResultCallback!=null) {
            weexResultCallback.callback(requestCode,resultCode,data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void destoryWeexInstance() {
        if (instance != null) {
            instance.registerRenderListener(null);
            instance.destroy();
            instance = null;
        }
    }



    public interface WeexResultCallback{
        void callback(int requestCode, int resultCode, Intent data);
    }
    private WeexResultCallback weexResultCallback;

    public void setWeexResultCallback(WeexResultCallback weexResultCallback) {
        this.weexResultCallback = weexResultCallback;
    }
}
