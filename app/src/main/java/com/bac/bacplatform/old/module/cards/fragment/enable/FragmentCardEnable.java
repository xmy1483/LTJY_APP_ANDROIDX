package com.bac.bacplatform.old.module.cards.fragment.enable;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseFragment;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.module.cards.fragment.active.CardBean;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.FragmentEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.fragment
 * 创建人：Wjz
 * 创建时间：2016/10/25
 * 类描述：
 * 激活后的代金券使用和已使用过的券的显示
 */

public class FragmentCardEnable extends AutomaticBaseFragment {
    private ListView lvActivityCardPhone;

    private List<Map<String, Object>> mListMap = new ArrayList<>();
    private CopyOnWriteArrayList<Map<String, Object>> mSelectedList = new CopyOnWriteArrayList<>();
    private CardPhoneEnableAdapter mCardPhoneAdapter;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();

    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phone_cards_enable_fragment, null);
        lvActivityCardPhone = (ListView) view.findViewById(R.id.lv_activity_card_phone);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCardPhoneAdapter = new CardPhoneEnableAdapter(activity,mListMap,mSelectedList);
        lvActivityCardPhone.setAdapter(mCardPhoneAdapter);
        initEvent();
    }

    @Override
    public void onResume() {
        super.onResume();

        initData();
    }

    private void initData() {
        //请求数据
        ArrayList<Integer> intParam2 = new ArrayList<>();
        intParam2.add(2);//2.已使用

        ArrayList<Integer> intParam = new ArrayList<>();
        intParam.add(0);//2.已使用
        intParam.add(1);//2.已使用
        intParam.add(2);//2.已使用

        //查询券

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_VOUCHER")
                .put("login_phone", bundle.getString("login_phone"))
                .put("voucher_type", intParam)
                .put("status", intParam2))
                .compose(this.<String>bindUntilEvent(FragmentEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(activity))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<Map<String, Object>>>() {
                    @Override
                    public List<Map<String, Object>> call(String s) {
                        List<Map<String, Object>> mapList = JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                        }.getType());

                        mSelectedList.clear();
                        for (Map<String, Object> map : mapList) {
                            Integer status = (Integer) map.get("status");
                            if (status == 1) {
                                mSelectedList.add(map);
                            }
                        }
                        return mapList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        if (maps != null) {
                            mListMap.clear();
                            mListMap.addAll(maps);

                            if (maps.size() <= 0) {
                                //显示对话框
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                builder.setTitle("提示")
                                        .setMessage("当前无可用券")
                                        .setNegativeButton("取消", null)
                                        .setPositiveButton("确认", null)
                                        .show();
                            }

                            mCardPhoneAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void initEvent() {
        lvActivityCardPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Map<String, Object> map = mListMap.get(position);

                if ((Integer) map.get("status") == 1) {
                    ImageView iv_card_select = (ImageView) lvActivityCardPhone.findViewWithTag(position);
                    boolean selected = iv_card_select.isSelected();
                    iv_card_select.setSelected(!selected);
                    if (selected) {
                        mSelectedList.remove(map);
                    } else {
                        mSelectedList.add(map);
                    }
                }

            }
        });

    }

    private CardBean buildCardBean(Map<String, Object> stringObjectMap, int i) {

        String activate_type = String.valueOf(stringObjectMap.get("activate_type"));
        String create_time = String.valueOf(stringObjectMap.get("create_time"));
        String expired = String.valueOf(stringObjectMap.get("expired"));
        String explain_text = String.valueOf(stringObjectMap.get("explain_text"));
        String holder = String.valueOf(stringObjectMap.get("holder"));
        String name = String.valueOf(stringObjectMap.get("name"));
        String recharge_money = String.valueOf(stringObjectMap.get("recharge_money"));
        String source = String.valueOf(stringObjectMap.get("source"));
        String voucher_id = String.valueOf(stringObjectMap.get("voucher_id"));
        String voucher_money = String.valueOf(stringObjectMap.get("voucher_money"));
        String voucher_type = String.valueOf(stringObjectMap.get("voucher_type"));

        String product_id = String.valueOf(stringObjectMap.get("product_id"));
        String price = String.valueOf(stringObjectMap.get("price"));
        String product_name = String.valueOf(stringObjectMap.get("product_name"));
        String market_price = String.valueOf(stringObjectMap.get("market_price"));
        String pay_money = String.valueOf(stringObjectMap.get("pay_money"));
        String product_remark = String.valueOf(stringObjectMap.get("product_remark"));

        CardBean cardBean = new CardBean(
                activate_type,
                create_time,
                expired,
                explain_text,
                holder,
                name,
                recharge_money,
                source,
                i + "",
                voucher_id,
                voucher_money,
                voucher_type,
                product_id,
                price,
                product_name,
                market_price,
                pay_money,
                product_remark, "");

        return cardBean;
    }



}
