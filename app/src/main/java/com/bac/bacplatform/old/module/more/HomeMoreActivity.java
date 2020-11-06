package com.bac.bacplatform.old.module.more;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.main.view.HomeFragment;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity;
import com.bac.bacplatform.old.module.phone.ActivityPhone1;
import com.bac.bacplatform.old.module.preferential.PreferentialActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.commonlib.utils.ui.UIUtil;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.wjz.weexlib.weex.activity.WeexActivity2;

import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.bac.bacplatform.utils.tools.Util.callUs;
import static com.bac.bacplatform.utils.ui.UIUtils.startActivityInAnim;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity
 * 创建人：Wjz
 * 创建时间：2016/9/26
 * 类描述：
 * 12.11修改，首页更多中，修改布局大屏适配
 */

public class HomeMoreActivity extends SuperActivity {

    // private RecyclerView rvExchange;
    private View img_1;
    private View img_2;
    private View img_3;
    private View img_4;
    private View img_5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_more_activity);
        //  rvExchange = (RecyclerView) findViewById(R.id.rv_exchange);
        initToolBar("更多");
        img_1 = findViewById(R.id.img_first);
        img_2 = findViewById(R.id.img_second);
        img_3 = findViewById(R.id.img_third);
        img_4 = findViewById(R.id.img_forth);
        img_5 = findViewById(R.id.img_fifth);

//苏果卡
        findViewById(R.id.img_sgk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment.getInstance().insurance_buy.performClick();
            }
        });
//etc
        findViewById(R.id.etc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment.getInstance().sgcard.performClick();
            }
        });

//        免费领取etc
        findViewById(R.id.etc_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment.getInstance().jdcard.performClick();
            }
        });
//        爱车估值
        findViewById(R.id.acgz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment.getInstance().phone.performClick();
            }
        });

//        车贴zq
        findViewById(R.id.ctzq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment.getInstance().flow.performClick();
            }
        });

        img_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUs(HomeMoreActivity.this);
            }
        });

        img_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //办卡进度
//                startActivityInAnim(HomeMoreActivity.this, new Intent(HomeMoreActivity.this,
//                        QueryOrderInfoActivity.class));
                HttpHelper.getInstance().activityAutoLifeAndLoading(HomeMoreActivity.this,
                        new BacHttpBean()
                                .setMethodName("QUERY_START_PARAM")
                                .put("login_phone", BacApplication.getLoginPhone())
                                .put("param_type", "FAQ"))

                        .observeOn(RxScheduler.RxPoolScheduler())
                        .map(new JsonFunc1<String, List<Map<String, String>>>())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Action1<List<Map<String, String>>>() {
                                    @Override
                                    public void call(List<Map<String, String>> maps) {
                                        startActivityInAnim(activity,
                                                new Intent(activity, WebAdvActivity.class)
                                                        .putExtra("title", "常见问题")
                                                        .putExtra("ads_url", maps.get(0).get("param_value")));
                                    }
                                }
                        );
            }
        });

        img_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.startActivityInAnim(activity, new Intent(HomeMoreActivity.this, ActivityPhone1.class));
            }
        });

        img_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtil.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
                        .setData(Uri.parse("https://app5.bac365.com:10443/app.pay/photo/weex/Card/dist/giftcardsPurchase.js")));
                //.setData(Uri.parse("https://app5.bac365.com:10443/weex/jd/JD_card/giftcardsPurchase.js")));
            }
        });
        img_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.startActivityInAnim(activity, new Intent(activity, PreferentialActivity.class));
            }
        });
//        initData();
//        initEvent();
    }

//    private void initData() {
//        ArrayList<TitleAndIconBean> titleList = new ArrayList<>();
//
//        int moreIcon[] = {
//                R.mipmap.more_contactus, R.mipmap.more_issue, R.mipmap.more_phone, R.mipmap.jingdong
//        };
//        for (int i = 0; i < moreIcon.length; i++) {
//            TitleAndIconBean titleAndIconBean = new TitleAndIconBean(String.valueOf(i), moreIcon[i], i);
//            titleList.add(titleAndIconBean);
//        }
//
//        rvExchange.setLayoutManager(new GridLayoutManager(this, 3));
//        HomeFragmentAdapter homeFragmentAdapter = new HomeFragmentAdapter(R.layout.home_more_item, titleList);
//        rvExchange.setAdapter(homeFragmentAdapter);
//    }
//
//    private void initEvent() {
//        rvExchange.addOnItemTouchListener(new OnItemClickListener() {
//            @Override
//            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                switch (position) {
//
//                    case 0://问题
//                        callUs(HomeMoreActivity.this);
//                        break;
//
//                    case 1:
//
////                        UIUtils.startActivityInAnim(HomeMoreActivity.this, new Intent(HomeMoreActivity.this,
////                                QueryOrderInfoActivity.class));
//                        HttpHelper.getInstance().activityAutoLifeAndLoading(HomeMoreActivity.this,
//                                new BacHttpBean()
//                                        .setMethodName("QUERY_START_PARAM")
//                                        .put("login_phone", BacApplication.getLoginPhone())
//                                        .put("param_type", "FAQ"))
//
//                                .observeOn(RxScheduler.RxPoolScheduler())
//                                .map(new JsonFunc1<String, List<Map<String, String>>>())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(
//                                        new Action1<List<Map<String, String>>>() {
//                                            @Override
//                                            public void call(List<Map<String, String>> maps) {
//                                                startActivityInAnim(activity,
//                                                        new Intent(activity, WebAdvActivity.class)
//                                                                .putExtra("title", "常见问题")
//                                                                .putExtra("ads_url", maps.get(_0).get("param_value")));
//                                            }
//                                        }
//                                );
//                        break;
//
//                    case 2:
//                        UIUtils.startActivityInAnim(activity, new Intent(HomeMoreActivity.this, ActivityPhone1.class));
//
//                        break;
//                    case 3:
//                        UIUtil.startActivityInAnim(activity, new Intent(activity, WeexActivity2.class)
//                                .setData(Uri.parse("https://app5.bac365.com:10443/weex/jd/JD_card/giftcardsPurchase.js")));
//
//                }
//            }
//        });
//    }
//

}

