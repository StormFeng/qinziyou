package com.midian.qzy.bean;

import com.google.gson.JsonSyntaxException;

import java.util.List;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 * 3.4 活动报名
 */
public class JoinActivityBean extends NetResult {
    public static JoinActivityBean parse(String json) throws AppException {
        JoinActivityBean res = new JoinActivityBean();
        try {
            res = gson.fromJson(json, JoinActivityBean.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw AppException.json(e);
        }
        return res;
    }

    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public class Content extends NetResult {
        private String order_sn;
        private String pay_way;
        private WeiXin weixin_param;
        private Alipay alipay_param;

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getPay_way() {
            return pay_way;
        }

        public void setPay_way(String pay_way) {
            this.pay_way = pay_way;
        }

        public WeiXin getWeixin_param() {
            return weixin_param;
        }

        public void setWeixin_param(WeiXin weixin_param) {
            this.weixin_param = weixin_param;
        }

        public Alipay getAlipay_param() {
            return alipay_param;
        }

        public void setAlipay_param(Alipay alipay_param) {
            this.alipay_param = alipay_param;
        }
    }
    public class WeiXin extends NetResult{
        private String appid;
        private String noncestr;
        private String pkg;
        private String partnerid;
        private String prepayid;
        private String sign;
        private String timestamp;

        public String getPkg() {
            return pkg;
        }

        public void setPkg(String pkg) {
            this.pkg = pkg;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

    public class Alipay extends NetResult{
        private String pay_price;
        private String pay_title;
        private String pay_desc;
        private String callback_url;
        private String merchant_no;
        private String receive_account;
        private String private_key;

        public String getPay_price() {
            return pay_price;
        }

        public void setPay_price(String pay_price) {
            this.pay_price = pay_price;
        }

        public String getPay_title() {
            return pay_title;
        }

        public void setPay_title(String pay_title) {
            this.pay_title = pay_title;
        }

        public String getPay_desc() {
            return pay_desc;
        }

        public void setPay_desc(String pay_desc) {
            this.pay_desc = pay_desc;
        }

        public String getCallback_url() {
            return callback_url;
        }

        public void setCallback_url(String callback_url) {
            this.callback_url = callback_url;
        }

        public String getMerchant_no() {
            return merchant_no;
        }

        public void setMerchant_no(String merchant_no) {
            this.merchant_no = merchant_no;
        }

        public String getReceive_account() {
            return receive_account;
        }

        public void setReceive_account(String receive_account) {
            this.receive_account = receive_account;
        }

        public String getPrivate_key() {
            return private_key;
        }

        public void setPrivate_key(String private_key) {
            this.private_key = private_key;
        }
    }
}
