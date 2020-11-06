package com.bac.bacplatform.module.bills;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.SubPersonal
 * 创建人：Wjz
 * 创建时间：2016/8/9
 * 类描述：
 */
public class QueryFlowActivity extends AutomaticBaseActivity {

    private ListView listView;
    private List<Map<String, Object>> listmap;
    private SimpleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setMethodName("QUERY_FLUX_ORDER")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("status", list))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {
                        if (mapList != null && mapList.size() > 0) {
                            mAdapter = new SimpleAdapter(QueryFlowActivity.this,mapList,R.layout.liuliang,
                                    new String[] {"price","product_name","recharge_phone","create_time_str","advance_time_str"},
                                    new int[] {R.id.money,R.id.liuliang,R.id.phone,R.id.creattime,R.id.time});
                            listView = (ListView) findViewById(R.id.bill_listview);
                            listView.setAdapter(mAdapter);
                        } else {
                            new AlertDialog.Builder(QueryFlowActivity.this)
                                    .setTitle("骆驼加油").setMessage("暂无账单")
                                    .setNegativeButton("取消", null).setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.onBackPressed();
                                        }
                                    })
                                    .show();

                        }
                    }
                });
        setContentView(R.layout.bills_activity);

        initToolBar("流量账单");


    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initFragment() {

    }



}
