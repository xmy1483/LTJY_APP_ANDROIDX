package com.bac.bacplatform.module.center;

import android.content.Context;
import android.content.Intent;

import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.module.center.model.UserCenterModelImpl;
import com.bac.bacplatform.module.center.presenter.UserCenterPresenterImpl;
import com.bac.bacplatform.module.center.view.UserCenterFragment;
import com.bac.bacplatform.utils.ui.UIUtils;

/**
 * Created by Wjz on 2017/5/4.
 * 用户中心辅助在mainActivity中
 */
@Deprecated
public class UserCenterActivity extends AutomaticBaseActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, UserCenterActivity.class);
        return intent;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.defualt_activity);
    }

    @Override
    protected void initFragment() {

        UserCenterFragment userCenterFragment = UIUtils.fragmentUtil(this, UserCenterFragment.newInstance(), R.id.fragment);

        new UserCenterPresenterImpl(userCenterFragment, new UserCenterModelImpl());
    }


}
