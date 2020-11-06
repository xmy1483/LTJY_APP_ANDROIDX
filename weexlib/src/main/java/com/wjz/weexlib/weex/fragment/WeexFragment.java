package com.wjz.weexlib.weex.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taobao.weex.WXSDKInstance;
import com.wjz.weexlib.R;
import com.wjz.weexlib.weex.delegate.BaseWeexDelegateCallback;
import com.wjz.weexlib.weex.delegate.FragmentWeexDelegate;
import com.wjz.weexlib.weex.delegate.IWeexView;
import com.wjz.weexlib.weex.delegate.impl.FragmentWeexDelegateImpl;

import java.util.Map;

/**
 * Created by wujiazhen on 2017/6/14.
 */

public class WeexFragment extends Fragment implements BaseWeexDelegateCallback, IWeexView {

    private FragmentWeexDelegate fragmentWeexDelegate;

    private WXSDKInstance wxsdkInstance;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentWeexDelegate().onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wx_weex_activity, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getFragmentWeexDelegate().onViewCreated(view,savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getFragmentWeexDelegate().onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        getFragmentWeexDelegate().onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getFragmentWeexDelegate().onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        getFragmentWeexDelegate().onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getFragmentWeexDelegate().onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getFragmentWeexDelegate().onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getFragmentWeexDelegate().onDetach();
    }

    private FragmentWeexDelegate getFragmentWeexDelegate() {
        if (fragmentWeexDelegate == null) {
            fragmentWeexDelegate = new FragmentWeexDelegateImpl(this);
        }
        return fragmentWeexDelegate;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String errorCode, String errorMsg) {

    }

    @Override
    public ViewGroup getContainer() {
        return (ViewGroup) getView().findViewById(R.id.fl);
    }

    @Override
    public WXSDKInstance createInstance() {
        return new WXSDKInstance(getActivity());
    }

    @Override
    public WXSDKInstance getInstance() {
        return wxsdkInstance;
    }

    @Override
    public void setInstance(WXSDKInstance instance) {

        this.wxsdkInstance = instance;
    }

    @Override
    public String getIndexUrl() {
        return "";
    }

    @Override
    public Map<String, Object> getInitData() {
        return null;
    }

    @Override
    public String getPagerName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public IWeexView getWeexView() {
        return this;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getFragmentWeexDelegate().onSaveInstanceState(outState);
    }

    public boolean onBackPressed() {
        return getFragmentWeexDelegate().onBackPressed();
    }
}
