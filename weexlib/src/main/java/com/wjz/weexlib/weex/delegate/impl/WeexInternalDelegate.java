package com.wjz.weexlib.weex.delegate.impl;


import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.http.WXHttpUtil;
import com.taobao.weex.utils.WXFileUtils;
import com.wjz.weexlib.weex.WeexManager;
import com.wjz.weexlib.weex.delegate.BaseWeexDelegateCallback;
import com.wjz.weexlib.weex.delegate.IWeexView;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * IWXRenderListener 的实现类
 */
public class WeexInternalDelegate implements IWXRenderListener, Runnable {

    private BaseWeexDelegateCallback baseWeexDelegateCallback;

    public WeexInternalDelegate(BaseWeexDelegateCallback baseWeexDelegateCallback) {
        this.baseWeexDelegateCallback = baseWeexDelegateCallback;
    }

    /**
     * 创建 WXSDKInstance
     */
    public void createInstance() {

        WXSDKInstance instance = baseWeexDelegateCallback.getInstance();

        if (instance == null) {
            instance = baseWeexDelegateCallback.createInstance();
        }

        instance.registerRenderListener(this);
        baseWeexDelegateCallback.setInstance(instance);

    }

    /**
     * 销毁 WXSDKInstance
     */
    public void destroyInstance() {

        WXSDKInstance instance = baseWeexDelegateCallback.getInstance();
        if (instance != null) {
            instance.registerRenderListener(null);
            instance.destroy();
        }

    }

    /**
     * 获取 WeexView
     *
     * @return
     */
    private IWeexView getWeexView() {
        IWeexView weexView = baseWeexDelegateCallback.getWeexView();
        return weexView;
    }

    /**
     * 获取 父容器
     *
     * @return
     */
    private ViewGroup getContainer() {
        ViewGroup container = getWeexView().getContainer();
        return container;
    }

    /**
     * 对外提供 WXSDKInstance
     *
     * @return
     */
    public WXSDKInstance getInstance() {
        return baseWeexDelegateCallback.getInstance();
    }

    private String getInitData() {
        Map<String, Object> initData = baseWeexDelegateCallback.getInitData();
        return JSON.toJSONString(initData);
    }

    /**
     * 使用 父容器 发送 post 加载 Weex 页面
     */
    public void attachView() {
        getContainer().post(this);
    }

    public void detachView() {
    }

    // IWXRenderListener 实现方法
    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        getWeexView().hideLoading();
        getContainer().removeAllViews();
        getContainer().addView(view);
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
        getWeexView().hideLoading();
    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {
        getWeexView().hideLoading();
    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        getWeexView().hideLoading();
        getWeexView().showError(errCode, msg);
    }

    // 加载页面方法
    @Override
    public void run() {

        String indexUrl = baseWeexDelegateCallback.getIndexUrl();
        int measuredWidth = getContainer().getMeasuredWidth();
        int measuredHeight = getContainer().getMeasuredHeight();
        // 加载 Weex 页面
        renderPager(indexUrl, measuredWidth, measuredHeight);
    }

    /**
     * dispatch Weex页面
     *
     * @param indexUrl
     * @param containerWidth
     * @param containerHeight
     */
    private void renderPager(String indexUrl, int containerWidth, int containerHeight) {

        System.out.println("indexUrl:"+indexUrl);

        getWeexView().showLoading();
        String pageName = baseWeexDelegateCallback.getPagerName();
        HashMap<String, Object> options = new HashMap<>();
        options.put(WXSDKInstance.BUNDLE_URL, indexUrl);
        if (indexUrl.startsWith("http")) {
            Uri uri = Uri.parse(indexUrl);
            if (TextUtils.equals(uri.getEncodedAuthority(),"file")) {
                String path = uri.getPath().replaceFirst("/", "");

                renderPagerFromFile(pageName, path, options, getInitData(), containerWidth, containerHeight);
            }else{
                renderPagerFromNet(pageName, indexUrl, options, getInitData(), containerWidth, containerHeight);
            }

        } else if (indexUrl.startsWith("file") || indexUrl.startsWith("bac")) {
            Uri uri = Uri.parse(indexUrl);
            String authority = uri.getEncodedAuthority();
            if (authority.equals("assets")) {
                String path = uri.getPath().replaceFirst("/", "");
                renderPagerFromAsset(pageName, path, options, getInitData(), containerWidth, containerHeight);
            } else {
                String path = uri.getPath().replaceFirst("/", "");

                renderPagerFromFile(pageName, path, options, getInitData(), containerWidth, containerHeight);
            }
        }
    }

    /**
     * 从 本地 文件读取 Weex
     *
     * @param pageName
     * @param path
     * @param options
     * @param jsonInitData
     * @param containerWidth
     * @param containerHeight
     */
    private void renderPagerFromFile(String pageName, String path, Map<String, Object> options, String jsonInitData, int containerWidth, int containerHeight) {
        Context context = getInstance().getContext();
        File file = new File(context.getExternalFilesDir(null), path);

        String template = WXFileUtils.loadFileOrAsset(file.toString(), context);
        getInstance().render(pageName, template, options, jsonInitData, containerWidth, containerHeight, WXRenderStrategy.APPEND_ASYNC);
    }

    /**
     * 从 Asset 中加载 Weex
     *
     * @param pageName
     * @param options
     * @param jsonInitData
     * @param containerWidth
     * @param containerHeight
     */
    private void renderPagerFromAsset(String pageName, String assetPath, Map<String, Object> options, String jsonInitData, int containerWidth, int containerHeight) {

        Context context = getInstance().getContext();
        String template = WXFileUtils.loadAsset(assetPath, context);
        getInstance().render(pageName, template, options, jsonInitData, containerWidth, containerHeight, WXRenderStrategy.APPEND_ASYNC);
    }


    /**
     * 网络框架retrofit只用来处理 json形式 的返回数据，
     * 而 渲染界面 返回的是 js，
     * retrofit 不能转化会报错，
     * 所以使用官方的 默认adapter 用来加载界面
     *
     * @param pageName
     * @param url
     * @param options
     * @param jsonInitData
     * @param containerWidth
     * @param containerHeight
     */
    private void renderPagerFromNet(String pageName, String url, Map<String, Object> options, String jsonInitData, int containerWidth, int containerHeight) {

        System.out.println("Net  url:"+url);
        System.out.println("Net  pageName:"+pageName);

        Uri uri = Uri.parse(url);
        if (uri != null && TextUtils.equals(uri.getScheme(), "file")) {
            // WXSDKInstance
            String path = uri.toString();
            System.out.println("Net         file_path: " + path);
            getInstance().render(pageName, WXFileUtils.loadAsset(path, getInstance().getContext()), options, jsonInitData, containerWidth, containerHeight, WXRenderStrategy.APPEND_ASYNC);
            return;
        }

        IWXHttpAdapter adapter = WeexManager.getInstance().getDefaultHttpAdapter();

        WXRequest wxRequest = new WXRequest();
        wxRequest.url = url;
        if (wxRequest.paramMap == null) {
            wxRequest.paramMap = new HashMap<>();
        }
        wxRequest.paramMap
                .put("user-agent",
                        WXHttpUtil.assembleUserAgent(getInstance().getContext(),
                                WXEnvironment.getConfig()));

        IWXHttpAdapter.OnHttpListener wxHttpListener =
                getWXHttpListener(pageName,
                        options,
                        jsonInitData,
                        containerWidth,
                        containerHeight,
                        WXRenderStrategy.APPEND_ASYNC,
                        System.currentTimeMillis()
                );

        adapter.sendRequest(wxRequest, wxHttpListener);
    }


    /**
     * 获取 WXHttpListener
     *
     * @param pageName
     * @param options
     * @param jsonInitData
     * @param containerWidth
     * @param containerHeight
     * @param flag
     * @param time
     * @return
     */
    private IWXHttpAdapter.OnHttpListener getWXHttpListener(String pageName,
                                                            Map<String, Object> options,
                                                            String jsonInitData,
                                                            Integer containerWidth,
                                                            Integer containerHeight,
                                                            WXRenderStrategy flag,
                                                            Long time) {
        try {

            Class<? extends WXSDKInstance> outClazz = getInstance().getClass();
            // 获取所有的内部类
            Class<?>[] innerClazzs = outClazz.getDeclaredClasses();

            for (Class innerClazz : innerClazzs) {

                // 因为是包访问的，所以需要使用反射获取对象
                Class<IWXHttpAdapter.OnHttpListener> onHttpListenerClass = IWXHttpAdapter.OnHttpListener.class;

                // this = innerClazz -> true
                if (onHttpListenerClass.isAssignableFrom(innerClazz)) {

                    // 构造方法
                    Constructor[] declaredConstructors = innerClazz.getDeclaredConstructors();
                    // 第0个可见
                    declaredConstructors[0].setAccessible(true);

                    Object o = declaredConstructors[0].
                            newInstance(getInstance(),
                                    pageName,
                                    options,
                                    jsonInitData,
                                    // containerWidth,
                                    // containerHeight,
                                    flag,
                                    time);
                    return (IWXHttpAdapter.OnHttpListener) o;
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }


}
