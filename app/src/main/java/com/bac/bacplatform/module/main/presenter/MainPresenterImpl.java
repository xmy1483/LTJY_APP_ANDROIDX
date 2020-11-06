package com.bac.bacplatform.module.main.presenter;
import com.bac.bacplatform.module.main.contract.MainContract;
import com.bac.bacplatform.module.main.view.HomeFragment;

/**
* Created by maibenben on 2017/05/22
*/

public class MainPresenterImpl implements MainContract.Presenter{

    private final HomeFragment view;
    private final MainContract.Model model;

    public MainPresenterImpl(HomeFragment view, MainContract.Model model) {
        this.view = view;
        this.model = model;
    }
}