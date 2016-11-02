package com.midian.qzy.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midian.qzy.R;
import com.midian.qzy.datasource.NewsDataResource;
import com.midian.qzy.itemtpl.NewsTpl;

import java.util.ArrayList;

import midian.baselib.base.BaseFragment;
import midian.baselib.base.BaseListFragment;
import midian.baselib.shizhefei.view.listviewhelper.IDataSource;
import midian.baselib.widget.pulltorefresh.PullToRefreshListView;

/**
 * 消息列表页面
 * Created by Administrator on 2016/7/18 0018.
 */
public class NewsFragment extends BaseListFragment{

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected IDataSource<ArrayList> getDataSource() {
        return new NewsDataResource(_activity);
    }

    @Override
    protected Class getTemplateClass() {
        return NewsTpl.class;
    }
}
