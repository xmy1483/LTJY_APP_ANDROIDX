package com.bac.bacplatform.module.recharge.adapter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Wjz on 2017/5/21.
 */

public class OilVoucherBean implements Parcelable {

    /**
     * activate_type : 0
     * create_time : 1495177319000
     * discount : 0.98
     * expired : 1496246399000
     * explain_text : 1、本券仅限当日有效；##2、本券可为加油卡最高折扣10元
     * name : 充油现金券
     * oil_type : 0
     * recharge_money_limit : 100.0
     * source : 0
     * status : 1
     * use_explain : 1、此券可在充油时使用
     * voucher_id : 1705191501596280091
     * voucher_money : 10.0
     * voucher_status : 1
     * voucher_type : 10
     * voucher_type_id : 120
     */

    private int activate_type;
    private long create_time;
    private double discount;
    private long expired;
    private String explain_text;
    private String name;
    private int oil_type;
    private double recharge_money_limit;
    private int source;
    private int status;
    private String use_explain;
    private long voucher_id;
    private double voucher_money;
    private int voucher_status;
    private int voucher_type;
    private int voucher_type_id;
    private boolean isSelected;

    public int getActivate_type() {
        return activate_type;
    }

    public void setActivate_type(int activate_type) {
        this.activate_type = activate_type;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public long getExpired() {
        return expired;
    }

    public void setExpired(long expired) {
        this.expired = expired;
    }

    public String getExplain_text() {
        return explain_text;
    }

    public void setExplain_text(String explain_text) {
        this.explain_text = explain_text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOil_type() {
        return oil_type;
    }

    public void setOil_type(int oil_type) {
        this.oil_type = oil_type;
    }

    public double getRecharge_money_limit() {
        return recharge_money_limit;
    }

    public void setRecharge_money_limit(double recharge_money_limit) {
        this.recharge_money_limit = recharge_money_limit;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUse_explain() {
        return use_explain;
    }

    public void setUse_explain(String use_explain) {
        this.use_explain = use_explain;
    }

    public long getVoucher_id() {
        return voucher_id;
    }

    public void setVoucher_id(long voucher_id) {
        this.voucher_id = voucher_id;
    }

    public double getVoucher_money() {
        return voucher_money;
    }

    public void setVoucher_money(double voucher_money) {
        this.voucher_money = voucher_money;
    }

    public int getVoucher_status() {
        return voucher_status;
    }

    public void setVoucher_status(int voucher_status) {
        this.voucher_status = voucher_status;
    }

    public int getVoucher_type() {
        return voucher_type;
    }

    public void setVoucher_type(int voucher_type) {
        this.voucher_type = voucher_type;
    }

    public int getVoucher_type_id() {
        return voucher_type_id;
    }

    public void setVoucher_type_id(int voucher_type_id) {
        this.voucher_type_id = voucher_type_id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.activate_type);
        dest.writeLong(this.create_time);
        dest.writeDouble(this.discount);
        dest.writeLong(this.expired);
        dest.writeString(this.explain_text);
        dest.writeString(this.name);
        dest.writeInt(this.oil_type);
        dest.writeDouble(this.recharge_money_limit);
        dest.writeInt(this.source);
        dest.writeInt(this.status);
        dest.writeString(this.use_explain);
        dest.writeLong(this.voucher_id);
        dest.writeDouble(this.voucher_money);
        dest.writeInt(this.voucher_status);
        dest.writeInt(this.voucher_type);
        dest.writeInt(this.voucher_type_id);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public OilVoucherBean() {
    }

    protected OilVoucherBean(Parcel in) {
        this.activate_type = in.readInt();
        this.create_time = in.readLong();
        this.discount = in.readDouble();
        this.expired = in.readLong();
        this.explain_text = in.readString();
        this.name = in.readString();
        this.oil_type = in.readInt();
        this.recharge_money_limit = in.readDouble();
        this.source = in.readInt();
        this.status = in.readInt();
        this.use_explain = in.readString();
        this.voucher_id = in.readLong();
        this.voucher_money = in.readDouble();
        this.voucher_status = in.readInt();
        this.voucher_type = in.readInt();
        this.voucher_type_id = in.readInt();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<OilVoucherBean> CREATOR = new Parcelable.Creator<OilVoucherBean>() {
        @Override
        public OilVoucherBean createFromParcel(Parcel source) {
            return new OilVoucherBean(source);
        }

        @Override
        public OilVoucherBean[] newArray(int size) {
            return new OilVoucherBean[size];
        }
    };

    @Override
    public String toString() {
        return "OilVoucherBean{" +
                "activate_type=" + activate_type +
                ", create_time=" + create_time +
                ", discount=" + discount +
                ", expired=" + expired +
                ", explain_text='" + explain_text + '\'' +
                ", name='" + name + '\'' +
                ", oil_type=" + oil_type +
                ", recharge_money_limit=" + recharge_money_limit +
                ", source=" + source +
                ", status=" + status +
                ", use_explain='" + use_explain + '\'' +
                ", voucher_id=" + voucher_id +
                ", voucher_money=" + voucher_money +
                ", voucher_status=" + voucher_status +
                ", voucher_type=" + voucher_type +
                ", voucher_type_id=" + voucher_type_id +
                ", isSelected=" + isSelected +
                '}';
    }
}
