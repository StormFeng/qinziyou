package com.midian.qzy.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.midian.qzy.R;
import com.midian.qzy.datasource.CommentDataResource;
import com.midian.qzy.itemtpl.CommentBotTpl;
import com.midian.qzy.itemtpl.CommentTpl;
import java.util.ArrayList;

import midian.baselib.base.BaseMultiTypeListFragment;
import midian.baselib.shizhefei.view.listviewhelper.IDataAdapter;
import midian.baselib.shizhefei.view.listviewhelper.IDataSource;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.pulltorefresh.PullToRefreshListView;

/**
 * Created by Administrator on 2016/7/18 0018.
 * 评论页Fragment
 */
@SuppressLint("ValidFragment")
public class CommentFragment extends BaseMultiTypeListFragment{

    public static String activity_id;
    public static boolean isUpdate=false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_activitycomment,null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public CommentFragment() {
    }

    public CommentFragment(String activity_id) {
        this.activity_id = activity_id;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activitycomment;
    }

    @Override
    protected IDataSource<ArrayList> getDataSource() {
//        Log.d("wqf",activity_id);
        return new CommentDataResource(_activity,activity_id);
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList tpls=new ArrayList();
        tpls.add(CommentBotTpl.class);
        tpls.add(CommentTpl.class);
        return tpls;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(isUpdate){
            listViewHelper.refresh();
            isUpdate=false;
        }
    }
}
