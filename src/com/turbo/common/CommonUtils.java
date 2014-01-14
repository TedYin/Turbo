package com.turbo.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;

import com.turbo.data.PropertiesHelper;
import com.turbo.data.StringHelper;

/**
 * 常用工具类
 * 
 * @author Ted
 * @since 2013-03-04
 */
public final class CommonUtils {

	private CommonUtils() throws Exception {
		throw new Exception("禁止实例化！");
	}

	/**
	 * 获取未安装的APK信息
	 * 
	 * @param context
	 * @param archiveFilePath
	 *            APK文件的路径
	 */
	public static PackageInfo getApkInfo(Context context, String archiveFilePath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo apkInfo = pm.getPackageArchiveInfo(archiveFilePath,
				PackageManager.GET_META_DATA);
		return apkInfo;
	}

	/**
	 * 安装APK文件
	 * 
	 * @param apkFilePath
	 * @param context
	 */
	public static void installApk(String apkFilePath, Context context) {
		File apkFile = new File(apkFilePath);
		if (!apkFile.exists()) {
			return;
		}
		if (apkFile.toString().contains(context.getFilesDir().toString())) {
			// 如果文件是在手机内存中，则将安装包剪贴到cache目录，再执行安装
			apkFile.renameTo(new File(context.getCacheDir(), UUID.randomUUID()
					+ ".apk"));
			apkFilePath = apkFile.getAbsolutePath();

			// 修改文件夹权限
			String[] args1 = { "chmod", "705", apkFile.getParent() };
			exeCmd(args1);
			// [文件604:-rw----r--]
			String[] args2 = { "chmod", "604", apkFile.getAbsolutePath() };
			exeCmd(args2);
		}

		if (CommonUtils.getApkInfo(context, apkFilePath) != null) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			intent.setDataAndType(Uri.parse("file://" + apkFilePath),
					"application/vnd.android.package-archive");
			context.startActivity(intent);
		} else {
			apkFile.delete();
			Log.e("CommonUtils", "文件损坏，重新更新");
		}
	}

	/**
	 * 检测某个应用是否安装
	 * 
	 * @param context
	 * @param pkgName
	 * @return
	 */
	public static boolean isAppInstalled(Context context, String packageName) {
		try {
			context.getPackageManager().getPackageInfo(packageName, 0);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * 卸载指定应用
	 * 
	 * @param context
	 * @param appPackage
	 */
	public static void uninstallApp(Activity activity, String packageName) {
		int UNINSTALL_REQUEST_CODE = 0x1;
		Uri uri = Uri.parse("package:" + packageName);
		Intent deleteIntent = new Intent();
		deleteIntent.setType(Intent.ACTION_DELETE);
		deleteIntent.setData(uri);
		activity.startActivityForResult(deleteIntent,UNINSTALL_REQUEST_CODE);
	}

	/**
	 * 执行命令行
	 * 
	 * @param args
	 * @return
	 */
	public static String exeCmd(String[] args) {
		String result = "";
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			process = processBuilder.start();
			errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			baos.write('\n');
			inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}
			byte[] data = baos.toByteArray();
			result = new String(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (errIs != null) {
					errIs.close();
				}
				if (inIs != null) {
					inIs.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return result;
	}

	/**
	 * 获取屏幕分辨率
	 * 
	 * @param _windowManager
	 * @return
	 */
	public static int[] readWindowMetrics(WindowManager _windowManager) {
		WindowManager manager = _windowManager;
		int hight = manager.getDefaultDisplay().getHeight();
		// 屏幕的高度
		int width = manager.getDefaultDisplay().getWidth();
		int screen_wh[] = new int[2];
		screen_wh[0] = hight;
		screen_wh[1] = width;
		return screen_wh;
	}

	/**
	 * 启动指定的应用
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void startApp(Context context, String packageName) {
		PackageManager packageManager = context.getPackageManager();
		Intent intent = new Intent();
		intent = packageManager.getLaunchIntentForPackage(packageName);
		context.startActivity(intent);
	}

	/**
	 * dip 转化为 px
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static float dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (dipValue * scale + 0.5f);
	}

	/**
	 * px 转化为 dip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static float px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (pxValue / scale + 0.5f);
	}

	/**
	 * 获取VersionName
	 * 
	 * @return
	 */
	public static String getVersionName(Context context) {
		try {
			PackageInfo pkg = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pkg.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取VersionCode
	 * 
	 * @return
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageInfo pkg = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pkg.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取渠道号Channel Id（存在Mainfest文件中）
	 * 
	 * @param qnKey
	 * @return
	 */
	public static String getChannelId(String key, Context context) {
		ApplicationInfo info;
		try {
			info = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			String msg = info.metaData.getString(key);
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	

	/**
	 * 判断当前网络是否可用
	 * */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		// return ni==null? false : ni.isConnectedOrConnecting(); //下面的写法更优秀
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取Android版本号
	 * */
	public static int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 获取手机IMSI号
	 * */
	public static String getIMSI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = tm.getSubscriberId();
		if (imsi == null)
			imsi = "";
		return imsi;
	}

	/**
	 * 获取手机串号（设备ID）
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = manager.getDeviceId();
		if (imei == null)
			imei = "";
		return imei;
	}

	/**
	 * 判断当前版本是否兼容目标版本的方法
	 * 
	 * @param versionCode
	 *            是应用所支持的版本号
	 * @return
	 */
	public static boolean isMethodsCompat(int versionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= versionCode;
	}

	/**
	 * 获取系统所安装的所有应用信息
	 * 
	 * @param context
	 * @return 返回的结果为HashMap<包名，应用名>
	 */
	public static Map<String, String> getAllAppPacakges(Context context) {
		HashMap<String, String> pakageInfoMap = new HashMap<String, String>();
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> packages = packageManager
				.getInstalledPackages(PackageManager.GET_GIDS);
		PackageInfo packageInfo = null;
		for (int i = 0; i < packages.size(); i++) {
			// flags > 0 是系统预装的应用, flags <= 0是非系统预装的应用
			packageInfo = packages.get(i);
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				continue;// 如果是系统应用，则继续循环
			}
			pakageInfoMap.put(packageInfo.packageName,
					packageInfo.applicationInfo.loadLabel(packageManager)
							.toString());
		}
		return pakageInfoMap;
	}

	/**
	 * 获取已经安装应用信息
	 * 
	 * @return
	 */
	public static ArrayList<AppBean> getAppList(Context context) {
		ArrayList<AppBean> appList = new ArrayList<AppBean>();
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> packages = packageManager
				.getInstalledPackages(PackageManager.GET_GIDS);
		PackageInfo packageInfo = null;
		for (int i = 0; i < packages.size(); i++) {
			// flags > 0 是系统预装的应用, flags <= 0是非系统预装的应用
			packageInfo = packages.get(i);
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				continue;// 如果是系统应用，则继续循环
			}
			AppBean bean = new AppBean();
			bean.setPkgName(packageInfo.packageName);
			bean.setAppName(packageInfo.applicationInfo.loadLabel(
					packageManager).toString());
			appList.add(bean);
		}
		return appList;
	}

	/**
	 * 获取App唯一标识
	 * 
	 * @return
	 */
	public static String getUniqueAppId() {
		// 先从properties配置文件里面去取，如果取不到则重新生成，并更新配置文件
		String uniqueId = PropertiesHelper
				.getPropertiesValue("getUniqueId");
		if (StringHelper.isEmpty(uniqueId)) {
			uniqueId = UUID.randomUUID().toString();
			PropertiesHelper
					.setPropertiesVale("getUniqueId", uniqueId);
		}
		return UUID.randomUUID().toString();
	}

	/**
	 * App 信息
	 */
	protected static class AppBean implements Serializable {

		private static final long serialVersionUID = 201303040303L;
		private String pkgName;
		private String appName;
		private String versionName;
		private int versionCode;

		public String getVersionName() {
			return versionName;
		}

		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}

		public int getVersionCode() {
			return versionCode;
		}

		public void setVersionCode(int versionCode) {
			this.versionCode = versionCode;
		}

		public String getPkgName() {
			return pkgName;
		}

		public void setPkgName(String pkgName) {
			this.pkgName = pkgName;
		}

		public String getAppName() {
			return appName;
		}

		public void setAppName(String appName) {
			this.appName = appName;
		}
	}

	/**
	 * 获取当前Apk信息
	 * @param context
	 * @return
	 */
	public static PackageInfo getCurrentPackageInfo(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取SD卡剩余空间
	 */
	public static long getRealSizeOnSdcard() {
		File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}
	
	/**
	 * 获取手机存储剩余空间
	 */
	public static long getRealSizeOnPhone() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		long realSize = blockSize * availableBlocks;
		return realSize;
	}


}
