package com.bac.bacplatform.module.kaiyoubao.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.extended.base.components.AutomaticBaseFragment;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.kaiyoubao.contract.KaiYouBaoCostContract;
import com.bac.bacplatform.old.module.phone.ActivityPhone1;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.weex_activities.WeexOilRechargeActivity;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.wjz.weexlib.weex.activity.WeexActivity2;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Wjz on 2017/5/4.
 */

public class KaiYouBaoCostFragment extends AutomaticBaseFragment implements KaiYouBaoCostContract.View {

    private KaiYouBaoCostContract.Presenter presenter;
    private RelativeLayout rl04;
    private RelativeLayout rl03;
    private RelativeLayout rl02;

    public static KaiYouBaoCostFragment newInstance() {
        return new KaiYouBaoCostFragment();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.kaiyoubao_cost_fragment, container, false);
        initToolBar(view, getString(R.string.kaiyoubao_cost_title));
        rl04 = (RelativeLayout) view.findViewById(R.id.rl_04);
        rl03 = (RelativeLayout) view.findViewById(R.id.rl_03);
        rl02 = (RelativeLayout) view.findViewById(R.id.rl_02);

        // rl03.setVisibility(View.GONE);
        //rl04.setVisibility(View.GONE);
//        TextView textView = (TextView) view.findViewById(R.id.tv_11);
//        textView.setText(getActivity().getIntent().getStringExtra("balance"));
        RxTextView.text((TextView) view.findViewById(R.id.tv_11)).call(getActivity().getIntent().getStringExtra("balance"));

        initEvent();
        return view;
    }

    private void initEvent() {
        RxView.clicks(rl02)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {


                        //添加时间判断  油卡充值时间2:30-23:00
                        Calendar cal = Calendar.getInstance();
// 当前日期
                        int hour = cal.get(Calendar.HOUR_OF_DAY);
// 获取小时
                        int minute = cal.get(Calendar.MINUTE);
// 获取分钟
                        int minuteOfDay = hour * 60 + minute;
// 从0:00分开是到目前为止的分钟数
                        final int start = 2 * 60 + 30;
// 起始时间 2：30的分钟数
                        final int end = 23 * 60;
// 结束时间 23:00的分钟数
                        if (minuteOfDay >= start && minuteOfDay <= end) {

                            HttpHelper.getInstance()
                                    .fragmentAutoLifeAndLoading(KaiYouBaoCostFragment.this,
                                            new BacHttpBean().setMethodName("CHECK_RECHARGE").put("login_phone", BacApplication.getLoginPhone())
                                            , null, null, null)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<String>() {
                                        @Override
                                        public void call(String s) {
//                                            UIUtils.startActivityInAnim(activity, OilActivity.newIntent(activity).putExtra("is", true));
                                            UIUtils.startActivityInAnim(activity, new Intent(activity,WeexOilRechargeActivity.class));

                                        }
                                    });


                        } else {

                            //自定义dialog
                            new AlertDialog.Builder(activity)
                                    .setTitle("暂停加油卡充值服务")
                                    .setMessage("23:00至02:30账务结算，期间暂停加油卡充值服务，请您谅解。")
                                    .show();
//                            View view = activity.getLayoutInflater().inflate(R.layout.time_dialog, null);
//                            AlertDialog alertDialog = new AlertDialog.Builder(activity)
//                                    .create();
//                            alertDialog.setView(view);
//                            alertDialog.show();

                            //Toast.makeText(getContext(), "充值时间在每天2：30到23：00开放", Toast.LENGTH_SHORT).show();
                        }


                    }


                    // UIUtils.startActivityInAnim(activity, OilActivity.newIntent(activity).putExtra("is",true));


                });
        RxView.clicks(rl03).throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UIUtils.startActivityInAnim(activity, new Intent(activity, ActivityPhone1.class).putExtra("balance", Float.parseFloat(activity.getIntent().getStringExtra("balance"))));
                    }
                });
        RxView.clicks(rl04).throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UIUtil.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                                .setData(Uri.parse("https://app5.bac365.com:10443/weex/sg/SGCard/giftCardList.js")));
                    }
                });
    }


    @Override
    public void setPresenter(KaiYouBaoCostContract.Presenter presenter) {
        this.presenter = presenter;
    }



}
