package com.bac.bacplatform.bean;

import com.bac.bacplatform.R;

public class ExpressBean {
    private boolean showPoint;
    private String time_ymd;
    private String time_hms;
    private String situation;
    private String status;
    private int iconId;
    private int index;

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIconId() {
        setIconId();
        return iconId;
    }

    private void setIconId() {

        if(index == 0) {
            switch (status) {
                case "已签收":
                    iconId = R.mipmap.ic_tick;
                    break;
                case "派件中":
                    iconId = R.mipmap.ic_deliveray_blue;
                    break;
                case "运输中":
                    iconId = R.mipmap.ic_trans_blue;
                    break;
                case "已发货":
                    iconId = R.mipmap.ic_exp_fh_blue;
                    break;
                case "已下单":
                    iconId = R.mipmap.ic_order_blue;
                    break;
                default:
                    iconId = R.mipmap.ic_order_blue;
                    break;
            }
        } else {
            switch (status) {
                case "派件中":{
                    if(isShowPoint()) {
                        iconId = R.mipmap.ic_transing;
                    } else {
                        iconId = R.mipmap.ic_exp_ps;
                    }
                }
                break;
                case "运输中": {
                    if(isShowPoint()) {
                        iconId = R.mipmap.ic_transing;
                    } else {
                        iconId = R.mipmap.ic_trans_gray;
                    }
                }
                break;
                case "已发货": {
                    if(isShowPoint()) {
                        iconId = R.mipmap.ic_transing;
                    } else {
                        iconId = R.mipmap.ic_exp_fh;
                    }
                }
                break;
                case "已下单": {
                    if(isShowPoint()) {
                        iconId = R.mipmap.ic_transing;
                    } else {
                        iconId = R.mipmap.ic_order_gray;
                    }
                }
                break;
                default:
                    iconId = R.mipmap.ic_exp_fh;
                    break;
            }
        }

    }

    public boolean isShowPoint() {
        return showPoint;
    }

    public void setShowPoint(boolean showPoint) {
        this.showPoint = showPoint;
    }

    public String getTime_ymd() {
        if(time_ymd!=null && time_ymd.contains(" "))
        {
            return time_ymd.substring(0,time_ymd.lastIndexOf(" "));
        }
        return time_ymd;
    }

    public void setTime_ymd(String time_ymd) {
        this.time_ymd = time_ymd;
    }

    public String getTime_hms() {
        if(time_hms!=null && time_hms.length()>5) {
            time_hms = time_hms.substring(0,6);
        }
        return time_hms;
    }

    public void setTime_hms(String time_hms) {
        this.time_hms = time_hms;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
