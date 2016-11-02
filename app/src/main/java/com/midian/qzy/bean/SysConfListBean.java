package com.midian.qzy.bean;

import com.google.gson.JsonSyntaxException;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 * 2.2.获取系统配置信息
 */
public class SysConfListBean extends NetResult {
    public static SysConfListBean parse(String json) throws AppException {
        SysConfListBean res = new SysConfListBean();
        try {
            res = gson.fromJson(json, SysConfListBean.class);
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

    public class Content extends NetResult{
        private String about_us_url;
        private String service_term_url;
        private String service_phone;

        public String getAbout_us_url() {
            return about_us_url;
        }

        public void setAbout_us_url(String about_us_url) {
            this.about_us_url = about_us_url;
        }

        public String getService_term_url() {
            return service_term_url;
        }

        public void setService_term_url(String service_term_url) {
            this.service_term_url = service_term_url;
        }

        public String getService_phone() {
            return service_phone;
        }

        public void setService_phone(String service_phone) {
            this.service_phone = service_phone;
        }
    }
}
