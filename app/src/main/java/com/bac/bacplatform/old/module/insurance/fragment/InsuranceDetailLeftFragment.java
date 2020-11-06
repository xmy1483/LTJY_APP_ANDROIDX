package com.bac.bacplatform.old.module.insurance.fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperFragment;
import com.bac.bacplatform.old.module.insurance.InsuranceQueryVideo;
import com.bac.bacplatform.old.module.insurance.InsuranceUploadAddress;
import com.bac.bacplatform.old.module.insurance.domain.InsuranceHomeBean;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.FragmentEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.bac.bacplatform.utils.tools.CountDown.format2;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.fragment.insurance
 * 创建人：Wjz
 * 创建时间：2017/1/10
 * 类描述：
 */

public class InsuranceDetailLeftFragment extends SuperFragment {
    private TextView tv01;
    private TextView tv02;
    private TextView tv03;
    private InsuranceHomeBean mBean;
    private LinearLayout mLl;
    private TextView tvAlter;
    private TextView tvImage;

    private Map<String, Object> hashMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBean = (InsuranceHomeBean) getArguments().get("bean");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insurance_detial_left_fragment, container, false);

        tv01 = (TextView) view.findViewById(R.id.tv_01);
        tv02 = (TextView) view.findViewById(R.id.tv_02);
        tv03 = (TextView) view.findViewById(R.id.tv_03);
        tvAlter = (TextView) view.findViewById(R.id.tv_alter);
        mLl = (LinearLayout) view.findViewById(R.id.ll);
        tvImage = (TextView) view.findViewById(R.id.tv_image);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        doNetQueryOrderDetail();
    }

    private void initData() {

    }

    private void initEvent() {
        tvAlter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //邮寄地址修改
                Intent intent = new Intent(getActivity(), InsuranceUploadAddress.class);
                UIUtils.startActivityInAnim(activity, intent.putExtra("bean", mBean)
                        .putExtra("alter", true));
                getActivity().finish();
            }
        });
        tvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InsuranceQueryVideo.class);
                UIUtils.startActivityInAnim(activity, intent.putExtra("bean", mBean));
                getActivity().finish();
            }
        });
    }

    private void doNetQueryOrderDetail() {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
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
                    public void call(List<Map<String, Object>> mapList) {
                        Map<String, Object> map = mapList.get(0);
                        if (map != null) {
                            hashMap = map;
                            StringBuilder sb = new StringBuilder();
                            sb.append("车牌号码：")
                                    .append(map.get("car_license_no"))
                                    .append("\n")
                                    .append("品牌型号：")
                                    .append(map.get("vehicle_name"))
                                    .append("\n")
                                    .append("车辆识别号：")
                                    .append(map.get("vin_code"))
                                    .append("\n")
                                    .append("发动机号：")
                                    .append(map.get("engine_no"))
                                    .append("\n")
                                    .append("初登日期：")
                                    .append(format2.format(map.get("regist_date")));
                            tv01.setText(sb.toString());

                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("车主姓名：").append(map.get("name")).append("\n")
                                    .append("身份证号：").append(map.get("idcard_no"));
                            tv02.setText(stringBuilder.toString());

                            Bundle arguments = getArguments();
                            if (arguments.getBoolean("isShow")) {

                                if (arguments.getInt("status", 1) == 1) {
                                    //是否影像上传
                                    if (arguments.getBoolean("is_upload_image")) {//true 需要，false 不需要

                                        tvImage.setVisibility(View.VISIBLE);
                                    } else {
                                        tvImage.setVisibility(View.GONE);
                                        mLl.setVisibility(View.VISIBLE);//邮寄地址
                                        tvAlter.setVisibility(View.GONE);
                                        //是否填过地址
                                        if (arguments.getBoolean("is_upload_address")) {
                                            tvAlter.setText("添加邮寄地址");
                                        } else {//已经填过
                                            //邮寄地址
                                            Object address = map.get("address");
                                            if (address != null) {
                                                tvAlter.setText("修改邮寄地址");
                                                tv03.setText("邮寄地址：");
                                                tv03.append(StringUtil.isNullOrEmpty(address).replace("##", "\n"));
                                            }
                                        }
                                    }
                                }

                            } else {
                                mLl.setVisibility(View.GONE);
                                tvAlter.setVisibility(View.GONE);
                                tvImage.setVisibility(View.GONE);
                            }
                        }
                    }
                });

    }
}
