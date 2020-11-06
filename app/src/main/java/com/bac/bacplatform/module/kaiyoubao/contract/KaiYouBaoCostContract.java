package com.bac.bacplatform.module.kaiyoubao.contract;

import com.bac.bacplatform.extended.base.mvp.BaseModel;
import com.bac.bacplatform.extended.base.mvp.BasePresenter;
import com.bac.bacplatform.extended.base.mvp.BaseView;

/**
 * Created by Wjz on 2017/5/4.
 */

public class KaiYouBaoCostContract {


    public interface View extends BaseView<Presenter> {

    }

    public interface Presenter extends BasePresenter {
        void loadData();
    }

    public interface Model extends BaseModel {
    }


}