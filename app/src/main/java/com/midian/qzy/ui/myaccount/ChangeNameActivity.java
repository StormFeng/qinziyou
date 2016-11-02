package com.midian.qzy.ui.myaccount;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.midian.login.api.LoginApiClient;
import com.midian.login.view.LoginActivity;
import com.midian.qzy.R;
import com.midian.qzy.ui.MainActivity;
import com.midian.qzy.utils.AppUtil;

import java.io.FileNotFoundException;
import java.text.NumberFormat;

import midian.baselib.api.ApiCallback;
import midian.baselib.base.BaseActivity;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

/**
 * Created by Administrator on 2016/7/21 0021.
 * 修改名字/手机号
 */
public class ChangeNameActivity extends BaseActivity implements TextWatcher {
    private BaseLibTopbarView topbar;
    private Button btn_ok;
    private EditText et_name;
    private String name;
    private Dialog dialog;
    private TextView tvNote,tvNotice;
    private EditText etContent;
    private Button btnCancel,btnOk,btnAgain;
    private Dialog failDialog;
    private Dialog validateCodeDialog;
    private String newNumber;
    private CountDownTimer mCountDownTimer;

    private TextView tv_Number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changename);
        topbar=findView(R.id.topbar);
        topbar.setLeftImageButton(R.drawable.icon_back, UIHelper.finish(_activity)).setLeftText("返回",UIHelper.finish(_activity));
        btn_ok=findView(R.id.btn_ok);
        et_name=findView(R.id.et_name);
        et_name.addTextChangedListener(this);
        tv_Number=findView(R.id.tv_Number);
        if("name".equals(mBundle.getString("type"))){
            topbar.setTitle("姓名").setRightText("保存",this);
        }else if("phone".equals(mBundle.getString("type"))){
            et_name.setVisibility(View.GONE);
            tv_Number.setVisibility(View.VISIBLE);
            topbar.setTitle("手机号").setRightText("",null);
//            et_name.setInputType(InputType.TYPE_CLASS_PHONE);
//            et_name.setText(ac.phone);
            tv_Number.setText(ac.phone);
            btn_ok.setVisibility(View.VISIBLE);
            btn_ok.setOnClickListener(listener);
        }
}
    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            name=tv_Number.getText().toString();
            validatePwd();
        }
    };
    private void downTime() {
        mCountDownTimer = new CountDownTimer(59 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timeText = getResources().getString(
                        com.midian.login.R.string.hint_time_text);
                btnAgain.setClickable(false);
                btnAgain.setTextColor(Color.parseColor("#FF909090"));
                btnAgain
                        .setText("重发("+millisUntilFinished / 1000 + timeText+")");
            }

            @Override
            public void onFinish() {
                btnAgain.setClickable(true);
                btnAgain.setTextColor(Color.parseColor("#FF037AFF"));
                btnAgain.setText("重发");
            }
        };
        mCountDownTimer.start();
    }

    private void validatePwd() {
        dialog=new Dialog(_activity, R.style.add_dialog);
        Window window=dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_changenumber);
        dialog.show();
        tvNotice= (TextView) window.findViewById(R.id.tv_notice);
        etContent= (EditText) window.findViewById(R.id.et_content);
        btnCancel= (Button) window.findViewById(R.id.btn_cancel);
        btnOk= (Button) window.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=etContent.getText().toString().trim();
                if("".equals(str)){
                    UIHelper.t(_activity,"请输入原密码");
                    return;
                }else{
                    ac.api.getApiClient(LoginApiClient.class).validatepwd(ac.user_id, str, new ApiCallback() {
                        @Override
                        public void onApiStart(String tag) {

                        }

                        @Override
                        public void onApiLoading(long count, long current, String tag) {

                        }

                        @Override
                        public void onApiSuccess(NetResult res, String tag) {
                            dialog.dismiss();
                            if("error".equals(res.ret)){
                                failDailog();
                            }else if("success".equals(res.ret)){
                                numberDialog();
                            }
                        }

                        @Override
                        public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {
                        }

                        @Override
                        public void onParseError(String tag) {

                        }
                    });
                }
            }
        });
    }

    private void numberDialog() {
        dialog=new Dialog(_activity, R.style.add_dialog);
        Window window=dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_changenumber);
        dialog.show();
        tvNotice= (TextView) window.findViewById(R.id.tv_notice);
        etContent= (EditText) window.findViewById(R.id.et_content);
        btnCancel= (Button) window.findViewById(R.id.btn_cancel);
        btnOk= (Button) window.findViewById(R.id.btn_ok);
        etContent.setInputType(InputType.TYPE_CLASS_NUMBER);
        tvNotice.setText("请输入新的手机号");
        btnOk.setText("发送验证码");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newNumber=etContent.getText().toString().trim();
                if("".equals(newNumber)){
                    UIHelper.t(_activity,"请输入新的手机号");
                    return;
                }
                if(ac.phone.equals(newNumber)){
                    UIHelper.t(_activity,"输入的新手机号与原手机号相同");
                    return;
                }else{
                    //1：注册，2：忘记密码，3：修改手机号
                    ac.api.getApiClient(LoginApiClient.class).sendCode(newNumber, "3", new ApiCallback() {
                        @Override
                        public void onApiStart(String tag) {

                        }

                        @Override
                        public void onApiLoading(long count, long current, String tag) {

                        }

                        @Override
                        public void onApiSuccess(NetResult res, String tag) {
                            if("success".equals(res.ret)){
                                dialog.dismiss();
                                codeDialog();
                            }else{
                                ac.handleErrorCode(_activity,res.ret_info);
                            }
                        }

                        @Override
                        public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {

                        }

                        @Override
                        public void onParseError(String tag) {

                        }
                    });
                }
            }
        });
    }

    private void codeDialog() {
        validateCodeDialog=new Dialog(_activity, R.style.add_dialog);
        Window window=validateCodeDialog.getWindow();
        validateCodeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        validateCodeDialog.setContentView(R.layout.dialog_code);
        validateCodeDialog.show();
        etContent = (EditText) window.findViewById(R.id.et_content);
        btnCancel = (Button) window.findViewById(R.id.btn_cancel);
        btnOk = (Button) window.findViewById(R.id.btn_ok);
        btnAgain = (Button) window.findViewById(R.id.btn_again);
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!"".equals(etContent.getText().toString())){
                    btnOk.setTextColor(Color.parseColor("#FF037AFF"));
                    btnOk.setClickable(true);
                }else{
                    btnOk.setTextColor(Color.parseColor("#FF909090"));
                    btnOk.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        downTime();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCodeDialog.dismiss();
            }
        });
        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ac.api.getApiClient(LoginApiClient.class).sendCode(newNumber, "3", new ApiCallback() {
                    @Override
                    public void onApiStart(String tag) {

                    }

                    @Override
                    public void onApiLoading(long count, long current, String tag) {

                    }

                    @Override
                    public void onApiSuccess(NetResult res, String tag) {
                        if("success".equals(res.ret)){
                            downTime();
                        }
                    }

                    @Override
                    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {

                    }

                    @Override
                    public void onParseError(String tag) {

                    }
                });
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.getPpApiClient(ac).updatePhone(ac.user_id, newNumber, etContent.getText().toString().trim(), new ApiCallback() {
                    @Override
                    public void onApiStart(String tag) {

                    }

                    @Override
                    public void onApiLoading(long count, long current, String tag) {

                    }

                    @Override
                    public void onApiSuccess(NetResult res, String tag) {
                        if("success".equals(res.ret)){
                            validateCodeDialog.dismiss();
                            _activity.finish();
                            UIHelper.t(_activity,res.ret_info);
                        }
                    }

                    @Override
                    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {

                    }

                    @Override
                    public void onParseError(String tag) {

                    }
                });
            }
        });
        btnOk.setClickable(false);
        btnOk.setTextColor(Color.parseColor("#FF909090"));
    }

    private void failDailog() {
        failDialog=new Dialog(_activity, R.style.add_dialog);
        Window window=failDialog.getWindow();
        failDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        failDialog.setContentView(R.layout.dialog_changenumber);
        failDialog.show();
        tvNotice = (TextView) window.findViewById(R.id.tv_notice);
        etContent = (EditText) window.findViewById(R.id.et_content);
        btnCancel = (Button) window.findViewById(R.id.btn_cancel);
        btnOk = (Button) window.findViewById(R.id.btn_ok);
        tvNote = (TextView) window.findViewById(R.id.tv_note);
        tvNotice.setText("验证失败");
        etContent.setVisibility(View.GONE);
        tvNote.setVisibility(View.VISIBLE);
        btnOk.setText("重试");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failDialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failDialog.dismiss();
                validatePwd();
            }
        });
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        name=et_name.getText().toString();
        try {
            AppUtil.getPpApiClient(ac).updateUser(ac.user_id, null, name, null, this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onApiSuccess(NetResult res, String tag) {
        super.onApiSuccess(res, tag);
        if(res.isOK()){
            ac.setProperty("name",name);
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!"".equals(et_name.getText().toString())){
            btn_ok.setBackgroundResource(R.drawable.radius_blue_bg);
            btn_ok.setClickable(true);
        }else{
            btn_ok.setBackgroundResource(R.drawable.radius_gray_bg);
            btn_ok.setClickable(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
