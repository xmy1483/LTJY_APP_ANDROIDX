package com.bac.bacinnermanager;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacinnermanager.utils.DataBean;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.http.HttpHelperLib;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by guke on 2017/8/15.
 */

public class SecondActivity extends AppCompatActivity {
    private Button btn;
    private ListView listview;


    public static int getPosition1() {
        return position1;
    }

    private static int position1;

    public static List<String> getData() {
        return data;
    }

    private static List<String> data = new ArrayList<>();
    private static List<Integer> data2 = new ArrayList<>();

    public static List<String> getData3() {
        return data3;
    }

    //接口名
    private static List<String> data3 = new ArrayList<>();

    private static List<DataBean> mydata = new ArrayList<>();

    public static List<DataBean> getMydata() {
        return mydata;
    }

    public static List<String> getKeys() {
        return keys;
    }

    public static List<String> getValues() {
        return values;
    }

    private static List<String> keys = new ArrayList<>();
    private static List<String> values = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private static String key = "";
    private static String value = "";
    private static String type = "";
    private static String remark = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initData();

    }


    /**
     * @return
     */
    private void initData() {

        HttpHelperLib.getInstance()
                .net(new BacHttpBean().setMethodName("BHPP_BASE.GET_METHOD"), null, null,null,this)
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<Map<String, Object>>>() {
                    @Override
                    public List<Map<String, Object>> call(String s) {
                        List<Map<String, Object>> list = JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                        }.getType());
                        data.clear();
                        for (Map<String, Object> map : list) {
                            data.add(map.get("method_name").toString());
                            data2.add((Integer) map.get("method_id"));
                            data3.add((String) map.get("method"));
                        }
                        adapter = new ArrayAdapter<String>(SecondActivity.this, android.R.layout.simple_expandable_list_item_1, data);
//                        adapter.notifyDataSetChanged();

                        return list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        // Map<String, Object> stringObjectMap = maps.get(0);//0


//                        adapter = new ArrayAdapter<String>(SecondActivity.this, android.R.layout.simple_expandable_list_item_1, data);
                        listview = findViewById(R.id.list_view);
                        listview.setAdapter(adapter);
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                position1 = position;
                                BacHttpBean bacHttpBean = new BacHttpBean().setMethodName("BHPP_BASE.GET_METHOD_PARAM")
                                        .put("method_id", data2.get(position));
                                HttpHelperLib.getInstance()
                                        .net(bacHttpBean, null, null,null,null)
                                        .observeOn(RxScheduler.RxPoolScheduler())
                                        .map(new Func1<String, List<DataBean>>() {
                                            @Override
                                            public List<DataBean> call(String s) {
                                                return JSON.parseObject(s, new TypeReference<List<DataBean>>() {
                                                }.getType());
                                            }
                                        })
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Action1<List<DataBean>>() {
                                            @Override
                                            public void call(List<DataBean> been) {
                                                mydata = been;
                                                startActivity(new Intent(SecondActivity.this, ThirdActivity.class).putExtra("Bean", (Serializable) been));
                                            }
                                        });

                            }
                        });
                    }
                });


    }
}
