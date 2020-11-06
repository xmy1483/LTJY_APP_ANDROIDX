package com.bac.commonlib.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * 项目名称：CustomCamera
 * 包名：com.wjz.www.customcamera.camera3
 * 创建人：Wjz
 * 创建时间：2017/2/13
 * 类描述：
 */

public class CameraManager3 {
    private static CameraManager3 CameraManager3;

    static final int SDK_INT;

    static {
        int sdkInt;
        try {
            sdkInt = android.os.Build.VERSION.SDK_INT;
        } catch (NumberFormatException nfe) {
            sdkInt = 10000;
        }
        SDK_INT = sdkInt;
    }

    private final CameraConfigurationManager3 configManager;
    private Camera camera;
    private boolean initialized;
    private boolean previewing;
    private static Context mContext;

    private Camera.Parameters parameter;

    public static void init(Context context) {
        if (CameraManager3 == null) {
            CameraManager3 = new CameraManager3(context);
            mContext = context;
        }
    }

    public static CameraManager3 get() {
        return CameraManager3;
    }

    private CameraManager3(Context context) {
        this.configManager = new CameraConfigurationManager3(context);

    }


    public void takePic(final Camera.PictureCallback callback, final OnAutoFocusCallback onAutoFocusCallback) {
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, final Camera camera) {
                if (success) {

                    camera.takePicture(null, null, callback);
                }else{
                    if (onAutoFocusCallback!=null) {
                        onAutoFocusCallback.onAutoFocusCallback(success,camera);
                    }
                }
            }
        });
    }


    public void openDriver(SurfaceHolder holder) throws IOException {
        if (camera == null) {
            camera = Camera.open();
            if (camera == null) {
                throw new IOException();
            }
            camera.setPreviewDisplay(holder);

            if (!initialized) {
                initialized = true;
                configManager.initFromCameraParameters(camera);
            }
            configManager.setDesiredCameraParameters(camera);

        }
    }

    public Point getCameraResolution() {
        return configManager.getCameraResolution();
    }

    public void closeDriver() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    public void startPreview() {
        if (camera != null && !previewing) {
            camera.cancelAutoFocus();
            camera.startPreview();
            previewing = true;
        }
    }

    public void stopPreview() {
        if (camera != null && previewing) {

            camera.stopPreview();

            previewing = false;
        }
    }

    public void openLight() {
        if (camera != null) {
            parameter = camera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameter);
        }
    }

    public void offLight() {
        if (camera != null) {
            parameter = camera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameter);
        }
    }

    public interface OnAutoFocusCallback {
        void onAutoFocusCallback(boolean b,Camera camera);
    }
}
