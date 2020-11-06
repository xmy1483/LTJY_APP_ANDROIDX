package com.bac.bacplatform.old.module.cards.fragment.active;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.beans
 * 创建人：Wjz
 * 创建时间：2016/8/22
 * 类描述：
 */
public class CardBean implements Parcelable {

    /**
     * activate_type : 0
     * create_time : 1471499477000
     * expired : 1471535999000
     * explain_text : 1.充值话费送10元加油金
     * 2.此券仅限骆驼加油使用
     * holder : 13641536078
     * name : 话费充值券
     * recharge_money : 100.0
     * source : 1
     * status : 1
     * voucher_id : 1608181738433770816
     * voucher_money : 10.0
     * voucher_type : 1
     */

    private String activate_type;
    private String create_time;
    private String expired;
    private String explain_text;
    private String holder;
    private String name;
    private String recharge_money;
    private String source;
    private String status;
    private String voucher_id;
    private String voucher_money;
    private String voucher_type;
    private String product_id;
    private String price;
    private String product_name;
    private String market_price;
    private String pay_money;
    private String product_remark;
    private String use_method;


    public CardBean(String activate_type, String create_time, String expired, String explain_text,
                    String holder, String name, String recharge_money, String source, String status,
                    String voucher_id, String voucher_money, String voucher_type, String product_id,
                    String price, String product_name, String market_price, String pay_money
            , String product_remark, String use_method) {
        this.activate_type = activate_type;
        this.create_time = create_time;
        this.expired = expired;
        this.explain_text = explain_text;
        this.holder = holder;
        this.name = name;
        this.recharge_money = recharge_money;
        this.source = source;
        this.status = status;
        this.voucher_id = voucher_id;
        this.voucher_money = voucher_money;
        this.voucher_type = voucher_type;
        this.product_id = product_id;
        this.price = price;
        this.product_name = product_name;
        this.market_price = market_price;
        this.pay_money = pay_money;
        this.product_remark = product_remark;
        this.use_method = use_method;
    }

    public String getActivate_type() {
        return activate_type;
    }

    public void setActivate_type(String activate_type) {
        this.activate_type = activate_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getExplain_text() {
        return explain_text;
    }

    public void setExplain_text(String explain_text) {
        this.explain_text = explain_text;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecharge_money() {
        return recharge_money;
    }

    public void setRecharge_money(String recharge_money) {
        this.recharge_money = recharge_money;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVoucher_id() {
        return voucher_id;
    }

    public void setVoucher_id(String voucher_id) {
        this.voucher_id = voucher_id;
    }

    public String getVoucher_money() {
        return voucher_money;
    }

    public void setVoucher_money(String voucher_money) {
        this.voucher_money = voucher_money;
    }

    public String getVoucher_type() {
        return voucher_type;
    }

    public void setVoucher_type(String voucher_type) {
        this.voucher_type = voucher_type;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getPay_money() {
        return pay_money;
    }

    public void setPay_money(String pay_money) {
        this.pay_money = pay_money;
    }

    public String getProduct_remark() {
        return product_remark;
    }

    public void setProduct_remark(String product_remark) {
        this.product_remark = product_remark;
    }

    public String getUse_method() {
        return use_method;
    }

    public void setUse_method(String use_method) {
        this.use_method = use_method;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.activate_type);
        dest.writeString(this.create_time);
        dest.writeString(this.expired);
        dest.writeString(this.explain_text);
        dest.writeString(this.holder);
        dest.writeString(this.name);
        dest.writeString(this.recharge_money);
        dest.writeString(this.source);
        dest.writeString(this.status);
        dest.writeString(this.voucher_id);
        dest.writeString(this.voucher_money);
        dest.writeString(this.voucher_type);
        dest.writeString(this.product_id);
        dest.writeString(this.price);
        dest.writeString(this.product_name);
        dest.writeString(this.market_price);
        dest.writeString(this.pay_money);
        dest.writeString(this.product_remark);
        dest.writeString(this.use_method);
    }

    protected CardBean(Parcel in) {
        this.activate_type = in.readString();
        this.create_time = in.readString();
        this.expired = in.readString();
        this.explain_text = in.readString();
        this.holder = in.readString();
        this.name = in.readString();
        this.recharge_money = in.readString();
        this.source = in.readString();
        this.status = in.readString();
        this.voucher_id = in.readString();
        this.voucher_money = in.readString();
        this.voucher_type = in.readString();
        this.product_id = in.readString();
        this.price = in.readString();
        this.product_name = in.readString();
        this.market_price = in.readString();
        this.pay_money = in.readString();
        this.product_remark = in.readString();
        this.use_method = in.readString();
    }

    public static final Parcelable.Creator<CardBean> CREATOR = new Parcelable.Creator<CardBean>() {
        @Override
        public CardBean createFromParcel(Parcel source) {
            return new CardBean(source);
        }

        @Override
        public CardBean[] newArray(int size) {
            return new CardBean[size];
        }
    };
}
