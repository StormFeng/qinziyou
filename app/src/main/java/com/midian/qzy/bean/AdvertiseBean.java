package com.midian.qzy.bean;

import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import java.util.List;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 * 新增启动页广告图
 */
public class AdvertiseBean extends NetResult {
    public static AdvertiseBean parse(String json) throws AppException {
        AdvertiseBean res = new AdvertiseBean();
        try {
            res = gson.fromJson(json, AdvertiseBean.class);
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

    public class Content{
        private String advertise_pic_id;
        private String advertise_url;

        public String getAdvertise_pic_id() {
            return advertise_pic_id;
        }

        public void setAdvertise_pic_id(String advertise_pic_id) {
            this.advertise_pic_id = advertise_pic_id;
        }

        public String getAdvertise_url() {
            return advertise_url;
        }

        public void setAdvertise_url(String advertise_url) {
            this.advertise_url = advertise_url;
        }
    }
}
