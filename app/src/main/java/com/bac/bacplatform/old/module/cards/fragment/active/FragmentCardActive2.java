package com.bac.bacplatform.old.module.cards.fragment.active;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseFragment;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.module.cards.CouponActiveActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.FragmentEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.fragment
 * 创建人：Wjz
 * 创建时间：2016/10/25
 * 类描述：
 */

public class FragmentCardActive2 extends AutomaticBaseFragment {
    private ListView lvActivityCardPhone;
    private CardPhoneAdapter mCardPhoneAdapter;


    private List<Map<String, Object>> mListMap = new ArrayList<>();
    
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();

    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phone_cards_active_fragment, null);
        lvActivityCardPhone = (ListView) view.findViewById(R.id.lv_activity_card_phone);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCardPhoneAdapter = new CardPhoneAdapter(getActivity(), mListMap);
        lvActivityCardPhone.setAdapter(mCardPhoneAdapter);
        initEvent();

    }

    private void initEvent() {
        lvActivityCardPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListMap.size() == 0) {
                    Toast.makeText(activity, "没有可用的优惠券", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> map = mListMap.get(position);
                int voucher_type = (Integer) map.get("voucher_type");
                switch (voucher_type) {
                    case 1://话费

                        Integer activate_type = (Integer) map.get("activate_type");
                        CardBean cardBean = null;
                        int isType = -1;
                        switch (activate_type) {
                            case 0://普通
                                //开启支付界面
                                cardBean = buildCardBean(map, 1);
                                isType = 0;
                                break;
                            case 1://非0
                                cardBean = buildCardBean(map, 1);
                                isType = 1;
                                break;
                        }
                        Intent intent = new Intent(activity, CouponActiveActivity.class);
                        intent.putExtra("cardBean", cardBean);
                        intent.putExtra("isType", isType);
                        UIUtils.startActivityInAnim(activity,intent);

                        break;
                    case 2://流量

                        Integer status = (Integer) map.get("status");
                        if (status == 0) {

                            int product_type = (Integer) map.get("product_type");

                            Intent cardAct2 = new Intent(activity, CouponActiveActivity.class);
                            CardBean cardBean_act2 = null;
                            switch (product_type) {
                                case 0:
                                    //当天
                                    cardBean_act2 = buildCardBean(map, 2);
                                    cardAct2.putExtra("active", 2);
                                    break;
                                case 1:
                                    //次月
                                    cardBean_act2 = buildCardBean(map, 2);
                                    cardAct2.putExtra("active", 3);
                                    break;
                                case 2:
                                    //次日
                                    cardBean_act2 = buildCardBean(map, 2);
                                    cardAct2.putExtra("active", 4);
                                    break;
                            }
                            cardAct2.putExtra("cardBean", cardBean_act2);
                            UIUtils.startActivityInAnim(activity,cardAct2);
                        } else if (status == 4) {
                            Toast.makeText(activity, "正在处理，请稍后...", Toast.LENGTH_SHORT).show();
                        }
                        break;
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
        String use_method = String.valueOf(stringObjectMap.get("use_explain"));


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
                product_remark,
                use_method);

        return cardBean;
    }

    @Override
    public void onResume() {
        super.onResume();

        initData();
    }

    private void initData() {

        //请求数据
        ArrayList<Integer> intParam = new ArrayList<>();
        intParam.add(0);
        intParam.add(1);
        intParam.add(2);

        ArrayList<Integer> intParam2 = new ArrayList<>();
        intParam2.add(0);

        //查询券
        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_VOUCHER")
                .put("login_phone", bundle.getString("login_phone"))
                .put("status", intParam2)
                .put("voucher_type", intParam)
        )
                .compose(this.<String>bindUntilEvent(FragmentEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(getActivity()))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        if (maps != null) {
                            if (maps.size() > 0) {
                                mListMap.clear();
                                mListMap.addAll(maps);
                                mCardPhoneAdapter.notifyDataSetChanged();
                            } else {
                                new AlertDialog.Builder(activity)
                                        .setTitle("提示")
                                        .setMessage("没有可激活券")
                                        .setPositiveButton("确定", null)
                                        .setNegativeButton("取消", null)
                                        .show();
                            }
                        }
                    }
                });
    }

}
