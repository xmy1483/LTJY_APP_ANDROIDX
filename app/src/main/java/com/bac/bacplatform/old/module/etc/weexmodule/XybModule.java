package com.bac.bacplatform.old.module.etc.weexmodule;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.http.GrpcTask;
import com.bac.bacplatform.module.center.SetMypswActivity;
import com.bac.bacplatform.old.module.etc.NewDES;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity;
import com.bac.bacplatform.old.module.hybrid.ZhiFuBaoActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.wxapi.WxPayReq;
import com.bac.bihupapa.BuildConfig;
import com.bac.bihupapa.bean.Method;
import com.bac.bihupapa.grpc.TaskPostExecute;
import com.bac.commonlib.seed.Encrypt;
import com.bac.commonlib.utils.logger.LoggerUtil;
import com.bac.commonlib.utils.logger.ToastUtil;
import com.bac.commonlib.utils.str.StringUtil;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
import com.baidu.location.BDLocation;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.wjz.weexlib.weex.activity.WeexActivity2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import etc.obu.data.CardInformation;
import etc.obu.data.CardTransactionRecord;
import etc.obu.data.DeviceInformation;
import etc.obu.data.ServiceStatus;

public class XybModule extends WXModule {
    BluetoothAdapter mBtAdapter;


    //关闭盒子
    @JSMethod
    public void stopDeviceConnectWithCallback(final JSCallback jsCallback) {
        ServiceStatus serviceStatus = BacApplication.obuInterface.disconnectDevice();
        if (serviceStatus.getServiceCode() == 0) {
            jsCallback.invokeAndKeepAlive("yes");
        } else {
            jsCallback.invokeAndKeepAlive("no");
        }

    }

    //（ETC盒子的接口）搜索盒子设备
    @JSMethod
    public void searchBluetoothDeviceWithCallback(final JSCallback jsCallback) {
        System.out.println("正在连接盒子");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "yes");
        map.put("error","未扫描到设备");
        jsCallback.invokeAndKeepAlive(map);
    }

    //（ETC盒子的接口）连接设备  ...
    @JSMethod
    public void connectDeviceWithCallback(final JSCallback jsCallback) {
        new AutoFocusTask(jsCallback).execute();
    }

    //（ETC盒子的接口）盒子认证
    @JSMethod
    public void authDevWithCallback(final JSCallback jsCallback) {
        new AutoFocusTask1(jsCallback).execute();
    }

    //（ETC盒子的接口）获取卡片信息
    @JSMethod
    public void getCardInformationWithCallback(final JSCallback jsCallback) {//获取卡片信息
        System.out.println("正在获取卡的信息");
        jsCallback.invokeAndKeepAlive(getCardInformation());
    }

    //获取当前城市
    @JSMethod
    public void currentCityWithCallback(final JSCallback jsCallback) {
        HashMap<String, Object> hashMap = new HashMap<>();
        BDLocation sendBDLocation = null;
        hashMap.put("status", "yes");
        hashMap.put("data", "南京");
        jsCallback.invokeAndKeepAlive(hashMap);
    }

    //（ETC盒子的接口）获取设备信息
    @JSMethod
    public void getObuInformationWithCallback(final JSCallback jsCallback) {
        HashMap<String, Object> hashMap = new HashMap<>();
        HashMap<String, Object> map = new HashMap<>();
        DeviceInformation deviceInformation = BacApplication.obuInterface.getDeviceInformation();
        System.out.println("设备信息：" + deviceInformation.Sn);
        String str = "";
        if (deviceInformation == null) {
            hashMap.put("error", "请重试");
            str = "no";
        } else {
            map.put("sn", deviceInformation.Sn);
            str = "yes";
        }
        hashMap.put("status", str);
        hashMap.put("data", map);
        jsCallback.invokeAndKeepAlive(hashMap);
    }

    //（ETC盒子的接口）写卡充值：输入卡号和充值金额，计算Mac1码。
    @JSMethod
    public void loadCreditGetMacWithInfo(final Map<Object, Object> map, final JSCallback jsCallback) {
        HashMap<String, Object> hashMap = new HashMap<>();
        HashMap<String, Object> map1 = new HashMap<>();
        String cardVersion = (String) map.get("cardversion");
        String cardId = (String) map.get("cardId");
        String credit = (String) map.get("recharge");
        String terminalNo = (String) map.get("terminalNo");
        String pinCode;
        if (cardVersion.equals("40")) {
            pinCode = "313233343536";
        } else {
            pinCode = "888888";
        }
        String procType = "02";
        String keyIndex = "01";
        ServiceStatus serviceStatus = BacApplication.obuInterface.loadCreditGetMac1(cardId, Integer.parseInt(credit), terminalNo, pinCode, procType, keyIndex);
        if (serviceStatus.getServiceCode() == 0) {
            String data = serviceStatus.getServiceInfo();
            String a_rnd = data.substring(data.indexOf("a_rnd") + 6, data.indexOf("&a_cbb"));
            String a_on = data.substring(data.indexOf("a_on") + 5, data.length());
            map1.put("a_rnd", a_rnd);
            map1.put("a_on", a_on);
            hashMap.put("data", map1);
            hashMap.put("status", "yes");
        } else {
            ServiceStatus serviceStatus1 = BacApplication.obuInterface.disconnectDevice();
            int str = BacApplication.obuInterface.CloseReader();
            hashMap.put("status", "no");
        }
        jsCallback.invokeAndKeepAlive(hashMap);
    }

    //（ETC盒子的接口）写卡充值：执行写卡操作。
    @JSMethod
    public void loadCreditWriteCardWithInfo(final Map<Object, Object> map, final JSCallback jsCallback) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String mac = (String) map.get("mac");
        ServiceStatus serviceStatus = BacApplication.obuInterface.loadCreditWriteCard(mac);
        if (serviceStatus.getServiceCode() == 0) {
            hashMap.put("status", "yes");
        } else {
            ServiceStatus serviceStatus1 = BacApplication.obuInterface.disconnectDevice();
            int str = BacApplication.obuInterface.CloseReader();
            hashMap.put("status", "no");
            hashMap.put("error", serviceStatus.getMessage());
        }
        jsCallback.invokeAndKeepAlive(hashMap);
    }

    //（ETC盒子的接口）读取卡片交易记录
    @JSMethod
    public void getTransactionOrderMessageWithInfo(final Map<Object, Object> map, final JSCallback jsCallback) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String cardVersion = (String) map.get("cardversion");
        String pinCode;
        if (cardVersion.equals("40")) {
            pinCode = "313233343536";
        } else {
            pinCode = "888888";
        }
        List<CardTransactionRecord> cardRecordList = new ArrayList<CardTransactionRecord>();
        ServiceStatus serviceStatus = BacApplication.obuInterface.readCardTransactionRecord(pinCode, 60, cardRecordList);
        if (serviceStatus.getServiceCode() == 0) {
            hashMap.put("status", "yes");
            hashMap.put("data", cardRecordList);
            System.out.println();
        }
    }

    //支付宝
    @JSMethod
    public void aLiPay(final Map<String, Object> map, String url) {
        map.put("login_phone", getCurrentPhone());
        Method method = new Method();
        method.setMethodName("PAY");
        method.setMap(map);
        String pay_type = map.get("pay_type") + "";
        new GrpcTask(mWXSDKInstance.getContext(), method, null, new aLiPayTask(url, pay_type)).execute();
    }

    //微信
    @JSMethod
    public void weiPay(final Map<String, Object> map, String url) {
        map.put("login_phone", getCurrentPhone());
        Method method = new Method();
        method.setMethodName("PAY");
        method.setMap(map);
        String pay_type = map.get("pay_type") + "";
        new GrpcTask(mWXSDKInstance.getContext(), method, null, new aLiPayTask(url, pay_type)).execute();
    }

    //KYB检测密码
    @JSMethod
    public void checkPassword(String password, final JSCallback jsCallback) {
        Map<String, Object> map = new HashMap<String, Object>();
        Method method = new Method();
        method.setMethodName("VERIFICATE_PAY_PASSWORD");
        map.put("login_phone", BacApplication.getLoginPhone());
        map.put("pay_password", new Encrypt().SHA256(password));
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.add(map);
        method.setListMap(list);
        LoggerUtil.loggerUtil("ETC请求", JSON.toJSONString(method));
        new GrpcTask(mWXSDKInstance.getContext(), method, null, new PasswordTask(jsCallback)).execute();
    }

    //检测密码回调
    private class PasswordTask implements TaskPostExecute {
        public PasswordTask(JSCallback jsCallback) {
            this.jsCallback = jsCallback;
        }

        private JSCallback jsCallback;
        HashMap<String, Object> hashMap = new HashMap<>();

        @Override
        public void onPostExecute(Method result) {
            LoggerUtil.loggerUtil("ETC请求返回", JSON.toJSONString(result));
            if (result.getErrorId() != 0) {
                hashMap.put("status", "no");
                hashMap.put("errorMsg", result.getMsg());
                jsCallback.invokeAndKeepAlive(hashMap);
                return;
            }
            hashMap.put("status", "yes");
            jsCallback.invokeAndKeepAlive(hashMap);

        }
    }

    //免费领取ETC页面
    @JSMethod
    public void pushToNativeWebViewController() {
        UIUtil.startActivityInAnim((AppCompatActivity) mWXSDKInstance.getContext(), new Intent(mWXSDKInstance.getContext(), WebAdvActivity.class).putExtra("title", "ETC")
                .putExtra("ads_url", "http://wechat.bac365.com/wxHosted/raw/ticket/jumpEntryInfo"));
    }

    //跳转设置支付密码页面
    @JSMethod
    public void pushToNativeController(final Map<String, Object> map) {
        String str = (String) map.get("ViewController");
        Context context = mWXSDKInstance.getContext();
        if (str.equals("SetPayPasswordController")) {
            Intent intent = new Intent(context, SetMypswActivity.class);
            UIUtil.startActivityInAnim((AppCompatActivity) context, intent);
        }
    }

    //密码锁定， 请联系客服
    @JSMethod
    public void connectService(final Map<String, Object> map) {
        Context context = mWXSDKInstance.getContext();
        if (map.get("ViewController") == "SetPayPasswordController") {
            Intent intent = new Intent("com.vrphogame.thyroidapp0716.ACTION_START");
            intent.addCategory("com.vrphogame.thyroidapp0716.SETMESSAGE_ACTIVITY");
            UIUtil.startActivityInAnim((AppCompatActivity) context, intent);
        }
    }

    //网络请求
    @JSMethod
    public void requestWithParams(final Map<Object, Object> map, final JSCallback jsCallback) {
        LoggerUtil.loggerUtil("httpServer", JSON.toJSONString(map));
        Method method = new Method();
        method.setMethodName(map.get("methodName") + "");
        method.setListMap((List<Map<String, Object>>) map.get("listMap"));
        LoggerUtil.loggerUtil("ETC请求", JSON.toJSONString(method));
        new GrpcTask(mWXSDKInstance.getContext(), method, null, new ETCTask(jsCallback)).execute();
    }

    //网络请求回调
    private class ETCTask implements TaskPostExecute {
        public ETCTask(JSCallback jsCallback) {
            this.jsCallback = jsCallback;
        }

        private JSCallback jsCallback;
        HashMap<String, Object> hashMap = new HashMap<>();

        @Override
        public void onPostExecute(Method result) {
            LoggerUtil.loggerUtil("ETC请求返回", JSON.toJSONString(result));
            if (result.getErrorId() != 0) {
                hashMap.put("errorMsg", result.getMsg());
                hashMap.put("error", "请重试");
                System.out.println("错误信息：" + result.getMsg());
                jsCallback.invokeAndKeepAlive(hashMap);
                return;
            }
            hashMap.put("data", result.getListMap());
            hashMap.put("status", "yes");

            jsCallback.invokeAndKeepAlive(hashMap);
        }
    }

    //支付回调
    private class aLiPayTask implements TaskPostExecute {
        public aLiPayTask(String url, String pay_type) {//pay_type :支付方式
            this.url = url;
            this.pay_type = pay_type;
        }
        private String url;
        private String pay_type;

        @Override
        public void onPostExecute(Method result) {
            LoggerUtil.loggerUtil("ETC请求返回", JSON.toJSONString(result));
            if (result.getErrorId() != 0) {
                return;
            }
            List<Map<String, Object>> list = result.getListMap();
            Map<String, Object> map = list.get(0);
            if (pay_type.equals("ALIPAY")) {//支付宝支付
                PackageInfo packageInfo;
                try {
                    packageInfo = mWXSDKInstance.getContext().getPackageManager().getPackageInfo(
                            "com.eg.android.AlipayGphone", 0);

                } catch (PackageManager.NameNotFoundException e) {
                    packageInfo = null;
                    e.printStackTrace();
                }
                if (packageInfo == null) {
                    ((AutomaticRxAppCompatActivity) mWXSDKInstance.getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(mWXSDKInstance.getContext(), "您尚未安装支付宝，请安装后再试！");
                        }
                    });
                } else {
                    String paymentUrl = map.get("paymentUrl") + "";
                    Intent intentToPay = new Intent(mWXSDKInstance.getContext(), ZhiFuBaoActivity.class);
                    intentToPay.putExtra("paymentUrl", paymentUrl);
                    intentToPay.putExtra("url", url);
                    UIUtils.startActivityInAnim((AppCompatActivity) mWXSDKInstance.getContext(), intentToPay);
                }
            } else {//微信支付
                HashMap<String, Object> hm_1 = new HashMap<>();
                hm_1.put("id", "oil");
                HashMap<String, Object> hm_2 = new HashMap<>();
                hm_2.put("recharge_money", map.get("pay_money"));
                hm_2.put("top", map.get("content"));
                hm_1.put("data", hm_2);
                WxPayReq.pay(map, hm_1);
            }
            System.out.println("成功跳转支付页面");
        }
    }

    //连接盒子回调
    private final class AutoFocusTask extends AsyncTask<Object, Object, Object> {
        public AutoFocusTask(JSCallback jsCallback) {
            this.jsCallback = jsCallback;
        }
        private JSCallback jsCallback;
        Map<String, Object> map = new HashMap<String, Object>();
        @Override
        protected Object doInBackground(Object... voids) {
            init();
            //DeviceInformation deviceInformation = BacApplication.obuInterface.getDeviceInformation();
            String msg = renzheng();
            if (msg.equals("yes")) {
                map.put("status", "yes");
                map.put("error", "请重试");
                jsCallback.invokeAndKeepAlive(map);
            } else {
                ServiceStatus serviceStatus = BacApplication.obuInterface.connectDevice();
                if (serviceStatus.getServiceCode() == 0) {
                    map.put("status", "yes");
                    jsCallback.invokeAndKeepAlive(map);
                } else {
                    map.put("status", "no");
                    map.put("error", serviceStatus.getMessage());
                    jsCallback.invokeAndKeepAlive(map);
                }
            }
            return null;
        }
    }

    //认证回调
    private final class AutoFocusTask1 extends AsyncTask<Object, Object, Object> {
        public AutoFocusTask1(JSCallback jsCallback) {
            this.jsCallback = jsCallback;
        }

        private JSCallback jsCallback;
        Map<String, Object> map = new HashMap<String, Object>();

        @Override
        protected Object doInBackground(Object... voids) {
            String msg = renzheng();
            if (msg.equals("yes")) {
                map.put("status", "yes");
                jsCallback.invokeAndKeepAlive(map);
            } else {
                map.put("status", "no");
                map.put("error", "请重试");
                jsCallback.invokeAndKeepAlive(map);
            }
            return null;
        }
    }

    //检测蓝牙
    private void init() {
        final WeexActivity2 context = (WeexActivity2) mWXSDKInstance.getContext();
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            return;
        }
        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivityForResult(enableIntent, 0x1);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mWXSDKInstance.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，申请权限
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
    }

    //连接盒子
    public Map<String, Object> connectDevice() {
        Map<String, Object> map = new HashMap<String, Object>();
        ServiceStatus serviceStatus = BacApplication.obuInterface.connectDevice();
        if (serviceStatus.getServiceCode() == 0) {
            map.put("status", "yes");
            return map;
        } else {
            map.put("status", "no");
            map.put("error", serviceStatus.getMessage());
            return map;
        }
    }

    //认证
    public String renzheng() {
        try {
            String intRandom = "12345678";
            String itMac = NewDES.PBOC_3DES_MAC(intRandom, "11223344556677888877665544332211").substring(0, 8);
            int msg1 = BacApplication.obuInterface.intAuthDev(4, intRandom, itMac);
            if (msg1 == 0) {
                System.out.println("认证成功");
                return "yes";
            } else {
                System.out.println("认证失败");
                return "no";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "no";
    }

    //获取卡片信息
    public Map<String, Object> getCardInformation() {
        Map<String, Object> map = new HashMap<String, Object>();
        CardInformation cardInformation = new CardInformation();
        ServiceStatus serviceStatus = BacApplication.obuInterface.getCardInformation(cardInformation);
        if (serviceStatus.getServiceCode() == 0) {
            map.put("data", cardInformation);
            map.put("status", "yes");
        } else {
            map.put("error", serviceStatus.getMessage());
            map.put("status", "no");
        }
        return map;
    }

    //用户ID
    @JSMethod(uiThread = false)
    public String getCustomersID() {
        String s = StringUtil.decode(mWXSDKInstance.getContext(), "customers_id", mWXSDKInstance.getContext().getString(com.bac.bihupapa.R.string.seed_num) + Integer.valueOf(BuildConfig.XX) + BuildConfig.appKeySeed + BuildConfig.appKeySeed2);
        return s;
    }

    //手机号
    @JSMethod(uiThread = false)
    public String getCurrentPhone() {
        //获取登录手机号
        String s = StringUtil.decode(mWXSDKInstance.getContext(), "bac_l", mWXSDKInstance.getContext().getString(com.bac.bihupapa.R.string.seed_num) + Integer.valueOf(BuildConfig.XX) + BuildConfig.appKeySeed + BuildConfig.appKeySeed2);
        return s;
    }

    //返回主页
    @JSMethod(uiThread = false)
    public void pop() {
        UIUtil.startActivityInAnim((AppCompatActivity) mWXSDKInstance.getContext(), new Intent(mWXSDKInstance.getContext(), WeexActivity2.class)//
                .setData(Uri.parse("https://app5.bac365.com:10443/app.pay/photo/weex/ATest/ETC/dist/HomePage.js")));
    }

    //手机唯一标识
    @JSMethod(uiThread = false)
    public String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    //获取盒子 类型
    @JSMethod(uiThread = false)
    public String getDeviceType() {
        String str = "JYBlueDevice";
        return str;
    }

    //错误信息
    private void showDialog(String msg) {
        new AlertDialog.Builder(mWXSDKInstance.getContext()).setTitle("温馨提示")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}

