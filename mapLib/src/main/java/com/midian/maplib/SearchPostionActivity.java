package com.midian.maplib;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.List;

import midian.baselib.base.BaseActivity;

/**
 * 搜索我的位置
 *
 * @author MIDIAN
 */
public class SearchPostionActivity extends BaseActivity implements OnGetGeoCoderResultListener,OnGetPoiSearchResultListener {
    public static final int REQUEST_CODE = 10002;
    public static final String LON_KEY = "lon_key";
    public static final String LAT_KEY = "lat_key";
    public static final String ADDRESS_KEY = "address_key";
    public static final String ADDRESS_NAME_KEY = "address_name_key";

    PoiSearch mPoiSearch;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    ReverseGeoCodeResult currReverseGeoCodeResult;
    private String lon = "";
    private String lat = "";
    private String selectAddress = "";
    private String name = "";
    List<PoiInfo> list;
    private BaiduMap baiduMap;
    private MapView mMapView;
    LatLng latlng;
    int selectIndex = 0;
    String currAddr = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initMap();
    }

    private void initMap() {
        mMapView = (MapView) findViewById(R.id.map);
        MapStatus ms = new MapStatus.Builder().overlook(0).zoom(15).build();
        MapStatusUpdate u1 = MapStatusUpdateFactory.newMapStatus(ms);
        baiduMap = mMapView.getMap();
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        UiSettings mapui = baiduMap.getUiSettings();

        baiduMap.setMapStatus(u1);
        mapui.setCompassEnabled(false);
        mapui.setOverlookingGesturesEnabled(false);
        mapui.setZoomGesturesEnabled(true);

        baiduMap.setMyLocationEnabled(true);
        baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true, null));
        initPoiSearch();

        baiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
                // TODO Auto-generated method stub
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(baiduMap.getMapStatus().target));

            }
        });

        LocationUtil.getInstance(_activity).startOneLocation(
                OneLocationListener);

        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

                    @Override
                    public void onMapStatusChangeStart(MapStatus arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onMapStatusChangeFinish(MapStatus arg0) {
                        // TODO Auto-generated method stub

                        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                                .location(arg0.target));

                    }

                    @Override
                    public void onMapStatusChange(MapStatus arg0) {
                        // TODO Auto-generated method stub

                    }
                });
    }

    public void initPoiSearch() {
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
         mSearch = GeoCoder.newInstance();
         mSearch.setOnGetGeoCodeResultListener(this);
    }

    /**
     * 地图搜索
     * @param key
     */
    public void search(String key) {

        if (latlng != null)
            mPoiSearch.searchNearby(new PoiNearbySearchOption()
                    .location(latlng).radius(2000000).pageNum(0).keyword(key));

    }

    private LocationUtil.OneLocationListener OneLocationListener = new LocationUtil.OneLocationListener() {
        @Override
        public void complete(BDLocation location) {
            try {
                // 定位为数据
                if (location != null) {
                    MyLocationData locData = new MyLocationData.Builder()
                            .direction(100).latitude(location.getLatitude())
                            .longitude(location.getLongitude()).build();
                    latlng = new LatLng(location.getLatitude(),
                            location.getLongitude());

                    baiduMap.setMyLocationData(locData);

                    try {
                        LocationUtil.getInstance(_activity).stopLocation();
                    } catch (Error e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private void returnResult() {
        Intent intent = getIntent();
        intent.putExtra(LON_KEY, lon);
        intent.putExtra(LAT_KEY, lat);
        intent.putExtra(ADDRESS_KEY, selectAddress);
        intent.putExtra(ADDRESS_NAME_KEY, name);
        setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
//            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
// TODO Auto-generated method stub
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            return;
        } else {
            list = result.getAllPoi();
//            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        currAddr = reverseGeoCodeResult.getAddress();
        selectIndex = 0;
        currReverseGeoCodeResult = reverseGeoCodeResult;
        selectAddress = currAddr;
        lon = reverseGeoCodeResult.getLocation().longitude + "";
        lat = reverseGeoCodeResult.getLocation().latitude + "";
        selectAddress = reverseGeoCodeResult.getAddress();
        latlng = new LatLng(reverseGeoCodeResult.getLocation().latitude,
                reverseGeoCodeResult.getLocation().longitude);
        search("城市");
    }
}
