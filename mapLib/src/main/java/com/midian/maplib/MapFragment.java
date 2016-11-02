package com.midian.maplib;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.midian.maplib.map.Cluster;

import com.midian.maplib.map.ItemBean;
import com.midian.maplib.map.MapViewFactory;
import com.midian.maplib.map.Marks;

import java.util.ArrayList;
import java.util.List;

import midian.baselib.base.BaseFragment;
import midian.baselib.utils.FDDataUtils;

/**
 * Created by MIDIAN on 2015/12/15.
 */
public class MapFragment extends BaseFragment implements OnGetPoiSearchResultListener {
    MapFragmentListener mMapFragmentListener;
    MapViewFactory mapView;
    LatLng latlng;
    List<ItemBean> itembeanList = new ArrayList<ItemBean>();
    PoiSearch mPoiSearch;
    List<PoiInfo> list;
    String lon, lat, curlat, curlon, address;
    public static BaiduMap baiduMap;
    private  MapView mMapView;
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
        }

        @Override
        public void onMapStatusChangeFinish(MapStatus status) {
            Projection mProjection = baiduMap.getProjection();//Projection接口用于屏幕像素点坐标系统和地球表面经纬度点坐标系统之间的变换。
            Point pointA = new Point(0,0);//左上角坐标
            LatLng left = mProjection.fromScreenLocation(pointA);//将屏幕坐标转换成地理坐标
            DisplayMetrics dm = new DisplayMetrics();
            _activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            Point pointB = new Point(dm.widthPixels,dm.heightPixels);//右下角坐标
            LatLng right = mProjection.fromScreenLocation(pointB);
            cuttentZoom = status.zoom;
            Log.d("wqf","left.longitude"+left.longitude
                    +"---left.latitude::"+left.latitude
                    +"---right.longitude::"+ right.longitude
                    +"---right.latitude::"+right.latitude);
            getNetData(1,left.longitude + "",//经度
                    left.latitude + "",//纬度
                    right.longitude + "",
                    right.latitude + "",
                    cuttentZoom);
//            Projection mProjection = baiduMap.getProjection();//Projection接口用于屏幕像素点坐标系统和地球表面经纬度点坐标系统之间的变换。
//            Point pointB = new Point(0, 0);
//            LatLng tempLatlbg = mProjection.fromScreenLocation(pointB);//将屏幕坐标转换成地理坐标
            // 判断
//            if (leftTopLatlng != null && cuttentZoom == status.zoom) {// TODO: 2016/7/7 0007 changed by weiqingfeng
//                pointB = mProjection.toScreenLocation(leftTopLatlng);
//                if (Math.abs(pointB.x) < changeRange
//                        && Math.abs(pointB.y) < changeRange) {
//                    // 不够范围
//                    return;
//                }
//            }
            // System.out.println("结束变化时间2：：" + new Date());
//            cuttentZoom = status.zoom;
//            System.out.println("cuttentZoom2：：" + cuttentZoom);
//            leftTopLatlng = tempLatlbg;
//            latlng = status.target;
//            mCluster.setmDistance(getDistance(status.zoom));
//            getNetData(status.bound.southwest.longitude + "",//经度
//                    status.bound.northeast.latitude + "",//纬度
//                    status.bound.northeast.longitude + "",
//                    status.bound.southwest.latitude + "",
//                    cuttentZoom);

        }

        @Override
        public void onMapStatusChangeStart(MapStatus arg0) {

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
                    System.out.println("定位到的经纬度为：latlng=" + latlng.toString());
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
        initMap(view);
        return view;
    }

    private void initMap(View view) {

        mMapView = (MapView) view.findViewById(R.id.map);
        baiduMap = mMapView.getMap();
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);//是否显示缩放控件
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);//设置是否允许定位图层
        UiSettings mapui = baiduMap.getUiSettings();//获取地图ui控制器
        mapui.setCompassEnabled(false);//设置是否允许指南针
        mapui.setOverlookingGesturesEnabled(false);//设置是否允许俯视手势
        mapui.setZoomGesturesEnabled(true);//设置是否允许缩放手势
      /*  MapStatus ms = new MapStatus.Builder().overlook(0).zoom(11).build();
        MapStatusUpdate u1 = MapStatusUpdateFactory.newMapStatus(ms);
        baiduMap.setMapStatus(u1);*/
        //MapStatusUpdate描述地图状态将要发生的变化
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(11);//设置地图缩放级别
        baiduMap.setMapStatus(msu);//改变地图状态
        //MapStatusUpdateFactory生成地图状态将要发生的变化
        msu  = MapStatusUpdateFactory
                .newLatLng(new LatLng(FDDataUtils.getDouble(ac.lat), FDDataUtils.getDouble(ac.lon)));//设置地图新中心点
//        LocationUtil.getInstance(_activity).startOneLocation(OneLocationListener);
        baiduMap.setMapStatus(msu);
       /* MyLocationData locData = new MyLocationData.Builder()
                .direction(100).latitude(FDDataUtils.getDouble(ac.lat))
                .longitude(FDDataUtils.getDouble(ac.lon)).build();
        baiduMap.setMyLocationData(locData);*/

        //设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效，参见 setMyLocationEnabled(boolean)
        baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true, null));
        mapView = MapViewFactory.getInstance(_activity);
        mCluster = new Cluster(baiduMap, isAverageCenter, 80, mDistance);
        baiduMap.setOnMapStatusChangeListener(changeListener);//设置地图状态监听者
        baiduMap.setOnMapLoadedCallback(callback);//设置地图加载完成回调
        baiduMap.setMaxAndMinZoomLevel(14,3);
        //设置地图 Marker 覆盖物点击事件监听者,自3.4.0版本起可设置多个监听对象，停止监听时调用removeMarkerClickListener移除监听对象
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                Bundle b = arg0.getExtraInfo();//获取覆盖物额外信息
                int i = b.getInt("id");
                Log.d("wqf","i的值："+i);
                String countyid = b.getString("countyid");
                int count = b.getInt("count");
                Log.d("wqf","count的值："+count);
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
                    Log.d("多个", "onMarkerClick: 存入的id=" + ids);
                    onClickMark(false, p,countyid);
                } else {
                    lon = m.getItemBean().getLon();
                    lat = m.getItemBean().getLat();
                    address = m.getItemBean().getName();
                    Bundle p = new Bundle();
                    p.putString("id", m.getItemBean().getId());
                    Log.d("一个", "onMarkerClick: 存入的id=" + m.getItemBean().getId());
                    onClickMark(true, p, countyid);
//                    changeState(i);
                }
                return false;
            }
        });


        baiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {

            @Override
            public void onTouch(MotionEvent arg0) {
            }
        });
        initPoiSearch();
    }

    public void changeState(int index) {
        for (int i = 0; i < currentClustersForItembean.size(); i++) {
            View itemView = mapView.createDefaultView(i, currentClustersForItembean.get(i).getCount());

            TextView title = (TextView) itemView.findViewById(R.id.title);
            TextView img = (TextView) itemView.findViewById(R.id.ioc_img);
            if (currentClustersForItembean.get(i).getCount() > 1) {
                img.setText(currentClustersForItembean.get(i).getItemBean().getCarrierCount() + "");
                title.setText(currentClustersForItembean.get(i).getItemBean().getName());
               /* number_tx.setText(currentClustersForItembean.get(i).getCount()+ "");
                number_tx.setVisibility(View.VISIBLE);*/
            } else {
              /*  number_tx.setVisibility(View.GONE);*/
            }

            BitmapDescriptor bdA = BitmapDescriptorFactory.fromView(itemView);
            if (markers != null && markers.size() > i)
                markers.get(i).setIcon(bdA);
        }
    }


    /**
     * 请求成功后调用地图装载数据
     */
    private void initMark() {
        mMarkersForItembean.clear();
        currentClustersForItembean.clear();
        markers.clear();
        baiduMap.clear();

        for (int i = 0; i < itembeanList.size(); i++) {
            ItemBean itembean = itembeanList.get(i);
            LatLng latLng = new LatLng(Double.parseDouble(itembean.getLat()),
                    Double.parseDouble(itembean.getLon()));
            Marks marks = new Marks(latLng);
            marks.setName(itembean.getName());
            marks.setId(itembean.getId());
            marks.setItemBean(itembean);
            mMarkersForItembean.add(marks);
        }

        currentClustersForItembean = mCluster.createCluster(mMarkersForItembean);

        for (int i = 0; i < currentClustersForItembean.size(); i++) {
            Marks m = currentClustersForItembean.get(i);

            View mView = mapView.createDefaultView(i, m.getCount());
            TextView title = (TextView) mView.findViewById(R.id.title);
            TextView img = (TextView) mView.findViewById(R.id.ioc_img);

            initMarkView(title, img, currentClustersForItembean.get(i).getItemBean().getName(), Integer.parseInt(currentClustersForItembean.get(i).getItemBean().getCarrierCount()));
            BitmapDescriptor bdA = BitmapDescriptorFactory.fromView(mView);
            OverlayOptions ooA = new MarkerOptions().position(m.getPosition())
                    .icon(bdA).zIndex(9);
            Marker marker = (Marker) (baiduMap.addOverlay(ooA));
            markers.add(marker);
            marker.setTitle(m.getName());
            Bundle b = new Bundle();
            b.putInt("id",i);
            b.putString("countyid", m.getItemBean().getId());
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
        public void getNetData(int flag,String left_top_lon, String left_top_lat, String right_bottom_lon, String right_bottom_lat, float cuttentZoom);

        public void onClickMark(boolean isOne, Bundle b, String countyid);

        public void initMarkView(TextView title, TextView number, String titleStr, int count);

        public void changeMarkView(TextView title, TextView number, String titleStr, int count, boolean select);
    }

    /**
     * 地图搜索
     *
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
    public void location() {
        LocationUtil.getInstance(_activity).startOneLocation(
                OneLocationListener);
    }

    /**
     * 放大地图
     * @param lat
     * @param lng
     */
    public void locationMagnify(int flag,String lat, String lng) {
        UiSettings mapui = baiduMap.getUiSettings();
//        mapui.setAllGesturesEnabled(true);
        mapui.setCompassEnabled(false);//获取是否允许指南针
        mapui.setOverlookingGesturesEnabled(false);//设置是否允许俯视手势
        mapui.setZoomGesturesEnabled(true);//设置是否允许缩放手势
        mapui.setScrollGesturesEnabled(true);//设置是否允许拖拽手势
//        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14);//设置地图缩放级别
//        baiduMap.setMapStatus(msu);
//        msu  = MapStatusUpdateFactory.newLatLng(new LatLng(FDDataUtils.getDouble(lat), FDDataUtils.getDouble(lng)));
//        baiduMap.setMapStatus(msu);
//        baiduMap.setOnMapStatusChangeListener(changeListener);
//        baiduMap.setOnMapLoadedCallback(callback);
        MapStatusUpdate msu;
        if(flag==0) {
            msu = MapStatusUpdateFactory.newLatLngZoom(new LatLng(FDDataUtils.getDouble(lat), FDDataUtils.getDouble(lng)),14);
            baiduMap.setMapStatus(msu);
        }
        msu = MapStatusUpdateFactory.zoomIn();
        baiduMap.setMapStatus(msu);
    }

    /**
     * 装载地图数据
     * @param itembeanList
     */
    public void refreshMap(List<ItemBean> itembeanList) {
        this.itembeanList = itembeanList;
        initMark();
    }

    /**
     * 获取地图数据
     *
     * @param left_top_lon
     * @param left_top_lat
     * @param right_bottom_lon
     * @param right_bottom_lat
     */
    private void getNetData(int flag,String left_top_lon, String left_top_lat, String right_bottom_lon, String right_bottom_lat, float cuttentZoom) {
        if (mMapFragmentListener != null)
            mMapFragmentListener.getNetData(flag,left_top_lon, left_top_lat, right_bottom_lon, right_bottom_lat, cuttentZoom);
    }


    /**
     * 点击地图图标
     *  @param isOne
     * @param b
     * @param countyid
     */
    private void onClickMark(boolean isOne, Bundle b, String countyid) {
        if (mMapFragmentListener != null)
            mMapFragmentListener.onClickMark(isOne, b,countyid);
    }

    /**
     * 初始化显示图标
     *
     * @param title
     * @param number
     * @param titleStr
     * @param count
     */
    public void initMarkView(TextView title, TextView number, String titleStr, int count) {
        if (mMapFragmentListener != null)
            mMapFragmentListener.initMarkView(title, number, titleStr, count);
    }

    /**
     * 改变地图图标显示
     *
     * @param title
     * @param number
     * @param titleStr
     * @param count
     * @param select
     */
    public void changeMarkView(TextView title, TextView number, String titleStr, int count, boolean select) {
        if (mMapFragmentListener != null)
            mMapFragmentListener.changeMarkView(title, number, titleStr, count, select);
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPoiSearch != null)
            mPoiSearch.destroy();
    }


}
