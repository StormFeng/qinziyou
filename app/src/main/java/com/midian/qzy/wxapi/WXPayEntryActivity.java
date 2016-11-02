package com.midian.qzy.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.midian.UMengUtils.Constant;
import com.midian.qzy.R;
import com.midian.qzy.ui.MainActivity;
import com.midian.qzy.ui.home.ActivityContent;
import com.midian.qzy.ui.home.ActivitySignUp;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midian.baselib.app.AppManager;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {


    @BindView(R.id.topbar)
    BaseLibTopbarView topbar;
    @BindView(R.id.btn_GoHome)
    Button btnGoHome;
    @BindView(R.id.ll_PayYes)
    LinearLayout llPayYes;
    @BindView(R.id.btn_RePay)
    Button btnRePay;
    @BindView(R.id.ll_PayNo)
    LinearLayout llPayNo;
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payresult);
        ButterKnife.bind(this);
        topbar.setLeftImageButton(R.drawable.icon_back, UIHelper.finish(this));
        topbar.setLeftText("返回",UIHelper.finish(this));
        api = WXAPIFactory.createWXAPI(this, Constant.weixinAppId);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d("wqf", "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                Log.d("wqf","支付成功");
                ActivityContent.isPay=true;
                AppManager.getAppManager().finishActivity(ActivitySignUp.class);
                topbar.setTitle("支付成功");
                llPayNo.setVisibility(View.GONE);
                llPayYes.setVisibility(View.VISIBLE);
            } else {
                Log.d("wqf","支付失败");
                ActivityContent.isPay=false;
                llPayYes.setVisibility(View.GONE);
                llPayNo.setVisibility(View.VISIBLE);
                topbar.setTitle("支付失败");
            }
        }
    }

    @OnClick({R.id.btn_GoHome, R.id.btn_RePay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_GoHome:
                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                this.finish();
                break;
            case R.id.btn_RePay:
                finish();
                break;
        }
    }

//    @Override
//    public void onResp(BaseResp resp) {
//        Log.d("wqf", "onPayFinish, errCode = " + resp.errCode);
//        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
////            AlertDialog.Builder builder = new AlertDialog.Builder(this);
////            builder.setTitle("提示");
////            builder.setMessage(String.valueOf(resp.errCode));
////            builder.show();
//            if (resp.errCode == 0) {
//                UIHelper.jumpForResult(WXPayEntryActivity.this,ActivityPay_Y.class,1002);
//            } else {
//                Bundle bundle=new Bundle();
//                bundle.putString("flag","WXPayEntryActivity");
//                UIHelper.jumpForResult(WXPayEntryActivity.this,ActivityPay_N.class,bundle,1001);
//            }
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==RESULT_OK){
//            switch (requestCode){
//                case 1001://支付失败
//                    ActivitySignUp.flag=0;
//                    finish();
//                    break;
//                case 1002://支付成功
//                    ActivitySignUp.flag=1;
//                    finish();
//                    break;
//            }
//        }
//    }
}
