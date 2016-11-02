package com.midian.baidupush;

import java.util.ArrayList;
import java.util.List;

import midian.baselib.app.AppContext;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.example.baidupush.R;

/*
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 *onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 *onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调

 * 返回值中的errorCode，解释如下：
 *0 - Success
 *10001 - Network Problem
 *10101  Integrate Check Error
 *30600 - Internal Server Error
 *30601 - Method Not Allowed
 *30602 - Request Params Not Valid
 *30603 - Authentication Failed
 *30604 - Quota Use Up Payment Required
 *30605 -Data Required Not Found
 *30606 - Request Time Expires Timeout
 *30607 - Channel Token Timeout
 *30608 - Bind Relation Not Found
 *30609 - Bind Number Too Many

 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 *
 */

public class MyPushMessageReceiver extends PushMessageReceiver {
    /**
     * TAG to Log
     */
    public static final String TAG = "push消息处理Receiver：：";
    private static int NOTIFICATION_FLAG = 1;
    public static List<PushListener> list = new ArrayList<PushListener>();
    private AppContext ac;
    private String orderid = null;//订单详情页
    private String messageid = null;//消息详情页
    private String questionid = null;//咨询记录页  //咨询点评页
//	private String quesId=null;//咨询点评页
    private int requestCode = 1;//推送请求参数

    public static void addPushListener(PushListener mPushListener) {
        if (!list.contains(mPushListener) && mPushListener != null)
            list.add(mPushListener);
    }

    public static void removePushListener(PushListener mPushListener) {
        if (list.contains(mPushListener))
            list.remove(mPushListener);
    }

    /**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
     *
     * @param context   BroadcastReceiver的执行Context
     * @param errorCode 绑定接口返回值，0 - 成功
     * @param appid     应用id。errorCode非0时为null
     * @param userId    应用user id。errorCode非0时为null
     * @param channelId 应用channel id。errorCode非0时为null
     * @param requestId 向服务端发起的请求id。在追查问题时有用；
     * @return none
     */
    @Override
    public void onBind(Context context, int errorCode, String appid, String userId, String channelId, String requestId) {
        this.ac = (AppContext) context.getApplicationContext();
        Log.d(TAG, "onBind");
        String responseString = "onBind errorCode=" + errorCode + " appid=" + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        Log.d(TAG, responseString);
        System.out.println("onBind绑定成功" + responseString);
        if (errorCode == 0) {
            // 绑定成功
//			((AppContext) context.getApplicationContext()).saveDeviceToken(channelId);
            ac.saveDeviceToken(channelId);
            System.out.println("百度云推送绑定成功;:::channelId=" + channelId);
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        // updateContent(context, responseString);
    }

    /**
     * 接收透传消息的函数。
     *
     * @param context             上下文
     * @param message             推送的消息
     * @param customContentString 自定义内容,为空或者json字符串
     */
    @Override
    public void onMessage(Context context, String message,
                          String customContentString) {
        String messageString = "透传消息 message=\"" + message
                + "\" customContentString=" + customContentString;
        Log.d(TAG, messageString);
        System.out.println("messageString" + messageString);
        // 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
        // if (!TextUtils.isEmpty(customContentString)) {
        // JSONObject customJson = null;
        // try {
        // customJson = new JSONObject(customContentString);
        // String myvalue = null;
        // if (!customJson.isNull("mykey")) {
        // myvalue = customJson.getString("mykey");
        // }
        // } catch (JSONException e) {
        // e.printStackTrace();
        // }
        // }

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, message);
    }

    /**
     * 接收通知点击的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {
        String notifyString = "通知点击 title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        // Log.d(TAG, notifyString);
        //
        // // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        // if (!TextUtils.isEmpty(customContentString)) {
        // JSONObject customJson = null;
        // try {
        // customJson = new JSONObject(customContentString);
        // String myvalue = null;
        // if (!customJson.isNull("mykey")) {
        // myvalue = customJson.getString("mykey");
        // }
        // } catch (JSONException e) {
        // e.printStackTrace();
        // }
        // }
        //
        // // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        // updateContent(context, notifyString);
    }

    /**
     * 接收通知到达的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */

    @Override
    public void onNotificationArrived(Context context, String title,
                                      String description, String customContentString) {

        String notifyString = "onNotificationArrived  title=\"" + title
                + "\" description=\"" + description + "\" customContent="
                + customContentString;
        Log.d(TAG, notifyString);

        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        // if (!TextUtils.isEmpty(customContentString)) {
        // JSONObject customJson = null;
        // try {
        // customJson = new JSONObject(customContentString);
        // String myvalue = null;
        // if (!customJson.isNull("mykey")) {
        // myvalue = customJson.getString("mykey");
        // }
        // } catch (JSONException e) {
        // e.printStackTrace();
        // }
        // }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        // 你可以參考 onNotificationClicked中的提示从自定义内容获取具体值
        // updateContent(context, notifyString);
    }

    /**
     * setTags() 的回调函数。
     *
     * @param context    上下文
     * @param errorCode  错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
     * @param sucessTags 设置成功的tag
     * @param failTags   设置失败的tag
     * @param requestId  分配给对云推送的请求的id
     */
    @Override
    public void onSetTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        // updateContent(context, responseString);
    }

    /**
     * delTags() 的回调函数。
     *
     * @param context    上下文
     * @param errorCode  错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
     * @param sucessTags 成功删除的tag
     * @param failTags   删除失败的tag
     * @param requestId  分配给对云推送的请求的id
     */
    @Override
    public void onDelTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        // updateContent(context, responseString);
    }

    /**
     * listTags() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示列举tag成功；非0表示失败。
     * @param tags      当前应用设置的所有tag。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
                           String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags="
                + tags;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        // updateContent(context, responseString);
    }

    /**
     * PushManager.stopWork() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示从云推送解绑定成功；非0表示失败。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        Log.d(TAG, responseString);

        if (errorCode == 0) {
            // 解绑定成功
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        // updateContent(context, responseString);
    }

    private void updateContent(Context context, String content) {
        DeviceMessage mDeviceMessage = null;

        if (TextUtils.isEmpty(content)) {
            return;
        }
        PushMessage msg = new PushMessage();
        System.out.println("updateContent:::" + content);
        try {
            JSONObject jsonObject = new JSONObject(content);
            mDeviceMessage = new DeviceMessage(
                    jsonObject.getString("page"),
                    jsonObject.getString("noticeCode"),
                    jsonObject.getString("roletype"),
                    jsonObject.getString("notice"),
                    jsonObject.getJSONObject("data"));
            JSONObject data = mDeviceMessage.getData();
            if ("order-detail".equals(mDeviceMessage.getPage())) {
                orderid = data.getString("orderid");
            } else if ("message-detail".equals(mDeviceMessage.getPage())) {
                messageid = data.getString("messageid");
            } else if ("consult-record".equals(mDeviceMessage.getPage())) {
                questionid = data.getString("questionid");
            } else if ("consult-comment".equals(mDeviceMessage.getPage())) {
                questionid = data.getString("questionid");
            }
//			MessageTool.getMessageTool(context).saveMessage(mDeviceMessage);//入本地库
            msg.setMsg(mDeviceMessage);

            String type = mDeviceMessage.getRoletype();
            //0为客商，1为客商中介，2为物业顾问，3为业主，4为服务达人
            String user_type = ((AppContext) context.getApplicationContext()).getProperty("user_type");
            if (user_type.equals(type)) {
                showNotification(context, mDeviceMessage);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (PushListener mPushListener : list) {
            if (mPushListener != null) {
                mPushListener.updateContent(msg);
            }
        }
    }

    /**
     * 展示消息通知
     *
     * @param context
     * @param mDeviceMessage page：
     */
    private void showNotification(Context context, DeviceMessage mDeviceMessage) {
        //创建消息通知
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent();
        //点击手机状态栏后要跳转的页面
//        if ("order-detail".equals(mDeviceMessage.getPage())) {//预约订单详细页
//            intent.putExtra("orderid", orderid);
//            if("0".equals(mDeviceMessage.getRoletype())){
//                intent.setClassName("com.midian.ppaddress", "com.midian.ppaddress.ui.personal.BookingActivity");//普通用户预约页面
//            }else if("2".equals(mDeviceMessage.getRoletype())){
//                intent.setClassName("com.midian.ppaddress", "com.midian.ppaddress.ui.personal.BookingActivity_Counselor");//物业顾问预约页面
//            }
        intent.setClassName("com.midian.qzy", "com.midian.qzy.ui.MainActivity");
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, requestCode, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        requestCode++;
        Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.defaults=Notification.DEFAULT_SOUND;//使用系统默认铃声
        long[] vibrates = {0, 1000, 1000, 1000};
        notification.vibrate=vibrates;//此处表示手机先震动1秒，然后静止1秒，然后再震动1秒
        notification.tickerText = mDeviceMessage.getNotice();
        notification.when = System.currentTimeMillis();
        notification.setLatestEventInfo(context, null, mDeviceMessage.getNotice(), pendingIntent);
        notification.number = 1;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(NOTIFICATION_FLAG, notification);

    }

    public interface PushListener {
        public void updateContent(PushMessage msg);
    }
}
