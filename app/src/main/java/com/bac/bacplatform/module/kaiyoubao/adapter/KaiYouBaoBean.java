package com.bac.bacplatform.module.kaiyoubao.adapter;

import android.util.SparseArray;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Wjz on 2017/5/4.
 */

public class KaiYouBaoBean implements MultiItemEntity {

    private int itemType;
    private SparseArray<String> sparseArray;

    public KaiYouBaoBean(int itemType, SparseArray<String> sparseArray) {
        this.itemType = itemType;
        this.sparseArray = sparseArray;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public SparseArray<String> getSparseArray() {
        return sparseArray;
    }

    public void setSparseArray(SparseArray<String> sparseArray) {
        this.sparseArray = sparseArray;
    }
}
