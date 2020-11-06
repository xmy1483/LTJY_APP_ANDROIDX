package com.bac.bacplatform.weex_utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.center.MypswActivity;
import com.bac.bacplatform.module.center.SetMypswActivity;
import com.bac.bacplatform.module.recharge.OilRechargeResultActivity;
import com.bac.bacplatform.module.recharge.OilVoucherActivity;
import com.bac.bacplatform.module.recharge.adapter.OilBean;
import com.bac.bacplatform.module.recharge.adapter.OilVoucherBean;
import com.bac.bacplatform.old.module.hybrid.ZhiFuBaoActivity;
import com.bac.bacplatform.repository.Repository;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.utils.tools.Util;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.view.CanClearEditText;
import com.bac.bacplatform.view.KeyboardPopupWindow;
import com.bac.bacplatform.weex_activities.WeexOilRechargeActivity;
import com.bac.bacplatform.weex_bean.AnyCountBean;
import com.bac.bacplatform.weex_bean.CervificatedCardBean;
import com.bac.bacplatform.weex_callback.SimpleCallback;
import com.bac.bacplatform.weex_fragments.WeexOilRechargeFragment;
import com.bac.bacplatform.wxapi.WxPayReq;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.seed.Encrypt;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.FragmentEvent;
import com.bac.rxbaclib.rx.permission.Permission;
import com.bac.rxbaclib.rx.permission.RxPermissionImpl;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.taobao.weex.bridge.JSCallback;
import com.unionpay.UPPayAssistEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static android.os.Build.MODEL;
import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.conf.Constants.CommonProperty._10;
import static com.bac.bacplatform.conf.Constants.CommonProperty._20;

//import com.unionpay.UPPayAssistEx;

/**
 * Author XueMingYong
 * Date 2019/4/24 13:53
 * Desc
 */
public class OilRechargeUtil {
    private static OilRechargeUtil ins;
    private AlertDialog waitDialog = null;

    public void hideWaitDialog() {
        if (waitDialog!=null && waitDialog.isShowing()){
            waitDialog.dismiss();
        }
    }

    private OilRechargeUtil() {
    }

    public static OilRechargeUtil newIns() {
        if (ins == null) ins = new OilRechargeUtil();
        return ins;
    }

    //删除验卡记录
    public void delCertificatedCardNum(String cardNum) {
        BacHttpBean httpDel = new BacHttpBean()
                .setMethodName("CARD_XML.DEL_VERIFICATE_CARD")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("card_no", cardNum);
        Repository.getInstance().getData(httpDel, true, WeexOilRechargeActivity.instance)
                .compose(WeexOilRechargeActivity.instance.getOilFragment().<String>bindUntilEvent(FragmentEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(WeexOilRechargeActivity.instance))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {
                    }
                });
    }


    //已经验卡的卡号列表
    public void getCertificatedCardLIst(final JSCallback callback) {
        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setMethodName("CARD_XML.GET_VERIFICATE_CARD")
                .put("login_phone", BacApplication.getLoginPhone()))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {
                        try {
                            List<CervificatedCardBean> listData = new ArrayList<>();
                            JSONArray verNumList = new JSONArray();
                            if (mapList.size() > 0) {
                                for (Map<String, Object> item : mapList) {
                                    CervificatedCardBean itemBean = new CervificatedCardBean();
                                    itemBean.setName(item.get("card_name") + "");
                                    itemBean.setNumber(item.get("card_no") + "");
                                    JSONObject numObj = new JSONObject();
                                    numObj.put("name", item.get("card_name") + "");
                                    numObj.put("number", item.get("card_no") + "");
                                    verNumList.put(numObj);
                                    listData.add(itemBean);
                                }
                            }
//                            Log.d("loglog1484", "call: "+verNumList.toString());
//                            callback.invokeAndKeepAlive(verNumList.toString());
                            callback.invokeAndKeepAlive(listData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    //获取油卡面额
    public void getOilCardData(String cardNo, final JSCallback callback) {
        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setMethodName("QUERY_RECHARGE_PRODUCT")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("oil_type", _10)
                .put("card_no", cardNo))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {

                        try {
                            List<Map<String, Object>> Amaps = maps;
                            List<OilBean> list = JSON.parseObject(maps.toString(), new TypeReference<List<OilBean>>() {
                            }.getType());
                            for (OilBean bean : list) {
                                if (!bean.getProduct_name().contains("##"))
                                    calculateDiscount(bean);
                            }
                            //到这里数据处理完成 转json
                            JSONArray arr = new JSONArray();
                            JSONObject tempObj;
                            for (OilBean bean : list) {
                                tempObj = new JSONObject();
                                tempObj.put("oil_type", bean.getOil_type() + "");
                                tempObj.put("product_name", bean.getProduct_name() + "");
                                tempObj.put("recharge_money", bean.getRecharge_money() + "");
                                tempObj.put("sale_money", bean.getSale_money() + "");
                                tempObj.put("discount", bean.getDiscount() + "");
                                tempObj.put("discount_img_path", bean.getDiscount_img_path());
                                arr.put(tempObj);
                            }
//                            callback.invokeAndKeepAlive(arr.toString());
                            callback.invokeAndKeepAlive(list);
                        } catch (JSONException e) {
                        }
                    }
                });
    }

    //计算折扣 和 初始化折扣对应的图片地址
    private void calculateDiscount(OilBean b) {
        int discount = (int) ((b.getSale_money() / b.getRecharge_money()) * 1000);
        String s = discount + "";
        if (s.endsWith("0"))
            s = s.substring(0, s.length() - 1);
        try {
            b.setDiscount(Integer.parseInt(s));
        } catch (Exception e) {
            b.setDiscount(100);
        }
        switch (b.getDiscount()) {
            case 95:
                b.setDiscount_img_path("discount_95.png");
                break;
            case 96:
                b.setDiscount_img_path("discount_96.png");
                break;
            case 97:
                b.setDiscount_img_path("discount_97.png");
                break;
            case 98:
                b.setDiscount_img_path("discount_98.png");
                break;
            case 99:
                b.setDiscount_img_path("discount_99.png");
                break;
            case 955:
                b.setDiscount_img_path("discount_955.png");
                break;
            case 965:
                b.setDiscount_img_path("discount_965.png");
                break;
            case 975:
                b.setDiscount_img_path("discount_975.png");
                break;
            case 985:
                b.setDiscount_img_path("discount_985.png");
                break;
            case 995:
                b.setDiscount_img_path("discount_995.png");
                break;
            default:
                b.setDiscount_img_path("");
        }
    }


    //输入任意金额后查询实际需要支付金额
    private void getSaleMoneyAfterInputAnyCount(final String cardNum, String oilType, final JSCallback callback) {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setMethodName("BASEXML.QUERY_RECHARGE_MONEY")
                .put("oil_type", oilType)
                .put("login_phone", BacApplication.getLoginPhone())
                .put("recharge_money", finalRechargeMoney))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        finalSaleMoney = Double.valueOf(maps.get(0).get("sale_money") + "");//记录为最终金额
                        //读取优惠券信息
                        queryVoucher(cardNum, callback);
                    }
                });


    }

    private List<OilVoucherBean> voucherList;//优惠群集合

    //查优惠券
    public void queryVoucher(String cardNum, final JSCallback callback) {
        BacHttpBean bacHttp = new BacHttpBean()
                .setMethodName("QUERY_USE_VOUCHER")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("card_no", cardNum)
                .put("recharge_money", finalRechargeMoney);

        Repository.getInstance().getData(bacHttp, true)
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<OilVoucherBean>>() {
                    @Override
                    public List<OilVoucherBean> call(String s) {
                        return JSON.parseObject(s, new TypeReference<List<OilVoucherBean>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<OilVoucherBean>>() {
                    @Override
                    public void call(List<OilVoucherBean> list) {
                        // 计算券金额
                        voucherList = list;
                        if (list.size() > _0) {
                            chooseMaxDiscount(list, callback);
                        } else {
                            voucherBeanReset(callback);
                        }
                    }
                });


        ////////////

    }

    private void resetData() {
        voucherId = "";
        finalSaleMoney = 0;
        finalRechargeMoney = 0;
        maxVoucher = 0;
    }

    public void showAnyCountDialog(final Context context, final String cardNum, final String oilType, double balance, final JSCallback callback) {
        resetData();
        View dialog = LayoutInflater.from(context).inflate(R.layout.oil_fragment_dialog_any, null);
        final CanClearEditText etView = dialog.findViewById(R.id.ccet_01);
        TextView tv = dialog.findViewById(R.id.tv_01);
        tv.setText("余额" + balance + "元");
        if (balance > 1) {
            String text = ((int) balance) + "";
            etView.setText(text);
            etView.setSelection(text.length());
        }
        new AlertDialog.Builder(context)
                .setTitle("请输入充值金额")
                .setView(dialog)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String s = etView.getText().toString().trim();
                        double recharge_money;
                        try {
                            recharge_money = Double.parseDouble(s);

                            if (recharge_money < 1) {
                                Toast.makeText(context, "请输入正确的充值金额", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (recharge_money > 200) {
                                new AlertDialog.Builder(context)
                                        .setTitle("充值金额不能超过200")
                                        .setNegativeButton("取消", null)
                                        .setPositiveButton("确定", null).show();
                                recharge_money = 200;
                            }
                            finalRechargeMoney = recharge_money;//记录最终充值金额
                            getSaleMoneyAfterInputAnyCount(cardNum, oilType, callback);//获取实付金额
                        } catch (Exception e) {
                        }
                    }
                }).show();
    }


    //====== util ==============================================================================================

    private OilVoucherBean selectedOilVoucherBean;//被选择的优惠券
    private int maxVoucher = 0;
    private String voucherId = "";
    private double finalSaleMoney = 0;
    private double finalRechargeMoney = 0;

    private void chooseMaxDiscount(List<OilVoucherBean> list, final JSCallback callback) {
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

                    double discount_money = discount.getDiscount() * finalSaleMoney;
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
                        selectedOilVoucherBean = oilVoucherBean;
                        calcMaxDiscount(oilVoucherBean, callback);
                    }
                });
    }

    @NonNull
    private void calcMaxDiscount(OilVoucherBean oilVoucherBean, JSCallback callback) {
        int disMax;
        if (oilVoucherBean.getVoucher_type() == _10) {//10 discount
            int disD = (int) ((1 - oilVoucherBean.getDiscount()) * finalSaleMoney);
            double voucher_money = oilVoucherBean.getVoucher_money();
            if (disD > voucher_money) {
                disMax = (int) voucher_money;
                voucherId = oilVoucherBean.getVoucher_id() + "";
            } else {
                disMax = (int) voucher_money;
                voucherId = oilVoucherBean.getVoucher_id() + "";
            }

        } else {//20 user
            disMax = (int) oilVoucherBean.getVoucher_money();
            voucherId = oilVoucherBean.getVoucher_id() + "";
        }
        // 合计
        maxVoucher = disMax;
        invokeJSCallback(callback);
    }


    private void voucherBeanReset(JSCallback callback) {
        selectedOilVoucherBean = null;
        invokeJSCallback(callback);
    }

    //这里返回  saleMoney discount voucherMoney 给 weex
    private void invokeJSCallback(JSCallback callback) {
        try {

            OilBean temp = new OilBean();
            temp.setRecharge_money(finalRechargeMoney);
            temp.setSale_money(finalSaleMoney);
            calculateDiscount(temp);//计算折扣和对应图片

            AnyCountBean bean = new AnyCountBean();
            bean.setRecharge(temp.getRecharge_money());
            bean.setPay(temp.getSale_money());
            bean.setDiscount(temp.getDiscount());
            bean.setDiscount_img_path(temp.getDiscount_img_path());
            bean.setVoucherCount(maxVoucher);
            bean.setVoucherId(voucherId);


            JSONObject obj = new JSONObject();
            obj.put("recharge", temp.getRecharge_money());
            obj.put("pay", temp.getSale_money());
            obj.put("discount", temp.getDiscount());
            obj.put("discount_img_path", temp.getDiscount_img_path());
            obj.put("voucherCount", maxVoucher);
            obj.put("voucherId", voucherId);
//            callback.invokeAndKeepAlive(obj.toString());
            callback.invokeAndKeepAlive(bean);
        } catch (JSONException e) {
        }
    }


    private boolean is_have_pay_pw = true;

    //获取KYB余额
    public void getKYBbalance(final SimpleCallback callback) {
        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setMethodName("CARD_XML.QUERY_ACCOUNT_BALANCE")
                .put("login_phone", BacApplication.getLoginPhone()))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        if (maps.size() > 0) {
                            Map<String, Object> map = maps.get(_0);
                            String balanceS = map.get("balance") + "";
                            is_have_pay_pw = Boolean.parseBoolean(map.get("is_have_pay_pw") + "");
                            callback.onSuccess(balanceS);
                        }
                    }
                });
    }


    private PayBean singlePayBean;

    public void skipToPay(String cardNum, String oilTYpe, String platform, String recharge, String saleMoney, String shouldPay, double voucherCount, String voucherId) {
        singlePayBean = new PayBean(cardNum, oilTYpe, platform, recharge, saleMoney, shouldPay, voucherId, voucherCount);
        WeexOilRechargeActivity.instance.setPayOrderBean(cardNum,recharge);
        verificateCard();
    }

    //验卡
    private void verificateCard() {
        //修改bug 19.05.21
        BacHttpBean bean = new BacHttpBean()
                .setMethodName("VERIFICATE_CARD")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("card_no", singlePayBean.getCardNum());
        Repository.getInstance().getData(bean, true, WeexOilRechargeActivity.instance)
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        onVerificate(maps);
                    }
                });
        ;


/*        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setMethodName("VERIFICATE_CARD")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("card_no", singlePayBean.getCardNum()))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call( List<Map<String, Object>> maps) {
                        onVerificate(maps);
                    }
                });*/
    }

    // 验卡反馈
    private void onVerificate(List<Map<String, Object>> list) {
        if (list.size() > _0) {
            Map<String, Object> map = list.get(_0);
            String s = map.get("card_no") + "";
            int length = s.length();
            Object nameObj = map.get("card_name");
            String name = "";
            if (nameObj != null)
                name = nameObj.toString();
            new AlertDialog.Builder(WeexOilRechargeActivity.instance)
                    .setTitle("信息验证")
                    .setMessage(Html.fromHtml(getWarnByOwner(name, s.substring(length - 6, length))))
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (singlePayBean.getPlatform().equals("KYB")) {
                                // 金额判断
                                if (is_have_pay_pw) {
                                    // 获取键盘键位
                                    HttpHelper.getInstance().fragmentAutoLifeAndLoading(WeexOilRechargeFragment.ins,
                                            new BacHttpBean().setMethodName("BASEXML.QUERY_KEYBOARD")
                                                    .put("login_phone", BacApplication.getLoginPhone()))
                                            .observeOn(RxScheduler.RxPoolScheduler())
                                            .map(new Func1<String, List<String>>() {
                                                @Override
                                                public List<String> call(String s) {
                                                    List<Map<String, String>> list = JSON.parseObject(s, new TypeReference<List<Map<String, String>>>() {
                                                    }.getType());
                                                    return JSON.parseObject(list.get(_0).get("keyboard_value"), new TypeReference<List<String>>() {
                                                    }.getType());
                                                }
                                            })
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Action1<List<String>>() {
                                                @Override
                                                public void call(List<String> list) {
                                                    createKaiYouBaoRecharge(list);
                                                }
                                            });
                                } else {
                                    UIUtils.startActivityInAnim(WeexOilRechargeActivity.instance, SetMypswActivity.newIntent(WeexOilRechargeActivity.instance));
                                }

                            } else {
                                showWaitDialog();
                                BacHttpBean httpBean = new BacHttpBean()
                                        .setMethodName("RECHARGE_OIL")
                                        .put("login_phone", BacApplication.getLoginPhone())
                                        .put("card_no", singlePayBean.getCardNum())
                                        .put("recharge_money", singlePayBean.getRecharge())
                                        .put("sale_money", singlePayBean.getSaleMoney())
                                        .put("voucher_id", singlePayBean.getVoucherId())
                                        .put("voucher_money", singlePayBean.getVoucherCount())
                                        .put("platform_name", "RECHARGE_OIL")
                                        .put("pay_type", singlePayBean.getPlatform())
                                        .put("pay_money", singlePayBean.getShouldPay())
                                        .put("terminal_id", MODEL);


                                Repository.getInstance().getData(httpBean, true, WeexOilRechargeActivity.instance)
                                        .compose(new Observable.Transformer<String, String>() {
                                            @Override
                                            public Observable<String> call(Observable<String> observable) {
                                                return observable.compose(new RxDialog<String>().rxDialog(WeexOilRechargeActivity.instance));
                                            }
                                        })
                                        .observeOn(RxScheduler.RxPoolScheduler())
                                        .map(new JsonFunc1<String, List<Map<String, Object>>>())
                                        .observeOn(Schedulers.from(AsyncTask.SERIAL_EXECUTOR))
                                        .flatMap(new Func1<List<Map<String, Object>>, Observable<String>>() {
                                            @Override
                                            public Observable<String> call(List<Map<String, Object>> mapList) {
                                                Map<String, Object> map = mapList.get(_0);
                                                BacHttpBean hb = new BacHttpBean().setMethodName("PAY")
                                                        .put("platform_name", map.get("platform_name"))
                                                        .put("pay_type", map.get("pay_type"))
                                                        .put("order_id", map.get("order_id"))
                                                        .put("content", "油卡充值：" + map.get("recharge_money") + "元")
                                                        .put("pay_money", map.get("pay_money"));

                                                return Repository.getInstance().getData(hb, true, WeexOilRechargeActivity.instance);
                                            }
                                        })
                                        .observeOn(RxScheduler.RxPoolScheduler())
                                        .map(new JsonFunc1<String, List<Map<String, Object>>>())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Action1<List<Map<String, Object>>>() {
                                            @Override
                                            public void call(List<Map<String, Object>> mapList) {
                                                RECHARGE_OIL(mapList);
                                            }
                                        });


                            }
                        }
                    }).show();
        }
    }

    private void showWaitDialog() {
        waitDialog = new AlertDialog.Builder(WeexOilRechargeActivity.instance).create();
        View view = LayoutInflater.from(WeexOilRechargeActivity.instance).inflate(R.layout.pay_wait_dialog,null,false);
        waitDialog.setView(view);
        waitDialog.setCanceledOnTouchOutside(false);
        waitDialog.show();
    }

    private void RECHARGE_OIL(List<Map<String, Object>> list) {
        final WeexOilRechargeActivity context = WeexOilRechargeActivity.instance;
        // 记录油卡信息
        PreferenceManager.getDefaultSharedPreferences(WeexOilRechargeActivity.instance)
                .edit().putString("oilCard", StringUtil.encode(context, singlePayBean.getCardNum()))
                .apply();

        // io 线程
        Map<String, Object> map = list.get(_0);
        String pay_type = singlePayBean.getPlatform();
        if ("ALIPAY".equals(pay_type)) {
            Intent it = new Intent(WeexOilRechargeActivity.instance,ZhiFuBaoActivity.class);
            it.putExtra("paymentUrl",String.valueOf(map.get("paymentUrl")));
            it.putExtra("cardNo",singlePayBean.getCardNum());
            it.putExtra("recharge",singlePayBean.getRecharge()+"");
            WeexOilRechargeActivity.instance.startActivity(it);
        } else if ("WECHAT".equals(pay_type)) {
            HashMap<String, Object> hm_1 = new HashMap<>();
            hm_1.put("id", "oil");

            HashMap<String, Object> hm_2 = new HashMap<>();
            hm_2.put("recharge_money", singlePayBean.getRecharge());
            hm_2.put("card_no", singlePayBean.getCardNum());
            hm_1.put("data", hm_2);
            WxPayReq.pay(map, hm_1);
        } else if ("KYB".equals(pay_type)) {
            if (Boolean.parseBoolean(map.get("is_kyb_succ") + "")) {
                // 充值成功
                keyboardPopupWindow.setShowCountDown(context, new Util._3CountDownCallback() {
                    @Override
                    public void _3CountDownCallback() {
                        // 消失
                        keyboardPopupWindow.dismiss();
                        //支付成功页面
                        UIUtils.startActivityInAnim(context, OilRechargeResultActivity.newIntent(context).putExtra("recharge_money", singlePayBean.getRecharge() + "").putExtra("card_no", singlePayBean.getCardNum()));
                    }
                });
            }else {
                // 充值失败
                keyboardPopupWindow.setFail(context, new Util._3CountDownCallback() {
                    @Override
                    public void _3CountDownCallback() {
                        // 消失
                        keyboardPopupWindow.dismiss();
                    }
                });
            }
        }  else if ("UNION_PAY".equals(pay_type)) {
            doUnionPay(map);
        }
    }

    private void doUnionPay(Map<String,Object> map){
        Object respCodeObj = map.get("respCode");
        if(respCodeObj != null && respCodeObj.toString().equals("00")) {
            Object orderIdObj = map.get("merchantOrderId");
            Object tn = map.get("tn");
            Object orderTime = map.get("txnTime");
            Object payPlatform = map.get("payPlatform");

            if(orderIdObj!=null && orderTime != null && payPlatform != null && tn != null) {
                WeexOilRechargeActivity.instance.setUnionPayBean(orderIdObj.toString(),orderTime.toString(),payPlatform.toString());
                UPPayAssistEx.startPay(WeexOilRechargeActivity.instance,null,null,tn.toString(),"00");
            } else {
                Toast.makeText(WeexOilRechargeActivity.instance, "调起支付失败："+map.get("desc").toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(WeexOilRechargeActivity.instance, "调起支付失败："+map.get("desc").toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getWarnByOwner(String owner, String cardNum) {
        String originalStr = "您尾号为“<font color=\"#4dadde\"> " + cardNum + " </font>”的加油卡<br>";
        String isEmpty = "未查询到持卡人信息，确认充值";
        String isNotEmpty = "持卡人为“<font color=\"#4dadde\">" + owner + "</font>”";
        return TextUtils.isEmpty(owner) ? originalStr + isEmpty : originalStr + isNotEmpty;
    }

    KeyboardPopupWindow keyboardPopupWindow;

    private void createKaiYouBaoRecharge(List<String> list) {
        final WeexOilRechargeActivity activity = WeexOilRechargeActivity.instance;
        final WeexOilRechargeFragment view1 = activity.getOilFragment();

        // 忘记密码
        keyboardPopupWindow = new KeyboardPopupWindow(list, WeexOilRechargeActivity.instance, WeexOilRechargeActivity.instance.getOilFragment().getView(), new KeyboardPopupWindow.KeyboardCallback() {
            @Override
            public void onKeyboardCallback(String pass, PopupWindow popupWindow) {

                BacHttpBean httpPW = new BacHttpBean()
                        .setMethodName("VERIFICATE_PAY_PASSWORD")
                        .put("login_phone", BacApplication.getLoginPhone())
                        .put("pay_password", new Encrypt().SHA256(pass));

                Repository.getInstance().getData(httpPW, true, WeexOilRechargeActivity.instance,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showKeyboardData();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showKeyboardData();
                            }
                        }, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                showKeyboardData();
                            }
                        })
                        .compose(WeexOilRechargeActivity.instance.getOilFragment().<String>bindUntilEvent(FragmentEvent.DESTROY))
                        .compose(new RxDialog<String>().rxDialog(WeexOilRechargeActivity.instance))
                        .observeOn(RxScheduler.RxPoolScheduler())
                        .map(new JsonFunc1<String, List<Map<String, Object>>>())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<Map<String, Object>>>() {
                            @Override
                            public void call(List<Map<String, Object>> mapList) {
                                VERIFICATE_PAY_PASSWORD(mapList);
                            }
                        });

                // 更改页面
                keyboardPopupWindow.setLoading(WeexOilRechargeActivity.instance);

            }

        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 忘记密码
//                Toast.makeText(WeexOilRechargeActivity.instance, "", Toast.LENGTH_SHORT).show();
                UIUtils.startActivityInAnim(activity, MypswActivity.newIntent(activity));
            }
        });


    }

    private void VERIFICATE_PAY_PASSWORD(List<Map<String, Object>> list) {

        if (list.size() > 0) {
            Map<String, Object> map = list.get(_0);
            new AlertDialog.Builder(WeexOilRechargeActivity.instance)
                    .setTitle("温馨提示")
                    .setMessage(map.get("MSG") + "")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            keyboardPopupWindow.dismiss();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            keyboardPopupWindow.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            keyboardPopupWindow.dismiss();
                            Observable.just(null)
                                    .compose(new RxPermissionImpl(WeexOilRechargeActivity.instance).ensureEach(android.Manifest.permission.CALL_PHONE))
                                    .subscribe(new Action1<Permission>() {
                                        @Override
                                        public void call(Permission permission) {
                                            if (permission.isGranted()) {
                                                String number = "4001106262";
                                                Intent intent = new Intent();
                                                intent.setAction(Intent.ACTION_CALL);
                                                intent.setData(Uri.parse("tel:" + number));
                                                WeexOilRechargeActivity.instance.startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }).show();

        } else {
            BacHttpBean httpRO = new BacHttpBean()
                    .setMethodName("RECHARGE_OIL")
                    .put("login_phone", BacApplication.getLoginPhone())
                    .put("card_no", singlePayBean.getCardNum())
                    .put("recharge_money", singlePayBean.getRecharge())
                    .put("sale_money", singlePayBean.getSaleMoney())
                    .put("voucher_id", singlePayBean.getVoucherId())
                    .put("voucher_money", singlePayBean.getVoucherCount())
                    .put("platform_name", "RECHARGE_OIL")
                    .put("pay_type", singlePayBean.getPlatform())
                    .put("pay_money", singlePayBean.getShouldPay())
                    .put("terminal_id", MODEL);

            Repository.getInstance().getData(httpRO, true, WeexOilRechargeActivity.instance)
                    .compose(WeexOilRechargeActivity.instance.getOilFragment().<String>bindUntilEvent(FragmentEvent.DESTROY))
                    .compose(new Observable.Transformer<String, String>() {
                        @Override
                        public Observable<String> call(Observable<String> observable) {
                            return observable;
                        }
                    })
                    .observeOn(RxScheduler.RxPoolScheduler())
                    .map(new JsonFunc1<String, List<Map<String, Object>>>())
                    .observeOn(Schedulers.from(AsyncTask.SERIAL_EXECUTOR))
                    .flatMap(new Func1<List<Map<String, Object>>, Observable<String>>() {
                        @Override
                        public Observable<String> call(List<Map<String, Object>> mapList) {
                            Map<String, Object> map = mapList.get(_0);

                            BacHttpBean httpPay = new BacHttpBean()
                                    .setMethodName("PAY")
                                    .put("platform_name", map.get("platform_name"))
                                    .put("pay_type", map.get("pay_type"))
                                    .put("order_id", map.get("order_id"))
                                    .put("content", "油卡充值：" + map.get("recharge_money") + "元")
                                    .put("pay_money", map.get("pay_money"));
                            return Repository.getInstance().getData(httpPay, true, WeexOilRechargeActivity.instance);
                        }
                    })
                    .observeOn(RxScheduler.RxPoolScheduler())
                    .map(new JsonFunc1<String, List<Map<String, Object>>>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<Map<String, Object>>>() {
                        @Override
                        public void call(List<Map<String, Object>> mapList) {
                            RECHARGE_OIL(mapList);
                        }
                    });
        }
    }


    private void showKeyboardData() {
        HttpHelper.getInstance().fragmentAutoLifeAndLoading(WeexOilRechargeFragment.ins,
                new BacHttpBean().setMethodName("BASEXML.QUERY_KEYBOARD")
                        .put("login_phone", BacApplication.getLoginPhone()))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<String>>() {
                    @Override
                    public List<String> call(String s) {
                        List<Map<String, String>> list = JSON.parseObject(s, new TypeReference<List<Map<String, String>>>() {
                        }.getType());
                        return JSON.parseObject(list.get(_0).get("keyboard_value"), new TypeReference<List<String>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> list) {
                        keyboardPopupWindow.showData(list);
                    }
                });
    }


    public void getLastCardNum(final JSCallback callback) {

        // sp 中没有
        HttpHelper.getInstance()
                .bacNet(new BacHttpBean()
                        .setMethodName("CARD_XML.QUERY_LAST_CARD")
                        .put("login_phone", BacApplication.getLoginPhone()))
                .compose(WeexOilRechargeActivity.instance.getOilFragment().<String>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(Schedulers.from(AsyncTask.SERIAL_EXECUTOR))
                .flatMap(new Func1<List<Map<String, Object>>, Observable<List<Map<String, Object>>>>() {
                    @Override
                    public Observable<List<Map<String, Object>>> call(List<Map<String, Object>> maps) {

                        if (maps != null && maps.size() > 0) {
                            return Observable.just(maps);
                        } else {
                            return HttpHelper.getInstance().bacNet(new BacHttpBean()
                                    .setMethodName("QUERY_USE_VOUCHER")
                                    .put("login_phone", BacApplication.getLoginPhone()))
                                    .compose(WeexOilRechargeActivity.instance.getOilFragment().<String>bindUntilEvent(FragmentEvent.DESTROY))
                                    .observeOn(RxScheduler.RxPoolScheduler())
                                    .map(new Func1<String, List<Map<String, Object>>>() {
                                        @Override
                                        public List<Map<String, Object>> call(String s) {
                                            return JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                                            }.getType());
                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map(new Func1<List<Map<String, Object>>, List<Map<String, Object>>>() {
                                        @Override
                                        public List<Map<String, Object>> call(List<Map<String, Object>> maps) {

                                            int size = maps.size();
                                            if (size > 0) {
                                                new AlertDialog.Builder(WeexOilRechargeActivity.instance)
                                                        .setTitle("温馨提示")
                                                        .setMessage("您还有 " + size + " 张加油优惠券未使用")
                                                        .setPositiveButton("取消", null)
                                                        .setNegativeButton("确认", null)
                                                        .show();
                                            }
                                            return null;
                                        }
                                    });
                        }
                    }
                })
                .filter(new Func1<List<Map<String, Object>>, Boolean>() {
                    @Override
                    public Boolean call(List<Map<String, Object>> maps) {
                        return maps != null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        callback.invokeAndKeepAlive(maps.get(0).get("card_no") + "");
                    }
                })
        ;
    }

    private final int REQUEST_CODE = 2111;

    //  优惠券点击
    public void onVoucherListClick(JSCallback callback) {
        Context context = WeexOilRechargeActivity.instance;
        if (voucherList != null && voucherList.size() > 0) {
            WeexOilRechargeActivity.instance.setJScallback(callback);//传送callback
            // 确定油券显示界面
            Intent intent = OilVoucherActivity.newIntent(context);
            intent.putExtra("REQUEST_CODE", REQUEST_CODE);
            intent.putParcelableArrayListExtra(OilVoucherActivity.VOUCHER, (ArrayList<? extends Parcelable>) voucherList);
            // 从fragment 启动activity
            WeexOilRechargeActivity.instance.startActivityForResult(intent, REQUEST_CODE);
            WeexOilRechargeActivity.instance.overridePendingTransition(R.anim.cl_slide_right_in, R.anim.cl_slide_left_out);
        } else {
            new AlertDialog.Builder(context)
                    .setTitle("温馨提示")
                    .setMessage("没有可使用券")
                    .setPositiveButton("确定", null)
                    .show();
        }
    }
}
