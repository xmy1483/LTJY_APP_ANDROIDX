package com.bac.bacplatform.module.center.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.extended.base.components.AutomaticBaseFragment;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.bills.BillsMoreActivity;
import com.bac.bacplatform.module.bills.QueryExchangeDetailActivity;
import com.bac.bacplatform.module.bills.QueryKaiYouBaoActivity;
import com.bac.bacplatform.module.bills.QueryOilActivity;
import com.bac.bacplatform.module.bills.QueryPhoneActivity;
import com.bac.bacplatform.module.center.MypswActivity;
import com.bac.bacplatform.module.center.adapter.UserCenterAdapter;
import com.bac.bacplatform.module.center.adapter.UserCenterSectionBean;
import com.bac.bacplatform.module.center.adapter.UserCenterSectionInnerBean;
import com.bac.bacplatform.module.center.contract.UserCenterContract;
import com.bac.bacplatform.module.kaiyoubao.KaiYouBaoActivity;
import com.bac.bacplatform.module.main.MainActivity;
import com.bac.bacplatform.old.module.cards.ActivityCardInsurance;
import com.bac.bacplatform.old.module.cards.ActivityCardsPhone;
import com.bac.bacplatform.old.module.hybrid.AboutUsActivity;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity;
import com.bac.bacplatform.utils.logger.LogUtil;
import com.bac.bacplatform.utils.tools.Util;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.FragmentEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.conf.Constants.CommonProperty._1;
import static com.bac.bacplatform.conf.Constants.CommonProperty._2;
import static com.bac.bacplatform.conf.Constants.CommonProperty._4;
import static com.bac.bacplatform.utils.ui.UIUtils.startActivityInAnim;
import static com.taobao.weex.ui.ExternalLoaderComponentHolder.TAG;

/**
 * Created by Wjz on 2017/5/4.
 */

public class UserCenterFragment extends AutomaticBaseFragment implements UserCenterContract.View {

    private RecyclerView rv;
    private UserCenterAdapter userCenterAdapter;
    private UserCenterContract.Presenter presenter;
    private TextView tv_01;
    private TextView tv_02;
    private TextView tv_03;
    private TextView tv_00;

    public static UserCenterFragment newInstance() {
        return new UserCenterFragment();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        LogUtil.sf("UserCenterFragment","---------UserCenterFragment---------");

        View view = inflater.inflate(R.layout.user_center_fragment, container, false);

        TextView title = (TextView) view.findViewById(R.id.tv_center);
        title.setText(getString(R.string.center_title));

        RxView.clicks(view.findViewById(R.id.iv))
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        ((ChangeFragment) activity).onChange(_0);
                    }
                });

        RxView.clicks(view.findViewById(R.id.exit))
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // 返回首页
                        ((ChangeFragment) activity).onChange(_0);
                        BacApplication.setLoginPhone(null);
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
                        // 清空手机号
                        preferences.edit().remove("bac_l").commit();
                        preferences.edit().remove("certificate").commit();
//                还原图标
                        if(getContext()==null)return;
                        ((MainActivity)getContext()).resetHomePageIcons();

                    }
                });

        rv = (RecyclerView) view.findViewById(R.id.rv);

        initAdapter();
        return view;
    }



    public interface ChangeFragment {
        void onChange(int i);
    }

    private void initAdapter() {

        userCenterAdapter = new UserCenterAdapter(R.layout.user_center_item_context,
                R.layout.user_center_item_header, null);
        userCenterAdapter.setHeaderView(getHeaderView());
        //userCenterAdapter.setFooterView(getFooterView());
        rv.setAdapter(userCenterAdapter);
        rv.setLayoutManager(new StaggeredGridLayoutManager(_4, StaggeredGridLayoutManager.VERTICAL));

        userCenterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserCenterSectionBean userCenterSectionBean = (UserCenterSectionBean) adapter.getData().get(position);
                if (userCenterSectionBean.isHeader) {
                    //header

                    if (userCenterSectionBean.header.equals("常用账单")) {
                        UIUtils.startActivityInAnim(activity, new Intent(activity, BillsMoreActivity.class));
                    }

                } else {// 其他

//if (Constants.bo){}
                    switch (userCenterSectionBean.t.getId()) {
                        case R.mipmap.center_order_recharge:

                            UIUtils.startActivityInAnim(activity, new Intent(activity, QueryOilActivity.class));
                            break;
                        case R.mipmap.center_order_insurance2:
                            UIUtils.startActivityInAnim(activity, new Intent(activity, QueryPhoneActivity.class));
/*                            //车险订单
                            startActivityInAnim(activity,
                                    new Intent(activity, WebAdvActivity.class)
                                            .putExtra("title", "车险订单")
                                            .putExtra("ads_url", "http://wechat.bac365.com:8081/carRisk/car_risk/jumpCarRisk/jumpPay_details?phone=" + BacApplication.getLoginPhone() + "&type=not"));*/
                            // UIUtils.startActivityInAnim(activity, new Intent(activity, QueryInsuranceAccount.class));
                            break;
                        case R.mipmap.oil_card_order:
                            //
                            startActivityInAnim(activity,
                                    new Intent(activity, WebAdvActivity.class)
                                            .putExtra("title", "油卡记录")
                                            .putExtra("ads_url", "https://test.bac365.com:10433/life_number/servlet/billRecharge?phone=" + BacApplication.getLoginPhone() + "&type=not"));
                            // UIUtils.startActivityInAnim(activity, new Intent(activity, QueryInsuranceAccount.class));
                            break;
                        case R.mipmap.center_order_kaiyoubao:
                            UIUtils.startActivityInAnim(activity, new Intent(activity, QueryKaiYouBaoActivity.class));
                            break;
                        case R.mipmap.center_order_exchange:
                            UIUtils.startActivityInAnim(activity, new Intent(activity, QueryExchangeDetailActivity.class));
                            break;
                        case R.mipmap.center_tools_pay:
                            startActivityInAnim(activity, MypswActivity.newIntent(activity));
                            break;
                        case R.mipmap.center_tools_call:
                            Util.callUs(activity);
                            break;
                        case R.mipmap.center_tools_issue:

                            HttpHelper.getInstance().fragmentAutoLifeAndLoading(UserCenterFragment.this,
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
                                                                    .putExtra("ads_url", maps.get(_0).get("param_value")));
                                                }
                                            }
                                    );


                            break;
                        case R.mipmap.center_tools_suggest:
                            Util.callUs(activity);
                            break;
                        case R.mipmap.center_tools_search:
                            HttpHelper.getInstance().fragmentAutoLifeAndLoading(UserCenterFragment.this,
                                    new BacHttpBean()
                                            .setMethodName("QUERY_START_PARAM")
                                            .put("login_phone", BacApplication.getLoginPhone())
                                            .put("param_type", "COMMON_PARAM"))
                                    .observeOn(RxScheduler.RxPoolScheduler())
                                    .map(new JsonFunc1<String, List<Map<String, String>>>())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            new Action1<List<Map<String, String>>>() {
                                                @Override
                                                public void call(List<Map<String, String>> maps) {
                                                    Log.d(TAG, "查询结果: "+ JSON.toJSONString(maps));
                                                    if (maps.get(0).get("param_value")!=null&&maps.get(0).get("param_value")!=""){
                                                        startActivityInAnim(activity,
                                                                new Intent(activity, WebAdvActivity.class)
                                                                        .putExtra("title", "油卡余额")
                                                                        .putExtra("ads_url", maps.get(_0).get("param_value")+"?phone="+BacApplication.getLoginPhone()));
                                                        Log.d(TAG, "查询结果: "+ maps.get(_0).get("param_value")+"?phone="+BacApplication.getLoginPhone());

                                                    }else {
                                                        Toast.makeText(activity,"没有数据",Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            }
                                    );


                         /*   HttpHelper.getInstance().fragmentAutoLifeAndLoading(UserCenterFragment.this,
                                    new BacHttpBean()
                                            .setMethodName("open_pay.query_card_side")
                                            .put("customers_id", LoginFragment2.getCustomers_id())
                                            .put("login_phone", BacApplication.getLoginPhone())
                                            )
                                    .observeOn(RxScheduler.RxPoolScheduler())
                                    .map(new JsonFunc1<String, List<Map<String, String>>>())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            new Action1<List<Map<String, String>>>() {
                                                @Override
                                                public void call(List<Map<String, String>> maps) {
                                                    Log.d(TAG, "查询结果: "+ JSON.toJSONString(maps));
                                                    Log.d(TAG, "查询数据: "+maps.get(0).get("is_enable"));
                                                    if (maps.get(0).get("is_enable").toString().equals("1")){
                                                        //添加判断是否有url
                                                        HttpHelper.getInstance().fragmentAutoLifeAndLoading(UserCenterFragment.this,
                                                                new BacHttpBean()
                                                                        .setMethodName("QUERY_START_PARAM")
                                                                        .put("login_phone", BacApplication.getLoginPhone())
                                                                        .put("param_type", "COMMON_PARAM"))
                                                                .observeOn(RxScheduler.RxPoolScheduler())
                                                                .map(new JsonFunc1<String, List<Map<String, String>>>())
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .subscribe(
                                                                        new Action1<List<Map<String, String>>>() {
                                                                            @Override
                                                                            public void call(List<Map<String, String>> maps) {
                                                                            Log.d(TAG, "查询结果: "+ JSON.toJSONString(maps));
                                                                                if (maps.get(0).get("param_value")!=null&&maps.get(0).get("param_value")!=""){
                                                                                    startActivityInAnim(activity,
                                                                                            new Intent(activity, WebAdvActivity.class)
                                                                                                    .putExtra("title", "油卡余额")
                                                                                                    .putExtra("ads_url", maps.get(_0).get("param_value")+"?phone="+BacApplication.getLoginPhone()));
                                                                                    Log.d(TAG, "查询结果: "+ maps.get(_0).get("param_value")+"?phone="+BacApplication.getLoginPhone());

                                                                                }else {
                                                                                    Toast.makeText(activity,"没有数据",Toast.LENGTH_SHORT).show();
                                                                                }

                                                                            }
                                                                        }
                                                                );

                                                    }else if (maps.get(0).get("is_enable").toString().equals("0")){
                                                        Toast.makeText(getContext(),maps.get(0).get("error_msg")+"",Toast.LENGTH_SHORT).show();
                                                        //Toast.makeText(getContext(),"黑名单用户，查询失败",Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        Toast.makeText(getContext(),"账户验证失败",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    );*/


                            break;
                        case R.mipmap.center_tools_about:

                            //String url = "https://mp.weixin.qq.com/s?__biz=MzA3ODk0MzI3Nw==&mid=503203426&idx=2&sn=d20e70ec6e8d08ca01371dfc37cfa8ee&chksm=07b1e32030c66a366782a8af6962f0ba034a604b75b285ea7157adcda7f0dfd533abbe585b19&mpshare=1&scene=1&srcid=0526aqyEIs46cpU4tehQcdXV&key=a3cf833dc622c366ba7eb8d31407ad3b1dd6b406e10367190095ab3e62ea0470d129c4b031c801717f68d23d293e814a1f71eba3a39b3025f63c3d719c7262bd312529c77c024f9da0719c32ba8e4691&ascene=0&uin=NTIyODQ0MzQw&devicetype=iMac+MacBookPro9%2C1+OSX+OSX+10.12.3+build&ptlang=2052&ADUIN=583431183&ADSESSION=1495761948&ADTAG=CLIENT.QQ.5479_.0&ADPUBNO=26582";
                            startActivity(new Intent(activity, AboutUsActivity.class)
//                                            .putExtra("title", "关于我们")
//                                            .putExtra("ads_url", url)
                            );
                            break;
                        case R.mipmap.center_weixin:

                            break;

                    }
                }


            }
        });

        presenter.loadData();
    }

    private void loadWxLabel() {
        HttpHelper.getInstance().fragmentAutoLifeAndLoading(UserCenterFragment.this,
                new BacHttpBean()
                        .setMethodName("QUERY_START_PARAM")
                        .put("login_phone", BacApplication.getLoginPhone())
                        .put("param_type", "WX_ACTIVITY"))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, String>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<List<Map<String, String>>>() {
                            @Override
                            public void call(List<Map<String, String>> maps) {
                                if (maps != null && maps.size() > 0) {
                                    List<UserCenterSectionBean> data = userCenterAdapter.getData();
                                    UserCenterSectionBean userCenterSectionBean = data.get(data.size() - 1);
                                    UserCenterSectionInnerBean t = userCenterSectionBean.t;
                                    t.setLabel_1(maps.get(_0).get("param_value"));
                                    t.setLabel_2(maps.get(_1).get("param_value"));
                                    t.setIndex(_2);
                                    userCenterAdapter.notifyItemChanged(12);
                                }
                            }
                        }
                );
    }

    /**
     * 底部分享券
     *
     * @return
     */
    private View getFooterView() {
        View footer = activity.getLayoutInflater().inflate(R.layout.user_center_footer, null);
        TextView tv_01 = (TextView) footer.findViewById(R.id.tv_01);
        tv_01.setText("5");
        return footer;
    }

    private View getHeaderView() {
        View header = activity.getLayoutInflater().inflate(R.layout.user_center_header, null);
        tv_00 = (TextView) header.findViewById(R.id.tv_00);


        tv_01 = (TextView) header.findViewById(R.id.tv_01);
        tv_02 = (TextView) header.findViewById(R.id.tv_02);
        tv_03 = (TextView) header.findViewById(R.id.tv_03);

        //tv_00.setText(Html.fromHtml(String.format(getString(R.string.center_user_header), BacApplication.getLoginPhone())));

        Observable<Void> _01 = RxView.clicks(tv_01)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        //启动揩油宝
                        startActivityInAnim(activity, KaiYouBaoActivity.newIntent(activity));
                    }
                });

        Observable<Void> _02 = RxView.clicks(tv_02)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivityInAnim(activity, new Intent(activity, ActivityCardsPhone.class));
                    }
                });

        Observable<Void> _03 = RxView.clicks(tv_03)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivityInAnim(activity, new Intent(activity, ActivityCardInsurance.class));
                    }
                });

        Observable.merge(_01, _02, _03)
                .subscribe();

        return header;
    }


    @Override
    public void onStart() {
        super.onStart();

        Observable<Object> late = HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setMethodName("QUERY_VOUCHER")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("login_phone", BacApplication.getLoginPhone())
                .put("status", new int[]{0, 1})
                .put("voucher_type", new int[]{0, 1, 2, 3}))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, SparseArrayCompat<String>>() {
                    @Override
                    public SparseArrayCompat<String> call(String s) {

                        List<Map<String, Object>> list = JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                        }.getType());


                        BigDecimal otherMoneyTotal = new BigDecimal(0.00);
                        BigDecimal insuranceMoneyTotal = new BigDecimal(0.00);


                        for (Map<String, Object> map : list) {
                            if ((int) map.get("voucher_type") == 3) {
                                insuranceMoneyTotal = insuranceMoneyTotal.add((BigDecimal) map.get("voucher_money"));
                            } else {
                                otherMoneyTotal = otherMoneyTotal.add((BigDecimal) map.get("voucher_money"));
                            }
                        }

                        SparseArrayCompat<String> sac = new SparseArrayCompat<>();
                        sac.put(_0, otherMoneyTotal.toString());
                        sac.put(_1, insuranceMoneyTotal.toString());


                        return sac;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<SparseArrayCompat<String>, Object>() {
                    @Override
                    public Object call(SparseArrayCompat<String> stringSparseArrayCompat) {
                        tv_02.setText(stringSparseArrayCompat.get(_0) + "元\n加油券");
                        tv_03.setText(stringSparseArrayCompat.get(_1) + "元\n保险券");
                        tv_00.setText(BacApplication.getLoginPhone());

                        return "";
                    }
                });

        Observable kaiyoubi = HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setMethodName("CARD_XML.QUERY_ACCOUNT_BALANCE")
                .put("login_phone", BacApplication.getLoginPhone()))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<List<Map<String, Object>>, Object>() {
                    @Override
                    public Object call(List<Map<String, Object>> mapList) {
                        if (mapList.size() > 0) {
                            Map<String, Object> map = mapList.get(_0);

                            tv_01.setText(map.get("balance") + "元\n揩油宝");

                        }
                        return "";
                    }
                });


        Observable
                .merge(kaiyoubi, late)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .compose(new RxDialog<Object>().rxDialog(activity))
                .subscribe();


    }

    @Override
    public void setPresenter(UserCenterContract.Presenter presenter) {
        this.presenter = presenter;

    }


    @Override
    public void showData(List<UserCenterSectionBean> beanList) {
        userCenterAdapter.getData().clear();
        userCenterAdapter.addData(beanList);
        // 关注有礼
        loadWxLabel();
    }
}
