package com.midian.qzy.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.midian.qzy.R;
import com.midian.qzy.ui.home.ActivitySignUp;
import com.midian.qzy.ui.myactivity.JoinedActicityFragment;
import com.midian.qzy.ui.myactivity.SignedActicityFragment;
import com.midian.qzy.widget.Banner;

import java.util.ArrayList;
import java.util.List;

import midian.baselib.base.BaseFragment;
import midian.baselib.utils.UIHelper;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class MyActivityFragment extends BaseFragment implements View.OnClickListener{
    private ViewPager viewPager;
    private JoinedActicityFragment joinedActicityFragment;//已参加活动
    private SignedActicityFragment signedActicityFragment;//已报名活动
    private List<Fragment> fragments=new ArrayList<>();
    private TextView tab1,tab2;
    private View v_tab1,v_tab2;
    private View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_activity, null);
        tab1= (TextView) v.findViewById(R.id.tab1);
        tab2= (TextView) v.findViewById(R.id.tab2);
        v_tab1=v.findViewById(R.id.v_tab1);
        v_tab2=v.findViewById(R.id.v_tab2);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        joinedActicityFragment=new JoinedActicityFragment();
        signedActicityFragment=new SignedActicityFragment();
        fragments.add(signedActicityFragment);
        fragments.add(joinedActicityFragment);
        viewPager= (ViewPager) v.findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
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
                switch (position){
                    case 0:
                        setButton(tab1,v_tab1);
                        break;
                    case 1:
                        setButton(tab2,v_tab2);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setButton(tab1,v_tab1);
        return v;
    }
    private void setButton(TextView tab,View arg0){
        tab1.setTextColor(Color.parseColor("#9B9B9B"));
        tab2.setTextColor(Color.parseColor("#9B9B9B"));
        v_tab1.setBackgroundColor(getResources().getColor(R.color.window_bg));
        v_tab2.setBackgroundColor(getResources().getColor(R.color.window_bg));
        tab1.setClickable(true);
        tab2.setClickable(true);
        tab.setTextColor(getResources().getColor(R.color.blue));
        arg0.setBackgroundColor(getResources().getColor(R.color.blue));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab1:
                setButton(tab1,v_tab1);
                viewPager.setCurrentItem(0);
                break;
            case R.id.tab2:
                setButton(tab2,v_tab2);
                viewPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            Log.d("wqf","onResume();");
            signedActicityFragment.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
