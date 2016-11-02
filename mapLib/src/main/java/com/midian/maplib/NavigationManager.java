package com.midian.maplib;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import java.io.File;
import java.net.URISyntaxException;

import midian.baselib.utils.UIHelper;
import midian.baselib.widget.dialog.CommonDialog;

/**
 * 导航管理类
 * Created by MIDIAN on 2015/12/17.
 */
public class NavigationManager {
    Activity mActivity;
    CommonDialog mCommonDialog;// 导航弹框
    String curlat = "", curlon = "", lat = "", lon = "", address = "";

    public NavigationManager(Activity mActivity) {
        this.mActivity = mActivity;
    }

    ;public void setPosition(String fromLat,String fromLon,String toLat,String toLon,String address){
        this.curlat=fromLat;
        this.curlon=fromLon;
        this.lat=toLat;
        this.lon=toLon;
        this.address=address;
    }

    public void showDialog() {
        if(mActivity==null||mActivity.isFinishing())
            return;
        if(mCommonDialog==null)
        mCommonDialog = new CommonDialog(mActivity, R.style.input_bottom_dialog);
        mCommonDialog.setContentID(R.layout.dialog_navigation);
        mCommonDialog.findViewById(R.id.baidu_btn).setOnClickListener(
                mOnClickListener);
        mCommonDialog.findViewById(R.id.gaode_btn).setOnClickListener(
                mOnClickListener);
        mCommonDialog.findViewById(R.id.google_btn).setOnClickListener(
                mOnClickListener);
        mCommonDialog.findViewById(R.id.cancel_btn).setOnClickListener(
                mOnClickListener);
        mCommonDialog.show();
    }

    /**
     * 释放资源
     */
    public void onDestroy(){
        if(mCommonDialog!=null){
            mCommonDialog.dismiss();
            mCommonDialog=null;
        }
        mActivity=null;
    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(mCommonDialog!=null)
            mCommonDialog.dismiss();
            if(mActivity==null|mActivity.isFinishing())
                return;
            if (v.getId() == R.id.baidu_btn) {
                gotoBaiduMap();
            } else if (v.getId() == R.id.gaode_btn) {
                gotoGaodeMap();
            } else if (v.getId() == R.id.google_btn) {
                // gotoGaodeMap();
            } else if (v.getId() == R.id.cancel_btn) {
            }
        }
    };

    public void gotoBaiduMap() {
        try {

            Intent intent = Intent
                    .getIntent("intent://map/direction?origin=latlng:"
                            + curlat
                            + ","
                            + curlon
                            + "|name:我的位置&destination="
                            + lat
                            + ","
                            + lon
                            + "&mode=driving#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            if (isInstallByread("com.baidu.BaiduMap")) {
                mActivity.startActivity(intent); // 启动调用

            } else {
                UIHelper.t(mActivity, "请安装百度地图客户端");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public void gotoGaodeMap() {
        Uri uri = Uri
                .parse("androidamap://route?sourceApplication=com.midian.mimi&slat="
                        + curlat
                        + "&slon="
                        + curlon
                        + "&dlat="
                        + lat
                        + "&dlon="
                        + lon
                        + "&dname="
                        + address
                        + "&dev=0&m=0&t=2&showType=1");

        Intent intent = new Intent("android.intent.action.VIEW", uri);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setPackage("com.autonavi.minimap");
        if (isInstallByread("com.autonavi.minimap")) {
            mActivity.startActivity(intent); // 启动调用
        } else {
            UIHelper.t(mActivity, "请安装高德地图客户端");
        }

    }
}
