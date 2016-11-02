package com.midian.qzy.datasource;

import android.content.Context;

import com.midian.qzy.bean.ActivityCommentsBean;
import com.midian.qzy.utils.AppUtil;

import java.util.ArrayList;
import midian.baselib.base.BaseListDataSource;
import midian.baselib.bean.NetResult;

public class CommentDataResource extends BaseListDataSource {
    private String activity_id;
    public CommentDataResource(Context context) {
        super(context);
    }

    public CommentDataResource(Context context, String activity_id) {
        super(context);
        this.activity_id = activity_id;
    }

    @Override
    protected ArrayList load(int page) throws Exception {
        this.page=page;
        ArrayList<NetResult> models = new ArrayList<>();
        if(page==1){
            NetResult netResult = new NetResult();
            netResult.setItemViewType(0);
            models.add(netResult);
        }
        ActivityCommentsBean bean = AppUtil.getPpApiClient(ac).getActivitiesCommentsBean(activity_id, page + "", "10");
        if(bean.isOK()){
            for(ActivityCommentsBean.Content item : bean.getContent()){
                item.setItemViewType(1);
                models.add(item);
            }
            if(bean.getContent().size()<10){
                hasMore=false;
            }else{
                hasMore=true;
            }
        }else{
            ac.handleErrorCode(context,bean.ret_info);
        }
        return models;
    }
}
