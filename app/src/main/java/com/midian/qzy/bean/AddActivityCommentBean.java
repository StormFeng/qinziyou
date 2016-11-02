package com.midian.qzy.bean;

import com.google.gson.JsonSyntaxException;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 * 3.6活动评论
 */
public class AddActivityCommentBean extends NetResult {
    public static AddActivityCommentBean parse(String json) throws AppException {
        AddActivityCommentBean res = new AddActivityCommentBean();
        try {
            res = gson.fromJson(json, AddActivityCommentBean.class);
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
        private String comment_id;

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }
    }
}
