package com.midian.qzy.bean;

import com.google.gson.JsonSyntaxException;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 * 1.8个人资料
 */
public class UserDetailBean extends NetResult {
    public static UserDetailBean parse(String json) throws AppException {
        UserDetailBean res = new UserDetailBean();
        try {
            res = gson.fromJson(json, UserDetailBean.class);
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
        private String user_id;
        private String name;
        private String head_pic;
        private String phone;
        private String sex;
        private String user_from;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getUser_from() {
            return user_from;
        }

        public void setUser_from(String user_from) {
            this.user_from = user_from;
        }
    }
}
