package com.bac.bacplatform.weex_activities;

class PayOrderBean {
    private String orderMoney;
    private String cardNum;

    PayOrderBean(String money, String cardNum) {
        this.orderMoney = money;
        this.cardNum = cardNum;
    }

    String getOrderMoney() {
        return orderMoney;
    }

    String getCardNum() {
        return cardNum;
    }
}
