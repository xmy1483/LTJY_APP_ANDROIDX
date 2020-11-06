package com.bac.bacplatform.weex_utils;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.module.recharge.adapter.OilVoucherBean;
import com.bac.bacplatform.repository.Repository;
import com.bac.bacplatform.weex_bean.CardItemVoucherBean;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.taobao.weex.bridge.JSCallback;
import org.json.JSONException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.conf.Constants.CommonProperty._10;
import static com.bac.bacplatform.conf.Constants.CommonProperty._20;

public class VoucherUtil {

    private VoucherUtil(){}

    private static VoucherUtil ins = null;

    private double voucherCount = 0;
    private String voucherId = "";

    private void reset()
    {
        voucherCount = 0;
        voucherId="";
    }

    public static VoucherUtil getIns(){
        if(ins == null)
            ins = new VoucherUtil();
        return ins;
    }

    private List<OilVoucherBean> voucherList;

    //查优惠券
    public void queryVoucher(String cardNum,String rechargeMoney,final double saleMoney ,final JSCallback callback){
        Log.d("loglog1483", "queryVoucher: voucher 1");
        reset();
        Log.d("loglog1483", "queryVoucher: voucher 2");
        BacHttpBean bacHttp =  new BacHttpBean()
                .setMethodName("QUERY_USE_VOUCHER")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("card_no", cardNum)
                .put("recharge_money", rechargeMoney);

        Repository.getInstance().getData(bacHttp, true)
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<OilVoucherBean>>() {
                    @Override
                    public List<OilVoucherBean> call(String s) {
                        return JSON.parseObject(s, new TypeReference<List<OilVoucherBean>>() {}.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<OilVoucherBean>>() {
                    @Override
                    public void call(List<OilVoucherBean> list) {
                        // 计算券金额
                        voucherList = list;
                        if (list.size() > _0) {
                            Log.d("loglog1483", "call: 查询优惠券： 有");
                            chooseMaxDiscount(list,saleMoney,callback);
                        } else {
                            Log.d("loglog1483", "call: 查询优惠券： 无");
                            reset();
                            invokeJSCallback(callback);
                        }
                    }
                });

    }



    private void chooseMaxDiscount(List<OilVoucherBean> list, final double saleMoney, final JSCallback callback) {
        Observable<OilVoucherBean> discount = Observable.from(list)
                .filter(new Func1<OilVoucherBean, Boolean>() {
                    @Override
                    public Boolean call(OilVoucherBean oilVoucherBean) {
                        return oilVoucherBean.getVoucher_type() == _10;
                    }
                })
                .toList()
                .map(new Func1<List<OilVoucherBean>, OilVoucherBean>() {
                    @Override
                    public OilVoucherBean call(List<OilVoucherBean> list) {
                        // 最小
                        OilVoucherBean min = null;

                        if (list.size() > 0) {
                            min = Collections.min(list, new Comparator<OilVoucherBean>() {
                                @Override
                                public int compare(OilVoucherBean o1, OilVoucherBean o2) {
                                    return (int) ((o1.getDiscount() - o2.getDiscount()) * 100000);
                                }
                            });
                        }

                        return min;
                    }
                });

        Observable<OilVoucherBean> use = Observable.from(list)
                .filter(new Func1<OilVoucherBean, Boolean>() {
                    @Override
                    public Boolean call(OilVoucherBean oilVoucherBean) {
                        return oilVoucherBean.getVoucher_type() == _20;
                    }
                })
                .toList()
                .map(new Func1<List<OilVoucherBean>, OilVoucherBean>() {
                    @Override
                    public OilVoucherBean call(List<OilVoucherBean> list) {
                        //最大
                        OilVoucherBean max = null;
                        if (list.size() > 0) {
                            max = Collections.max(list, new Comparator<OilVoucherBean>() {
                                @Override
                                public int compare(OilVoucherBean o1, OilVoucherBean o2) {
                                    return (int) (o1.getVoucher_money() - o2.getVoucher_money());
                                }
                            });
                        }
                        return max;

                    }
                });

        Observable.combineLatest(use, discount, new Func2<OilVoucherBean, OilVoucherBean, OilVoucherBean>() {
            @Override
            public OilVoucherBean call(OilVoucherBean use, OilVoucherBean discount) {
                OilVoucherBean oilVoucherBean = null;
                if (discount != null && use != null) {
                    double use_money = use.getVoucher_money();

                    double discount_money = discount.getDiscount() * saleMoney;
                    double voucher_money = discount.getVoucher_money();

                    if (discount_money > voucher_money) {
                        discount_money = voucher_money;
                    }

                    oilVoucherBean = use_money - discount_money > 0 ? use : discount;
                } else if (discount != null) {
                    oilVoucherBean = discount;
                } else if (use != null) {
                    oilVoucherBean = use;
                }
                return oilVoucherBean;
            }
        })
                .filter(new Func1<OilVoucherBean, Boolean>() {
                    @Override
                    public Boolean call(OilVoucherBean oilVoucherBean) {
                        return oilVoucherBean != null;
                    }
                })
                .subscribeOn(RxScheduler.RxPoolScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OilVoucherBean>() {
                    @Override
                    public void call(OilVoucherBean oilVoucherBean) {
//                        selectedOilVoucherBean = oilVoucherBean;
                        calcMaxDiscount(oilVoucherBean,saleMoney,callback);
                    }
                });
    }

    private void calcMaxDiscount(OilVoucherBean oilVoucherBean, double saleMOney, JSCallback callback) {
        int disMax;
        if (oilVoucherBean.getVoucher_type() == _10) {//10 discount
            int disD = (int) ((1 - oilVoucherBean.getDiscount()) * saleMOney);
            double voucher_money = oilVoucherBean.getVoucher_money();
            if (disD > voucher_money) {
                disMax = (int) voucher_money;
                voucherId = oilVoucherBean.getVoucher_id()+"";
            } else {
                disMax = (int) voucher_money;
                voucherId = oilVoucherBean.getVoucher_id()+"";
            }

        } else {//20 user
            disMax = (int) oilVoucherBean.getVoucher_money();
            voucherId = oilVoucherBean.getVoucher_id()+"";
        }
        // 合计
        voucherCount = disMax;
        invokeJSCallback(callback);
    }

    private void invokeJSCallback(JSCallback callback){
        Log.d("loglog1483", "invokeJSCallback:============= "+voucherId+"   "+voucherCount);
        CardItemVoucherBean bean = new CardItemVoucherBean();
        bean.setVoucherCount(voucherCount);
        bean.setVoucherId(voucherId);
        Log.d("loglog1483", "invokeJSCallback: ");
        callback.invokeAndKeepAlive(bean);

    }

}
