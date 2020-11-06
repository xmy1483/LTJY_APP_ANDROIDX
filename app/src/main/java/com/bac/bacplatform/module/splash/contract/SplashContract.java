package com.bac.bacplatform.module.splash.contract;

import com.bac.commonlib.domain.BacHttpBean;
import com.bac.bacplatform.extended.base.mvp.BaseModel;
import com.bac.bacplatform.extended.base.mvp.BasePresenter;
import com.bac.bacplatform.extended.base.mvp.BaseView;

import java.util.List;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.module.splash.contract
 * 创建人：Wjz
 * 创建时间：2017/4/12
 * 类描述：
 */

public class SplashContract {

    public interface View extends BaseView<Presenter> {
        // TODO: 2017/4/12 返回内容
        void showData();
    }

    public interface Presenter extends BasePresenter {
        // 获取单条接口数据
        void loadData(BacHttpBean bean, boolean refreshData, boolean showLoading);

        // 获取多条接口的数据
        void loadData(List<BacHttpBean> list, boolean refreshData, boolean showLoading);
    }

    public interface Model extends BaseModel{
    }
}