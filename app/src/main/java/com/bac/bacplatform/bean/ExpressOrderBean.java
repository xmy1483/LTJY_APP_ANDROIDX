package com.bac.bacplatform.bean;

public class ExpressOrderBean {
    private String cnums;
    private String create_time;
    private String out_trade_no;
    private String type_name;
    private String post_no;
    private String hadPost;

    public String getPost_no() {
        if(post_no == null){
            return "";
        }
        return post_no;
    }

    public void setPost_no(Object post_no) {
        if(post_no != null) {
            this.post_no = post_no.toString();
        }
    }

    public String getCnums() {
        if(cnums!=null && cnums.endsWith(",")) {
            return cnums.substring(0,cnums.lastIndexOf(","));
        }
        return cnums;
    }

    public void setCnums(Object cnums) {
        if(cnums != null) {
            this.cnums = cnums.toString();
        }
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Object create_time) {
        if(create_time!=null) {
            this.create_time = create_time.toString();
        }
    }

    public String getOut_trade_no() {
        if(out_trade_no == null){
            return "";
        }
        return out_trade_no;
    }

    public void setOut_trade_no(Object out_trade_no) {
        if(out_trade_no!=null) {
            this.out_trade_no = out_trade_no.toString();
        }
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(Object type_name) {
        if(type_name!=null) {
            this.type_name = type_name.toString();
        }
    }

    public String getHadPost() {
        return hadPost;
    }

    public void setHadPost(Object hadPost) {
        this.hadPost = hadPost.toString();
    }
}
