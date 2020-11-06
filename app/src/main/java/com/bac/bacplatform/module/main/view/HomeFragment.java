package com.bac.bacplatform.module.main.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.activity.HomeModel;
import com.bac.bacplatform.activity.expressorder.OrderRecordActivity;
import com.bac.bacplatform.activity.homemsg.HomeMsgActivity;
import com.bac.bacplatform.adapter.HomeFragmentJdCardListViewAdapter;
import com.bac.bacplatform.bean.MsgBean;
import com.bac.bacplatform.conf.SingleMode;
import com.bac.bacplatform.extended.base.components.AutomaticBaseFragment;
import com.bac.bacplatform.http.GrpcTask;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.login.LoginActivity;
import com.bac.bacplatform.module.login.view.LoginFragment2;
import com.bac.bacplatform.module.main.adapter.NetworkImageHolderView;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity3;
import com.bac.bacplatform.old.module.insurance.domain.AdsBean;
import com.bac.bacplatform.old.module.more.HomeMoreActivity;
import com.bac.bacplatform.old.module.phone.ActivityPhone1;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.weex_activities.WeexOilRechargeActivity;
import com.bac.bihupapa.bean.Method;
import com.bac.bihupapa.grpc.TaskPostExecute;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.life.automatic.enums.FragmentEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.bac.rxbaclib.rx.rxbus.RxBus;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bumptech.glide.Glide;
import com.wjz.weexlib.weex.activity.WeexActivity2;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import static com.alibaba.fastjson.JSON.parseObject;
import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.module.main.MainActivity.QUERY_VOUCHER;
import static com.bac.bacplatform.utils.ui.UIUtils.startActivityInAnim;
import static com.bac.commonlib.utils.Util.callPhoneUs;
import static com.taobao.weex.ui.ExternalLoaderComponentHolder.TAG;

/**
 * Created by Wjz on 2017/5/22.
 */

public class HomeFragment extends AutomaticBaseFragment {
    public static final String WX_SHARE = "wx_share";
    public ImageView insurance_buy; // 苏果卡
    public ImageView sgcard; // etc
    public ImageView jdcard;
    public ImageView phone;
    public ImageView flow;
    private TextView text_oil2;
    private SharedPreferences.Editor editor;
    private int i = 0;

    private TextView txtHornMsg;
    private View btMsg;// 右上角msg
    private View msgRedPoint; // 右上角msg的红点
    private RecyclerView msgListView; // 消息列表
    public HomeFragmentJdCardListViewAdapter jdCardListAdapter; // 消息列表adapter
    private HomeModel homeModel = new HomeModel(this);
    public RecyclerView funcGrid; // 功能区

    private void findTxtViewById(View rootView) {
        txtHornMsg = rootView.findViewById(R.id.txt_new_msg);
        msgListView = rootView.findViewById(R.id.msg_list_view);
        btMsg = rootView.findViewById(R.id.img_msg);
        msgRedPoint = rootView.findViewById(R.id.msg_red_point);
        funcGrid = rootView.findViewById(R.id.func_grid);
//        initIcon(); // 初始化图标
        homeModel.initFuncData(true);
        initRecyclerView(); // 初始化消息列表
        handleViewListener(rootView); // 设置点击监听
    }



    private void initIcon() {
        Context context = getContext();
        if(context == null){
            return;
        }
        int iconPadding = 10;
        txtHornMsg.setCompoundDrawables(UIUtils.getDrawable(context,R.mipmap.ic_horn,20),null,null,null);
        txtHornMsg.setCompoundDrawablePadding(iconPadding);
    }

    // 初始化消息列表（又改为了京东卡列表）
    private void initRecyclerView() {
        GridLayoutManager llm = new GridLayoutManager(activity,2);
        llm.setOrientation(RecyclerView.VERTICAL);
        msgListView.setLayoutManager(llm);
        jdCardListAdapter = new HomeFragmentJdCardListViewAdapter(homeModel.getMsgListData(),activity);
        msgListView.setAdapter(jdCardListAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                homeModel.getJdCardList();
            }
        }, 2000);
    }

    private void handleViewListener(View view) {
        btMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityInAnim(activity,new Intent(activity, HomeMsgActivity.class));
            }
        });

        view.findViewById(R.id.txt_more_jd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jingDongKa();
            }
        });

        view.findViewById(R.id.txt_more_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btMsg.performClick();
            }
        });
    }

    // 常见问题
    private void commonQuestion() {
        HttpHelper.getInstance().activityAutoLifeAndLoading(activity,
                new BacHttpBean()
                        .setMethodName("QUERY_START_PARAM")
                        .put("login_phone", BacApplication.getLoginPhone())
                        .put("param_type", "FAQ"))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, String>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<List<Map<String, String>>>() {
                            @Override
                            public void call(List<Map<String, String>> maps) {
                                startActivityInAnim(activity,
                                        new Intent(activity, WebAdvActivity.class)
                                                .putExtra("title", "常见问题")
                                                .putExtra("ads_url", maps.get(0).get("param_value")));
                            }
                        }
                );
    }


    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    private List<String> list = new ArrayList<>();

    private Observable<List<Map<String, Object>>> register;
    private ConvenientBanner mBanner;
    private ArrayList<String> adsStrList = new ArrayList<>();
    private List<AdsBean> mAdsList;
    private Handler mHandler = new Handler();

    private List<Map<String, Object>> icons = null;

    private static HomeFragment homeFragment;
    public static HomeFragment getInstance(){
        return homeFragment;
    }

    private static String insuranceurl;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    //    xmy：退出登录后恢复所有的首页图标
    public void resetAllIcon(){
        isBlackList = false;
        txtHornMsg.setText("暂无消息");
        txtHornMsg.setOnClickListener(null);
        homeModel.initFuncData(true);
        jdCardListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String adsString = PreferenceManager.getDefaultSharedPreferences(activity).getString("adsString", getResources().getString(R.string.ads_string));
        mAdsList = JSON.parseArray(adsString, AdsBean.class);
        adsStrList.clear();
        if (mAdsList != null) {
            for (AdsBean adsBean : mAdsList) {
                adsStrList.add(adsBean.getImage_url());
            }
        }
        homeFragment = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshView();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main4, null);
        findTxtViewById(view); // 新版view

        //判断是否在黑白名单
        SharedPreferences is_hasMakeCard = getContext().getSharedPreferences("is_hasMakeCard", Context.MODE_PRIVATE);
        editor = is_hasMakeCard.edit();//获取编辑器
        editor.putBoolean("is_hasMakeCard", false);
        editor.apply();

        mBanner = view.findViewById(R.id.banner);
        mBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, adsStrList)
                .setPageIndicator(new int[]{R.mipmap.home_banner_next_dot, R.mipmap.home_banner_current_dot});


        mBanner.setOnItemClickListener(new com.bigkoo.convenientbanner.listener.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                System.out.println(position);
                String http_url = mAdsList.get(position).getHttp_url();
                if (!TextUtils.isEmpty(http_url)) {

                    // 跳转广告详情
                    Intent intentToAgree = new Intent(getActivity(),
                            WebAdvActivity.class);
                    intentToAgree.putExtra("title", "活动详情");
                    intentToAgree.putExtra("ads_url", http_url);
                    UIUtils.startActivityInAnim(activity, intentToAgree);
                }
            }
        });

        insurance_buy = view.findViewById(R.id.insurance_buy);

        sgcard = view.findViewById(R.id.mainpage_icon_sg_card);
        jdcard = view.findViewById(R.id.mainpage_icon_jd_card);

        phone = view.findViewById(R.id.mainpage_icon_phone);
        flow = view.findViewById(R.id.mainpage_icon_flow);
        TextView all = view.findViewById(R.id.mainpage_icon_all);
        text_oil2 = view.findViewById(R.id.text2_oil);

//查询券，若有可使用权，显示加油券条目
        Method method = new Method();
        method.setMethodName(QUERY_VOUCHER);
        method.put("login_phone", BacApplication.getLoginPhone());
        method.put("status", new int[]{0, 1});
        method.put("voucher_type", new int[]{0, 1, 2, 3});
        new GrpcTask(activity, method, null, new SearchTask()).execute();

        HttpHelper.getInstance().bacNet(
                new BacHttpBean()
                        .setMethodName("BASEXML.GET_CARD_AND_NOT_BLACK")
                        .put("customers_id", LoginFragment2.getCustomers_id())
                        .put("login_phone", BacApplication.getLoginPhone())
        )
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<List<Map<String, Object>>>() {
                            @Override
                            public void call(List<Map<String, Object>> mapList) {
                                String s = mapList.get(0).get("is_enable") + "";
                                System.out.println("////////   " + s);
                                if (s.equals("0")) {
                                    SingleMode.getInstance().setIs_hasmakecard(false);

                                } else {
                                    SingleMode.getInstance().setIs_hasmakecard(true);

                                }

                            }
                        });

        final Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                i--;
                if (i <= 0) {
                    i = 0;
                } else {
                    mHandler.postDelayed(this, 1000);
                }
            }
        };
        // img_1_over
        insurance_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 判断是否有证书
                if (i == 0) {
                    if (!PreferenceManager.getDefaultSharedPreferences(activity).contains("certificate")) {
                        UIUtils.startActivityInAnim(activity, new Intent(activity, LoginActivity.class));
                        return;
                    }
                    //黑名单
                    if(isBlackList)
                    {
                        if(errorMsgStr==null)
                            errorMsgStr = "暂时无法操作！";
                        if(getContext()==null)return;
                        final Dialog dialog = new Dialog(activity);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_blacklist_layout);
                        if(dialog.getWindow()!=null)//主要解决dialog圆角不显示问题
                            dialog.getWindow().setBackgroundDrawable(null);
                        dialog.show();
                        TextView msg = dialog.findViewById(R.id.txt_msg_id);
                        TextView button = dialog.findViewById(R.id.txt_ok_id);
                        msg.setText(errorMsgStr);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
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
                                        return HttpHelper.getInstance().LOGIN((Activity) getContext(), s);
                                    }
                                })
                                .subscribeOn(RxScheduler.RxPoolScheduler())
                                .observeOn(RxScheduler.RxPoolScheduler())
                                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                                .observeOn(AndroidSchedulers.mainThread())
                                .compose(new RxDialog<List<Map<String, Object>>>(false).rxDialog(activity))
                                .subscribe(new Action1<List<Map<String, Object>>>() {
                                    @Override
                                    public void call(List<Map<String, Object>> list) {
                                        if (list != null && Boolean.parseBoolean(list.get(0).get("is_login") + "")) {
                                            HttpHelper.getInstance().fragmentAutoLifeAndLoading(HomeFragment.this,
                                                    new BacHttpBean()
                                                            .setMethodName("QUERY_START_PARAM")
                                                            .put("login_phone", BacApplication.getLoginPhone())
                                                            .put("param_type", "INSURE"))
                                                    .observeOn(RxScheduler.RxPoolScheduler())
                                                    .map(new JsonFunc1<String, List<Map<String, String>>>())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(
                                                            new Action1<List<Map<String, String>>>() {
                                                                @Override
                                                                public void call(List<Map<String, String>> maps) {
                                                                    insuranceurl = maps.get(_0).get("param_value") + "&phone=" + BacApplication.getLoginPhone();
                                                                    String http_url = null;
                                                                    if (icons != null && icons.size() > 0) {
                                                                        Map<String, Object> map = getMapByName(icons, "YKCZ_DE");
                                                                        http_url = (String) map.get("http_url");
                                                                    }
                                                                    if (http_url != null && !"".equals(http_url)) {
                                                                        //  wyy weex页面
                                                                        UIUtil.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                                                                                .setData(Uri.parse(http_url)));
                                                                    } else {
                                                                        UIUtil.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                                                                                .setData(Uri.parse("https://app5.bac365.com:10443/app.pay/photo/weex/Card/dist/SGCardPurshase.js")));
                                                                    }
                                                                }
                                                            }
                                                    );
                                        }
                                    }
                                });
                    }
                    HttpHelper.getInstance().fragmentAutoLifeAndLoading(HomeFragment.this,
                            new BacHttpBean()
                                    .setMethodName("QUERY_START_PARAM")
                                    .put("login_phone", BacApplication.getLoginPhone())
                                    .put("param_type", "INSURE"))
                            .observeOn(RxScheduler.RxPoolScheduler())
                            .map(new JsonFunc1<String, List<Map<String, String>>>())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    new Action1<List<Map<String, String>>>() {
                                        @Override
                                        public void call(List<Map<String, String>> maps) {
                                            insuranceurl = maps.get(_0).get("param_value") + "&phone=" + BacApplication.getLoginPhone();
                                        }
                                    }
                            );
                    String http_url = null;
                    if (icons != null && icons.size() > 0) {
                        Map<String, Object> map = getMapByName(icons, "YKCZ_DE");
                        http_url = (String) map.get("http_url");
                    }
                    if (http_url != null && !"".equals(http_url)) {
                        //  wyy weex页面
                        UIUtil.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                                .setData(Uri.parse(http_url)));

                    } else {
                        UIUtil.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                                .setData(Uri.parse("https://app5.bac365.com:10443/app.pay/photo/weex/Card/dist/SGCardPurshase.js")));
                    }
                    i = 2;
                } else {
                    Toast.makeText(activity, "跳转中，请您稍后！", Toast.LENGTH_SHORT).show();
                }
                mHandler.postDelayed(mRunnable, 1000);
            }
        });

        //img_4_over
        sgcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
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
                                        return HttpHelper.getInstance().LOGIN((Activity) getContext(), s);
                                    }
                                })
                                .subscribeOn(RxScheduler.RxPoolScheduler())
                                .observeOn(RxScheduler.RxPoolScheduler())
                                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                                .observeOn(AndroidSchedulers.mainThread())
                                .compose(new RxDialog<List<Map<String, Object>>>(false).rxDialog(activity))
                                .subscribe(new Action1<List<Map<String, Object>>>() {
                                    @Override
                                    public void call(List<Map<String, Object>> list) {
                                        if (list != null && Boolean.parseBoolean(list.get(0).get("is_login") + "")) {
                                            String http_url = null;
                                            if (icons != null && icons.size() > 0) {
                                                Map<String, Object> map = getMapByName(icons, "ETCPAY");
                                                http_url = (String) map.get("http_url");
                                            }
                                            if (http_url != null && !"".equals(http_url)) {
                                                UIUtil.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                                                        .setData(Uri.parse(http_url)));

                                            } else {
                                                UIUtil.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                                                        .setData(Uri.parse("https://app5.bac365.com:10443/app.pay/photo/weex/ETC/dist/HomePage.js")));
                                            }
                                        }
                                    }
                                });
                        return;
                    }

                    String http_url = null;
                    if (icons != null && icons.size() > 0) {
                        Map<String, Object> map = getMapByName(icons, "ETCPAY");
                        http_url = (String) map.get("http_url");
                    }
                    if (http_url != null && !"".equals(http_url)) {
                        UIUtil.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                                .setData(Uri.parse(http_url)));
                    } else {
                        UIUtil.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                                .setData(Uri.parse("https://app5.bac365.com:10443/app.pay/photo/weex/ETC/dist/HomePage.js")));
                    }
                    i = 2;
                } else {
                    Toast.makeText(activity, "跳转中，请您稍后！", Toast.LENGTH_SHORT).show();
                }
                mHandler.postDelayed(mRunnable, 1000);
            }
        });

        //img5_over
        jdcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
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
                                        return HttpHelper.getInstance().LOGIN((Activity) getContext(), s);
                                    }
                                })
                                .subscribeOn(RxScheduler.RxPoolScheduler())
                                .observeOn(RxScheduler.RxPoolScheduler())
                                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                                .observeOn(AndroidSchedulers.mainThread())
                                .compose(new RxDialog<List<Map<String, Object>>>(false).rxDialog(activity))
                                .subscribe(new Action1<List<Map<String, Object>>>() {
                                    @Override
                                    public void call(List<Map<String, Object>> list) {
                                        if (list != null && Boolean.parseBoolean(list.get(0).get("is_login") + "")) {
                                            String http_url = null;
                                            if (icons != null && icons.size() > 0) {
                                                Map<String, Object> map = getMapByName(icons, "ETC");
                                                http_url = (String) map.get("http_url");
                                            }
                                            if (http_url != null && !"".equals(http_url)) {
                                                UIUtils.startActivityInAnim(activity, new Intent(getActivity(), WebAdvActivity.class)
                                                        .putExtra("title", "ETC")
                                                        .putExtra("ads_url", http_url));

                                            } else {
                                                UIUtils.startActivityInAnim(activity, new Intent(getActivity(), WebAdvActivity3.class)
                                                        .putExtra("title", "ETC")
                                                        .putExtra("ads_url", "http://app.bac365.com/wxHosted/raw/ticket/jumpEntryInfo\n"));
                                            }
                                        }
                                    }
                                });
                        return;
                    }
                    String http_url = null;
                    if (icons != null && icons.size() > 0) {
                        Map<String, Object> map = getMapByName(icons, "ETC");
                        http_url = (String) map.get("http_url");
                    }
                    if (http_url != null && !"".equals(http_url)) {
                        UIUtils.startActivityInAnim(activity, new Intent(getActivity(), WebAdvActivity3.class)
                                .putExtra("title", "ETC")
                                .putExtra("ads_url", http_url));
                    } else {
                        UIUtils.startActivityInAnim(activity, new Intent(getActivity(), WebAdvActivity3.class)
                                .putExtra("title", "ETC")
                                .putExtra("ads_url", "http://wechat.bac365.com/wxHosted/raw/ticket/jumpEntryInfo"));

                    }
                    //UIUtils.startActivityInAnim(activity, new Intent(getActivity(), ActivityPhone1.class));
//                // 流量
//                //UIUtils.startActivityInAnim(activity, new Intent(getActivity(), FlowHomeActivity.class));
//                UIUtil.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
//                        .setData(Uri.parse("https://app5.bac365.com:10443/weex/sg/SGCard/giftCardList.js")));
                    i = 2;

                } else {
                    Toast.makeText(activity, "跳转中，请您稍后！", Toast.LENGTH_SHORT).show();
                }
                mHandler.postDelayed(mRunnable, 1000);
            }
        });

        //img_7
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
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
                                        return HttpHelper.getInstance().LOGIN((Activity) getContext(), s);
                                    }
                                })
                                .subscribeOn(RxScheduler.RxPoolScheduler())
                                .observeOn(RxScheduler.RxPoolScheduler())
                                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                                .observeOn(AndroidSchedulers.mainThread())
                                .compose(new RxDialog<List<Map<String, Object>>>(false).rxDialog(activity))
                                .subscribe(new Action1<List<Map<String, Object>>>() {
                                    @Override
                                    public void call(List<Map<String, Object>> list) {
                                        if (list != null && Boolean.parseBoolean(list.get(0).get("is_login") + "")) {
                                            String http_url = null;
                                            if (icons != null && icons.size() > 0) {
                                                Map<String, Object> map = getMapByName(icons, "C300");
                                                http_url = (String) map.get("http_url");
                                            }
                                            if (http_url != null && !"".equals(http_url)) {
                                                UIUtils.startActivityInAnim(activity, new Intent(getActivity(), WebAdvActivity3.class)
                                                        .putExtra("title", "爱车估值")
                                                        .putExtra("ads_url", http_url));
                                            } else {
                                                UIUtils.startActivityInAnim(activity, new Intent(getActivity(), WebAdvActivity3.class)
                                                        .putExtra("title", "爱车估值")
                                                        .putExtra("ads_url", "https://m.che300.com/partner/ltjy"));
                                            }
                                        }
                                    }
                                });
                        return;
                    }
                    String http_url = null;
                    if (icons != null && icons.size() > 0) {
                        Map<String, Object> map = getMapByName(icons, "C300");
                        http_url = (String) map.get("http_url");
                    }
                    if (http_url != null && !"".equals(http_url)) {
                        UIUtils.startActivityInAnim(activity, new Intent(getActivity(), WebAdvActivity3.class)
                                .putExtra("title", "爱车估值")
                                .putExtra("ads_url", http_url));

                    } else {

                        UIUtils.startActivityInAnim(activity, new Intent(getActivity(), WebAdvActivity3.class)
                                .putExtra("title", "爱车估值")
                                .putExtra("ads_url", "https://m.che300.com/partner/ltjy"));
                    }
                    //UIUtils.startActivityInAnim(activity, new Intent(getActivity(), OrderSplashActivity.class));
                    i = 2;

                } else {
                    Toast.makeText(activity, "跳转中，请您稍后！", Toast.LENGTH_SHORT).show();
                }
                mHandler.postDelayed(mRunnable, 1000);
            }
        });

        //img_8
        flow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
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
                                        return HttpHelper.getInstance().LOGIN((Activity) getContext(), s);
                                    }
                                })
                                .subscribeOn(RxScheduler.RxPoolScheduler())
                                .observeOn(RxScheduler.RxPoolScheduler())
                                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                                .observeOn(AndroidSchedulers.mainThread())
                                .compose(new RxDialog<List<Map<String, Object>>>(false).rxDialog(activity))
                                .subscribe(new Action1<List<Map<String, Object>>>() {
                                    @Override
                                    public void call(List<Map<String, Object>> list) {
                                        if (list != null && Boolean.parseBoolean(list.get(0).get("is_login") + "")) {

                                            String http_url = null;
                                            if (icons != null && icons.size() > 0) {
                                                Map<String, Object> map = getMapByName(icons, "BHPP");
                                                http_url = (String) map.get("http_url");
                                            }
                                            if (http_url != null && !"".equals(http_url)) {
                                                UIUtils.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                                                        .setData(Uri.parse(http_url))
                                                        .putExtra("loginphone", BacApplication.getLoginPhone())
                                                        .putExtra("customers_id", LoginFragment2.getCustomers_id()));
                                            } else {
                                                UIUtils.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                                                        .setData(Uri.parse("http://app5.bac365.com:10080/dist/index.js"))
                                                        .putExtra("loginphone", BacApplication.getLoginPhone())
                                                        .putExtra("customers_id", LoginFragment2.getCustomers_id()));
                                            }
                                        }
                                    }
                                });
                        return;
                    }
                    //兑换码
                    String http_url = null;
                    if (icons != null && icons.size() > 0) {
                        Map<String, Object> map = getMapByName(icons, "BHPP");
                        http_url = (String) map.get("http_url");
                    }
                    if (http_url != null && !"".equals(http_url)) {
                        UIUtils.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                                .setData(Uri.parse(http_url))
                                .putExtra("loginphone", BacApplication.getLoginPhone())
                                .putExtra("customers_id", LoginFragment2.getCustomers_id()));

                    } else {

                        UIUtils.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                                .setData(Uri.parse("http://app5.bac365.com:10080/dist/index.js"))
                                .putExtra("loginphone", BacApplication.getLoginPhone())
                                .putExtra("customers_id", LoginFragment2.getCustomers_id()));
                    }
                    // UIUtils.startActivityInAnim(activity, new Intent(getActivity(), PreferentialActivity.class));
                    i = 2;

                } else {
                    Toast.makeText(activity, "跳转中，请您稍后！", Toast.LENGTH_SHORT).show();
                }
                mHandler.postDelayed(mRunnable, 1000);
            }
        });

        //img_9
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    // 判断是否有证书
                    if (!PreferenceManager.getDefaultSharedPreferences(activity).contains("certificate")) {
                        UIUtils.startActivityInAnim(activity, new Intent(activity, HomeMoreActivity.class));
                        //UIUtils.startActivityInAnim(activity, new Intent(activity, jinYi.class));
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
                                        return HttpHelper.getInstance().LOGIN((Activity) getContext(), s);
                                    }
                                })
                                .subscribeOn(RxScheduler.RxPoolScheduler())
                                .observeOn(RxScheduler.RxPoolScheduler())
                                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                                .observeOn(AndroidSchedulers.mainThread())
                                .compose(new RxDialog<List<Map<String, Object>>>(false).rxDialog(activity))
                                .subscribe(new Action1<List<Map<String, Object>>>() {
                                    @Override
                                    public void call(List<Map<String, Object>> list) {
                                        if (list != null && Boolean.parseBoolean(list.get(0).get("is_login") + "")) {
                                            UIUtils.startActivityInAnim(activity, new Intent(getActivity(), HomeMoreActivity.class));
                                        }
                                    }
                                });
                        return;
                    }

                    UIUtils.startActivityInAnim(activity, new Intent(getActivity(), HomeMoreActivity.class));
                    i = 2;

                } else {
                    Toast.makeText(activity, "跳转中，请您稍后！", Toast.LENGTH_SHORT).show();
                }
                mHandler.postDelayed(mRunnable, 1000);
            }
        });

        return view;
    }

    private Map<String, Object> getMapByName(List<Map<String, Object>> list, String name) {
        for (Map<String, Object> item : list) {
            String icon_name = (String) item.get("icon_name");
            if (icon_name == null || "".equals(icon_name)) {
                return null;
            }
            if (icon_name.equals(name)) {
                return item;
            }
        }
        return null;
    }

    private boolean isBlackList = false;
    private String errorMsgStr = "";
    private void refreshView() {
        //首页图片加载
        try {
            Method method1 = new Method();
            method1.setMethodName("GET_HOME_ICON");
            method1.put("customers_id", LoginFragment2.getCustomers_id());
            method1.put("login_phone", BacApplication.getLoginPhone());
            HttpHelper.getInstance().bacNet(
                    new BacHttpBean()
                            .setMethodName("GET_HOME_ICON")
                            .put("customers_id", LoginFragment2.getCustomers_id())
                            .put("login_phone", BacApplication.getLoginPhone())
            )
                    .observeOn(RxScheduler.RxPoolScheduler())
                    .map(new JsonFunc1<String, List<Map<String, Object>>>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            //断点
                            new Action1<List<Map<String, Object>>>() {
                                @Override
                                public void call(List<Map<String, Object>> mapList) {
                                    //说根据error_msg判断是否是黑名单
                                    boolean isfind = false;
                                    for(Map<String,Object> map:mapList){
                                        Set<String> keys = map.keySet();
                                        if(keys.contains("error_msg")){//存在这个key，说明是黑名单了
                                            isBlackList  = true;
                                            errorMsgStr = (String) map.get("error_msg");
                                            break;//找到就结束循环
                                        }
                                    }
                                    homeModel.initFuncData(isBlackList);
                                    // 石化卡黑名单

                                    WindowManager wm = null;
                                    try {
                                        wm = (WindowManager) getContext()
                                                .getSystemService(Context.WINDOW_SERVICE);
                                    } catch (NullPointerException e) {
                                        Toast.makeText(activity, "等待加载", Toast.LENGTH_SHORT).show();
                                    }
//                                    width = wm.getDefaultDisplay().getWidth()*4/5;
//                                    height = wm.getDefaultDisplay().getHeight()*4/5;
                                    try {
                                        Glide.with(activity).load(mapList.get(0).get("icon_url")).override(wm.getDefaultDisplay().getWidth() / 3, wm.getDefaultDisplay().getWidth() / 3).fitCenter().into(insurance_buy);
//                                     tagxxx   Glide.with(activity).load(mapList.get(1).get("icon_url")).override(wm.getDefaultDisplay().getWidth() / 3, wm.getDefaultDisplay().getWidth() / 3).fitCenter().into(iv_one_key_oil);
//                                     tagxxx   Glide.with(activity).load(mapList.get(2).get("icon_url")).override(wm.getDefaultDisplay().getWidth() / 3, wm.getDefaultDisplay().getWidth() / 3).fitCenter().into(iv_wash_car);
                                        Glide.with(activity).load(mapList.get(3).get("icon_url")).override(wm.getDefaultDisplay().getWidth() / 3, wm.getDefaultDisplay().getWidth() / 3).fitCenter().into(sgcard);
                                        Glide.with(activity).load(mapList.get(4).get("icon_url")).override(wm.getDefaultDisplay().getWidth() / 3, wm.getDefaultDisplay().getWidth() / 3).fitCenter().into(jdcard);
//                                        Glide.with(activity).load(mapList.get(5).get("icon_url")).override(wm.getDefaultDisplay().getWidth() / 3, wm.getDefaultDisplay().getWidth() / 3).fitCenter().into(exchange);
                                        Glide.with(activity).load(mapList.get(6).get("icon_url")).override(wm.getDefaultDisplay().getWidth() / 3, wm.getDefaultDisplay().getWidth() / 3).fitCenter().into(phone);
                                        Glide.with(activity).load(mapList.get(7).get("icon_url")).override(wm.getDefaultDisplay().getWidth() / 3, wm.getDefaultDisplay().getWidth() / 3).fitCenter().into(flow);
//                                        Glide.with(activity).load(mapList.get(8).get("icon_url")).override(wm.getDefaultDisplay().getWidth() / 3, wm.getDefaultDisplay().getWidth() / 3).fitCenter().into(all);
                                    } catch (IndexOutOfBoundsException e) {
                                        Log.d(TAG, "首页九张logo图为空");
                                    }
                                    icons = mapList;
                                    System.out.println("图片集" + JSON.toJSONString(mapList));

                                }
                            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "出错了", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        if (mBanner != null) {
            mBanner.startTurning(7000);
        }
        homeModel.handleMsgListData();
//        homeModel.getJdCardList();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mBanner != null) {
            //停止翻页
            mBanner.stopTurning();
        }
    }


    public void registRxbus() {

        register = RxBus.get().register(QUERY_VOUCHER);
        register.subscribe(new Action1<List<Map<String, Object>>>() {
            @Override
            public void call(List<Map<String, Object>> mapList) {


                int size = mapList.size();
                Float me = 0f;
                for (int i = 0; i < size; i++) {
                    Float m = Float.valueOf(mapList.get(i).get("voucher_money") + "");
                    me = m + me;
                    System.out.println("券金额" + me.toString());

                    if (me > 0) {
                        text_oil2.setText(me.toString());
//                        rl_oil.setVisibility(View.VISIBLE);
                    } else {
//                        rl_oil.setVisibility(View.GONE);
                    }
                }
                /*
                // 所有的券数据
                if (mapList != null && mapList.size() > 0) {
                    BigDecimal insuranceMoneyTotal = new BigDecimal(0.00);
                    BigDecimal otherMoneyTotal = new BigDecimal(0.00);
                    for (Map<String, Object> map : mapList) {
                        if ((int) map.get("voucher_type") == 3) {
                            insuranceMoneyTotal = insuranceMoneyTotal.add((BigDecimal) map.get("voucher_money"));
                        } else {
                            otherMoneyTotal = otherMoneyTotal.add((BigDecimal) map.get("voucher_money"));
                        }
                    }

//                    List<HomeContentIcon> data = mHomeFragmentAdapter.getData();
//                    data.get(0).setMoney(otherMoneyTotal.toString());
//                    data.get(1).setMoney(insuranceMoneyTotal.toString());
//                    mHomeFragmentAdapter.notifyDataSetChanged();


                }
                */
//                if (canDo) {
//                    getIsBiHuPaPa();
//                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(QUERY_VOUCHER, register);
        RxBus.get().unregister(HomeFragment.WX_SHARE, null);
        homeFragment = null;
    }


    /**
     * 网络访问获取用户资料判断是电信还是移动
     */
    private void goPayPage() {
        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_PRODUCT")
                .put("login_phone", BacApplication.getLoginPhone().trim())
                .put("charge_mobile", BacApplication.getLoginPhone().trim()))
                .compose(this.<String>bindUntilEvent(FragmentEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(getActivity()))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<Map<String, Object>>>() {
                    @Override
                    public List<Map<String, Object>> call(String s) {
                        return parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        Map<String, Object> map = maps.get(0);
                        if (map != null) {
                            HttpHelper.getInstance()
                                    .bacNetWithContext(activity,
                                            new BacHttpBean()
                                                    .setActionType(_0)
                                                    .setMethodName("JUMP_YD_PAGE")
                                                    .put("charge_mobile", BacApplication.getLoginPhone().trim())
                                                    .put("login_phone", BacApplication.getLoginPhone().trim())

                                    ).compose(activity.<String>bindUntilEvent(ActivityEvent.DESTROY))
                                    .compose(new RxDialog<String>().rxDialog(activity))
                                    .observeOn(RxScheduler.RxPoolScheduler())
                                    .map(new Func1<String, List<Map<String, Object>>>() {
                                        @Override
                                        public List<Map<String, Object>> call(String s) {
                                            return parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                                            }.getType());
                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<List<Map<String, Object>>>() {
                                        @Override
                                        public void call(List<Map<String, Object>> maps) {
                                            Map<String, Object> stringObjectMap = maps.get(_0);//0
                                            String mUrl = stringObjectMap.get("jump_url").toString();
                                            UIUtils.startActivityInAnim(activity, new Intent(getActivity(), WebAdvActivity3.class)
                                                    .putExtra("title", "话费购")
                                                    .putExtra("ads_url", mUrl));
                                        }
                                    });
                        }
                    }
                });
    }

    private class SearchTask implements TaskPostExecute {
        @Override
        public void onPostExecute(Method result) {
            List<Map<String, Object>> listmap = result.getListMap();
            int size = listmap.size();
            Float me = 0f;
            for (int i = 0; i < size; i++) {
                Float m = Float.valueOf(listmap.get(i).get("voucher_money") + "");
                me = m + me;
                System.out.println("券金额" + me.toString());
                if (me > 0) {
                    text_oil2.setText(me.toString());
//                    rl_oil.setVisibility(View.VISIBLE);
                } else {
//                    rl_oil.setVisibility(View.GONE);
                }
            }
        }
    }

    public void setRightTopRedDotVisibility(int visibility) {
        msgRedPoint.setVisibility(visibility);
    }

    public void setHornMsg(final Map<String,Object> smg) {
        txtHornMsg.setText(smg.get("brief_desc").toString());
        txtHornMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = JSON.toJSONString(smg);
                MsgBean msg = JSONObject.parseObject(json,MsgBean.class);
                homeModel.markMsgRead(msg);
                homeModel.turnToWebView(msg);
            }
        });
    }

    public void setHornMsg(String msg) {
        txtHornMsg.setOnClickListener(null);
        txtHornMsg.setText("暂无消息");
    }

    // 油卡充值
    public void turnToOilRecharge() {
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
                            return HttpHelper.getInstance().LOGIN((Activity) getContext(), s);
                        }
                    })
                    .subscribeOn(RxScheduler.RxPoolScheduler())
                    .observeOn(RxScheduler.RxPoolScheduler())
                    .map(new JsonFunc1<String, List<Map<String, Object>>>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(new RxDialog<List<Map<String, Object>>>(false).rxDialog(activity))
                    .subscribe(new Action1<List<Map<String, Object>>>() {
                        @Override
                        public void call(List<Map<String, Object>> list) {
                            if (list != null && Boolean.parseBoolean(list.get(0).get("is_login") + "")) {
                                Calendar calendar = Calendar.getInstance();
                                //23-24,0-1  不启动加油页面
                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                //  System.out.println("hour  :" + hour);
                                boolean firstTime = 23 <= hour;//23-24
                                boolean secondTime = hour < 2.5;//0-1
                                if (firstTime || secondTime) {
                                    new AlertDialog.Builder(activity)
                                            .setTitle("暂停加油卡充值服务")
                                            .setMessage("23:00至02:30账务结算，期间暂停加油卡充值服务，请您谅解。")
                                            .show();
                                    return;
                                }


                                HttpHelper.getInstance()
                                        .activityAutoLifeAndLoading(activity
                                                , new BacHttpBean().setMethodName("CHECK_RECHARGE").put("login_phone", BacApplication.getLoginPhone())
                                                , null, null, null)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Action1<String>() {
                                            @Override
                                            public void call(String s) {
                                                String http_url = null;
                                                if (icons != null && icons.size() > 0) {
                                                    Map<String, Object> map = getMapByName(icons, "YKCZ_DE");
                                                    http_url = (String) map.get("http_url");
                                                }
                                                if (http_url != null && !"".equals(http_url)) {
                                                    //  wyy weex页面
                                                    UIUtil.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                                                            .setData(Uri.parse(http_url)));

                                                } else {
                                                    UIUtils.startActivityInAnim(activity, new Intent(getActivity(), WeexOilRechargeActivity.class));
                                                }

                                            }
                                        });
                            }
                        }
                    });
            return;
        }
        Calendar calendar = Calendar.getInstance();
        //23-24,0-1  不启动加油页面
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //  System.out.println("hour  :" + hour);
        boolean firstTime = 23 <= hour;//23-24
        boolean secondTime = hour < 2.5;//0-1
        if (firstTime || secondTime) {
            new AlertDialog.Builder(activity)
                    .setTitle("暂停加油卡充值服务")
                    .setMessage("23:00至02:30账务结算，期间暂停加油卡充值服务，请您谅解。")
                    .show();
            return;
        }
        HttpHelper.getInstance()
                .activityAutoLifeAndLoading(activity
                        , new BacHttpBean().setMethodName("CHECK_RECHARGE").put("login_phone", BacApplication.getLoginPhone())
                        , null, null, null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        String http_url = null;
                        if (icons != null && icons.size() > 0) {
                            Map<String, Object> map = getMapByName(icons, "YKCZ_DE");
                            http_url = (String) map.get("http_url");
                        }
                        if (http_url != null && !"".equals(http_url)) {
                            //  wyy weex页面
                            UIUtil.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                                    .setData(Uri.parse(http_url)));

                        } else {
                            UIUtils.startActivityInAnim(activity, new Intent(getActivity(), WeexOilRechargeActivity.class));
                        }

                    }
                });
    }

    // 石化卡
    public void shihuaka() {
        // 判断是否有证书
        if (!PreferenceManager.getDefaultSharedPreferences(activity).contains("certificate")) {
            UIUtils.startActivityInAnim(activity, new Intent(activity, LoginActivity.class));
            return;
        }

        // 有证书，没登录，拦截点击事件再去请求，请求完成在继续点击
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
                            return HttpHelper.getInstance().LOGIN((Activity) getContext(), s);
                        }
                    })
                    .subscribeOn(RxScheduler.RxPoolScheduler())
                    .observeOn(RxScheduler.RxPoolScheduler())
                    .map(new JsonFunc1<String, List<Map<String, Object>>>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(new RxDialog<List<Map<String, Object>>>(false).rxDialog(activity))
                    .subscribe(new Action1<List<Map<String, Object>>>() {
                        @Override
                        public void call(List<Map<String, Object>> list) {
                            if (list != null && Boolean.parseBoolean(list.get(0).get("is_login") + "")) {
                                HttpHelper.getInstance().bacNet(
                                        new BacHttpBean()
                                                .setMethodName("BASEXML.GET_CARD_PERMISSION")
                                                .put("customers_id", LoginFragment2.getCustomers_id())
                                                .put("login_phone", BacApplication.getLoginPhone())
                                )
                                        .observeOn(RxScheduler.RxPoolScheduler())
                                        .map(new JsonFunc1<String, List<Map<String, Object>>>())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                new Action1<List<Map<String, Object>>>() {
                                                    @Override
                                                    public void call(List<Map<String, Object>> mapList) {
                                                        String s = mapList.get(0).get("is_enable") + "";
                                                        String http_url = null;
                                                        if (icons != null && icons.size() > 0) {
                                                            Map<String, Object> map = getMapByName(icons, "DZK");
                                                            http_url = (String) map.get("http_url");
                                                        }

                                                        if (s.equals("0")) {
                                                            editor.putBoolean("is_hasMakeCard", false);
                                                            editor.commit();
                                                            new AlertDialog.Builder(getContext())
                                                                    .setTitle(getString(R.string.alert_title))
                                                                    .setMessage(mapList.get(0).get("error_msg") + "")
                                                                    .setNegativeButton(getString(R.string.alert_cancel), null)
                                                                    .show();
                                                        } else {
                                                            editor.putBoolean("is_hasMakeCard", true);
                                                            editor.commit();
                                                            if (http_url != null && !"".equals(http_url)) {
                                                                UIUtils.startActivityInAnim(activity, new Intent(getActivity(), WebAdvActivity3.class)
                                                                        .putExtra("title", "油卡申请")
                                                                        .putExtra("ads_url", http_url + "?phone=" + BacApplication.getLoginPhone()));

                                                            } else {
                                                                UIUtils.startActivityInAnim(activity, new Intent(getActivity(), WebAdvActivity3.class)
                                                                        .putExtra("title", "油卡申请")
                                                                        .putExtra("ads_url", "https://test.bac365.com:10433/life_number/servlet/sideCardIndex?phone=" + BacApplication.getLoginPhone()));
                                                            }
                                                        }
                                                    }
                                                });
                            }
                        }
                    });
            return;
        }
        HttpHelper.getInstance().bacNet(
                new BacHttpBean()
                        .setMethodName("BASEXML.GET_CARD_PERMISSION")
                        .put("customers_id", LoginFragment2.getCustomers_id())
                        .put("login_phone", BacApplication.getLoginPhone())
        )
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<List<Map<String, Object>>>() {
                            @Override
                            public void call(List<Map<String, Object>> mapList) {
                                String s = mapList.get(0).get("is_enable") + "";
                                String http_url = null;
                                if (icons != null && icons.size() > 0) {
                                    Map<String, Object> map = getMapByName(icons, "DZK");
                                    if(map!=null){
                                        Set<String> keys = map.keySet();
                                        if(keys.contains("http_url"))
                                            http_url = (String) map.get("http_url");
                                    }
                                }

                                if (s.equals("0")) {
                                    editor.putBoolean("is_hasMakeCard", false);
                                    editor.commit();
                                    new AlertDialog.Builder(getContext())
                                            .setTitle(getString(R.string.alert_title))
                                            .setMessage(mapList.get(0).get("error_msg") + "")
                                            .setNegativeButton(getString(R.string.alert_cancel), null)
                                            .show();
                                } else {
                                    editor.putBoolean("is_hasMakeCard", true);
                                    editor.commit();
                                    if (http_url != null && !"".equals(http_url)) {
                                        UIUtils.startActivityInAnim(activity, new Intent(getActivity(), WebAdvActivity3.class)
                                                .putExtra("title", "油卡申请")
                                                .putExtra("ads_url", http_url + "?phone=" + BacApplication.getLoginPhone()));
                                    } else {
                                        UIUtils.startActivityInAnim(activity, new Intent(getActivity(), WebAdvActivity3.class)
                                                .putExtra("title", "油卡申请")
                                                .putExtra("ads_url", "https://test.bac365.com:10433/life_number/servlet/sideCardIndex?phone=" + BacApplication.getLoginPhone()));
                                    }
                                }
                            }
                        });
    }

    // 话费狗友
    public void huafeigouyou() {
        // 判断是否有证书
        if (!PreferenceManager.getDefaultSharedPreferences(activity).contains("certificate")) {
            UIUtils.startActivityInAnim(activity, new Intent(activity, LoginActivity.class));
//            return;
        } else {
            new AlertDialog.Builder(activity)
                    .setTitle("温馨提示")
                    .setMessage("油费将充值到“揩油宝”账户，适用充油等APP上的所有消费。")
                    .setPositiveButton(getString(R.string.alert_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //判断是移动还是电信号码，跳往不同的页面
                            goPayPage();
                        }
                    }).show();
        }
/*        // 有证书，没登录，拦截点击事件再去请求，请求完成在继续点击事件
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
                            return HttpHelper.getInstance().LOGIN((Activity) getContext(), s);
                        }
                    })
                    .subscribeOn(RxScheduler.RxPoolScheduler())
                    .observeOn(RxScheduler.RxPoolScheduler())
                    .map(new JsonFunc1<String, List<Map<String, Object>>>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(new RxDialog<List<Map<String, Object>>>(false).rxDialog(activity))
                    .subscribe(new Action1<List<Map<String, Object>>>() {
                        @Override
                        public void call(List<Map<String, Object>> list) {
                            if (list != null && Boolean.parseBoolean(list.get(0).get("is_login") + "")) {
                                new AlertDialog.Builder(activity)
                                        .setTitle("温馨提示")
                                        .setMessage("油费将充值到“揩油宝”账户，适用充油等APP上的所有消费。")
                                        .setPositiveButton(getString(R.string.alert_confirm), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //判断是移动还是电信号码，跳往不同的页面
                                                goPayPage();
                                            }
                                        }).show();
                            }
                        }
                    });
            return;
        }
        new AlertDialog.Builder(activity)
                .setTitle("温馨提示")
                .setMessage("油费将充值到“揩油宝”账户，适用充油等APP上的所有消费。")
                .setPositiveButton(getString(R.string.alert_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //判断是移动还是电信号码，跳往不同的页面
                        goPayPage();
                    }
                }).show();*/
    }

    // 话费充值
    public void huafeichongzhi() {
        if(getIsLogin()) {
            UIUtils.startActivityInAnim(activity, new Intent(getContext(), ActivityPhone1.class));
        }
    }

    // 🐕东卡
    public void jingDongKa () {
        if(getIsLogin()) {
            UIUtil.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                    .setData(Uri.parse("https://app5.bac365.com:10443/app.pay/photo/weex/Card/dist/giftcardsPurchase.js")));
        }
    }

    // 快递查询
    public void kuaiDiChaXun() {
        if(getIsLogin()) {
            startActivityInAnim(activity,new Intent(activity, OrderRecordActivity.class));
        }
    }

    // 常见问题
    public void changJianWenTi() {
        HttpHelper.getInstance().activityAutoLifeAndLoading(activity,
                new BacHttpBean()
                        .setMethodName("QUERY_START_PARAM")
                        .put("login_phone", BacApplication.getLoginPhone())
                        .put("param_type", "FAQ"))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, String>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<List<Map<String, String>>>() {
                            @Override
                            public void call(List<Map<String, String>> maps) {
                                startActivityInAnim(activity,
                                        new Intent(activity, WebAdvActivity.class)
                                                .putExtra("title", "常见问题")
                                                .putExtra("ads_url", maps.get(0).get("param_value")));
                            }
                        }
                );
    }

    // 联系我们
    public void contactUs() {
        Calendar calendar = Calendar.getInstance();
        //7:00-22:00  不启动加油页面
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        boolean isTime = (hour > 7) && (hour < 22);
        if (!isTime) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("电话客服")
                    .setMessage("客服电话：400-110-6262 \n客服工作时间 8:00-22:00，请您谅解！")
                    .setNegativeButton("取消",null)
                    .show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("电话客服")
                .setMessage("客服电话：400-110-6262 \n客服工作时间：8:00-22:00，请您谅解！")
                .setPositiveButton("拨打",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callPhoneUs(activity);
                            }
                        })
                .setNegativeButton("取消", null)
                .show();
    }


    private boolean getIsLogin() {
        if (!PreferenceManager.getDefaultSharedPreferences(activity).contains("certificate")) {
            UIUtils.startActivityInAnim(activity, new Intent(activity, LoginActivity.class));
            return false;
        }
        return true;
    }
}
