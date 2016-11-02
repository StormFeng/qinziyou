package com.midian.qzy.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.midian.qzy.datasource.HomeFragmentDataResource;
import com.midian.qzy.itemtpl.HomeFragmentTopTpl;
import com.midian.qzy.itemtpl.HomeFragmentBotTpl;

import java.util.ArrayList;

import midian.baselib.base.BaseMultiTypeListFragment;
import midian.baselib.shizhefei.view.listviewhelper.IDataSource;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class HomeFragment extends BaseMultiTypeListFragment{



    @Override
    protected IDataSource<ArrayList> getDataSource() {
        return new HomeFragmentDataResource(_activity);
    }

//    @Override
//    public void onItemClick(AdapterView parent, View view, int position, long id) {
//        super.onItemClick(parent, view, position, id);
//        parent.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
//    }



    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls=new ArrayList<>();
        tpls.add(HomeFragmentTopTpl.class);
        tpls.add(HomeFragmentBotTpl.class);
        return tpls;
    }
}
