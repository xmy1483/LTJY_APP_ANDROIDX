package com.bac.bacplatform.old.module.exchange.adapter;

import com.bac.bacplatform.old.base.SuperBean;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.beans
 * 创建人：Wjz
 * 创建时间：2016/9/21
 * 类描述：
 */

public class ItemExchangeBean extends SuperBean {

    private int    bg;

    private float  charge_money;//"    : 0.01,
    private float  pay_money;//"       : 0.01,
    private String product_id;//"      : "EQY20160912140603954073",
    private String product_name;//"    : "测试2",
    private int    product_status;//"  : 1



    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public float getCharge_money() {
        return charge_money;
    }

    public void setCharge_money(float charge_money) {
        this.charge_money = charge_money;
    }

    public float getPay_money() {
        return pay_money;
    }

    public void setPay_money(float pay_money) {
        this.pay_money = pay_money;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_status() {
        return product_status;
    }

    public void setProduct_status(int product_status) {
        this.product_status = product_status;
    }
}
