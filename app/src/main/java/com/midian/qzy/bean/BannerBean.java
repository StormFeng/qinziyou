package com.midian.qzy.bean;

import com.google.gson.JsonSyntaxException;
import com.midian.qzy.widget.Banner;

import java.util.List;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 * 3.1 首页banner
 */
public class BannerBean extends NetResult {
    public static BannerBean parse(String json) throws AppException {
        BannerBean res = new BannerBean();
        try {
            res = gson.fromJson(json, BannerBean.class);
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

    public class Content extends NetResult{
        private List<Banner> banner;

        public List<Banner> getBanner() {
            return banner;
        }

        public void setBanner(List<Banner> banner) {
            this.banner = banner;
        }
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public class Banner extends NetResult{
        private String banner_id;
        private String banner_pic_id;
        private String activity_id;
        private String url;

        public String getBanner_id() {
            return banner_id;
        }

        public void setBanner_id(String banner_id) {
            this.banner_id = banner_id;
        }

        public String getBanner_pic_id() {
            return banner_pic_id;
        }

        public void setBanner_pic_id(String banner_pic_id) {
            this.banner_pic_id = banner_pic_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
        }
    }
}
