package com.bac.bacplatform.module.kaiyoubao;

import android.content.Context;
import android.content.Intent;

import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.module.kaiyoubao.model.KaiYouBaoCostModelImpl;
import com.bac.bacplatform.module.kaiyoubao.presenter.KaiYouBaoCostPresenterImpl;
import com.bac.bacplatform.module.kaiyoubao.view.KaiYouBaoCostFragment;
import com.bac.bacplatform.utils.ui.UIUtils;

/**
 * Created by Wjz on 2017/5/4.
 */

public class KaiYouBaoCostActivity extends AutomaticBaseActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, KaiYouBaoCostActivity.class);
        return intent;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.defualt_activity);
    }

    @Override
    protected void initFragment() {
        // 绑定mvp

        KaiYouBaoCostFragment kaiYouBaoCostFragment = UIUtils.fragmentUtil(this, KaiYouBaoCostFragment.newInstance(), R.id.fragment);

        new KaiYouBaoCostPresenterImpl(kaiYouBaoCostFragment,new KaiYouBaoCostModelImpl());


    }


}
