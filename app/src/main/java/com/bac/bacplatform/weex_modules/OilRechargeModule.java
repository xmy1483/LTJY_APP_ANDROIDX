package com.bac.bacplatform.weex_modules;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity3;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.weex_activities.WeexOilRechargeActivity;
import com.bac.bacplatform.weex_callback.SimpleCallback;
import com.bac.bacplatform.weex_utils.OilRechargeUtil;
import com.bac.bacplatform.weex_utils.VoucherUtil;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class OilRechargeModule extends WXModule {

    //已实现
    @JSMethod(uiThread=false)
    public void logd(String msg){
        String oilCard = StringUtil.decode(WeexOilRechargeActivity.instance, "oilCard"); //sp
        if(!TextUtils.isEmpty(msg))
            Log.d("loglogJS", "WeexLog: "+msg);
    }

    //已实现
    @JSMethod
    public void toast(String msg){
        if(!TextUtils.isEmpty(msg))
            Toast.makeText(WXEnvironment.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    //已实现  optionClick
    @JSMethod
    public void onOptionPress(){
        UIUtils.startActivityInAnim(WeexOilRechargeActivity.instance,
                new Intent(WeexOilRechargeActivity.instance, WebAdvActivity3.class)
                        .putExtra("title", "油卡申请")
                        .putExtra("ads_url", "https://test.bac365.com:10433/life_number/servlet/sideCardIndex"+ "?phone=" + BacApplication.getLoginPhone()));
    }

    //已实现 获取油卡面额
    @JSMethod(uiThread=false)
    public void getOilCardData(String cardNum, JSCallback callback){
        try{
            OilRechargeUtil.newIns().getOilCardData(cardNum,callback);
        }catch (Exception e){
            callback.invoke("[]");
        }

    }

    //已实现 获取已经验卡的号码s
    @JSMethod(uiThread=false)
    public void getCertificatedCardNumList(JSCallback callback){
        OilRechargeUtil.newIns().getCertificatedCardLIst(callback);
    }

    //已实现
    @JSMethod
    public void skipToPay(String cardNum,String oilTYpe,String platform,String recharge,String saleMoney,String shouldPay,double voucherCount,String voucherId){
        OilRechargeUtil.newIns().skipToPay(cardNum,oilTYpe,platform,recharge,saleMoney,shouldPay,voucherCount,voucherId);
    }

    //已实现 任意金额 ====
    @JSMethod
    public void showAnyCountDialog(String cardNum,String oilType,JSCallback callback){
        OilRechargeUtil.newIns().showAnyCountDialog(WeexOilRechargeActivity.instance,cardNum,oilType,kybBalance,callback);
    }

    //已实现  删除验卡记录
    @JSMethod(uiThread = false)
    public void delCertificatedCardNum(String cardNum){
        OilRechargeUtil.newIns().delCertificatedCardNum(cardNum);
    }


    //已实现  KYB余额
    private double kybBalance = 0;
    @JSMethod
    public void getKYBbalance(final JSCallback callback){
        OilRechargeUtil.newIns().getKYBbalance(new SimpleCallback() {
            @Override
            public void onSuccess(Object obj) {
                kybBalance = Double.parseDouble(obj.toString()+"");
                callback.invokeAndKeepAlive(kybBalance);
            }
        });
    }

    //点击优惠券
    @JSMethod
    public void onCouponClick(JSCallback callback){

        OilRechargeUtil.newIns().onVoucherListClick(callback);
    }

    //已实现   查询优惠券
    @JSMethod
    public void queryVoucher(String cardNum,String recharge,double sale,JSCallback callback){
        VoucherUtil.getIns().queryVoucher(cardNum,recharge,sale,callback);
    }

    //已实现   获取上次验卡的卡号
    @JSMethod
    public void getLastCardNum(JSCallback callback){
        String oilCard = StringUtil.decode(WeexOilRechargeActivity.instance, "oilCard"); //sp
        if (!TextUtils.isEmpty(oilCard))
            callback.invokeAndKeepAlive(oilCard);
        else
            OilRechargeUtil.newIns().getLastCardNum(callback);
    }
}
