package com.midian.qzy.bean;

import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import java.util.List;

import midian.baselib.app.AppException;
import midian.baselib.bean.NetResult;

/**
 * 3.7活动筛选参数接口
 */
public class ActivityFilterParamsBean extends NetResult {
    public static ActivityFilterParamsBean parse(String json) throws AppException {
        ActivityFilterParamsBean res = new ActivityFilterParamsBean();
        try {
            res = gson.fromJson(json, ActivityFilterParamsBean.class);
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
        private JsonArray ages;
        private List<Types> types;

        public JsonArray getAges() {
            return ages;
        }

        public void setAges(JsonArray ages) {
            this.ages = ages;
        }

        public List<Types> getTypes() {
            return types;
        }

        public void setTypes(List<Types> types) {
            this.types = types;
        }
    }
    public class Types extends NetResult{
        private String type_id;
        private String type_name;

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }
    }
}
