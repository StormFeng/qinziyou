package com.midian.login.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.midian.UMengUtils.UMengLoginUtil;
import com.midian.login.R;
import com.midian.login.api.LoginApiClient;
import com.midian.login.bean.LoginBean;
import com.midian.login.utils.ObjectAnimatorTools;
import com.midian.login.utils.ValidateTools;
import com.umeng.socialize.bean.SHARE_MEDIA;

import midian.baselib.app.AppManager;
import midian.baselib.base.BaseActivity;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

/**
 * 登陆
 */
public class LoginActivity extends BaseActivity {
    EditText phone_et, password_et;
    private Button login_btn;
    private LinearLayout ll_phone, ll_password;
    private float exitTime;
    private BaseLibTopbarView topbar;
    private View view;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        view = findView(R.id.view);
        login_btn = (Button) findViewById(R.id.login_btn);
        ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
        ll_password = (LinearLayout) findViewById(R.id.ll_password);

        view.setOnClickListener(this);
        login_btn.setOnClickListener(this);
//        findViewById(R.id.qq).setOnClickListener(this);
        findViewById(R.id.weixin).setOnClickListener(this);
        findViewById(R.id.forget_password_tv).setOnClickListener(this);
//        findViewById(R.id.register_btn).setOnClickListener(this);
        phone_et = (EditText) findViewById(R.id.phone_et);
        password_et = (EditText) findViewById(R.id.password_et);

        String phone = ac.getProperty("account");
        if (!TextUtils.isEmpty(phone)) {
            phone_et.setText(phone);
            phone_et.setSelection(phone.length());
        }
        initTitle();
        if (ac.isAccess()) {
            finish();
        }
    }


    private void initTitle() {
        topbar = (BaseLibTopbarView) findViewById(R.id.topbar);
        try {
            topbar.setLeftImageButton(R.drawable.icon_back, UIHelper.finish(_activity)).
                    setLeftText("返回",UIHelper.finish(_activity)).setTitle("登录").
                    setRightText("注册", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UIHelper.jumpForResult(_activity,RegisterOneActivity.class,1001);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("wqf",resultCode+"");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        try {
            String phone = phone_et.getText().toString().trim();
            String pwd = password_et.getText().toString().trim();

            int id = v.getId();
            if (id == R.id.login_btn) {
                if (ValidateTools.isEmptyString(phone)) {
                    ObjectAnimatorTools.onVibrationView(ll_phone);
                    UIHelper.t(this, R.string.hint_account_not_empty);
                    return;
                }
                if (!ValidateTools.isPhoneNumber(phone)) {
                    ObjectAnimatorTools.onVibrationView(ll_phone);
                    UIHelper.t(this, R.string.hint_account_error);
                    return;
                }
                if (ValidateTools.isEmptyString(pwd)) {
                    ObjectAnimatorTools.onVibrationView(ll_password);
                    UIHelper.t(this, R.string.hint_pwd_not_empty);
                    return;
                }
                showLoadingDlg();
                login_btn.setClickable(false);
                ac.saveAccount(phone);// 保存账号
                ac.savePassword(pwd);// 保存密码
                ac.api.getApiClient(LoginApiClient.class).login(phone, pwd, this);
//                ac.api.getApiClient(LoginApiClient.class).login(phone, pwd, ac.getProperty("device_token"), this);
            } else if (id == R.id.forget_password_tv) {
                UIHelper.jump(_activity, ForgetPasswordOneActivity.class);
            }
//            else if (id == R.id.qq) {// qq登录
//                qq();
//            }
            else if (id == R.id.weixin) {// weixin 登录
                weixin();
            } else if (id == R.id.view) {//取消输入框焦点
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
//            else if (id == R.id.weibo) {//微博登录
//                weibo();
//            } else if (id == R.id.register_btn) {
//                UIHelper.jump(_activity, RegisterOneActivity.class);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void weibo() {
        UMengLoginUtil.getInstance(_activity).setDataListener(mDataListener);
        UMengLoginUtil.getInstance(_activity).login(SHARE_MEDIA.SINA);
    }

    private void weixin() {
        UMengLoginUtil.getInstance(_activity).setDataListener(mDataListener);
        UMengLoginUtil.getInstance(_activity).login(SHARE_MEDIA.WEIXIN);
    }

    private void qq() {
        UMengLoginUtil.getInstance(_activity).setDataListener(mDataListener);
        UMengLoginUtil.getInstance(_activity).login(SHARE_MEDIA.QQ);
    }

    UMengLoginUtil.DataListener mDataListener = new UMengLoginUtil.DataListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            Log.d("wqf","onStart");
        }

        @Override
        public void onSuccess(SHARE_MEDIA platform, String uid, String nickName, String headurl,String sex) {
            Log.d("wqf","onSuccess");
            thirdLogin(uid, nickName, headurl, getTag(platform),sex);

        }

        @Override
        public void onFail(SHARE_MEDIA platform) {
            Log.d("wqf","onFail");
        }
    };

    private void thirdLogin(String uid, String nickName, String headurl, String tag, String sex) {
        this.uid = uid;
        this.thirdname = nickName;
        this.thirdhead = headurl;
        this.authType = tag;
        ac.api.getApiClient(LoginApiClient.class).authLogin(uid,nickName,headurl,tag,sex,this);

    }

    private String getTag(SHARE_MEDIA platform) {

        if (platform == SHARE_MEDIA.WEIXIN) {
            tag = "2";
        } else if (platform == SHARE_MEDIA.QQ) {
            tag = "3";
        } else if (platform == SHARE_MEDIA.SINA) {
            tag = "4";
        }
        return tag;
    }


    String is_receive = "";

//    /**
//     * 保存用户类型
//     *
//     * @param context
//     * @param str
//     */
//    private void saveData(Context context, String str) {
//        SharedPreferences sp = context.getSharedPreferences("user_info", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("user_type", str);
//        editor.commit();
//    }

    private String uid, thirdname, thirdhead, authType;
    private LoginBean bean;

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        showLoadingDlg();
    }

    @Override
    public void onApiSuccess(NetResult res, String tag) {
        super.onApiSuccess(res, tag);
        login_btn.setClickable(true);
        Bundle bundle = new Bundle();
        if (res.isOK()) {
            hideLoadingDlg();
            if (tag.equals("login")) {//登录成功回调
                UIHelper.t(_activity, "登陆成功");//登陆成功
                ac.setProperty("account", phone_et.getText().toString().trim());//保存用户登陆过的手机号
                LoginBean item = (LoginBean) res;
                this.bean = item;
                //保存用户登陆信息 依次为：用户id、手机、性别、用户名、Token、头像、登陆类型
//                ac.saveUserInfo(item.getData().getMemberid(), item.getData().getMobilephone(), item.getData().getSex()
//                        , item.getData().getUsername(), item.getData().getAccessToken(), item.getData().getNickname()
//                        , item.getData().getPortrait(), item.getData().getRoletype(), item.getData().getRoletype());
                ac.saveUserInfo(item.getContent().getUser_id(),item.getContent().getPhone(),item.getContent().getSex(),
                        item.getContent().getName(),item.getContent().getAccess_token(),item.getContent().getHead_pic(),item.getContent().getUser_from());
                System.out.println("登陆后device_token::::" + ac.device_token);//3729802399301311275
                setResult(RESULT_OK);
                finish();
            }
            if (tag.equals("authLogin")) {//第三方登录成功回调
                UIHelper.t(_activity,  "登陆成功");
                LoginBean item = (LoginBean) res;
                this.bean = item;
                //保存用户登陆信息 依次为：用户id、手机、性别、用户名、Token、头像、登陆类型
                ac.saveUserInfo(item.getContent().getUser_id(),null,null, item.getContent().getName(),
                        item.getContent().getAccess_token(),item.getContent().getHead_pic(),item.getContent().getUser_from());
                System.out.println("登陆后device_token::::" + ac.device_token);//3729802399301311275
//                saveData(_activity, ac.user_type);
                finish();
            }

        }else{
            hideLoadingDlg();
            ac.handleErrorCode(_activity,res.ret_info);
        }
//        else {
//            hideLoadingDlg();
//            //第三方登录返回account_not_auth时，说明没有授权，到下一步进行新手机号注册或绑定验证
//            ac.handleErrorCode(_activity, res.ret);
//            if (res.ret_code.equals("account_not_auth")) {//account_not_auth 第三方登录返回没有授权标识
//                Log.d("onApiSuccess333", "onApiSuccess: 第三方登录错误码=" + res.ret_code + "thirdname=" + thirdname);
//                bundle.putString("authid", uid);
//                bundle.putString("thirdname", thirdname);
//                bundle.putString("thirdhead", thirdhead);
//                bundle.putString("authType", authType);
//                UIHelper.jump(_activity, ThirdBindRegisterActivity.class, bundle);
//            }
//        }
    }

//     @Override
//     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//         super.onActivityResult(requestCode, resultCode, data);
//         UMengLoginUtil.getInstance(this).onActivityResult(requestCode, resultCode, data);
//         if (resultCode != RESULT_OK) {
//            hideLoadingDlg();
//         }
//     }



    @Override
    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {
        super.onApiFailure(t, errorNo, strMsg, tag);

        hideLoadingDlg();
        login_btn.setClickable(true);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exit();
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

//    public void exit() {
//        if ((System.currentTimeMillis() - exitTime) > 2000) {
//            UIHelper.t(getApplicationContext(), R.string.exit_text);
//            exitTime = System.currentTimeMillis();
//        } else {
//            AppManager.getAppManager().appExit(this);
//            finish();
//        }
//    }

}
