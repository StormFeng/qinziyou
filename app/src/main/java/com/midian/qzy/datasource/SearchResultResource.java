package com.midian.qzy.datasource;

import android.content.Context;

import com.midian.qzy.bean.ActivitiesBean;
import com.midian.qzy.utils.AppUtil;

import java.util.ArrayList;

import midian.baselib.base.BaseListDataSource;
import midian.baselib.bean.NetResult;

public class SearchResultResource extends BaseListDataSource {
    private String time,distance,type,age,key,lat,lon;
    public SearchResultResource(Context context) {
        super(context);
    }

    public SearchResultResource(Context context, String time, String distance, String type, String age, String key,String lat,String lon) {
        super(context);
        this.time = time;
        this.distance = distance;
        this.type = type;
        this.age = age;
        this.key = key;
        this.lat=lat;
        this.lon=lon;
    }

    @Override
    protected ArrayList load(int page) throws Exception {
        this.page=page;
        ArrayList<NetResult> models = new ArrayList<>();
        ActivitiesBean bean= AppUtil.getPpApiClient(ac).getActivitiesBean(page+"","10",lon,lat,time,type,age,distance,key);
        if(bean.isOK()){
            for(ActivitiesBean.Content item : bean.getContent()){
                models.add(item);
            }
            if(bean.getContent().size()<10){
                hasMore=false;
            }else{
                hasMore=true;
            }
        }
        return models;
    }
}
