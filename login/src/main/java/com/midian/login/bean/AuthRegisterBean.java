package com.midian.login.bean;

import com.google.gson.JsonSyntaxException;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 * 14.3第三方账号注册
 * Created by Administrator on 2016/6/28 0028.
 */
public class AuthRegisterBean extends NetResult {
    public static AuthRegisterBean parse(String json) throws AppException {
        AuthRegisterBean res = new AuthRegisterBean();
        try {
            res = gson.fromJson(json, AuthRegisterBean.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw AppException.json(e);
        }
        return res;
    }

    private AuthRegisterData data;

    public AuthRegisterData getData() {
        return data;
    }

    public void setData(AuthRegisterData data) {
        this.data = data;
    }

    public class AuthRegisterData extends NetResult {
        private String id;//": 41,
        private String baiduDeviceType;//": 3,
        private String accessToken;//": "NDJfMTU5MMDhiODllMWM5MTk4OWU=",
        private String memberid;//": 42,
        private String roletype;//": 0,
        private String baiduChannelid;//": "434jkj3o4098"

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBaiduDeviceType() {
            return baiduDeviceType;
        }

        public void setBaiduDeviceType(String baiduDeviceType) {
            this.baiduDeviceType = baiduDeviceType;
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

        public String getRoletype() {
            return roletype;
        }

        public void setRoletype(String roletype) {
            this.roletype = roletype;
        }

        public String getBaiduChannelid() {
            return baiduChannelid;
        }

        public void setBaiduChannelid(String baiduChannelid) {
            this.baiduChannelid = baiduChannelid;
        }
    }

}
