package com.midian.qzy.ui.home;

import android.os.Bundle;
import android.util.Log;

import com.midian.qzy.R;
import com.midian.qzy.datasource.SearchResultResource;
import com.midian.qzy.itemtpl.SearchResultTpl;

import java.util.ArrayList;

import midian.baselib.base.BaseListActivity;
import midian.baselib.shizhefei.view.listviewhelper.IDataSource;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class ActivitySearchResult extends BaseListActivity{
    private BaseLibTopbarView topbar;
    private String time="",distance="",type="",age="",key="",lat="",lon="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topbar=findView(R.id.topbar);
        topbar.setTitle("").setLeftText("返回", UIHelper.finish(_activity)).setLeftImageButton(R.drawable.icon_back,UIHelper.finish(_activity));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_topbar_list;
    }

    @Override
    protected IDataSource<ArrayList> getDataSource() {
        if("HomeFragmentTopTpl".equals(mBundle.getString("flag"))){
            key=mBundle.getString("keyWord");
        }else{
            time=mBundle.getString("time");
            distance=mBundle.getString("distance");
            type=mBundle.getString("type");
            age=mBundle.getString("age");
            lat=mBundle.getString("lat");
            lon=mBundle.getString("lon");

        }
//        Log.d("wqf",time+"\n"+type+"\n"+distance+"\n"+age);
        return new SearchResultResource(_activity,time,distance,type,age,key,lat,lon);
    }

    @Override
    protected Class getTemplateClass() {
        return SearchResultTpl.class;
    }
}
