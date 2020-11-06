package com.bac.commonlib.utils.tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wujiazhen on 2017/7/10.
 */

public class AppInfoTool {

    private String[] rootApkName = {
            "com.qihoo.permmgr",
            "com.noshufou.android.su",
            "eu.chainfire.supersu",
            "com.kingroot.kinguser",
            "com.kingouser.com",
            "com.koushikdutta.superuser",
            "com.dianxinos.superuser",
            "com.lbe.security.shuame",
            "com.geohot.towelroot"
    };

    private String[] jlName = {
            "net.aisence.Touchelper",
            "com.cyjh.mobileanjian",
            "com.scriptelf",
            "com.touchsprite.android"
    };
    private Context context;
    private final List<String> rootApkNameList;
    private final List<String> jlNameList;

    public AppInfoTool(Context context) {
        this.context = context;
        rootApkNameList = Arrays.asList(rootApkName);
        jlNameList = Arrays.asList(jlName);
    }

    public List<BacAppInfo> getAllAppInfo(List<BacAppInfo> existRootPkList, List<BacAppInfo> existJingLingPkList) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(0);
        // 所有的 appInfo
        List<BacAppInfo> bacAppInfoList = new ArrayList<>(apps.size());
        for (ApplicationInfo app : apps) {
            String packageName = app.packageName;
            // rootApk
            if (rootApkNameList.contains(packageName)) {
                existRootPkList.add(new BacAppInfo().setAppName(app.loadLabel(packageManager)).setAppPackage(packageName).setDataDir(app.dataDir));
            }
            // jingling
            if (jlNameList.contains(packageName)) {
                existJingLingPkList.add(new BacAppInfo().setAppName(app.loadLabel(packageManager)).setAppPackage(packageName).setDataDir(app.dataDir));
            }
            bacAppInfoList.add(new BacAppInfo().setAppName(app.loadLabel(packageManager)).setAppPackage(packageName).setDataDir(app.dataDir));
        }
        return bacAppInfoList;
    }
}
