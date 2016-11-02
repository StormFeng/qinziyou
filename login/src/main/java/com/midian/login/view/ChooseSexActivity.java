package com.midian.login.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.midian.login.R;
import com.midian.login.api.LoginApiClient;

import java.io.FileNotFoundException;

import midian.baselib.base.BaseActivity;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

public class ChooseSexActivity extends BaseActivity {

    private BaseLibTopbarView topbar;
    private TextView sex_man, sex_girl;
    private String title, sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sex);
        topbar = findView(R.id.topbar);
        Bundle mBundle = getIntent().getExtras();
        title = mBundle.getString("title");
        topbar.setTitle(title).setLeftImageButton(R.drawable.icon_back, UIHelper.finish(_activity));

        sex_man = findView(R.id.sex_man);
        sex_girl = findView(R.id.sex_girl);
        sex_man.setOnClickListener(this);
        sex_girl.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.sex_man) {
            sex = "1";

        } else if (id == R.id.sex_girl) {
            sex = "2";

        } else {
        }
       /* try {
            ac.api.getApiClient(LoginApiClient.class).updateUser(null, null, sex, null, null, this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
    }


    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        showLoadingDlg();
        topbar.showProgressBar();
    }

    @Override
    public void onApiSuccess(NetResult res, String tag) {
        super.onApiSuccess(res, tag);
        hideLoadingDlg();
        topbar.hideProgressBar();
        if (res.isOK()) {
            UIHelper.t(_activity, "修改成功!");
            // 把获取到的输入内容返回前一页
            Bundle bundle = new Bundle();
            bundle.putString("sex", sex);
            setResult(RESULT_OK, bundle);
            finish();

        }
    }
}
