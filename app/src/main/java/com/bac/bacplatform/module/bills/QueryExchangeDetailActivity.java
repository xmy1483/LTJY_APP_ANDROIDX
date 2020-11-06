package com.bac.bacplatform.module.bills;


import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.alibaba.fastjson.JSON;
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
 * 包名：com.bac.bacplatform.activity.exchange
 * 创建人：Wjz
 * 创建时间：2016/9/23
 * 类描述：
 */

public class QueryExchangeDetailActivity extends AutomaticBaseActivity {
    List<Map<String, Object>> luckListMap = new ArrayList<Map<String, Object>>() {
    };
   private ListView listView;
private SimpleAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Integer> intParam = new ArrayList<>();
        intParam.add(1);
        intParam.add(3);

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setMethodName("QUERY_CHARGE_ORDER")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("status", intParam))

                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {

                        if (maps.size() > 0) {
//                            for (int i = 0;i<maps.size();i++){
//                                Object tmp=maps.get(i).get("arrive_time");
//                                //if(tmp instanceof Date){
//                                    maps.get(i).put("arrive_time", format_yyyy_mm_dd_hh_mm_ss1.format(tmp));
//                                //}
//                                tmp=maps.get(i).get("create_time");
//                                //if(tmp instanceof Date){
//                                    maps.get(i).put("create_time", format_yyyy_mm_dd_hh_mm_ss1.format(tmp));
//                                //}
//
//                                //format_yyyy_mm_dd_hh_mm_ss1.format(item.get("pay_time"))
//                            }
                            luckListMap=maps;
                            System.out.println(JSON.toJSONString(luckListMap)+"");



                            mAdapter = new SimpleAdapter(QueryExchangeDetailActivity.this,luckListMap,R.layout.hfhyzhangdan,
                                    new String[] {"pay_money","charge_phone","pay_money","arrive_time_str","order_id","create_time_str"},
                                    new int[] {R.id.money1,R.id.phone,R.id.money2,R.id.time1,R.id.id,R.id.time2});
                            listView = (ListView) findViewById(R.id.listview);

                            listView.setAdapter(mAdapter);

                            // mQueryExchangeDetailAdapter.notifyDataSetChanged();
                        } else {
                            new AlertDialog.Builder(QueryExchangeDetailActivity.this)
                                    .setTitle("骆驼加油").setMessage("暂无账单")
                                    .setNegativeButton("取消", null)
                                    .setPositiveButton("确定", null).show();

                        }
                    }
                });
        setContentView(R.layout.layout_hyzhangdan);
        initToolBar("交易明细");


    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initFragment() {

    }



}
