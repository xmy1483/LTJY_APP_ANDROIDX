package com.bac.bacplatform.module.center.presenter;

import com.bac.bacplatform.R;
import com.bac.bacplatform.module.center.adapter.UserCenterSectionBean;
import com.bac.bacplatform.module.center.adapter.UserCenterSectionInnerBean;
import com.bac.bacplatform.module.center.contract.UserCenterContract;

import java.util.ArrayList;
import java.util.List;

/**
* Created by maibenben on 2017/05/04
*/

public class UserCenterPresenterImpl implements UserCenterContract.Presenter{
    private final UserCenterContract.View view;
    private final UserCenterContract.Model model;

    public UserCenterPresenterImpl(UserCenterContract.View view, UserCenterContract.Model model) {
        this.view=view;
        this.model=model;
        view.setPresenter(this);
    }


    @Override
    public void loadData() {

        view.showData(getData());
    }

    private List<UserCenterSectionBean> getData(){
        ArrayList<UserCenterSectionBean> list = new ArrayList<>();

        list.add(new UserCenterSectionBean(true,"常用账单","查看更多账单"));
        list.add(new UserCenterSectionBean(new UserCenterSectionInnerBean(R.mipmap.center_order_recharge,false)));
        list.add(new UserCenterSectionBean(new UserCenterSectionInnerBean(R.mipmap.center_order_insurance2,false)));
        list.add(new UserCenterSectionBean(new UserCenterSectionInnerBean(R.mipmap.center_order_kaiyoubao,false)));
        list.add(new UserCenterSectionBean(new UserCenterSectionInnerBean(R.mipmap.center_order_exchange,false)));


        list.add(new UserCenterSectionBean(true,"必备工具",null));
        list.add(new UserCenterSectionBean(new UserCenterSectionInnerBean(R.mipmap.center_tools_search,false)));
        list.add(new UserCenterSectionBean(new UserCenterSectionInnerBean(R.mipmap.center_tools_pay,false)));
        list.add(new UserCenterSectionBean(new UserCenterSectionInnerBean(R.mipmap.center_tools_call,false)));
        list.add(new UserCenterSectionBean(new UserCenterSectionInnerBean(R.mipmap.center_tools_issue,false)));
        list.add(new UserCenterSectionBean(new UserCenterSectionInnerBean(R.mipmap.center_tools_suggest,false)));
        list.add(new UserCenterSectionBean(new UserCenterSectionInnerBean(R.mipmap.center_tools_about,false)));
        list.add(new UserCenterSectionBean(new UserCenterSectionInnerBean(R.mipmap.oil_card_order,false)));

        list.add(new UserCenterSectionBean(true,"关注有礼",null));
        list.add(new UserCenterSectionBean(new UserCenterSectionInnerBean(R.mipmap.center_weixin,true)));

        return list;
    }
}