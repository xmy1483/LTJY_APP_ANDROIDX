package com.bac.commonlib.http;

import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
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
import com.bac.commonlib.utils.str.StringUtil;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action1;
import rx.functions.Func1;

import static android.os.Build.BRAND;
import static android.os.Build.MODEL;
import static android.os.Build.SERIAL;
import static android.os.Build.VERSION.RELEASE;
import static java.lang.String.valueOf;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.commonlib.http
 * 创建人：Wjz
 * 创建时间：2017/4/7
 * 类描述：
 * 饿汉式在类创建的同时就已经创建好一个静态的对象供系统使用，以后不再改变，所以天生是线程安全的。
 *
 * 线程太碎，有些地方除了问题，
 * 从加密到网络请求，到服务器返回的 token 应该在同一个线程中完成
 */
@Deprecated
public class HttpHelperLib2 {

    private final List<Integer> errorList;
    private static int count;
    private final List<String> noLoginList;
    private final List<Integer> errorCodeToList;


    private HttpHelperLib2() {
        Integer[] errorCode = {2, 3, 4, 5, 10, 11, 12, 13, 14, 15};
        errorList = Arrays.asList(errorCode);

        String[] noLogin = {"LOGIN", "GET_CODE_LOGIN", "BASEXML.QUERY_ANDROID_DOWNLOAD",
                "QUERY_CAROUSEL_IMAGES",
                "QUERY_PAGE_INFO",
                "RECORD_PHONE_DETAIL"};
        noLoginList = Arrays.asList(noLogin);

        Integer[] errorCodeToListMap = {10130005};
        errorCodeToList = Arrays.asList(errorCodeToListMap);
    }

    private static final HttpHelperLib2 HTTP_HELPER_LIB = new HttpHelperLib2();

    //静态工厂方法
    public static HttpHelperLib2 getInstance() {
        return HTTP_HELPER_LIB;
    }

    private Map<String, String> mRsaMap;

    private String mToken;

    private String mSession;

    private String mPrivateKey;

    private String mSignJson;

    private String mUploadJson;
    private AES mAes;

    /**
     * 请求数据
     *
     * @param bacHttpBean
     * @return
     */
    public Observable<String> net(BacHttpBean bacHttpBean, final Dialog dialog, final Class clazz) {

        /*
        处理自动登录问题
        进入自动登录 当前无登录手机号 需要手动添加
        */
        return Observable
                .just(bacHttpBean)
                // io 线程
                .observeOn(RxScheduler.RxPoolScheduler())
                .flatMap(new Func1<BacHttpBean, Observable<String>>() {
                    @Override
                    public Observable<String> call(BacHttpBean bacHttpBean) {
                        // 判断sRsaMap 是否申请
                        if (mRsaMap == null || mPrivateKey == null || mToken == null || mSession == null) {
                            throw new BacHttpError();
                        } else if (!noLoginList.contains(bacHttpBean.getMethodName())) {// 非登录接口
                            // 是否传入当前登录的手机号
                            Map<String, Object> map = bacHttpBean.getListMap().get(0);
                            if (map.get("login_phone") == null) {// 未传登录手机号
                                // 判断是否登录
                                String loginPhone = CommonParam.getInstance().getLoginPhone();
                                if (!TextUtils.isEmpty(loginPhone)) {// 登陆
                                    map.put("login_phone", loginPhone);
                                } else { // 未登陆
                                    throw new AuthenticateError();
                                }
                            }
                        }

                        try {
                            // 加解密
                            byte[] decode = Base64.decode(mPrivateKey, 2);
                            mAes = new AES(decode);
                            // 上传数据
                            String toJSONString = JSON.toJSONString(bacHttpBean);
                            // 上传数据打印
                            byte[] bytes = toJSONString.getBytes();
                            byte[] aes2Str = mAes.encrypt(bytes);
                            byte[] base2Str = Base64.encode(aes2Str, 2);
                            String fMd5 = KeyedDigestMD5_HEX.getKeyedDigest(new String(base2Str), new String(decode));
                            mSignJson = URLEncoder.encode(fMd5, "UTF-8");
                            mUploadJson = URLEncoder.encode(new String(base2Str), "UTF-8");
                        } catch (Exception e) {
                            Observable.error(e);
                        }

                        // 组拼数据
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("VER", CommonParam.getInstance().getVersionName());
                        hashMap.put("AES", "Y");
                        hashMap.put("ZIP", "N");
                        hashMap.put("JSON", mUploadJson);
                        hashMap.put("SIGN", mSignJson);

                        // 请求接口的数据 非GET_KEY
                        return GET_DATA(hashMap);
                    }
                })
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return retryWhenError(observable);
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends String>>() {
                    @Override
                    public Observable<? extends String> call(Throwable throwable) {
                        if (throwable instanceof AuthenticateError) {
                            // 登录错误,无证书,去登录页面
                            return Observable
                                    .just("")
                                    .map(new Func1<String, String>() {
                                        @Override
                                        public String call(String s) {
                                            if (clazz != null) {
                                                // 不存在 启动登录界面
                                                Application application = CommonParam.getInstance().getApplication();
                                                Intent intent = new Intent(application, clazz);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                application.startActivity(intent);
                                            }
                                            return null;
                                        }
                                    });
                        } else if (throwable instanceof CountOutOfBoundsError) {
                            return Observable.just(null);
                        } else {
                            // 切换线程显示对话
                            return showAlert(throwable, dialog);
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
    private Observable<String> showAlert(Throwable throwable, final Dialog dialog) {
        return Observable.just(throwable)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Throwable, String>() {
                    @Override
                    public String call(Throwable throwable) {
                        System.out.println("Thread:" + Thread.currentThread().getName());
                        String message = throwable.getMessage();
                        System.out.println("message:" + message);
                        if (!TextUtils.isEmpty(message)) {
                            System.out.println("message:" + message);
                            if (throwable instanceof ShowDialogError) {
                                if (dialog != null) {
                                    System.out.println("Thread:" + Thread.currentThread().getName());
                                    System.out.println("message:" + message);
                                    dialog.show();
                                }
                            } else if (throwable instanceof ShowToastError) {
                                Toast.makeText(CommonParam.getInstance().getApplication(), message, Toast.LENGTH_SHORT).show();
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
    private Observable<?> retryWhenError(final Observable<? extends Throwable> observable) {
        return observable
                .observeOn(RxScheduler.RxPoolScheduler())
                .flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {
                        // TODO: 2017/8/9 此处计数应该无意义，在多个线程中，会有问题
                        count++;
                        if (count > 7) {
                            throw new CountOutOfBoundsError();
                        }
                        // 指定的error 和 请求超时的 error 会重新请求,其他抛出
                        else if (throwable instanceof BacHttpError || throwable instanceof OnErrorNotImplementedException) {
                            try {
                                // 获取RSA
                                mRsaMap = RSA.generateKeyPair();

                                // 组拼JSON
                                BacHttpBean bean = new BacHttpBean()
                                        .setMethodName("GET_KEY")
                                        .put("PublicKey", mRsaMap.get("publicKey"))
                                        .put("DeviceName", MODEL)
                                        .put("SystemName", BRAND)
                                        .put("SystemVersion", RELEASE)
                                        .put("PhoneId",
                                                SERIAL.concat("##").concat(
                                                        Settings.Secure.getString(
                                                                CommonParam.getInstance().getApplication().getContentResolver(),
                                                                Settings.Secure.ANDROID_ID))
                                        );

                                // 3.AES加密 -> RSA公钥
                                AES aesLocal = new AES(CommonParam.getInstance().getSeed().getBytes());
                                byte[] rsaPublicAes = aesLocal.encrypt(JSON.toJSONString(bean).getBytes());
                                // 4.MD5 -> AES加密 -> RSA公钥
                                byte[] encode = Base64.encode(rsaPublicAes, 2);
                                String strSrc = new String(encode);
                                String aesRsaMd5 = KeyedDigestMD5_HEX.getKeyedDigest(strSrc, CommonParam.getInstance().getSeed());
                                // 5.urlCode
                                // URLEncoder  md5
                                mSignJson = URLEncoder.encode(aesRsaMd5, "UTF-8");
                                // URLEncoder  RSA公钥的AES加密
                                mUploadJson = URLEncoder.encode(strSrc, "UTF-8");
                            } catch (Exception e) {
                                // 不可能出的现异常
                                Observable.error(e);
                            }

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("VER", CommonParam.getInstance().getVersionName());
                            hashMap.put("AES", "F");
                            hashMap.put("ZIP", "N");
                            hashMap.put("ID", CommonParam.getInstance().getPid());
                            hashMap.put("SIGN", mSignJson);
                            hashMap.put("JSON", mUploadJson);

                            // 这些错误 都是需要重新加解密 获取秘钥
                            return GET_KEY(hashMap);
                        } else if (throwable instanceof AuthenticateError) {
                            // 判断当前是否有证书
                            String certificate = StringUtil.decode(CommonParam.getInstance().getApplication(), "certificate", CommonParam.getInstance().getS());
                            if (TextUtils.isEmpty(certificate)) {
                                // 没有证书
                                return Observable.error(new Exception());
                            } else {
                                // 有证书
                                return LOGIN(certificate);
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
    public Observable<String> LOGIN(String certificate) {

        return Observable.just(certificate)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        // 获取登录手机号
                        final String loginPhone = StringUtil.decode(CommonParam.getInstance().getApplication(), "bac_l", CommonParam.getInstance().getS());

                        return HttpHelperLib2
                                .getInstance()
                                .net(new BacHttpBean()
                                        .setMethodName("LOGIN")
                                        .put("login_phone", loginPhone)
                                        .put("certificate", s)
                                        .put("phone_id", SERIAL.concat("##").concat(
                                                Settings.Secure.getString(CommonParam.getInstance().getApplication().getContentResolver(),
                                                        Settings.Secure.ANDROID_ID))), null, null
                                )
                                .doOnNext(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        count = 0;
                                        // 设置登录手机号
                                        CommonParam.getInstance().setLoginPhone(loginPhone);

                                    }
                                });
                    }
                });

    }

    /**
     * 交换RSA
     *
     * @param hashMap
     * @return
     */
    private Observable<?> GET_KEY(HashMap<String, String> hashMap) {

        return Observable.just(hashMap)
                .observeOn(RxScheduler.RxSerialScheduler())
                .flatMap(new Func1<HashMap<String, String>, Observable<HashMap<String, Object>>>() {
                    @Override
                    public Observable<HashMap<String, Object>> call(HashMap<String, String> hashMap) {
                        return CommonParam
                                .getInstance()
                                .getBacApi()
                                .get(hashMap);
                    }
                })
                .map(new Func1<HashMap<String,Object>, HashMap<String,Object>>() {
                    @Override
                    public HashMap<String,Object> call(HashMap<String, Object> map) {
                        mSession = valueOf(map.get("ID"));
                        mToken = valueOf(map.get("TOKEN"));
                        return map;
                    }
                })
                .observeOn(RxScheduler.RxPoolScheduler())
                .doOnNext(new Action1<HashMap<String, Object>>() {
                    @Override
                    public void call(HashMap<String, Object> map) {
                        count = 0;

                        // GET_KEY 的错误
                        Object error = map.get("ERROR");
                        if (error != null && errorList.contains(error)) {
                            Observable.error(new Exception(valueOf(map.get("MSG"))));
                        }

                        // 交换RSA后的解密
                        try {
                            byte[] decode = Base64.decode(valueOf(map.get("JSON")).getBytes(), 2);
                            AES aes = new AES(CommonParam.getInstance().getSeed().getBytes());
                            String json = new String(aes.decrypt(decode));
                            BacHttpBean httpBean = JSON.parseObject(json, BacHttpBean.class);
                            // token
                            // mToken = httpBean.getToken();
                            // rsa
                            mPrivateKey = RSA.decrypt(valueOf(httpBean.getListMap().get(0).get("KEY")), mRsaMap.get("privateKey"));
                            System.out.println("-------mPrivateKey---:" + mPrivateKey);
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
        // 回刷新
        hashMap.put("TOKEN", mToken);
        hashMap.put("ID", mSession);
        return Observable
                .just(hashMap)
                .observeOn(RxScheduler.RxSerialScheduler())
                .flatMap(new Func1<HashMap<String, String>, Observable<HashMap<String, Object>>>() {
                    @Override
                    public Observable<HashMap<String, Object>> call(HashMap<String, String> stringStringHashMap) {
                        return CommonParam
                                .getInstance()
                                .getBacApi()
                                .get(hashMap);
                    }
                })
                .map(new Func1<HashMap<String, Object>, HashMap<String, Object>>() {
                    @Override
                    public HashMap<String, Object> call(HashMap<String, Object> hashMap) {
                        //mSession = valueOf(map.get("ID"));
                        mToken = valueOf(hashMap.get("TOKEN"));
                        return hashMap;
                    }
                })
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<HashMap<String, Object>, String>() {
                    @Override
                    public String call(HashMap<String, Object> map) {
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
                                        getDefaultSharedPreferences(CommonParam.getInstance().getApplication())
                                        .edit().remove("bac_l").remove("certificate").commit();

                                throw new AuthenticateError();
                            } else if (err == -2) {
                                throw new ShowDialogError(map.get("MSG") + "");
                            } else if (errorCodeToList.contains(err)) {
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
