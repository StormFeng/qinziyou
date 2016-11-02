package com.midian.login.bean;

import com.google.gson.JsonSyntaxException;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 * 1.3第三方登录
 */
public class ThirdLoginBean extends NetResult {
    public static ThirdLoginBean parse(String json) throws AppException {
        ThirdLoginBean res = new ThirdLoginBean();
        try {
            res = gson.fromJson(json, ThirdLoginBean.class);
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
        private String access_token;
        private String user_id;
        private String name;
        private String head_pic;
        private String user_from;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

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

        public String getUser_from() {
            return user_from;
        }

        public void setUser_from(String user_from) {
            this.user_from = user_from;
        }
    }
}
