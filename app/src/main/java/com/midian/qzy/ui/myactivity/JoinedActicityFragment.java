package com.midian.qzy.ui.myactivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midian.qzy.R;
import com.midian.qzy.datasource.JoinedActivityDataResource;
import com.midian.qzy.itemtpl.JoinedActivityTpl;

import java.util.ArrayList;

import midian.baselib.base.BaseFragment;
import midian.baselib.base.BaseListFragment;
import midian.baselib.shizhefei.view.listviewhelper.IDataSource;
import midian.baselib.shizhefei.view.listviewhelper.ListViewHelper;

/**
 * Created by Administrator on 2016/7/18 0018.
 * 已参加活动页
 */
public class JoinedActicityFragment extends BaseListFragment{

    public static ListViewHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        helper=listViewHelper;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_joinedactivity;
    }

    @Override
    protected IDataSource<ArrayList> getDataSource() {
        return new JoinedActivityDataResource(_activity);
    }

    @Override
    protected Class getTemplateClass() {
        return JoinedActivityTpl.class;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("wqf","listViewHelper.refresh()-1");
        listViewHelper.refresh();
    }
}
