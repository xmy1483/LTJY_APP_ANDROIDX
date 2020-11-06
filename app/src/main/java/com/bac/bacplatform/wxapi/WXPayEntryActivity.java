package com.bac.bacplatform.wxapi;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.module.main.MainActivity;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.wjz.weexlib.weex.activity.WeexActivity2;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.bac.bacplatform.conf.Constants.SECOND_2;

public class WXPayEntryActivity extends SuperActivity implements IWXAPIEventHandler {


    private IWXAPI api;
    private String id;
    private String gift_type;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = BacApplication.getmWXApi();
        // 微信回调页
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    /**
     * @param resp 主线程
     */
    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                //支付成功

                setContentView(R.layout.wx_pay_result);

                initToolBar("充值结果");

                Button btn = (Button) findViewById(R.id.btn);
                RxTextView.text(btn).call("确定");

                PayResp payResp = (PayResp) resp;
                String extData = payResp.extData;

                HashMap<String, String> hm_1 = JSON.parseObject(extData, new TypeReference<HashMap<String, String>>() {
                }.getType());
                gift_type = hm_1.get("gift_type");
                id = hm_1.get("id");
                HashMap<String, String> hm_2 = JSON.parseObject(hm_1.get("data"), new TypeReference<HashMap<String, String>>() {
                }.getType());

                RxView.clicks(btn).throttleFirst(SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                if ("gift".equals(id)){
                                    //支付成功页面  0代表京东卡  1代表苏果卡
                                    if (gift_type.equals("0")){
                                        UIUtil.startActivityInAnim(WXPayEntryActivity.this , new Intent(WXPayEntryActivity.this, WeexActivity2.class)
                                                .setData(Uri.parse("file://file/dist/ResultPage.js")));
                                    }else {
                                        UIUtil.startActivityInAnim(WXPayEntryActivity.this , new Intent(WXPayEntryActivity.this, WeexActivity2.class)
                                                .setData(Uri.parse("file://file/dist/PaySuccess.js")));
                                    }
                                }else {
                                    com.bac.bihupapa.util.UIUtils.startActivityInAnim(WXPayEntryActivity.this,
                                            new Intent("com.vrphogame.thyroidapp0716.ACTION_START"));
                                }

                            }
                        });



                // 根据id 显示具体界面
                if ("oil".equals(id)) { //充油

                    View view = ((ViewStub) findViewById(R.id.vs_wx_result_one)).inflate();
                    TextView tv12 = (TextView) view.findViewById(R.id.tv_12);
                    tv12.setText(String.format(getString(R.string.oil_recharge_result), hm_2.get("recharge_money")));
                    TextView tv22 = (TextView) view.findViewById(R.id.tv_22);
                    tv22.setText(hm_2.get("card_no"));

                } else if ("other".equals(id)) { // 其他
                    View view = ((ViewStub) findViewById(R.id.vs_wx_result_two)).inflate();

                    TextView tv_exchange_time_1 = (TextView) view.findViewById(R.id.tv_exchange_time_1);
                    TextView tv_exchange_time_2 = (TextView) view.findViewById(R.id.tv_exchange_time_2);
                    tv_exchange_time_1.setText(hm_2.get("top"));
                    tv_exchange_time_2.setText(hm_2.get("bottom"));

                } else if ("deliver".equals(id)) {// 办卡
                    View view = ((ViewStub) findViewById(R.id.vs_wx_result_three)).inflate();

                    TextView tv01 = (TextView) view.findViewById(R.id.tv_01);
                    tv01.setText(hm_2.get("top"));
                    tv01.append("\n".concat(hm_2.get("bottom")));

                }else if ("gift".equals(id)){
                    View view = ((ViewStub) findViewById(R.id.vs_wx_result_one)).inflate();
                    RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl_belo);
                    rl.setVisibility(View.GONE);

                }

            } else {
                //支付失败
                UIUtils.startActivityInAnim(WXPayEntryActivity.this,
                        new Intent(WXPayEntryActivity.this, MainActivity.class));
            }
        }
    }

    @Override
    public void onBackPressed() {
        UIUtils.startActivityInAnim(WXPayEntryActivity.this,
                new Intent(WXPayEntryActivity.this, MainActivity.class));
    }
}