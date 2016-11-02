package com.midian.qzy.datasource;

import android.content.Context;

import com.midian.qzy.bean.MyActivitiesBean;
import com.midian.qzy.utils.AppUtil;

import java.util.ArrayList;

import midian.baselib.base.BaseListDataSource;
import midian.baselib.bean.NetResult;

public class JoinedActivityDataResource extends BaseListDataSource {
    public JoinedActivityDataResource(Context context) {
        super(context);
    }

    @Override
    protected ArrayList load(int page) throws Exception {
        this.page=page;
        ArrayList<NetResult> models = new ArrayList<>();
        //1：已报名  2：已参加
        MyActivitiesBean bean= AppUtil.getPpApiClient(ac).getMyActivitiesBean(ac.user_id,page+"","10","2");
        if(bean.isOK()){
            for(MyActivitiesBean.Content item : bean.getContent()){
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
