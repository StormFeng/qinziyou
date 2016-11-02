package com.midian.qzy.ui;

import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.midian.login.view.LoginActivity;
import com.midian.qzy.R;
import com.midian.qzy.utils.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midian.baselib.api.ApiCallback;
import midian.baselib.base.BaseFragment;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.UIHelper;

import static android.app.Activity.RESULT_OK;

/**
 * 设置
 * Created by Administrator on 2016/7/18 0018.
 */
public class SettingFragment extends BaseFragment implements ApiCallback {

    @BindView(R.id.btn_Switch)
    SwitchButton btnSwitch;
    @BindView(R.id.et_Content)
    EditText etContent;
    @BindView(R.id.btn_Commit)
    Button btnCommit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, null);
        ButterKnife.bind(this, v);
        return v;
    }


    @OnClick({R.id.btn_Switch,  R.id.et_Content, R.id.btn_Commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Switch:
                //1：接收  2：不接收
                if(!ac.isUserIdExsit()){
                    UIHelper.jump(_activity,LoginActivity.class);
                    return;
                }
                if(btnSwitch.isChecked()){
                    AppUtil.getPpApiClient(ac).updateReceiveStatus(ac.user_id,"2",this);
                }else{
                    AppUtil.getPpApiClient(ac).updateReceiveStatus(ac.user_id,"1",this);
                }
                break;
            case R.id.et_Content:
                break;
            case R.id.btn_Commit:
                String content=etContent.getText().toString().trim();
                if("".equals(content)){
                    UIHelper.t(_activity,"请输入您的反馈意见");
                    return;
                }
                AppUtil.getPpApiClient(ac).advice(ac.user_id,content,this);
                break;
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
        if(res.isOK()){
            if("advice".equals(tag)){
                etContent.setText("");
                UIHelper.t(_activity,"反馈成功！");
            }
            if("updateReceiveStatus".equals(tag)){
                if(btnSwitch.isChecked()){
                    UIHelper.t(_activity,"允许接收系统推送消息");
                }else {
                    UIHelper.t(_activity,"拒绝接收系统推送消息");
                }
            }
        }else{
            ac.handleErrorCode(_activity,res.ret_code);
        }
    }

    @Override
    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {

    }

    @Override
    public void onParseError(String tag) {

    }
}
