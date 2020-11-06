package com.bac.bihupapa.viewModle;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bac.bihupapa.R;
import com.bac.bihupapa.util.Util;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;


import static com.bac.bihupapa.conf.Constants.CommonProperty._0;
import static com.bac.bihupapa.conf.Constants.CommonProperty._1;
import static com.bac.bihupapa.conf.Constants.CommonProperty._11;
import static com.bac.bihupapa.conf.Constants.CommonProperty._2;
import static com.bac.bihupapa.conf.Constants.CommonProperty._3;
import static com.bac.bihupapa.conf.Constants.CommonProperty._4;
import static com.bac.bihupapa.conf.Constants.CommonProperty._5;
import static com.bac.bihupapa.conf.Constants.CommonProperty._6;
import static com.bac.bihupapa.conf.Constants.CommonProperty._9;
import static com.bac.bihupapa.conf.Constants.CommonProperty.__1;

/**
 * Created by Wjz on 2017/5/8.
 */

public class KeyboardPopupWindow {

    //private final ArrayList<ImageView> ivList = new ArrayList<>(_6);
    private final ArrayList<String> stringArrayList = new ArrayList<>(_6);

    private final SparseArrayCompat<ImageView> sac = new SparseArrayCompat<>(_6);
    private TextView tv;
    private final KaiYouBaoRechargeAdapter kaiYouBaoRechargeAdapter;
    private final RecyclerView rv;
    private final PopupWindow popupWindow;
    private StringBuilder sb = new StringBuilder(_6);


    public interface KeyboardCallback {
        void onKeyboardCallback(String s, PopupWindow popupWindow);
    }

    public KeyboardPopupWindow(List<String> list, final Context activity, View view, final KeyboardCallback keyboardCallback, final View.OnClickListener clickListener) {

        generateRandom(list);
        // popup
        final View popup = View.inflate(activity, R.layout.oil_kaiyoubao_recharge_dialog, null);

        popupWindow = new PopupWindow(popup, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        popup.findViewById(R.id.iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        popup.findViewById(R.id.tv_forget).setOnClickListener(clickListener);

        rv = popup.findViewById(R.id.rv);
        kaiYouBaoRechargeAdapter = new KaiYouBaoRechargeAdapter(R.layout.oil_kaiyoubao_recharge_item, stringArrayList);
        rv.setLayoutManager(new GridLayoutManager(activity, _3));
        rv.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
        rv.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL));
        rv.setAdapter(kaiYouBaoRechargeAdapter);
        // 设置获取焦点
        popupWindow.setFocusable(true);
        // 设置边缘点击收起
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(view, Gravity.BOTTOM, _0, _0);


        sac.put(_0, (ImageView) popup.findViewById(R.id.iv_01));// 0
        sac.put(_1, (ImageView) popup.findViewById(R.id.iv_02));
        sac.put(_2, (ImageView) popup.findViewById(R.id.iv_03));
        sac.put(_3, (ImageView) popup.findViewById(R.id.iv_04));
        sac.put(_4, (ImageView) popup.findViewById(R.id.iv_05));
        sac.put(_5, (ImageView) popup.findViewById(R.id.iv_06));

        rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, final View view, final int position) {
                Observable.just(position)
                        .filter(new Func1<Integer, Boolean>() {
                            @Override
                            public Boolean call(Integer integer) {
                                return integer != _9;
                            }
                        })
                        .groupBy(new Func1<Integer, String>() {
                            @Override
                            public String call(Integer integer) {
                                return integer == _11 ? "delete" : "add";
                            }
                        })
                        .subscribeOn(RxScheduler.RxPoolScheduler())
                        .subscribe(new Action1<GroupedObservable<String, Integer>>() {
                            @Override
                            public void call(GroupedObservable<String, Integer> objectIntegerGroupedObservable) {
                                if (objectIntegerGroupedObservable.getKey().equals("add")) {
                                    objectIntegerGroupedObservable
                                            .map(new Func1<Integer, Integer>() {
                                                @Override
                                                public Integer call(Integer integer) {
                                                    int length = sb.length();//0
                                                    if (length < _6) {
                                                        sb.append(view.getTag());
                                                    }
                                                    return length;
                                                }
                                            })
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Action1<Integer>() {
                                                @Override
                                                public void call(Integer integer) {
                                                    if (integer < _6) {
                                                        sac.get(integer).setVisibility(View.VISIBLE);
                                                        if (integer == _5) {
                                                           keyboardCallback.onKeyboardCallback(sb.toString(),popupWindow);
                                                        }
                                                    }
                                                }
                                            });
                                } else {
                                    objectIntegerGroupedObservable
                                            .map(new Func1<Integer, Integer>() {
                                                @Override
                                                public Integer call(Integer integer) {
                                                    int length = sb.length();
                                                    if (length-- > _0) {
                                                        sb.deleteCharAt(length);
                                                    }
                                                    return length;
                                                }
                                            })
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Action1<Integer>() {
                                                @Override
                                                public void call(Integer integer) {
                                                    if (integer > __1) {
                                                        sac.get(integer).setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                }
                            }
                        });


//                if (position != _11 && position != _9) {
//                    sb.append(view.getTag());
//                    int length = sb.length() + __1;
//
//                    if (length < _6) {
//                        sac.get(length).setVisibility(View.VISIBLE);
//                    }
//                    if (length == _5) {
//                        //keyboardCallback.onKeyboardCallback(sb.toString(), popupWindow);
//                    }
//                } else if (position == _11) {
//                    int length = sb.length() + __1;
//                    sb.deleteCharAt(length);
//
//
//                    sac.get(length).setVisibility(View.GONE);
//                }

            }
        });
    }

    /**
     * 设置键盘键位
     */
    private void generateRandom(List<String> list) {
        // 请空密码
        sb.delete(_0, sb.length());

        // 清空密码框
        for (int i = _0; i < _6; i++) {//0-5
            ImageView imageView = sac.get(i);
            if (imageView != null) {
                imageView.setVisibility(View.GONE);
            }
        }

        // 设置键盘键位
        stringArrayList.clear();
        stringArrayList.addAll(list);
        /*Random random = new Random();
        int size = stringArrayList.size();
        while (size < _10) {
            String s = random.nextInt(_10) + "";
            if (!stringArrayList.contains(s)) {
                stringArrayList.add(s);
                size = stringArrayList.size();
            }
        }
        // 添加
        stringArrayList.add(size - _1, __1 + "");
        stringArrayList.add(_10 + "");*/
    }

    /**
     * 消失popup
     */
    public void dismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 重新设置键盘
     */
    public void showData(List<String> list) {
        generateRandom(list);
        kaiYouBaoRechargeAdapter.setNewData(stringArrayList);
    }


    public void setLoading(AutomaticRxAppCompatActivity activity) {
        int width = rv.getWidth();
        int height = rv.getHeight();
        View loading = activity.getLayoutInflater().inflate(R.layout.oil_kaiyoubao_recharge_dialog_loading, null);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(width, height);
        loading.setLayoutParams(params);
        kaiYouBaoRechargeAdapter.setNewData(null);
        kaiYouBaoRechargeAdapter.setEmptyView(loading);
    }

    public void setFail(AutomaticRxAppCompatActivity activity, Util._3CountDownCallback callback) {
        int width = rv.getWidth();
        int height = rv.getHeight();
        // 增加成功切换界面
        View fail = activity.getLayoutInflater().inflate(R.layout.oil_kaiyoubao_recharge_dialog_fail, null);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(width, height);
        fail.setLayoutParams(params);
        tv = fail.findViewById(R.id.tv);
        tv.setText(String.format(activity.getString(R.string.kaiyoubao_pwd_dialog), "3"));
        kaiYouBaoRechargeAdapter.setNewData(null);
        kaiYouBaoRechargeAdapter.setEmptyView(fail);
        // 倒计时3秒
        Util._3CountDown(activity, tv, callback);
    }

    public void setShowCountDown(AutomaticRxAppCompatActivity activity, Util._3CountDownCallback callback) {

        int width = rv.getWidth();
        int height = rv.getHeight();
        // 增加成功切换界面
        View success = activity.getLayoutInflater().inflate(R.layout.oil_kaiyoubao_recharge_dialog_success, null);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(width, height);
        success.setLayoutParams(params);
        tv = success.findViewById(R.id.tv);
        tv.setText(String.format(activity.getString(R.string.kaiyoubao_pwd_dialog), "3"));
        kaiYouBaoRechargeAdapter.setNewData(null);
        kaiYouBaoRechargeAdapter.setEmptyView(success);
        // 倒计时3秒
        Util._3CountDown(activity, tv, callback);
    }
}
