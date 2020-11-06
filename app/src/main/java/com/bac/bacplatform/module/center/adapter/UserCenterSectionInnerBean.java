package com.bac.bacplatform.module.center.adapter;

/**
 * Created by Wjz on 2017/5/4.
 */

public class UserCenterSectionInnerBean {

    private int id;
    private boolean isFull;

    private String label_1;
    private String label_2;
    private int index;

    public UserCenterSectionInnerBean(int id, boolean isFull) {
        this.id = id;
        this.isFull = isFull;

    }

    public UserCenterSectionInnerBean(int id, boolean isFull, String label_1, String label_2,int index) {
        this.id = id;
        this.isFull = isFull;
        this.label_1 = label_1;
        this.label_2 = label_2;
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public String getLabel_1() {
        return label_1;
    }

    public void setLabel_1(String label_1) {
        this.label_1 = label_1;
    }

    public String getLabel_2() {
        return label_2;
    }

    public void setLabel_2(String label_2) {
        this.label_2 = label_2;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "UserCenterSectionInnerBean{" +
                "id=" + id +
                ", isFull=" + isFull +
                ", label_1='" + label_1 + '\'' +
                ", label_2='" + label_2 + '\'' +
                ", index=" + index +
                '}';
    }
}
