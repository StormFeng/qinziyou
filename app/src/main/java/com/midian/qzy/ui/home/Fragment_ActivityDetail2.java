package com.midian.qzy.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.midian.qzy.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midian.baselib.base.BaseFragment;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
@SuppressLint("ValidFragment")
public class Fragment_ActivityDetail2 extends BaseFragment {
    @BindView(R.id.tab1)
    TextView tab1;
    @BindView(R.id.tab2)
    TextView tab2;
    @BindView(R.id.v_tab1)
    View vTab1;
    @BindView(R.id.v_tab2)
    View vTab2;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    public String detailUrl;
    public String activity_id;
    private FragmentManager fm;
    private List<String> params;
    private List<Fragment> fragments = new ArrayList<>();
    private ActivityDetailFragment activityDetailFragment;
    private CommentFragment commentFragment;
    View v;

    public Fragment_ActivityDetail2() {
    }

    public Fragment_ActivityDetail2(List<String> params, FragmentManager fm) {
        this.params=params;
        this.fm=fm;
        activity_id=params.get(0);
        detailUrl=params.get(1);
        Log.e("wqf","Fragment_ActivityDetail2初始化");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_detail_2, null);
        ButterKnife.bind(this, v);
        initView();
        return v;
    }

    private void initView(){
        setButton(R.id.tab1,R.id.v_tab1);
        activityDetailFragment=new ActivityDetailFragment(detailUrl);
        commentFragment=new CommentFragment(activity_id);
        fragments.add(activityDetailFragment);
        fragments.add(commentFragment);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    setButton(R.id.tab1,R.id.v_tab1);
                }else{
                    setButton(R.id.tab2,R.id.v_tab2);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    @OnClick({R.id.tab1, R.id.tab2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab1:
                viewPager.setCurrentItem(0);
                setButton(R.id.tab1,R.id.v_tab1);
                break;
            case R.id.tab2:
                viewPager.setCurrentItem(1);
                setButton(R.id.tab2,R.id.v_tab2);
                break;
        }
    }
    private void setButton(int tab_id, int v_id){
        tab1.setTextColor(_activity.getResources().getColor(R.color.dp_black));
        tab2.setTextColor(_activity.getResources().getColor(R.color.dp_black));
        vTab1.setBackgroundColor(_activity.getResources().getColor(R.color.window_bg));
        vTab2.setBackgroundColor(_activity.getResources().getColor(R.color.window_bg));
        tab1.setClickable(true);
        tab2.setClickable(true);
        TextView tv = (TextView) v.findViewById(tab_id);
        View view=v.findViewById(v_id);
        tv.setClickable(false);
        tv.setTextColor(_activity.getResources().getColor(R.color.blue));
        view.setBackgroundColor(_activity.getResources().getColor(R.color.blue));
    }
}
