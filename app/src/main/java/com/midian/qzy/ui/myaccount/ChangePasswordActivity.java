package com.midian.qzy.ui.myaccount;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.midian.login.api.LoginApiClient;
import com.midian.qzy.R;
import com.midian.qzy.utils.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midian.baselib.base.BaseActivity;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

/**
 * Created by Administrator on 2016/7/21 0021.
 * 修改密码
 */
public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.topbar)
    BaseLibTopbarView topbar;
    @BindView(R.id.et_OldPass)
    EditText etOldPass;
    @BindView(R.id.et_NewPass)
    EditText etNewPass;
    @BindView(R.id.et_NewPassAgain)
    EditText etNewPassAgain;
    @BindView(R.id.btn_Ok)
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setLeftImageButton(R.drawable.icon_back, UIHelper.finish(_activity)).setTitle("修改密码").setLeftText("返回", UIHelper.finish(_activity));
    }


    @OnClick(R.id.btn_Ok)
    public void onClick() {
        String oldPass=etOldPass.getText().toString().trim();
        String newPass=etNewPass.getText().toString().trim();
        String newPassAgain=etNewPassAgain.getText().toString().trim();
        if("".equals(etOldPass)||"".equals(etNewPass)){
            UIHelper.t(_activity,"请输入密码");
            return;
        }
        if(newPass.equals(newPassAgain)){
            if(newPass.length()<6){
                UIHelper.t(_activity,"请输入至少六位的密码");
                return;
            }
            ac.api.getApiClient(LoginApiClient.class).updatePwd(ac.user_id,newPass,oldPass,this);
        }else{
            UIHelper.t(_activity,"两次输入的密码不一样");
            return;
        }
    }

    @Override
    public void onApiSuccess(NetResult res, String tag) {
        super.onApiSuccess(res, tag);
        if(res.isOK()){
            UIHelper.t(_activity,res.ret_info);
            finish();
        }else{
            ac.handleErrorCode(_activity,res.ret_info);
        }
    }
}
