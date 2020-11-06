package com.bac.bacplatform.module.kaiyoubao.contract;

import com.bac.commonlib.domain.BacHttpBean;
import com.bac.bacplatform.extended.base.mvp.BaseModel;
import com.bac.bacplatform.extended.base.mvp.BasePresenter;
import com.bac.bacplatform.extended.base.mvp.BaseView;
import com.bac.bacplatform.module.kaiyoubao.adapter.KaiYouBaoBean;

import java.util.List;

/**
 * Created by Wjz on 2017/5/4.
 */

public class KaiYouBaoContract {

    public interface View extends BaseView<Presenter> {
        void showData(List<KaiYouBaoBean> beanList);
    }

    public interface Presenter extends BasePresenter {
        // 获取单条接口数据
        void loadData(BacHttpBean bean, boolean refreshData, boolean showLoading);

        void loadData();
    }

    public interface Model extends BaseModel {
    }


}