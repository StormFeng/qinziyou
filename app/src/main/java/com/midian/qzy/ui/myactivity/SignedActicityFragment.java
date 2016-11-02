package com.midian.qzy.ui.myactivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midian.qzy.R;
import com.midian.qzy.datasource.SignedActivityDataResource;
import com.midian.qzy.itemtpl.SignedActivityTpl;

import java.util.ArrayList;

import midian.baselib.base.BaseFragment;
import midian.baselib.base.BaseListFragment;
import midian.baselib.shizhefei.view.listviewhelper.IDataAdapter;
import midian.baselib.shizhefei.view.listviewhelper.IDataSource;

/**
 * Created by Administrator on 2016/7/18 0018.
 * 已报名活动页
 */
public class SignedActicityFragment extends BaseListFragment{

    private int flag=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        flag++;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_joinedactivity;
    }

    @Override
    protected IDataSource<ArrayList> getDataSource() {
        return new SignedActivityDataResource(_activity);
    }

    @Override
    protected Class getTemplateClass() {
        return SignedActivityTpl.class;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(flag>0){
                Log.d("wqf","listViewHelper.refresh()-2");
                listViewHelper.refresh();
            }
        }
    }
}
