package com.bac.bacplatform.module.splash.presenter;

import com.bac.commonlib.domain.BacHttpBean;
import com.bac.bacplatform.module.splash.contract.SplashContract;
import com.bac.bacplatform.module.splash.view.SplashFragment;

import java.util.List;


/**
 * Created by Wjz on 2017/04/12
 */
@Deprecated
public class SplashPresenterImpl implements SplashContract.Presenter {

    private SplashFragment mView;
    private SplashContract.Model       mModel;


    public SplashPresenterImpl(SplashFragment fragment, SplashContract.Model model) {
        mView = fragment;
        mModel = model;
        // view 绑定 presenter
        mView.setPresenter(this);
    }


    /**
     * 每次onResume都会调用
     * <p>
     * 当前数据是否有缓存
     * 当前数据是否过期
     * <p>
     * 只需判断当前数据是否过期，不需要判断是否是第一次加载
     * 只有在用户下拉刷新后才需要强制加载新数据
     * <p>
     * 加载数据
     * 判断是否需要显示加载框
     *
     * @param refreshData
     */
    @Override
    public void loadData(BacHttpBean bean, boolean refreshData, boolean showLoading) {

        // 管理是否显示进度条
        if (showLoading) {
           // mView.showLoadingIndicator(showLoading);
        }

        // 加载数据
        // 订阅observer
        // 在此之前都是String类型,处理具体逻辑,返回要求的数据
      /*  Subscription subscription =
                mModel.getData(bean, refreshData)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                mView.showLoadingIndicator(false);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String s) {
                                LogUtil.sf(String.class,s);
                            }
                        });

        mSubscriptions.add(subscription);*/
    }

    /**
     * 多条接口实质上是 遍历 实现单条接口
     *
     * @param list
     * @param refreshData
     */
    @Override
    public void loadData(List<BacHttpBean> list, boolean refreshData, boolean showLoading) {
        for (BacHttpBean httpBean : list) {
            loadData(httpBean, refreshData, showLoading);
        }
    }

}