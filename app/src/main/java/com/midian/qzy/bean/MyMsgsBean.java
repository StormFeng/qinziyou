package com.midian.qzy.bean;

import com.google.gson.JsonSyntaxException;

import java.util.List;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 * 4.1 我的消息列表
 */
public class MyMsgsBean extends NetResult {
    public static MyMsgsBean parse(String json) throws AppException {
        MyMsgsBean res = new MyMsgsBean();
        try {
            res = gson.fromJson(json, MyMsgsBean.class);
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
        private String title;
        private String intro;
        private String content;
        private String time;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
