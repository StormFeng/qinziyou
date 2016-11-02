package com.midian.qzy.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.midian.login.view.LoginActivity;
import com.midian.qzy.R;
import com.midian.qzy.app.Constant;
import com.midian.qzy.bean.ActivityDetailBean;
import com.midian.qzy.utils.AppUtil;
import com.midian.qzy.widget.BorderScrollView;
import com.midian.qzy.widget.DentistDetailShareDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import midian.baselib.base.BaseFragmentActivity;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

/**
 * 活动详情页面
 * Created by Administrator on 2016/9/1 0001.
 */
public class ActivityContent extends BaseFragmentActivity {


    @BindView(R.id.topbar)
    BaseLibTopbarView topbar;
    @BindView(R.id.viewPager)
    VerticalViewPager viewPager;
    @BindView(R.id.btn_Signup)
    Button btnSignup;

    private String activity_id;
    private String detailUrl;
    private String title;
    private String content;
    private String OldPrice;
    private String ChildPrice;
    private List<Fragment> list=new ArrayList<>();
    private List<String> params=new ArrayList<>();
    private int flag=0;//用于控制viewpager添加次数
    private float x1 = 0, x2 = 0, y1 = 0, y2 = 0;

    public static boolean isPay=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
        activity_id = mBundle.getString("activity_id");
        Log.d("wqf",activity_id+"");
        topbar.setLeftImageButton(R.drawable.icon_back, UIHelper.finish(_activity));
        topbar.setTitle("活动详情").setLeftText("返回", UIHelper.finish(_activity));
        topbar.setRightImageButton(R.drawable.icon_share, listener);
        AppUtil.getPpApiClient(ac).activityDetail(activity_id, ac.user_id, null, null, this);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DentistDetailShareDialog(_activity).show(Constant.BBASEURL + detailUrl, title,
                    content, null);
        }
    };

    @OnClick(R.id.btn_Signup)
    public void onClick() {
        if(!ac.isUserIdExsit()){
            UIHelper.jump(_activity, LoginActivity.class);
            return;
        }
//        String s=btnSignup.getText().toString().trim();
        Bundle bundle=new Bundle();
        bundle.putString("OldPrice",OldPrice);
        bundle.putString("ChildPrice",ChildPrice);
        bundle.putString("activity_id",activity_id);
        bundle.putString("text",btnSignup.getText().toString().trim());
        UIHelper.jumpForResult(_activity,ActivitySignUp.class,bundle,1001);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==RESULT_OK){
//            AppUtil.getPpApiClient(ac).activityDetail(activity_id, ac.user_id, null, null, this);
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("wqf","super.onResume()");
        if(isPay){
            btnSignup.setText("已经报名");
            btnSignup.setBackgroundColor(_activity.getResources().getColor(R.color.text_bg90));
            btnSignup.setClickable(false);
        }
//        AppUtil.getPpApiClient(ac).activityDetail(activity_id, ac.user_id, null, null, this);
    }

    @Override
    public void onApiSuccess(NetResult res, String tag) {
        super.onApiSuccess(res, tag);
        if (res.isOK()) {
            ActivityDetailBean bean = (ActivityDetailBean) res;
            String isJoin=bean.getContent().getIs_join();
            String begin_time = bean.getContent().getBegin_time();
            detailUrl=bean.getContent().getDetail();
            title = bean.getContent().getTitle();
            content = bean.getContent().getOrganization_name();
            OldPrice = bean.getContent().getAdult_price();
            ChildPrice = bean.getContent().getChild_price();
            if ("1".equals(isJoin)) {
                btnSignup.setText("已经报名");
                btnSignup.setBackgroundColor(_activity.getResources().getColor(R.color.text_bg90));
                btnSignup.setClickable(false);
            } else {
                if ("0.0".equals(OldPrice) && "0.0".equals(ChildPrice) || "".equals(OldPrice) && "".equals(ChildPrice)) {
                    btnSignup.setText("免费参加");
                    btnSignup.setBackgroundColor(_activity.getResources().getColor(R.color.blue_qzy));
                    btnSignup.setClickable(true);
                } else {
                    btnSignup.setText("立即报名");
                    btnSignup.setBackgroundColor(_activity.getResources().getColor(R.color.blue_qzy));
                    btnSignup.setClickable(true);
                }
                if (bean.getContent().getJoin_limit_count().equals(bean.getContent().getJoin_count())) {
                    btnSignup.setText("活动名额已满");
                    btnSignup.setBackgroundColor(_activity.getResources().getColor(R.color.text_bg90));
                    btnSignup.setClickable(false);
                }
                // TODO: 2016/8/3 0003  判断时间
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime = df.format(new Date());
            if (Long.valueOf(begin_time.replace("-", "").replace(" ", "").replace(":", ""))
                    < Long.valueOf(nowTime.replace("-", "").replace(" ", "").replace(":", ""))) {
                btnSignup.setText("报名已结束");
                btnSignup.setBackgroundColor(_activity.getResources().getColor(R.color.text_bg90));
                btnSignup.setClickable(false);
            }
            params.add(activity_id);
            params.add(detailUrl);
            if(flag==0){
                flag++;
                list.add(new Fragment_ActivityDetai1(activity_id,viewPager));
                list.add(new Fragment_ActivityDetail2(params,fm));
            }
            viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return list.get(position);
                }

                @Override
                public int getCount() {
                    return list.size();
                }
            });
        }else{
            ac.handleErrorCode(_activity,res.ret_code);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
            y1 = event.getY();
            Log.e("wqf","x1:"+x1+"\n"+"y1:"+y1);
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            x2 = event.getX();
            y2 = event.getY();
            Log.e("wqf","x2:"+x2+"\n"+"y2:"+y2);
            if(flag==1){
                if(y1-y2>50){
                    if(viewPager.getCurrentItem()==0){
                        viewPager.setCurrentItem(1);
                        return false;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
            if(viewPager.getCurrentItem()==1){
                viewPager.setCurrentItem(0);
            } else{
                finish();
            }
        }
        return false;
    }
}
