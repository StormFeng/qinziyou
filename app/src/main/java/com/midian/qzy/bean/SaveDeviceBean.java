package com.midian.qzy.bean;

import com.google.gson.JsonSyntaxException;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 *1.13发送用户设备号/清空设备号
 */
public class SaveDeviceBean extends NetResult {
    public static SaveDeviceBean parse(String json) throws AppException {
        SaveDeviceBean res = new SaveDeviceBean();
        try {
            res = gson.fromJson(json, SaveDeviceBean.class);
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
        private String is_receive;

        public String getIs_receive() {
            return is_receive;
        }

        public void setIs_receive(String is_receive) {
            this.is_receive = is_receive;
        }
    }
}
