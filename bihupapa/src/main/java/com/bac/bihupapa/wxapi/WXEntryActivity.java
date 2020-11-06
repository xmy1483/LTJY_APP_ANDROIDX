package com.bac.bihupapa.wxapi;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.bac.bihupapa.weexmodel.Bhpputil;
import com.bac.rxbaclib.rx.rxbus.RxBus;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by ntop on 15/9/4.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IWXAPI iwxapi = Bhpputil.getInstance().getIwxapi();
        // 微信回调页
        iwxapi.handleIntent(getIntent(), this);

    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        RxBus.get().post("wx_share",baseResp);
        finish();
    }

}
