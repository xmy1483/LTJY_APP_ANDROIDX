package com.bac.commonlib.utils.tools;

/**
 * Created by wujiazhen on 2017/6/30.
 */

public class BacAppInfo {
    private CharSequence appName;
    private String appPackage;
    private String dataDir;

    public CharSequence getAppName() {
        return appName;
    }

    public BacAppInfo setAppName(CharSequence appName) {
        this.appName = appName;
        return this;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public BacAppInfo setAppPackage(String appPackage) {
        this.appPackage = appPackage;
        return this;
    }

    public String getDataDir() {
        return dataDir;
    }

    public BacAppInfo setDataDir(String dataDir) {
        this.dataDir = dataDir;
        return this;
    }

    @Override
    public String toString() {
        return "BacAppInfo{" +
                "appName=" + appName +
                ", appPackage='" + appPackage + '\'' +
                ", dataDir='" + dataDir + '\'' +
                '}';
    }
}
