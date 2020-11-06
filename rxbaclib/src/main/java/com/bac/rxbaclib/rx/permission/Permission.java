package com.bac.rxbaclib.rx.permission;

/**
 * Created by Wjz on 2017/5/9.
 */

public class Permission {
    public final String name;
    public final boolean granted;//是否允许
    public final boolean shouldShowRequestPermissionRationale;// 是否显示请求理由

    /*构造方法*/
    public Permission(String name, boolean granted) {
        this(name, granted, false);
    }

    public Permission(String name, boolean granted, boolean shouldShowRequestPermissionRationale) {
        this.name = name;
        this.granted = granted;
        this.shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale;
    }

    public String getName() {
        return name;
    }

    public boolean isGranted() {
        return granted;
    }

    public boolean isShouldShowRequestPermissionRationale() {
        return shouldShowRequestPermissionRationale;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "name='" + name + '\'' +
                ", granted=" + granted +
                ", shouldShowRequestPermissionRationale=" + shouldShowRequestPermissionRationale +
                '}';
    }
}
