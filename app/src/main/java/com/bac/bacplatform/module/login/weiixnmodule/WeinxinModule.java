package com.bac.bacplatform.module.login.weiixnmodule;


import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.GrpcTask;
import com.bac.bacplatform.module.login.model.LoginModelImpl;
import com.bac.bacplatform.module.login.presenter.LoginPresenterImpl;
import com.bac.bacplatform.module.login.view.LoginFragment2;
import com.bac.bacplatform.module.main.MainActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bihupapa.BuildConfig;
import com.bac.bihupapa.bean.Method;
import com.bac.bihupapa.grpc.TaskPostExecute;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.logger.LoggerUtil;
import com.bac.commonlib.utils.str.StringUtil;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Build.SERIAL;
import static com.bac.bacplatform.utils.str.StringUtil.encode;

public class WeinxinModule extends WXModule {

    private LoginFragment2 view=new LoginFragment2();

    //微信授权绑定手机号
    @JSMethod(uiThread = false)
    public void WeiXinLoginCallback(final Map<Object, Object> map,final JSCallback jsCallback) {
        String Phone= null;
        String Openid = null;
        Method method=new Method();
        method.setMethodName(map.get("methodName") + "");
        List<Map<String, Object>> list=(List<Map<String, Object>>) map.get("listMap");
        for(Map<String, Object> map1:list){
            Phone= (String) map1.get("login_phone");
            Openid= (String) map1.get("openid");
        }
        loginwx(Openid,Phone);
//        method.setListMap((List<Map<String, Object>>) map.get("listMap"));
//
//        LoggerUtil.loggerUtil("微信授权登录请求", JSON.toJSONString(method));
//
//        new GrpcTask(mWXSDKInstance.getContext(), method, null, new UPDATE_WX_PHONE(Openid,Phone)).execute();

    }
    private class UPDATE_WX_PHONE implements TaskPostExecute {
        public UPDATE_WX_PHONE(String Openid, String Phone) {//pay_type :支付方式
            this.Openid = Openid;
            this.Phone = Phone;
        }
        private String Openid;
        private String Phone;
        @Override
        public void onPostExecute(Method result) {
            LoggerUtil.loggerUtil("微信授权登录请求返回", JSON.toJSONString(result));
            if(result.getErrorId()==0){
                loginwx(Openid,Phone);
            }else{
                Toast.makeText(mWXSDKInstance.getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    //微信授权Openid
    @JSMethod(uiThread = false)
    public String WeiXinOpenidCallback() {
        String s = StringUtil.decode(mWXSDKInstance.getContext(),"wx_info",mWXSDKInstance.getContext().getString(R.string.seed_num) + Integer.valueOf(BuildConfig.XX) + BuildConfig.appKeySeed + BuildConfig.appKeySeed2);
        System.out.println("-----------------------------------------openid:"+s);
        return s;
    }
    //微信登录
    public void loginwx(String openid,String phone){
        List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
        Method method=new Method();
        method.setMethodName("LOGIN");
        Map<String,Object> map=new HashMap<>();
        map.put("login_phone",phone);
//        map.put("wx_info",openid);
        map.put("openid",openid);
        map.put("phone_id",SERIAL.concat("##").concat(
                Settings.Secure.getString(BacApplication.getBacApplication().getContentResolver(),
                        Settings.Secure.ANDROID_ID)));
        list.add(map);
        method.setListMap(list);
        LoggerUtil.loggerUtil("微信授权登录请求", JSON.toJSONString(method));
        new GrpcTask(mWXSDKInstance.getContext(), method, null, new WXloginTask()).execute();
    }
    private class WXloginTask implements TaskPostExecute {
        @Override
        public void onPostExecute(Method result) {
            LoggerUtil.loggerUtil("微信授权登录请求返回", JSON.toJSONString(result));
            if(result.getErrorId()==0){
                Object certificate= null;
                Object is_login= null ;
                Object login_phone= null ;
                Object customers_id = null;
                List<Map<String,Object>> L=result.getListMap();
                for (Map<String,Object> map:L){
                    certificate = map.get("certificate");
                    is_login = map.get("is_login");
                    login_phone = map.get("login_phone");
                    customers_id = map.get("customers_id");
                }
                if (customers_id != null) {
                    PreferenceManager.getDefaultSharedPreferences(BacApplication.getBacApplication())
                            .edit()
                            .putString("customers_id", encode(mWXSDKInstance.getContext(), customers_id))
                            .commit();
                }

                if (certificate != null && !"".equals(certificate)) {
                    PreferenceManager.getDefaultSharedPreferences(BacApplication.getBacApplication())
                            .edit()
                            .putString("certificate", encode(mWXSDKInstance.getContext(), certificate))
                            .commit();
                }

                if (login_phone != null && !"".equals(login_phone)) {

                    BacApplication.setLoginPhone(login_phone + "");
                    // 保存登录手机号
                    PreferenceManager.getDefaultSharedPreferences(BacApplication.getBacApplication())
                            .edit()
                            .putString("bac_l", encode(mWXSDKInstance.getContext(), login_phone))
                            .commit();
                    //9.13
                }
                //String certificate1 = StringUtil.decode(CommonParam.getInstance().getApplication(), "certificate", CommonParam.getInstance().getS());
                if (is_login != null ) {
                    if ((boolean) is_login) {
                        BacHttpBean bean = new BacHttpBean();
                        bean.setMethodName("LOGIN");
                        bean.put("login_phone", login_phone)
                                .put("certificate", certificate);
                        LoginPresenterImpl loginPresenter = new LoginPresenterImpl(view, new LoginModelImpl());
                        loginPresenter.loginOv(bean, false, (AppCompatActivity) mWXSDKInstance.getContext());

                        UIUtils.startActivityInAnimAndFinishSelf((AppCompatActivity) mWXSDKInstance.getContext(), MainActivity.newIntent(mWXSDKInstance.getContext()));
                    } else {
                        Toast.makeText(mWXSDKInstance.getContext(), "登录失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }else{
                Toast.makeText(mWXSDKInstance.getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    }


}

