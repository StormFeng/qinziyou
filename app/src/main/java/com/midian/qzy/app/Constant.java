package com.midian.qzy.app;

import com.midian.maplib.ServerConstant;

public class Constant {

    public static final String BBASEURL = ServerConstant.BHOST;// 亲子游

    /**
     * 1.1注册
     */
    public static final String REGISTER = BBASEURL + "register";
    /**
     * 1.2登陆
     */
    public static final String LOGIN = BBASEURL + "login";

    /**
     * 1.3第三方登录
     */
    public static final String THIRD_USER_LOGIN = BBASEURL + "third_user_login";

    /**
     * 1.4发送验证码
     */
    public static final String SEND_CODE = BBASEURL + "send_code";

    /**
     * 1.5验证验证码
     */
    public static final String VALIDATE_CODE = BBASEURL + "validate_code";
    /**
     * 1.6验证原密码
     */
    public static final String VALIDATE_PWD = BBASEURL + "validate_pwd";

    /**
     * 1.7修改密码
     */
    public static final String UPDATE_PWD = BBASEURL + "update_pwd";
    /**
     * 1.8个人资料
     */
    public static final String USER_DETAIL = BBASEURL + "user_detail";
    /**
     * 1.9修改个人资料
     */
    public static final String UPDATE_USER = BBASEURL + "update_user";
    /**
     * 1.10修改手机号
     */
    public static final String UPDATE_PHONE = BBASEURL + "update_phone";
    /**
     * 1.11更改会员推送接收状态
     */
    public static final String UPDATE_RECEIVE_STATUS = BBASEURL + "update_receive_status";
    /**
     * 1.12找回密码
     */
    public static final String FIND_PWD = BBASEURL + "find_pwd";
    /**
     * 1.13发送用户设备号/清空设备号
     */
    public static final String SAVE_DEVICE = BBASEURL + "save_device";
    /**
     * 2.1.获取最新版本信息
     */
    public static final String ANDROID_VERSION = BBASEURL + "android_version";
    /**
     * 2.2.获取系统配置信息
     */
    public static final String SYS_CONF_LIST = BBASEURL + "sys_conf_list";
    /**
     * 2.3.反馈
     */
    public static final String ADVICE = BBASEURL + "advice";

    /**
     * 2.4 机构加盟申请
     */
    public static final String ORGANIZATION_JOIN_APPLY = BBASEURL + "organization_join_apply";

    /**
     * 2.5 举报
     */
    public static final String REPORT = BBASEURL + "report";
    /**
     * 3.1 首页banner
     */
    public static final String BANNER = BBASEURL + "mengzhu_index";
    /**
     * 3.2 活动列表
     */
    public static final String ACTIVITIES = BBASEURL + "activities";
    /**
     * 3.3 活动详情
     */
    public static final String ACTIVITY_DETAIL = BBASEURL + "activity_detail";
    /**
     * 3.4 活动报名
     */
    public static final String JOIN_ACTIVITY = BBASEURL + "join_activity";

    /**
     * 3.5活动评论列表
     */
    public static final String ACTIVITY_COMMENTS = BBASEURL + "activity_comments";
    /**
     * 3.6活动评论
     */
    public static final String ADD_ACTIVITY_COMMENT = BBASEURL + "add_activity_comment";
    /**
     * 3.7活动筛选参数接口
     */
    public static final String ACTIVITY_FILTER_PARAMS = BBASEURL + "activity_filter_params";
    /**
     * 4.1 我的消息列表
     */
    public static final String MY_MSGS = BBASEURL + "my_msgs";
    /**
     * 4.2 我的活动列表
     */
    public static final String MY_ACTIVITIES = BBASEURL + "my_activities";
    /**
     * 4.3我的活动评论列表
     */
    public static final String MY_ACTIVITIES_COMMENTS = BBASEURL + "my_activity_comments";
    /**
     * 4.4删除我的活动评论
     */
    public static final String DELETE_ACTIVITY_COMMENT = BBASEURL + "delete_activity_comment";

    /**
     * 新增启动页广告图
     */
    public static final String ADVERTISEPIC = BBASEURL + "advertisePic";
}