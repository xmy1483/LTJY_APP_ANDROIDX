package com.bac.bacplatform.old.module.flow;

import com.bac.bacplatform.old.base.SuperBean;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.beans
 * 创建人：Wjz
 * 创建时间：2016/9/21
 * 类描述：
 */

public class ItemFlowBean extends SuperBean {


    /**
     * discount_price : 9.5
     * product_id : 1
     * donate_money : 1.0
     * market_price : 10.0
     * type : 0
     * product_name : 100M
     * status : 1
     */
    private int    bg;
    /**
     * discount_price : 47.5
     * donate_money : 10.0
     * market_price : 50.0
     * price : 37.5
     * product_id : 4
     * product_name : 1G
     * type : 0
     */

    private double discount_price;
    private double donate_money;
    private double market_price;
    private double price;
    private int    product_id;
    private String product_name;
    private String product_remark;
    private int    type;
    private int label;
    private boolean is_enable;
    private String enable_remark;

    public boolean is_enable() {
        return is_enable;
    }

    public void setIs_enable(boolean is_enable) {
        this.is_enable = is_enable;
    }

    public String getEnable_remark() {
        return enable_remark;
    }

    public void setEnable_remark(String enable_remark) {
        this.enable_remark = enable_remark;
    }

    public String getProduct_remark() {
        return product_remark;
    }

    public void setProduct_remark(String product_remark) {
        this.product_remark = product_remark;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }


    public double getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(double discount_price) {
        this.discount_price = discount_price;
    }

    public double getDonate_money() {
        return donate_money;
    }

    public void setDonate_money(double donate_money) {
        this.donate_money = donate_money;
    }

    public double getMarket_price() {
        return market_price;
    }

    public void setMarket_price(double market_price) {
        this.market_price = market_price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
