package com.midian.login.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midian.login.R;
import com.midian.login.api.LoginApiClient;
import com.midian.login.bean.RegisterBean;
import com.midian.login.utils.Constant;
import com.midian.login.utils.ObjectAnimatorTools;

import java.io.File;
import java.io.FileNotFoundException;

import midian.baselib.base.BasePicActivity;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;
import midian.baselib.widget.CircleImageView;
import tmp.MainActivity;

/**
 * 注册第二步
 *
 * @author MIDIAN
 */
public class RegisterTwoActivity extends BasePicActivity {

//    private CheckBox man_tv, woman_tv;
    private EditText nick_name_et;
    private CircleImageView head_cv;
    private int sex ;
    private Button next_btn;
    private File mhead = null;
    private BaseLibTopbarView topbar;
    private String phone,pwd,code;
    private LinearLayout ll_name;

//    private Button btn_select;
    private TextView tv_url;
    private int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);
        phone = mBundle.getString("phone");
        pwd = mBundle.getString("pwd");
        code = mBundle.getString("code");
        head_cv = findView(R.id.head_cv);
//        man_tv = findView(R.id.man_tv);
//        woman_tv = findView(R.id.woman_tv);
//        woman_tv.setOnClickListener(this);
//        man_tv.setOnClickListener(this);
        nick_name_et = findView(R.id.nick_name_et);
        ll_name=findView(R.id.ll_name);
        next_btn = findView(R.id.next_btn);
//        btn_select=findView(R.id.btn_select);
//        tv_url=findView(R.id.tv_url);
//        btn_select.setOnClickListener(this);
//        tv_url.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        head_cv.setOnClickListener(this);
//        man_tv.setChecked(true);
        next_btn.setClickable(true);
        sex = 1;
        initTitle();
    }

    private void initTitle() {
        topbar = (BaseLibTopbarView) findViewById(R.id.topbar);
        topbar.setTitle(R.string.register).setLeftText(R.string.back, UIHelper.finish(_activity)).setLeftImageButton(R.drawable.icon_back,UIHelper.finish(_activity));
    }

    @Override
    public void onClick(View v) {
        String name = nick_name_et.getText().toString().trim();
        String birthday=null;
        int id = v.getId();
        if (id == R.id.next_btn) {
            if (TextUtils.isEmpty(name)) {
                UIHelper.t(_activity,"昵称不能为空!");
                ObjectAnimatorTools.onVibrationView(ll_name);
                return;
            }
//            if (mhead==null||mhead.equals("")) {
//                UIHelper.t(_activity,"头像不能为空!");
//                return;
//            }
            try {
                Log.d("_+_+_+_+_+_+_+", "onClick: 注册时mhead="+mhead);
                ac.api.getApiClient(LoginApiClient.class).register(phone, pwd, code, mhead, name,sex+"", birthday,this);
//                ac.api.getApiClient(LoginApiClient.class).register(phone, pwd, code, name, ac.device_token, sex, mhead, this);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.head_cv) {
            takePhoto();
        }
//        else if (id == R.id.man_tv) {
//            man_tv.setChecked(true);
//            woman_tv.setChecked(false);
//            sex = 1;
//        } else if (id == R.id.woman_tv) {
//            sex = 0;
//            woman_tv.setChecked(true);
//            man_tv.setChecked(false);
//        }
//        else if(id == R.id.btn_select){
//            if(flag==0){
//                flag=1;
//                btn_select.setBackgroundResource(R.drawable.icon_checked);
//                next_btn.setBackgroundResource(R.drawable.radius_bg_light_bluesolid);
//                next_btn.setClickable(true);
//            }else{
//                flag=0;
//                btn_select.setBackgroundResource(R.drawable.icon_uncheck);
//                next_btn.setBackgroundResource(R.drawable.radius_bg_light_graysolid);
//                next_btn.setClickable(false);
//            }
//        }
//        else if(id == R.id.tv_url){
//            Bundle bundle=new Bundle();
//            bundle.putString("url","RegisterTwoActivity");
//            UIHelper.jump(_activity,AgreementActivity.class,bundle);
//        }
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        showLoadingDlg();
        next_btn.setClickable(false);
    }

    @Override
    public void onApiSuccess(NetResult res, String tag) {
        super.onApiSuccess(res, tag);
        hideLoadingDlg();
        next_btn.setClickable(true);
        if (res.isOK()) {
            if ("register".equals(tag)) {
                RegisterBean item = (RegisterBean) res;
                UIHelper.t(_activity, "注册成功");
                ac.saveUserInfo(item.getContent().getUser_id(),item.getContent().getPhone(),item.getContent().getSex(),
                        item.getContent().getName(),item.getContent().getAccess_token(),"",item.getContent().getUser_from());
                setResult(RESULT_OK);
                finish();
            }
        } else {
            ac.handleErrorCode(_activity, res.ret_info);
        }
    }

    @Override
    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {
        super.onApiFailure(t, errorNo, strMsg, tag);
        hideLoadingDlg();
        next_btn.setClickable(true);
    }

    @Override
    public void outputBitmap(Bitmap bitmap, String path) {
        super.outputBitmap(bitmap, path);
        head_cv.setImageBitmap(bitmap);
        mhead = new File(path);
    }
}