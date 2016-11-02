package com.midian.login.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.midian.login.R;
import com.midian.login.api.LoginApiClient;
import com.midian.login.utils.ObjectAnimatorTools;
import com.midian.login.utils.ValidateTools;

import midian.baselib.base.BaseActivity;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.Func;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

/**
 * 第三方账号注册绑定页面
 * Created by chu on 2016/6/28 0028.
 */
public class ThirdBindRegisterActivity extends BaseActivity {
    private BaseLibTopbarView topbar;
    private EditText phone_et, code_et;
    private Button code_btn;
    private CountDownTimer mCountDownTimer;
    private String phone, code;
    private String uid, thirdname, thirdhead, authType;
    private TextView hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_bind_register);
        uid = mBundle.getString("authid");
        thirdname = mBundle.getString("thirdname");
        thirdhead = mBundle.getString("thirdhead");
        authType = mBundle.getString("authType");
        topbar = findView(R.id.topbar);
        topbar.setTitle("绑定手机号");
        topbar.setLeftText("取消", UIHelper.finish(_activity));
        topbar.getLeft_tv().setTextColor(getResources().getColor(R.color.blue));
        topbar.setRightText("下一步", this);
        topbar.getRight_tv().setTextColor(getResources().getColor(R.color.blue));
        phone_et = findView(R.id.phone_et);
        code_et = findView(R.id.code_et);
        code_btn = findView(R.id.code_btn);
        hint = findView(R.id.hint);
        code_btn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int codeType = 1;//必填；1:注册, 2:忘记密码, 3:更换手机, 4:修改密码, 5:更改支付信息
        phone = phone_et.getText().toString().trim();
        code = code_et.getText().toString().trim();
        int id = v.getId();
        if (ValidateTools.isEmptyString(phone)) {
            ObjectAnimatorTools.onVibrationView(phone_et);
            UIHelper.t(_activity, "手机号码不能为空");
            return;
        }
        if (id == R.id.code_btn) {//验证码
            if (!Func.isMobileNO(phone)) {
                ObjectAnimatorTools.onVibrationView(phone_et);
                UIHelper.t(_activity, "手机号码格式不正确");
                return;
            }
//            ac.api.getApiClient(LoginApiClient.class).sendCode(phone, codeType, this);//发送验证码
            downTime();
        } else if (id == R.id.left_tv) {
            finish();
        } else if (id == R.id.right_tv) {
            if ("下一步".equals(topbar.getRight_tv().getText().toString().trim())) {
                if (ValidateTools.isEmptyString(code)) {
                    ObjectAnimatorTools.onVibrationView(code_et);
                    UIHelper.t(_activity, "验证码不能为空");
                    return;
                }
//                ac.api.getApiClient(LoginApiClient.class).authorizeVerifyAccount(phone, this);//验证账号
            } else if ("绑定".equals(topbar.getRight_tv().getText().toString().trim())) {
                try {
//                    ac.api.getApiClient(LoginApiClient.class).memberAuthorizeAuthBind(phone, code, ac.device_token, uid, thirdname, thirdhead, authType, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        Bundle bundle = new Bundle();
        if ("sendCode".equals(tag)) {
//            if (res.isOK()) {
//                UIHelper.t(_activity, res.message);
//            } else {
//                ac.handleErrorCode(_activity, res.message);
//            }
        }
        if (tag.equals("authorizeVerifyAccount")) {//验证账号回调
            if (res.isOK()) {
                //没有返回account_had_exist，说明没有注册过此手机号，则走第三方注册接口
                bundle.putString("authid", uid);
                bundle.putString("thirdname", thirdname);
                bundle.putString("thirdhead", thirdhead);
                bundle.putString("authType", authType);
                bundle.putString("phone", phone);
                bundle.putString("code", code);
                UIHelper.jump(_activity, SettingPwdActivity.class, bundle);
            } else if (res.ret_code.equals("account_had_exist")) {
                //返回account_had_exist，说明此手机号已在平台 注册过，调绑定第三方接口
                topbar.setRightText("绑定", this);
                hint.setVisibility(View.VISIBLE);
            }
        }

        if ("memberAuthorizeAuthBind".equals(tag)) {//绑定手机号成功回调
            if (res.isOK()) {
                UIHelper.t(_activity, res.ret);
//                MemberAuthorizeAuthBindBean item = (MemberAuthorizeAuthBindBean) res;
//                //保存用户登陆信息 依次为：用户id、手机、性别、用户名、Token、昵称、头像、登陆角色类型Roletype
//                ac.saveUserInfo(item.getData().getMemberid(), item.getData().getMobilephone(), item.getData().getSex()
//                        , item.getData().getUsername(), item.getData().getAccessToken(), item.getData().getNickname()
//                        , item.getData().getPortrait(), item.getData().getRoletype(), item.getData().getRoletype());
                System.out.println("登陆后device_token::::" + ac.device_token);//3729802399301311275
                saveData(_activity, ac.user_type);
                finish();
            } else {
                ac.handleErrorCode(_activity, res.ret);
            }
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

    private void downTime() {
        mCountDownTimer = new CountDownTimer(59 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timeText = getResources().getString(R.string.hint_time_text);
                code_btn.setClickable(false);
                code_btn.setText(millisUntilFinished / 1000 + timeText);
            }

            @Override
            public void onFinish() {
                code_btn.setClickable(true);
                code_btn.setText("发送验证码");
            }
        };
        mCountDownTimer.start();
    }
}
