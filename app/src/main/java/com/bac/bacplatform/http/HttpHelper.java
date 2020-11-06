package com.bac.bacplatform.http;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.extended.base.components.AutomaticBaseFragment;
import com.bac.bacplatform.module.login.LoginActivity;
import com.bac.bacplatform.utils.logger.LogUtil;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bihupapa.grpc.TaskPostExecute;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.http.error.AuthenticateError;
import com.bac.commonlib.http.error.BacHttpError;
import com.bac.commonlib.http.error.CountOutOfBoundsError;
import com.bac.commonlib.http.error.ShowDialogError;
import com.bac.commonlib.http.error.ShowToastError;
import com.bac.commonlib.param.CommonParam;
import com.bac.commonlib.seed.AES;
import com.bac.commonlib.seed.KeyedDigestMD5_HEX;
import com.bac.commonlib.seed.RSA;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.life.automatic.enums.FragmentEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action1;
import rx.functions.Func1;

import static android.os.Build.BRAND;
import static android.os.Build.MODEL;
import static android.os.Build.SERIAL;
import static android.os.Build.VERSION.RELEASE;
import static com.bac.bacplatform.conf.Constants.APP.VERSION_NAME;
import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static java.lang.String.valueOf;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.commonlib.http
 * 创建人：Wjz
 * 创建时间：2017/4/7
 * 类描述：
 * 饿汉式在类创建的同时就已经创建好一个静态的对象供系统使用，以后不再改变，所以天生是线程安全的。
 */

public class HttpHelper {

    private final List<Integer> errorList;
    private static int count;
    private final List<String> noLoginList;
    private final List<Integer> errorCodeToList;
    private CommonParam commonParam = CommonParam.getInstance();


    private HttpHelper() {
        Integer[] errorCode = {2, 3, 4, 5, 10, 11, 12, 13, 14, 15};
        errorList = Arrays.asList(errorCode);

        String[] noLogin = {"LOGIN", "GET_CODE_LOGIN", "BASEXML.QUERY_ANDROID_DOWNLOAD",
                "BASEXML.QUERY_DOWNLOAD",
                "QUERY_CAROUSEL_IMAGES",
                "QUERY_PAGE_INFO",
                "RECORD_PHONE_DETAIL"};
        noLoginList = Arrays.asList(noLogin);

        Integer[] errorCodeToListMap = {10130005};
        errorCodeToList = Arrays.asList(errorCodeToListMap);
    }

    private static final HttpHelper HTTP_HELPER = new HttpHelper();

    //静态工厂方法
    public static HttpHelper getInstance() {
        return HTTP_HELPER;
    }


    private String mSignJson;

    private String mUploadJson;

    private AES mAes;

    /**
     * 无对话框
     *
     * @param bacHttpBean
     * @return
     */
    public Observable<String> bacNet(BacHttpBean bacHttpBean) {
        return bacNetWithContext(null, bacHttpBean);
    }

    /**
     * 显示对话框
     *
     * @param activity
     * @param bacHttpBean
     * @return
     */
    public Observable<String> bacNetWithContext(AppCompatActivity activity, BacHttpBean bacHttpBean) {
        return bacNetWithContextWithOnClickListener(activity, bacHttpBean, null, null, null);
    }

    /**
     * 显示对话框+确认点击事件
     *
     * @param activity
     * @param bacHttpBean
     * @return
     */
    public Observable<String> bacNetWithContextWithOnClickListener(AppCompatActivity activity, BacHttpBean bacHttpBean,
                                                                   final DialogInterface.OnClickListener positive,
                                                                   final DialogInterface.OnClickListener negative,
                                                                   final DialogInterface.OnCancelListener cancel) {
        return net(activity, bacHttpBean, positive, negative, cancel);
    }


    /**
     * Fragment 中自动生命周期和显示加载框
     *
     * @param fragment
     * @param bacHttpBean
     * @return
     */
    public Observable<String> fragmentAutoLifeAndLoading(AutomaticBaseFragment fragment, BacHttpBean bacHttpBean) {
        return fragmentAutoLifeAndLoading(fragment, bacHttpBean, null, null, null);
//        return null;
    }

    /**
     * Fragment 中自动生命周期和显示加载框
     *
     * @param fragment
     * @param bacHttpBean
     */
    public Observable<String> fragmentAutoLifeAndLoading(final AutomaticBaseFragment fragment, BacHttpBean bacHttpBean,
                                                         final DialogInterface.OnClickListener positive,
                                                         final DialogInterface.OnClickListener negative,
                                                         final DialogInterface.OnCancelListener cancel) {
        return bacNetWithContextWithOnClickListener(fragment.activity, bacHttpBean, positive, negative, cancel)
                .compose(new Observable.Transformer<String, String>() {
                    @Override
                    public Observable<String> call(Observable<String> observable) {
                        return observable.compose(fragment.<String>bindUntilEvent(FragmentEvent.DESTROY))
                                .compose(new RxDialog<String>().rxDialog(fragment.activity));
                    }
                });
        //   return null;

    }


    /**
     * Activity 中自动生命周期和显示加载框
     *
     * @param activity
     * @param bacHttpBean
     * @return
     */
    public Observable<String> activityAutoLifeAndLoading(AutomaticBaseActivity activity, BacHttpBean bacHttpBean) {
        return activityAutoLifeAndLoading(activity, bacHttpBean, null, null, null);
    }

    /**
     * Activity 中自动生命周期和显示加载框
     *
     * @param activity
     * @param bacHttpBean
     * @return
     */
    public Observable<String> activityAutoLifeAndLoading(final AutomaticBaseActivity activity, BacHttpBean bacHttpBean,
                                                         final DialogInterface.OnClickListener positive,
                                                         final DialogInterface.OnClickListener negative,
                                                         final DialogInterface.OnCancelListener cancel) {
        return bacNetWithContextWithOnClickListener(activity, bacHttpBean, positive, negative, cancel)
                .compose(new Observable.Transformer<String, String>() {
                    @Override
                    public Observable<String> call(Observable<String> observable) {
                        return observable.compose(activity.<String>bindUntilEvent(ActivityEvent.DESTROY))
                                .compose(new RxDialog<String>().rxDialog(activity));
                    }
                });
        // return null;

    }

    /**
     * 请求数据
     *
     * @param activity
     * @param bacHttpBean
     * @return
     */
    public Observable<String> net(final AppCompatActivity activity, final BacHttpBean bacHttpBean,
                                  final DialogInterface.OnClickListener positive,
                                  final DialogInterface.OnClickListener negative,
                                  final DialogInterface.OnCancelListener cancel) {

        /*
        处理自动登录问题
        进入自动登录 当前无登录手机号 需要手动添加
        */
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        Log.d("时间", "net: " + curDate);

        System.out.println("=====net=====");
        return Observable.create(new Observable.OnSubscribe<BacHttpBean>() {
            @Override
            public void call(Subscriber<? super BacHttpBean> subscriber) {
                System.out.println("Thread2:" + Thread.currentThread().getName());
                subscriber.onNext(bacHttpBean);

                System.out.println(bacHttpBean.getMethodName());
            }
        })
                .observeOn(RxScheduler.RxSerialScheduler())
                .flatMap(new Func1<BacHttpBean, Observable<String>>() {
                    @Override
                    public Observable<String> call(BacHttpBean bacHttpBean) {
                        // 在此处停止， AsyncTask 的同步线程被阻塞
                        System.out.println("Thread4:" + Thread.currentThread().getName());
                        // 判断sRsaMap 是否申请
                        if (commonParam.getmRsaMap() == null || commonParam.getmPrivateKey() == null || commonParam.getmToken() == null || commonParam.getmSession() == null) {
                            throw new BacHttpError();
                        } else if (!noLoginList.contains(bacHttpBean.getMethodName())) {// 非登录接口

                            // 是否传入当前登录的手机号
                            Map<String, Object> map = bacHttpBean.getListMap().get(_0);
                            if (map.get("login_phone") == null) {// 未传登录手机号
                                // 判断是否登录
                                String loginPhone = BacApplication.getLoginPhone();

                                if (!TextUtils.isEmpty(loginPhone)) {// 登陆
                                    map.put("login_phone", loginPhone);
                                } else { // 未登陆
                                    return Observable.error(new AuthenticateError());
                                }
                            }
                        }


                        try {
                            // 加解密
                            Log.d("loglog1485", "call: comparam "+commonParam.getmPrivateKey());
                            byte[] decode = Base64.decode(commonParam.getmPrivateKey(), 2);
                            mAes = new AES(decode);
                            // 上传数据
                            String toJSONString = JSON.toJSONString(bacHttpBean);

                            Log.d("loglog1485", "call: json "+toJSONString);

                            // 上传数据打印
                            LogUtil.sf(HttpHelper.this, "上传数据：" + toJSONString);
                            byte[] bytes = toJSONString.getBytes();
                            byte[] aes2Str = mAes.encrypt(bytes);//加密json
                            byte[] base2Str = Base64.encode(aes2Str, 2);//編碼加密的json
                            String fMd5 = KeyedDigestMD5_HEX.getKeyedDigest(new String(base2Str), new String(decode));//
                            Log.d("loglog1485", "call: MD5 "+ fMd5);
                            mSignJson = URLEncoder.encode(fMd5, "UTF-8");
                            Log.d("loglog1485", "call: MD5 URL "+ mSignJson);
                            mUploadJson = URLEncoder.encode(new String(base2Str), "UTF-8");
                            Log.d("loglog1483", "call: uploadJSON "+mUploadJson);
                        } catch (Exception e) {
                            Observable.error(e);
                        }

                        // 组拼数据
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("VER", VERSION_NAME);
                        hashMap.put("AES", "Y");
                        hashMap.put("ZIP", "N");
                        hashMap.put("TOKEN", commonParam.getmToken());
                        hashMap.put("ID", commonParam.getmSession());
                        hashMap.put("SIGN", mSignJson);
                        hashMap.put("JSON", mUploadJson);

                        // 请求接口的数据 非GET_KEY
                        return GET_DATA(hashMap);
                    }
                })
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
                        Date curDate = new Date(System.currentTimeMillis());
                        Log.d("时间", "net_call" + curDate);

                        //11.28添加参数activity
                        return retryWhenError(activity, observable);
                    }
                })

                .onErrorResumeNext(new Func1<Throwable, Observable<? extends String>>() {
                    @Override
                    public Observable<? extends String> call(Throwable throwable) {
                        if (throwable instanceof AuthenticateError) {
                            // 登录错误,无证书,去登录页面
                            return Observable.just("")
                                    .map(new Func1<String, String>() {
                                        @Override
                                        public String call(String s) {
                                            // 不存在 启动登录界面
                                            if (null != activity) {
                                                UIUtils.startActivityInAnim(activity, new Intent(activity, LoginActivity.class));
                                            } else {
                                                BacApplication bacApplication = BacApplication.getBacApplication();
                                                Intent intent = new Intent(bacApplication, LoginActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                bacApplication.startActivity(intent);
                                            }
                                            return null;
                                        }
                                    });
                        } else if (throwable instanceof CountOutOfBoundsError) {
                            return Observable.just(null);
                        } else {
                            // 切换线程显示对话
                            return showAlert(throwable, activity, positive, negative, cancel);
                        }
                    }
                })
                .observeOn(RxScheduler.RxPoolScheduler())
                // 过滤null
                .takeFirst(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        count = 0;
                        return null != s;
                    }
                })
                .subscribeOn(RxScheduler.RxPoolScheduler());
                /*.subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object object) {
                        System.out.println(object);
                    }
                });*/
    }


    @NonNull
    private Observable<String> showAlert(Throwable throwable, final AppCompatActivity activity, final DialogInterface.OnClickListener positive, final DialogInterface.OnClickListener negative, final DialogInterface.OnCancelListener cancel) {
        return Observable.just(throwable)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Throwable, String>() {
                    @Override
                    public String call(Throwable throwable) {

                        String message = throwable.getMessage();
                        if (!TextUtils.isEmpty(message)) {
                            if (throwable instanceof ShowDialogError) {
                                if (activity != null) {
                                    new AlertDialog.Builder(activity)
                                            .setTitle(activity.getString(R.string.alert_title))
                                            .setMessage(message)
                                            .setPositiveButton(activity.getString(R.string.alert_confirm), positive)
                                            .setNegativeButton(activity.getString(R.string.alert_cancel), negative)
                                            .setOnCancelListener(cancel)
                                            .show();
                                }
                            } else if (throwable instanceof ShowToastError) {
                                Toast.makeText(BacApplication.getBacApplication(), message, Toast.LENGTH_SHORT).show();
                            }
                        }
                        return null;
                    }
                });
    }

    /**
     * 错误处理
     *
     * @param observable
     * @return
     */
    //11.28添加参数activity
    private Observable<?> retryWhenError(final Activity activity, final Observable<? extends Throwable> observable) {
        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                Log.d("时间", "retryWhenError: " + curDate);

                count++;
                if (count > 7) {
                    throw new CountOutOfBoundsError();
                }
                // 指定的error 和 请求超时的 error 会重新请求,其他抛出
                else if (throwable instanceof BacHttpError || throwable instanceof OnErrorNotImplementedException) {
                    try {
                        // 获取RSA
                        commonParam.setmRsaMap(RSA.generateKeyPair());

                        // 组拼JSON
                        BacHttpBean bean = new BacHttpBean()
                                .setMethodName("GET_KEY")
                                .put("PublicKey", commonParam.getmRsaMap().get("publicKey"))
                                .put("DeviceName", MODEL)
                                .put("SystemName", BRAND)
                                .put("SystemVersion", RELEASE)
                                .put("PhoneId",
                                        SERIAL.concat("##").concat(
                                                Settings.Secure.getString(
                                                        BacApplication.getBacApplication().getContentResolver(),
                                                        Settings.Secure.ANDROID_ID))
                                );

                        // 3.AES加密 -> RSA公钥
                        AES aesLocal = new AES(BacApplication.getSeed().getBytes());
                        byte[] rsaPublicAes = aesLocal.encrypt(JSON.toJSONString(bean).getBytes());
                        // 4.MD5 -> AES加密 -> RSA公钥
                        byte[] encode = Base64.encode(rsaPublicAes, 2);
                        String strSrc = new String(encode);
                        String aesRsaMd5 = KeyedDigestMD5_HEX.getKeyedDigest(strSrc, BacApplication.getSeed());
                        // 5.urlCode
                        // URLEncoder  md5
                        mSignJson = URLEncoder.encode(aesRsaMd5, "UTF-8");
                        // URLEncoder  RSA公钥的AES加密
                        mUploadJson = URLEncoder.encode(strSrc, "UTF-8");
                    } catch (Exception e) {
                        // 不可能出的现异常
                        Observable.error(e);
//                        SimpleDateFormat formatter4   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss");
//                        Date curDate4 =  new Date(System.currentTimeMillis());
//                        Log.d("时间", "Exception: "+curDate4);
                    }

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("VER", VERSION_NAME);
                    hashMap.put("AES", "F");
                    hashMap.put("ZIP", "N");
                    hashMap.put("ID", "bacplatform");
                    hashMap.put("SIGN", mSignJson);
                    hashMap.put("JSON", mUploadJson);

                    // 这些错误 都是需要重新加解密 获取秘钥
                    return GET_KEY(hashMap);
                } else if (throwable instanceof AuthenticateError) {
                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
                    Date curDate2 = new Date(System.currentTimeMillis());
                    Log.d("时间", "AuthenticateError: " + curDate2);

                    // 判断当前是否有证书
                    String certificate = StringUtil.decode(BacApplication.getBacApplication(), "certificate");
                    if (TextUtils.isEmpty(certificate)) {
                        // 没有证书
                        return Observable.error(new Exception());
                    } else {
                        SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
                        Date curDate3 = new Date(System.currentTimeMillis());
                        Log.d("时间", "AuthenticateError:call " + curDate3);
                        // 有证书
                        //11.28添加参数activity
                        return LOGIN(activity, certificate);
                    }

                }
                return Observable.error(throwable);
            }
        });
    }

    /**
     * 证书登录
     *
     * @param certificate
     * @return
     */
    //11.28添加参数activity
    public Observable<String> LOGIN(final Activity activity, String certificate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        Log.d("时间", "LOGIN: " + curDate);
//            public Observable<String> LOGIN(final Activity activity, String certificate) {

        return Observable.just(certificate)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        // 获取登录手机号
                        final String loginPhone = StringUtil.decode(BacApplication.getBacApplication(), "bac_l");
                        //12.8 修改部分用户自动登录异常问题，登录手机号和证书为空
                        if (loginPhone == null || loginPhone == "" || s == null || s == "") {
                            UIUtils.startActivityInAnim((AppCompatActivity) activity, new Intent(activity, LoginActivity.class));
                            return null;
                        } else {
//                            Method method = new Method();
//                            method.setMethodName("LOGIN");
//                            method.put("login_phone", loginPhone);
//                            method.put("certificate", s);
//                            method.put("phone_id", SERIAL.concat("##").concat(
//                                    Settings.Secure.getString(BacApplication.getBacApplication().getContentResolver(),
//                                            Settings.Secure.ANDROID_ID)));
//                           return new GrpcTask(activity,method,null,new LoginTask()).execute();
                            count = 0;
                            // 设置登录手机号
                            BacApplication.setLoginPhone(loginPhone);
                            Log.d("loglog111", "call: login "+loginPhone);
                            return HttpHelper.getInstance()
                                    .bacNet(new BacHttpBean()
                                            .setMethodName("LOGIN")
                                            .put("login_phone", loginPhone)
                                            .put("certificate", s)
                                            .put("phone_id", SERIAL.concat("##").concat(
                                                    Settings.Secure.getString(BacApplication.getBacApplication().getContentResolver(),
                                                            Settings.Secure.ANDROID_ID)))
                                    )
                                    .doOnNext(new Action1<String>() {
                                        @Override
                                        public void call(String s) {
                                            Log.d("loglog111", "call2 :  onSetPhone");
                                            count = 0;
                                            // 设置登录手机号
                                            BacApplication.setLoginPhone(loginPhone);
                                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
                                            Date curDate = new Date(System.currentTimeMillis());
                                            Log.d("时间", "LOGIN2: " + curDate);

                                        }
                                    });

                        }


                    }
                });

    }

    private class LoginTask implements TaskPostExecute {

        public LoginTask() {
        }

        @Override

        public void onPostExecute(com.bac.bihupapa.bean.Method result) {

        }
    }

    /**
     * 交换RSA
     *
     * @param hashMap
     * @return
     */
    private Observable<?> GET_KEY(HashMap<String, String> hashMap) {
        SimpleDateFormat formatter4 = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate4 = new Date(System.currentTimeMillis());
        Log.d("时间", "GET_KEY: " + curDate4);
        return BacApplication.getBacApi()
                .get(hashMap)
                .doOnNext(new Action1<HashMap<String, Object>>() {
                    @Override
                    public void call(HashMap<String, Object> map) {
                        commonParam.setmSession(valueOf(map.get("ID")));
                        commonParam.setmToken(valueOf(map.get("TOKEN")));

                        count = 0;

                        // GET_KEY 的错误
                        Object error = map.get("ERROR");
                        if (error != null && errorList.contains(error)) {
                            Observable.error(new Exception(valueOf(map.get("MSG"))));
                        }

                        // 交换RSA后的解密
                        try {
                            byte[] decode = Base64.decode(valueOf(map.get("JSON")).getBytes(), 2);
                            AES aes = new AES(BacApplication.getSeed().getBytes());
                            String json = new String(aes.decrypt(decode));
                            BacHttpBean httpBean = JSON.parseObject(json, BacHttpBean.class);
                            // token
                            // mToken = httpBean.getToken();
                            // rsa
                            commonParam.setmPrivateKey(RSA.decrypt(valueOf(httpBean.getListMap().get(0).get("KEY")), commonParam.getmRsaMap().get("privateKey")));
                        } catch (Exception e) {
                            Observable.error(e);
                        }
                    }
                });
    }

    /**
     * 请求数据
     *
     * @param hashMap
     * @return
     */
    private Observable<String> GET_DATA(final HashMap<String, String> hashMap) {
        Log.d("loglog111", "GET_DATA: ");
        return BacApplication.getBacApi()
                .get(hashMap)
                .map(new Func1<HashMap<String, Object>, String>() {
                    @Override
                    public String call(HashMap<String, Object> map) {
                        Log.d("loglog111", "call: parse json");
                        // 此处有JSON解析
                        if (Constants.APP.DEBUG) {
                            LogUtil.sf(HttpHelper.this, JSON.toJSONString(map));
                        }

                        //mSession = valueOf(map.get("ID"));

                        commonParam.setmToken(valueOf(map.get("TOKEN")));
                        // 解密数据
                        // 解析数据
                        // 优先处理 0
                        Object error = map.get("ERROR");
                        if (null == error) {
                            return null;
                        } else {
                            int err = (int) error;
                            if (err == 0) { // 0
                                String s = null;
                                try {
                                    // 解密后的数据
                                    Map<String, Object> mapJson = JSON.parseObject(new String(mAes.decrypt(Base64.decode(valueOf(map.get("JSON")), 2))), new TypeReference<Map<String, Object>>() {
                                    }.getType());
                                    s = mapJson.get("listMap") + "";//[{}]
                                } catch (Exception e) {
                                    Observable.error(e);
                                }
                                LogUtil.sf(HttpHelper.this, "返回数据：" + s);
                                if (!TextUtils.isEmpty(s)) {
                                    return s;
                                } else {
                                    throw new ShowToastError();
                                }
                            } else if (errorList.contains(err)) {// 0 -> ok
                                // 码表中的错误
                                throw new BacHttpError();
                            } else if (err == 1) {// 没登录，
                                throw new AuthenticateError();
                            }
                            // 登录错误AuthenticateError 被retryWhen捕获, 证书错误，登录手机号没查到，
                            // 去登录界面
                            else if (err == 10010006 || err == 10010008) {
                                // 删除证书
                                // 删除账户
                                PreferenceManager.
                                        getDefaultSharedPreferences(BacApplication.getBacApplication())
                                        .edit().remove("bac_l").remove("certificate").commit();

                                throw new AuthenticateError();
                            } else if (err == -2) {
                                throw new ShowDialogError(map.get("MSG") + "");
                            }
                            else if (err == 10019999) {
                                //throw new LoginError(map.get("methodName").toString());
                                throw new ShowToastError(map.get("MSG") + "");

                            }
                            else if (errorCodeToList.contains(err)) {
                                List<HashMap<String, Object>> list = new ArrayList<>();
                                list.add(map);
                                return JSON.toJSONString(list);
                            } else if (err > 1000) { // toast 提示
                                throw new ShowToastError(map.get("MSG") + "");
                            } else {
                                throw new ShowToastError();
                            }
                        }
                    }
                });
    }
}
