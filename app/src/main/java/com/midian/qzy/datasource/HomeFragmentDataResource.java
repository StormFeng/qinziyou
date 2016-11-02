package com.midian.qzy.datasource;

import android.content.Context;
import android.util.Log;

import com.midian.qzy.bean.ActivitiesBean;
import com.midian.qzy.bean.BannerBean;
import com.midian.qzy.itemtpl.HomeFragmentMulBean;
import com.midian.qzy.utils.AppUtil;
import com.midian.qzy.widget.Banner;

import java.util.ArrayList;

import midian.baselib.base.BaseListDataSource;
import midian.baselib.bean.NetResult;

public class HomeFragmentDataResource extends BaseListDataSource {

    public HomeFragmentDataResource(Context context) {
        super(context);
    }

    @Override
    protected ArrayList load(int page) throws Exception {
        this.page=page;
        ArrayList<HomeFragmentMulBean> models = new ArrayList<>();
        BannerBean topBean=AppUtil.getPpApiClient(ac).getBanner();
        if(page==1){
            if(topBean.isOK()){
                HomeFragmentMulBean item0=new HomeFragmentMulBean();
                item0.setItemViewType(0);
                item0.topBean=topBean;
                models.add(item0);
            }else{
                ac.handleErrorCode(context,topBean.ret_code);
            }
        }
        ActivitiesBean botBean= AppUtil.getPpApiClient(ac).getActivitiesBean(page+"","10",null,null,null,null,null,null,null);
        if(botBean.isOK()){
            for(ActivitiesBean.Content bean : botBean.getContent()){
                HomeFragmentMulBean item1=new HomeFragmentMulBean();
                item1.setItemViewType(1);
                item1.botBean=bean;
                models.add(item1);
            }
            Log.e("wqf",botBean.getContent().size()+"");
            if(botBean.getContent().size()<10){
                hasMore=false;
            }else{
                hasMore=true;
            }
        }else{
            ac.handleErrorCode(context,botBean.ret_code);
        }
//        for(int i=0;i<10;i++){
//            HomeFragmentMulBean item1=new HomeFragmentMulBean();
//            item1.setItemViewType(1);
//            models.add(item1);
//        }
        return models;
    }
}
