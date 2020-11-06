package com.bac.bacplatform.old.module.insurance.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Wjz on 2017/5/17.
 */

public class DetailRightBean<T> implements MultiItemEntity {
    public static final int HEADER  = 0;
    public static final int TITLE   = 1;
    public static final int CONTEXT = 2;
    public static final int FOOTER  = 3;

    private int    itemType;
    private String title;
    private T      data;

    @Override
    public int getItemType() {
        return itemType;
    }

    public DetailRightBean(int itemType, T data) {
        this.itemType = itemType;
        this.data = data;
    }

    public DetailRightBean(int itemType, String title, T data) {
        this.itemType = itemType;
        this.title = title;
        this.data = data;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}