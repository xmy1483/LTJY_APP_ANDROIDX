package com.bac.bacplatform.extended.base.components;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxFragment;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.base
 * 创建人：Wjz
 * 创建时间：2017/4/12
 * 类描述：
 * fragment 的封装类
 * 抽取公共方法
 * 显示对应页面
 */

public abstract class AutomaticBaseFragment extends AutomaticRxFragment {

    public AutomaticBaseActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AutomaticBaseActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView(inflater, container, savedInstanceState);
        return view;
    }

    /**
     * 处理toolbar
     *
     * @param s
     * @param listener
     * @return
     */
    protected TextView initToolBar(View view, String s, View.OnClickListener listener) {
        return UIUtils.initToolBar(activity, view, s, listener);
    }

    protected TextView initToolBar(View view, String s) {
        return initToolBar(view, s, null);
    }


    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

}
