package com.bac.commonlib.camera;

/**
 * Created by Wjz on 2017/5/11.
 */

public class CameraMeasureBean {
    private int mSvH;
    private int mSvW;
    private int mHeight;
    private int mWidth;
    private float mY;
    private float mX;

    public CameraMeasureBean(int mSvW, int mSvH, int mWidth, int mHeight, float mX, float mY) {
        this.mSvW = mSvW;
        this.mSvH = mSvH;
        this.mHeight = mHeight;
        this.mWidth = mWidth;
        this.mY = mY;
        this.mX = mX;
    }

    public int getmSvH() {
        return mSvH;
    }

    public void setmSvH(int mSvH) {
        this.mSvH = mSvH;
    }

    public int getmSvW() {
        return mSvW;
    }

    public void setmSvW(int mSvW) {
        this.mSvW = mSvW;
    }

    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public float getmY() {
        return mY;
    }

    public void setmY(float mY) {
        this.mY = mY;
    }

    public float getmX() {
        return mX;
    }

    public void setmX(float mX) {
        this.mX = mX;
    }
}
