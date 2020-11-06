package com.bac.commonlib.capture;

/**
 * Created by wujiazhen on 2017/6/27.
 */

public class ClipImageBean {

    private float ratio = 1.6f;
    private int padding;
    private String url;

    public ClipImageBean(float ratio, int padding, String url) {
        this.ratio = ratio;
        this.padding = padding;
        this.url = url;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
