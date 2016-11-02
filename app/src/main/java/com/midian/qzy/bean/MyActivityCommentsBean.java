package com.midian.qzy.bean;

import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import java.util.List;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 * 4.3我的活动评论列表
 */
public class MyActivityCommentsBean extends NetResult {
    public static MyActivityCommentsBean parse(String json) throws AppException {
        MyActivityCommentsBean res = new MyActivityCommentsBean();
        try {
            res = gson.fromJson(json, MyActivityCommentsBean.class);
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

    public class Content extends NetResult {
        private String activity_id;
        private String head_pic;
        private String name;
        private String time;
        private String content;
        private String comment_id;
        private String activity_name;
        private JsonArray pics;

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public void setActivity_name(String activity_name) {
            this.activity_name = activity_name;
        }

        public JsonArray getPics() {
            return pics;
        }

        public void setPics(JsonArray pics) {
            this.pics = pics;
        }

        public String getActivity_name() {
            return activity_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
        }

    }
}
