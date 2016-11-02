package com.midian.login.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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
 * 注册第一步
 *
 * @author MIDIAN
 */
public class RegisterOneActivity extends BaseActivity {

    private EditText phone_et, password_et, password_again_et, auth_code_et;
    private CountDownTimer mCountDownTimer;
    private Button code_btn, next_btn;
    private LinearLayout ll_phone, ll_password, ll_assword_again, ll_code;
    private BaseLibTopbarView topbar;
    private String phone, pwd, code;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);
        initTitle();
        view = findView(R.id.view);
        view.setOnClickListener(this);

        next_btn = (Button) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(this);
        code_btn = (Button) findViewById(R.id.code_btn);
        code_btn.setOnClickListener(this);
        phone_et = (EditText) this.findViewById(R.id.phone);
        password_et = (EditText) this.findViewById(R.id.password_et);
        password_again_et = (EditText) this
                .findViewById(R.id.password_again_et);
        auth_code_et = (EditText) this.findViewById(R.id.auth_code_et);
        ll_phone = (LinearLayout) this.findViewById(R.id.ll_phone);
        ll_password = (LinearLayout) this.findViewById(R.id.ll_password);
        ll_assword_again = (LinearLayout) this
                .findViewById(R.id.ll_assword_again);
        ll_code = (LinearLayout) this.findViewById(R.id.ll_code);
        System.out.println(":::::ac.device_token=" +ac.device_token);
    }

    private void initTitle() {
        topbar = (BaseLibTopbarView) findViewById(R.id.topbar);
        topbar.setTitle("注册").setLeftImageButton(R.drawable.icon_back, UIHelper.finish(_activity));
        topbar.setLeftText("返回",UIHelper.finish(_activity));
    }

    @Override
    public void onClick(View v) {
        String type = "1";//必填；1:注册, 2:忘记密码, 3:更换手机, 4:修改密码, 5:更改支付信息
        phone = phone_et.getText().toString().trim();
        pwd = password_et.getText().toString().trim();
        String pwd2 = password_again_et.getText().toString().trim();
        code = auth_code_et.getText().toString().trim();
        super.onClick(v);

        int id = v.getId();
        if (id == R.id.code_btn) {
            if (ValidateTools.isEmptyString(phone)) {
                ObjectAnimatorTools.onVibrationView(ll_phone);
                UIHelper.t(this, R.string.hint_phone_not_empty);
                return;
            }
            if (!ValidateTools.isPhoneNumber(phone)) {
                ObjectAnimatorTools.onVibrationView(ll_phone);
                UIHelper.t(this, R.string.hint_phone_error);
                return;
            }
//            ac.api.getApiClient(LoginApiClient.class).sendCode(phone, type, this);//发送验证码
//            if(ac.api.getApiClient(LoginApiClient.class)==null){
//                Log.d("wqf","空的");
//            }
            ac.api.getApiClient(LoginApiClient.class).sendCode(phone, type, this);//发送验证码
            showLoadingDlg();
            next_btn.setClickable(false);
        } else if (id == R.id.next_btn) {
            if (ValidateTools.isEmptyString(phone)) {
                ObjectAnimatorTools.onVibrationView(ll_phone);
                UIHelper.t(_activity, R.string.hint_phone_not_empty);
                return;
            }
            if (!Func.isMobileNO(phone)) {
                ObjectAnimatorTools.onVibrationView(ll_phone);
                UIHelper.t(_activity, R.string.hint_phone_error);
                return;
            }

            if (ValidateTools.isEmptyString(pwd)) {
                ObjectAnimatorTools.onVibrationView(ll_password);
                UIHelper.t(_activity, R.string.hint_pwd_not_empty);
                return;
            }
            if (pwd.length() < 6 || pwd.length() > 16) {
                ObjectAnimatorTools.onVibrationView(ll_password);
                UIHelper.t(_activity, "密码长度须6到16位");
                return;
            }

            if (!pwd.equals(pwd2)) {
                ObjectAnimatorTools.onVibrationView(ll_assword_again);
                UIHelper.t(_activity, R.string.hint_pwd2);
                return;
            }
            if (ValidateTools.isEmptyString(code)) {
                ObjectAnimatorTools.onVibrationView(ll_code);
                UIHelper.t(_activity, R.string.hint_auth_code_not_empty);
                return;
            }
            ac.api.getApiClient(LoginApiClient.class).validateCode(phone, code, this);// 验证验证码
            showLoadingDlg();
            next_btn.setClickable(false);
        } else if (id == R.id.view) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public void onApiSuccess(NetResult res, String tag) {
        super.onApiSuccess(res, tag);
        hideLoadingDlg();
        next_btn.setClickable(true);
        if (res.isOK()) {
            if ("sendCode".equals(tag)) {
                downTime();
                UIHelper.t(_activity, "发送成功");
            }
            if ("validateCode".equals(tag)) {
                Bundle mBundle = new Bundle();
                mBundle.putString("phone", phone);
                mBundle.putString("pwd", pwd);
                mBundle.putString("code", code);
                UIHelper.jumpForResult(_activity, RegisterTwoActivity.class, mBundle,1001);
                finish();
            }
        } else {
            ac.handleErrorCode(_activity, res.ret_info);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {
        super.onApiFailure(t, errorNo, strMsg, tag);
        System.out.println("onApiFailure");
        hideLoadingDlg();
        next_btn.setClickable(true);

        if ("validate_error".equals(errorNo)) {
            UIHelper.t(_activity, "验证失败");
        }
    }

    private void downTime() {
        mCountDownTimer = new CountDownTimer(59 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timeText = getResources().getString(
                        R.string.hint_time_text);
                code_btn.setClickable(false);
                code_btn
                        .setText(millisUntilFinished / 1000 + timeText);
            }

            @Override
            public void onFinish() {
                code_btn.setClickable(true);
                code_btn.setText(R.string.auth_code);
            }
        };
        mCountDownTimer.start();
    }


}
