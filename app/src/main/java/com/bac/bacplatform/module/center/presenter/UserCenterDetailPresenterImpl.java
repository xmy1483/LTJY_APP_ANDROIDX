package com.bac.bacplatform.module.center.presenter;

import com.bac.bacplatform.module.center.contract.UserCenterDetailContract;

/**
 * Created by maibenben on 2017/05/08
 */

public class UserCenterDetailPresenterImpl implements UserCenterDetailContract.Presenter {

    private final UserCenterDetailContract.View view;
    private final UserCenterDetailContract.Model model;

    public UserCenterDetailPresenterImpl(UserCenterDetailContract.View view, UserCenterDetailContract.Model model) {
        this.view = view;
        this.model = model;
        view.setPresenter(this);
    }

}