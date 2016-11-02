package com.midian.qzy.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import com.midian.qzy.R;
import com.midian.qzy.ui.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midian.baselib.app.AppManager;
import midian.baselib.base.BaseActivity;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

/**
 * 活动报名页面
 */
public class ActivityPay_Y extends BaseActivity{
    @BindView(R.id.topbar)
    BaseLibTopbarView topbar;
    @BindView(R.id.btn_GoHome)
    Button btnGoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_y);
        ButterKnife.bind(this);
        ActivitySignUp.flag=1;
        ActivityContent.isPay=true;
        topbar.setLeftImageButton(R.drawable.icon_back, listener);
        topbar.setTitle("支付成功").setLeftText("返回", listener);
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            setResult(RESULT_OK);
            AppManager.getAppManager().finishActivity(ActivitySignUp.class);
            finish();
        }
    };

    @OnClick(R.id.btn_GoHome)
    public void onClick() {
        Intent intent = new Intent();
        intent.setClass(_activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        _activity.startActivity(intent);
        _activity.finish();
//        UIHelper.jump(_activity, MainActivity.class);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            setResult(RESULT_OK);
            AppManager.getAppManager().finishActivity(ActivitySignUp.class);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
