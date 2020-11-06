package com.bac.bacplatform.old.module.bihupapa.domain;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.beans.adv
 * 创建人：Wjz
 * 创建时间：2017/4/14
 * 类描述：
 */

public class CarAdvHomeBean {


    /**
     * adv_id : 10010001
     * adv_remark : 移动200M活动
     * adv_title : 移动200M活动
     * end_time : 1494575991000
     * start_time : 1491983988000
     * status : 10
     */

    private long adv_id;
    private String adv_remark;
    private String adv_title;
    private long   end_time;
    private long   start_time;
    private int    status;

    public long getAdv_id() {
        return adv_id;
    }

    public void setAdv_id(long adv_id) {
        this.adv_id = adv_id;
    }

    public String getAdv_remark() {
        return adv_remark;
    }

    public void setAdv_remark(String adv_remark) {
        this.adv_remark = adv_remark;
    }

    public String getAdv_title() {
        return adv_title;
    }

    public void setAdv_title(String adv_title) {
        this.adv_title = adv_title;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
