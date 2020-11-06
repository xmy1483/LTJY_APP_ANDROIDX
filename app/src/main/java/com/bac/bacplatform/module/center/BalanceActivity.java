package com.bac.bacplatform.module.center;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bacplatform.view.CanClearEditText;
import com.bac.commonlib.domain.BacHttpBean;

public class  BalanceActivity extends AutomaticBaseActivity {

private CanClearEditText ccet01;
    private TextView tv;
    private Button btn;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_balance);
        initToolBar("余额查询");

        ccet01 = (CanClearEditText)findViewById(R.id.ccet_01);
         tv = (TextView)findViewById(R.id.textView3);
        btn = (Button) findViewById(R.id.btn_search);


        String oilCard = StringUtil.decode(activity, "oilCard"); //sp
        if (!TextUtils.isEmpty(oilCard)) {
            ccet01.setText(oilCard);
        } else {

        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String card = ccet01.getText().toString();
                HttpHelper.getInstance().bacNet(new BacHttpBean().setMethodName(""));

            }
        });


    }

    @Override
    protected void initFragment() {

    }


}
