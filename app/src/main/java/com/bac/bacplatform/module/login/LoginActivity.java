package com.bac.bacplatform.module.login;

import android.content.Context;
import android.content.Intent;

import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.module.login.model.LoginModelImpl;
import com.bac.bacplatform.module.login.presenter.LoginPresenterImpl;
import com.bac.bacplatform.module.login.view.LoginFragment2;
import com.bac.bacplatform.module.main.MainActivity;
import com.bac.bacplatform.utils.ui.UIUtils;

/**
 * Created by kegu on 2017/4/24.
 * opt + ent  代码修复
 */

public class LoginActivity extends AutomaticBaseActivity {


    private LoginModelImpl loginModel;
    private LoginPresenterImpl presenter;

    @Override
    protected void initView() {
        setContentView(R.layout.login_activity);
    }

    @Override
    protected void initFragment() {
        LoginFragment2 view = UIUtils.fragmentUtil(this, LoginFragment2.newInstance(), R.id.fragment);
        loginModel = new LoginModelImpl();
        presenter = new LoginPresenterImpl(view, loginModel);
    }

//    @Override
//    protected void initStatusBarColor() {
//        //StatusBarCompat.compat(this, ContextCompat.getColor(this,R.color.loginBg));
//    }
public static Intent newIntent(Context context) {
    return new Intent(context, MainActivity.class);
}

}
