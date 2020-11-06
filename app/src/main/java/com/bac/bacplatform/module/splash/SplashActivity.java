package com.bac.bacplatform.module.splash;

import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.module.splash.contract.SplashContract;
import com.bac.bacplatform.module.splash.model.SplashModelImpl;
import com.bac.bacplatform.module.splash.presenter.SplashPresenterImpl;
import com.bac.bacplatform.module.splash.view.SplashFragment;
import com.bac.bacplatform.utils.ui.UIUtils;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.module.splash
 * 创建人：Wjz
 * 创建时间：2017/4/12
 * 类描述：
 * app的启动页
 * fragment的托管页面
 * 管理启动页的生命周期
 */
@Deprecated
public class SplashActivity extends AutomaticBaseActivity {

    private SplashContract.Presenter mSplashPresenter;
    private SplashFragment mView;

    @Override
    protected void initView() {
        setContentView(R.layout.splash_activity);
    }

    @Override
    protected void initFragment() {
        mView = UIUtils.fragmentUtil(this, SplashFragment.newInstance(), R.id.fragment);
        // mvp 操作
        mSplashPresenter = new SplashPresenterImpl(mView, new SplashModelImpl());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSplashPresenter = null;

    }


}
