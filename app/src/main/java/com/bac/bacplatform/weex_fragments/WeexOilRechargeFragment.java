package com.bac.bacplatform.weex_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bac.bacplatform.extended.base.components.AutomaticBaseFragment;

public class WeexOilRechargeFragment extends AutomaticBaseFragment {

   public static WeexOilRechargeFragment ins;

   public WeexOilRechargeFragment(){}

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ins = this;
        if(contentView!=null)
            return contentView;
        else
            return null;
    }

    public View getView(){
        return contentView;
    }

    private View contentView;
    public WeexOilRechargeFragment(View contentView){
        this.contentView = contentView;
    }



}
