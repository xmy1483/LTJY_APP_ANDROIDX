package com.bac.bihupapa.wxapi;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bihupapa.R;
import com.bac.bihupapa.util.UIUtils;
import com.bac.bihupapa.weexmodel.Bhpputil;
import com.bac.commonlib.utils.logger.ToastUtil;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
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

import static com.bac.bihupapa.conf.Constants.SECOND_2;


public class WXPayEntryActivity extends AutomaticRxAppCompatActivity implements IWXAPIEventHandler {


    private IWXAPI api;
    private String id;
    private String gift_type;
    private String extData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = Bhpputil.getInstance().getIwxapi();
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
        ToastUtil.showToast(this,resp.toString());
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                //支付成功

                setContentView(R.layout.wx_pay_result);
                ((TextView)findViewById(R.id.toolbar)).setText("充值结果");

                Button btn = findViewById(R.id.btn);
                RxTextView.text(btn).call("确定");

                PayResp payResp = (PayResp) resp;
                extData = payResp.extData;


                HashMap<String, String> hm_1 = JSON.parseObject(extData, new TypeReference<HashMap<String, String>>() {
                }.getType());
                id = hm_1.get("id");

                gift_type = hm_1.get("gift_type");
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
                                                .setData(Uri.parse("https://app5.bac365.com:10443/weex/sg/SGCard/ResultPage.js")));
                                    }else {
                                        UIUtil.startActivityInAnim(WXPayEntryActivity.this , new Intent(WXPayEntryActivity.this, WeexActivity2.class)
                                                .setData(Uri.parse("https://app5.bac365.com:10443/weex/sg/SGCard/PaySuccess.js")));
                                    }
                                }else {
                                    UIUtils.startActivityInAnim(WXPayEntryActivity.this,
                                            new Intent("com.vrphogame.thyroidapp0716.ACTION_START"));
                                }
                            }
                        });





                // 根据id 显示具体界面
                if ("oil".equals(id)) { //充油

                    View view = ((ViewStub) findViewById(R.id.vs_wx_result_one)).inflate();
                    TextView tv12 = view.findViewById(R.id.tv_12);
                    tv12.setText(String.format(getString(R.string.oil_recharge_result), hm_2.get("recharge_money")));
                    TextView tv22 = view.findViewById(R.id.tv_22);
                    tv22.setText(hm_2.get("card_no"));

                } else if ("other".equals(id)) { // 其他
                    View view = ((ViewStub) findViewById(R.id.vs_wx_result_two)).inflate();

                    TextView tv_exchange_time_1 = view.findViewById(R.id.tv_exchange_time_1);
                    TextView tv_exchange_time_2 = view.findViewById(R.id.tv_exchange_time_2);
                    tv_exchange_time_1.setText(hm_2.get("top"));
                    tv_exchange_time_2.setText(hm_2.get("bottom"));

                } else if ("deliver".equals(id)) {// 办卡
                    View view = ((ViewStub) findViewById(R.id.vs_wx_result_three)).inflate();

                    TextView tv01 = view.findViewById(R.id.tv_01);
                    tv01.setText(hm_2.get("top"));
                    tv01.append("\n".concat(hm_2.get("bottom")));
                }

            } else {
                //支付失败
                UIUtils.startActivityInAnim(WXPayEntryActivity.this,
                        new Intent("com.vrphogame.thyroidapp0716.ACTION_START"));
            }
        }
    }

    @Override
    public void onBackPressed() {
        UIUtils.startActivityInAnim(WXPayEntryActivity.this,
                new Intent("com.vrphogame.thyroidapp0716.ACTION_START"));
    }


}