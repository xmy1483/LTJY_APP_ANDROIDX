package com.bac.bacplatform.weex_utils;

public class PayBean {
    private String cardNum, oilTYpe, platform, recharge, saleMoney, shouldPay,voucherId;
    private double voucherCount;

    public PayBean(String cardNum, String oilTYpe, String platform, String recharge, String saleMoney, String shouldPay, String voucherId, double voucherCount) {
        this.cardNum = cardNum;
        this.oilTYpe = oilTYpe;
        this.platform = platform;
        this.recharge = recharge;
        this.saleMoney = saleMoney;
        this.shouldPay = shouldPay;
        this.voucherId = voucherId;
        this.voucherCount = voucherCount;
    }

    public String getCardNum() {
        return cardNum;
    }

    public String getOilTYpe() {
        return oilTYpe;
    }

    public String getPlatform() {
        return platform;
    }

    public double getRecharge() {
        double temp;
        try{
            temp = Double.parseDouble(recharge);
        }catch (Exception e){
            temp = -1;
        }
        return temp;
    }

    public double getSaleMoney() {
        double temp;
        try{
            temp = Double.parseDouble(saleMoney);
        }catch (Exception e){
            temp = -1;
        }
        return temp;
    }

    public double getShouldPay() {
        double temp;
        try{
            temp = Double.parseDouble(shouldPay);
        }catch (Exception e){
            temp = -1;
        }
        return temp;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public double getVoucherCount() {
        return voucherCount;
    }
}
