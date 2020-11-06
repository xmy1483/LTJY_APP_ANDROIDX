package com.bac.bacplatform.module.recharge;

import android.content.Context;
import android.content.Intent;

import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.module.recharge.model.OilRechargeKybModelImpl;
import com.bac.bacplatform.module.recharge.presenter.OilRechargeKybPresenterImpl;
import com.bac.bacplatform.module.recharge.view.OilRechargeKybFragment;
import com.bac.bacplatform.utils.ui.UIUtils;

/**
 * Created by Wjz on 2017/5/8.
 */

public class OilRechargeKybActivity extends AutomaticBaseActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, OilRechargeKybActivity.class);
        return intent;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.defualt_activity);
    }

    @Override
    protected void initFragment() {

        OilRechargeKybFragment oilRechargeKybFragment = UIUtils.fragmentUtil(this, OilRechargeKybFragment.newInstance(), R.id.fragment);
        new OilRechargeKybPresenterImpl(oilRechargeKybFragment,new OilRechargeKybModelImpl());

    }


}
