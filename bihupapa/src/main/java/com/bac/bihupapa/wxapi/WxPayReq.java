package com.bac.bihupapa.wxapi;


import com.alibaba.fastjson.JSON;
import com.bac.bihupapa.weexmodel.Bhpputil;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.List;
import java.util.Map;

/**
 * Created by Wjz on 2017/5/12.
 * 微信 PayReq Generate
 */

public class WxPayReq {

    //微信支付AppID
    private String appId;
    //微信支付商户号
    private String partnerId;
    //预支付码（重要）
    private String prepayId;
    //"Sign=WXPay"
    private String packageValue;
    private String nonceStr;
    //时间戳
    private String timeStamp;
    //签名
    private String sign;
    // extra
    private String extra;


    /**
     * 发送微信支付请求
     */
    public void send() {
        PayReq request = new PayReq();
        request.appId = this.appId;
        request.partnerId = this.partnerId;
        request.prepayId = this.prepayId;
        request.packageValue = this.packageValue != null ? this.packageValue : "Sign=WXPay";
        request.nonceStr = this.nonceStr;
        request.timeStamp = this.timeStamp;
        request.sign = this.sign;
        request.extData = this.extra;

        Bhpputil.getInstance().getIwxapi().sendReq(request);
    }


    /**
     * 内部类
     */
    public static class Builder {

        //微信支付AppID
        private String appId;
        //微信支付商户号
        private String partnerId;
        //预支付码（重要）
        private String prepayId;
        //"Sign=WXPay"
        private String packageValue = "Sign=WXPay";
        private String nonceStr;
        //时间戳
        private String timeStamp;
        //签名
        private String sign;
        // extra
        private String extra;


        /**
         * 设置微信支付AppID
         *
         * @param appId
         * @return
         */
        public Builder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        /**
         * 微信支付商户号
         *
         * @param partnerId
         * @return
         */
        public Builder setPartnerId(String partnerId) {
            this.partnerId = partnerId;
            return this;
        }

        /**
         * 设置预支付码（重要）
         *
         * @param prepayId
         * @return
         */
        public Builder setPrepayId(String prepayId) {
            this.prepayId = prepayId;
            return this;
        }


        /**
         * 设置
         *
         * @param packageValue
         * @return
         */
        public Builder setPackageValue(String packageValue) {
            this.packageValue = packageValue;
            return this;
        }


        /**
         * 设置
         *
         * @param nonceStr
         * @return
         */
        public Builder setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
            return this;
        }

        /**
         * 设置时间戳
         *
         * @param timeStamp
         * @return
         */
        public Builder setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        /**
         * 设置签名
         *
         * @param sign
         * @return
         */
        public Builder setSign(String sign) {
            this.sign = sign;
            return this;
        }

        /**
         * 额外参数，会在支付回调页显示
         *
         * @param extra
         */
        public Builder setExtra(String extra) {
            this.extra = extra;
            return this;
        }

        public WxPayReq create() {
            WxPayReq wechatPayReq = new WxPayReq();

            //微信支付AppID
            wechatPayReq.appId = this.appId;
            //微信支付商户号
            wechatPayReq.partnerId = this.partnerId;
            //预支付码（重要）
            wechatPayReq.prepayId = this.prepayId;
            //"Sign=WXPay"
            wechatPayReq.packageValue = this.packageValue;
            wechatPayReq.nonceStr = this.nonceStr;
            //时间戳
            wechatPayReq.timeStamp = this.timeStamp;
            //签名
            wechatPayReq.sign = this.sign;
            // extra
            wechatPayReq.extra = this.extra;

            return wechatPayReq;
        }

    }

    public static void pay(Map stringObjectMap, Map hm) {
        List<Map<String,Map<String,Object>>> data = (List<Map<String, Map<String, Object>>>) stringObjectMap.get("listMap");
        // 微信支付
        new WxPayReq.Builder()
                .setAppId(data.get(0).get("appid") + "")
                .setPartnerId(data.get(0).get("partnerid") + "")
                .setPrepayId(data.get(0).get("prepayid") + "")
                .setNonceStr(data.get(0).get("noncestr") + "")
                .setTimeStamp(data.get(0).get("timestamp") + "")
                .setPackageValue(data.get(0).get("package") + "")
                .setSign(data.get(0).get("sign") + "")
                .setExtra(JSON.toJSONString(hm))
                .create()
                .send();
    }
}
