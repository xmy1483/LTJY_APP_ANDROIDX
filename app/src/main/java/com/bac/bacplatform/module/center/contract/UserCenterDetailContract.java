package com.bac.bacplatform.module.center.contract;

import com.bac.bacplatform.extended.base.mvp.BaseModel;
import com.bac.bacplatform.extended.base.mvp.BasePresenter;
import com.bac.bacplatform.extended.base.mvp.BaseView;

/**
 * Created by Wjz on 2017/5/4.
 */

public class UserCenterDetailContract {


    public interface View extends BaseView<Presenter> {
    }

    public interface Presenter extends BasePresenter {
    }

    public interface Model extends BaseModel {
    }


}