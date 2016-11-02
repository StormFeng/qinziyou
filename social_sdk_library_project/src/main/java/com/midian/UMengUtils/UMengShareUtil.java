package com.midian.UMengUtils;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.midian.maplib.ServerConstant;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.MailShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 友盟分享
 * 
 * @author MIDIAN
 * 
 */
public class UMengShareUtil {

	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;
	//
	// public final static String APIKEY = "SGU4OeMo6tHluRlRG9Da3HxG";
	// // 新浪
	// public final static String SINA_APP_KEY = "901776916";
	// public final static String SINA_REDIRECT_URL = "http://weibo.com";
	// public final static String SINA_SCOPE =
	// "email,direct_messages_read,direct_messages_write,"
	// + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
	// + "follow_app_official_microblog," + "invitation_write";
	// // QQ
	// public final static String QQ_APP_KEY = "MpAbAxXkBiDWTczU";
	// public final static String QQ_APP_ID = "1104253874";

	// 微信
	 public final static String WEIXIN_APP_ID = "wx6ec4b95185411c3c";
	 public final static String WEIXIN_APP_KEY =
	 "ccee8a071aa4bac591399fe2de6f637e";

	// private static final String TAG = "MIMI";
	private Activity mContext;
	private static UMengShareUtil mShareUtil = null;
	UMengShareUtilListener mUMengShareUtilListener;

	private UMengShareUtil(Activity context) {
		this.mContext = context;
		configPlatforms();
		mController.getConfig().closeToast();
		// setShareContent();
	}

	public static UMengShareUtil getInstance(Activity context) {

		mShareUtil = new UMengShareUtil(context);

		return mShareUtil;
	}

	private void openShare(String name, String icon, String url) {
		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.SINA, SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL);
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setTargetUrl(url);
		circleMedia.setTitle(name);
		circleMedia.setShareContent(name + url);
		// UMImage image = new UMImage(mContext, icon);
		// circleMedia.setShareMedia(image);
		mController.setShareMedia(circleMedia);
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// weixinContent.setShareMedia(image);
		weixinContent.setShareContent(name + url);
		weixinContent.setTargetUrl(url);
		weixinContent.setTitle(name);
		mController.setShareMedia(weixinContent);

		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setTargetUrl(url);
		qzone.setTitle(name);
		qzone.setShareContent(name + url);
		// qzone.setShareMedia(image);
		mController.setShareMedia(qzone);

		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setTitle(name);
		qqShareContent.setTargetUrl(url);
		qqShareContent.setShareContent(name + url);
		// qqShareContent.setShareMedia(image);
		mController.setShareMedia(qqShareContent);
		SinaShareContent sinaShareContent = new SinaShareContent();
		sinaShareContent.setTitle(name);
		sinaShareContent.setTargetUrl(url);
		sinaShareContent.setShareContent(name + url);
		// sinaShareContent.setShareMedia(image);
		mController.setShareMedia(sinaShareContent);
		mController.openShare(mContext, false);
	}

	/**
	 * 配置分享平台参数</br>
	 */
	public void configPlatforms() {
		// 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().setSinaCallbackUrl("http://sns.whalecloud.com");
		// 添加腾讯微博SSO授权
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

		// 添加QQ、QZone平台
		addQQQZonePlatform();

		// 添加微信、微信朋友圈平台
		addWXPlatform();
		// 添加短信分享
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
		EmailHandler emailHandler = new EmailHandler();
		emailHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = Constant.weixinAppId;
		String appSecret = Constant.weixinAppSecret;
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(mContext, appId, appSecret);
		wxHandler.addToSocialSDK();
		// wxHandler.showCompressToast(false);
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(mContext, appId,
				appSecret);
		wxCircleHandler.setToCircle(true);
		// wxCircleHandler.showCompressToast(false);
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
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mContext, appId,
				appKey);
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mContext, appId,
				appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * 直接分享，底层分享接口。如果分享的平台是新浪、腾讯微博、豆瓣、人人，则直接分享，无任何界面弹出； 其它平台分别启动客户端分享</br>
	 */
	private void directShare() {
		mController.directShare(mContext, mPlatform, new SnsPostListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				String showText = "分享成功";
				if (eCode != StatusCode.ST_CODE_SUCCESSED) {
					showText = "分享失败 [" + eCode + "]";
				}
				Toast.makeText(mContext, showText, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void setSMSShareContent(ShareContent frontiaSocialShareContent) {
		SmsShareContent smsShareContent = new SmsShareContent();
		smsShareContent.setShareContent(frontiaSocialShareContent.getSummary()
				+ getShareUrl(frontiaSocialShareContent));
		UMImage smsImage = null;

		if (frontiaSocialShareContent.getmBitmap() != null) {
			smsImage = new UMImage(mContext,
					frontiaSocialShareContent.getmBitmap());
		} else {
			smsImage = new UMImage(mContext,
					frontiaSocialShareContent.getImage());
		}
		smsShareContent.setShareImage(smsImage);
		mController.setShareMedia(smsShareContent);
	}

	private void setEmailShareContent(ShareContent frontiaSocialShareContent) {

		MailShareContent mailShareContent = new MailShareContent();
		mailShareContent.setShareContent(frontiaSocialShareContent.getSummary()
				+ getShareUrl(frontiaSocialShareContent));
		mailShareContent.setTitle(frontiaSocialShareContent.getTitle());
		UMImage smsImage = null;

		if (frontiaSocialShareContent.getmBitmap() != null) {
			smsImage = new UMImage(mContext,
					frontiaSocialShareContent.getmBitmap());
		} else {
			smsImage = new UMImage(mContext,
					frontiaSocialShareContent.getImage());
		}
		mailShareContent.setShareImage(smsImage);
		mController.setShareMedia(mailShareContent);
	}

	private void setQQShareContent(ShareContent frontiaSocialShareContent) {
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent(frontiaSocialShareContent.getSummary());
		qqShareContent.setTitle(frontiaSocialShareContent.getTitle());
		UMImage qqImage = null;
		if (frontiaSocialShareContent.getmBitmap() != null) {
			qqImage = new UMImage(mContext,
					frontiaSocialShareContent.getmBitmap());
		} else {
			qqImage = new UMImage(mContext,
					getShareUrl(frontiaSocialShareContent.getImage()));
		}
		qqShareContent.setShareMedia(qqImage);
		qqShareContent.setTargetUrl(getShareUrl(frontiaSocialShareContent));
		mController.setShareMedia(qqShareContent);
	}

	private void setQQZoneShareContent(ShareContent frontiaSocialShareContent) {
		QZoneShareContent qqZoneShareContent = new QZoneShareContent();
		qqZoneShareContent.setShareContent(frontiaSocialShareContent
				.getSummary());
		qqZoneShareContent.setTitle(frontiaSocialShareContent.getTitle());
		UMImage qqZoneImage = null;
		if (frontiaSocialShareContent.getmBitmap() != null) {
			qqZoneImage = new UMImage(mContext,
					frontiaSocialShareContent.getmBitmap());
		} else {
			qqZoneImage = new UMImage(mContext,
					getShareUrl(frontiaSocialShareContent.getImage()));
		}
		qqZoneShareContent.setShareMedia(qqZoneImage);
		qqZoneShareContent.setTargetUrl(getShareUrl(frontiaSocialShareContent));
		mController.setShareMedia(qqZoneShareContent);
	}

	private void setWeiXinShareContent(ShareContent frontiaSocialShareContent) {

		WeiXinShareContent mShareContent = new WeiXinShareContent();
		mShareContent.setShareContent(frontiaSocialShareContent.getSummary());
		mShareContent.setTitle(frontiaSocialShareContent.getTitle());
		UMImage qqZoneImage = null;
		if (frontiaSocialShareContent.getmBitmap() != null) {
			qqZoneImage = new UMImage(mContext,
					frontiaSocialShareContent.getmBitmap());
		} else {
			qqZoneImage = new UMImage(mContext,
					getShareUrl(frontiaSocialShareContent.getImage()));
		}
		mShareContent.setShareMedia(qqZoneImage);
		mShareContent.setTargetUrl(getShareUrl(frontiaSocialShareContent));
		mController.setShareMedia(mShareContent);
	}

	private void setWeiXinCycleShareContent(
			ShareContent frontiaSocialShareContent) {

		CircleShareContent mShareContent = new CircleShareContent();
		mShareContent.setShareContent(frontiaSocialShareContent.getSummary());
		mShareContent.setTitle(frontiaSocialShareContent.getTitle());
		UMImage qqZoneImage = null;
		if (frontiaSocialShareContent.getmBitmap() != null) {
			qqZoneImage = new UMImage(mContext,
					frontiaSocialShareContent.getmBitmap());
		} else {
			qqZoneImage = new UMImage(mContext,
					getShareUrl(frontiaSocialShareContent.getImage()));
		}
		mShareContent.setShareMedia(qqZoneImage);
		mShareContent.setTargetUrl(getShareUrl(frontiaSocialShareContent));
		mController.setShareMedia(mShareContent);
	}

	private void setSinaShareContent(ShareContent frontiaSocialShareContent) {

		SinaShareContent mShareContent = new SinaShareContent();
		mShareContent.setShareContent(frontiaSocialShareContent.getSummary()
				+ getShareUrl(frontiaSocialShareContent));
		mShareContent.setTitle(frontiaSocialShareContent.getTitle());
		UMImage qqZoneImage = null;
		if (frontiaSocialShareContent.getmBitmap() != null) {
			qqZoneImage = new UMImage(mContext,
					frontiaSocialShareContent.getmBitmap());
		} else {
			qqZoneImage = new UMImage(mContext,
					getShareUrl(frontiaSocialShareContent.getImage()));
		}
		mShareContent.setShareMedia(qqZoneImage);
		mShareContent.setTargetUrl(getShareUrl(frontiaSocialShareContent));
		mController.setShareMedia(mShareContent);
	}

	public void share(SHARE_MEDIA platform,
			ShareContent frontiaSocialShareContent) {
		try {
			if (platform == SHARE_MEDIA.QQ) {// qq
				shareToQQ(frontiaSocialShareContent);
			} else if (platform == SHARE_MEDIA.QZONE) {// qq空间
				shareToQQZone(frontiaSocialShareContent);
			} else if (platform == SHARE_MEDIA.WEIXIN) {// 微信
				shareToWeixin(frontiaSocialShareContent);
			} else if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {//微信 朋友圈
				shareToWeixinCycle(frontiaSocialShareContent);
			} else if (platform == SHARE_MEDIA.SINA) {// 新浪微博
				shareToWeibo(frontiaSocialShareContent);
			} else if (platform == SHARE_MEDIA.SMS) {// 短信
				shareToSMS(frontiaSocialShareContent);
			} else if (platform == SHARE_MEDIA.EMAIL) {// 邮箱
				shareToEmail(frontiaSocialShareContent);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 分享到QQ
	 * 
	 * @param frontiaSocialShareContent
	 */
	public void shareToQQ(ShareContent frontiaSocialShareContent) {
		setQQShareContent(frontiaSocialShareContent);
		mController.getConfig().closeToast();

		mController.postShare(mContext, SHARE_MEDIA.QQ, new SnsPostListener() {

			@Override
			public void onStart() {

				Toast.makeText(mContext, "分享中...", Toast.LENGTH_SHORT).show();
				if (mUMengShareUtilListener != null) {
					mUMengShareUtilListener.onStart();
				}
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
                System.out.println("-=-=-=-=-=-=-"+ "QQ分享: ecode="+eCode);
				String showText = platform.toString();
				if (SHARE_MEDIA.QQ == platform) {
					if (eCode == StatusCode.ST_CODE_SUCCESSED) {
						showText = "QQ分享成功";
					} else {
						showText = "QQ分享失败";
					}
					Toast.makeText(mContext, showText, Toast.LENGTH_SHORT)
							.show();
				}
				mController.getConfig().cleanListeners();
				if (mUMengShareUtilListener != null) {
					mUMengShareUtilListener.onComplete(platform, eCode, entity);
				}
			}

		});
	}

	/**
	 * 分享到QQ空间
	 * 
	 * @param frontiaSocialShareContent
	 */
	public void shareToQQZone(ShareContent frontiaSocialShareContent) {
		setQQZoneShareContent(frontiaSocialShareContent);
		mController.getConfig().closeToast();
		mController.postShare(mContext, SHARE_MEDIA.QZONE,
				new SnsPostListener() {

					@Override
					public void onStart() {
						Toast.makeText(mContext, "分享中...", Toast.LENGTH_SHORT)
								.show();

						if (mUMengShareUtilListener != null) {
							mUMengShareUtilListener.onStart();
						}
					}

					@Override
					public void onComplete(SHARE_MEDIA platform, int eCode,
							SocializeEntity entity) {
						String showText = platform.toString();

						if (SHARE_MEDIA.QZONE == platform) {
							if (eCode == StatusCode.ST_CODE_SUCCESSED) {
								showText = "QQ空间分享成功";
							} else {
								showText = "QQ空间分享失败";
							}
							Toast.makeText(mContext, showText,
									Toast.LENGTH_SHORT).show();
						}
						mController.getConfig().cleanListeners();
						if (mUMengShareUtilListener != null) {
							mUMengShareUtilListener.onComplete(platform, eCode,
									entity);
						}
					}
				});
	}

	/**
	 * 分享到微信
	 * 
	 * @param frontiaSocialShareContent
	 */
	public void shareToWeixin(ShareContent frontiaSocialShareContent) {
		setWeiXinShareContent(frontiaSocialShareContent);
		mController.getConfig().closeToast();
		mController.postShare(mContext, SHARE_MEDIA.WEIXIN,
				new SnsPostListener() {
					@Override
					public void onStart() {
						Toast.makeText(mContext, "分享中...", Toast.LENGTH_SHORT).show();
                        if (mUMengShareUtilListener != null) {
							mUMengShareUtilListener.onStart();
						}
					}

					@Override
					public void onComplete(SHARE_MEDIA platform, int eCode,
							SocializeEntity entity) {
						if (SHARE_MEDIA.WEIXIN == platform) {
							String showText = platform.toString();
							if (eCode == StatusCode.ST_CODE_SUCCESSED) {
								showText = "微信分享成功";
							} else {
								showText = "微信分享失败";
							}
							Toast.makeText(mContext, showText, Toast.LENGTH_SHORT).show();
						}
						mController.getConfig().cleanListeners();
						if (mUMengShareUtilListener != null) {
							mUMengShareUtilListener.onComplete(platform, eCode,
									entity);
						}
					}
				});
	}

	public void shareToWeixinCycle(ShareContent frontiaSocialShareContent) {
		setWeiXinCycleShareContent(frontiaSocialShareContent);
		mController.getConfig().closeToast();
		mController.postShare(mContext, SHARE_MEDIA.WEIXIN_CIRCLE,
				new SnsPostListener() {

					@Override
					public void onStart() {
						Toast.makeText(mContext, "分享中...", Toast.LENGTH_SHORT)
								.show();
						if (mUMengShareUtilListener != null) {
							mUMengShareUtilListener.onStart();
						}
					}

					@Override
					public void onComplete(SHARE_MEDIA platform, int eCode,
							SocializeEntity entity) {
						String showText = platform.toString();
						if (SHARE_MEDIA.WEIXIN_CIRCLE == platform) {
							if (eCode == StatusCode.ST_CODE_SUCCESSED) {
								showText = "微信朋友圈分享成功";
							} else {
								showText = "微信朋友圈分享失败";
							}
							Toast.makeText(mContext, showText,
									Toast.LENGTH_SHORT).show();
						}
						mController.getConfig().cleanListeners();
						if (mUMengShareUtilListener != null) {
							mUMengShareUtilListener.onComplete(platform, eCode,
									entity);
						}
					}
				});
	}

	/**
	 * 分享到新浪微博，必须要有图片
	 * 
	 * @param frontiaSocialShareContent
	 */
	public void shareToWeibo(ShareContent frontiaSocialShareContent) {
		setSinaShareContent(frontiaSocialShareContent);
		mController.getConfig().closeToast();
		mController.directShare(mContext, SHARE_MEDIA.SINA,
				new SnsPostListener() {

					@Override
					public void onStart() {
						Toast.makeText(mContext, "分享中...", Toast.LENGTH_SHORT)
								.show();
						if (mUMengShareUtilListener != null) {
							mUMengShareUtilListener.onStart();
						}
					}

					@Override
					public void onComplete(SHARE_MEDIA platform, int eCode,
							SocializeEntity entity) {
						if (SHARE_MEDIA.SINA == platform) {
							String showText = "微博分享成功";
							if (eCode != StatusCode.ST_CODE_SUCCESSED) {
								showText = "微博分享失败 ";
							}
							Toast.makeText(mContext, showText,
									Toast.LENGTH_SHORT).show();
						}
						mController.getConfig().cleanListeners();
						if (mUMengShareUtilListener != null) {
							mUMengShareUtilListener.onComplete(platform, eCode,
									entity);
						}
					}
				});
	}

	/**
	 * 短信分享
	 * 
	 * @param frontiaSocialShareContent
	 */
	public void shareToSMS(ShareContent frontiaSocialShareContent) {
		setSMSShareContent(frontiaSocialShareContent);
		mController.getConfig().closeToast();

		mController.postShare(mContext, SHARE_MEDIA.SMS, new SnsPostListener() {

			@Override
			public void onStart() {

				Toast.makeText(mContext, "分享中...", Toast.LENGTH_SHORT).show();
				if (mUMengShareUtilListener != null) {
					mUMengShareUtilListener.onStart();
				}
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				String showText = platform.toString();
				if (SHARE_MEDIA.SMS == platform) {
					if (eCode == StatusCode.ST_CODE_SUCCESSED) {
						showText = "短信分享成功";
					} else {
						showText = "短信分享失败";
					}
					Toast.makeText(mContext, showText, Toast.LENGTH_SHORT)
							.show();
				}
				mController.getConfig().cleanListeners();
				if (mUMengShareUtilListener != null) {
					mUMengShareUtilListener.onComplete(platform, eCode, entity);
				}
			}

		});
	}

	public void shareToEmail(ShareContent frontiaSocialShareContent) {
		try {
			setEmailShareContent(frontiaSocialShareContent);
			mController.getConfig().closeToast();

			mController.postShare(mContext, SHARE_MEDIA.EMAIL,
					new SnsPostListener() {

						@Override
						public void onStart() {

							Toast.makeText(mContext, "分享中...",
									Toast.LENGTH_SHORT).show();
							if (mUMengShareUtilListener != null) {
								mUMengShareUtilListener.onStart();
							}
						}

						@Override
						public void onComplete(SHARE_MEDIA platform, int eCode,
								SocializeEntity entity) {
							String showText = platform.toString();
							if (SHARE_MEDIA.SMS == platform) {
								if (eCode == StatusCode.ST_CODE_SUCCESSED) {
									showText = "邮件分享成功";
								} else {
									showText = "邮件分享失败";
								}
								Toast.makeText(mContext, showText,
										Toast.LENGTH_SHORT).show();
							}
							mController.getConfig().cleanListeners();
							if (mUMengShareUtilListener != null) {
								mUMengShareUtilListener.onComplete(platform,
										eCode, entity);
							}
						}

					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示下载与分享信息
	 * 
	 * @param frontiaSocialShareContent
	 * @return
	 */
	private String getShareUrl(ShareContent frontiaSocialShareContent) {
		if (frontiaSocialShareContent.getUrl().contains(ServerConstant.BASEURL)
				|| frontiaSocialShareContent.getUrl().contains("http://")
				|| frontiaSocialShareContent.getUrl().contains("https://")) {
			return frontiaSocialShareContent.getUrl();
		} else {
			frontiaSocialShareContent.setUrl(ServerConstant.BASEURL
					+ frontiaSocialShareContent.getUrl());
		}

		return frontiaSocialShareContent.getUrl();
	}

	private String getShareUrl(String url) {
		if (url.contains(ServerConstant.BASEURL) || url.contains("http://")
				|| url.contains("https://") || url.contains("file://")) {
			return url;
		} else {
			url = ServerConstant.BASEURL + url;
		}

		return url;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("", "#### ssoHandler.authorizeCallBack11111");
		String result = "null";
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
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			Log.d("", "#### ssoHandler.authorizeCallBack");
		}
	}

	public void setUMengShareUtilListener(
			UMengShareUtilListener mUMengShareUtilListener) {
		this.mUMengShareUtilListener = mUMengShareUtilListener;
	}

	public interface UMengShareUtilListener {
		public void onStart();

		public void onComplete(SHARE_MEDIA platform, int eCode,
				SocializeEntity entity);
	}

}
