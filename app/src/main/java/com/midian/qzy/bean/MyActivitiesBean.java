package com.midian.qzy.bean;

import com.google.gson.JsonSyntaxException;

import java.util.List;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 * 4.2 我的活动列表
 */
public class MyActivitiesBean extends NetResult {
    public static MyActivitiesBean parse(String json) throws AppException {
        MyActivitiesBean res = new MyActivitiesBean();
        try {
            res = gson.fromJson(json, MyActivitiesBean.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw AppException.json(e);
        }
        return res;
    }

    private List<Content> content;

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public class Content extends NetResult{
        private String activity_id;
        private String cover_pic;
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        private String begin_time;
        private String type_name;
        private String organization_name;
        private String adult_price;
        private String child_price;
        private String ages;
        private String activity_address;
        private String activity_address_lon;
        private String activity_address_lat;
        private String adult_count;

        public String getAdult_count() {
            return adult_count;
        }

        public void setAdult_count(String adult_count) {
            this.adult_count = adult_count;
        }

        public String getChild_count() {
            return child_count;
        }

        public void setChild_count(String child_count) {
            this.child_count = child_count;
        }

        private String child_count;

        public String getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
        }

        public String getCover_pic() {
            return cover_pic;
        }

        public void setCover_pic(String cover_pic) {
            this.cover_pic = cover_pic;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public String getOrganization_name() {
            return organization_name;
        }

        public void setOrganization_name(String organization_name) {
            this.organization_name = organization_name;
        }

        public String getAdult_price() {
            return adult_price;
        }

        public void setAdult_price(String adult_price) {
            this.adult_price = adult_price;
        }

        public String getChild_price() {
            return child_price;
        }

        public void setChild_price(String child_price) {
            this.child_price = child_price;
        }

        public String getAges() {
            return ages;
        }

        public void setAges(String ages) {
            this.ages = ages;
        }

        public String getActivity_address() {
            return activity_address;
        }

        public void setActivity_address(String activity_address) {
            this.activity_address = activity_address;
        }

        public String getActivity_address_lon() {
            return activity_address_lon;
        }

        public void setActivity_address_lon(String activity_address_lon) {
            this.activity_address_lon = activity_address_lon;
        }

        public String getActivity_address_lat() {
            return activity_address_lat;
        }

        public void setActivity_address_lat(String activity_address_lat) {
            this.activity_address_lat = activity_address_lat;
        }
    }
}
