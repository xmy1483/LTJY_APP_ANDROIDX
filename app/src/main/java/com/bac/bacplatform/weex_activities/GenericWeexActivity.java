package com.bac.bacplatform.weex_activities;

import android.view.View;
import android.widget.Toast;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;

public class GenericWeexActivity extends AutomaticBaseActivity implements IWXRenderListener {

    @Override
    protected void initView() {
        String url = getIntent().getStringExtra("URL");
        if(url==null||url.length()<1)
        {
            Toast.makeText(activity, "加载失败", Toast.LENGTH_SHORT).show();
            return;
        }
        loadURl(url);
    }
    private WXSDKInstance mWXSDKInstance;
    private void loadURl(String url){
        mWXSDKInstance = new WXSDKInstance(this);
        mWXSDKInstance.registerRenderListener(this);
        mWXSDKInstance.renderByUrl("oil_recharge",url,null,null, WXRenderStrategy.APPEND_ASYNC);
    }

    @Override
    protected void initFragment() {

    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        if(view!=null)
            setContentView(view);
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mWXSDKInstance!=null)
            mWXSDKInstance.onActivityPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mWXSDKInstance!=null)
            mWXSDKInstance.onActivityResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityStop();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityDestroy();
        }
    }
}
