package com.midian.qzy.ui.home;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.midian.qzy.R;
import com.midian.qzy.bean.ActivityFilterParamsBean;
import com.midian.qzy.utils.AppUtil;
import com.midian.qzy.widget.tagflowlayout.FlowLayout;
import com.midian.qzy.widget.tagflowlayout.TagAdapter;
import com.midian.qzy.widget.tagflowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import midian.baselib.base.BaseActivity;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

/**
 * 首页筛选页面
 */
public class SearchActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{

    private BaseLibTopbarView topbar;
    private TagFlowLayout tag_time;
    private TagFlowLayout tag_type;
    private TagFlowLayout tag_age;
    private TagFlowLayout tag_position;
    private CheckBox cb_time;
    private CheckBox cb_type;
    private CheckBox cb_age;
    private CheckBox cb_position;
    private List<String> times=new ArrayList<>();
    private List<String> timeIds=new ArrayList<>();
    private List<String> types=new ArrayList<>();
    private List<String> typeIds=new ArrayList<>();
    private List<String> ages=new ArrayList<>();
    private List<String> positions=new ArrayList<>();

    private String time;
    private String distance;

    private LocationManager locationManager;
    private Location location;
    private String lat = "", lon = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        topbar=findView(R.id.topbar);
        topbar.setLeftImageButton(R.drawable.icon_back, UIHelper.finish(_activity));
        topbar.setTitle("筛选").setLeftText("返回",UIHelper.finish(_activity)).setRightText("确定",this);
        tag_time=findView(R.id.tag_time);
        tag_type=findView(R.id.tag_type);
        tag_age=findView(R.id.tag_age);
        tag_position=findView(R.id.tag_position);
        tag_time.setMaxSelectCount(1);
        tag_position.setMaxSelectCount(1);
        tag_time.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Log.d("wqf","点击");
                if(tag_time.getSelectedList().size()>0) {
                    cb_time.setChecked(false);
                    time=timeIds.get(position);
                }else{
                    cb_time.setChecked(true);
                }
                return false;
            }
        });
        tag_age.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if(tag_age.getSelectedList().size()>0) {
                    cb_age.setChecked(false);
                }else{
                    cb_age.setChecked(true);
                }
                return false;
            }
        });
        tag_type.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if(tag_type.getSelectedList().size()>0) {
                    cb_type.setChecked(false);
                }else{
                    cb_type.setChecked(true);
                }
                return false;
            }
        });
        tag_position.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if(tag_position.getSelectedList().size()>0) {
                    cb_position.setChecked(false);
                    distance=positions.get(position).replace("Km","");
                }else{
                    cb_position.setChecked(true);
                }
                return false;
            }
        });
        cb_time=findView(R.id.cb_time);
        cb_type=findView(R.id.cb_type);
        cb_age=findView(R.id.cb_age);
        cb_position=findView(R.id.cb_position);
        cb_time.setOnCheckedChangeListener(this);
        cb_type.setOnCheckedChangeListener(this);
        cb_age.setOnCheckedChangeListener(this);
        cb_position.setOnCheckedChangeListener(this);

        AppUtil.getPpApiClient(ac).activityFilterParams(this);

        times.add("今天");
        times.add("明天");
        times.add("周末");
        times.add("已结束");
        timeIds.add("today");
        timeIds.add("tomorrow");
        timeIds.add("weekend");
        timeIds.add("old");

        positions.add("5Km");
        positions.add("10Km");

        tag_time.setAdapter(new TagAdapter<String>(times) {
            @Override
            public View getView(FlowLayout parent, int position, final String s) {
                TextView tv = (TextView) LayoutInflater.from(_activity).inflate(R.layout.tag_layout, tag_time, false);
                tv.setText(s);
                return tv;
            }
        });

        tag_position.setAdapter(new TagAdapter<String>(positions) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(_activity).inflate(R.layout.tag_layout, tag_position, false);
                tv.setText(s);
                return tv;
            }
        });

        getLocation();
        if (location != null) {
            lat = location.getLatitude() + "";
            lon = location.getLongitude() + "";
        } else {
            Log.d("wqf", "定位为空");
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(_activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(_activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                UIHelper.t(_activity, "请在权限设置中允许未来宝贝使用定位");
            } else {
                ActivityCompat.requestPermissions(_activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3001);
            }
        } else {
            locationManager = (LocationManager) _activity.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
            criteria.setAltitudeRequired(false);//不要求海拔
            criteria.setBearingRequired(false);//不要求方位
            criteria.setCostAllowed(true);//允许有花费
            criteria.setPowerRequirement(Criteria.POWER_LOW);//低功率
            String provider = locationManager.getBestProvider(criteria, true);
            location = locationManager.getLastKnownLocation(provider);
        }
    }

    @Override
    public void onApiSuccess(NetResult res, String tag) {
        super.onApiSuccess(res, tag);
        if(res.isOK()){
            ActivityFilterParamsBean bean= (ActivityFilterParamsBean) res;
            for(int i=0;i<bean.getContent().getAges().size();i++){
                ages.add(bean.getContent().getAges().get(i).toString().replace("\"",""));
            }
            for(int i=0;i<bean.getContent().getTypes().size();i++){
                types.add(bean.getContent().getTypes().get(i).getType_name());
                typeIds.add(bean.getContent().getTypes().get(i).getType_id());
            }
            tag_type.setAdapter(new TagAdapter<String>(types) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(_activity).inflate(R.layout.tag_layout, tag_type, false);
                    tv.setText(s);
                    return tv;
                }
            });
            tag_age.setAdapter(new TagAdapter<String>(ages) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(_activity).inflate(R.layout.tag_layout, tag_age, false);
                    tv.setText(s+"岁");
                    return tv;
                }
            });
        }
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        List<String> temp_type=new ArrayList<>();
        Set<Integer> lType = tag_type.getSelectedList();
        Iterator<Integer> t=lType.iterator();
        while (t.hasNext()) {
            int i=t.next();
            temp_type.add(typeIds.get(i));
        }
        String type=temp_type.toString().replace("[", "").replace("]", "").replace(" ","");
        List<String> temp_age=new ArrayList<>();
        Set<Integer> lAge = tag_age.getSelectedList();
        Iterator<Integer> q=lAge.iterator();
        while (q.hasNext()) {
            int i=q.next();
            temp_age.add(ages.get(i));
        }
        String age=temp_age.toString().replace("[", "").replace("]", "").replace(" ","");
        if(cb_type.isChecked()){
            type = "";
        }
        if(cb_age.isChecked()){
            age = "";
        }
        if(cb_time.isChecked()){
            time = "";
        }
        if(cb_position.isChecked()){
            distance = "";
        }
        Log.d("wqf",time+"\n"+type+"\n"+age+"\n"+distance);
        Bundle bundle=new Bundle();
        bundle.putString("time",time);
        bundle.putString("distance",distance);
        bundle.putString("type",type);
        bundle.putString("age",age);
        bundle.putString("lat",lat);
        bundle.putString("lon",lon);
        bundle.putString("flag","SearchActivity");
        UIHelper.jump(_activity,ActivitySearchResult.class,bundle);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView==cb_time){
            if(isChecked){
                tag_time.getAdapter().setSelectedList();
            }
        }else if(buttonView==cb_type){
            if(isChecked){
                tag_type.getAdapter().setSelectedList();
            }
        }else if(buttonView==cb_age){
            if(isChecked){
                tag_age.getAdapter().setSelectedList();
            }
        }else if(buttonView==cb_position){
            if(isChecked){
                tag_position.getAdapter().setSelectedList();
            }
        }
    }
}
