//package com.turbo.other;
//
//import android.app.Activity;
//import android.content.Context;
//import android.util.Log;
//
//import com.turbo.common.CommonUtils;
//
//import org.apache.http.HttpStatus;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.RandomAccessFile;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 网络请求类
// * */
//public class NetRequest {
//
//	public static interface DownloadCallback {
//		void onProgress(long current, long fileTotalSize);
//
//		void onFinish(String filePath);
//
//		void onError(int code, String msg, Exception e);
//	}
//
//	public static interface NetRequestCallback {
//		void onSucc(Map<String, Object> data);
//
//		void onError(int code, String msg, Exception e);
//	}
//
//	private final static String TAG = "NetRequest";
//
//	public static void doGetWithoutResponse(String url) {
//		HttpGet httpget = new HttpGet(url);
//		HttpClient client = new DefaultHttpClient();
//		try {
//			client.execute(httpget);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static String getStringFromNet(String urlStr, Context _context) {
//
//		URL url = null;
//		HttpURLConnection connection = null;
//		InputStreamReader in = null;
//		try {
//			url = new URL(Utils.appendUrlWithCustomProperty(urlStr, _context));
//			connection = (HttpURLConnection) url.openConnection();
//			in = new InputStreamReader(connection.getInputStream());
//			BufferedReader bufferedReader = new BufferedReader(in);
//			StringBuffer strBuffer = new StringBuffer();
//			String line = null;
//			while ((line = bufferedReader.readLine()) != null) {
//				strBuffer.append(line);
//			}
//			return strBuffer.toString();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		} finally {
//			if (connection != null) {
//				connection.disconnect();
//			}
//			if (in != null) {
//				try {
//					in.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//
//	/**
//	 * 向服务器请求更新数据
//	 */
//	public static NetResult getUpdateDataRequest(Context context,
//			String apiDomain) {
//		// 如果网络不可用
//		if (!MobileFunction.isNetworkAvailable(context)) {
//			return null;
//		}
//
//		String result = getStringFromNet(apiDomain + "/webApi/checkVersion.do",
//				context);
//
//		try {
//			JSONObject js = new JSONObject(result);
//			if (js.has("rc")) {
//				NetResult netResult = new NetResult();
//				netResult.setRc(js.getInt("rc"));
//				if (js.has("msg")) {
//					netResult.setMsg(js.getString("msg"));
//				}
//				if (js.has("dt")) {
//					netResult.setDt(js.getString("dt"));
//				}
//				return netResult;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
//
////	/**
////	 * 比较客户端和服务器中静态文件的版本信息，返回给客户端静态文件增量包的下载地址。如果最新的静态文件包是测试版，则只有在白名单中的用户可以。
////	 * 
////	 * @param context
////	 * @param apiDomain
////	 * @return
////	 */
////	public static NetResult checkStaticFilesRequest(Context context,
////			String apiDomain) {
////		// 如果网络不可用
////		if (!MobileFunction.isNetworkAvailable(context)) {
////			return null;
////		}
////
////		String path = context.getFilesDir() + "/script/static_data";
////		String clSign = SignatureUtils.computeSign(path);
////		// TODO 此处请求需要将静态数值表的版本id带上
////		String result = getStringFromNet(apiDomain
////				+ "/webApi/checkStaticFiles.do?clSign=" + clSign + "&ts="
////				+ System.currentTimeMillis(), context);
////		try {
////			JSONObject js = new JSONObject(result);
////			if (js.has("rc")) {
////				NetResult netResult = new NetResult();
////				netResult.setRc(js.getInt("rc"));
////				if (js.has("msg")) {
////					netResult.setMsg(js.getString("msg"));
////				}
////				if (js.has("dt")) {
////					netResult.setDt(js.getString("dt"));
////				}
////				return netResult;
////			}
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////		return null;
////	}
//
//	/**
//	 * 向服务器请求时间戳
//	 */
//	public static void getTimeDataRequest(Context context,
//			NetRequestCallback netRequestCallback, String apiDomain) {
//		// 如果网络不可用
//		if (!CommonUtils.isNetworkAvailable(context)) {
//			netRequestCallback.onError(1, "网络不可用", null);
//			return;
//		}
//		try {
//			String result = getStringFromNet(apiDomain
//					+ "/webApi/getSysTime.do", context);
//
//			JSONObject js = new JSONObject(result);
//			int rc = js.getInt("rc");
//			JSONObject so = js.getJSONObject("dt");
//			if (rc == 1000 && so != null) {
//				HashMap<String, Object> data = new HashMap<String, Object>();
//				long time = so.getLong("t");
//				data.put("time", time);
//				netRequestCallback.onSucc(data);
//			} else {
//				netRequestCallback.onError(2, "服务器返回错误码 rc=" + rc, null);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			netRequestCallback.onError(3, "网络请求，或网络响应数据解析发生异常", e);
//		}
//
//	}
//
//	/**
//	 * 向服务器请求服务器列表
//	 */
//	public static void getServerListDataRequest(Context context,
//			NetRequestCallback netRequestCallback, String apiDomain) {
//		// 如果网络不可用
//		if (!MobileFunction.isNetworkAvailable(context)) {
//			netRequestCallback.onError(1, "网络不可用", null);
//			return;
//		}
//
//		try {
//			String result = getStringFromNet(apiDomain
//					+ "/webApi/getServerList.do", context);
//
//			JSONObject js = new JSONObject(result);
//			JSONArray so = js.getJSONArray("sl");
//			if (so != null) {
//				Map<String, Object> data = new HashMap<String, Object>();
//				data.put("serverlist", result);
//				netRequestCallback.onSucc(data);
//			} else {
//				netRequestCallback.onError(2, "服务器返回误" + result, null);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			netRequestCallback.onError(3, "网络请求，或网络响应数据解析发生异常", e);
//		}
//	}
//
//	/**
//	 * 登录
//	 * 
//	 * |token|string|平台用户中心验证数据| |partnerId|string|合作平台ID|
//	 * |serverId|string|服务器ID| |timestamp|long|时间戳| |sign|stirng|签名|
//	 * 
//	 * !!sign算法 md5(token + partnerId + serverId + timestamp + key)
//	 */
//	public static void LoginCenterRequest(Context context,
//			NetRequestCallback netRequestCallback, String apiDomain,
//			String token, String partnerId, String serverId, long timestamp,
//			String sign) {
//		// 如果网络不可用
//		if (!MobileFunction.isNetworkAvailable(context)) {
//			netRequestCallback.onError(1, "网络不可用", null);
//			return;
//		}
//
//		StringBuffer url_str = new StringBuffer(apiDomain + "/webApi/login.do");
//		url_str.append("?token=" + token).append("&partnerId=" + partnerId)
//				.append("&serverId=" + serverId)
//				.append("&timestamp=" + timestamp).append("&sign=" + sign);
//		// Log.e("login",url_str.toString());
//
//		try {
//			String result = getStringFromNet(url_str.toString(), context);
//
//			JSONObject js = new JSONObject(result);
//
//			int rc = -1;
//			if (js.has("rc")) {
//				rc = js.getInt("rc");
//			} else {
//				Log.e(TAG, "url=" + url_str.toString() + "\nresult=" + result);
//			}
//			if (rc == 1000) {
//				JSONObject so = js.getJSONObject("dt");
//
//				HashMap<String, Object> data = new HashMap<String, Object>();
//				data.put("rc", rc);
//				data.put("data", so);
//				netRequestCallback.onSucc(data);
//			} else {
//				netRequestCallback.onError(2, "服务器返回数据有误  rc=" + rc, null);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			netRequestCallback.onError(3, "网络请求，或网络响应数据解析发生异常", e);
//		}
//	}
//
//	/**
//	 * 下载文件，支持断点续传
//	 * 
//	 * @param bean
//	 */
//	public static void download(final Activity activity, String updateUrl,
//			String downloadFilePath, DownloadCallback downloadCallback) {
//
//		if (updateUrl == null || updateUrl.length() <= 0) {
//			downloadCallback.onError(1, "updateUrl 不能为空", null);
//			return;
//		}
//		// 因为服务器问题做的特殊处理
//		updateUrl = updateUrl + "?" + Utils.getUUID();
//
//		HttpURLConnection conn = null;
//		RandomAccessFile fos = null;
//		InputStream inputStream = null;
//		long fileTotalSize = 0L;
//
//		try {
//			File downloadFile = new File(downloadFilePath);
//			long curPositionInWholeFile = 0L;
//			if (downloadFile.exists()) {
//				curPositionInWholeFile = downloadFile.length();
//			} else {
//				File dir = downloadFile.getParentFile();
//				if (!dir.exists()) {
//					dir.mkdirs();
//				}
//				downloadFile.createNewFile();
//			}
//
//			fileTotalSize = getFileTotalSizeFromUrl(updateUrl);
//
//			if (curPositionInWholeFile > 0
//					&& curPositionInWholeFile == fileTotalSize) {
//				downloadCallback.onFinish(downloadFile.getAbsolutePath());
//
//				return;
//			}
//			if (curPositionInWholeFile > fileTotalSize) {
//				curPositionInWholeFile = 0;
//				downloadFile.delete();
//				downloadFile.createNewFile();
//			}
//			URL url = new URL(updateUrl);
//			conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestProperty("Range", "bytes=" + curPositionInWholeFile
//					+ "-");
//			conn.setReadTimeout(10000);
//			conn.connect();
//			int responseCode = conn.getResponseCode();
//			if (responseCode == HttpStatus.SC_OK
//					|| responseCode == HttpStatus.SC_PARTIAL_CONTENT) {
//				fos = new RandomAccessFile(downloadFile, "rw");
//				fos.seek(curPositionInWholeFile);
//
//				inputStream = conn.getInputStream();
//				int len = -1;
//				long current = 0;
//				byte[] bytes = new byte[1024];
//
//				// 如果有断点，则从断点处开始计算
//				if (curPositionInWholeFile > 0) {
//					current = curPositionInWholeFile;
//				}
//
//				while ((len = inputStream.read(bytes)) != -1) {
//					fos.write(bytes, 0, len);
//					current += len;
//
//					/*** 通知栏显示更新 ***/
//
//					ProgressNotifition.sendNotifitionMessage(activity,
//							fileTotalSize, current,downloadFilePath);
//
//					/*****/
//
//					// 通知下载进度
//					downloadCallback.onProgress(current, fileTotalSize);
//				}
//				if (current == fileTotalSize)
//					downloadCallback.onFinish(downloadFile.getAbsolutePath());
//				return;
//			} else {
//				downloadCallback.onError(3, "服务器响应错误码：HttpStatus="
//						+ responseCode, null);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			downloadCallback.onError(2, "下载过程中发生异常", e);
//		} finally {
//			try {
//				if (inputStream != null)
//					inputStream.close();
//				if (conn != null)
//					conn.disconnect();
//				if (fos != null)
//					fos.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	private static long getFileTotalSizeFromUrl(String updateUrl)
//			throws IOException {
//		if (updateUrl == null) {
//			return 0;
//		}
//		HttpURLConnection conn = null;
//
//		try {
//			URL url = new URL(updateUrl);
//			conn = (HttpURLConnection) url.openConnection();
//			conn.setReadTimeout(5000);
//			conn.connect();
//			int responseCode = conn.getResponseCode();
//			if (responseCode == HttpStatus.SC_OK) {
//				return conn.getContentLength();
//			}
//		} finally {
//			if (conn != null) {
//				conn.disconnect();
//				conn = null;
//			}
//		}
//		return 0;
//	}
//}
