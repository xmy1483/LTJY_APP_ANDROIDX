package com.bac.rxbaclib.rx.permission;


import android.annotation.TargetApi;
import android.os.Build;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

/**
 * Created by Wjz on 2017/5/9.
 * 使用 fragment 请求权限
 */

public class RxPermissionImpl {
    private static final String TAG = RxPermissionImpl.class.getName();
    private final RxPermissionFragment rxPermissionFragment;

    public RxPermissionImpl(AppCompatActivity activity) {
        rxPermissionFragment = getRxPermissionFragment(activity);
    }

    private RxPermissionFragment getRxPermissionFragment(AppCompatActivity activity) {

        RxPermissionFragment rxPermissionFragment = findRxPermissionFragment(activity);
        if (rxPermissionFragment == null) {
            // create
            rxPermissionFragment = new RxPermissionFragment();
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            supportFragmentManager
                    .beginTransaction()
                    .add(rxPermissionFragment, TAG)
                    .commitAllowingStateLoss();
            supportFragmentManager.executePendingTransactions();
        }

        return rxPermissionFragment;
    }

    private RxPermissionFragment findRxPermissionFragment(AppCompatActivity activity) {

        return (RxPermissionFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
    }

    /**
     * 多个权限请求
     *
     * @param permissions
     * @return
     */
    @SuppressWarnings("WeakerAccess")
    public Observable.Transformer<Object, Boolean> ensure(final String... permissions) {
        return new Observable.Transformer<Object, Boolean>() {
            @Override
            public Observable<Boolean> call(Observable<Object> o) {
                return request(o, permissions)
                        // Transform Observable<Permission> to Observable<Boolean>
                        .buffer(permissions.length) //它定期从Observable收集数据到一个集合，然后把这些数据集合打包发 射，而不是一次发射一个
                        .flatMap(new Func1<List<Permission>, Observable<Boolean>>() {
                            @Override
                            public Observable<Boolean> call(List<Permission> permissionList) {
                                if (permissionList.isEmpty()) {
                                    // Occurs during orientation change, when the subject receives onComplete.
                                    // In that case we don't want to propagate that empty list to the
                                    // subscriber, only the onComplete.
                                    return Observable.empty();
                                }
                                // Return true if all permissionList are granted.
                                for (Permission p : permissionList) {
                                    if (!p.granted) {
                                        return Observable.just(false);
                                    }
                                }
                                return Observable.just(true);
                            }
                        });
            }
        };
    }

    /**
     * 单个权限
     *
     * @param permissions
     * @return
     */
    @SuppressWarnings("WeakerAccess")
    public Observable.Transformer<Object, Permission> ensureEach(final String... permissions) {
        return new Observable.Transformer<Object, Permission>() {
            @Override
            public Observable<Permission> call(Observable<Object> o) {
                return request(o, permissions);
            }
        };
    }

    /**
     * 权限请求
     *
     * @param trigger
     * @param permissions
     * @return
     */
    Observable<Permission> request(final Observable<?> trigger, final String... permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("RxPermissions.request/requestEach requires at least one input permission");
        }
        /*Observable<?> observable = oneOf(trigger, pending(permissions));
        observable.flatMap(new Func1<Object, Observable<?>>() {
            @Override
            public Observable<?> call(Object o) {
                return null;
            }
        })*/
        return oneOf(trigger, pending(permissions))
                .flatMap(new Func1<Object, Observable<Permission>>() {
                    @Override
                    public Observable<Permission> call(Object o) {
                        // 处理权限请求
                        return requestImplementation(permissions);
                    }
                });
    }


    @SuppressWarnings({"WeakerAccess", "unused"})
    public Observable<Boolean> request(final String... permissions) {
        return Observable.just(null).compose(ensure(permissions));
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public Observable<Permission> requestEach(final String... permissions) {
        return Observable.just(null).compose(ensureEach(permissions));
    }

    /**
     * @param permissions
     * @return
     */
    private Observable<?> pending(final String... permissions) {
        // 判断当前权限是否已经存在
        for (String p : permissions) {
            if (!rxPermissionFragment.containsByPermission(p)) {
                return Observable.empty();
            }
        }
        return Observable.just(null);//不存在
    }

    /**
     * 将多个Observable 合并成一个Observable
     * 将Observable 与 请求的权限的Observable 组合
     *
     * @param trigger
     * @param pending
     * @return
     */
    private Observable<?> oneOf(Observable<?> trigger, Observable<?> pending) {
        if (trigger == null) {
            return Observable.just(null);
        }
        return Observable.merge(trigger, pending);
    }

    /**
     * 权限请求
     *
     * @param permissions 请求的权限 ，一般一个
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    Observable<Permission> requestImplementation(final String... permissions) {
        List<Observable<Permission>> list = new ArrayList<>(permissions.length);
        List<String> unrequestedPermissions = new ArrayList<>();

        // In case of multiple permissions, we create an Observable for each of them.
        // At the end, the observables are combined to have a unique response.
        for (String permission : permissions) {
            if (isGranted(permission)) {//允许
                // Already granted, or not Android M
                // Return a granted Permission object.
                list.add(Observable.just(new Permission(permission, true, false)));
                continue;
            }

            if (isRevoked(permission)) {//拒绝
                // Revoked by a policy, return a denied Permission object.
                list.add(Observable.just(new Permission(permission, false, false)));
                continue;
            }

            // 系统未处理过的权限
            PublishSubject<Permission> subject = rxPermissionFragment.getSubjectByPermission(permission);
            // Create a new subject if not exists
            if (subject == null) {
                unrequestedPermissions.add(permission);
                subject = PublishSubject.create();
                rxPermissionFragment.setSubjectForPermission(permission, subject);
            }

            list.add(subject);
        }

        // 未处理的权限 执行
        if (!unrequestedPermissions.isEmpty()) {
            String[] unrequestedPermissionsArray = unrequestedPermissions.toArray(new String[unrequestedPermissions.size()]);
            requestPermissionsFromFragment(unrequestedPermissionsArray);
        }

        return
                Observable.concat(
                        Observable.from(list)//多个Observable
                );// 不交错的连接多个Observable的数据
    }

    @TargetApi(Build.VERSION_CODES.M)
    void requestPermissionsFromFragment(String[] permissions) {
        rxPermissionFragment.requestPermissions(permissions);
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isGranted(String permission) {
        return !isMarshmallow() || rxPermissionFragment.isGranted(permission);
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isRevoked(String permission) {
        return isMarshmallow() && rxPermissionFragment.isRevoked(permission);
    }

    boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
