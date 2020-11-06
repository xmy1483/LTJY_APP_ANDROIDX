package com.bac.bihupapa.weexmodel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bac.bihupapa.activity.CaptureActivity;
import com.bac.bihupapa.bean.Method;
import com.bac.bihupapa.grpc.GrpcTask;
import com.bac.bihupapa.grpc.TaskPostExecute;
import com.bac.commonlib.hybrid.WebViewHybrid;
import com.bac.commonlib.param.CommonParam;
import com.bac.commonlib.utils.logger.LoggerUtil;
import com.bac.commonlib.utils.str.StringUtil;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.permission.Permission;
import com.bac.rxbaclib.rx.permission.RxPermissionImpl;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.wjz.weexlib.weex.AppManager;
import com.wjz.weexlib.weex.activity.WeexActivity2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import static android.os.Build.SERIAL;

/**
 * Created by wujiazhen on 2017/8/28.
 */

public class BhppHttpModule extends WXModule {
    //private static JSCallback jsCallback;


    private static String id;

    public static Method getRes() {
        return res;
    }

    public static void setRes(Method res) {
        BhppHttpModule.res = res;
    }

    private static Method res;


    public static String getPhoneId() {
        return PHONE_ID;
    }

    public static void setPhoneId(String phoneId) {
        PHONE_ID = phoneId;
    }

    private static String PHONE_ID=null;

    private List<String> sd = Arrays.asList("BHPP_BASE.CLICK_BHPP", "BHPP_BASE.QUERY_CAROUSEL_IMAGES", "ACTIVITIE.QUERY_CUR_TASK"
            , "ACTIVITIE.QUERY_LATEST_MESSAGE", "SHOW_BUTTON");

    public BhppHttpModule() {

    }

    @JSMethod
    public void openNewContrller() {
        AppManager.getInstance().finishAllActivity();
        UIUtil.startActivityInAnim((AppCompatActivity) mWXSDKInstance.getContext(), new Intent(mWXSDKInstance.getContext(), WeexActivity2.class)
                .setData(Uri.parse("file://file/dist/index.js")));

    }

    @JSMethod
    public void httpServer(final Map<String, Object> map, final JSCallback jsCallback) throws Exception {
        if(PHONE_ID==null) {
            PHONE_ID = SERIAL.concat("##").concat(
                    Settings.Secure.getString(mWXSDKInstance.getContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID));
            GrpcTask.setID(PHONE_ID);
        }

        //setPhoneId(Util.getDeviceID(mWXSDKInstance.getContext()));

        LoggerUtil.loggerUtil("BHPPhttpServer", JSON.toJSONString(map));
//
        Method method = new Method();
        method.setMethodName(map.get("methodName") + "");
        method.setListMap((List<Map<String, Object>>) map.get("listMap"));
        ///bean.setMethodName(map.get("methodName").toString()).getListMap().add(map);
        LoggerUtil.loggerUtil("壁虎趴趴请求",JSON.toJSONString(method));
        new GrpcTask(mWXSDKInstance.getContext(),method,null,new BhppTask(jsCallback)).execute();
    }

    private class BhppTask implements TaskPostExecute {
        public BhppTask(JSCallback jsCallback) {
            this.jsCallback = jsCallback;
        }

        private JSCallback jsCallback;






        public void onPostExecute(Method result) {
            LoggerUtil.loggerUtil("壁虎趴趴请求返回",JSON.toJSONString(result) );
            if (result.getErrorId() != 0) {
                showDialog(result.getMsg());
                // Log.d(TAG, "错误信息: "+result.getErrorId()+"错误信息——————"+result.getMsg());
                return;
            }
             //判断超过申请日期的广告活动需要下线
         if(result.getMethodName().equals("ACTIVITIE.QUERY_ACTIVITIES")){
                ArrayList<Map<String, Object>>list= (ArrayList<Map<String, Object>>) result.getListMap();
              SimpleDateFormat df = null;//设置日期格式
                  df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                  long ts=System.currentTimeMillis();//获取当前的系统时间撮
                  System.out.println("====================================================当前时间为："+ts);
                  for(int i=0;i<list.size();){
                  Map<String, Object> map= list.get(i);
                  System.out.println("==========================================报名时间"+map.get("end_time"));
                  long l = (long) map.get("end_time");
                  if(l<ts){
                      System.out.println("==========================================报名时间已过");
                      list.remove(i);
                  }
                  else{
                      i++;
                  }
              }
         }

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("data", result);
//            String json = JSON.toJSONString(hashMap);
//            System.out.println("123" + json);
            jsCallback.invokeAndKeepAlive(hashMap);
        }
    }

    /**
     * 自己将手机的信息传入回调
     * (先要的信息currentPhone登录手机号,currentversion当前版本号,customers_id用户的id,)
     * (currentPhone,currentversion,customers_id都为key值)
     *
     * @param map
     * @param jsCallback
     */
    @JSMethod
    public void getUserInfo(final Map<String, Object> map, final JSCallback jsCallback) {
        HashMap<String, Object> hashMap = new HashMap<>();
        Observable.just(hashMap)
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<HashMap<String, Object>, HashMap<String, Object>>() {
                    @Override
                    public HashMap<String, Object> call(HashMap<String, Object> hashMap) {
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("currentPhone", StringUtil.decode(CommonParam.getInstance().getApplication(), "bac_l", CommonParam.getInstance().getS()));
                        hm.put("currentversion", CommonParam.getInstance().getVersionName());
                        hm.put("customers_id", StringUtil.decode(CommonParam.getInstance().getApplication(), "customers_id", CommonParam.getInstance().getS()));
                        hashMap.put("data", JSON.toJSONString(hm));
                        return hashMap;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(((AutomaticRxAppCompatActivity) mWXSDKInstance.getContext()).<HashMap<String, Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<HashMap<String, Object>>() {
                    @Override
                    public void call(HashMap<String, Object> hashMap) {
                        jsCallback.invokeAndKeepAlive(hashMap);
                    }
                });
    }

    @JSMethod
    public void toTakePhotoView(final Map<String, Object> map, final JSCallback jsCallback) {
        final WeexActivity2 context = (WeexActivity2) mWXSDKInstance.getContext();

//        Context context = mWXSDKInstance.getContext();
        // 拍摄行驶证
        //     this.jsCallback = jsCallback;
        //jsCallback.invokeAndKeepAlive(this.jsCallback);
        try {
            if (map != null && map.get("type").toString().equals("emergency")) {
                Intent intentTop = new Intent(context, CaptureActivity.class);
                intentTop.putExtra("title", "上传图片");
                intentTop.putExtra("label1", "拍摄照片");
                intentTop.putExtra("label2", "讲所需拍摄的照片放入方框内");
                intentTop.putExtra("picName", "7890");
                intentTop.putExtra("tag", 3);
                UIUtil.startActivityInAnim(context, intentTop);
            }
        } catch (Exception e) {
            e.printStackTrace();

//            Intent intentTop = new Intent(context, CaptureActivity.class);
//            intentTop.putExtra("title", "我的车辆");
//            intentTop.putExtra("label1", "拍摄行驶证");
//            intentTop.putExtra("label2", "将行驶证正面放入方框内");
//            intentTop.putExtra("picName", "7890");
//            intentTop.putExtra("tag", 123);

            context.startActivityForResult(new Intent(context, CaptureActivity.class).putExtra("tag", 6), 1);
            context.setWeexResultCallback(new WeexActivity2.WeexResultCallback() {
                @Override
                public void callback(int requestCode, int resultCode, Intent data) {
                    if (resultCode == 1) {
                        if (requestCode == 1) {
                            if (data != null) {
//                                ArrayList<String> result = data.getStringArrayListExtra("result");
//                                HashMap<String, Object> hashMap = new HashMap<>();
//                                hashMap.put("data", result);
//                                jsCallback.invokeAndKeepAlive(hashMap);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("UUID", ((ArrayList) data.getExtras().get("data")).get(0));
                                jsonObject.put("url", ((ArrayList) data.getExtras().get("data")).get(1));
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("data", jsonObject.toString());
                                jsCallback.invokeAndKeepAlive(hashMap);
                            }
                        }
                    }
                }
            });
        }
    }

    @JSMethod
    public void callNative(final Map<String, String> map, final JSCallback jsCallback) {
        Context context = mWXSDKInstance.getContext();
        String nativeName = map.get("nativeName");
        if (!TextUtils.isEmpty(nativeName)) {
            if ("callPhone".equals(nativeName)) {
                Observable.just("")
                        .compose(new RxPermissionImpl((AppCompatActivity) context).ensureEach(android.Manifest.permission.CALL_PHONE))
                        .subscribe(new Action1<Permission>() {
                            @Override
                            public void call(Permission permission) {
                                if (permission.isGranted()) {
                                    String number = "4001106262";
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse("tel:" + number));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    CommonParam.getInstance().getApplication().startActivity(intent);
                                }
                            }
                        });
            }
            if ("kaiyoubao".equals(nativeName)) {
//              揩油宝界面   from: weex
                //  UIUtil.startActivityInAnim(context,new Intent(context,KaiYouBaoActivity.class));
            }
        }

    }

    @JSMethod
    public void toWebView(String s) {
        Context context = mWXSDKInstance.getContext();
        UIUtil.startActivityInAnim((AppCompatActivity) context,
                new Intent(context, WebViewHybrid.class)
                        .putExtra("title", "活动")
                        .putExtra("ads_url", s)
        );
    }

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


