package com.midian.qzy.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.midian.maplib.MyPostionActivity;
import com.midian.maplib.TestActivity;
import com.midian.qzy.R;
import com.midian.qzy.bean.ActivityDetailBean;
import com.midian.qzy.utils.AppUtil;
import com.midian.qzy.widget.Banner;
import com.midian.qzy.widget.BorderScrollView;
import com.midian.qzy.widget.tagflowlayout.FlowLayout;
import com.midian.qzy.widget.tagflowlayout.TagAdapter;
import com.midian.qzy.widget.tagflowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import midian.baselib.api.ApiCallback;
import midian.baselib.base.BaseFragment;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.FDDataUtils;
import midian.baselib.utils.UIHelper;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
@SuppressLint("ValidFragment")
public class Fragment_ActivityDetai1 extends BaseFragment implements ApiCallback {
    @BindView(R.id.bannerView)
    Banner bannerView;
    @BindView(R.id.tv_Title)
    TextView tvTitle;
    @BindView(R.id.tv_Type)
    TextView tvType;
    @BindView(R.id.tv_Name)
    TextView tvName;
    @BindView(R.id.tag_Age)
    TagFlowLayout tagAge;
    @BindView(R.id.tv_OldPrice)
    TextView tvOldPrice;
    @BindView(R.id.tv_ChildPrice)
    TextView tvChildPrice;
    @BindView(R.id.tv_Distance)
    TextView tvDistance;
    @BindView(R.id.tv_ContactPerson)
    TextView tvContactPerson;
    @BindView(R.id.tv_ContactNum)
    TextView tvContactNum;
    @BindView(R.id.tv_ContactAddress)
    TextView tvContactAddress;
    @BindView(R.id.tv_JoinLimit)
    TextView tvJoinLimit;
    @BindView(R.id.tv_JoinCount)
    TextView tvJoinCount;
    @BindView(R.id.tv_BeginTime)
    TextView tvBeginTime;
    @BindView(R.id.tv_More)
    TextView tvMore;
    //    @BindView(R.id.scrollview)
    public static BorderScrollView scrollview;

    private List<String> images = new ArrayList<>();
    private String activity_id;
    private String OldPrice;
    private String ChildPrice;
    private LocationManager locationManager;
    private Location location;
    private String lat = "", lon = "";
    private String title;
    private String content;
    private VerticalViewPager viewPager;
    private int flag = 0;
    private String phoneNumber="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_detail, null);
//        scrollview= (BorderScrollView) v.findViewById(R.id.scrollview);
        ButterKnife.bind(this, v);
        initView();
        return v;
    }

    public Fragment_ActivityDetai1() {
    }

    public Fragment_ActivityDetai1(String activity_id, VerticalViewPager viewPager) {
        this.activity_id = activity_id;
        this.viewPager = viewPager;
    }

    private void initView() {
        bannerView.setBannerStyle(Banner.CIRCLE_INDICATOR);
        bannerView.setIndicatorGravity(Banner.CENTER);
        bannerView.isAutoPlay(true);
        bannerView.setDelayTime(5000);
        getLocation();
        if (location != null) {
            lat = location.getLatitude() + "";
            lon = location.getLongitude() + "";
        } else {
            Log.d("wqf", "定位为空");
        }

        AppUtil.getPpApiClient(ac).activityDetail(activity_id, ac.user_id, lon, lat, this);
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(_activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(_activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                UIHelper.t(_activity, "请在权限设置中允许萌主使用定位");
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
    public void onApiStart(String tag) {

    }

    @Override
    public void onApiLoading(long count, long current, String tag) {

    }

    @Override
    public void onApiSuccess(NetResult res, String tag) {
        if (res.isOK()) {
            ActivityDetailBean bean = (ActivityDetailBean) res;
            for (int i = 0; i < bean.getContent().getPics().size(); i++) {
                images.add(FDDataUtils.getImageUrl(bean.getContent().getPics().get(i), 800, 1000));
            }
            bannerView.setImages(images.toArray());
            title = bean.getContent().getTitle();
            tvTitle.setText(title);
            content = bean.getContent().getOrganization_name();
            tvName.setText(content);
            tvType.setText(bean.getContent().getType_name());
            OldPrice = bean.getContent().getAdult_price();
            ChildPrice = bean.getContent().getChild_price();
            lon = bean.getContent().getActivity_address_lon();
            lat = bean.getContent().getActivity_address_lat();
            tvOldPrice.setText(OldPrice);
            tvChildPrice.setText(ChildPrice);
            tvContactPerson.setText(bean.getContent().getContact_person());
            tvContactNum.setText(bean.getContent().getContact_num());
            tvContactAddress.setText(bean.getContent().getActivity_address());
            tvJoinLimit.setText(bean.getContent().getJoin_limit_count());
            tvJoinCount.setText(bean.getContent().getJoin_count());
            String begin_time = bean.getContent().getBegin_time();
            tvBeginTime.setText("活动时间：" + begin_time);
            tvDistance.setText(bean.getContent().getDistance());
            String age = bean.getContent().getAges();
            final String[] ages = age.split(",");
            tagAge.setAdapter(new TagAdapter<String>(ages) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(_activity).inflate(R.layout.tag_age_layout, tagAge, false);
                    tv.setText(ages[position]);
                    return tv;
                }
            });
        }
    }

    @Override
    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {

    }

    @Override
    public void onParseError(String tag) {

    }

    @OnClick({R.id.tv_ContactNum, R.id.tv_ContactAddress})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ContactNum:
                phoneNumber = tvContactNum.getText().toString().trim();
                final Dialog dialog = new Dialog(_activity, R.style.add_dialog);
                Window window = dialog.getWindow();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_logout);
                dialog.show();
                TextView tv = (TextView) window.findViewById(R.id.tv_notice);
                Button btnCancel = (Button) window.findViewById(R.id.btn_cancel);
                Button btnOk = (Button) window.findViewById(R.id.btn_ok);
                tv.setText(phoneNumber);
                btnOk.setText("通话");
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String phoneNumber = tvContactNum.getText().toString().trim();
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (ActivityCompat.checkSelfPermission(_activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(_activity, Manifest.permission.CALL_PHONE)) {
                                UIHelper.t(_activity, "请在权限设置中允许萌主拨打电话");
                            } else {
                                ActivityCompat.requestPermissions(_activity, new String[]{Manifest.permission.CALL_PHONE}, 4001);
                            }
                        }else{
                            _activity.startActivity(intent);
                        }
                    }
                });
                break;
            case R.id.tv_ContactAddress:
                Bundle bundle=new Bundle();
                bundle.putString("lat",lat);
                bundle.putString("lon",lon);
                UIHelper.jump(_activity,TestActivity.class,bundle);
                break;
        }
    }
}
