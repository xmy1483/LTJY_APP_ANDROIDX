package com.bac.bacplatform.module.splash;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.main.MainActivity;
import com.bac.bacplatform.old.module.insurance.domain.AdsBean;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.utils.tools.Util;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.ui.CircleProgressbar;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.bac.bacplatform.service.DownService.BAC_APK;
import static com.bac.bacplatform.utils.tools.Util.getDiskCacheDir;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.module.splash
 * 创建人：Wjz
 * 创建时间：2017/4/12
 * 类描述：
 * app的启动页
 * fragment的托管页面
 * 管理启动页的生命周期
 */

public class SplashActivity2 extends AutomaticRxAppCompatActivity {
//    @BindView(R.id.image)
    ImageView image;
    private AlertDialog alertDialog;
    private SharedPreferences preferences;
    private CircleProgressbar mCircleProgressbar;
    private boolean isExe = false;
    private boolean isClick = false;
    private boolean  shared;
    private MyCountDownTimer time;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_2_acitivity);
//        ButterKnife.bind(this);

        image = findViewById(R.id.image);

//倒计时3秒数字添加
        time = new MyCountDownTimer(3000, 1000);
        time.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!shared) {
                    mCircleProgressbar.stop();
                    Intent intent = new Intent(SplashActivity2.this, MainActivity.class);
                    startActivity(intent);
                    SplashActivity2.this.finish();
                }else{
                }
            }
        }, 3000);


        mCircleProgressbar = (CircleProgressbar) findViewById(R.id.tv_red_skip);
        mCircleProgressbar.setOutLineColor(Color.TRANSPARENT);
        mCircleProgressbar.setInCircleColor(Color.parseColor("#505559"));
        mCircleProgressbar.setProgressColor(Color.parseColor("#1BB079"));
        mCircleProgressbar.setProgressLineWidth(3);
        mCircleProgressbar.setTimeMillis(3000);
        mCircleProgressbar.reStart();
        //  mCircleProgressbar.setCountdownProgressListener(1, progressListener);
//后台下图片url
//        HttpHelper.getInstance()
//                .bacNet(
//                        new BacHttpBean()
//                                .setMethodName("QUERY_CAROUSEL_IMAGES")
//                                .put("version", Constants.APP.VERSION_NAME))
//                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
//                .observeOn(RxScheduler.RxPoolScheduler())
//                // .map(new JsonFunc1<String,List<Map<String,Object>>>())
//                .map(new Func1<String, List<Map<String, Object>>>() {
//                    @Override
//                    public List<Map<String, Object>> call(String s) {
//                        return JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
//                        }.getType());
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<List<Map<String, Object>>>() {
//                    @Override
//                    public void call(List<Map<String, Object>> maps) {
//                        Map<String, Object> stringObjectMap = maps.get(_0);//0
//
        String url = "https://app5.bac365.com:10443/app.pay/photo/images/lunchImage.png";
        Glide.with(SplashActivity2.this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(image);

//                    }
//                });
        // 点击跳转
        mCircleProgressbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = true;
                Intent intent = new Intent(SplashActivity2.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        // 删除apk
        HashMap<String, Object> diskCacheDir = getDiskCacheDir(this);
        Object path = diskCacheDir.get("path");
        if (path != null) {
            File file = new File(path.toString(), BAC_APK);
            if (file.exists()) {
                file.delete();
            }
        }


        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //判断手机系统的版本  即API大于10 就是3.0或以上版本
        alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.alert_title))
                .setMessage("打开“移动网络”来允许“骆驼加油”使用网络")
                .setNegativeButton(R.string.alert_cancel, null)
                .setPositiveButton(R.string.alert_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = null;
                        //判断手机系统的版本  即API大于10 就是3.0或以上版本
                        if (Build.VERSION.SDK_INT > 10) {
                            intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        } else {
                            intent = new Intent();
                            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                            intent.setComponent(component);
                            intent.setAction("android.intent.action.VIEW");
                        }
                        UIUtils.startActivityInAnim(SplashActivity2.this, intent);
                    }
                }).create();

        Log.d("版本号是", BacApplication.getVersionCode(this)+"");

    }



    private Handler handler = new Handler();

//    private CircleProgressbar.OnCountdownProgressListener progressListener = new CircleProgressbar.OnCountdownProgressListener() {
//        @Override
//        public void onProgress(int what, int progress) {
//            // 5s 结束跳转
//            if (what == 1 && progress == 100) {
//                Intent intent = new Intent(SplashActivity2.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }
//    };

    @Override
    protected void onStart() {
        super.onStart();
        //网络是否可用
        if (!Util.isNetworkAvailable(this)) {
            // 提示当前网络不用
            if (alertDialog != null && !alertDialog.isShowing()) {
                alertDialog.show();
            }
        } else if (!isExe) {
            // 允许使用网络
            getCommonData();
//            HttpHelper.getInstance().bacNet(
//                    new BacHttpBean()
//                            .setMethodName("GET_HOME_ICON")
//                            .put("customers_id", LoginFragment2.getCustomers_id())
//                            .put("login_phone", BacApplication.getLoginPhone())
//            )
//                    .observeOn(RxScheduler.RxPoolScheduler())
//                    .map(new JsonFunc1<String, List<Map<String, Object>>>())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            new Action1<List<Map<String, Object>>>() {
//                                @Override
//                                public void call(List<Map<String, Object>> mapList) {
//                                    String s = (String) mapList.get(0).get("icon_url");
//                                    System.out.println("splash页面数据"+s);
//
//                                    List<String> list = new ArrayList<String>();
//                                    for (int i = 0;i<9;i++){
//                                    list.add(mapList.get(i).get("icon_url")+"");
//                                    }
//                                    SharedPreferences preferences=getSharedPreferences("user", Context.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor=preferences.edit();
//                                    String first=list.get(0)+"";
//                                    String secongd=list.get(1)+"";
//                                    String third=list.get(2)+"";
//                                    String forth=list.get(3)+"";
//                                    String fifth=list.get(4)+"";
//                                    String sixth=list.get(5)+"";
//                                    String seventh=list.get(6)+"";
//                                    String eighth=list.get(7)+"";
//                                    String nighth=list.get(8)+"";
//
//                                    editor.putString("first", first);
//                                    editor.putString("secongd", secongd);
//                                    editor.putString("third", third);
//                                    editor.putString("forth", forth);
//                                    editor.putString("fifth", fifth);
//                                    editor.putString("sixth", sixth);
//                                    editor.putString("seventh", seventh);
//                                    editor.putString("eighth", eighth);
//                                    editor.putString("nighth", nighth);
//
//
//                                    editor.commit();
//                                }
//                            });
        }
    }

    private void getCommonData() {

        isExe = true;

        // QUERY_CAROUSEL_IMAGES
        Observable<Boolean> QUERY_CAROUSEL_IMAGES = HttpHelper.getInstance()
                .bacNet(new BacHttpBean()
                        .setMethodName("QUERY_CAROUSEL_IMAGES")
                        .put("version", Constants.APP.VERSION_NAME))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        List<AdsBean> adsList = new ArrayList<>();
                        List<AdsBean> insuranceList = new ArrayList<>();
                        List<Map<String, Object>> mapList = parseObject(s, new TypeReference<Object>() {
                        }.getType());

                        for (Map<String, Object> map : mapList) {
                            int image_type = (int) map.get("image_type");
                            if (image_type == 0) {
                                AdsBean adsBean = new AdsBean();
                                adsBean.setHttp_url(map.get("http_url").toString());
                                adsBean.setImage_url(map.get("image_url").toString());
                                adsList.add(adsBean);
                            } else if (image_type == 1) {
                                AdsBean adsBean = new AdsBean();
                                adsBean.setHttp_url(map.get("http_url").toString());
                                adsBean.setImage_url(map.get("image_url").toString());
                                insuranceList.add(adsBean);
                            }

                        }

                        String adsListStr = JSON.toJSON(adsList).toString();
                        String insuranceListStr = JSON.toJSON(insuranceList).toString();

                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("adsString", adsListStr);
                        edit.putString("insuranceString", insuranceListStr);
                        edit.commit();
                        return true;
                    }
                });


        Observable<Boolean> QUERY_PAGE_INFO = HttpHelper.getInstance()
                .bacNet(new BacHttpBean()
                        .setMethodName("QUERY_PAGE_INFO")
                        .put("version", "1"))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("QUERY_PAGE_INFO", s);
                        edit.commit();
                        System.out.println("string数据="+s);
                        return true;
                    }
                });



        Observable.combineLatest(QUERY_CAROUSEL_IMAGES, QUERY_PAGE_INFO, new Func2<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                return aBoolean && aBoolean2;
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        // 界面分发
                        if (!preferences.getBoolean("isFirstIn3", false)) {
                            shared=true;
                            UIUtils.startActivityInAnim(SplashActivity2.this, new Intent(SplashActivity2.this,
                                    LoadingActivity.class));
                            SplashActivity2.this.finish();
                            preferences.edit()
                                    .putBoolean("isFirstIn3", true)
                                    .commit();
                        } else {
                            shared=false;
//                            mCircleProgressbar.stop();
//                            UIUtils.startActivityInAnim(SplashActivity2.this, new Intent(SplashActivity2.this,
//                                    MainActivity.class));
//                            SplashActivity2.this.finish();

                        }
                    }
                });


    }

    private void autoLogin() {
        String certificate = StringUtil.decode(BacApplication.getBacApplication(), "certificate");
        if (!TextUtils.isEmpty(certificate)) {
            HttpHelper.getInstance()
                    .LOGIN(SplashActivity2.this, certificate)
                    .subscribe();
        }
    }


    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数
         *                          <p>
         *                          例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
         *                          <p>
         *                          例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
//            mCircleProgressbar.setText("正在跳转");
        }

        public void onTick(long millisUntilFinished) {
            mCircleProgressbar.setText("跳过\n" + millisUntilFinished / 1000 + "");
        }
    }

}
