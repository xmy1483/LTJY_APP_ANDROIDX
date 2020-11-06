package com.bac.bihupapa.weexmodel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bac.bihupapa.BuildConfig;
import com.bac.bihupapa.R;
import com.bac.bihupapa.util.PayMoudleUtils;
import com.bac.bihupapa.util.Util;
import com.bac.bihupapa.viewModle.KeyboardPopupWindow;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.http.HttpHelperLib;
import com.bac.commonlib.seed.Encrypt;
import com.bac.commonlib.utils.logger.LoggerUtil;
import com.bac.commonlib.utils.logger.ToastUtil;
import com.bac.commonlib.utils.str.StringUtil;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.wjz.weexlib.weex.activity.WeexActivity2;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static android.os.Build.MODEL;

/**
 * Created by guke on 2017/9/11.
 */

public class JDKDWeexModule extends WXModule {
    //查询已兑换礼品列表（加礼品卡id  即查询礼品卡详情）
    public static final String QUERY_GOODS_INFO= "GIFT_XML.QUERY_GIFT_RECORD";



    private String url;
    private KeyboardPopupWindow keyboardPopupWindow;

    @JSMethod
    public void pushWeexViewController(String jsurl, final JSCallback jsCallback) {
    //页面跳转
        Context context = mWXSDKInstance.getContext();
        org.json.JSONObject jsonObject = null;
        try {
            org.json.JSONObject json = new org.json.JSONObject(jsurl);
            url =  json.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UIUtil.startActivityInAnim((AppCompatActivity) context, new Intent(context, WeexActivity2.class)
                .setData(Uri.parse(url)));
    }

    @JSMethod(uiThread = false)
    public String getCurrentPhone() {
        //获取登录手机号
        String s = StringUtil.decode(mWXSDKInstance.getContext(),"bac_l",mWXSDKInstance.getContext().getString(R.string.seed_num) + Integer.valueOf(BuildConfig.XX) + BuildConfig.appKeySeed + BuildConfig.appKeySeed2);
        return s;
    }

    @JSMethod(uiThread = false)
    public String getCustomersID() {
        //获取CustomersID
        String s = StringUtil.decode(mWXSDKInstance.getContext(),"customers_id",mWXSDKInstance.getContext().getString(R.string.seed_num) + Integer.valueOf(BuildConfig.XX) + BuildConfig.appKeySeed + BuildConfig.appKeySeed2);

        return s;
    }

    @JSMethod
    public void requestWithParams(final Map<String, Object> map, final JSCallback jsCallback) {
//网络请求
// 请求地址方法内定义
        //
        LoggerUtil.loggerUtil("httpServer", JSON.toJSONString(map));
        Context context = mWXSDKInstance.getContext();
        BacHttpBean bean = new BacHttpBean();
        bean.setMethodName(map.get("methodName") + "").setActionType((int) map.get("actionType")).setListMap((List<Map<String, Object>>) map.get("listMap"));
        HttpHelperLib.getInstance()
                .net(bean, new AlertDialog.Builder(context).create(), null, true,mWXSDKInstance.getUIContext())
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, Object>() {
                    @Override
                    public Object call(String s) {
                        return JSON.parseObject(s, new TypeReference<Object>() {}.getType());
                    }
                })
                .compose(((AutomaticRxAppCompatActivity) mWXSDKInstance.getContext()).bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<Object>().rxDialog(mWXSDKInstance.getContext()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        if(o==null || o.toString().length()<5){
                            return;
                        }
                        Map mapo = JSONObject.parseObject(o.toString(), Map.class);
                        hashMap.put("data", mapo.get("listMap"));
                        jsCallback.invokeAndKeepAlive(hashMap);
                    }
                });
    }

    @JSMethod
    public void pushNativeController(Map<String, String> map) {
        //指定页面
        //1.设置支付密码页面
        Context context = mWXSDKInstance.getContext();
        if (map.get("ViewController") == "SetPayPasswordController") {
            Intent intent = new Intent("com.vrphogame.thyroidapp0716.ACTION_START");
            intent.addCategory("com.vrphogame.thyroidapp0716.SETMESSAGE_ACTIVITY");
            UIUtil.startActivityInAnim((AppCompatActivity) context, intent);
        }
    }

    @JSMethod
    public void pushNativeMainController() {
        //返回骆驼加油首页
        Context context = mWXSDKInstance.getContext();
        Intent intent = new Intent("com.vrphogame.thyroidapp0716.ACTION_START");
        intent.addCategory("com.vrphogame.thyroidapp0716.MAIN_ACTIVITY");
        UIUtil.startActivityInAnim((AppCompatActivity) context, intent);

    }

    @JSMethod
    public void pushWebViewWithUrl(String url) {
        //webview
        Context context = mWXSDKInstance.getContext();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        UIUtil.startActivityInAnim((AppCompatActivity) context, intent);
    }


    @JSMethod
    public void getCardCode(final JSCallback jsCallback) {
        //卡密
        String gift_id = PreferenceManager.getDefaultSharedPreferences(mWXSDKInstance.getContext()).getString("gift_id", "");
        Context context = mWXSDKInstance.getContext();
        BacHttpBean bean = new BacHttpBean();
        bean.setMethodName(QUERY_GOODS_INFO).put("login_phone",getCurrentPhone()).put("customers_id",getCustomersID()).put("gift_id",gift_id);
        LoggerUtil.loggerUtil("httpServer", JSON.toJSONString(bean.toString()));
        HttpHelperLib.getInstance()
                .net(bean, new AlertDialog.Builder(context).create(), null, true,mWXSDKInstance.getUIContext())
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, Object>() {
                    @Override
                    public Object call(String s) {
                        return JSON.parseObject(s, new TypeReference<Object>() {
                        }.getType());
                    }
                })

                .compose(((AutomaticRxAppCompatActivity) mWXSDKInstance.getContext()).bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<Object>().rxDialog(mWXSDKInstance.getContext()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        Map<String, Object> map = (Map<String, Object>) o;
                        List<Map<String, Object>> data = (List<Map<String, Object>>) map.get("listMap");
                        if (data.get(0).size() == 0) {
                            jsCallback.invokeAndKeepAlive("查询卡密失败,请稍后重试");
                        } else {
                            jsCallback.invokeAndKeepAlive(data.get(0).get("gift_card_pwd") + "");
                        }
                    }
                });
    }


    @JSMethod
    public void showPayPageView(final Map<Object, Object> map) {
        Log.e("showPayPageView", JSON.toJSONString(map));
        //揩油宝支付弹框
        HttpHelperLib.getInstance().net(
                new BacHttpBean().setMethodName("BASEXML.QUERY_KEYBOARD")
                        .put("login_phone", getCurrentPhone()),new AlertDialog.Builder(mWXSDKInstance.getContext()).create(), null, true,mWXSDKInstance.getUIContext())
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<String>>() {
                    @Override
                    public List<String> call(String s) {
                        Map<String, String> map = JSON.parseObject(s, new TypeReference<Map<String, String>>() {
                        }.getType());
                        List<Map<String, String>> map1 = JSON.parseObject(map.get("listMap"), new TypeReference<List<Map<String, String>>>() {
                        }.getType());
                        return JSON.parseObject(map1.get(0).get("keyboard_value"),new TypeReference<List<String>>(){});
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> list) {
                            // 忘记密码
                              keyboardPopupWindow = new KeyboardPopupWindow(list, mWXSDKInstance.getContext(), mWXSDKInstance.getContainerView(), new KeyboardPopupWindow.KeyboardCallback() {
                                @Override
                                public void onKeyboardCallback(String pass, final PopupWindow popupWindow) {
                                    // 更改页面
                                    BacHttpBean bean = new BacHttpBean().setMethodName("VERIFICATE_PAY_PASSWORD").put("login_phone", getCurrentPhone()).put("pay_password", new Encrypt().SHA256(pass));
                                    HttpHelperLib.getInstance()
                                            .net(bean, new AlertDialog.Builder(mWXSDKInstance.getContext()).create(), null, true,mWXSDKInstance.getUIContext())
                                            .observeOn(RxScheduler.RxPoolScheduler())
                                            .map(new Func1<String, Object>() {
                                                @Override
                                                public Object call(String s) {
                                                    return JSON.parseObject(s, new TypeReference<Object>() {
                                                    }.getType());
                                                }
                                            })
                                            .compose(((AutomaticRxAppCompatActivity) mWXSDKInstance.getContext()).bindUntilEvent(ActivityEvent.DESTROY))
                                            .compose(new RxDialog<Object>().rxDialog(mWXSDKInstance.getContext()))
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Action1<Object>() {
                                                @Override
                                                public void call(final Object o) {
                                                    try {
                                                        Map<String,Object> map1 = (Map<String,Object>)o;
                                                        if ((Boolean) ((Map<String, Object>) o).get("success")){
                                                            //跳转支付成功页面
                                                            Map<Object,Object> bean = new HashMap<>();
                                                            bean.put("methedName","PAY_GIFT_CARD");
                                                            bean.put("login_phone", getCurrentPhone());
                                                            bean.put("gift_type", map.get("gift_type"));
                                                            bean.put("gift_id", map.get("gift_id"));
                                                            bean.put("recharge_money", map.get("gift_value"));
                                                            bean.put("sale_money", map.get("sale_money"));
                                                            bean.put("platform_name", "GIFT_CARD_PAY");
                                                            bean.put("pay_type", "KYB");
                                                            bean.put("pay_money", map.get("pay_money"));
                                                            bean.put("gift_discount", map.get("gift_discount"));
                                                            bean.put("delivery_fee", map.get("delivery_fee"));
                                                            bean.put("gifts", map.get("gifts"));
                                                            bean.put("terminal_id", MODEL);
                                                            payMoneyActually(bean,"KYB",keyboardPopupWindow);
                                                        }else {
                                                            keyboardPopupWindow.setFail((AutomaticRxAppCompatActivity) mWXSDKInstance.getContext(), new Util._3CountDownCallback() {
                                                                @Override
                                                                public void _3CountDownCallback() {
                                                                    keyboardPopupWindow.dismiss();

                                                                }
                                                            });
                                                        }
                                                    }catch (Exception e){
                                                        keyboardPopupWindow.setFail((AutomaticRxAppCompatActivity) mWXSDKInstance.getContext(), new Util._3CountDownCallback() {
                                                            @Override
                                                            public void _3CountDownCallback() {
                                                                keyboardPopupWindow.dismiss();
                                                                try {
                                                                    List<Map<String, Map<String, Object>>> data = (List<Map<String, Map<String, Object>>>)o;
                                                                    ToastUtil.showToast(mWXSDKInstance.getContext(),String.valueOf(data.get(0).get("MSG")));
                                                                }catch (Exception a){

                                                                }
                                                            }
                                                        });
                                                    }

                                                }
                                            });
                                    keyboardPopupWindow.setLoading((AutomaticRxAppCompatActivity)mWXSDKInstance.getContext());
                                }

                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // 忘记密码
                                    ToastUtil.showToast(mWXSDKInstance.getContext(),"请在我的页面重置支付密码");
                                }
                            });
                        }

                });
    }


    @JSMethod
    public void payMoneyActually(Map<Object, Object> map, String paytype,KeyboardPopupWindow keyboardPopupWindow) {
        //获取订单号，执行支付，微信、支付宝、揩油宝
        PayMoudleUtils.pay(paytype,map,mWXSDKInstance.getContext(),keyboardPopupWindow);
    }
}
