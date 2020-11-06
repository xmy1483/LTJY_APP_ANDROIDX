package com.bac.bacplatform.module.login.presenter;

import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.http.GrpcTask;
import com.bac.bacplatform.module.login.contract.LoginContract;
import com.bac.bacplatform.module.login.view.LoginFragment2;
import com.bac.bacplatform.module.main.MainActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bihupapa.bean.Method;
import com.bac.bihupapa.grpc.TaskPostExecute;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.commonlib.utils.logger.LoggerUtil;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.FragmentEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.wjz.weexlib.weex.activity.WeexActivity2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static android.os.Build.SERIAL;
import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.utils.str.StringUtil.encode;


/**
 * Created by maibenben on 2017/05/03
 */

public class LoginPresenterImpl implements LoginContract.Presenter {


    private final LoginContract.Model model;

    private final LoginFragment2 view;
    private String openid;

    public LoginPresenterImpl(LoginFragment2 view, LoginContract.Model model) {
        this.view = view;
        this.model = model;
        // view 绑定 presenter
        view.setPresenter(this);
    }


    @Override
    public void sendMsg(BacHttpBean bean, boolean refreshData) {
        model.getData(bean, refreshData, view.activity)
                .compose(view.<String>bindUntilEvent(FragmentEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(view.activity))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {
                        Map<String, Object> map = mapList.get(_0);
                        if (map != null) {
                            view.sendMsg(map);
                        }
                    }
                });
    }

    @Override
    public void login(BacHttpBean bean, boolean refreshData, AppCompatActivity activity) {
        // TODO 改成grpc方式调用
        Method method = new Method();
        method.setMethodName(bean.getMethodName());
        method.setListMap(bean.getListMap());
        new GrpcTask(view.activity, method, null, new PasswordTask1()).execute();
    }

    public void loginOv(BacHttpBean bean, boolean refreshData, AppCompatActivity activity) {
        // TODO 原系统login
        model.getData(bean, refreshData, activity)
                .compose(view.<String>bindUntilEvent(FragmentEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(view.activity))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {

                    }
                });
    }

    private class PasswordTask1 implements TaskPostExecute {
        @Override
        public void onPostExecute(Method result) {
            if(result.getErrorId()==0){
                System.out.println("-----------------------登录成功");
                view.login(result.getListMap().get(0));
            }else{
                Toast.makeText(view.activity, result.getMsg(), Toast.LENGTH_SHORT).show();
            }

        }
    }
    //查询微信登录关联手机号（查不到让用户填信息）
    public void WeiXinLogin(String openid){
        Object wx_info=openid;
        if (wx_info != null&& !"".equals(wx_info)) {
            PreferenceManager.getDefaultSharedPreferences(BacApplication.getBacApplication())
                    .edit()
                    .putString("wx_info", encode(view.activity, wx_info))
                    .commit();
        }
        Method method=new Method();
        method.setMethodName("BASEXML.QUERY_WX_PHONE");
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("openid",openid);
        List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
        list.add(map);
        method.setListMap(list);
        System.out.println("--------------------------------------查询微信登录是否关联手机号");
        LoggerUtil.loggerUtil("微信授权登录请求", JSON.toJSONString(method));
        new GrpcTask(view.activity, method, null, new QUERY_WX_PHONE(openid)).execute();
    }

    private class QUERY_WX_PHONE implements TaskPostExecute {
        public QUERY_WX_PHONE(String Openid) {
            this.Openid = Openid;
        }
        private String Openid;
        @Override
        public void onPostExecute(Method result) {
            LoggerUtil.loggerUtil("微信授权登录请求返回", JSON.toJSONString(result));
            if(result.getErrorId()==0){
                if(result.getListMap().size()!=0){//已经绑定手机号
                    String phone= (String) result.getListMap().get(0).get("phone");
                    loginwx(Openid,phone);
                }else{//没有绑定过手机号
                    System.out.println("----------------------------没有绑定过手机号");
                    UIUtil.startActivityInAnim(view.activity, new Intent(view.activity, WeexActivity2.class)
                            .setData(Uri.parse("https://app5.bac365.com:10443/app.pay/photo/weex/ATest/wx/dist/index.js")));
                }
            }else{
                Toast.makeText(view.activity, result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    }
//    //已经绑定手机号的微信登录
    public void loginwx(String openid,String phone){
        Method method=new Method();
        method.setMethodName("LOGIN");
        Map<String,Object> map=new HashMap<>();
        map.put("login_phone",phone);
        map.put("openid",openid);
//        map.put("wx_info",openid);
        map.put("phone_id",SERIAL.concat("##").concat(
                Settings.Secure.getString(BacApplication.getBacApplication().getContentResolver(),
                        Settings.Secure.ANDROID_ID)));
        method.getListMap().add(map);
        System.out.println("----------------------------绑定过手机号");
        new GrpcTask(view.activity, method, null, new WXloginTask()).execute();
    }

    private class WXloginTask implements TaskPostExecute {
        @Override
        public void onPostExecute(Method result) {
            if(result.getErrorId()==0){
                System.out.println("----------------------------绑定过手机号222222");
                Object is_login = (Object) result.getListMap().get(0).get("is_login");
                Object customers_id =(Object) result.getListMap().get(0).get("customers_id");
                Object login_phone= (Object) result.getListMap().get(0).get("login_phone");
                Object certificate= (Object)result.getListMap().get(0).get("certificate");
                if (customers_id != null) {
                    PreferenceManager.getDefaultSharedPreferences(BacApplication.getBacApplication())
                            .edit()
                            .putString("customers_id", encode(view.activity, customers_id))
                            .commit();
                }

                if (certificate != null && !"".equals(certificate)) {
                    PreferenceManager.getDefaultSharedPreferences(BacApplication.getBacApplication())
                            .edit()
                            .putString("certificate", encode(view.activity, certificate))
                            .commit();

                }

                if (login_phone != null && !"".equals(login_phone)) {

                    BacApplication.setLoginPhone(login_phone + "");
                    // 保存登录手机号
                    PreferenceManager.getDefaultSharedPreferences(BacApplication.getBacApplication())
                            .edit()
                            .putString("bac_l", encode(view.activity, login_phone))
                            .commit();
                    //9.13
                }
                //String certificate1 = StringUtil.decode(CommonParam.getInstance().getApplication(), "certificate", CommonParam.getInstance().getS());
                if (is_login != null ) {
                    if ((boolean) is_login) {
//                        String s = StringUtil.decode(view.activity,"wx_info",view.activity.getString(R.string.seed_num) + Integer.valueOf(BuildConfig.XX) + BuildConfig.appKeySeed + BuildConfig.appKeySeed2);
                        BacHttpBean bean = new BacHttpBean();
                        bean.setMethodName("LOGIN");
                        bean.put("login_phone", login_phone)
                                .put("certificate", certificate);
//                        System.out.println("----------------------------执行原系统LOGIN获取openid:"+s);
                             model.getData(bean, false, view.activity)
                                .compose(view.<String>bindUntilEvent(FragmentEvent.DESTROY))
                                .compose(new RxDialog<String>().rxDialog(view.activity))
                                .observeOn(RxScheduler.RxPoolScheduler())
                                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<List<Map<String, Object>>>() {
                                    @Override
                                    public void call(List<Map<String, Object>> mapList) {
                                        Map<String, Object> map = mapList.get(_0);
                                        if (map != null) {
                                            System.out.println("----------------------------执行跳转首页");
                                            UIUtils.startActivityInAnim(view.activity, new Intent(view.activity, MainActivity.class));
                                           // UIUtils.startActivityInAnimAndFinishSelf(view.activity, MainActivity.newIntent(view.activity));
                                            System.out.println("----------------------------跳转首页成功");
                                        }else{
                                            Toast.makeText(view.activity, "登录失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
//                       loginOv(bean,false,view.activity);
                    } else {
                        Toast.makeText(view.activity, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }else{
                Toast.makeText(view.activity, result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}