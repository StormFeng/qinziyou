package com.midian.qzy.ui.home;

import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.alipay.sdk.app.PayTask;
import com.midian.qzy.R;
import com.midian.qzy.alipay.AliPay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midian.baselib.base.BaseActivity;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

/**
 * 活动报名页面
 */
public class ActivityPay_N extends BaseActivity {
    @BindView(R.id.topbar)
    BaseLibTopbarView topbar;
    @BindView(R.id.btn_RePay)
    Button btnRePay;

    private String sign;
    private String orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_n);
        ButterKnife.bind(this);
        if("AliPay".equals(mBundle.getString("flag"))){
            sign=mBundle.getString("sign");
            orderInfo=mBundle.getString("orderInfo");
            ActivitySignUp.flag=0;
            topbar.setLeftImageButton(R.drawable.icon_back, UIHelper.finish(_activity));
            topbar.setTitle("支付失败").setLeftText("返回", UIHelper.finish(_activity));
        }else{
            topbar.setLeftImageButton(R.drawable.icon_back, listener);
            topbar.setTitle("支付失败").setLeftText("返回", listener);
        }
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_OK);
            finish();
        }
    };

    @OnClick(R.id.btn_RePay)
    public void onClick() {
        if("AliPay".equals(mBundle.getString("flag"))){
            finish();
            new AliPay(_activity).pay(sign,orderInfo);
        }else if("WXPayEntryActivity".equals(mBundle.getString("flag"))){
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResult(RESULT_OK);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
