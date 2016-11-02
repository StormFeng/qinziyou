package com.midian.login.bean;

import com.google.gson.JsonSyntaxException;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 * 第三方登录
 * Created by Administrator on 2016/6/28 0028.
 */
public class AuthLoginBean extends NetResult {

    public static AuthLoginBean parse(String json) throws AppException {
        AuthLoginBean res = new AuthLoginBean();
        try {
            res = gson.fromJson(json, AuthLoginBean.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw AppException.json(e);
        }
        return res;
    }

    private AuthLoginBeanData data;

    public AuthLoginBeanData getData() {
        return data;
    }

    public void setData(AuthLoginBeanData data) {
        this.data = data;
    }

    public class AuthLoginBeanData extends NetResult {
        private String mobilephone;//": "15920370134",
        private String sex;//": 1,
        private String username;//": "15920370134",
        private String accessToken;//": "MzVfMTU5Yzc=",
        private String memberid;//": 35,
        private String nickname;//": "石头",
        private String portrait;//": "http://img.t/6e.jpeg",
        private String roletype;//": 0


        public String getMobilephone() {
            return mobilephone;
        }

        public void setMobilephone(String mobilephone) {
            this.mobilephone = mobilephone;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getMemberid() {
            return memberid;
        }

        public void setMemberid(String memberid) {
            this.memberid = memberid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getRoletype() {
            return roletype;
        }

        public void setRoletype(String roletype) {
            this.roletype = roletype;
        }
    }

}
