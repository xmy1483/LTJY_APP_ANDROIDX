package com.bac.bacplatform.old.module.insurance.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.beans.insurance
 * 创建人：Wjz
 * 创建时间：2017/2/22
 * 类描述：
 */

public class InsuranceCityBean implements Parcelable {

    /**
     * citys : [{"city":"320500","cityName":"苏州市"}]
     * province : 320000
     * provinceName : 江苏省
     */

    private String province;
    private String provinceName;
    private List<CitysBean> citys;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public List<CitysBean> getCitys() {
        return citys;
    }

    public void setCitys(List<CitysBean> citys) {
        this.citys = citys;
    }

    public static class CitysBean implements Parcelable {
        /**
         * city : 320500
         * cityName : 苏州市
         */

        private String city;
        private String cityName;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.city);
            dest.writeString(this.cityName);
        }

        public CitysBean() {
        }

        protected CitysBean(Parcel in) {
            this.city = in.readString();
            this.cityName = in.readString();
        }

        public static final Creator<CitysBean> CREATOR = new Creator<CitysBean>() {
            @Override
            public CitysBean createFromParcel(Parcel source) {
                return new CitysBean(source);
            }

            @Override
            public CitysBean[] newArray(int size) {
                return new CitysBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.province);
        dest.writeString(this.provinceName);
        dest.writeList(this.citys);
    }

    public InsuranceCityBean() {
    }

    protected InsuranceCityBean(Parcel in) {
        this.province = in.readString();
        this.provinceName = in.readString();
        this.citys = new ArrayList<CitysBean>();
        in.readList(this.citys, CitysBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<InsuranceCityBean> CREATOR = new Parcelable.Creator<InsuranceCityBean>() {
        @Override
        public InsuranceCityBean createFromParcel(Parcel source) {
            return new InsuranceCityBean(source);
        }

        @Override
        public InsuranceCityBean[] newArray(int size) {
            return new InsuranceCityBean[size];
        }
    };
}
