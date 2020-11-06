package com.bac.bacplatform.old.module.preferential;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.view.CanClearEditText;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.utils.str.StringUtil.isNullOrEmpty;


/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.preferential
 * 创建人：Wjz
 * 创建时间：2017/1/18
 * 类描述：优惠码
 */

public class PreferentialActivity extends SuperActivity {
    private CanClearEditText ccePreferential;
    private Button btnPreferential;
    private TextView tvPreferentialDescription;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferential_activity);


        initToolBar("优惠码兑换");

        ccePreferential = (CanClearEditText) findViewById(R.id.cce_preferential);
        btnPreferential = (Button) findViewById(R.id.btn_preferential);
        tvPreferentialDescription = (TextView) findViewById(R.id.tv_preferential_description);
        mTv = (TextView) findViewById(R.id.tv);

        ccePreferential.setTransformationMethod(new AllCapTransformationMethod(true));

        initData();
        initEvent();
    }

    private void initEvent() {
        btnPreferential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNetForPreferential();
            }
        });
        mTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNetGetPreferentialDescription();
            }
        });
    }

    private void initData() {

    }

    private void doNetGetPreferentialDescription() {


        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setActionType(0)
                .setMethodName("QUERY_START_PARAM")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("param_type", "BENEFIT_CODE"))
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new <String>RxDialog().rxDialog(this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        Map<String, Object> map = maps.get(0);
                        if (map != null) {
                            String param_value = String.valueOf(map.get("param_value")).replace("##", "\n");
                            tvPreferentialDescription.setText(param_value);
                        } else {
                            Toast.makeText(PreferentialActivity.this, "请稍后...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void doNetForPreferential() {

        String code = ccePreferential.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "请填写优惠码", Toast.LENGTH_SHORT).show();
            return;
        }


        HttpHelper.getInstance().bacNet(new BacHttpBean()
                .setMethodName("SUBMIT_BENEFIT_CODE")
                .put("login_phone", BacApplication.getLoginPhone())
                .put("benefit_code", code.toUpperCase()))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new RxDialog<String>().rxDialog(PreferentialActivity.this))
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> maps) {
                        Map<String, Object> map = maps.get(_0);
                        if (map != null) {
                            new AlertDialog.Builder(PreferentialActivity.this)
                                    .setTitle(map.get("msg_title") == null ? "提示" : isNullOrEmpty(map.get("msg_title")))
                                    .setMessage(isNullOrEmpty(map.get("msg")))
                                    .setPositiveButton("确定", null)
                                    .setNegativeButton("取消", null)
                                    .show();
                        } else {
                            Toast.makeText(PreferentialActivity.this, "请稍后...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    class AllCapTransformationMethod extends ReplacementTransformationMethod {

        private char[] lower = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        private char[] upper = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        private boolean allUpper = false;

        public AllCapTransformationMethod(boolean needUpper) {
            this.allUpper = needUpper;
        }

        @Override
        protected char[] getOriginal() {
            if (allUpper) {
                return lower;
            } else {
                return upper;
            }
        }

        @Override
        protected char[] getReplacement() {
            if (allUpper) {
                return upper;
            } else {
                return lower;
            }
        }
    }

}
