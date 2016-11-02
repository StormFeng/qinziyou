package com.midian.qzy.datasource;

import android.content.Context;

import com.midian.qzy.bean.MyMsgsBean;
import com.midian.qzy.utils.AppUtil;

import java.util.ArrayList;

import midian.baselib.base.BaseListDataSource;
import midian.baselib.bean.NetResult;

public class NewsDataResource extends BaseListDataSource {
    public NewsDataResource(Context context) {
        super(context);
    }

    @Override
    protected ArrayList load(int page) throws Exception {
        this.page=page;
        ArrayList<MyMsgsBean.Content> models = new ArrayList<>();
        MyMsgsBean bean=AppUtil.getPpApiClient(ac).getMyMsgsBean(page+"","10");
        if(bean.isOK()){
            for(MyMsgsBean.Content item : bean.getContent()){
                models.add(item);
            }
            if(bean.getContent().size()<10){
                hasMore=false;
            }else{
                hasMore=true;
            }
        }else{
            ac.handleErrorCode(context,bean.ret_code);
        }
        return models;
    }
}
