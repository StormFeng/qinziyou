package com.midian.qzy.api;
import android.util.Log;

import com.midian.fastdevelop.afinal.http.AjaxParams;
import com.midian.qzy.app.Constant;
import com.midian.qzy.bean.ActivitiesBean;
import com.midian.qzy.bean.ActivityCommentsBean;
import com.midian.qzy.bean.ActivityDetailBean;
import com.midian.qzy.bean.ActivityFilterParamsBean;
import com.midian.qzy.bean.AdvertiseBean;
import com.midian.qzy.bean.AndroidVersionBean;
import com.midian.qzy.bean.BannerBean;
import com.midian.qzy.bean.JoinActivityBean;
import com.midian.qzy.bean.MyActivitiesBean;
import com.midian.qzy.bean.MyActivityCommentsBean;
import com.midian.qzy.bean.MyMsgsBean;
import com.midian.qzy.bean.SaveDeviceBean;
import com.midian.qzy.bean.SysConfListBean;
import com.midian.qzy.bean.UpdateUserBean;
import com.midian.qzy.bean.UserDetailBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import midian.baselib.api.ApiCallback;
import midian.baselib.api.BaseApiClient;
import midian.baselib.app.AppContext;
import midian.baselib.bean.NetResult;

/**
 * 网络请求客户端
 *
 * @author moshouguan
 */
public class QzyApiClient extends BaseApiClient {

    public QzyApiClient(AppContext api) {
        super(api);
    }

    static public void init(AppContext appcontext) {
        if (appcontext == null)
            return;
        appcontext.api.addApiClient(new QzyApiClient(appcontext));
    }

    /**
     * 1.8个人资料
     * @param user_id
     * @param callback
     */
    public void userDetail(String user_id,ApiCallback callback) {
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("access_token", ac.access_token);
        params.put("user_id", user_id);
        get(callback, Constant.USER_DETAIL, params, UserDetailBean.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 1.9修改个人资料
     * @param user_id
     * @param head_pic
     * @param name
     * @param sex
     * @param callback
     * @throws FileNotFoundException
     */
    public void updateUser(String user_id,File head_pic,String name,String sex,ApiCallback callback) throws FileNotFoundException {
        AjaxParams params = new AjaxParams();
        params.setHasFile(true);
        params.put("client_key", ac.getClientKey());
        params.put("access_token", ac.access_token);
        params.put("user_id", user_id);
        if(head_pic!=null)
            params.put("head_pic", head_pic);
        params.put("name", name);
        params.put("sex", sex);
        post(callback, Constant.UPDATE_USER, params, UpdateUserBean.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 1.10修改手机号
     * @param user_id
     * @param new_phone
     * @param code
     * @param callback
     */
    public void updatePhone(String user_id,String new_phone,String code,ApiCallback callback){
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("access_token", ac.access_token);
        params.put("user_id", user_id);
        params.put("new_phone", new_phone);
        params.put("code", code);
        post(callback, Constant.UPDATE_PHONE, params, NetResult.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 1.11更改会员推送接收状态
     * @param user_id
     * @param receive_status
     * @param callback
     */
    public void updateReceiveStatus(String user_id,String receive_status,ApiCallback callback){
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("access_token", ac.access_token);
        params.put("user_id", user_id);
        params.put("receive_status", receive_status);
        post(callback, Constant.UPDATE_RECEIVE_STATUS, params, NetResult.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 1.12找回密码
     * @param phone
     * @param pwd
     * @param code
     * @param callback
     */
    public void findPwd(String phone,String pwd,String code,ApiCallback callback){
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("phone", phone);
        params.put("pwd", pwd);
        params.put("code", code);
        post(callback, Constant.FIND_PWD, params, NetResult.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 1.13发送用户设备号/清空设备号
     * @param user_id
     * @param callback
     */
    public void saveDevice(String user_id,ApiCallback callback){
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("access_token", ac.access_token);
        params.put("device_token", ac.device_token);
        params.put("user_id", user_id);
        post(callback, Constant.SAVE_DEVICE, params, SaveDeviceBean.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 2.1.获取最新版本信息
     * @param callback
     */
    public void androidVersion(ApiCallback callback){
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        get(callback, Constant.ANDROID_VERSION, params, AndroidVersionBean.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 2.2.获取系统配置信息
     * @param callback
     */
    public void sysConfList(ApiCallback callback){
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        get(callback, Constant.SYS_CONF_LIST, params, SysConfListBean.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 2.3.反馈
     * @param user_id
     * @param content
     * @param callback
     */
    public void advice(String user_id,String content,ApiCallback callback){
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("access_token", ac.access_token);
        params.put("user_id", user_id);
        params.put("content", content);
        post(callback, Constant.ADVICE, params, NetResult.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 2.4 机构加盟申请
     * @param name
     * @param logo
     * @param contact_person
     * @param contact_num
     * @param contact_address
     * @param website
     * @param business_license
     * @param remark
     * @param callback
     * @throws FileNotFoundException
     */
    public void organizationJoinApply(String name,File logo,String contact_person,String contact_num,
                                      String contact_address, String website,File business_license,
                                      String remark,ApiCallback callback) throws FileNotFoundException {
        AjaxParams params = new AjaxParams();
        params.setHasFile(true);
        params.put("client_key", ac.getClientKey());
        params.put("name", name);
        params.put("contact_person", contact_person);
        params.put("contact_num", contact_num);
        params.put("contact_address", contact_address);
        params.put("website", website);
        params.put("remark", remark);
        if(logo!=null){
            params.put("logo", logo);
        }
        if(business_license!=null){
            params.put("business_license", business_license);
        }
        post(callback, Constant.ORGANIZATION_JOIN_APPLY, params, NetResult.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 3.1 首页banner
     * @param callback
     */
    public void report(String content,String type,String target_id,String user_id,ApiCallback callback){
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("access_token", ac.access_token);
        params.put("content", content);
        params.put("type", type);
        params.put("target_id", target_id);
        params.put("user_id", user_id);
        post(callback, Constant.REPORT, params, NetResult.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }
    /**
     * 3.1 首页banner
     * @param callback
     */
    public void banner(ApiCallback callback){
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        get(callback, Constant.BANNER, params, BannerBean.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     *  3.1 首页banner
     * @return
     * @throws Exception
     */
    public BannerBean getBanner() throws Exception {
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        return (BannerBean) getSync(Constant.BANNER, params, BannerBean.class);
    }

    /**
     * 3.2 活动列表
     * @param page
     * @param size
     * @param lon
     * @param lat
     * @param time_filter
     * @param type_filter
     * @param age_filter
     * @param distance_filter
     * @param key
     * @return
     * @throws Exception
     */
    public ActivitiesBean getActivitiesBean(String page,String size,String lon,String lat,
                                            String time_filter,String type_filter,String age_filter,
                                            String distance_filter,String key)throws Exception{
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("page", page);
        params.put("size", size);
        params.put("lon", lon);
        params.put("lat", lat);
        params.put("time_filter", time_filter);
        params.put("type_filter", type_filter);
        params.put("age_filter", age_filter);
        params.put("distance_filter", distance_filter);
        params.put("key", key);
        return (ActivitiesBean) getSync(Constant.ACTIVITIES, params, ActivitiesBean.class);
    }

    /**
     * 3.2 活动列表
     * @param page
     * @param size
     * @param lon
     * @param lat
     * @param time_filter
     * @param type_filter
     * @param age_filter
     * @param distance_filter
     * @param key
     * @param callback
     */
    public void getActivitiesBean(String page,String size,String lon,String lat,
                                            String time_filter,String type_filter,String age_filter,
                                            String distance_filter,String key,ApiCallback callback){
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("page", page);
        params.put("size", size);
        params.put("lon", lon);
        params.put("lat", lat);
        params.put("time_filter", time_filter);
        params.put("type_filter", type_filter);
        params.put("age_filter", age_filter);
        params.put("distance_filter", distance_filter);
        params.put("key", key);
        get(callback, Constant.ACTIVITY_DETAIL, params, ActivitiesBean.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 3.3 活动详情
     * @param activity_id
     * @param user_id
     * @param callback
     */
    public void activityDetail(String activity_id,String user_id,String lon,String lat,ApiCallback callback){
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("access_token", ac.access_token);
        params.put("activity_id", activity_id);
        params.put("user_id", user_id);
        params.put("lon", lon);
        params.put("lat", lat);
        get(callback, Constant.ACTIVITY_DETAIL, params, ActivityDetailBean.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 3.4 活动报名
     * @param user_id
     * @param activity_id
     * @param adult_count
     * @param child_count
     * @param contact_person
     * @param contact_num
     * @param pay_way
     * @param callback
     */
    public void joinActivity(String user_id,String activity_id,String adult_count,String child_count,
                             String contact_person,String contact_num,String pay_way,ApiCallback callback){
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("access_token", ac.access_token);
        params.put("activity_id", activity_id);
        params.put("user_id", user_id);
        params.put("adult_count", adult_count);
        params.put("child_count", child_count);
        params.put("contact_person", contact_person);
        params.put("contact_num", contact_num);
        params.put("pay_way", pay_way);
        post(callback, Constant.JOIN_ACTIVITY, params, JoinActivityBean.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 3.5活动评论列表
     * @param activity_id
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    public ActivityCommentsBean getActivitiesCommentsBean(String activity_id,String page,String size)throws Exception{
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("activity_id", activity_id);
        params.put("page", page);
        params.put("size", size);
        return (ActivityCommentsBean) getSync(Constant.ACTIVITY_COMMENTS, params, ActivityCommentsBean.class);
    }

    /**
     * 3.5活动评论列表
     * @param activity_id
     * @param page
     * @param size
     * @param callback
     */
    public void getActivitiesCommentsBean(String activity_id,String page,String size,ApiCallback callback){
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("activity_id", activity_id);
        params.put("page", page);
        params.put("size", size);
        get(callback, Constant.ACTIVITY_COMMENTS, params, ActivityCommentsBean.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 3.6活动评论
     * @param user_id
     * @param activity_id
     * @param content
     * @param pics
     * @param callback
     * @throws FileNotFoundException
     */
    public void addActivityComment(String user_id, String activity_id, String content, List<File> pics, ApiCallback callback) throws FileNotFoundException {
        AjaxParams params = new AjaxParams();
        params.setHasFile(true);
        params.put("client_key", ac.getClientKey());
        params.put("access_token", ac.access_token);
        params.put("activity_id", activity_id);
        params.put("user_id", user_id);
        params.put("content", content);
        if(pics!=null){
            for(int i=0;i<pics.size();i++){
                params.put("pics", pics.get(i));
                Log.d("wqf","AAAAAAAAAAAAA"+"\n");
            }
        }
        post(callback, Constant.ADD_ACTIVITY_COMMENT, params, BannerBean.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 3.7活动筛选参数接口
     * @param callback
     */
    public void activityFilterParams(ApiCallback callback){
        AjaxParams params = new AjaxParams();

        params.put("client_key", ac.getClientKey());
        get(callback, Constant.ACTIVITY_FILTER_PARAMS, params, ActivityFilterParamsBean.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 4.1 我的消息列表
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    public MyMsgsBean getMyMsgsBean(String page, String size)throws Exception{
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("page", page);
        params.put("size", size);
        return (MyMsgsBean) getSync(Constant.MY_MSGS, params, MyMsgsBean.class);
    }

    /**
     * 4.2 我的活动列表
     * @param user_id
     * @param page
     * @param size
     * @param activity_filter
     * @return
     * @throws Exception
     */
    public MyActivitiesBean getMyActivitiesBean(String user_id,String page, String size,String activity_filter)throws Exception{
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("access_token", ac.access_token);
        params.put("user_id", user_id);
        params.put("size", size);
        params.put("page", page);
        params.put("activity_filter", activity_filter);
        return (MyActivitiesBean) getSync(Constant.MY_ACTIVITIES, params, MyActivitiesBean.class);
    }

    /**
     * 4.3我的活动评论列表
     * @param user_id
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    public MyActivityCommentsBean getMyActivityCommentsBean(String user_id, String page, String size)throws Exception{
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("access_token", ac.access_token);
        params.put("user_id", user_id);
        params.put("size", size);
        params.put("page", page);
        return (MyActivityCommentsBean) getSync(Constant.MY_ACTIVITIES_COMMENTS, params, MyActivityCommentsBean.class);
    }

    /**
     * 4.3我的活动评论列表
     * @param user_id
     * @param page
     * @param size
     * @param callback
     */
    public void getMyActivityCommentsBean(String user_id, String page, String size,ApiCallback callback){
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("access_token", ac.access_token);
        params.put("user_id", user_id);
        params.put("size", size);
        params.put("page", page);
        get(callback,Constant.MY_ACTIVITIES_COMMENTS, params, MyActivityCommentsBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 4.4删除我的活动评论
     * @param user_id
     * @param comment_id
     * @param callback
     */
    public void deleteActivityComment(String user_id,String comment_id,ApiCallback callback){
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        params.put("access_token", ac.access_token);
        params.put("comment_id", comment_id);
        params.put("user_id", user_id);
        post(callback, Constant.DELETE_ACTIVITY_COMMENT, params, NetResult.class,
                getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 新增启动页广告图
     * @param client_key
     * @param callback
     */
    public void getAdvertisePic(String client_key,ApiCallback callback){
        AjaxParams params = new AjaxParams();
        params.put("client_key", ac.getClientKey());
        get(callback,Constant.ADVERTISEPIC, params, AdvertiseBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }
}
