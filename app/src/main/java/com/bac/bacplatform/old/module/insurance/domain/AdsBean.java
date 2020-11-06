package com.bac.bacplatform.old.module.insurance.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.beans
 * 创建人：Wjz
 * 创建时间：2016/9/30
 * 类描述：
 */

public class AdsBean implements Parcelable {


    private String http_url;
    private String image_url;

    public String getHttp_url() {
        return http_url;
    }

    public void setHttp_url(String http_url) {
        this.http_url = http_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.http_url);
        dest.writeString(this.image_url);
    }

    public AdsBean() {
    }

    protected AdsBean(Parcel in) {
        this.http_url = in.readString();
        this.image_url = in.readString();
    }

    public static final Parcelable.Creator<AdsBean> CREATOR = new Parcelable.Creator<AdsBean>() {
        @Override
        public AdsBean createFromParcel(Parcel source) {
            return new AdsBean(source);
        }

        @Override
        public AdsBean[] newArray(int size) {
            return new AdsBean[size];
        }
    };
}
