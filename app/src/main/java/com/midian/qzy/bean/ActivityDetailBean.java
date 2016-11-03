package com.midian.qzy.bean;

import com.google.gson.JsonSyntaxException;

import java.util.List;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 * 3.3 活动详情
 */
public class ActivityDetailBean extends NetResult {
    public static ActivityDetailBean parse(String json) throws AppException {
        ActivityDetailBean res = new ActivityDetailBean();
        try {
            res = gson.fromJson(json, ActivityDetailBean.class);
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
        private String activity_id;
        private String cover_pic;
        private String title;
        private String begin_time;
        private String type_name;
        private String organization_name;
        private String adult_price;
        private String child_price;
        private String ages;
        private String activity_address;
        private String activity_address_lon;
        private String activity_address_lat;
        private String is_adult_record;
        private String contact_address;
        private String contact_address_lon;
        private String contact_address_lat;
        private String contact_person;
        private String contact_num;
        private String distance;
        private String join_count;
        private String join_limit_count;
        private String detail;
        private String is_join;
        private List<String> pics;

        public String getIs_adult_record() {
            return is_adult_record;
        }

        public void setIs_adult_record(String is_adult_record) {
            this.is_adult_record = is_adult_record;
        }

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getContact_address() {
            return contact_address;
        }

        public void setContact_address(String contact_address) {
            this.contact_address = contact_address;
        }

        public String getContact_address_lon() {
            return contact_address_lon;
        }

        public void setContact_address_lon(String contact_address_lon) {
            this.contact_address_lon = contact_address_lon;
        }

        public String getContact_address_lat() {
            return contact_address_lat;
        }

        public void setContact_address_lat(String contact_address_lat) {
            this.contact_address_lat = contact_address_lat;
        }

        public String getContact_person() {
            return contact_person;
        }

        public void setContact_person(String contact_person) {
            this.contact_person = contact_person;
        }

        public String getContact_num() {
            return contact_num;
        }

        public void setContact_num(String contact_num) {
            this.contact_num = contact_num;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getJoin_count() {
            return join_count;
        }

        public void setJoin_count(String join_count) {
            this.join_count = join_count;
        }

        public String getJoin_limit_count() {
            return join_limit_count;
        }

        public void setJoin_limit_count(String join_limit_count) {
            this.join_limit_count = join_limit_count;
        }

        public void setPics(List<String> pics) {
            this.pics = pics;
        }

        public List<String> getPics() {
            return pics;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getIs_join() {
            return is_join;
        }

        public void setIs_join(String is_join) {
            this.is_join = is_join;
        }
    }


}
