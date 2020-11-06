package com.bac.bacplatform.old.module.bihupapa;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.bihupapa.domain.CarAdvInfoBean;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.jakewharton.rxbinding.view.RxView;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.utils.tools.CountDown.format2;
import static com.bac.bacplatform.utils.tools.CountDown.getDayOfWeek;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.voucher
 * 创建人：Wjz
 * 创建时间：2017/2/17
 * 类描述：
 */

public class QueryCarAdvInfoActivity extends SuperActivity {

    private LinearLayout ll01;
    private ImageView iv01;
    private TextView tv01;
    private LinearLayout ll02;
    private ImageView iv02;
    private TextView tv02;
    private TextView tv021;
    private LinearLayout ll03;
    private ImageView iv03;
    private TextView tv03;
    private TextView tv031;
    private CarAdvInfoBean mCarAdvInfoBean;
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_adv_info_activity);

        initToolBar("进度");

        ll01 = (LinearLayout) findViewById(R.id.ll_01);
        iv01 = (ImageView) findViewById(R.id.iv_01);
        tv01 = (TextView) findViewById(R.id.tv_01);
        ll02 = (LinearLayout) findViewById(R.id.ll_02);
        iv02 = (ImageView) findViewById(R.id.iv_02);
        tv02 = (TextView) findViewById(R.id.tv_02);
        tv021 = (TextView) findViewById(R.id.tv_021);
        ll03 = (LinearLayout) findViewById(R.id.ll_03);
        iv03 = (ImageView) findViewById(R.id.iv_03);
        tv03 = (TextView) findViewById(R.id.tv_03);
        tv031 = (TextView) findViewById(R.id.tv_031);

        tv031.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv031.getPaint().setAntiAlias(true);//抗锯齿


        btn = (Button) findViewById(R.id.btn);
        btn.setVisibility(View.GONE);

        RxView.clicks(btn)
                .throttleFirst(Constants.SECOND_2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // 启动上传页面
                        UIUtils.startActivityInAnimAndFinishSelf(activity, new Intent(QueryCarAdvInfoActivity.this, CarAdvCollectActivity.class).putExtra("car_adv", mCarAdvInfoBean));
                    }
                });

        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        mCarAdvInfoBean = intent.getParcelableExtra("car_adv");
        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_ADV_INFO")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("adv_id", mCarAdvInfoBean.getAdv_id())
                .put("adv_title", mCarAdvInfoBean.getAdv_title()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
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

                                int status = (int) map.get("status");

                                Calendar instance = Calendar.getInstance();
                                Object auti_remark = map.get("auti_remark");
                                switch (status) {

                                    case 50://审核不通过

                                        showTopView(R.mipmap.deliver_will_fail, map, instance);
                                        showMidView(R.mipmap.deliver_fail, map, instance, auti_remark == null ? "" : StringUtil.isNullOrEmpty(auti_remark), "审核失败");
                                        btn.setVisibility(View.VISIBLE);
                                        btn.setText("重新提交");
                                        break;
                                    case 10://提交成功
                                        showTopView(R.mipmap.deliver_icon, map, instance);
                                        break;
                                    case 30:

                                        showTopView(R.mipmap.deliver_success, map, instance);
                                        showMidView(R.mipmap.deliver_icon, map, instance, auti_remark == null ? "" : StringUtil.isNullOrEmpty(auti_remark), "审核成功");

                                        break;
                                    case 40://已发送酬金
                                        showTopView(R.mipmap.deliver_success, map, instance);
                                        showMidView(R.mipmap.deliver_icon, map, instance, StringUtil.isNullOrEmpty(map.get("award_money"))
                                                + "元已到账，请前往揩油宝查看", "酬金发放成功");
                                        break;
                                    default:
                                        showTopView(R.mipmap.deliver_icon, map, instance);
                                        break;
                                }

                            } else if (code == 1) {

                                ll01.setVisibility(View.VISIBLE);
                                iv01.setImageResource(R.mipmap.deliver_fail);
                                ((TextView)findViewById(R.id.tv_00)).setText(map.get("msg")+"");

                            } else if (code == -2) {
                                new AlertDialog.Builder(QueryCarAdvInfoActivity.this)
                                        .setMessage(String.valueOf(map.get("msg")))
                                        .setNegativeButton("取消", null)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                QueryCarAdvInfoActivity.this.onBackPressed();
                                            }
                                        }).show();
                            }
                        }
                    }
                });

    }

    private void showMidView(int viewId, Map<String, Object> map, Calendar instance, String label, String title) {
        instance.setTimeInMillis((long) map.get("create_time"));

        ll02.setVisibility(View.VISIBLE);
        iv02.setImageResource(viewId);
        tv02.setText(format2.format(instance.getTime()));
        tv02.append(" 星期");

        tv02.append(getDayOfWeek(instance.get(Calendar.DAY_OF_WEEK)));
        tv02.append("\n".concat(label));

        tv021.setText(title);

    }

    private void showTopView(int viewId, Map<String, Object> map, Calendar instance) {
        ll01.setVisibility(View.VISIBLE);
        iv01.setImageResource(viewId);
        //submit_time
        instance.setTimeInMillis((long) map.get("create_time"));
        tv01.setText(format2.format(instance.getTime()));
        tv01.append(" 星期");
        tv01.append(getDayOfWeek(instance.get(Calendar.DAY_OF_WEEK)));
        tv01.append("\n车身广告资料提交成功");
        tv01.append("\n等待审核，成功后发放酬金");
    }

}
