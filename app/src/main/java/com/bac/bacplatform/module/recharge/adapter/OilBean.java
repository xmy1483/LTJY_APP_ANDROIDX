package com.bac.bacplatform.module.recharge.adapter;

/**
 * Created by Wjz on 2017/5/21.
 */

public class OilBean {


    /**
     * benefit_money : 100.0
     * oil_type : 20
     * product_id : 6
     * product_name : 100元
     * recharge_money : 100.0
     * sale_money : 100.0
     * status : 1
     * isSelected : true
     */

    private double benefit_money;
    private int oil_type;
    private int product_id;
    private String product_name;
    private double recharge_money;
    private double sale_money;
    private int status;
    private boolean isSelected;
    private boolean is_show;
    private boolean is_nine;
    private String jump_url;
    private boolean isAnyMoney = false;

    private String discount_img_path;//折扣图片地址

    public void setDiscount_img_path(String setDiscount_img_path) {
        this.discount_img_path = setDiscount_img_path;
    }

    public String getDiscount_img_path() {
        return discount_img_path;
    }

    public void setAnyMoney(boolean anyMoney) {
        isAnyMoney = anyMoney;
    }

    public boolean isAnyMoney() {
        return isAnyMoney;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    private int discount;


    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    public boolean getIs_nine() {
        return is_nine;
    }

    public void setIs_nine(boolean is_nine) {
        this.is_nine = is_nine;
    }



    public double getBenefit_money() {
        return benefit_money;
    }

    public void setBenefit_money(double benefit_money) {
        this.benefit_money = benefit_money;
    }

    public int getOil_type() {
        return oil_type;
    }

    public void setOil_type(int oil_type) {
        this.oil_type = oil_type;
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

    public double getRecharge_money() {
        return recharge_money;
    }

    public void setRecharge_money(double recharge_money) {
        this.recharge_money = recharge_money;
    }

    public double getSale_money() {
        return sale_money;
    }

    public void setSale_money(double sale_money) {
        this.sale_money = sale_money;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean is_show() {
        return is_show;
    }

    public void setIs_show(boolean is_show) {
        this.is_show = is_show;
    }
}
