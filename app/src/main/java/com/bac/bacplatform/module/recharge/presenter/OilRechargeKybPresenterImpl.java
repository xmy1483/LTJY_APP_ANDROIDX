package com.bac.bacplatform.module.recharge.presenter;
import com.bac.bacplatform.module.recharge.contract.OilRechargeKybContract;

/**
* Created by maibenben on 2017/05/08
*/

public class OilRechargeKybPresenterImpl implements OilRechargeKybContract.Presenter{

    private final OilRechargeKybContract.View view;
    private final OilRechargeKybContract.Model model;

    public OilRechargeKybPresenterImpl(OilRechargeKybContract.View view, OilRechargeKybContract.Model model) {
        this.view=view;
        this.model=model;
        view.setPresenter(this);
    }

}