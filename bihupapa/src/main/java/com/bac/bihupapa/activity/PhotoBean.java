package com.bac.bihupapa.activity;

/**
 * Created by guke on 2017/9/22.
 */

public class PhotoBean {

    /**
     * image_id : 56816c00-698e-4819-9e25-2a81f55637d4
     * image_url : http://121.43.172.16:88/app.pay/driving/2017-09-22/1506051453364.png
     * is_succ : true
     */

    private String image_id;
    private String image_url;
    private  boolean is_succ;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public boolean isIs_succ() {
        return is_succ;
    }

    public void setIs_succ(boolean is_succ) {
        this.is_succ = is_succ;
    }
}
