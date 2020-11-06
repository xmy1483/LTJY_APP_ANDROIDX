package com.bac.bacplatform.wxapi;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.module.login.view.LoginFragment2;
import com.bac.bacplatform.module.main.view.HomeFragment;
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
        IWXAPI iwxapi = BacApplication.getmWXApi();
        // 微信回调页
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }
    @Override
    public void onResp(BaseResp baseResp) {
        if(baseResp.getType()==1){//微信授权登录
//            LoginFragment2 view = new LoginFragment2();
//            if(baseResp.errCode==0){
//                String code = ((SendAuth.Resp) baseResp).code;
//                System.out.println("-----------------------------------0"+code);
//                view.getAccessToken1(code);
//            }else{
//                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
//            }
//            System.out.println("---------------------------baseResp");
          RxBus.get().post(LoginFragment2.WX_LOGIN,baseResp);
        }else if(baseResp.getType()==2){//微信分享
            RxBus.get().post(HomeFragment.WX_SHARE,baseResp);
        }
        finish();

    }
}
