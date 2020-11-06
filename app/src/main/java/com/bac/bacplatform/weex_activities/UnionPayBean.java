package com.bac.bacplatform.weex_activities;

public class UnionPayBean {
    private String merchantOrderId;
    private String orderTime;
    private String payPlatform;

    public UnionPayBean(String tn, String orderTime,String payPlatform) {
        this.merchantOrderId = tn;
        this.orderTime = orderTime;
        this.payPlatform = payPlatform;
    }

    public String getPayPlatform() {
        return payPlatform;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public String getOrderTime() {
        return orderTime;
    }
}
