package com.bac.bacplatform.weex_bean;

public class AnyCountBean {
    private double recharge;
    private double pay;
    private int discount;
    private String discount_img_path;

    public void setRecharge(double recharge) {
        this.recharge = recharge;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setDiscount_img_path(String discount_img_path) {
        this.discount_img_path = discount_img_path;
    }

    public void setVoucherCount(int voucherCount) {
        this.voucherCount = voucherCount;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public double getRecharge() {
        return recharge;
    }

    public double getPay() {
        return pay;
    }

    public int getDiscount() {
        return discount;
    }

    public String getDiscount_img_path() {
        return discount_img_path;
    }

    public int getVoucherCount() {
        return voucherCount;
    }

    public String getVoucherId() {
        return voucherId;
    }

    private int voucherCount;
    private String voucherId;


}
