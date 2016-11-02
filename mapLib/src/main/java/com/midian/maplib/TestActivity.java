package com.midian.maplib;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import midian.baselib.base.BaseActivity;
import midian.baselib.widget.BaseLibTopbarView;

/**
 * Created by Administrator on 2016/9/22 0022.
 */

public class TestActivity extends BaseActivity {
    MapView mMapView = null;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener=new MyLocationListener();
    private BaiduMap mBaidumap;
    private boolean isFristLocation=true;
    private LatLng mLastLocationData;
    private String lon,lat;
    private Button btnDao;
    private ImageView ivSelf;
    private ImageView ivTarget;

    private LatLng ll_Self;
    private LatLng ll_Target;
    private BaseLibTopbarView topbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_my_postion);
        lon=mBundle.getString("lon");
        lat=mBundle.getString("lat");
        mMapView = findView(R.id.map_view);
        btnDao=findView(R.id.btn_Dao);
        ivSelf=findView(R.id.iv_Self);
        ivTarget=findView(R.id.iv_Target);
        topbar=findView(R.id.topbar);
        topbar.setLeftText("返回",listener);
        topbar.setLeftImageButton(R.drawable.icon_back,listener);
        topbar.setTitle("活动地点");
        btnDao.setOnClickListener(this);
        ivSelf.setOnClickListener(this);
        ivTarget.setOnClickListener(this);
        mMapView.showZoomControls(false);
        initLocation();
        mBaidumap=mMapView.getMap();
        MapStatusUpdate msu= MapStatusUpdateFactory.zoomTo(18.0f);
        mBaidumap.setMapStatus(msu);

        ll_Target=new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
        setMarker();
        ivTarget.performClick();
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    private void initLocation(){
        mLocationClient=new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(myListener);
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        int id = arg0.getId();
        if(id==R.id.iv_Self){
            Log.d("wqf","R.id.iv_Self");
            if(mLastLocationData!=null){
                MapStatusUpdate u=MapStatusUpdateFactory.newLatLng(ll_Self);
                mBaidumap.animateMapStatus(u);
            }
        }else if(id==R.id.iv_Target){
            Log.d("wqf","R.id.iv_Target");
            MapStatusUpdate u=MapStatusUpdateFactory.newLatLng(ll_Target);
            mBaidumap.animateMapStatus(u);
        }else if(id==R.id.btn_Dao){
            // 构建 导航参数
            NaviParaOption para = new NaviParaOption()
                    .startPoint(ll_Self).endPoint(ll_Target)
                    .startName("我的位置").endName("活动地点");

            try {
                BaiduMapNavigation.openBaiduMapNavi(para, this);
            } catch (BaiduMapAppNotSupportNaviException e) {
                e.printStackTrace();
//                showDialog();
            }
        }
    }

    private void setMarker(){
        BitmapDescriptor bitmap= BitmapDescriptorFactory.fromResource(R.drawable.icon_target);
        OverlayOptions options=new MarkerOptions()
                .position(ll_Target)
                .icon(bitmap);
        mBaidumap.addOverlay(options);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBaidumap.setMyLocationEnabled(true);
        if(!mLocationClient.isStarted()){
            mLocationClient.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation){
            if(bdLocation==null||mMapView==null){
                return;
            }
            MyLocationData locData=new MyLocationData.Builder()
//                    .accuracy(bdLocation.getRadius())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            mBaidumap.setMyLocationData(locData);
            ll_Self=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            mLastLocationData=ll_Self;
            if(isFristLocation){
                isFristLocation=false;
                MapStatusUpdate u=MapStatusUpdateFactory.newLatLng(ll_Target);
                mBaidumap.animateMapStatus(u);
            }
        }
    }
}
