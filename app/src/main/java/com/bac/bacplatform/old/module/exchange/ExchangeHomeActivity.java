package com.bac.bacplatform.old.module.exchange;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.exchange.adapter.ExchangeLabelAdapter;
import com.bac.bacplatform.old.module.exchange.adapter.ItemExchangeBean;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.conf.Constants.CommonProperty._1;
import static com.bac.bacplatform.conf.Constants.CommonProperty._13;
import static com.bac.bacplatform.conf.Constants.CommonProperty._3;
import static com.bac.bacplatform.conf.Constants.CommonProperty._4;
import static com.bac.bacplatform.conf.Constants.CommonProperty._8;
import static com.bac.bacplatform.conf.Constants.CommonProperty._9;
import static com.bac.bacplatform.utils.ui.UIUtils.startActivityInAnim;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.exchange
 * 创建人：Wjz
 * 创建时间：2016/9/19
 * 类描述：
 */
public class ExchangeHomeActivity extends SuperActivity {

    private boolean result;
    private Button btnExchange;
    private RecyclerView mRv_exchange;
    private boolean is_criterion = false;

    private ExchangeLabelAdapter mExchangeLabelAdapter;
    private List<ItemExchangeBean> mList = new ArrayList<>();

    private String product_id;
    private float payMoney;
    private String mPhone;
    private String mBalance;
    private TextView tvExchangeOilMoney;
    private TextView etExchangePhone;
    private TextView tvExchangeLabel;
    private TextView tvExplainTop;
    private TextView tvExplainBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_activity);

        initToolBar(getString(R.string.exchange_title));

        //修改
        mRv_exchange = (RecyclerView) findViewById(R.id.rv_exchange);
        mRv_exchange.setLayoutManager(new GridLayoutManager(this, _3));

        btnExchange = (Button) findViewById(R.id.btn);
        RxTextView.text(btnExchange).call(getString(R.string.exchange_btn));

        initData();
    }

    private void initData() {

        mExchangeLabelAdapter = new ExchangeLabelAdapter(R.layout.exchange_item, mList);
        mExchangeLabelAdapter.addFooterView(getFootLabelView());
        mExchangeLabelAdapter.addHeaderView(getHeadLabelView());
        mRv_exchange.setAdapter(mExchangeLabelAdapter);

        initEvent();

    }

    private void initEvent() {
        mRv_exchange.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                if (!is_criterion) {
                    return;
                }
                List<ItemExchangeBean> data = baseQuickAdapter.getData();


                for (ItemExchangeBean itemExchangeBean : data) {

                    if (itemExchangeBean.getId() == i) {
                        itemExchangeBean.setBg(R.drawable.flow_choice);

                        payMoney = data.get(i).getPay_money();
                        product_id = data.get(i).getProduct_id();
                        tvExchangeOilMoney.setText(StringUtil.isNullOrEmpty(payMoney));

                    } else {
                        itemExchangeBean.setBg(Color.TRANSPARENT);
                    }
                }

                baseQuickAdapter.notifyDataSetChanged();

            }
        });

        btnExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2018.2.5取消电信用户android端判断余额不足，取消弹框显示

               // if (Float.parseFloat(mBalance) >= payMoney) {

                    Intent intent = new Intent(ExchangeHomeActivity.this, ExchangeMsgActivity.class);
                    intent.putExtra("phone", mPhone);
                    intent.putExtra("money", payMoney);
                    intent.putExtra("product_id", product_id);
                    startActivityInAnim(ExchangeHomeActivity.this, intent);
//                } else {
//                    new AlertDialog.Builder(ExchangeHomeActivity.this)
//                            .setTitle("提示")
//                            .setMessage("可用余额不足")
//                            .setPositiveButton("确定", null)
//                            .show();
//                }
            }
        });
    }

    private View getHeadLabelView() {
        View header = getLayoutInflater().inflate(R.layout.exchange_item_home_header, null);

        String str = getString(R.string.exchange_header_label_1);
        TextView tv = (TextView) header.findViewById(R.id.tv);
        SpannableString spanColor = new SpannableString(str);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary));
        spanColor.setSpan(foregroundColorSpan, 0, 4, 0);
        tv.setText(spanColor);
        return header;
    }


    private void getExchangeProduct(String phone) {

        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_PRODUCT")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("charge_mobile", phone))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(ExchangeHomeActivity.this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new Func1<String, List<Map<String, Object>>>() {
                    @Override
                    public List<Map<String, Object>> call(String s) {

                        return JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                        }.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        Map<String, Object> map = maps.get(0);
                        if (map != null) {

                            // is_criterion : 是否是电信号码
                            // result ：话费是否查询成功
                            // is_show_balance ：是否显示label
                            is_criterion = (boolean) map.get("is_criterion");
                            if (map.get("result") == null && map.get("result") == "") {
                                result = false;
                            } else {
                                result = (boolean) map.get("result");
                            }
                            boolean is_show_balance = (boolean) map.get("is_show_balance");

                            String innerProducts = map.get("products").toString();

                            //默认 非电信
                            Spanned text = Html.fromHtml(getString(R.string.exchange_label_false));

                            if (is_criterion) {//电信
                                if (result) {//查询成功
                                    btnExchange.setEnabled(true);//按钮可用
                                    mBalance = String.valueOf(map.get("balance"));

                                    String showBalance = mBalance;
                                    if (Float.parseFloat(showBalance) >= 200) {
                                        showBalance = getString(R.string.exchange_label_1);
                                    }
                                    text = Html.fromHtml(String.format(getString(R.string.exchange_label_true), showBalance));
                                } else {
                                    text = null;
                                }
                            } else {
                                HttpHelper.getInstance()
                                        .bacNetWithContext(activity,
                                                new BacHttpBean()
                                                        .setActionType(_0)
                                                        .setMethodName("JUMP_YD_PAGE")
                                                        .put("charge_mobile", StringUtil.replaceBlank(etExchangePhone.getText()+""))
                                                        .put("login_phone", BacApplication.getLoginPhone().trim())

                                        ).compose(activity.<String>bindUntilEvent(ActivityEvent.DESTROY))
                                        .compose(new RxDialog<String>().rxDialog(activity))
                                        .observeOn(RxScheduler.RxPoolScheduler())
                                        // .map(new JsonFunc1<String,List<Map<String,Object>>>())
                                        .map(new Func1<String, List<Map<String, Object>>>() {
                                            @Override
                                            public List<Map<String, Object>> call(String s) {
                                                return JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
                                                }.getType());
                                            }
                                        })
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Action1<List<Map<String, Object>>>() {
                                            @Override
                                            public void call(List<Map<String, Object>> maps) {
                                                Map<String, Object> stringObjectMap = maps.get(_0);//0
                                                String mUrl = stringObjectMap.get("jump_url").toString();
                                                UIUtils.startActivityInAnimAndFinishSelf(activity, new Intent(ExchangeHomeActivity.this, WebAdvActivity.class)
                                                        .putExtra("title", "话费购")
                                                        .putExtra("ads_url", mUrl));
                                            }

                                        });
//                                AlertDialog.Builder builder2 = new AlertDialog.Builder(ExchangeHomeActivity.this);
//                                builder2.setTitle(R.string.exchange_label_2)
//                                        .setMessage(R.string.exchange_label_3)
//                                        .setPositiveButton(R.string.exchange_label_insure, new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                etExchangePhone.setText("");
//                                                etExchangePhone.requestFocus();
//                                            }
//                                        })
//                                        .setNegativeButton(R.string.exchange_label_cancel, new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                etExchangePhone.setText("");
//                                                etExchangePhone.requestFocus();
//                                            }
//                                        })
//                                        .setCancelable(false)
//                                        .show();
                            }


                            if (is_show_balance) {//显示
                                tvExchangeLabel.setText(text);
                            } else {//隐藏
                                tvExchangeLabel.setText("");
                            }

                            mList.clear();
                            mList.addAll(JSON.parseArray(innerProducts, ItemExchangeBean.class));

                            for (int i = 0; i < mList.size(); i++) {

                                mList.get(i).setId(i);

                                if (is_criterion) {
                                    if (i == 0) {
                                        mList.get(i).setBg(R.drawable.flow_choice);
                                        product_id = mList.get(i).getProduct_id();
                                        payMoney = mList.get(i).getPay_money();
                                        //更新兑换比例
                                        tvExchangeOilMoney.setText(String.valueOf(payMoney));
                                    }
                                } else {
                                    mList.get(i).setBg(Color.TRANSPARENT);
                                }
                            }
                            mExchangeLabelAdapter.notifyDataSetChanged();
                        } else {
                            btnExchange.setEnabled(true);
                        }
                    }
                });
    }

    private View getFootLabelView() {

        View foot = getLayoutInflater().inflate(R.layout.exchange_item__home_foot, null);

        tvExchangeOilMoney = (TextView) foot.findViewById(R.id.tv_exchange_oil_money);

        etExchangePhone = (TextView) foot.findViewById(R.id.et_exchange_phone);
        etExchangePhone.addTextChangedListener(watcher);
        etExchangePhone.setText(BacApplication.getLoginPhone());


        tvExchangeLabel = (TextView) foot.findViewById(R.id.tv_exchange_label);
        //默认 非电信
        Spanned text = Html.fromHtml(getString(R.string.exchange_label_false));
        tvExchangeLabel.setText(text);

        tvExplainTop = (TextView) foot.findViewById(R.id.tv_explain_top);
        Spanned explain = Html.fromHtml(getString(R.string.exchange_explain));
        tvExplainTop.setText(getString(R.string.exchange_footer_label_1));
        tvExplainBottom = (TextView) foot.findViewById(R.id.tv_explain_bottom);
        tvExplainBottom.setText(getString(R.string.exchange_footer_label_2));
        return foot;
    }

    TextWatcher watcher = new TextWatcher() {
        char c = ' ';

        /**
         * 格式化手机号码 xxx xxxx xxxx
         * @param s
         * @param start
         * @param before
         * @param count
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s == null || s.length() == _0) {
                return;
            }
            StringBuffer sb = new StringBuffer();
            for (int i = _0; i < s.length(); i++) {

                if (i != _3 && i != _8 && s.charAt(i) == c) {
                    continue;
                } else {
                    sb.append(s.charAt(i));
                    if ((sb.length() == _4 || sb.length() == _9) && sb.charAt(sb.length() - _1) != c) {
                        sb.insert(sb.length() - _1, c);
                    }
                }
            }

            if (!(sb.toString().equals(s.toString()))) {
                int index = start + _1;
                if (sb.charAt(start) == c) {
                    if (before == _0) {
                        index++;
                    } else {
                        index--;
                    }
                } else {
                    if (before == _1) {
                        index--;
                    }
                }


                etExchangePhone.setText(sb.toString());
                //etExchangePhone.setSelection(index);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == _13) {
                mPhone = StringUtil.replaceBlank(s.toString());
                if (StringUtil.isPhone(mPhone)) {
                    //隐藏键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etExchangePhone.getWindowToken(), _0); //关->开 ，开->关
                    //获取 手机号信息
                    getExchangeProduct(mPhone);
                }
            } else {
                btnExchange.setEnabled(false);
            }
        }
    };
}
