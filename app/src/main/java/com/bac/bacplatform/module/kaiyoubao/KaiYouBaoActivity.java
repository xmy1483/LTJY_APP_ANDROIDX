package com.bac.bacplatform.module.kaiyoubao;

import android.content.Context;
import android.content.Intent;

import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.module.kaiyoubao.model.KaiYouBaoModelImpl;
import com.bac.bacplatform.module.kaiyoubao.presenter.KaiYouBaoPresenterImpl;
import com.bac.bacplatform.module.kaiyoubao.view.KaiYouBaoFragment;
import com.bac.bacplatform.utils.ui.UIUtils;


/**
 * Created by Wjz on 2017/5/4.
 */

public class KaiYouBaoActivity extends AutomaticBaseActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, KaiYouBaoActivity.class);
        return intent;
    }


    @Override
    protected void initView() {
        setContentView(R.layout.defualt_activity);
    }

    @Override
    protected void initFragment() {
        // 绑定mvp
        KaiYouBaoFragment kaiYouBaoFragment = UIUtils.fragmentUtil(this, KaiYouBaoFragment.newInstance(), R.id.fragment);
        new KaiYouBaoPresenterImpl(kaiYouBaoFragment, new KaiYouBaoModelImpl());


    }



}
