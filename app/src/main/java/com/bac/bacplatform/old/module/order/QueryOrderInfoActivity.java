package com.bac.bacplatform.old.module.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.bac.bacplatform.utils.tools.CountDown.format2;
import static com.bac.bacplatform.utils.tools.CountDown.getDayOfWeek;


/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.voucher
 * 创建人：Wjz
 * 创建时间：2017/2/17
 * 类描述：
 */

public class QueryOrderInfoActivity extends SuperActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_query_order_info);

        initToolBar("办卡进度");

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

        try {
            initData();
        } catch (Exception e) {
        }
    }

    private void initData() throws Exception{


        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUEYR_ORDER_INFO")
                .put("login_phone", BacApplication.getLoginPhone()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        Map<String, Object> map = maps.get(0);
                        if (map != null) {
                            int code = (int) map.get("code");
                            if (code == 0) {

                                int status = (int) map.get("status");

                                /*
                                1.审核通过
                                2.审核不通过
                                3.已寄出
                                4.已提交
                                5.办卡成功
                                * */
                                Calendar instance = Calendar.getInstance();
                                switch (status) {

                                    case 4:
                                        showTopView(R.mipmap.deliver_icon, map, instance);
                                        break;
                                    case 2:
                                        showTopView(R.mipmap.deliver_will_fail, map, instance);
                                        showMidView(R.mipmap.deliver_fail, map, instance, String.valueOf(map.get("auth_remark")), "审核失败");

                                        break;
                                    case 1://审核通过
                                        showTopView(R.mipmap.deliver_success, map, instance);
                                        showMidView(R.mipmap.deliver_icon, map, instance, "您的加油卡号\n".concat(String.valueOf(map.get("owner_idcard"))), "审核成功");
                                        break;
                                    case 3:
                                        showTopView(R.mipmap.deliver_success, map, instance);
                                        showMidView(R.mipmap.deliver_success, map, instance, "您的加油卡号\n".concat(String.valueOf(map.get("owner_idcard"))), "审核成功");

                                        instance.setTimeInMillis((long) map.get("delivery_time"));
                                        ll03.setVisibility(View.VISIBLE);
                                        iv03.setImageResource(R.mipmap.deliver_icon);
                                        tv03.setText(format2.format(instance.getTime()));
                                        tv03.append(" 星期");
                                        tv03.append(getDayOfWeek(instance.get(Calendar.DAY_OF_WEEK)));
                                        tv03.append("\n".concat(String.valueOf(map.get("express_company_name"))));
                                        tv03.append(" 快递号：");
                                        String express_no = String.valueOf(map.get("express_no"));
                                        tv03.append(express_no);
                                        tv031.setText("快递查询");
                                        final String url = "http://m.kuaidi100.com/result.jsp?nu=".concat(express_no);
                                        tv031.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(QueryOrderInfoActivity.this, WebAdvActivity.class);
                                                intent.putExtra("title", "快递查询");
                                                intent.putExtra("urltype", "order");
                                                intent.putExtra("ads_url", url);
                                                UIUtils.startActivityInAnim(activity, intent);
                                            }
                                        });
                                        break;
                                    default:
                                        showTopView(R.mipmap.deliver_icon, map, instance);
                                        break;
                                }

                            } else if (code == -2) {
                                new AlertDialog.Builder(QueryOrderInfoActivity.this)
                                        .setMessage(String.valueOf(map.get("msg")))
                                        .setNegativeButton("取消", null)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                QueryOrderInfoActivity.this.onBackPressed();
                                            }
                                        }).show();
                            }
                        }
                    }
                });

    }

    private void showMidView(int viewId, Map<String, Object> map, Calendar instance, String label, String title) {
        instance.setTimeInMillis((long) map.get("auth_time"));
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
        instance.setTimeInMillis((long) map.get("submit_time"));
        tv01.setText(format2.format(instance.getTime()));
        tv01.append(" 星期");
        tv01.append(getDayOfWeek(instance.get(Calendar.DAY_OF_WEEK)));
        tv01.append("\n办卡资料提交成功");
    }
}
