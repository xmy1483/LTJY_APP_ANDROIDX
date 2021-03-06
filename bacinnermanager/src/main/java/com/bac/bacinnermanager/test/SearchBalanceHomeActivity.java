package com.bac.bacinnermanager.test;

import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.bac.bacinnermanager.R;
import com.bac.commonlib.param.CommonParam;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by wujiazhen on 2017/8/18.
 */

public class SearchBalanceHomeActivity extends AppCompatActivity {

    private TextView tv;
    private SearchBalanceApi api;

    private int currentPage;
    private List<Integer> list = new ArrayList<>();
    private int i;
    private String nextStep;

    private int j;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_search_balance_home_activity);

        tv = findViewById(R.id.tv);

        currentPage = getIntent().getIntExtra("currentPageInt", 1);
        i = getIntent().getIntExtra("currentCountInt", 0);
        nextStep = getIntent().getStringExtra("nextStep");

        api = CommonParam.getInstance().getRetrofit().create(SearchBalanceApi.class);

/*

        // 0 1 2
        Observable.interval(0, 2, TimeUnit.SECONDS, RxScheduler.RxPoolScheduler())
                .take(10) // 10 -> 0-9  10000 -> 0-9999
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        System.out.println(aLong);
                    }
                });
*/


      /* String[] arr = {"1","2","3"};

        Observable.from(arr)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });*/
        try {
            doSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }


       /* getPhoneBalance("13400050000").subscribeOn(RxScheduler.RxPoolScheduler()).subscribe(new Action1<Map<String, Object>>() {
            @Override
            public void call(Map<String, Object> map) {

            }
        });*/
    }

    private void doSearch() {//int currentPage,int pageSize

        // 1. 查询手机 号码 前缀  ,当前页
        // 2. 10000 次计数 ,然后停止
        // 3. 获取下一次 计数
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                list.clear();
                for (; i <= 10000; i++) {
                    list.add(i);
                }
                subscriber.onNext("");
            }
        }).flatMap(new Func1<Object, Observable<?>>() {
            @Override
            public Observable<?> call(Object o) {
                return getPhonePrefix("南京", "移动", currentPage, 1)
                        .observeOn(RxScheduler.RxPoolScheduler())
                        .map(new Func1<List<Map<String, Object>>, String>() {
                            @Override
                            public String call(List<Map<String, Object>> maps) {
                                // 拼接完成  1340005   0000-9999  共 10000次数
                                return maps.get(0).get("match_code") + "";
                            }
                        })
                        .flatMap(new Func1<String, Observable<?>>() {
                            @Override
                            public Observable<Integer> call(final String s) {
                                return Observable.from(list)
                                        .doOnNext(new Action1<Integer>() {
                                            @Override
                                            public void call(Integer integer) {
                                                SystemClock.sleep(2000);
                                                if (integer < 10000) {
                                                    String suffix = integer + "";
                                                    System.out.println("suffix-1:" + suffix);
                                                    if (integer < 10) { // 0 - 9
                                                        suffix = "000" + suffix;
                                                    } else if (integer < 100) { // 10 - 99
                                                        suffix = "00" + suffix;
                                                    } else if (integer < 1000) { // 100 - 999
                                                        suffix = "0" + suffix;
                                                    }
                                                    System.out.println("suffix-2:" + suffix);
                                                    final String finalSuffix = suffix;

                                                    getPhoneBalance(s + suffix)
                                                            .observeOn(RxScheduler.RxPoolScheduler())
                                                            //getPhoneBalance("13641536078")
                                                            .flatMap(new Func1<Map<String, Object>, Observable<String>>() {
                                                                @Override
                                                                public Observable<String> call(Map<String, Object> mapB) {
                                                                    /*
                                                                    {
                                                                        "BALANCE":13922,"CODE":"0","VERSION":"1.0",
                                                                            "flag":0,"json":"true","limitFee":"30000",
                                                                            "msg":"success","payFee":11922,
                                                                            "remainFee":11922,"useFee":"0"
                                                                    }
                                                                    */
                                                                    //http://localhost:8080/servlet/phoneNumSaveServlet?
                                                                    // phone=136314350678&
                                                                    // payFee=100.0&
                                                                    // balance=110.0&
                                                                    // city=%E5%8D%97%E4%BA%AC&
                                                                    // type=%E7%A7%BB%E5%8A%A8&
                                                                    // prefix=1364153&
                                                                    // limitFee=300

                                                                    // 查询 保存 手机号 信息
                                                                    HashMap<String, Object> map = new HashMap<>();
                                                                    map.put("phone", s + finalSuffix);
                                                                    map.put("payFee", mapB.get("payFee"));
                                                                    map.put("balance", mapB.get("BALANCE"));
                                                                    map.put("city", "南京");
                                                                    map.put("type", "移动");
                                                                    map.put("prefix", s);
                                                                    map.put("limitFee", mapB.get("limitFee"));
                                                                    return savePhoneBalance(map);
                                                                }
                                                            })
                                                            .subscribeOn(RxScheduler.RxPoolScheduler())
                                                            .retry()
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(new Action1<String>() {
                                                                @Override
                                                                public void call(String s) {
                                                                    // 显示数据
                                                                    tv.setText("当前已查询："+(++j)+" 次");
                                                                }
                                                            });
                                                } else {

                                                    if ("asc".equals(nextStep)) {
                                                        currentPage++;
                                                    } else if ("desc".equals(nextStep)) {
                                                        currentPage--;
                                                    } else {
                                                        currentPage++;
                                                    }

                                                    // 还原数据
                                                    i = 0;

                                                    doSearch();
                                                }
                                            }
                                        });
                            }
                        });
            }
        })
                .subscribeOn(RxScheduler.RxPoolScheduler())
                .subscribe();
    }


    /**
     * 保存 查询 结果
     *
     * @param map
     */
    private Observable<String> savePhoneBalance(HashMap<String, Object> map) {
        return api.savePhoneBalance(map);
    }

    /**
     * 查询 手机号 话费
     */
    private Observable<Map<String, Object>> getPhoneBalance(String phone) {

        /*
                        {
                        "BALANCE":13922,"CODE":"0","VERSION":"1.0",
                        "flag":0,"json":"true","limitFee":"30000",
                        "msg":"success","payFee":11922,
                        "remainFee":11922,"useFee":"0"
                        }
                        */

        // 查询 手机号码  字段
        HashMap<String, Object> map = new HashMap<>();
        map.put("uterminalid", phone);
        map.put("type", "1");
        map.put("version", "1.0");
        return api.getPhoneBalance(map);
    }

    /**
     * 查询 手机号前缀
     *
     * @param city        城市
     * @param type        类型
     * @param currentPage 当前页 从 1 开始
     * @param pageSize    每页大小
     * @return
     */
    private Observable<List<Map<String, Object>>> getPhonePrefix(String city, String type, int currentPage, int pageSize) {

        //{operator=移动全球通卡, match_code=1340005, city=江苏 南京市}
        // 查询 手机号码  字段
        HashMap<String, Object> map = new HashMap<>();
        map.put("city", city);
        map.put("type", type);
        map.put("currentPage", currentPage);
        map.put("pageSize", pageSize);
        return api.getPhonePrefix(map);
    }
}
