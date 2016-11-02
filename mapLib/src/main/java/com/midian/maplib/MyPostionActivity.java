package com.midian.maplib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus.Builder;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.io.File;
import java.net.URISyntaxException;

import midian.baselib.base.BaseActivity;
import midian.baselib.utils.FDDataUtils;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;
import midian.baselib.widget.dialog.CommonDialog;

/**
 * 我的位置
 *
 * @author MIDIAN
 */
public class MyPostionActivity extends BaseActivity {

    public static final String LON_KEY = "lon";
    public static final String LAT_KEY = "lat";
    public static final String ADDRESS_KEY = "address";
    RelativeLayout mposition;
    TextView tipTv;
    ImageView navigation_iv;
    BaseLibTopbarView mBaseLibTopbarView;
    private String lon;
    private String lat;
    private String address;
    private String curlon = "";
    private String curlat = "";
    private MapView mapView;
    private BaiduMap mBaiduMap;


    public static void gotoMyPostion(Context mContext, String address,
                                     String lon, String lat) {
        Intent intent = new Intent(mContext, MyPostionActivity.class);
        intent.putExtra(MyPostionActivity.LON_KEY, lon);
        intent.putExtra(MyPostionActivity.LAT_KEY, lat);
        intent.putExtra(MyPostionActivity.ADDRESS_KEY, address);

        mContext.startActivity(intent);
    }
    NavigationManager mNavigationManager;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_my_postion);

        mBaseLibTopbarView = (BaseLibTopbarView) findViewById(R.id.topbar);
        mBaseLibTopbarView
                .recovery()
                .setLeftImageButton(R.drawable.icon_back,
                        UIHelper.finish(_activity)).setTitle("地图")
                .setLeftText("返回", null);

        lon = getIntent().getStringExtra(LON_KEY);
        lat = getIntent().getStringExtra(LAT_KEY);
//        address = getIntent().getStringExtra(ADDRESS_KEY);

        System.out.println("map_lon" + lon);
        System.out.println("map_lat" + lat);

//        mposition = (RelativeLayout) LayoutInflater.from(this).inflate(
//                R.layout.item_map_ioc, null);
//        ImageView ioc_img = (ImageView) mposition.findViewById(R.id.ioc_img);
//        ioc_img.setImageResource(R.drawable.map_number_bg);
//        TextView number_tx = (TextView) mposition.findViewById(R.id.number_tx);
//        TextView title = (TextView) mposition.findViewById(R.id.title);
//        title.setText(address);
//        number_tx.setVisibility(View.GONE);

        initView();
    }

    private void initView() {
        address="我的位置";
        mapView = (MapView) findViewById(R.id.map_view);
        mNavigationManager=new NavigationManager(_activity);
        mNavigationManager.setPosition(ac.lat,ac.lon,lat,lon,address);
    }

    @Override
    protected void onStart() {

        super.onStart();

        mBaiduMap = mapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setTrafficEnabled(false);
        System.out.println("FDDataUtils.getDouble(lon)"
                + FDDataUtils.getDouble(lon));
        System.out.println("FDDataUtils.getDouble(lat)"
                + FDDataUtils.getDouble(lat));

        LatLng point = new LatLng(FDDataUtils.getDouble(lat),
                FDDataUtils.getDouble(lon));
        Builder mbuild = new Builder();
        mbuild.target(point);
        mbuild.zoom(15);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(mbuild.build());

        mBaiduMap.setMapStatus(mapStatusUpdate);

        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(mposition);
        OverlayOptions option = new MarkerOptions().position(point)
                .icon(bitmap);

        mBaiduMap.addOverlay(option);

        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                // TODO Auto-generated method stub
                mNavigationManager.showDialog();
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mNavigationManager!=null)
        mNavigationManager.onDestroy();
    }
}
