package com.bac.bacplatform.module.splash.view;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseFragment;
import com.bac.bacplatform.module.splash.contract.SplashContract;
import com.bac.bacplatform.utils.logger.LogUtil;
import com.bac.commonlib.domain.BacHttpBean;

import java.util.ArrayList;

import static com.bac.bacplatform.conf.Constants.APP.VERSION_NAME;


/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.module.splash.view
 * 创建人：Wjz
 * 创建时间：2017/4/12
 * 类描述：
 * 继承 AutomaticBaseFragment
 * 实现 SplashContract.View
 * 显示具体页面
 */
@Deprecated
public class SplashFragment extends AutomaticBaseFragment implements SplashContract.View {

    /*
    prsenter

    具体要加载什么样的不用关心
    具体加载什么内容不用关心

    管理 加载框是否显示，
    管理数据加载完成后，view的数据显示
    */
    private SplashContract.Presenter mPresenter;
    private SwipeRefreshLayout       mSrl;
    private Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * presenter的load方法
     */

    @Override
    public void  onStart() {
        super.onStart();

        LogUtil.sf(SplashFragment.class,"activity--onResume");

        BacHttpBean bacHttpBean = new BacHttpBean();
        bacHttpBean.setActionType(0);
        bacHttpBean.setMethodName("QUERY_CAROUSEL_IMAGES");
        bacHttpBean.put("version", VERSION_NAME);

        ArrayList<BacHttpBean> beanArrayList = new ArrayList<>();
        beanArrayList.add(bacHttpBean);
        beanArrayList.add(bacHttpBean);
        beanArrayList.add(bacHttpBean);
        beanArrayList.add(bacHttpBean);

        // 加载数据
        mPresenter.loadData(bacHttpBean, false, true);
    }

    /**
     * presenter的反订阅方法
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 反订阅
        mPresenter = null;
    }

    /**
     * 显示对应页面
     * fragment的具体显示的内容
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash_fragment, container, false);
        mSrl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        mSrl.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*
                下拉刷新
                此时数据已经过期，需要强制刷新数据
                重新组拼数据bean
                * */
                BacHttpBean bacHttpBean = new BacHttpBean();
                bacHttpBean.setActionType(0);
                bacHttpBean.setMethodName("QUERY_CAROUSEL_IMAGES");
                bacHttpBean.put("version", VERSION_NAME);
            }
        });
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(SplashFragment.class.getSimpleName());

        return view;
    }

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * 显示presenter处理的数据
     */
    @Override
    public void showData() {

    }



    /*@Override
    public void showLoadingIndicator(boolean active) {
        // 是否正在刷新，需要显示
        mSrl.setRefreshing(active);
    }*/

}
