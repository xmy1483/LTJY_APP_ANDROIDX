package com.bac.bacplatform.module.recharge.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bac.bacplatform.extended.base.components.AutomaticBaseFragment;
import com.bac.bacplatform.module.recharge.contract.OilRechargeKybContract;

/**
 * Created by Wjz on 2017/5/8.
 */

public class OilRechargeKybFragment extends AutomaticBaseFragment implements OilRechargeKybContract.View{

    public static OilRechargeKybFragment newInstance(){
        return new OilRechargeKybFragment();
    }

    @Override
    public void setPresenter(OilRechargeKybContract.Presenter presenter) {

    }



    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return null;
    }


}
