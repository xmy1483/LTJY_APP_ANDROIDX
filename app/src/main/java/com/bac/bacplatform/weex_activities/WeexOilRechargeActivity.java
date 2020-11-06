package com.bac.bacplatform.weex_activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.http.GrpcTask;
import com.bac.bacplatform.module.recharge.OilVoucherActivity;
import com.bac.bacplatform.module.recharge.adapter.OilVoucherBean;
import com.bac.bacplatform.module.recharge.view.OilVoucherFragment;
import com.bac.bacplatform.weex_bean.CardItemVoucherBean;
import com.bac.bacplatform.weex_fragments.WeexOilRechargeFragment;
import com.bac.bacplatform.weex_utils.OilRechargeUtil;
import com.bac.bihupapa.bean.Method;
import com.bac.bihupapa.grpc.TaskPostExecute;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXRenderStrategy;

import java.util.Map;

public class WeexOilRechargeActivity extends AutomaticBaseActivity implements IWXRenderListener {

    private WXSDKInstance mWXSDKInstance;
    public static WeexOilRechargeActivity instance;

    @Override
    protected void initView() {
        instance = this;
        setContentView(R.layout.weex_oilrecharge_layout);
//        FrameLayout container = findViewById(R.id.container_id);
        initWeex();//
    }

//    public WXSDKInstance getWxIns() {
//        return mWXSDKInstance;
//    }

    @Override
    protected void initFragment() {

    }

    //初始化weex
    private void initWeex() {
        mWXSDKInstance = new WXSDKInstance(this);
        mWXSDKInstance.registerRenderListener(this);
//测试服
//        mWXSDKInstance.renderByUrl("oil_recharge", "http://192.168.0.88:9999/LTJY/index.js", null, null, WXRenderStrategy.APPEND_ASYNC);
//正式服
        String url = "https://app5.bac365.com:10443/app.pay/photo/weex/ATest/app/OilRechargePageV1.js";
        mWXSDKInstance.renderByUrl("oil_recharge",url,null,null,WXRenderStrategy.APPEND_ASYNC);
    }

    private WeexOilRechargeFragment fragment;

    public WeexOilRechargeFragment getOilFragment() {
        return fragment;
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {

        fragment = new WeexOilRechargeFragment(view);
//        setContentView(view);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_id, fragment).commitNow();
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
    protected void onResume() {
        super.onResume();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityResume();
        }
        OilRechargeUtil.newIns().hideWaitDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWXSDKInstance != null)
            mWXSDKInstance.onActivityPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mWXSDKInstance != null)
            mWXSDKInstance.onActivityStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("XmyLog", "onDestroy: ");
        OilRechargeUtil.newIns().hideWaitDialog();
        instance = null;
        super.onDestroy();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityDestroy();
        }
    }

    private JSCallback callback;

    public void setJScallback(JSCallback callback) {
        this.callback = callback;
    }


    private UnionPayBean unionPayBean; // 当前的union支付信息
    public void setUnionPayBean(String tn,String time,String payPlatform){
        unionPayBean = new UnionPayBean(tn,time,payPlatform);
    }

    private PayOrderBean payOrderBean;
    public void setPayOrderBean(String cardNo,String money) {
        payOrderBean = new PayOrderBean(money,cardNo);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int REQUEST_CODE = 2111;
        if (requestCode == REQUEST_CODE && resultCode == OilVoucherFragment.RESULT_CODE && data != null) {
            OilVoucherBean bean = data.getParcelableExtra(OilVoucherActivity.VOUCHER);
            if (bean != null) {
                CardItemVoucherBean vb = new CardItemVoucherBean();
                vb.setVoucherCount(bean.getVoucher_money());
                vb.setVoucherId(bean.getVoucher_id() + "");
                if (callback != null)
                    callback.invokeAndKeepAlive(vb);
            }  //                voucherBeanReset();

        } else if (requestCode == 10) { //云闪付回调
            onUnionPayResult(data);
            this.unionPayBean = null;
        }
    }



    private void onUnionPayResult(Intent data) {
        if (data == null) {
            finish();
            return;
        }

        // 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
        if(data.getExtras()==null) {
            Toast.makeText(this, "支付回调有误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String str = data.getExtras().getString("pay_result");
        if (str != null && str.equalsIgnoreCase("success")) {
            // 支付成功，调用后台订单查询接口验证是否确实成功
            onUnionPaySuccess();
        } else if (str != null && str.equalsIgnoreCase("fail")) {
            showResultDialog("支付失败");
        } else if (str != null && str.equalsIgnoreCase("cancel")) {
            Toast.makeText(this, "支付被取消", Toast.LENGTH_LONG).show();
        }
    }

    private void onUnionPaySuccess() {
        Method mb = new Method();
        mb.setMethodName("QUERY_UNION_ORDER_STATUS");
        mb.put("merchantOrderId",unionPayBean.getMerchantOrderId());
        mb.put("txnTime",unionPayBean.getOrderTime());
        mb.put("payPlatform",unionPayBean.getPayPlatform());
        new GrpcTask(this, mb, null, new TaskPostExecute() {
            @Override
            public void onPostExecute(Method result) {
                if(result.getErrorId() != 0 || result.getListMap().size() == 0){
                    showResultDialog(result.getMsg());
                    return;
                }

                Map<String,Object> payResult = result.getListMap().get(0);
                if(!payResult.containsKey("code")) {
                    showResultDialog("验证失败，请稍后查看订单状态");
                    return;
                }
                Object msgInfo = payResult.get("msg");
                if(msgInfo != null) {
                    Intent it = new Intent(WeexOilRechargeActivity.this,PayResultPage.class);
                    it.putExtra("rechargeMoney",payOrderBean.getOrderMoney());
                    it.putExtra("cardNo",payOrderBean.getCardNum());
                    startActivity(it);
                    finish();
//                    showResultDialog(msgInfo.toString());
                }
            }
        }).execute();
    }

    private AlertDialog alertDialog = null;
    private void showResultDialog(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                        finish();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }



}
