package com.midian.login.api;

import com.midian.fastdevelop.afinal.http.AjaxParams;
import com.midian.login.bean.LoginBean;
import com.midian.login.bean.RegisterBean;
import com.midian.maplib.LoginConstant;

import java.io.File;
import java.io.FileNotFoundException;

import midian.baselib.api.ApiCallback;
import midian.baselib.api.BaseApiClient;
import midian.baselib.app.AppContext;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.Md5Utils;

/**
 * 网络请求客户端基类
 *
 * @author moshouguan
 */
public class LoginApiClient extends BaseApiClient {

    public LoginApiClient(AppContext api) {
        super(api);
    }

    /**
     * 1.1.注册
     */
    public void register(String account, String password, String code, File head, String name,
                         String sex, String birthday, ApiCallback callback) throws FileNotFoundException {
        AjaxParams params = new AjaxParams();
        params.setHasFile(true);
        params.put("client_key", ac.getClientKey());
        params.put("account", account);
        params.put("password", Md5Utils.md5(password));
        params.put("code", code);
        if(head!=null){
            params.put("head", head);
        }
        params.put("name", name);
        params.put("sex", sex);
        params.put("birthday", birthday);
        params.put("device_token", ac.device_token);

        post(callback, LoginConstant.MEMBER_AUTHORIZE_REGISTER, params, RegisterBean.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 1.2登录
     */
    public void login(String account, String password, ApiCallback callback) {
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("account", account);
        params.put("password", Md5Utils.md5(password));
        params.put("device_token", ac.device_token);
        post(callback, LoginConstant.MEMBER_AUTHORIZE_LOGIN, params, LoginBean.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 1.3第三方登录
     * @param third_id
     * @param name
     * @param head
     * @param user_from
     * @param callback
     */
    public void authLogin(String third_id, String name, String head, String user_from, String sex,ApiCallback callback) {
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("third_id", third_id);
        params.put("name", name);
        params.put("head", head);
        params.put("user_from", user_from);
        params.put("sex", sex);
        params.put("device_token", ac.device_token);
        post(callback, LoginConstant.AUTHORIZE_AUTHLOGIN, params, LoginBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }
    /**
     * 1.4发送验证码
     *
     * @param callback
     */
    public void sendCode(String phone, String type, ApiCallback callback) {
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("phone", phone);
        params.put("type", type);
        get(callback, LoginConstant.member_platform_codes_send, params, NetResult.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }


    /**
     * 1.5验证验证码
     *
     * @param callback
     */
    public void validateCode(String phone, String code, ApiCallback callback) {
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("phone", phone);
        params.put("code", code);
        get(callback, LoginConstant.member_platform_codes_verify, params, NetResult.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 1.6验证原密码
     *
     * @param callback
     */
    public void validatepwd(String user_id, String pwd, ApiCallback callback) {
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("access_token", ac.access_token);
        params.put("user_id", user_id);
        params.put("pwd", Md5Utils.md5(pwd));
        get(callback, LoginConstant.VALIDATE_PWD, params, NetResult.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }
    /**
     * 1.7修改密码
     *
     * @param callback
     */
    public void updatePwd(String user_id, String pwd, String old_pwd,ApiCallback callback) {
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("access_token", ac.access_token);
        params.put("user_id", user_id);
        params.put("pwd", Md5Utils.md5(pwd));
        params.put("old_pwd", Md5Utils.md5(old_pwd));
        post(callback, LoginConstant.member_membership_info_updatePassword, params, NetResult.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 1.12找回密码
     *
     * @param callback
     */
    public void findpwd(String phone,String pwd, String code, ApiCallback callback) {
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("phone", phone);
        params.put("pwd", Md5Utils.md5(pwd));
        params.put("code", code);
        post(callback, LoginConstant.member_membership_info_findPassword, params, NetResult.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    static public void init(AppContext appcontext) {
        if (appcontext == null)
            return;
        appcontext.api.addApiClient(new LoginApiClient(appcontext));
    }
}
