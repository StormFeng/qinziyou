package com.midian.qzy.datasource;

import android.content.Context;

import com.midian.qzy.bean.MyActivitiesBean;
import com.midian.qzy.utils.AppUtil;

import java.util.ArrayList;

import midian.baselib.base.BaseListDataSource;
import midian.baselib.bean.NetResult;

public class SignedActivityDataResource extends BaseListDataSource {
    public SignedActivityDataResource(Context context) {
        super(context);
    }

    @Override
    protected ArrayList load(int page) throws Exception {
        this.page=page;
        ArrayList<NetResult> models = new ArrayList<>();
        MyActivitiesBean bean= AppUtil.getPpApiClient(ac).getMyActivitiesBean(ac.user_id,page+"","10","1");
        if(bean.isOK()){
            if(bean.getContent().size()!=0){
                for(MyActivitiesBean.Content item : bean.getContent()){
                    item.setItemViewType(0);
                    models.add(item);
                }
                if(bean.getContent().size()<10){
                    NetResult netResult=new NetResult();
                    netResult.setItemViewType(1);
                    models.add(netResult);
                    hasMore=false;
                }else{
                    hasMore=true;
                }
            }
        }else{
//            ac.handleErrorCode(context,bean.ret_code);
        }
        return models;
    }
}
