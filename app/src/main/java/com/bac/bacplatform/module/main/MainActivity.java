package com.bac.bacplatform.module.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.collection.SparseArrayCompat;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.http.GrpcTask;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.center.model.UserCenterModelImpl;
import com.bac.bacplatform.module.center.presenter.UserCenterPresenterImpl;
import com.bac.bacplatform.module.center.view.UserCenterFragment;
import com.bac.bacplatform.module.kaiyoubao.KaiYouBaoActivity;
import com.bac.bacplatform.module.login.LoginActivity;
import com.bac.bacplatform.module.main.model.MainModelImpl;
import com.bac.bacplatform.module.main.presenter.MainPresenterImpl;
import com.bac.bacplatform.module.main.view.HomeFragment;
import com.bac.bacplatform.old.module.cards.ActivityCardsHome;
import com.bac.bacplatform.service.DownService;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.utils.tools.ToolBarUtil;
import com.bac.bacplatform.utils.tools.Util;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bihupapa.bean.Method;
import com.bac.bihupapa.grpc.TaskPostExecute;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.bac.rxbaclib.rx.rxbus.RxBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.conf.Constants.CommonProperty._3;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.module.main
 * 创建人：Wjz
 * 创建时间：2017/4/12
 * 类描述：
 * xiugai2
 */

public class MainActivity extends AutomaticBaseActivity implements UserCenterFragment.ChangeFragment {

    public static final String QUERY_VOUCHER = "QUERY_VOUCHER";
    public static final String QUERY_PAGE_INFO ="QUERY_PAGE_INFO";
    private SparseArrayCompat<Fragment> sparseArrayCompat;


    public ToolBarUtil toolBarUtil;

    private static boolean isShow1 = false;

    public static boolean isShow2() {
        return isShow2;
    }

    public static void setIsShow2(boolean isShow2) {
        MainActivity.isShow2 = isShow2;
    }

    private static boolean isShow2 = false;

    private boolean isShow;

    private boolean isFirstDo = true;
    //dialog控件
    private TextView time;
    private TextView version;
    private TextView size;
    private TextView refresh_text;
    private ImageView del;
    private ImageView sure;


    private static List<Map<String, Object>> listmap;
    private HomeFragment homeFragment;
    private UserCenterFragment userCenterFragment;


    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        Log.d("计时", "开始: " + curDate);
        setContentView(R.layout.main_activity_2);


        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        toolBarUtil = new ToolBarUtil();

        toolBarUtil.createToolBar(ll, new String[]{"首页", "揩油宝", "优惠", "用户中心"},
                new int[]{R.drawable.toolbar_home_icon_selector, R.drawable.toolbar_kaiyoubao_icon_selector, R.drawable.toolbar_card_icon_selector, R.drawable.toolbar_profile_icon_selector},
                ContextCompat.getColorStateList(this, R.color.toolbar_text_color_selector));

        toolBarUtil.setmOnToolBarClickListener(new ToolBarUtil.OnToolBarClickListener() {
            @Override
            public void onToolBarClick(final int position, ToolBarUtil.ViewHolder viewHolder) {

                if (position == 0) {
                    toolBarUtil.changeColor(position);
                    showFragment(position);
                } else {
                    // 判断是否有证书
                    if (!PreferenceManager.getDefaultSharedPreferences(activity).contains("certificate")) {
                        UIUtils.startActivityInAnim(activity, new Intent(activity, LoginActivity.class));
                        return;
                    }
                    // 有证书，没登录，拦截点击事件再去请求，请求完成在继续点击事件
                    // 点击事件保持最新
                    if (TextUtils.isEmpty(BacApplication.getLoginPhone())) {
                        Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                subscriber.onNext(StringUtil.decode(BacApplication.getBacApplication(), "certificate"));
                                subscriber.onCompleted();
                            }
                        })
                                .flatMap(new Func1<String, Observable<String>>() {
                                    @Override
                                    public Observable<String> call(String s) {
                                        return HttpHelper.getInstance().LOGIN(MainActivity.this, s);
//
//                                        return GrpcTask.LOGIN();
                                    }
                                })
                                .subscribeOn(RxScheduler.RxPoolScheduler())
                                .observeOn(RxScheduler.RxPoolScheduler())
                                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                                .observeOn(AndroidSchedulers.mainThread())
                                //.compose(new RxDialog<List<Map<String, Object>>>(false).rxDialog(activity))
                                .subscribe(new Action1<List<Map<String, Object>>>() {
                                    @Override
                                    public void call(List<Map<String, Object>> list) {
                                        if (list != null && Boolean.parseBoolean(list.get(0).get("is_login") + "")) {
                                            rvInterceptEvent(position);
                                        } else {
                                            return;
                                        }
                                    }
                                });
                        return;
                    }
                    rvInterceptEvent(position);
                }
            }
        });
        // 默认选中
        toolBarUtil.changeColor(0);
    }

    private void rvInterceptEvent(int position) {
        switch (position) {
            case 1:// 揩油宝
                UIUtils.startActivityInAnim(MainActivity.this, KaiYouBaoActivity.newIntent(MainActivity.this));
                break;
            case 2://优惠
                UIUtils.startActivityInAnim(MainActivity.this, new Intent(MainActivity.this, ActivityCardsHome.class));
                break;
            case 3:// 我的
                toolBarUtil.changeColor(position);
                showFragment(position);

//                HomeFragment homeFragment = HomeFragment.newInstance();
//                UserCenterFragment userCenterFragment = UserCenterFragment.newInstance();
//                FragmentManager supportFragmentManager = getSupportFragmentManager();
//                android.support.v4.app.FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
//                fragmentTransaction.add(userCenterFragment, "3");
//                fragmentTransaction.show(userCenterFragment);

                break;


        }
    }

    //   xmy:退出后恢复首页的图片
    public void resetHomePageIcons(){
        if(homeFragment==null)return;
        homeFragment.resetAllIcon();
    }

    @Override
    protected void initFragment() {

        // 首页
        homeFragment = HomeFragment.newInstance();
        new MainPresenterImpl(homeFragment, new MainModelImpl());
        // 注册监听
        homeFragment.registRxbus();

        // center
        userCenterFragment = UserCenterFragment.newInstance();
        new UserCenterPresenterImpl(userCenterFragment, new UserCenterModelImpl());

        // 加入集合管理
        sparseArrayCompat = new SparseArrayCompat<>(2);
        sparseArrayCompat.put(_0, homeFragment);
        sparseArrayCompat.put(_3, userCenterFragment);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl,homeFragment);
        fragmentTransaction.add(R.id.fl, userCenterFragment);
        fragmentTransaction.commitAllowingStateLoss();

        // 0 -> 首页
        showFragment(_0);
    }


    /**
     * 判断当前网络状态
     *
     * @param map
     */

    private void showRefresh(final Map<String, Object> map) {

//        if (Util.showRefresh(MainActivity.this)) {
//            //下载更新
//            doUpload(map, true);
//
//        } else {

        Method method = new Method();
        method.setMethodName("BASEXML.QUERY_DOWNLOAD");
        method.put("app_type", "Android");
        method.put("versioncode", Constants.APP.VERSION_CODE);
        Log.d(getString(R.string.set_util), JSON.toJSONString(method));
        new GrpcTask(MainActivity.this, method, null, new RefreshTask(map)).execute();

//        HttpHelper.getInstance().bacNet(
//                new BacHttpBean().setMethodName("BASEXML.QUERY_DOWNLOAD")
//                        .put("versioncode", Constants.APP.VERSION_CODE)
//                .put("app_type","Android")
//        )
//                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
//                .observeOn(RxScheduler.RxPoolScheduler())
//                .map(new JsonFunc1<String, List<Map<String, Object>>>())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<List<Map<String, Object>>>() {
//                    @Override
//                    public void call(List<Map<String, Object>> mapList) {
//                        final Dialog dialog = new Dialog(activity);
//                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialog.setContentView(R.layout.refresh_dialog);
//                        Window dialogWindow = dialog.getWindow();
//                        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//                        dialogWindow.setGravity(Gravity.CENTER);
//                        dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        lp.width = 800;
//                        lp.height = 1400;
//                        time = (TextView) dialog.findViewById(R.id.text_time);
//                        time.setText(mapList.get(0).get("create_time")+"");
//                        version = (TextView) dialog.findViewById(R.id.new_version);
//                        version.setText(mapList.get(0).get("versionname")+"");
//                        size = (TextView) dialog.findViewById(R.id.version_size);
//                        size.setText(mapList.get(0).get("version_size")+"");
//                        refresh_text = (TextView) dialog.findViewById(R.id.refresh_context);
//                        refresh_text.setMovementMethod(new ScrollingMovementMethod());
//                        refresh_text.setText(mapList.get(0).get("dsc")+"");
//                        dialog.setCancelable(false);
//                        dialog.show();
//                        del = (ImageView) dialog.findViewById(R.id.img_del);
//                        del.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.dismiss();
//                            }
//                        });
//                        sure = (ImageView) dialog.findViewById(R.id.img_sure);
//                        sure.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                showIsWifiDialog(map);
//                                dialog.dismiss();
//                            }
//                        });
//                    }
//                });


        //   点击更新，进入网络环境判断showisWiFidialog（）方法；
//        new AlertDialog.Builder(MainActivity.this)
//                .setTitle(getString(R.string.alert_title))
//                .setMessage(map.get("dsc") + "")
//                .setNegativeButton(getString(R.string.alert_cancel), null)
//                .setPositiveButton(getString(R.string.alert_confirm), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        showIsWifiDialog(map);
//
//                    }
//                }).show();
//          }

    }


    private void doUpload(Map<String, Object> map, boolean notify) {
        // 启动下载
        Intent intent = DownService.newIntent(MainActivity.this);
        intent.putExtra("url", map.get("url") + "");
        intent.putExtra("notify", notify);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(intent);
//        } else {
            startService(intent);
//        }
    }


    @Override
    protected void onStart() {
        super.onStart();
//        homeFragment.registRxbus();
//        //用户中心消息提示 备忘录


        // 自动登录 + 券请求
        Observable.just("")
                .compose(new Observable.Transformer<String, String>() {
                    @Override
                    public Observable<String> call(Observable<String> stringObservable) {


                        String loginPhone = BacApplication.getLoginPhone();
                        if (TextUtils.isEmpty(loginPhone)) {
                            // 登录手机号 空
                            final String certificate = StringUtil.decode(BacApplication.getBacApplication(), "certificate");


                            if (!TextUtils.isEmpty(certificate)) {

                                // 强制显示不可取消的进度条
                                // 证书非空 执行登录

                                return HttpHelper.getInstance().LOGIN(MainActivity.this, certificate);


//                                return new GrpcTask(MainActivity.this, method, null, new MainActivityLog()).execute();
                            } else {
                                return Observable.just("");
                            }

                        } else {
                            // 登录手机号 非空
                            return Observable.just(loginPhone);
                        }
                    }
                })
                // 过滤空字符串
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {

                        return !TextUtils.isEmpty(s);
                    }
                })
                // 查询券，显示券信息，跳转
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        System.out.println("=========s==========:" + s);
                        return HttpHelper.getInstance().bacNet(
                                new BacHttpBean()
                                        .setMethodName(QUERY_VOUCHER)
                                        .put("login_phone", BacApplication.getLoginPhone())
                                        .put("status", new int[]{0, 1})
                                        .put("voucher_type", new int[]{0, 1, 2, 3})
                        );
                    }
                })
                .compose(MainActivity.this.<String>bindToLifecycle())
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new RxDialog<List<Map<String, Object>>>(false).rxDialog(activity))
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {


                        int size = mapList.size();

                        /*
                        Float me = 0f;
                        for (int i = 0; i < size; i++) {
                            Float m = Float.valueOf(mapList.get(i).get("voucher_money") + "");
                            me = m + me;
                        }
                        if (!isShow) {
                            isShow = true;
                            //添加代金券为0时不提示   from  guke  8.21
                            if (me > 0) {
//                                new AlertDialog.Builder(activity)
//                                        .setTitle(R.string.alert_title)
//                                        .setMessage("您有价值" + me + "的代金券未使用")
//                                        .setNegativeButton(R.string.alert_cancel, null)
//                                        .setPositiveButton(R.string.alert_confirm, new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                UIUtils.startActivityInAnim(activity, new Intent(activity, ActivityCardsHome.class));
//                                            }
//                                        })
//                                        .show();
                            }


                        }


//                 查询手机号，弹框显示手机号信息，跳转
//                .flatMap(new Func1<String, Observable<String>>() {
//                    @Override
//                    public Observable<String> call(String s) {
//
//                        return HttpHelper.getInstance().bacNet(
//                                new BacHttpBean()
//                                        .setMethodName("DIALOG_INFO")
//                                        .put("login_phone", BacApplication.getLoginPhone())
//                        );
//                    }
//                })
//                .compose(MainActivity.this.<String>bindToLifecycle())
//                .observeOn(RxScheduler.RxPoolScheduler())
//                .map(new JsonFunc1<String, List<Map<String, Object>>>())
//                .observeOn(AndroidSchedulers.mainThread())
//                .compose(new RxDialog<List<Map<String, Object>>>(false).rxDialog(activity))
//                .subscribe(new Action1<List<Map<String, Object>>>() {
//                    @Override
//                    public void call(List<Map<String, Object>> mapList) {
//                        Map<String, Object> stringObjectMap = mapList.get(_0);//0
//
//                        //查询电话号注释结束----------

*/
                       // 券的集合，发送 给 之前注册的 bus
                        RxBus.get().post(QUERY_VOUCHER, mapList);

//                设置红点消息提示
                        ((ToolBarUtil.ViewHolder) toolBarUtil.getList().get(2).getTag()).setCount(size);
                    }
                });

        //首页弹框
        //  HttpHelper.getInstance().activityAutoLifeAndLoading(MainActivity.this,
//        HttpHelper.getInstance().bacNet(new BacHttpBean()
//                .setMethodName("DIALOG_INFO")
//                .put("login_phone", BacApplication.getLoginPhone())
//        )
//                .observeOn(RxScheduler.RxPoolScheduler())
//                .map(new JsonFunc1<String, List<Map<String, Object>>>())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<List<Map<String, Object>>>() {
//                               @Override
//                               public void call(List<Map<String, Object>> maps) {
//
//                                   System.out.println("---------打开壁虎趴趴后，回退，未进入首页弹框--------");// 打开壁虎趴趴后，回退，未进入
//
//                                   Map<String, Object> stringObjectMap = maps.get(_0);//0
//                                   String dialog = stringObjectMap.get("dialog_content") + "";
//                                   final String url = stringObjectMap.get("ok_btn_next") + "";
//                                   if (!isShow1) {
//                                       if (dialog != "" && dialog != null) {
//
//                                           new AlertDialog.Builder(activity)
//                                                   .setTitle(R.string.alert_title)
//                                                   .setMessage(dialog)
//                                                   .setNegativeButton(R.string.alert_cancel, null)
//                                                   .setPositiveButton(R.string.alert_confirm,
//                                                           new DialogInterface.OnClickListener() {
//                                                               @Override
//                                                               public void onClick(DialogInterface dialog, int which) {
//
//                                                                   HttpHelper.getInstance().
//                                                                           bacNet(
//                                                                                   //activityAutoLifeAndLoading(MainActivity.this,
//                                                                                   new BacHttpBean()
//                                                                                           .setMethodName(url)
//                                                                                           .put("login_phone", BacApplication.getLoginPhone())
//                                                                                           .put("charge_mobile", BacApplication.getLoginPhone())
//                                                                           )
//                                                                           .observeOn(RxScheduler.RxPoolScheduler())
//                                                                           .map(new JsonFunc1<String, List<Map<String, Object>>>())
//                                                                           .observeOn(AndroidSchedulers.mainThread())
//                                                                           .subscribe(new Action1<List<Map<String, Object>>>() {
//                                                                                          @Override
//                                                                                          public void call(List<Map<String, Object>> maps) {
//                                                                                              Map<String, Object> stringObjectMap = maps.get(_0);//0
//                                                                                              String url1 = stringObjectMap.get("jump_url") + "";
//                                                                                              UIUtils.startActivityInAnimAndFinishSelf(activity, new Intent(MainActivity.this, WebAdvActivity.class)
//                                                                                                      .putExtra("title", "话费购油")
//                                                                                                      .putExtra("ads_url", url1));
//                                                                                              //    UIUtils.startActivityInAnim(activity, new Intent(activity, ActivityCardsHome.class));
//                                                                                          }
//                                                                                      }
//                                                                           );
//
//
//                                                               }
//                                                           })
//                                                   .show();
//                                       }
//
//                                       isShow1 = true;
//                                   }
//
//                               }
//                           }
//                );
        if (isFirstDo) {
            // 验证当前版本
            BASEXML_QUERY_ANDROID_DOWNLOAD();
        }
    }

    public void showFragment(int i) {
        // 默认0
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if (i == 0) {
            fragmentTransaction
//                .replace(R.id.fl, sparseArrayCompat.get(i))
                    .show(sparseArrayCompat.get(_0))
                    .hide(sparseArrayCompat.get(_3))
                    .commitAllowingStateLoss();
        } else if (i == 3) {
            fragmentTransaction
                    .hide(sparseArrayCompat.get(_0))
                    .show(sparseArrayCompat.get(_3))
                    .commitAllowingStateLoss();
        }

        /*
        if (i == 99) {
            //返回首页时刷新数据
            // 首页
            if (homeFragment.isAdded()) {
                Log.d("显示状态homefragment", homeFragment.isHidden() + "");
                fragmentTransaction.remove(homeFragment);
            }
            if (userCenterFragment.isAdded()) {
                Log.d("显示状态usercenterfragment", userCenterFragment.isHidden() + "");
                fragmentTransaction.remove(userCenterFragment);
            }
            homeFragment = HomeFragment.newInstance();
            new MainPresenterImpl(homeFragment, new MainModelImpl());
            homeFragment.registRxbus();
            userCenterFragment = UserCenterFragment.newInstance();
            new UserCenterPresenterImpl(userCenterFragment, new UserCenterModelImpl());
            sparseArrayCompat.put(_0, homeFragment);
            sparseArrayCompat.put(_3, userCenterFragment);
            fragmentTransaction.add(R.id.fl, sparseArrayCompat.get(_0));
            fragmentTransaction.add(R.id.fl, sparseArrayCompat.get(_3));
//            this.homeFragment = homeFragment;
//            this.userCenterFragment = userCenterFragment;
////            homeFragment.onHiddenChanged(true);
////            userCenterFragment.onHiddenChanged(true);
            showFragment(_0);
            toolBarUtil.changeColor(_0);
            LoginFragment2.setTorefresh(false);
        }
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

//        if (homeFragment.isAdded()) {
//            fragmentTransaction.remove(homeFragment);
//        }
//        if (userCenterFragment.isAdded()) {
//            fragmentTransaction.remove(userCenterFragment);
//        }
//        if (homeFragment.isAdded()) {
//            homeFragment.getActivity().onBackPressed();
//        }
//        if (userCenterFragment.isAdded()) {
//            userCenterFragment.getActivity().onBackPressed();
//        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        // 解除 注册
        RxBus.get().unregister(QUERY_VOUCHER, null);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        // activity 退到后台，被系统强制杀掉后会调用该方法，会保存被杀掉之前的状态  -> onCreate() 结合使用
        if (getSupportFragmentManager().findFragmentById(R.id.fl) != null) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fl)).commit();
        }


        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * 为了 刷新 HomeFragment 里面的数据
     */
    @Override
    protected void onRestart() {
        super.onRestart();

        /*
        if (LoginFragment2.getTorefresh() == true) {
            showFragment(99);
        }


        if (Constants.is_open) {
            ((ToolBarUtil.ViewHolder) toolBarUtil.getList().get(_3).getTag()).setCount(0);
        }
        */
    }


    @Override
    public void onChange(int i) {
        toolBarUtil.changeColor(i);
        showFragment(i);

    }

    private void BASEXML_QUERY_ANDROID_DOWNLOAD() {
        Method method = new Method();
        method.setMethodName("BASEXML.QUERY_DOWNLOAD");
        method.put("versioncode", Constants.APP.VERSION_CODE);
        method.put("app_type", "Android");

//        method.setListMap(listmap);
        Log.d(getString(R.string.set_util), JSON.toJSONString(method));
        new GrpcTask(MainActivity.this, method, null, new MainActivityLog()).execute();
//        HttpHelper.getInstance().bacNet(
//                new BacHttpBean().setMethodName("BASEXML.QUERY_ANDROID_DOWNLOAD")
//                        .put("versioncode", Constants.APP.VERSION_CODE)
//        )
//                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
//                .observeOn(RxScheduler.RxPoolScheduler())
//                .map(new JsonFunc1<String, List<Map<String, Object>>>())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<List<Map<String, Object>>>() {
//                    @Override
//                    public void call(List<Map<String, Object>> mapList) {
//
////                        versionname = mapList.get(0).get("versionname")+"";
////                        starttime = mapList.get(0).get("creat_time")+"";
////                        versionsize = mapList.get(0).get("version_size")+"";
////                        versioncontext = mapList.get(0).get("")+"";
//                        System.out.println("更新消息" + mapList.toString());
//                        System.out.println("更新消息版本号" + Constants.APP.VERSION_CODE);
//                        isFirstDo = false;
//
//                        if (mapList.size() > 0) {
//                            final Map<String, Object> map = mapList.get(_0);
//                            if ((int) map.get("versioncode") > Constants.APP.VERSION_CODE) {
//                                showRefresh(map);
//                            }
//                        }
//                    }
//                });
    }

    private class RefreshTask implements TaskPostExecute {
        private Map<String, Object> map;

        public RefreshTask(Map<String, Object> map) {
            this.map = map;
        }

        @Override
        public void onPostExecute(Method result) {
            Log.d(getString(R.string.get_util), JSON.toJSONString(result));

            listmap = result.getListMap();
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.refresh_dialog);
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.CENTER);
            dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager wm = (WindowManager) MainActivity.this
                    .getSystemService(Context.WINDOW_SERVICE);
            lp.width = wm.getDefaultDisplay().getWidth() * 4 / 5;
            lp.height = wm.getDefaultDisplay().getHeight() * 4 / 5;
            try {
                String s = listmap.get(0).get("dsc") + "";
                String s1 = s.replaceAll("##","\n");
                time = (TextView) dialog.findViewById(R.id.text_time);
                time.setText(listmap.get(0).get("create_time") + "");
                version = (TextView) dialog.findViewById(R.id.new_version);
                version.setText(listmap.get(0).get("versionname") + "");
                size = (TextView) dialog.findViewById(R.id.version_size);
                size.setText(listmap.get(0).get("version_size") + "");
                refresh_text = (TextView) dialog.findViewById(R.id.refresh_context);
                refresh_text.setMovementMethod(new ScrollingMovementMethod());
                refresh_text.setText(s1);
            } catch (Exception e) {
                Log.d("日志", "onPostExecute: 更新弹框数据不全");
            }
            dialog.setCancelable(false);
            dialog.show();
            del = (ImageView) dialog.findViewById(R.id.img_del);
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            sure = (ImageView) dialog.findViewById(R.id.img_sure);
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showIsWifiDialog(map);
                    dialog.dismiss();
                }
            });
        }
    }


    private class MainActivityLog implements TaskPostExecute {
        public MainActivityLog() {

        }

        @Override
        public void onPostExecute(Method result) {
            Log.d(getString(R.string.get_util), JSON.toJSONString(result));

            isFirstDo = false;
            List<Map<String, Object>> mapList = result.getListMap();
            if (mapList.size() > 0) {
                final Map<String, Object> map = mapList.get(_0);
                if ((int) map.get("versioncode") > Constants.APP.VERSION_CODE) {
                    showRefresh(map);
                }
            }
        }
    }

    private void showIsWifiDialog(final Map<String, Object> map) {
        if (Util.isWifi(MainActivity.this)) {
            //下载更新


            doUpload(map, true);


        } else {
            new AlertDialog.Builder(this).setTitle("流量提醒")
                    .setMessage("您正在通过移动数据下载，这可能产生流量费用，是否继续？")
                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //下载更新
                            doUpload(map, false);
                        }
                    })
                    .setNegativeButton("取消", null).show();
        }

    }
}
