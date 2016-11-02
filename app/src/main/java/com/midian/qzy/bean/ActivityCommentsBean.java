package com.midian.qzy.bean;

import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import java.util.List;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 * 3.5活动评论列表
 */
public class ActivityCommentsBean extends NetResult {
    public static ActivityCommentsBean parse(String json) throws AppException {
        ActivityCommentsBean res = new ActivityCommentsBean();
        try {
            res = gson.fromJson(json, ActivityCommentsBean.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw AppException.json(e);
        }
        return res;
    }

    private String comment_count;

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    private List<Content> content;

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public class Content extends NetResult{
        private String head_pic;
        private String name;
        private String time;
        private String content;
        private String comment_id;
        private JsonArray pics;

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
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

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public JsonArray getPics() {
            return pics;
        }

        public void setPics(JsonArray pics) {
            this.pics = pics;
        }

        @Override
        public String toString() {
            return "Content{" +
                    "head_pic='" + head_pic + '\'' +
                    ", name='" + name + '\'' +
                    ", time='" + time + '\'' +
                    ", content='" + content + '\'' +
                    ", comment_id='" + comment_id + '\'' +
                    ", pics=" + pics +
                    '}';
        }
    }
}
