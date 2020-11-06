package com.bac.commonlib.utils.tools;

/**
 * Created by wujiazhen on 2017/6/30.
 */

public class BacSensorInfo {

    private int type;
    private String sensorName;
    private String productName;

    public int getType() {
        return type;
    }

    public BacSensorInfo setType(int type) {
        this.type = type;
        return this;
    }

    public String getSensorName() {
        return sensorName;
    }

    public BacSensorInfo setSensorName(String sensorName) {
        this.sensorName = sensorName;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public BacSensorInfo setProductName(String productName) {
        this.productName = productName;
        return this;
    }
}
