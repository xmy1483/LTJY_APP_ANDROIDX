package com.bac.bacplatform.bean;

import android.text.TextUtils;

public class JdBean {
    private String id;
    private String name;
    private String originPrice;
    private String salePrice;
    private String imgUrl = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginPrice() {
        return "￥"+originPrice;
    }

    public void setOriginPrice(Object originPrice) {
        if(originPrice!=null){
            this.originPrice = originPrice.toString();
        }
    }

    public String getSalePrice() {
        return "￥"+salePrice;
    }

    public void setSalePrice(Object salePrice) {
        if(salePrice!=null && salePrice.toString().contains(".")) {
            salePrice = salePrice.toString().substring(0,salePrice.toString().indexOf("."));
        }
        this.salePrice = salePrice.toString();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(Object imgUrl) {
        if(imgUrl!=null && !TextUtils.isEmpty(imgUrl.toString())){
            this.imgUrl = imgUrl.toString();
        }
    }
}
