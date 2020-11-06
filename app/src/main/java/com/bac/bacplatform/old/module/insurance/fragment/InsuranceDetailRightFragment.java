package com.bac.bacplatform.old.module.insurance.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperFragment;
import com.bac.bacplatform.old.module.insurance.adapter.DetailRightAdapter;
import com.bac.bacplatform.old.module.insurance.domain.DetailRightBean;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceHomeBean;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.FragmentEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.utils.tools.CountDown.format2;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.fragment.insurance
 * 创建人：Wjz
 * 创建时间：2017/1/10
 * 类描述：
 */

public class InsuranceDetailRightFragment extends SuperFragment {

    private InsuranceHomeBean mBean;
    private RecyclerView mRv;
    private List<DetailRightBean> list    = new ArrayList<>();
    private List<DetailRightBean> primary = new ArrayList<>();
    private List<DetailRightBean> extra   = new ArrayList<>();
    private DetailRightAdapter mAdapter;
    private TextView mTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBean = (InsuranceHomeBean) getArguments().get("bean");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insurance_detial_right_fragment, container, false);
        mRv = (RecyclerView) view.findViewById(R.id.rv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initEvent();
    }

    private void initData() {

        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new DetailRightAdapter(list);
        View footer = View.inflate(getActivity(), R.layout.insurance_detail_footer_2_item, null);
        mTv = (TextView) footer.findViewById(R.id.tv_footer);
        mAdapter.addFooterView(footer);
        mRv.setAdapter(mAdapter);

        doNetQueryOrderDetail();
    }

    private void initEvent() {

    }

    private void doNetQueryOrderDetail() {

        HttpHelper.getInstance().bacNet( new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_ORDER_DETAIL")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("order_id", mBean.getOrder_id())
                .put("prv_id", mBean.getPrv_id()))
                .compose(this.<String>bindUntilEvent(FragmentEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(activity))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        Map<String, Object> map = maps.get(_0);
                        if (map != null) {
                            list.clear();
                            primary.clear();
                            extra.clear();

                            //处理生效日期
                            StringBuilder sb = new StringBuilder();

                            try {
                                Object efcInsureInfo_startDate = map.get("efcInsureInfo_startDate");
                                Object efcInsureInfo_endDate   = map.get("efcInsureInfo_endDate");
                                Object bizInsureInfo_startDate = map.get("bizInsureInfo_startDate");
                                Object bizInsureInfo_endDate   = map.get("bizInsureInfo_endDate");

                                if (efcInsureInfo_startDate != null && efcInsureInfo_endDate != null) {
                                    sb.append("交强险生效日期：\n")
                                            .append(format2.format(efcInsureInfo_startDate))
                                            .append("至")
                                            .append(format2.format(efcInsureInfo_endDate));
                                }

                                if (bizInsureInfo_startDate != null && bizInsureInfo_endDate != null) {
                                    sb.append("\n商业险生效日期：\n")
                                            .append(format2.format(bizInsureInfo_startDate))
                                            .append("至")
                                            .append(format2.format(bizInsureInfo_endDate));
                                }

                            } catch (Exception e) {
                            }
                            mTv.setText(sb.toString());

                            list.add(new DetailRightBean(DetailRightBean.HEADER, null));//头

                            Object efcInsureInfo_premium1 = map.get("efcInsureInfo_premium");
                            Object taxInsureInfo_taxFee1  = map.get("taxInsureInfo_taxFee");

                            if (efcInsureInfo_premium1 != null && taxInsureInfo_taxFee1 != null) {
                                Double efcInsureInfo_premium = Double.parseDouble(efcInsureInfo_premium1.toString());
                                Double taxInsureInfo_taxFee  = Double.parseDouble(taxInsureInfo_taxFee1.toString());
                                if (efcInsureInfo_premium > 0 && taxInsureInfo_taxFee > 0) {
                                    list.add(new DetailRightBean(DetailRightBean.TITLE, "交强险／车船税", null));//交和车
                                    list.add(new DetailRightBean(DetailRightBean.CONTEXT, "efcInsureInfo_premium", map));
                                    list.add(new DetailRightBean(DetailRightBean.CONTEXT, "taxInsureInfo_taxFee", map));
                                }
                            }

                            //商业险
                            Object risk_kinds = map.get("risk_kinds");
                            if (risk_kinds != null) {

                                List<HashMap<String, Object>> mapList = JSON.parseObject(
                                        String.valueOf(risk_kinds),
                                        new TypeReference<List<HashMap<String, Object>>>() {
                                        }.getType());

                                for (HashMap<String, Object> hashMap : mapList) {
                                    switch ((int) hashMap.get("risk_type")) {
                                        case 0://主险
                                            primary.add(new DetailRightBean(DetailRightBean.CONTEXT, hashMap));
                                            break;
                                        case 1://附加
                                            extra.add(new DetailRightBean(DetailRightBean.CONTEXT, hashMap));
                                            break;
                                    }
                                }
                                if (primary.size() > 0) {
                                    primary.add(0, new DetailRightBean(DetailRightBean.TITLE, "商业险主险", null));
                                }
                                if (extra.size() > 0) {
                                    extra.add(0, new DetailRightBean(DetailRightBean.TITLE, "商业险附加险", null));
                                }
                            }
                            if (primary.size() > 1) {
                                //primary.add(new DetailRightBean(DetailRightBean.CONTEXT, "不计免赔车损险", map));
                                list.addAll(primary);
                            }
                            if (extra.size() > 1) {
                                list.addAll(extra);
                            }

                            Object bizInsureInfo_nfcPremium = map.get("bizInsureInfo_nfcPremium");
                            if (bizInsureInfo_nfcPremium != null) {
                                if (Double.parseDouble(bizInsureInfo_nfcPremium.toString()) > 0) {
                                    //显示不计包赔
                                    list.add(new DetailRightBean(DetailRightBean.TITLE, "不计免赔", null));
                                    list.add(new DetailRightBean(DetailRightBean.CONTEXT, "不计免赔", map));
                                }
                            }


                            list.add(new DetailRightBean(DetailRightBean.FOOTER, map));//尾

                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });


    }
}
