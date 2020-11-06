package com.bac.bihupapa.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bihupapa.BuildConfig;
import com.bac.bihupapa.R;
import com.bac.bihupapa.activity.ZhiFuBaoActivity;
import com.bac.bihupapa.viewModle.KeyboardPopupWindow;
import com.bac.bihupapa.wxapi.WxPayReq;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.http.HttpHelperLib;
import com.bac.commonlib.utils.logger.ToastUtil;
import com.bac.commonlib.utils.str.StringUtil;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.wjz.weexlib.weex.activity.WeexActivity2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static android.os.Build.MODEL;

/**
 * Title
 * Created by 黄云龙 on 2017/9/26.
 */

public class PayMoudleUtils {


    public static void pay(final String payType, final Map<Object, Object> params, final Context context, final KeyboardPopupWindow keyboardPopupWindow) {
        BacHttpBean bean = new BacHttpBean();
        bean.setMethodName("PAY_GIFT_CARD");
        bean.put("login_phone", StringUtil.decode(context, "bac_l", context.getString(R.string.seed_num) + Integer.valueOf(BuildConfig.XX) + BuildConfig.appKeySeed + BuildConfig.appKeySeed2));
        bean.put("customers_id", StringUtil.decode(context, "customers_id", context.getString(R.string.seed_num) + Integer.valueOf(BuildConfig.XX) + BuildConfig.appKeySeed + BuildConfig.appKeySeed2));
        bean.put("gift_id", params.get("gift_id"));
        bean.put("gift_type", params.get("gift_type"));
        bean.put("recharge_money", params.get("recharge_money"));
        bean.put("sale_money", params.get("sale_money"));
        bean.put("platform_name", "GIFT_CARD_PAY");
        bean.put("pay_type", payType);
        bean.put("pay_money", params.get("pay_money"));
 //       bean.put("pay_money", 0.01);
        bean.put("gift_discount", params.get("gift_discount"));
        bean.put("delivery_fee", params.get("delivery_fee"));
        bean.put("gifts", params.get("gifts"));
        bean.put("terminal_id", MODEL);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("gift_id", params.get("gift_id").toString()).commit();
        HttpHelperLib.getInstance()
                .net(bean, new AlertDialog.Builder(context).create(), null, true, null)
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, Object>() {
                    @Override
                    public Object call(String s) {
                        return JSON.parseObject(s, new TypeReference<Object>() {
                        }.getType());
                    }
                })
                .compose(((AutomaticRxAppCompatActivity) context).bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<Object>().rxDialog(context))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Map<String, Object> map = (Map<String, Object>) o;
                            List<Map<String, Map<String, Object>>> data = (List<Map<String, Map<String, Object>>>) map.get("listMap");
                            BacHttpBean payBean = new BacHttpBean();
                            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("orderId", data.get(0).get("order_id") + "").commit();
                            payBean.setMethodName("PAY")
                                    .put("login_phone", StringUtil.decode(context, "bac_l", context.getString(R.string.seed_num) + Integer.valueOf(BuildConfig.XX) + BuildConfig.appKeySeed + BuildConfig.appKeySeed2))
                                    .put("platform_name", "GIFT_CARD_PAY")
                                    .put("pay_type", payType)
                                    .put("order_id", data.get(0).get("order_id"))
                                    .put("content", data.get(0).get("content"))
                                    //   支付金额改为0.01
                                    // .put("pay_money", 0.01);
                                    //   正常支付金额
                                    .put("pay_money", data.get(0).get("pay_money"));
                            HttpHelperLib.getInstance()
                                    .net(payBean, new AlertDialog.Builder(context).create(), null, true, null)
                                    .observeOn(RxScheduler.RxPoolScheduler())
                                    .map(new Func1<String, Object>() {
                                        @Override
                                        public Object call(String s) {
                                            return JSON.parseObject(s, new TypeReference<Object>() {
                                            }.getType());
                                        }
                                    })
                                    .compose(((AutomaticRxAppCompatActivity) context).bindUntilEvent(ActivityEvent.DESTROY))
                                    .compose(new RxDialog<Object>().rxDialog(context))
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Object>() {
                                        @Override
                                        public void call(Object o) {
                                            try {
                                                final Map<String, Object> map1 = (Map<String, Object>) o;
                                                List<Map<String, Map<String, Object>>> data1 = (List<Map<String, Map<String, Object>>>) map1.get("listMap");
                                                switch (payType) {
                                                    case "KYB":
                                                        Map<String, Object> bean = (Map<String, Object>) o;
                                                        List<Map<String, Map<String, Object>>> dataBean = (List<Map<String, Map<String, Object>>>) map1.get("listMap");
                                                        if (Boolean.parseBoolean(dataBean.get(0).get("is_kyb_succ") + "")) {
                                                            // 充值成功
                                                            keyboardPopupWindow.setShowCountDown((AutomaticRxAppCompatActivity) context, new Util._3CountDownCallback() {
                                                                @Override
                                                                public void _3CountDownCallback() {
                                                                    // 消失
                                                                    keyboardPopupWindow.dismiss();
                                                                    //支付成功页面  0代表京东卡  1代表苏果卡
                                                                    if ("0".equals(String.valueOf(params.get("gift_type")))) {
                                                                        UIUtil.startActivityInAnim((AppCompatActivity) context, new Intent(context, WeexActivity2.class)
                                                                                //.setData(Uri.parse("https://app5.bac365.com:10443/weex/sg/SGCard/ResultPage.js")));
                                                                                .setData(Uri.parse("https://app5.bac365.com:10443/weex/jd/JD_card/ResultPage.js")));

                                                                    } else {
                                                                        UIUtil.startActivityInAnim((AppCompatActivity) context, new Intent(context, WeexActivity2.class)
                                                                                .setData(Uri.parse("https://app5.bac365.com:10443/weex/sg/SGCard/PaySuccess.js")));
                                                                    }

                                                                }
                                                            });
                                                        } else {
                                                            // 充值失败
                                                            keyboardPopupWindow.setFail((AutomaticRxAppCompatActivity) context, new Util._3CountDownCallback() {
                                                                @Override
                                                                public void _3CountDownCallback() {
                                                                    // 消失
                                                                    keyboardPopupWindow.dismiss();
                                                                }
                                                            });
                                                        }
                                                        break;
                                                    case "ALIPAY":
                                                        PackageInfo packageInfo;
                                                        try {
                                                            packageInfo = context.getPackageManager().getPackageInfo(
                                                                    "com.eg.android.AlipayGphone", 0);

                                                        } catch (PackageManager.NameNotFoundException e) {
                                                            packageInfo = null;
                                                            e.printStackTrace();
                                                        }
                                                        if (packageInfo == null) {
                                                            ((AutomaticRxAppCompatActivity) context).runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    ToastUtil.showToast(context, "您尚未安装支付宝，请安装后再试！");
                                                                }
                                                            });
                                                        } else {
                                                            UIUtils.startActivityInAnim((AppCompatActivity) context, new Intent(context, ZhiFuBaoActivity.class).putExtra("paymentUrl", String.valueOf(data1.get(0).get("paymentUrl"))).putExtra("type", "gift").putExtra("gift_type", String.valueOf(params.get("gift_type"))));
                                                        }
                                                        break;
                                                    case "WECHAT":
                                                        ((AutomaticRxAppCompatActivity) context).runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                HashMap<String, Object> hm_1 = new HashMap<>();
                                                                hm_1.put("id", "gift");
                                                                HashMap<String, Object> hm_2 = new HashMap<>();

                                                                hm_2.put("recharge_money", params.get("recharge_money"));

                                                                hm_2.put("card_no", params.get("gift_id"));
                                                                hm_1.put("data", hm_2);
                                                                hm_1.put("gift_type", params.get("gift_type"));
                                                                WxPayReq.pay(map1, hm_1);
                                                            }
                                                        });

                                                        break;
                                                }
                                            } catch (Exception e) {
                                                // 充值失败
                                                keyboardPopupWindow.setFail((AutomaticRxAppCompatActivity) context, new Util._3CountDownCallback() {
                                                    @Override
                                                    public void _3CountDownCallback() {
                                                        // 消失
                                                        keyboardPopupWindow.dismiss();
                                                    }
                                                });
                                            }

                                        }
                                    });
                        } catch (Exception e) {
                            ((AutomaticRxAppCompatActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(context).setTitle("温馨提示")
                                            .setMessage("出错啦！请拨打400-100-6262联系客服人员。")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                }
                            });
                        }
                    }
                });
    }
}
