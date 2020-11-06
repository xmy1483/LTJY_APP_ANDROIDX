package com.bac.bacplatform.view.address;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.beans
 * 创建人：Wjz
 * 创建时间：2017/2/17
 * 类描述：
 */

public class CardInfoBean implements Parcelable {


    /**
     * code : 0
     * msg :
     * province :
     * city :
     * area :
     * image_infos : [{"image_name":"办卡人身份证正面照","image_type":100,"upload":false},{"image_name":"办卡人身份证反面照","image_type":101,"upload":false}]
     * order_id : 1702171049013847243
     */

    private int code;
    private String msg;
    private String province;
    private String city;
    private String area;
    private long                 order_id;
    private List<ImageInfosBean> image_infos;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public List<ImageInfosBean> getImage_infos() {
        return image_infos;
    }

    public void setImage_infos(List<ImageInfosBean> image_infos) {
        this.image_infos = image_infos;
    }

    public static class ImageInfosBean implements Parcelable {
        /**
         * image_name : 办卡人身份证正面照
         * image_type : 100
         * upload : false
         */

        private String image_name;
        private int     image_type;
        private boolean upload;

        public String getImage_name() {
            return image_name;
        }

        public void setImage_name(String image_name) {
            this.image_name = image_name;
        }

        public int getImage_type() {
            return image_type;
        }

        public void setImage_type(int image_type) {
            this.image_type = image_type;
        }

        public boolean isUpload() {
            return upload;
        }

        public void setUpload(boolean upload) {
            this.upload = upload;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.image_name);
            dest.writeInt(this.image_type);
            dest.writeByte(this.upload ? (byte) 1 : (byte) 0);
        }

        public ImageInfosBean() {
        }

        protected ImageInfosBean(Parcel in) {
            this.image_name = in.readString();
            this.image_type = in.readInt();
            this.upload = in.readByte() != 0;
        }

        public static final Creator<ImageInfosBean> CREATOR = new Creator<ImageInfosBean>() {
            @Override
            public ImageInfosBean createFromParcel(Parcel source) {
                return new ImageInfosBean(source);
            }

            @Override
            public ImageInfosBean[] newArray(int size) {
                return new ImageInfosBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.msg);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.area);
        dest.writeLong(this.order_id);
        dest.writeList(this.image_infos);
    }

    public CardInfoBean() {
    }

    protected CardInfoBean(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.area = in.readString();
        this.order_id = in.readLong();
        this.image_infos = new ArrayList<ImageInfosBean>();
        in.readList(this.image_infos, ImageInfosBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<CardInfoBean> CREATOR = new Parcelable.Creator<CardInfoBean>() {
        @Override
        public CardInfoBean createFromParcel(Parcel source) {
            return new CardInfoBean(source);
        }

        @Override
        public CardInfoBean[] newArray(int size) {
            return new CardInfoBean[size];
        }
    };
}
