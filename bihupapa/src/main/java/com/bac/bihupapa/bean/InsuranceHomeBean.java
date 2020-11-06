package com.bac.bihupapa.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.beans.insurance
 * 创建人：Wjz
 * 创建时间：2017/1/5
 * 类描述：
 */

public class InsuranceHomeBean extends InsuranceBaseBean implements Parcelable {


    /**
     * car_id : 1
     * car_license_no : 粤W3B103
     * car_owner_id : 2
     * engine_no : 12***51A
     * idcard_no : 44122919630205341X
     * idcard_type : 0
     * insure_area_code : 441900
     * order_id : 1701051607415465150
     * regist_date : 1323878400000
     * task_id : 1778398
     * vehicle_name : 东风日产DFL7151VAK1轿车
     * vin_code : LGBP12E******2406
     */

    private long car_id;
    private String car_license_no;
    private long    car_owner_id;
    private String engine_no;
    private String idcard_no;
    private int    idcard_type;
    private String insure_area_code;
    private long   order_id;
    private long   regist_date;
    private String task_id;
    private String vehicle_name;
    private String vin_code;
    private String vin_code_2;

    private String car_owner_name;
    private String driving_license;
    private String id_card;

    private boolean is_transfer_car;
    private Long transfer_date;

    private String phone;
    private String vehicle_id;

    private boolean is_new;
    private double price;

    private boolean is_history;

    private String city;

    private String prv_id;

    public String getPrv_id() {
        return prv_id;
    }

    public void setPrv_id(String prv_id) {
        this.prv_id = prv_id;
    }

    public String getVin_code_2() {
        return vin_code_2;
    }

    public void setVin_code_2(String vin_code_2) {
        this.vin_code_2 = vin_code_2;
    }

    public long getCar_id() {
        return car_id;
    }

    public void setCar_id(long car_id) {
        this.car_id = car_id;
    }

    public String getCar_license_no() {
        return car_license_no;
    }

    public void setCar_license_no(String car_license_no) {
        this.car_license_no = car_license_no;
    }

    public long getCar_owner_id() {
        return car_owner_id;
    }

    public void setCar_owner_id(long car_owner_id) {
        this.car_owner_id = car_owner_id;
    }

    public String getEngine_no() {
        return engine_no;
    }

    public void setEngine_no(String engine_no) {
        this.engine_no = engine_no;
    }

    public String getIdcard_no() {
        return idcard_no;
    }

    public void setIdcard_no(String idcard_no) {
        this.idcard_no = idcard_no;
    }

    public int getIdcard_type() {
        return idcard_type;
    }

    public void setIdcard_type(int idcard_type) {
        this.idcard_type = idcard_type;
    }

    public String getInsure_area_code() {
        return insure_area_code;
    }

    public void setInsure_area_code(String insure_area_code) {
        this.insure_area_code = insure_area_code;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public long getRegist_date() {
        return regist_date;
    }

    public void setRegist_date(long regist_date) {
        this.regist_date = regist_date;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getVin_code() {
        return vin_code;
    }

    public void setVin_code(String vin_code) {
        this.vin_code = vin_code;
    }

    public String getCar_owner_name() {
        return car_owner_name;
    }

    public void setCar_owner_name(String car_owner_name) {
        this.car_owner_name = car_owner_name;
    }

    public String getDriving_license() {
        return driving_license;
    }

    public void setDriving_license(String driving_license) {
        this.driving_license = driving_license;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public boolean is_transfer_car() {
        return is_transfer_car;
    }

    public void setIs_transfer_car(boolean is_transfer_car) {
        this.is_transfer_car = is_transfer_car;
    }

    public Long getTransfer_date() {
        return transfer_date;
    }

    public void setTransfer_date(Long transfer_date) {
        this.transfer_date = transfer_date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public boolean is_new() {
        return is_new;
    }

    public void setIs_new(boolean is_new) {
        this.is_new = is_new;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean is_history() {
        return is_history;
    }

    public void setIs_history(boolean is_history) {
        this.is_history = is_history;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.car_id);
        dest.writeString(this.car_license_no);
        dest.writeLong(this.car_owner_id);
        dest.writeString(this.engine_no);
        dest.writeString(this.idcard_no);
        dest.writeInt(this.idcard_type);
        dest.writeString(this.insure_area_code);
        dest.writeLong(this.order_id);
        dest.writeLong(this.regist_date);
        dest.writeString(this.task_id);
        dest.writeString(this.vehicle_name);
        dest.writeString(this.vin_code);
        dest.writeString(this.vin_code_2);
        dest.writeString(this.car_owner_name);
        dest.writeString(this.driving_license);
        dest.writeString(this.id_card);
        dest.writeByte(this.is_transfer_car ? (byte) 1 : (byte) 0);
        dest.writeValue(this.transfer_date);
        dest.writeString(this.phone);
        dest.writeString(this.vehicle_id);
        dest.writeByte(this.is_new ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.price);
        dest.writeByte(this.is_history ? (byte) 1 : (byte) 0);
        dest.writeString(this.city);
        dest.writeString(this.prv_id);
    }

    public InsuranceHomeBean() {
    }

    protected InsuranceHomeBean(Parcel in) {
        this.car_id = in.readLong();
        this.car_license_no = in.readString();
        this.car_owner_id = in.readLong();
        this.engine_no = in.readString();
        this.idcard_no = in.readString();
        this.idcard_type = in.readInt();
        this.insure_area_code = in.readString();
        this.order_id = in.readLong();
        this.regist_date = in.readLong();
        this.task_id = in.readString();
        this.vehicle_name = in.readString();
        this.vin_code = in.readString();
        this.vin_code_2 = in.readString();
        this.car_owner_name = in.readString();
        this.driving_license = in.readString();
        this.id_card = in.readString();
        this.is_transfer_car = in.readByte() != 0;
        this.transfer_date = (Long) in.readValue(Long.class.getClassLoader());
        this.phone = in.readString();
        this.vehicle_id = in.readString();
        this.is_new = in.readByte() != 0;
        this.price = in.readDouble();
        this.is_history = in.readByte() != 0;
        this.city = in.readString();
        this.prv_id = in.readString();
    }

    public static final Creator<InsuranceHomeBean> CREATOR = new Creator<InsuranceHomeBean>() {
        @Override
        public InsuranceHomeBean createFromParcel(Parcel source) {
            return new InsuranceHomeBean(source);
        }

        @Override
        public InsuranceHomeBean[] newArray(int size) {
            return new InsuranceHomeBean[size];
        }
    };
}
