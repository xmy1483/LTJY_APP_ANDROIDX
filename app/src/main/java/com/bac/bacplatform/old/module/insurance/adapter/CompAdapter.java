package com.bac.bacplatform.old.module.insurance.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.module.insurance.OnDestroyCallback;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.commonlib.utils.tools.CountDownTimerUtil;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.adapter
 * 创建人：Wjz
 * 创建时间：2017/3/7
 * 类描述：
 */

public class CompAdapter extends BaseQuickAdapter<Map<String, Object>, BaseViewHolder> implements OnDestroyCallback {

    private List<CountDownTimerUtil> cdtList = new ArrayList<>();

    public CompAdapter(int layoutResId, List<Map<String, Object>> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Map<String, Object> stringObjectMap) {
        Context context = baseViewHolder.itemView.getContext();
        Object flag = stringObjectMap.get("flag");
        if (flag != null) {
            baseViewHolder
                    .setVisible(R.id.ll_top, false)
                    .setVisible(R.id.ll_bottom, true)
                    .setText(R.id.tv_01, Html.fromHtml(String.format(
                            context.getString(
                                    R.string.insurance_platform_discount), "", "重要提示"
                    )))
                    .setText(R.id.tv_02, "如果您希望投保其他保险公司\n联系客服 400-110-6262 获取在线即时报价")
                    .setText(R.id.tv_03, "联系客服")
                    .addOnClickListener(R.id.ll_bottom);

        } else {
            baseViewHolder
                    .setVisible(R.id.ll_top, true)
                    .setText(R.id.tv_comp, StringUtil.isNullOrEmpty(stringObjectMap.get("prv_name")))
                    .addOnClickListener(R.id.iv_comp_right)
                    .setVisible(R.id.ll_bottom, false);
            ImageView iv = baseViewHolder.getView(R.id.iv_comp_left);

            Glide.with(context).load(stringObjectMap.get("prv_image")).into(iv);

            /*计时器*/
            CountDownTimerUtil countDownTimerUtil = countDownTimer(baseViewHolder, stringObjectMap);
            countDownTimerUtil.start();
            cdtList.add(countDownTimerUtil);
        }
    }

    private CountDownTimerUtil countDownTimer(final BaseViewHolder baseViewHolder, final Map<String, Object> stringObjectMap) {

        return new CountDownTimerUtil(119 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int count = (int) (millisUntilFinished * .001);
                baseViewHolder.setText(R.id.iv_comp_right, String.format("报价中...%ss", count));

                if (count != 0 && count % 10 == 0) {

                    getOrderInfo(baseViewHolder, stringObjectMap);
                }
            }

            @Override
            public void onFinish() {
                baseViewHolder.setText(R.id.iv_comp_right, "暂无报价");
                //stringObjectMap.put("label", 0000);
            }
        };
    }

    private void getOrderInfo(final BaseViewHolder baseViewHolder, final Map<String, Object> stringObjectMap) {
        final AutomaticBaseActivity context = (AutomaticBaseActivity) baseViewHolder.itemView.getContext();

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_ORDER_INFO")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("order_id", stringObjectMap.get("order_id"))
                .put("prv_id", stringObjectMap.get("prv_id")))
                .compose(context.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {

                        Map<String, Object> map = mapList.get(_0);
                        if (map != null) {
                            int code = (int) map.get("code");
                            if (code == 0) {
                        /*
                          1.报价成功
                          2.报价失败
                          3.报价超时
                        */
                                int status = (int) map.get("status");
                                if (status == 11) {//报价中
                                    baseViewHolder.setVisible(R.id.ll_bottom, false);
                                } else {

                                    TextView iv_comp_right = baseViewHolder.getView(R.id.iv_comp_right);

                                    //中划线并加清晰
                                    iv_comp_right.getPaint().setFlags(0);
                                    cdtList.get(baseViewHolder.getLayoutPosition()).cancel();
                                    stringObjectMap.put("map", map);
                                    baseViewHolder.setVisible(R.id.ll_bottom, true);
                                    String strRight = "";
                                    String strBottom = "";
                                    switch (status) {
                                        case 2://报价成功
                                            //抗锯齿
                                            iv_comp_right.getPaint().setAntiAlias(true);
                                            iv_comp_right.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                                            strRight = "¥ ".concat(StringUtil.isNullOrEmpty(map.get("total_premium")));
                                            strBottom = StringUtil.isNullOrEmpty(map.get("present_remark"))
                                                    .replace("##", "\n");

                                            baseViewHolder
                                                    .addOnClickListener(R.id.ll_bottom)
                                                    .setVisible(R.id.tv_01, true)
                                                    .setText(R.id.tv_01, Html.fromHtml(String.format(
                                                            context.getString(R.string.insurance_platform_discount), "平台优惠价 ",
                                                            "¥".concat(StringUtil.isNullOrEmpty(map.get("pay_money"))))))
                                                    .setText(R.id.tv_03, "立即查看")
                                                    .setVisible(R.id.tv_03, true);
                                            break;
                                        case 3://报价失败
                                            strRight = StringUtil.isNullOrEmpty(map.get("task_state_description"));
                                            strBottom = StringUtil.isNullOrEmpty(map.get("msg"));
                                            baseViewHolder.setVisible(R.id.tv_01, false)
                                                    .setVisible(R.id.tv_03, false);
                                            break;
                                        case 7://承保
                                            strRight = StringUtil.isNullOrEmpty(map.get("task_state_description"));
                                            strBottom = StringUtil.isNullOrEmpty(map.get("msg"));
                                            baseViewHolder.setVisible(R.id.tv_01, false)
                                                    .setVisible(R.id.tv_03, false);
                                            break;
                                    }
                                    iv_comp_right.setText(strRight);
                                    baseViewHolder.setText(R.id.tv_02, strBottom);
                                }
                            } else if (code == -2) {

                            }
                        }

                    }
                });

    }

    @Override
    public void onDestroyCallback() {
        for (CountDownTimerUtil countDownTimerUtil : cdtList) {
            countDownTimerUtil.cancel();
        }
    }
}
