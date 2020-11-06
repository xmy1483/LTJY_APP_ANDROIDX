package com.bac.bacplatform.module.bills;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.widget.ListView;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.bills.adapter.QueryPhoneAdapter;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

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
public class QueryPhoneActivity extends SuperActivity {

    private ListView query_phone_listview;
    private QueryPhoneAdapter mQueryPhoneAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bills_activity);

        initToolBar("话费账单");


        query_phone_listview = (ListView) findViewById(R.id.bill_listview);

        // 选中拖动的背景色
        query_phone_listview.setCacheColorHint(Color.TRANSPARENT);

        // 设置选中时为背景色透明
        query_phone_listview.setSelector(new ColorDrawable(Color.TRANSPARENT));

        mQueryPhoneAdapter = new QueryPhoneAdapter(this);
        query_phone_listview.setAdapter(mQueryPhoneAdapter);
        phoneSearch();
    }


    private void phoneSearch() {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setMethodName("phone.SELECT_RECHARGE_PHONE_RECORD")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("business_status", 1))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {

                        if (mapList != null && mapList.size() > 0) {
                            List<Map<String, Object>> query_phone_listMap = mQueryPhoneAdapter.getQuery_phone_listMap();
                            query_phone_listMap.clear();
                            query_phone_listMap.addAll(mapList);
                            mQueryPhoneAdapter.notifyDataSetChanged();
                        } else {
                            new AlertDialog.Builder(QueryPhoneActivity.this)
                                    .setTitle("骆驼加油").setMessage("暂无账单")
                                    .setNegativeButton("取消", null)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            QueryPhoneActivity.this.onBackPressed();
                                        }
                                    })
                                    .show();
                        }
                    }
                });

    }
}
