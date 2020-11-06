package com.bac.commonlib.utils.tools;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujiazhen on 2017/7/10.
 */

public class SensorInfoTool {
    private Context context;

    public SensorInfoTool(Context context) {
        this.context = context;
    }

    public List<BacSensorInfo> getSensorInfo() {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        List<BacSensorInfo> bacSensorList = new ArrayList<>();
        for (Sensor sensor : sensorList) {
            int type = sensor.getType();
            String sensorName = getSensorName(type);
            String productName = sensor.getName();
            bacSensorList.add(new BacSensorInfo().setType(type).setSensorName(sensorName).setProductName(productName));
        }
        return bacSensorList;
    }

    private String getSensorName(int type) {
        String str = "";
        switch (type) {
            case Sensor.TYPE_ACCELEROMETER:
                str = "加速度传感器";
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                str = "温度传感器";
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                str = "用来探测运动而不必收到电磁干扰的影响，因为它并不依赖于磁北极";
                break;
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                str = "地磁旋转矢量传感器，提供手机的旋转矢量，当手机处于休眠状态时，任可以记录设备的方位";
                break;
            case 24:
                str = "快速手势（Android Lollipop新手势）支持短暂打开屏幕，以便用户根据特定动作浏览屏幕上的内容";
                break;
            case Sensor.TYPE_GRAVITY:
                str = "重力传感器";
                break;
            case Sensor.TYPE_GYROSCOPE:
                str = "陀螺仪传感器";
                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                str = "为校准陀螺仪传感器";
                break;
            case Sensor.TYPE_HEART_RATE:
                str = "心率传感器";
                break;
            case Sensor.TYPE_LIGHT:
                str = "光线传感器";
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                str = "线性加速度传感器";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                str = "磁力传感器";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                str = "为校准磁力传感器";
                break;
            case 25://TYPE_PICK_UP_GESTURE
                str = "设备抬起探测器（Android Lollipop 新手势）拾起设备时触发";
                break;
            case Sensor.TYPE_PRESSURE:
                str = "压力传感器";
                break;
            case Sensor.TYPE_PROXIMITY:
                str = "接近传感器";
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                str = "湿度传感器";
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                str = "旋转矢量传感器";
                break;
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                str = "特殊动作触发传感器";
                break;
            case Sensor.TYPE_STEP_COUNTER:
                str = "计步传感器";
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                str = "步行检测传感器";
                break;
            case 22:
                str = "倾斜探测器   （Android Lollipop 新手势）每次检测到倾斜时间后均生成事件";
                break;
            case 23:
                str = "唤醒手势传感器  （Android Lollipop 新手势）支持根据设备特定的动作唤醒设备";
                break;
            case Sensor.TYPE_ORIENTATION:
                str = "方向传感器";
                break;
            case Sensor.TYPE_TEMPERATURE:
                str = "温度传感器";
                break;
            case 27:
                str = "设备方向传感器";
                break;
            case 32:
                str = "传感器动态添加和删除";
                break;
        }
        return str;
    }
}
