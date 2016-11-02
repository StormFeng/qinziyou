package com.midian.qzy.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import midian.baselib.utils.UIHelper;

/**
 * Created by Administrator on 2016/8/5 0005.
 */
public class MyLocation {

    private LocationManager locationManager;
    private String locationProvider;
    private Activity context;

    public MyLocation(Activity context) {
        this.context = context;
    }


}
