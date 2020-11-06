package com.bac.bacplatform.module.recharge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.module.recharge.model.OilVoucherModelImpl;
import com.bac.bacplatform.module.recharge.presenter.OilVoucherPresenterImpl;
import com.bac.bacplatform.module.recharge.view.OilVoucherFragment;
import com.bac.bacplatform.utils.ui.UIUtils;

/**
 * Created by Wjz on 2017/5/22.
 */

public class OilVoucherActivity extends AutomaticBaseActivity {

    public static final String VOUCHER = "voucher";

    public static Intent newIntent(Context context) {
        return new Intent(context, OilVoucherActivity.class);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.defualt_activity);
    }

    @Override
    protected void initFragment() {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(VOUCHER,getIntent().getParcelableArrayListExtra(VOUCHER));
        OilVoucherFragment oilVoucherFragment = UIUtils.fragmentUtil(this, OilVoucherFragment.newInstance(bundle), R.id.fragment);
        new OilVoucherPresenterImpl(oilVoucherFragment, new OilVoucherModelImpl());

    }


}
