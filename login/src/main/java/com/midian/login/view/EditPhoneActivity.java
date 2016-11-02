package com.midian.login.view;

import midian.baselib.base.BaseActivity;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.midian.login.R;
import com.midian.login.utils.ObjectAnimatorTools;
import com.midian.login.utils.ValidateTools;

/**
 * 验证修改手机号
 *
 * @author chu
 */
public class EditPhoneActivity extends BaseActivity {
    private BaseLibTopbarView topbar;
    private CountDownTimer mCountDownTimer;
    private EditText phone_et, auth_et;
    private Button get_auth_code_btn, next_btn;
    private View phone_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);
        topbar = (BaseLibTopbarView) findViewById(R.id.topbar);
        topbar.setLeftImageButton(R.drawable.icon_back, UIHelper.finish(_activity))
                .setTitle("修改手机号");

        phone_ll = findViewById(R.id.phone_ll);
        phone_et = findView(R.id.phone_et);
        auth_et = findView(R.id.auth_et);
        get_auth_code_btn = findView(R.id.get_auth_code_btn);
        next_btn = findView(R.id.next_btn);

        get_auth_code_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.next_btn) {
            UIHelper.jump(_activity, EditPhoneTwoActivity.class);
            finish();
        } else if (id == R.id.get_auth_code_btn) {
            if (ValidateTools.isEmptyString(phone_et.getText().toString())) {
                ObjectAnimatorTools.onVibrationView(phone_ll);
                UIHelper.t(this, "手机号码不能为空");
                return;
            }
            if (!ValidateTools.isPhoneNumber(phone_et.getText().toString())) {
                ObjectAnimatorTools.onVibrationView(phone_ll);
                UIHelper.t(this, "手机号码格式不正确");
                return;
            }
            // mRegisterOnePresenter.onSendcode(phone_no_et.getText().toString());
            downTime();
        } else {
        }
    }

    private void downTime() {
        mCountDownTimer = new CountDownTimer(59 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timeText = getResources().getString(R.string.hint_time_text);
                get_auth_code_btn.setClickable(false);
                get_auth_code_btn.setText(millisUntilFinished / 1000 + timeText);
            }

            @Override
            public void onFinish() {
                get_auth_code_btn.setClickable(true);
                get_auth_code_btn.setText("验证码");
            }
        };
        mCountDownTimer.start();
    }
}
