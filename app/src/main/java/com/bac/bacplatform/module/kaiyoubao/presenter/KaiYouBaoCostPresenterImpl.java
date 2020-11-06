package com.bac.bacplatform.module.kaiyoubao.presenter;

import com.bac.bacplatform.module.kaiyoubao.contract.KaiYouBaoCostContract;

/**
* Created by maibenben on 2017/05/04
*/

public class KaiYouBaoCostPresenterImpl implements KaiYouBaoCostContract.Presenter{

    private final KaiYouBaoCostContract.View view;
    private final KaiYouBaoCostContract.Model model;

    public KaiYouBaoCostPresenterImpl(KaiYouBaoCostContract.View view
            , KaiYouBaoCostContract.Model model) {

        this.view = view;
        this.model=model;

        view.setPresenter(this);
    }



    @Override
    public void loadData() {


    }
}