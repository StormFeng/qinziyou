package com.midian.login.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.midian.login.R;
import com.midian.login.api.LoginApiClient;
import com.midian.login.bean.AuthRegisterBean;
import com.midian.login.bean.ThirdLoginBean;
import com.midian.login.utils.ObjectAnimatorTools;
import com.midian.login.utils.ValidateTools;

import midian.baselib.base.BaseActivity;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

/**
 * 设置密码
 * Created by chu on 2016/6/28 0028.
 */
public class SettingPwdActivity extends BaseActivity {
    private BaseLibTopbarView topbar;
    private EditText pwd_et, again_et;
    private String uid, thirdname, thirdhead, authType, phone, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pwd);
        uid = mBundle.getString("authid");
        thirdname = mBundle.getString("thirdname");
        thirdhead = mBundle.getString("thirdhead");
        authType = mBundle.getString("authType");
        phone = mBundle.getString("phone");
        code = mBundle.getString("code");
        topbar = findView(R.id.topbar);
        pwd_et = findView(R.id.pwd_et);
        again_et = findView(R.id.again_et);

        topbar.setTitle("设置登录密码");
        topbar.setLeftImageButton(R.drawable.icon_back, UIHelper.finish(_activity));
        topbar.setRightText("确定", this);
        topbar.getRight_tv().setTextColor(getResources().getColor(R.color.blue));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        String pwd = pwd_et.getText().toString().trim();
        String pwd2 = again_et.getText().toString().trim();
        if (id == R.id.right_tv) {
            if (ValidateTools.isEmptyString(pwd)) {
                ObjectAnimatorTools.onVibrationView(pwd_et);
                UIHelper.t(_activity, R.string.hint_pwd_not_empty);
                return;
            }
            if (pwd.length() < 6 || pwd.length() > 16) {
                ObjectAnimatorTools.onVibrationView(again_et);
                UIHelper.t(_activity, "密码长度须6到16位");
                return;
            }

            if (!pwd.equals(pwd2)) {
                ObjectAnimatorTools.onVibrationView(again_et);
                UIHelper.t(_activity, "两次输入的密码不一致");
                return;
            }
            //第三方账号注册
            try {
//                ac.api.getApiClient(LoginApiClient.class).authorizeAuthRegister(phone, pwd, code, ac.device_token, uid, thirdname, thirdhead, authType, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        showLoadingDlg();
    }

    @Override
    public void onApiSuccess(NetResult res, String tag) {
        super.onApiSuccess(res, tag);
        hideLoadingDlg();
        if (res.isOK()) {
            if ("authorizeAuthRegister".equals(tag)) {//第三方账号注册
//                UIHelper.t(_activity, res.message);
                ThirdLoginBean item = (ThirdLoginBean) res;
                //保存用户登陆信息 依次为：用户id、手机、性别、用户名、Token、昵称、头像、登陆角色类型Roletype
                ac.saveUserInfo(item.getContent().getUser_id(),null,null, item.getContent().getName(),item.getContent().getAccess_token(),item.getContent().getHead_pic(),null);
//                ac.saveDeviceToken(item.getData().getBaiduChannelid());
//                System.out.println("登陆后device_token::::" + ac.device_token);//3729802399301311275
//                saveData(_activity, ac.user_type);
                finish();
            }
        } else {
//            ac.handleErrorCode(_activity, res.message);
        }
    }

    /**
     * 保存用户类型
     *
     * @param context
     * @param str
     */
    private void saveData(Context context, String str) {
        SharedPreferences sp = context.getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_type", str);
        editor.commit();
    }

    @Override
    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {
        super.onApiFailure(t, errorNo, strMsg, tag);
        hideLoadingDlg();
    }
}
