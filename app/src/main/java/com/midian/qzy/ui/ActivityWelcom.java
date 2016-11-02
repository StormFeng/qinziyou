package com.midian.qzy.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.midian.qzy.R;
import com.midian.qzy.bean.AdvertiseBean;
import com.midian.qzy.ui.home.ActivityDetail;
import com.midian.qzy.utils.AppUtil;

import java.util.Timer;
import java.util.TimerTask;

import midian.baselib.api.ApiCallback;
import midian.baselib.app.AppManager;
import midian.baselib.base.BaseActivity;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.FDDataUtils;
import midian.baselib.utils.UIHelper;

public class ActivityWelcom extends BaseActivity implements OnClickListener {
    private Button button;
    private ImageView iv;
    private String url;
    Timer timer;
    TimerTask timerTask;

    boolean isStart=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv = (ImageView) findViewById(R.id.imageView1);
        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(this);
        AppUtil.getPpApiClient(ac).getAdvertisePic(ac.getClientKey(),apiCallback);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1);
        alphaAnimation.setDuration(1000);
        iv.setAnimation(alphaAnimation);
        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                inApp();
            }
        };
        timer.schedule(timerTask,1000*5);
    }

    OnClickListener listener=new OnClickListener() {
        @Override
        public void onClick(View v) {
            isStart=true;
            timer.cancel();
            Bundle bundle=new Bundle();
            bundle.putString("url",url);
            UIHelper.jump(_activity, ActivityDetail.class,bundle);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(isStart){
            inApp();
        }
    }

    ApiCallback apiCallback=new ApiCallback() {
        @Override
        public void onApiStart(String tag) {

        }

        @Override
        public void onApiLoading(long count, long current, String tag) {

        }

        @Override
        public void onApiSuccess(NetResult res, String tag) {
            if(res.isOK()){
                AdvertiseBean bean = (AdvertiseBean) res;
                ac.setImage(iv, FDDataUtils.getImageUrl(bean.getContent().getAdvertise_pic_id(),500,500));
                url=bean.getContent().getAdvertise_url();
                iv.setOnClickListener(listener);
            }else{
                iv.setImageResource(R.drawable.icon_wel_no);
            }
        }

        @Override
        public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {

        }

        @Override
        public void onParseError(String tag) {

        }
    };


    @Override
    public void onClick(View v) {
        super.onClick(v);
        v.setEnabled(false);
        timer.cancel();
        UIHelper.jump(ActivityWelcom.this, MainActivity.class);
        ac.setBoolean("app", true);
        finish();
    }

    private  void inApp(){
        if (ac.getBoolean("app")) {
            UIHelper.jump(ActivityWelcom.this, MainActivity.class);
        } else {
            ac.setBoolean("app", true);
            UIHelper.jump(ActivityWelcom.this, ActivityGuide.class);
        }
        finish();
    }
}
