package com.bac.bacplatform.module.center.contract;

import com.bac.bacplatform.extended.base.mvp.BaseModel;
import com.bac.bacplatform.extended.base.mvp.BasePresenter;
import com.bac.bacplatform.extended.base.mvp.BaseView;
import com.bac.bacplatform.module.center.adapter.UserCenterSectionBean;

import java.util.List;

/**
 * Created by Wjz on 2017/5/4.
 */

public class UserCenterContract {

    public interface View extends BaseView<Presenter> {
        void showData(List<UserCenterSectionBean> beanList);
    }

    public interface Presenter extends BasePresenter {
        void loadData();
    }

    public interface Model extends BaseModel {
    }


}