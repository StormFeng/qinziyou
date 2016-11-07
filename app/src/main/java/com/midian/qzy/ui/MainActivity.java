package com.midian.qzy.ui;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.makeramen.roundedimageview.RoundedImageView;
import com.midian.login.view.LoginActivity;
import com.midian.qzy.R;
import com.midian.qzy.bean.UserDetailBean;
import com.midian.qzy.ui.home.SearchActivity;
import com.midian.qzy.ui.myactivity.JoinedActicityFragment;
import com.midian.qzy.utils.AppUtil;

import java.io.FileNotFoundException;

import midian.baselib.api.ApiCallback;
import midian.baselib.base.BaseFragmentActivity;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.FDDataUtils;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener, SlidingMenu.OnOpenedListener {
    private BaseLibTopbarView topbar;
    private SlidingMenu menu;
    private HomeFragment homeFragment;//首页
    private MyAccountFragment myAccountFragment;//个人中心
    private MyActivityFragment myActivityFragment;//我的活动
    private MyCommentFragment myCommentFragment;//我的评论
    private InstitutionFragment institutionFragment;//机构加盟
    private NewsFragment newsFragment;//消息列表
    private AboutUsFragment aboutUsFragment;//关于我们
    private SettingFragment settingFragment;//设置
    private RoundedImageView ivHead;
    private TextView tvName;

    long waitTime = 2000;
    long touchTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUtil.getMAppContext(ac).startPush();
        initmenu();
        initFragment();
//        if(Build.VERSION.SDK_INT >= 23){
//            if (ContextCompat.checkSelfPermission(_activity,
//                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                Log.d("wqf","ContextCompat.checkSelfPermission----------NO");
//                if (ActivityCompat.shouldShowRequestPermissionRationale(_activity,
//                        Manifest.permission.CAMERA)) {
////                    UIHelper.t(_activity,"请在权限设置中允许萌主使用相机");
//                } else {
//                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 1002);
//                }
//            }
//        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 1001:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                    UIHelper.t(_activity, "已允许萌主使用相机");
//                } else {
//                    UIHelper.t(_activity, "已禁止萌主读取照片");
//                }
//            case 1002:
//                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
////                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 1001);
//                    ActivityCompat.requestPermissions(_activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
//                }else{
//                    UIHelper.t(_activity,"已禁止萌主使用相机");
//                }
//                break;
//        }
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onApiSuccess(NetResult res, String tag) {
        super.onApiSuccess(res, tag);
        if(res.isOK()){
            UserDetailBean bean= (UserDetailBean) res;
            try {
                ac.setImage(ivHead, FDDataUtils.getImageUrl(bean.getContent().getHead_pic(),100,100));
            } catch (Exception e) {
                e.printStackTrace();
            }
            tvName.setText(bean.getContent().getName());
        }else{
            ac.handleErrorCode(_activity,res.ret_code);
        }
    }

    private void initmenu() {
        menu=new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.leftmenu);
        menu.setOnOpenedListener(this);
        View v = menu.getMenu();
        ivHead = (RoundedImageView) v.findViewById(R.id.iv_Head);
        tvName = (TextView) v.findViewById(R.id.tv_Name);
        v.findViewById(R.id.ll_person).setOnClickListener(this);//个人资料
        v.findViewById(R.id.iv_1).setOnClickListener(this);//我的活动
        v.findViewById(R.id.iv_2).setOnClickListener(this);//我的评论
        v.findViewById(R.id.iv_3).setOnClickListener(this);//机构加盟
        v.findViewById(R.id.iv_4).setOnClickListener(this);//消息列表
        v.findViewById(R.id.iv_5).setOnClickListener(this);//关于我们
        v.findViewById(R.id.iv_6).setOnClickListener(this);//设置
        topbar=findView(R.id.topbar);
        topbar.setLeftImageButton(R.drawable.icon_menu, listener_1);
        topbar.setTitle("萌主");
        topbar.setRightText("筛选", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.jump(_activity, SearchActivity.class);
            }
        });
    }
    private void initFragment() {
        homeFragment=new HomeFragment();
        myActivityFragment = new MyActivityFragment();
        myCommentFragment = new MyCommentFragment();
        myAccountFragment = new MyAccountFragment();
        institutionFragment = new InstitutionFragment();
        newsFragment = new NewsFragment();
        aboutUsFragment = new AboutUsFragment();
        settingFragment = new SettingFragment();
        switchFragment(homeFragment);
    }

    View.OnClickListener listener_1=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menu.showMenu();
        }
    };
    View.OnClickListener listener_2=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switchFragment(homeFragment);
            topbar.setLeftImageButton(R.drawable.icon_menu, listener_1);
            topbar.setTitle("萌主").setLeftText("",null);
            topbar.setRightText("筛选",new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.jump(_activity, SearchActivity.class);
                }
            });
        }
    };

    @Override
    public void switchFragment(Fragment to) {
        super.switchFragment(to);
    }

    @Override
    public int getFragmentContentId() {
        return R.id.fragment;
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        menu.showContent();
        if(id==R.id.ll_person){
            if(!ac.isUserIdExsit()){
                UIHelper.jump(_activity, LoginActivity.class);
                return;
            }
            changeTitle();
            topbar.setTitle("个人资料");
            switchFragment(myAccountFragment);
        }else if(id==R.id.iv_1){
            if(!ac.isUserIdExsit()){
                UIHelper.jump(_activity, LoginActivity.class);
                return;
            }
            changeTitle();
            topbar.setTitle("我的活动");
            switchFragment(myActivityFragment);
            myActivityFragment.onResume();
        }else if(id==R.id.iv_2){
            if(!ac.isUserIdExsit()){
                UIHelper.jump(_activity, LoginActivity.class);
                return;
            }
            changeTitle();
            topbar.setTitle("我的评价");
            switchFragment(myCommentFragment);
        }else if(id==R.id.iv_3){
            changeTitle();
            topbar.setTitle("机构加盟");
            topbar.setRightText("提交", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name=institutionFragment.etName.getText().toString();
                    String contact_person=institutionFragment.etContactPerson.getText().toString();
                    String contact_num=institutionFragment.etContactNum.getText().toString();
                    String contact_address=institutionFragment.etContactAddress.getText().toString();
                    final String remark=institutionFragment.etNote.getText().toString();
                    if("".equals(contact_person)||"".equals(contact_num)||"".equals(contact_address)||"".equals(name)){
                        UIHelper.t(_activity,"请补充加盟信息");
                    }else {
                        try {
                            if(InstitutionFragment.mFile==null){
                                Log.d("wqf","InstitutionFragment.mFile==null");
                            }else{
                                Log.d("wqf","InstitutionFragment.mFile!=null");
                                Log.d("wqf",InstitutionFragment.mFile.getPath());
                            }
                            AppUtil.getPpApiClient(ac).organizationJoinApply(name, null, contact_person, contact_num, contact_address, null, InstitutionFragment.mFile, remark, new ApiCallback() {
                                @Override
                                public void onApiStart(String tag) {
                                    showLoadingDlg();
                                }

                                @Override
                                public void onApiLoading(long count, long current, String tag) {

                                }

                                @Override
                                public void onApiSuccess(NetResult res, String tag) {
                                    hideLoadingDlg();
                                    UIHelper.t(_activity, "您的加盟申请已收到，我们会尽快安排专员联系您!");
                                    switchFragment(homeFragment);
                                    topbar.setLeftImageButton(R.drawable.icon_menu, listener_1);
                                    topbar.setTitle("萌主").setLeftText("", null);
                                    topbar.setRightText("筛选", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            UIHelper.jump(_activity, SearchActivity.class);
                                        }
                                    });
                                }

                                @Override
                                public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {

                                }

                                @Override
                                public void onParseError(String tag) {

                                }
                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            switchFragment(institutionFragment);
        }else if(id==R.id.iv_4){
            if(!ac.isUserIdExsit()){
                UIHelper.jump(_activity, LoginActivity.class);
                return;
            }
            changeTitle();
            topbar.setTitle("消息列表");
            switchFragment(newsFragment);
        }else if(id==R.id.iv_5){
            changeTitle();
            topbar.setTitle("关于我们");
            switchFragment(aboutUsFragment);
        }else if(id==R.id.iv_6){
            changeTitle();
            topbar.setTitle("设置");
            switchFragment(settingFragment);
        }
    }

    private void changeTitle() {
        topbar.setLeftImageButton(R.drawable.icon_back, listener_2);
        topbar.setLeftText("返回",listener_2);
        topbar.getRight_tv().setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (homeFragment.isHidden()) {
                switchFragment(homeFragment);
                topbar.setLeftImageButton(R.drawable.icon_menu, listener_1);
                topbar.setTitle("萌主").setLeftText("", null);
                topbar.setRightText("筛选",new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIHelper.jump(_activity, SearchActivity.class);
                    }
                });
            } else {
                if(event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
                    long currentTime = System.currentTimeMillis();
                    if((currentTime-touchTime)>=waitTime) {
                        UIHelper.t(_activity,"再按一次退出");
                        touchTime = currentTime;
                    }else {
                        finish();
                    }
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onOpened() {
        if(ac.isUserIdExsit()){
            AppUtil.getPpApiClient(ac).userDetail(ac.user_id,this);
//            ac.setImage(ivHead, FDDataUtils.getImageUrl(ac.getProperty("head_pic"),100,100));
//            tvName.setText(ac.getProperty("name"));
            tvName.setTextColor(getResources().getColor(R.color.red));
        }else{
            ivHead.setImageResource(R.drawable.icon_logo);
            tvName.setText("未登录");
            tvName.setTextColor(getResources().getColor(R.color.text_bg90));
        }
    }
}
