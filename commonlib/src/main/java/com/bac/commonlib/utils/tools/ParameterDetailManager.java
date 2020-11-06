package com.bac.commonlib.utils.tools;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;

import com.stericson.RootTools.RootTools;

import java.io.DataOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.stericson.RootTools.RootTools.getWorkingToolbox;
import static com.stericson.RootTools.RootTools.remount;
import static com.stericson.RootTools.RootTools.sendShell;

/**
 * Created by Wjz on 2017/6/2.
 * <p>
 * 1. 本机上所安装的app
 * 2. 本机是否root
 * 3. 本机上的传感器
 * 4. 本机上的摄像头
 * 5. wifi 信息
 */

public class ParameterDetailManager {
    private Context context;

    private final Map<String, Object> hm = new LinkedHashMap<>();
    private final AppInfoTool appInfoTool;
    private final List<BacAppInfo> existRootPkList;
    private final List<BacAppInfo> existJingLingPkList;
    private final SensorInfoTool sensorInfoTool;
    private final WifiInfoTool wifiInfoTool;
    private boolean canRootExe;

    private final String[] suPaths = {
            "/system/bin/su",
            "/system/xbin/su",
            "/system/sbin/su",
            "/sbin/su",
            "/vendor/bin/su"};
    private final List<String> suPathList;

    public ParameterDetailManager(Context context) {
        this.context = context;
        appInfoTool = new AppInfoTool(context);

        sensorInfoTool = new SensorInfoTool(context);
        wifiInfoTool = new WifiInfoTool(context);

        existRootPkList = new ArrayList<>();
        existJingLingPkList = new ArrayList<>();

        suPathList = Arrays.asList(suPaths);
    }

    public Map<String, Object> execute() {
        // 获取 appInfo
        hm.put("ApplicationInfo", appInfoTool.getAllAppInfo(existRootPkList, existJingLingPkList));
        hm.put("RootAppInfo", existRootPkList);

        // 获取传感器
        hm.put("SensorInfo", sensorInfoTool.getSensorInfo());

        // 摄像头
        hm.put("BackFacingCamera", hasBackFacingCamera());
        hm.put("FrontFacingCamera", hasFrontFacingCamera());
        // 获取wifi
        hm.put("WiFiInfo", wifiInfoTool.getWifiInfo());
        // root
        try {
            Runtime.getRuntime().exec("su");
            hm.put("RootInfo", canRootExe = true);
        } catch (Exception e) {
            hm.put("RootInfo", canRootExe = false);
        }
        return hm;
    }

    public boolean isCanRootExe() {
        return canRootExe;
    }

    /**
     * 检测摄像头
     *
     * @param facing
     * @return
     */
    private boolean checkCameraFacing(final int facing) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            return false;
        }
        final int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (facing == info.facing) {
                return true;
            }
        }
        return false;
    }

    public boolean hasBackFacingCamera() {
        final int CAMERA_FACING_BACK = 0;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }

    public boolean hasFrontFacingCamera() {
        final int CAMERA_FACING_BACK = 1;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }


    // root

    private boolean isRootSystem() {
        File rootSystem1 = isRootSystem1();
        File rootSystem2 = isRootSystem2();

        //System.out.println("rootSystem1:" + rootSystem1.getAbsolutePath());
        //System.out.println("rootSystem2:" + rootSystem2.getAbsolutePath());

        // 可加其他判断 如是否装了权限管理的apk，大多数root 权限 申请需要app配合，也有不需要的，这个需要改su源码。因为管理su权限的app太多，无法列举所有的app，特别是国外的，暂时不做判断是否有root权限管理app
// 多数只要su可执行就是root成功了，但是成功后用户如果删掉了权限管理的app，就会造成第三方app无法申请root权限，此时是用户删root权限管理app造成的。
// 市场上常用的的权限管理app的包名
// com.qihoo.permmgr
// com.noshufou.android.su
// eu.chainfire.supersu
// com.kingroot.kinguser
// com.kingouser.com
// com.koushikdutta.superuser
// com.dianxinos.superuser
// com.lbe.security.shuame
// com.geohot.towelroot
//silentUninstall(rootSystem1, rootSystem2);
        return rootSystem1 != null || rootSystem2 != null || isRootSystem3();
    }

    private File isRootSystem1() {
        File f = null;
        final String[] kSuSearchPaths = {"/system/bin/", "/system/xbin/",
                "/system/sbin/", "/sbin/", "/vendor/bin/"};
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists() && f.canExecute()) {
                    return f;
                }
            }
        } catch (Exception e) {
        }
        return f;
    }

    private File isRootSystem2() {
        List<String> pros = Arrays.asList(System.getenv("PATH").split(":"));
        File f = null;
        try {
            for (int i = 0; i < pros.size(); i++) {
                f = new File(pros.get(i), "su");
                if (f != null && f.exists() && f.canExecute()) {
                    return f;
                }
            }
        } catch (Exception e) {
        }
        return f;
    }

    private boolean isRootSystem3() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            process.getOutputStream().write("exit\n".getBytes());
            process.getOutputStream().flush();
            int i = process.waitFor();
            if (0 == i) {
                process = Runtime.getRuntime().exec("su");
                return true;
            }

        } catch (Exception e) {
            return false;
        }
        return false;

    }

    public boolean checkRootPermission() {
        return RootTools.checkUtil("su");
    }

    public void silentUninstall() {
        try {
            // 1. 删除精灵软件
            for (BacAppInfo bacAppInfo : existJingLingPkList) {
                RootTools.sendShell("pm uninstall " + bacAppInfo.getAppPackage(), 30000);
            }

            // 2. 删除root软件
            for (BacAppInfo bacAppInfo : existRootPkList) {
                RootTools.sendShell("pm uninstall " + bacAppInfo.getAppPackage(), 30000);
            }

            if (suPathList.size() > 0) {
                String workingToolbox = getWorkingToolbox();
                remount("/system", "rw");
                // 3. 删除 su 文件
                for (String s : suPathList) {
                    sendShell(workingToolbox+" rm " + s, 300000);
                }
                //remount("/system", "ro");
            }
        } catch (Exception e) {
        }
    }


    public void unInstall() {
        DataOutputStream dataOutputStream = null;
        try {
            Process process = Runtime.getRuntime().exec("su");

            dataOutputStream = new DataOutputStream(process.getOutputStream());
            String workingToolbox = getWorkingToolbox();
            remount("/system", "rw");
            // 3. 删除 su 文件
            String command = workingToolbox + " rm /system/xbin/su" ;


            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

    }


}
