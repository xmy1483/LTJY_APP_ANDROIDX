package com.bac.bacinnermanager.utils;

import java.io.Serializable;

/**
 * Created by guke on 2017/8/16.
 */

public class DataBean implements Serializable{

    /**
     * method_id : 2
     * param_key : adv_activity_id
     * param_type : i
     * param_value : 1
     * remark : 广告活动id
     */

    private int method_id;
    private String param_key;
    private String param_type;
    private String param_value;
    private String remark;

    public int getMethod_id() {
        return method_id;
    }

    public void setMethod_id(int method_id) {
        this.method_id = method_id;
    }

    public String getParam_key() {
        return param_key;
    }

    public void setParam_key(String param_key) {
        this.param_key = param_key;
    }

    public String getParam_type() {
        return param_type;
    }

    public void setParam_type(String param_type) {
        this.param_type = param_type;
    }

    public String getParam_value() {
        return param_value;
    }

    public void setParam_value(String param_value) {
        this.param_value = param_value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

//    @Override
//    public String toString() {
//        return "DataBean{" +
//                "method_id=" + method_id +
//                ", param_key='" + param_key + '\'' +
//                ", param_type='" + param_type + '\'' +
//                ", param_value='" + param_value + '\'' +
//                ", remark='" + remark + '\'' +
//                '}';
//    }
}
