package com.bac.bacplatform.module.kaiyoubao.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.extended.base.components.AutomaticBaseFragment;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.bills.QueryKaiYouBaoActivity;
import com.bac.bacplatform.module.kaiyoubao.KaiYouBaoCostActivity;
import com.bac.bacplatform.module.kaiyoubao.adapter.KaiYouBaoBean;
import com.bac.bacplatform.module.kaiyoubao.adapter.KaiYouBaoRVAdapter;
import com.bac.bacplatform.module.kaiyoubao.contract.KaiYouBaoContract;
import com.bac.bacplatform.module.recharge.OilVoucherActivity2;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.view.RecycleViewDivider;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.conf.Constants.CommonProperty._1;
import static com.bac.bacplatform.conf.Constants.CommonProperty._10;
import static com.bac.bacplatform.conf.Constants.SECOND_2;
import static com.bac.bacplatform.utils.ui.UIUtils.startActivityInAnim;

/**
 * Created by Wjz on 2017/5/4.
 */

public class KaiYouBaoFragment extends AutomaticBaseFragment implements KaiYouBaoContract.View {

    private Button btn;
    private TextView tvCenter;
    private RecyclerView rv;
    private TextView tvkaiyoubaomoney;
    private FrameLayout flIssue;
    private KaiYouBaoContract.Presenter presenter;
    //    private KaiYouBaoAdapter kaiYouBaoAdapter;
    private TextView tv01;
    private TextView tv02;
    private TextView tv11;
    private TextView tv;
    private RelativeLayout rl2;


    public static KaiYouBaoFragment newInstance() {
        return new KaiYouBaoFragment();
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kaiyoubao_fragment, container, false);
        initToolBar(view, "揩油宝");
        btn = (Button) view.findViewById(R.id.btn);
        tvCenter = (TextView) view.findViewById(R.id.tv_center);

        tv01 = (TextView) view.findViewById(R.id.tv_01);
        tv02 = (TextView) view.findViewById(R.id.tv_02);
        tv11 = (TextView) view.findViewById(R.id.tv_11);
        flIssue = (FrameLayout) view.findViewById(R.id.fl_issue);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rl2 = (RelativeLayout) view.findViewById(R.id.rl_02);
        tvkaiyoubaomoney = (TextView) view.findViewById(R.id.tv_kaiyoubao_money);
        //查看明细 tv
        tv = (TextView) view.findViewById(R.id.tv);

        RxView.clicks(view.findViewById(R.id.rl_01))
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // 去揩油宝账单
                        UIUtils.startActivityInAnim(activity, new Intent(activity, QueryKaiYouBaoActivity.class));
                    }
                });

        RxTextView.text(tvCenter).call(getString(R.string.kaiyoubao_label));
        RxTextView.text(btn).call(getString(R.string.kaiyoubao_user));
        //RxTextView.text(tvkaiyoubaomoney).call("199999");
//        initAdapter();
        initEvent();


        RxView.clicks(view.findViewById(R.id.rl_02))
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // 去揩油宝账单
                        UIUtils.startActivityInAnim(activity, new Intent(activity, OilVoucherActivity2.class));
                    }
                });


        Observable<List<Map<String, Object>>> QUERY_ACCOUNT_BALANCE = HttpHelper.getInstance()
                .bacNet(
                        new BacHttpBean()
                                .setActionType(_0)
                                .setMethodName("CARD_XML.QUERY_ACCOUNT_BALANCE")
                                .put("login_phone", BacApplication.getLoginPhone())

                )
                .observeOn(RxScheduler.RxPoolScheduler())
                // .map(new JsonFunc1<String,List<Map<String,Object>>>())
                .map(new Func1<String, List<Map<String, Object>>>() {
                    @Override
                    public List<Map<String, Object>> call(String s) {
                        return JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        Map<String, Object> stringObjectMap = maps.get(_0);//0

                        tvkaiyoubaomoney.setText(stringObjectMap.get("balance").toString());


                    }

                });
        final Observable<List<Map<String, Object>>> QUERY_ACCOUNT_BILL = HttpHelper.getInstance()
                .bacNet(
                        new BacHttpBean()
                                .setActionType(_0)
                                .setMethodName("CARD_XML.QUERY_ACCOUNT_BILL")
                                .put("login_phone", BacApplication.getLoginPhone())
                                .put("PAGE_NUM", _1)
                                .put("PAGE_SIZE", _10))

                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<Map<String, Object>>>() {
                    @Override
                    public List<Map<String, Object>> call(String s) {
                        return JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        if (maps != null && maps.size() > 0) {
                            rv.setLayoutManager(new LinearLayoutManager(activity));
                            rv.setAdapter(new KaiYouBaoRVAdapter(maps, getContext()));
                            rv.addItemDecoration(new RecycleViewDivider(activity, LinearLayoutManager.HORIZONTAL, _1, R.color.line));
                        }
                    }

                });
        QUERY_ACCOUNT_BILL.startWith(QUERY_ACCOUNT_BALANCE)
                .subscribe();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //presenter.loadData();


    }

    private void initAdapter() {
//        kaiYouBaoAdapter = new KaiYouBaoAdapter(null);
        rv.setLayoutManager(new LinearLayoutManager(activity));
//        rv.setAdapter(kaiYouBaoAdapter);
        rv.addItemDecoration(new RecycleViewDivider(activity, LinearLayoutManager.HORIZONTAL, _1, R.color.line));


    }

    private void initEvent() {

        RxView.clicks(flIssue)
                .throttleFirst(SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {


                        String load = ("https://mp.weixin.qq.com/s?__biz=MzA3ODk0MzI3Nw==&mid=503203426&idx=1&sn=8a6646e2dbf1651eab8e05f9a0de229b&key=fa89a07420f7b4e6fbf8b906417b74f80e3fadfb85a1732cf1520795109eee8ac0f5182c592eed4f83a657668cd709b581aca93f8391f5fcc25934a3a41447b83e88787dd627802d9a7127fd4eac2a17&ascene=0&uin=NTIyODQ0MzQw&devicetype=iMac+MacBookPro9%2C1+OSX+OSX+10.12.3+build(16D32)&version=12020510&nettype=WIFI&fontScale=100&pass_ticket=GzizIHiAPSNUP65x8Osg0aHj4ZmImlTiPA%2BccLXxSGJxEawb9OB3C8OUgOpJsdfD");

                        // 跳转广告详情
                        Intent intentToAgree = new Intent(activity,
                                WebAdvActivity.class);
                        intentToAgree.putExtra("title", "关于揩油宝");
                        intentToAgree.putExtra("ads_url", load);
                        UIUtils.startActivityInAnim(activity, intentToAgree);
                        //startActivityInAnim(activity, new Intent(activity, KaiYouBaoProblemActivity.class));
                    }
                });
        RxView.clicks(btn)
                .throttleFirst(SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getActivity(), KaiYouBaoCostActivity.class);
                        intent.putExtra("balance", tvkaiyoubaomoney.getText().toString());

                        startActivityInAnim(activity, intent);
//                        startActivityInAnim(activity,KaiYouBaoCostActivity.newIntent(activity) );
                    }
                });
    }

    @Override
    public void setPresenter(KaiYouBaoContract.Presenter presenter) {

        this.presenter = presenter;

    }


    @Override
    public void showData(List<KaiYouBaoBean> beanList) {
//        显示数据
//        kaiYouBaoAdapter.getData().clear();
//        kaiYouBaoAdapter.addData(beanList);
    }


}
