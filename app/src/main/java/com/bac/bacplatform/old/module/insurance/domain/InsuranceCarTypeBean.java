package com.bac.bacplatform.old.module.insurance.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.beans.insurance
 * 创建人：Wjz
 * 创建时间：2017/1/6
 * 类描述：
 */

public class InsuranceCarTypeBean {

    /**
     * carModelInfos : [{"gearbox":"手动档","maket_date":"201501","price":147700,"seat":5,"tax_price":154012,"vehicle_code":"FV7148LAMWG","vehicle_id":"4028b2b6482124f101483458c2cf0d3b","vehicle_name":"奥迪FV7148LAMWG轿车","year_style":"2015"},{"gearbox":"手动档","maket_date":"201501","price":147700,"seat":5,"tax_price":154012,"vehicle_code":"FV7148LAMBG","vehicle_id":"4028b2b649ccc817014adcc03b702f4a","vehicle_name":"奥迪FV7148LAMBG轿车","year_style":"2015"},{"gearbox":"双离合变速器","maket_date":"201604","price":147900,"seat":5,"tax_price":154221,"vehicle_code":"FV7148LADBG","vehicle_id":"4028b2b653c77fc40154129f361c4093","vehicle_name":"奥迪FV7148LADBG轿车","year_style":"2016"},{"gearbox":"双离合变速器","maket_date":"201604","price":147900,"seat":5,"tax_price":154221,"vehicle_code":"FV7148LADWG","vehicle_id":"4028b2b653c77fc4015412a0ca8940a6","vehicle_name":"奥迪FV7148LADWG轿车","year_style":"2016"},{"gearbox":"手动档","maket_date":"201501","price":151000,"seat":5,"tax_price":157453,"vehicle_code":"FV7148BAMBG","vehicle_id":"4028b2b64b2aec76014b48c5ef081ff7","vehicle_name":"奥迪FV7148BAMBG轿车","year_style":"2015"},{"gearbox":"双离合变速器","maket_date":"201406","price":151700,"seat":5,"tax_price":158183,"vehicle_code":"FV7148LADBG","vehicle_id":"4028b2b648393720014858304a990b7e","vehicle_name":"奥迪FV7148LADBG轿车","year_style":"2014"},{"gearbox":"双离合变速器","maket_date":"201403","price":152700,"seat":5,"tax_price":159226,"vehicle_code":"FV7148LADWG","vehicle_id":"4028b28844b618a00144f1bf1b2146f7","vehicle_name":"奥迪FV7148LADWG轿车","year_style":"2014"}]
     * total_size : 846
     */

    private int                     total_size;
    /**
     * gearbox : 手动档
     * maket_date : 201501
     * price : 147700
     * seat : 5
     * tax_price : 154012
     * vehicle_code : FV7148LAMWG
     * vehicle_id : 4028b2b6482124f101483458c2cf0d3b
     * vehicle_name : 奥迪FV7148LAMWG轿车
     * year_style : 2015
     */

    private List<CarModelInfosBean> carModelInfos;

    public int getTotal_size() {
        return total_size;
    }

    public void setTotal_size(int total_size) {
        this.total_size = total_size;
    }

    public List<CarModelInfosBean> getCarModelInfos() {
        return carModelInfos;
    }

    public void setCarModelInfos(List<CarModelInfosBean> carModelInfos) {
        this.carModelInfos = carModelInfos;
    }

    public static class CarModelInfosBean implements Parcelable {
        private String gearbox;
        private String maket_date;
        private int    price;
        private int    seat;
        private int    tax_price;
        private String vehicle_code;
        private String vehicle_id;
        private String vehicle_name;
        private String year_style;
        private int select;

        public int getSelect() {
            return select;
        }

        public void setSelect(int select) {
            this.select = select;
        }

        public String getGearbox() {
            return gearbox;
        }

        public void setGearbox(String gearbox) {
            this.gearbox = gearbox;
        }

        public String getMaket_date() {
            return maket_date;
        }

        public void setMaket_date(String maket_date) {
            this.maket_date = maket_date;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getSeat() {
            return seat;
        }

        public void setSeat(int seat) {
            this.seat = seat;
        }

        public int getTax_price() {
            return tax_price;
        }

        public void setTax_price(int tax_price) {
            this.tax_price = tax_price;
        }

        public String getVehicle_code() {
            return vehicle_code;
        }

        public void setVehicle_code(String vehicle_code) {
            this.vehicle_code = vehicle_code;
        }

        public String getVehicle_id() {
            return vehicle_id;
        }

        public void setVehicle_id(String vehicle_id) {
            this.vehicle_id = vehicle_id;
        }

        public String getVehicle_name() {
            return vehicle_name;
        }

        public void setVehicle_name(String vehicle_name) {
            this.vehicle_name = vehicle_name;
        }

        public String getYear_style() {
            return year_style;
        }

        public void setYear_style(String year_style) {
            this.year_style = year_style;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.gearbox);
            dest.writeString(this.maket_date);
            dest.writeInt(this.price);
            dest.writeInt(this.seat);
            dest.writeInt(this.tax_price);
            dest.writeString(this.vehicle_code);
            dest.writeString(this.vehicle_id);
            dest.writeString(this.vehicle_name);
            dest.writeString(this.year_style);
            dest.writeInt(this.select);
        }

        public CarModelInfosBean() {
        }

        protected CarModelInfosBean(Parcel in) {
            this.gearbox = in.readString();
            this.maket_date = in.readString();
            this.price = in.readInt();
            this.seat = in.readInt();
            this.tax_price = in.readInt();
            this.vehicle_code = in.readString();
            this.vehicle_id = in.readString();
            this.vehicle_name = in.readString();
            this.year_style = in.readString();
            this.select = in.readInt();
        }

        public static final Parcelable.Creator<CarModelInfosBean> CREATOR = new Parcelable.Creator<CarModelInfosBean>() {
            @Override
            public CarModelInfosBean createFromParcel(Parcel source) {
                return new CarModelInfosBean(source);
            }

            @Override
            public CarModelInfosBean[] newArray(int size) {
                return new CarModelInfosBean[size];
            }
        };
    }
}
