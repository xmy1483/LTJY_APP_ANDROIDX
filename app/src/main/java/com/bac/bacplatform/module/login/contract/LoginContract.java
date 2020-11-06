package com.bac.bacplatform.module.login.contract;

import androidx.appcompat.app.AppCompatActivity;

import com.bac.commonlib.domain.BacHttpBean;
import com.bac.bacplatform.extended.base.mvp.BaseModel;
import com.bac.bacplatform.extended.base.mvp.BasePresenter;
import com.bac.bacplatform.extended.base.mvp.BaseView;

import java.util.Map;

/**
 * Created by Wjz on 2017/5/3.
 */

public class LoginContract {

    public interface View extends BaseView<Presenter> {
        void sendMsg(Map<String, Object> map);

        void login(Map<String, Object> map);

    }

    public interface Presenter extends BasePresenter {
        // 登录
        void sendMsg(BacHttpBean bean, boolean refreshData);

        void login(BacHttpBean bean, boolean refreshData, AppCompatActivity activity);
    }

    public interface Model extends BaseModel {

    }


}