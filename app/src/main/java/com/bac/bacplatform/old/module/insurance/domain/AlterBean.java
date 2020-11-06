package com.bac.bacplatform.old.module.insurance.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Wjz on 2017/5/17.
 */

public  class AlterBean implements MultiItemEntity {
    public static final int HEADER  = 0;
    public static final int TITLE   = 1;
    public static final int CONTEXT = 2;

    private int                             itemType;
    private boolean                         is_payment_efc;
    public boolean                         is_payment_tax;
    private String header;
    public InsurancePlanBean.RiskKindsBean data;

    public AlterBean(int itemType, boolean b_efc, boolean b_tax) {
        this.itemType = itemType;
        this.is_payment_efc = b_efc;
        this.is_payment_tax = b_tax;
    }

    public AlterBean(int itemType, String header) {
        this.itemType = itemType;
        this.header = header;
    }

    public AlterBean(int itemType, InsurancePlanBean.RiskKindsBean data) {
        this.itemType = itemType;
        this.data = data;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public boolean is_payment_efc() {
        return is_payment_efc;
    }

    public void setIs_payment_efc(boolean is_payment_efc) {
        this.is_payment_efc = is_payment_efc;
    }

    public boolean is_payment_tax() {
        return is_payment_tax;
    }

    public void setIs_payment_tax(boolean is_payment_tax) {
        this.is_payment_tax = is_payment_tax;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public InsurancePlanBean.RiskKindsBean getData() {
        return data;
    }

    public void setData(InsurancePlanBean.RiskKindsBean data) {
        this.data = data;
    }
}
