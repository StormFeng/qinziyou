package com.midian.qzy.ui.myaccount;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.midian.qzy.utils.AppUtil;

import java.io.File;
import java.io.FileNotFoundException;

import midian.baselib.base.BaseActivity;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

//修改性别
public class ChooseSexActivity extends BaseActivity {

    private BaseLibTopbarView topbar;
    private TextView sex_man, sex_girl;
    private String sex;
    private ImageView iv_man,iv_women;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.midian.login.R.layout.activity_choose_sex);
        topbar = findView(com.midian.login.R.id.topbar);
        topbar.setTitle("性别").setLeftImageButton(com.midian.login.R.drawable.icon_back, UIHelper.finish(_activity));
        sex_man = findView(com.midian.login.R.id.sex_man);
        sex_girl = findView(com.midian.login.R.id.sex_girl);
        iv_man=findView(com.midian.login.R.id.iv_man);
        iv_women=findView(com.midian.login.R.id.iv_women);
        if("1".equals(ac.getProperty("sex"))){
            iv_man.setVisibility(View.VISIBLE);
        }else{
            iv_women.setVisibility(View.VISIBLE);
        }
        sex_man.setOnClickListener(this);
        sex_girl.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == com.midian.login.R.id.sex_man) {
            iv_women.setVisibility(View.GONE);
            iv_man.setVisibility(View.VISIBLE);
            sex = "1";
        } else if (id == com.midian.login.R.id.sex_girl) {
            iv_women.setVisibility(View.VISIBLE);
            iv_man.setVisibility(View.GONE);
            sex = "2";
        }
        File file=null;
        try {
            AppUtil.getPpApiClient(ac).updateUser(ac.user_id, null, null, sex, this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
            UIHelper.t(_activity, res.ret_info);
            // 把获取到的输入内容返回前一页
            ac.setProperty("sex",sex);
            Bundle bundle = new Bundle();
            bundle.putString("sex", sex);
            setResult(RESULT_OK, bundle);
        }
    }
}
