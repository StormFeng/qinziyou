package com.midian.login.bean;

import com.google.gson.JsonSyntaxException;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 * Created by chu on 2015.12.1.001.
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
        // System.out.println(res);
        return res;
    }


    private SaveContent content;

    public SaveContent getContent() {
        return content;
    }

    public void setContent(SaveContent content) {
        this.content = content;
    }

    public class SaveContent {
        private String is_receive;//推送接收状态

        public String getIs_receive() {
            return is_receive;
        }

        public void setIs_receive(String is_receive) {
            this.is_receive = is_receive;
        }
    }
}
