package com.midian.maplib;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.midian.maplib.LocationUtil;
import com.midian.maplib.R;
import com.midian.maplib.map.Cluster;
import com.midian.maplib.map.ItemBean;
import com.midian.maplib.map.MapViewFactory;
import com.midian.maplib.map.Marks;

import java.util.ArrayList;
import java.util.List;

import midian.baselib.base.BaseFragment;

/**
 * Created by MIDIAN on 2015/12/15.
 */
public class MyPositionFragment extends BaseFragment implements OnGetPoiSearchResultListener {
    MapFragmentListener mMapFragmentListener;
    MapViewFactory mapView;
    LatLng latlng;
    List<ItemBean> itembeanList = new ArrayList<ItemBean>();
    PoiSearch mPoiSearch;
    List<PoiInfo> list;
    String lon, lat, curlat, curlon, address;
    private BaiduMap baiduMap;
    private MapView mMapView;
    private Boolean isAverageCenter = false;
    private List<Marks> mMarkersForItembean = new ArrayList<Marks>();
    // 当前的聚合集合
    private List<Marks> currentClustersForItembean = new ArrayList<Marks>();
    private Cluster mCluster;
    private Cluster mECluster;
    private double mDistance = 600000;
    // 左上角的经纬度，用于判断地图改变范围
    private LatLng leftTopLatlng;
    // 当前数据的缩放等级
    private float cuttentZoom = 0;
    // 移动一定距离后，才开始load
    private int changeRange;
    private List<Marker> markers = new ArrayList<Marker>();
    /**
     * 地图状态改变监听器
     */
    private BaiduMap.OnMapStatusChangeListener changeListener = new BaiduMap.OnMapStatusChangeListener() {

        @Override
        public void onMapStatusChange(MapStatus arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onMapStatusChangeFinish(MapStatus status) {
            // TODO Auto-generated method stub

            Projection mProjection = baiduMap.getProjection();
            Point pointB = new Point(0, 0);
            LatLng tempLatlbg = mProjection.fromScreenLocation(pointB);

            // 判断
            if (leftTopLatlng != null && cuttentZoom == status.zoom) {
                pointB = mProjection.toScreenLocation(leftTopLatlng);
                if (Math.abs(pointB.x) < changeRange
                        && Math.abs(pointB.y) < changeRange) {
                    // 不够范围
                    return;
                }
            }
            // System.out.println("结束变化时间2：：" + new Date());
            cuttentZoom = status.zoom;
            System.out.println("cuttentZoom2：：" + cuttentZoom);
            leftTopLatlng = tempLatlbg;
            latlng = status.target;
            mCluster.setmDistance(getDistance(status.zoom));
            getNetData(status.bound.southwest.longitude + "",
                    status.bound.northeast.latitude + "",
                    status.bound.northeast.longitude + "",
                    status.bound.southwest.latitude + "");

        }

        @Override
        public void onMapStatusChangeStart(MapStatus arg0) {
            // TODO Auto-generated method stub

        }
    };
    BaiduMap.OnMapLoadedCallback callback = new BaiduMap.OnMapLoadedCallback() {
        /**
         * 地图加载完成回调函数
         */
        public void onMapLoaded() {
            changeListener.onMapStatusChangeFinish(baiduMap.getMapStatus());

            // mSearch.reverseGeoCode(new
            // ReverseGeoCodeOption().location(baiduMap
            // .getMapStatus().target));
        }
    };
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

                    curlat = location.getLatitude() + "";
                    curlon = location.getLongitude() + "";
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MapFragmentListener)
            mMapFragmentListener = (MapFragmentListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, null);
        return view;
    }

    private void initMap(View view) {
        mMapView = (MapView) view.findViewById(R.id.map);
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
        LocationUtil.getInstance(_activity).startOneLocation(
                OneLocationListener);
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true, null));
        mapView = MapViewFactory.getInstance(_activity);
        mCluster = new Cluster(baiduMap, isAverageCenter, 80, mDistance);
        baiduMap.setOnMapStatusChangeListener(changeListener);
        baiduMap.setOnMapLoadedCallback(callback);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                // TODO Auto-generated method stub
                Bundle b = arg0.getExtraInfo();
                int i = b.getInt("id");
                int count = b.getInt("count");
                Marks m = currentClustersForItembean.get(i);

                if (count > 1) {

                    String ids = "";
                    int j = 0;
                    for (Marks item : m.getmMarkers()) {
                        if (j == 0) {
                            ids = item.getItemBean().getId();
                        } else {
                            ids = ids + "," + item.getItemBean().getId();
                        }
                        if (j > 50)
                            break;
                        j++;
                    }
                    Bundle p = new Bundle();
                    p.putString("ids", ids);
                    onClickMark(false, p);
                } else {
                    lon = m.getItemBean().getLon();
                    lat = m.getItemBean().getLat();
                    address = m.getItemBean().getName();
                    Bundle p = new Bundle();
                    p.putString("id", m.getItemBean().getId());
                    onClickMark(true, p);
                    changeState(i);
                }
                return false;
            }
        });


        baiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {

            @Override
            public void onTouch(MotionEvent arg0) {
                // TODO Auto-generated method stub
            }
        });
        initPoiSearch();
    }

    public void changeState(int index) {
        for (int i = 0; i < currentClustersForItembean.size(); i++) {
            View itemView = mapView.createDefaultView(i,
                    currentClustersForItembean.get(i).getCount());

            TextView title = (TextView) itemView.findViewById(R.id.title);
            ImageView img = (ImageView) itemView.findViewById(R.id.ioc_img);
            TextView number_tx = (TextView) itemView
                    .findViewById(R.id.number_tx);
            title.setVisibility(View.GONE);
            if (currentClustersForItembean.get(i).getCount() > 1) {
                number_tx.setText(currentClustersForItembean.get(i).getCount()
                        + "");
                number_tx.setVisibility(View.VISIBLE);
            } else {
                number_tx.setVisibility(View.GONE);
            }

//            if (i == index) {
//                img.setImageResource(getIconId(true));
//            } else {
//                img.setImageResource(getIconId(false));
//            }
            BitmapDescriptor bdA = BitmapDescriptorFactory.fromView(itemView);
            if (markers != null && markers.size() > i)
                markers.get(i).setIcon(bdA);
        }
    }



    private void initMark() {

        for (int i = 0; i < itembeanList.size(); i++) {
            ItemBean itembean = itembeanList.get(i);
            LatLng latLng = new LatLng(Double.parseDouble(itembean.getLat()),
                    Double.parseDouble(itembean.getLon()));

            Marks marks = new Marks(latLng);
            marks.setName(itembean.getName());
            if (TextUtils.isEmpty(itembean.getPic())) {
                marks.setPic("");
            } else {
                marks.setPic(itembean.getPic());
            }
            marks.setItemBean(itembean);
            mMarkersForItembean.add(marks);
        }

        currentClustersForItembean = mCluster.createCluster(mMarkersForItembean);

        for (int i = 0; i < currentClustersForItembean.size(); i++) {
            Marks m = currentClustersForItembean.get(i);

            View mView = mapView.createDefaultView(i, m.getCount());
            TextView title = (TextView) mView.findViewById(R.id.title);
            ImageView img = (ImageView) mView.findViewById(R.id.ioc_img);
            TextView number_tx = (TextView) mView.findViewById(R.id.number_tx);

//            title.setVisibility(View.GONE);
//            if (currentClustersForItembean.get(i).getCount() > 1) {
//                number_tx.setText(currentClustersForItembean.get(i).getCount()
//                        + "");
//                number_tx.setVisibility(View.VISIBLE);
//            } else {
//                number_tx.setVisibility(View.GONE);
////                if (isTest) {
////                    title.setText(currentClustersForItembean.get(i).getItemBean()
////                            .getName());
////                    title.setVisibility(View.VISIBLE);
////                }
//            }

//            img.setImageResource(getIconId(false));

            initMarkView(title, number_tx, img, currentClustersForItembean.get(i).getName(), currentClustersForItembean.get(i).getPic(), currentClustersForItembean.get(i).getCount());

            BitmapDescriptor bdA = BitmapDescriptorFactory.fromView(mView);
            OverlayOptions ooA = new MarkerOptions().position(m.getPosition())
                    .icon(bdA).zIndex(9);
            Marker marker = (Marker) (baiduMap.addOverlay(ooA));
            markers.add(marker);
            marker.setTitle(m.getName());
            Bundle b = new Bundle();
            b.putInt("id", i);
            b.putInt("count", m.getCount());
            marker.setExtraInfo(b);
        }
    }

    public void initPoiSearch() {
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        // mSearch = GeoCoder.newInstance();
        // mSearch.setOnGetGeoCodeResultListener(this);
    }

    /**
     * 地图缩放等级
     *
     * @param room
     * @return
     */
    private double getDistance(float room) {
        double d = mDistance;

        if (room > 6) {
            d = mDistance;
        } else if (room > 5) {
            d = mDistance * 2;
        } else if (room > 4) {
            d = mDistance * 3;
        } else if (room > 3) {
            d = mDistance * 4;
        } else {
            d = mDistance * 5;
        }

        return d;
    }



    /**
     * 对外使用接口
     */
    public interface MapFragmentListener {
        public void getNetData(String left_top_lon, String left_top_lat, String right_bottom_lon, String right_bottom_lat);

        public void onClickMark(boolean isOne, Bundle b);

        public void initMarkView(TextView title, TextView number, ImageView img, String titleStr, String url, int count);

        public void changeMarkView(TextView title, TextView number, ImageView img, String titleStr, String url, int count, boolean select);
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

    /**
     * 定位
     */
    public void location(){
        LocationUtil.getInstance(_activity).startOneLocation(
                OneLocationListener);
    }

    public void refreshMap(List<ItemBean> itembeanList) {
        this.itembeanList = itembeanList;
        initMark();
    }
    /**
     * 获取地图数据
     * @param left_top_lon
     * @param left_top_lat
     * @param right_bottom_lon
     * @param right_bottom_lat
     */
    private void getNetData(String left_top_lon, String left_top_lat,
                            String right_bottom_lon, String right_bottom_lat) {
        if (mMapFragmentListener != null)
            mMapFragmentListener.getNetData(left_top_lon, left_top_lat,
                    right_bottom_lon, right_bottom_lat);
    }

    /**
     * 点击地图图标
     * @param isOne
     * @param b
     */
    private void onClickMark(boolean isOne, Bundle b) {
        if (mMapFragmentListener != null)
            mMapFragmentListener.onClickMark(isOne, b);
    }

    /**
     * 初始化显示图标
     * @param title
     * @param number
     * @param img
     * @param titleStr
     * @param url
     * @param count
     */
    public void initMarkView(TextView title, TextView number, ImageView img, String titleStr, String url, int count) {
        if (mMapFragmentListener != null)
            mMapFragmentListener.initMarkView(title,number,img,titleStr, url, count);
    }

    /**
     * 改变地图图标显示
     * @param title
     * @param number
     * @param img
     * @param titleStr
     * @param url
     * @param count
     * @param select
     */
    public void changeMarkView(TextView title, TextView number, ImageView img, String titleStr, String url, int count, boolean select) {
        if (mMapFragmentListener != null)
            mMapFragmentListener.changeMarkView(title,number,img,titleStr, url, count,select);
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        // TODO Auto-generated method stub
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            return;
        } else {
            list = result.getAllPoi();
            if (list != null && list.size() > 0) {
                MyLocationData locData = new MyLocationData.Builder()
                        .direction(100).latitude(list.get(0).location.latitude)
                        .longitude(list.get(0).location.longitude).build();
                baiduMap.setMyLocationData(locData);

                for (PoiInfo item : list) {
                    System.out.println("item:::::::" + item.name);
                }
            }
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPoiSearch != null)
            mPoiSearch.destroy();
    }


}
