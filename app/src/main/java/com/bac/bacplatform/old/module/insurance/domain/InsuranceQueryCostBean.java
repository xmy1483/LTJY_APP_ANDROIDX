package com.bac.bacplatform.old.module.insurance.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Wjz on 2017/5/17.
 */

public class InsuranceQueryCostBean<T> implements MultiItemEntity {

    public static final int ONE   = 1;
    public static final int TWO   = 2;
    public static final int THREE = 3;
    public static final int FOUR  = 4;
    public static final int FIVE  = 5;
    private int    itemType;
    private String str1;
    private String str2;
    private String str3;
    private T      data;

    public InsuranceQueryCostBean(int itemType, T data) {
        this.itemType = itemType;
        this.data = data;
    }

    public InsuranceQueryCostBean(int itemType, String str1, String str2) {
        this.itemType = itemType;
        this.str1 = str1;
        this.str2 = str2;
    }

    public InsuranceQueryCostBean(int itemType, String str1, String str2, String str3) {
        this.itemType = itemType;
        this.str1 = str1;
        this.str2 = str2;
        this.str3 = str3;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStr3() {
        return str3;
    }

    public void setStr3(String str3) {
        this.str3 = str3;
    }
}
