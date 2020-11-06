package com.bac.bacplatform.weex;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.module.hybrid.ZhiFuBaoActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.wxapi.WxPayReq;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.os.Build.SERIAL;
import static com.bac.bacplatform.conf.Constants.CommonProperty._0;

/**
 * Created by wujiazhen on 2017/7/25.
 */

public class ActiviteTicketModel extends WXModule {


    @JSMethod
    public void activeTicket(final Map<String, Object> map, final JSCallback jsCallback) {
        Context context = mWXSDKInstance.getContext();
        String payType = map.get("payType") + "";
        int voucher_type = (int) map.get("voucher_type");
        BacHttpBean bean = new BacHttpBean();
        switch (voucher_type) {
            case 1:// 话费
                bean.setMethodName("ACTIVATE_VOUCHER");
                break;
            case 2:// 流量
                bean.setMethodName("ACTIVATE_FLUX_VOUCHER");
                break;
        }
        bean.put("voucher_id", map.get("voucher_id_str"))
                .put("voucher_money", map.get("voucher_money"))
                .put("voucher_type", voucher_type)
                .put("recharge_money", map.get("recharge_money"))
                .put("login_phone", BacApplication.getLoginPhone())
                .put("recharge_phone", BacApplication.getLoginPhone())
                .put("terminal_id", SERIAL.concat("##").concat(
                        Settings.Secure.getString(
                                BacApplication.getBacApplication().getContentResolver(),
                                Settings.Secure.ANDROID_ID)))
                .put("pay_money", map.get("pay_money"))
                .put("pay_type", payType);


        HttpHelper.getInstance().bacNet(bean)
                .compose(((AutomaticRxAppCompatActivity) context).<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(context))
                .observeOn(RxScheduler.RxPoolScheduler())

                .map(new Func1<String, BacHttpBean>() {
                    @Override
                    public BacHttpBean call(String s) {

                        List<Map<String, Object>> cardListMap = JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                        }.getType());

                        String order_id = String.valueOf(cardListMap.get(0).get("order_id"));
                        BacHttpBean httpBean = new BacHttpBean()
                                .setActionType(0)
                                .setMethodName("PAY")
                                .put("pay_type", cardListMap.get(0).get("pay_type"))
                                .put("order_id", order_id)
                                .put("pay_money", cardListMap.get(0).get("pay_money"));

                        int voucher_type = (int) map.get("voucher_type");
                        switch (voucher_type) {
                            case 1:// 话费
                                httpBean.put("platform_name", "PHONE_RECHARGE_VOUCHER");
                                httpBean.put("content", "话费充值");
                                break;
                            case 2:// 流量
                                httpBean.put("platform_name", "VOUCHER_FLUX_ACTIVATE");
                                httpBean.put("content", "流量充值");
                                break;
                        }
                        return httpBean;
                    }
                })
                .observeOn(Schedulers.from(AsyncTask.SERIAL_EXECUTOR))
                .concatMap(new Func1<BacHttpBean, Observable<String>>() {
                    @Override
                    public Observable<String> call(BacHttpBean bacHttpBean) {
                        return HttpHelper.getInstance().bacNet(bacHttpBean);
                    }
                })
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        Map<String, Object> stringObjectMap = maps.get(_0);

                        if (stringObjectMap != null) {

                            String payType = map.get("payType") + "";

                                if ("WECHAT".equals(payType)) {

                                    HashMap<String, Object> hm_1 = new HashMap<>();
                                    hm_1.put("id", "other");
                                    HashMap<String, Object> hm_2 = new HashMap<>();

                                    int voucher_type = (int) map.get("voucher_type");
                                    switch (voucher_type) {
                                        case 1:// 话费
                                            hm_2.put("top", "成功充值".concat(map.get("voucher_money")+"").concat("元").concat("话费"));
                                            hm_2.put("bottom", "话费预计两个工作日到账");
                                            break;
                                        case 2:// 流量
                                            hm_2.put("top", "成功充值".concat(String.valueOf(map.get("product_name"))).concat("流量"));
                                            hm_2.put("bottom", map.get("product_remark"));
                                            break;
                                    }


                                    hm_1.put("data", hm_2);

                                    WxPayReq.pay(stringObjectMap, hm_1);


                                } else if ("ALIPAY".equals(payType)) {
                                    Context context = mWXSDKInstance.getContext();
                                    String paymentUrl = String.valueOf(stringObjectMap.get("paymentUrl"));
                                    Intent intentToPay = new Intent(context, ZhiFuBaoActivity.class);
                                    intentToPay.putExtra("paymentUrl", paymentUrl);
                                    UIUtils.startActivityInAnim((AppCompatActivity) context, intentToPay);
                                }

                        }
                    }
                });
    }
}
