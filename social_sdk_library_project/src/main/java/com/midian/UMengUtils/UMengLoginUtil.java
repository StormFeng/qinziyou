package com.midian.UMengUtils;

import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * 友盟登录
 * 
 * @author MIDIAN
 * 
 */
public class UMengLoginUtil {

	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.login");
	private Activity mContext;
	UMSsoHandler ssoHandler;
	DataListener mDataListener;
	String uId;
	public static UMengLoginUtil mLoginUtil = null;

	private UMengLoginUtil(final Activity context) {
		this.mContext = context;
		if (ssoHandler != null) {
			mController.getConfig().setSsoHandler(ssoHandler);
		}
		configPlatforms();

	}

	public void sinaSso() {

		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().setSinaCallbackUrl("http://open.weibo.com/");
		// SocializeConfig config = mController.getConfig();
		// config.addFollow(SHARE_MEDIA.SINA, "900966085");
		// // 添加follow 时的回调
		// config.setOauthDialogFollowListener(new MulStatusListener() {
		// @Override
		// public void onStart() {
		// }
		//
		// @Override
		// public void onComplete(MultiStatus multiStatus, int st,
		// SocializeEntity entity) {
		// if (st == 200) {
		//
		// }
		// }
		// });
	}

	public static UMengLoginUtil getInstance(Activity context) {

		if (mLoginUtil == null)
			mLoginUtil = new UMengLoginUtil(context);

		return mLoginUtil;
	}

	public void clear() {
		mLoginUtil = null;
	}

	public void configPlatforms() {
//		// 添加新浪SSO授权
//		mController.getConfig().setSsoHandler(new SinaSsoHandler());
//		// 添加腾讯微博SSO授权
//		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
//
		// 添加QQ、QZone平台
		addQQQZonePlatform();

		// 添加微信、微信朋友圈平台
		addWXPlatform();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
        Log.d("wqf","addWXPlatform");
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = Constant.weixinAppId;
		String appSecret = Constant.weixinAppSecret;
		// String appId = "wx90c6a7bcba68e783";
		// String appSecret = "b2a4552aa9821d36bfe4d9eae47c5aae";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(mContext, appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(mContext, appId,
				appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = Constant.qqAppId;

		String appKey = Constant.qqAppKEY;
		// 添加QQ支持
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mContext, appId,
				appKey);
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mContext, appId,
				appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * 注销本次登录</br>
	 */
	public void logout(final SHARE_MEDIA platform) {
		mController.deleteOauth(mContext, platform,
				new SocializeClientListener() {

					@Override
					public void onStart() {

					}

					@Override
					public void onComplete(int status, SocializeEntity entity) {
						String showText = "解除" + platform.toString() + "平台授权成功";
						if (status != StatusCode.ST_CODE_SUCCESSED) {
							showText = "解除" + platform.toString() + "平台授权失败["
									+ status + "]";
						}
						// Toast.makeText(mContext, showText,
						// Toast.LENGTH_SHORT)
						// .show();
					}
				});
	}

	/**
	 * 授权。如果授权成功，则获取用户信息</br>
	 */
	public void login(final SHARE_MEDIA platform) {

		// if (OauthHelper.isAuthenticated(mContext, platform)) {
		// getUserInfo(platform);
		//
		// } else {
		// if (SHARE_MEDIA.SINA == platform) {
		// mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// }

			mController.doOauthVerify(mContext, platform, new UMAuthListener() {

				@Override
				public void onStart(SHARE_MEDIA platform) {
					Log.d("wqf","授权开始");
				}


				@Override
				public void onError(SocializeException e, SHARE_MEDIA platform) {
					Log.d("wqf","授权失败");
				}

				@Override
				public void onComplete(Bundle value, SHARE_MEDIA platform) {
					uId = value.getString("uid");
					Log.d("wqf","onComplete");
					if (!TextUtils.isEmpty(uId)) {
						Log.d("wqf","获取用户信息");
						getUserInfo(platform);

					} else {
						Log.d("wqf","授权失败");
						Toast.makeText(mContext, "授权失败...", Toast.LENGTH_SHORT)
								.show();
					}
				}

				@Override
				public void onCancel(SHARE_MEDIA platform) {
					Log.d("wqf","onCancel");
				}
			});

	}

	// }

	/**
	 * 获取授权平台的用户信息</br>
	 */
	private void getUserInfo(final SHARE_MEDIA platform) {
		Log.d("wqf","getUserInfo");
		mController.getPlatformInfo(mContext, platform, new UMDataListener() {

			@Override
			public void onStart() {
				if (mDataListener != null) {
					mDataListener.onStart(platform);
				}
			}

			@Override
			public void onComplete(int status, Map<String, Object> info) {
				System.out.println("onComplete" + info.toString());
				String showText = "";

				if (status == 200) {
					System.out.println("info::::::::::::::" + info.toString());
					if (info == null) {
						if (mDataListener != null) {
							mDataListener.onFail(platform);
						}
						return;
					}

					String nickName = "";
					String headurl = "";
					int sex=0;

					if (platform == SHARE_MEDIA.QQ) {
						nickName = (String) info.get("screen_name");
						headurl = (String) info.get("profile_image_url");

					} else if (platform == SHARE_MEDIA.WEIXIN) {
						nickName = (String) info.get("nickname");
						headurl = (String) info.get("headimgurl");
						uId=(String) info.get("unionid");
						sex= (int) info.get("sex");
						System.out.println("nickName" + nickName + "headurl"
								+ headurl);
					} else if (platform == SHARE_MEDIA.SINA) {
						nickName = (String) info.get("screen_name");
						headurl = (String) info.get("profile_image_url");
						System.out.println("screen_name" + nickName + "headurl"
								+ headurl);
					}
					if (mDataListener != null) {
						mDataListener.onSuccess(platform, uId, nickName,
								headurl,sex+"");
					}
				}
			}
		});
	}

	public String getTag(SHARE_MEDIA platform) {
		String tag = "";
		if (platform == SHARE_MEDIA.QQ) {
			tag = "3";
		} else if (platform == SHARE_MEDIA.WEIXIN) {
			tag = "2";
		} else if (platform==SHARE_MEDIA.SINA) {
			tag = "4";
		}
		return tag;
	}

	public void setDataListener(DataListener mDataListener) {
		this.mDataListener = mDataListener;
	}

	public interface DataListener {
		public void onStart(SHARE_MEDIA platform);

		public void onSuccess(SHARE_MEDIA platform, String uid,
				String nickName, String headurl,String sex);

		public void onFail(SHARE_MEDIA platform);

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("", "#### ssoHandler.authorizeCallBack11111");
		String result = "null";

		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			Log.d("", "#### ssoHandler.authorizeCallBack");
		}

		try {
			Bundle b = data.getExtras();
			Set<String> keySet = b.keySet();
			if (keySet.size() > 0)
				result = "result size:" + keySet.size();
			for (String key : keySet) {
				Object object = b.get(key);
				Log.d("TestData", "Result:" + key + "   " + object.toString());
			}
		} catch (Exception e) {

		}
		Log.d("TestData", "onActivityResult   " + requestCode + "   "
				+ resultCode + "   " + result);

		// 根据requestCode获取对应的SsoHandler
	}
}
