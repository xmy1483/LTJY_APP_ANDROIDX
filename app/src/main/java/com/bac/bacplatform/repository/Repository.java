package com.bac.bacplatform.repository;

import android.content.DialogInterface;
import androidx.collection.LruCache;
import androidx.appcompat.app.AppCompatActivity;

import com.bac.commonlib.domain.BacHttpBean;
import com.bac.bacplatform.http.HttpHelper;

import rx.Observable;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.repository
 * 创建人：Wjz
 * 创建时间：2017/4/7
 * 类描述：
 * 粒度：
 * 1.每个Activity/Fragment对应一个
 * 2.由于会出现多个接口同时请求的原因，并且提高数据的获取速度，将该模块设置成为单例
 * 3.增加 LruCahce 缓存
 * 三级缓存：
 * 1.缓存数据（非过期）
 * 1.是否需要缓存设置？
 * 2.缓存什么时候释放？
 * 2.本地数据
 * 1.读写数据库
 * 3.远程数据
 * 2.请求网络
 * 4.非单例模式
 * 5.向protocol提供数据处理
 */

public class Repository implements DataSource {

    // 动态获取jvm的内存  魅族 获取该参数有问题
    //private int maxSize = (int) (Runtime.getRuntime().freeMemory());
    // 缓存的容器
    private LruCache<String, String> memCache = new LruCache<String, String>(
            10) {
        @Override
        protected int sizeOf(String key, String value) {
            // 计算大小
            return value.getBytes().length;
        }
    };

    // 单例
    private static final Repository REPOSITORY = new Repository();

    private Repository() {
    }

    public static Repository getInstance() {
        return REPOSITORY;
    }


    /**
     * 提交数据 refresh 为 true
     *
     * @param bean
     * @param refresh true 过期  false 未过期
     * @return
     */

    public Observable<String> getData(BacHttpBean bean, boolean refresh) {
        return getData(bean, refresh, null);
    }


    public Observable<String> getData(BacHttpBean bean, boolean refresh, AppCompatActivity activity) {
        return getData(bean, refresh, activity, null, null, null);
    }

    public Observable<String> getData(BacHttpBean bean, boolean refresh, AppCompatActivity activity,DialogInterface.OnClickListener positive) {
        return getData(bean, refresh, activity, positive, null, null);
    }

    @Override
    public Observable<String> getData(BacHttpBean bean, boolean refresh, AppCompatActivity activity,
                                      final DialogInterface.OnClickListener positive,
                                      final DialogInterface.OnClickListener negative,
                                      final DialogInterface.OnCancelListener cancel) {
        // 判断当前请求 是否过期
        // 未过期
       /* if (!refresh) {
            // 取数据
            // 无缓存 网络请求
            final String methodName = bean.getMethodName();
            final String cacheStr = memCache.get(methodName);
            // 内存缓存
            if (cacheStr != null && cacheStr.length() > 0) {
                return Observable.just(cacheStr);
            } else {
                // 内存没有缓存，取本地缓存
                if (false) {
                    // 重新存入 内存缓存
                }
            }
        }*/
        // 过期网络请求 或者 缓存不在内存，不在本地
        return getAndSaveNetData(bean, activity, positive, negative, cancel);
    }

    /**
     * 内存无缓存
     * 本地加载
     * 存入内存
     */
    private void getAndSaveLocalData() {
    }


    /**
     * 数据过期
     * 无缓存数据
     * 网络请求，重新保存数据
     */
    private Observable<String> getAndSaveNetData(final BacHttpBean bean, AppCompatActivity activity,
                                                 final DialogInterface.OnClickListener positive,
                                                 final DialogInterface.OnClickListener negative,
                                                 final DialogInterface.OnCancelListener cancel) {
        return HttpHelper.getInstance()
                .bacNetWithContextWithOnClickListener(activity, bean, positive, negative, cancel)
                /*.doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        // 内存缓存
                        memCache.put(bean.getMethodName(), s);
                        // 本地缓存
                    }
                })*/
                ;
    }


}
