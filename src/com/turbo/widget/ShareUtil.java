package com.turbo.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

/**
 * 分享工具类
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class ShareUtil {

	/** 包名 */
	public static String SINA_WEIBO_PACKAGE_NAME = "com.sina.weibo";
	public static String TECENT_WEIBO_PACKAGE_NAME = "com.tencent.WBlog";
	public static String QZONE_PACKAGE_NAME = "com.qzone";
	public static String QQ_PACKAGE_NAME = "com.tencent.mobileqq";
	public static String WEIXIN_PACKAGE_NAME = "com.tencent.mm";
	

	/** 拼接网页版本新浪微博url */
	private static final String SINA_URL = "http://service.weibo.com/share/share.php?url=";
	private static final String SINA_TITLE = "&pic=&title=";
	private static final String SINA_END = "&appkey=&ralateUid=";

	/** 拼接网页版腾讯微博url */
	private static final String TECENT_URL = "http://v.t.qq.com/share/share.php?title=";
	private static final String TECENT_TITLE = "&url=";
	private static final String TECENT_END = "&appkey=&site=&pic=";

	private static ResolveInfo resolveInfo = null;

	private ShareUtil() {
	}

	public static void sendShare(Context context, String msg,String picPath){
		picPath = "content://media/external/images/media";
		Uri uri = Uri.parse(picPath);
		Intent intent = new  Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_TEXT, msg);
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		context.startActivity(Intent.createChooser(intent, "Turbo快速开发框架！"));
	}

	/**
	 * 发送新浪微博
	 * 
	 * @param contentStr
	 *            分享的内容
	 * @param shareUrl
	 *            分享的链接
	 * @param appName
	 *            分享的APP名字
	 */
	public static void sendSinaWeibo(Context context, String contentStr,
			String shareUrl) {
		boolean flag = check(context, SINA_WEIBO_PACKAGE_NAME);
		if (!flag) {
			Uri sinaWeiboToUri = Uri.parse(SINA_URL + shareUrl + SINA_TITLE
					+ contentStr + SINA_END);
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					sinaWeiboToUri);
			context.startActivity(intent);
		} else {
			shareThroughClient(context, resolveInfo, contentStr, shareUrl);
		}
	}

	/**
	 * 发送微信
	 * @param context
	 * @param contentStr
	 * @param shareUrl
	 */
	public static void sendWeiXin(Context context, String contentStr,
			String shareUrl) {
		boolean flag = check(context, WEIXIN_PACKAGE_NAME);
		if (!flag) {
			Toast.makeText(context, "您还未安装微信", Toast.LENGTH_SHORT).show();
		} else {
			// 通过客户端分享
			shareThroughClient(context, resolveInfo, contentStr, shareUrl);
		}
	}
	
	/**
	 * 发送腾讯微博
	 * 
	 * @param contentStr
	 *            分享的内容
	 * @param shareUrl
	 *            分享的链接
	 */
	public static void sendTecentWeibo(Context context, String contentStr,
			String shareUrl) {
		boolean flag = check(context, TECENT_WEIBO_PACKAGE_NAME);
		if (!flag) {
			// 通过网页分享
			Uri tecentWeiboToUri = Uri.parse(TECENT_URL + contentStr
					+ TECENT_TITLE + shareUrl + TECENT_END);
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					tecentWeiboToUri);
			context.startActivity(intent);
		} else {
			// 通过客户端分享
			shareThroughClient(context, resolveInfo, contentStr, shareUrl);
		}
	}
	
	
	/**
	 * 分享到QZone
	 * 
	 * @param contentStr
	 *            分享的内容
	 * @param shareUrl
	 *            分享的链接
	 */
	public static void sendQZone(Context context, String contentStr,
			String shareUrl) {
		boolean flag = check(context, QZONE_PACKAGE_NAME);
		if (!flag) {
//			// 通过网页分享
//			Uri tecentWeiboToUri = Uri.parse(TECENT_URL + contentStr
//					+ TECENT_TITLE + shareUrl + TECENT_END);
//			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//					tecentWeiboToUri);
//			context.startActivity(intent);
			Toast.makeText(context, "您还没有安装QQ空间客户端", Toast.LENGTH_SHORT).show();
		} else {
			// 通过客户端分享
			shareThroughClient(context, resolveInfo, contentStr, shareUrl);
		}
	}
	
	
	/**
	 * 发送短信
	 */
	public static void sendSMS_Share(Context context, String contentStr) {
		Uri smsToUri = Uri.parse("smsto:");
		Intent intent = new Intent(android.content.Intent.ACTION_SENDTO,
				smsToUri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("sms_body", contentStr);
		context.startActivity(intent);
	}
	
	/**
	 * 初始化检查
	 * @param context
	 * @param packageName
	 * @return
	 */
	private static boolean check(final Context context,
			String packageName) {
		boolean flag = isAppInstalled(context, packageName);
		if(flag){
			resolveInfo = getShareTarget(context, packageName);
		}
		return flag;
	}

	/**
	 * 从客户端分享
	 * 
	 * @param _mResolveInfo
	 */
	private static void shareThroughClient(Context context,
			ResolveInfo _mResolveInfo, String contentStr, String shareUrl) {
		ResolveInfo launchable = _mResolveInfo;
		ActivityInfo activity = launchable.activityInfo;
		ComponentName name = new ComponentName(
				activity.applicationInfo.packageName, activity.name);
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setComponent(name);
		i.setType("*/*");
		if (activity.applicationInfo.packageName.contains("tencent.WBlog")) {
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		} else {
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			i.addCategory(Intent.CATEGORY_LAUNCHER);
		}
		i.putExtra(Intent.EXTRA_TEXT, contentStr + shareUrl);
		context.startActivity(i);
	}

	/**
	 * 获取分享列表
	 * 
	 * @return List<ResolveInfo>
	 * */
	private static ResolveInfo getShareTarget(Context context,
			String packageName) {
		List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
		Intent intent = new Intent(Intent.ACTION_SEND, null);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		PackageManager pm = context.getPackageManager();
		mApps = pm.queryIntentActivities(intent,
				PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);

		for (int i = 0; i < mApps.size(); i++) {
			if (packageName.equals(mApps.get(i).activityInfo.packageName)) {
				return mApps.get(i);
			}
		}
		return null;
	}

	/**
	 * 检测某个应用是否安装
	 * 
	 * @param context
	 * @param pkgName
	 * @return
	 */
	private static boolean isAppInstalled(Context context, String packageName) {
		try {
			context.getPackageManager().getPackageInfo(packageName, 0);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
}
