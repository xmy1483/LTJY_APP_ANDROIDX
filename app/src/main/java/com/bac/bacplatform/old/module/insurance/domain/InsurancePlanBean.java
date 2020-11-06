package com.bac.bacplatform.old.module.insurance.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.beans.insurance
 * 创建人：Wjz
 * 创建时间：2017/1/6
 * 类描述：
 */

public class InsurancePlanBean implements Parcelable {


    /**
     * is_payment_efc : true
     * is_payment_tax : true
     * package_id : 1
     * package_image : http://121.196.221.73:88/app.pay/images/rx_image.jpg
     * package_type : 0
     * risk_kinds : [{"is_check":true,"is_not_deductible":true,"option_id":1,"option_remark":"元","option_type":0,"option_value":50000,"remark":"第三者责任险,单位：元，可选项：50000、100000、150000、200000、300000、500000、1000000、1500000、2000000、2500000、3000000,","risk_code":"ThirdPart1Ins","risk_id":2,"risk_name":"第三者责任险","risk_type":0},{"is_check":true,"is_not_deductible":true,"option_id":12,"option_remark":"元","option_type":0,"option_value":5000,"remark":"司机责任险,单位：元，可选项：5000、10000、20000、30000、40000、50000、100000、150000、200000、500000,","risk_code":"DriverIns","risk_id":3,"risk_name":"司机责任险","risk_type":0},{"is_check":false,"is_not_deductible":false,"option_id":33,"option_remark":"国产","option_type":1,"option_value":1,"remark":"玻璃单独破碎险,1=国产、2=进口,投保前置条件：车辆损失险=投保","risk_code":"GlassIns","risk_id":6,"risk_name":"玻璃单独破碎险","risk_type":1},{"is_check":false,"is_not_deductible":true,"option_id":35,"option_remark":"折旧价","option_type":1,"option_value":1,"remark":"自燃损失险,1=按折旧价,投保前置条件：车辆损失险=投保","risk_code":"CombustionIns","risk_id":7,"risk_name":"自燃损失险","risk_type":1},{"is_check":false,"is_not_deductible":true,"option_id":36,"option_remark":"元","option_type":0,"option_value":2000,"remark":"车身划痕险,单位：元，可选项：2000、5000、10000、20000,投保前置条件：车辆损失险=投保","risk_code":"ScratchIns","risk_id":8,"risk_name":"车身划痕险","risk_type":1},{"is_check":false,"is_not_deductible":true,"option_id":22,"option_remark":"/座","option_type":0,"option_value":5000,"remark":"乘客责任险,单位：元/座，可选项：5000、10000、20000、30000、40000、50000、100000、150000、200000、500000,","risk_code":"PassengerIns","risk_id":4,"risk_name":"乘客责任险","risk_type":0}]
     */

    private boolean             is_payment_efc;
    private boolean             is_payment_tax;
    private int                 package_id;
    private String package_image;
    private int                 package_type;
    /**
     * is_check : true
     * is_not_deductible : true
     * option_id : 1
     * option_remark : 元
     * option_type : 0
     * option_value : 50000
     * remark : 第三者责任险,单位：元，可选项：50000、100000、150000、200000、300000、500000、1000000、1500000、2000000、2500000、3000000,
     * risk_code : ThirdPart1Ins
     * risk_id : 2
     * risk_name : 第三者责任险
     * risk_type : 0
     */

    private List<RiskKindsBean> risk_kinds;

    public boolean isIs_payment_efc() {
        return is_payment_efc;
    }

    public void setIs_payment_efc(boolean is_payment_efc) {
        this.is_payment_efc = is_payment_efc;
    }

    public boolean isIs_payment_tax() {
        return is_payment_tax;
    }

    public void setIs_payment_tax(boolean is_payment_tax) {
        this.is_payment_tax = is_payment_tax;
    }

    public int getPackage_id() {
        return package_id;
    }

    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }

    public String getPackage_image() {
        return package_image;
    }

    public void setPackage_image(String package_image) {
        this.package_image = package_image;
    }

    public int getPackage_type() {
        return package_type;
    }

    public void setPackage_type(int package_type) {
        this.package_type = package_type;
    }

    public List<RiskKindsBean> getRisk_kinds() {
        return risk_kinds;
    }

    public void setRisk_kinds(List<RiskKindsBean> risk_kinds) {
        this.risk_kinds = risk_kinds;
    }

    public static class RiskKindsBean implements Parcelable {
        private boolean is_check;
        private boolean is_not_deductible;
        private boolean not_deductible;
        private Integer option_id;
        private String option_remark;
        private Integer option_type;
        private Integer option_value;
        private String remark;
        private String risk_code;
        private int     risk_id;
        private String risk_name;
        private int     risk_type;

        @Override
        public String toString() {
            return "RiskKindsBean{" +
                    "is_check=" + is_check +
                    ", is_not_deductible=" + is_not_deductible +
                    ", not_deductible=" + not_deductible +
                    ", option_id=" + option_id +
                    ", option_remark='" + option_remark + '\'' +
                    ", option_type=" + option_type +
                    ", option_value=" + option_value +
                    ", remark='" + remark + '\'' +
                    ", risk_code='" + risk_code + '\'' +
                    ", risk_id=" + risk_id +
                    ", risk_name='" + risk_name + '\'' +
                    ", risk_type=" + risk_type +
                    '}';
        }

        public boolean isIs_check() {
            return is_check;
        }

        public void setIs_check(boolean is_check) {
            this.is_check = is_check;
        }

        public boolean isIs_not_deductible() {
            return is_not_deductible;
        }

        public void setIs_not_deductible(boolean is_not_deductible) {
            this.is_not_deductible = is_not_deductible;
        }

        public boolean isNot_deductible() {
            return not_deductible;
        }

        public void setNot_deductible(boolean not_deductible) {
            this.not_deductible = not_deductible;
        }

        /* 为空 */
        public Integer getOption_id() {
            return option_id;
        }

        public void setOption_id(Integer option_id) {
            this.option_id = option_id;
        }

        public String getOption_remark() {
            return option_remark;
        }

        public void setOption_remark(String option_remark) {
            this.option_remark = option_remark;
        }

        public Integer getOption_type() {
            return option_type;
        }

        public void setOption_type(Integer option_type) {
            this.option_type = option_type;
        }

        public Integer getOption_value() {
            return option_value;
        }

        public void setOption_value(Integer option_value) {
            this.option_value = option_value;
        }

        /* 为空 */

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRisk_code() {
            return risk_code;
        }

        public void setRisk_code(String risk_code) {
            this.risk_code = risk_code;
        }

        public int getRisk_id() {
            return risk_id;
        }

        public void setRisk_id(int risk_id) {
            this.risk_id = risk_id;
        }

        public String getRisk_name() {
            return risk_name;
        }

        public void setRisk_name(String risk_name) {
            this.risk_name = risk_name;
        }

        public int getRisk_type() {
            return risk_type;
        }

        public void setRisk_type(int risk_type) {
            this.risk_type = risk_type;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.is_check ? (byte) 1 : (byte) 0);
            dest.writeByte(this.is_not_deductible ? (byte) 1 : (byte) 0);
            dest.writeByte(this.not_deductible ? (byte) 1 : (byte) 0);
            dest.writeValue(this.option_id);
            dest.writeString(this.option_remark);
            dest.writeValue(this.option_type);
            dest.writeValue(this.option_value);
            dest.writeString(this.remark);
            dest.writeString(this.risk_code);
            dest.writeInt(this.risk_id);
            dest.writeString(this.risk_name);
            dest.writeInt(this.risk_type);
        }

        public RiskKindsBean() {
        }

        protected RiskKindsBean(Parcel in) {
            this.is_check = in.readByte() != 0;
            this.is_not_deductible = in.readByte() != 0;
            this.not_deductible = in.readByte() != 0;
            this.option_id = (Integer) in.readValue(Integer.class.getClassLoader());
            this.option_remark = in.readString();
            this.option_type = (Integer) in.readValue(Integer.class.getClassLoader());
            this.option_value = (Integer) in.readValue(Integer.class.getClassLoader());
            this.remark = in.readString();
            this.risk_code = in.readString();
            this.risk_id = in.readInt();
            this.risk_name = in.readString();
            this.risk_type = in.readInt();
        }

        public static final Creator<RiskKindsBean> CREATOR = new Creator<RiskKindsBean>() {
            @Override
            public RiskKindsBean createFromParcel(Parcel source) {
                return new RiskKindsBean(source);
            }

            @Override
            public RiskKindsBean[] newArray(int size) {
                return new RiskKindsBean[size];
            }
        };
    }




    ////////////

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.is_payment_efc ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_payment_tax ? (byte) 1 : (byte) 0);
        dest.writeInt(this.package_id);
        dest.writeString(this.package_image);
        dest.writeInt(this.package_type);
        dest.writeList(this.risk_kinds);
    }

    public InsurancePlanBean() {
    }

    protected InsurancePlanBean(Parcel in) {
        this.is_payment_efc = in.readByte() != 0;
        this.is_payment_tax = in.readByte() != 0;
        this.package_id = in.readInt();
        this.package_image = in.readString();
        this.package_type = in.readInt();
        this.risk_kinds = new ArrayList<RiskKindsBean>();
        in.readList(this.risk_kinds, RiskKindsBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<InsurancePlanBean> CREATOR = new Parcelable.Creator<InsurancePlanBean>() {
        @Override
        public InsurancePlanBean createFromParcel(Parcel source) {
            return new InsurancePlanBean(source);
        }

        @Override
        public InsurancePlanBean[] newArray(int size) {
            return new InsurancePlanBean[size];
        }
    };
}
