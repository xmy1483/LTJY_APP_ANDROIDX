package com.bac.bacplatform.module.center.view;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseFragment;
import com.bac.bacplatform.module.center.adapter.UserCenterSectionBean;
import com.bac.bacplatform.module.center.contract.UserCenterContract;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;

/**
 * Created by Wjz on 2017/5/8.
 */

public class UserCenterDetailFragment extends AutomaticBaseFragment implements UserCenterContract.View {

    private Button btn;

    public static UserCenterDetailFragment newInstance() {
        return new UserCenterDetailFragment();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_center_detail_fragment, container, false);

        initToolBar(view,getString(R.string.center_user_detail_title));

        RxTextView.text((TextView) view.findViewById(R.id.tv_01_01)).call(Html.fromHtml(getString(R.string.center_user_detail_tv_01_01).trim()));

        btn = (Button) view.findViewById(R.id.btn);
        RxTextView.text(btn).call(getString(R.string.center_user_detail_btn));

        return view;
    }

    @Override
    public void setPresenter(UserCenterContract.Presenter presenter) {

    }


    @Override
    public void showData(List<UserCenterSectionBean> beanList) {

    }



}
