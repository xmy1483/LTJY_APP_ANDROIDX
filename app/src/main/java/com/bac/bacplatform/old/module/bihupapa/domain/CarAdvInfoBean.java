package com.bac.bacplatform.old.module.bihupapa.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.beans.adv
 * 创建人：Wjz
 * 创建时间：2017/4/14
 * 类描述：
 */

public class CarAdvInfoBean implements Parcelable {


    /**
     * adv_id : 10010001
     * adv_title :
     * car_license_no : 粤W3B103
     * code : 0
     * image_infos : [{"image_name":"车辆左前45度","image_type":201,"upload":false},{"image_name":"车辆右后45度","image_type":202,"upload":false}]
     * is_into_upload : false
     * login_phone : 15050590500
     * msg :
     * order_id : 201704122105431076
     * status : 10
     * submit_time : 1492047590000
     */

    private long adv_id;
    private String adv_title;
    private String car_license_no;
    private int                  code;
    private boolean              is_into_upload;
    private long                 login_phone;
    private String msg;
    private String order_id;
    private int                  status;
    private long                 submit_time;
    private List<ImageInfosBean> image_infos;

    public long getAdv_id() {
        return adv_id;
    }

    public void setAdv_id(long adv_id) {
        this.adv_id = adv_id;
    }

    public String getAdv_title() {
        return adv_title;
    }

    public void setAdv_title(String adv_title) {
        this.adv_title = adv_title;
    }

    public String getCar_license_no() {
        return car_license_no;
    }

    public void setCar_license_no(String car_license_no) {
        this.car_license_no = car_license_no;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isIs_into_upload() {
        return is_into_upload;
    }

    public void setIs_into_upload(boolean is_into_upload) {
        this.is_into_upload = is_into_upload;
    }

    public long getLogin_phone() {
        return login_phone;
    }

    public void setLogin_phone(long login_phone) {
        this.login_phone = login_phone;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getSubmit_time() {
        return submit_time;
    }

    public void setSubmit_time(long submit_time) {
        this.submit_time = submit_time;
    }

    public List<ImageInfosBean> getImage_infos() {
        return image_infos;
    }

    public void setImage_infos(List<ImageInfosBean> image_infos) {
        this.image_infos = image_infos;
    }

    public static class ImageInfosBean implements Parcelable {
        /**
         * image_name : 车辆左前45度
         * image_type : 201
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
        dest.writeLong(this.adv_id);
        dest.writeString(this.adv_title);
        dest.writeString(this.car_license_no);
        dest.writeInt(this.code);
        dest.writeByte(this.is_into_upload ? (byte) 1 : (byte) 0);
        dest.writeLong(this.login_phone);
        dest.writeString(this.msg);
        dest.writeString(this.order_id);
        dest.writeInt(this.status);
        dest.writeLong(this.submit_time);
        dest.writeList(this.image_infos);
    }

    public CarAdvInfoBean() {
    }

    protected CarAdvInfoBean(Parcel in) {
        this.adv_id = in.readLong();
        this.adv_title = in.readString();
        this.car_license_no = in.readString();
        this.code = in.readInt();
        this.is_into_upload = in.readByte() != 0;
        this.login_phone = in.readLong();
        this.msg = in.readString();
        this.order_id = in.readString();
        this.status = in.readInt();
        this.submit_time = in.readLong();
        this.image_infos = new ArrayList<ImageInfosBean>();
        in.readList(this.image_infos, ImageInfosBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<CarAdvInfoBean> CREATOR = new Parcelable.Creator<CarAdvInfoBean>() {
        @Override
        public CarAdvInfoBean createFromParcel(Parcel source) {
            return new CarAdvInfoBean(source);
        }

        @Override
        public CarAdvInfoBean[] newArray(int size) {
            return new CarAdvInfoBean[size];
        }
    };
}
