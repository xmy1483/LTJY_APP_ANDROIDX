package com.bac.bacplatform.module.center;

import android.content.Context;
import android.content.Intent;

import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.module.center.model.UserCenterModelImpl;
import com.bac.bacplatform.module.center.presenter.UserCenterPresenterImpl;
import com.bac.bacplatform.module.center.view.UserCenterDetailFragment;
import com.bac.bacplatform.utils.ui.UIUtils;

/**
 * Created by Wjz on 2017/5/8.
 */

public class UserCenterDetailActivity extends AutomaticBaseActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, UserCenterDetailActivity.class);
        return intent;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.defualt_activity);
    }

    @Override
    protected void initFragment() {

        UserCenterDetailFragment userCenterDetailFragment = UIUtils.fragmentUtil(this, UserCenterDetailFragment.newInstance(), R.id.fragment);

        new UserCenterPresenterImpl(userCenterDetailFragment, new UserCenterModelImpl());

    }


}
