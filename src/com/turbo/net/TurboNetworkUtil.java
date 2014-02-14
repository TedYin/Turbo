package com.turbo.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.turbo.app.TurboBaseApp;
import com.turbo.common.EnvHelper;

/**
 * 网络模块工具类
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class TurboNetworkUtil {
	
	/** 静态量*/
	public static final int METHOD_GET = 0;
	public static final int METHOD_POST = 1;
	
	
	
	private static DefaultHttpClient httpClient = null;
	/**
	 * 获取自定义HttpClient
	 * @return
	 */
	public static DefaultHttpClient getHttpClient(){
		if (null == httpClient) {
			HttpParams params = new BasicHttpParams();
			// 设置一些基本参数
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams
					.setUserAgent(
							params,
							"Mozilla/5.0(Linux;U;Android 3.0+;en-us;Turbo) "
									+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
			// 超时设置
			/* 从连接池中取连接的超时时间 */
			ConnManagerParams.setTimeout(params, 1000);
			/* 连接超时 */
			int ConnectionTimeOut = 3000;
			if (EnvHelper.getNetworkType(TurboBaseApp.getAppContext()) == EnvHelper.NetType.WIFI) {
				ConnectionTimeOut = 10000;
			}
			HttpConnectionParams
					.setConnectionTimeout(params, ConnectionTimeOut);
			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(params, 4000);
			// 设置我们的HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			// 使用线程安全的连接管理来创建HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			httpClient = new DefaultHttpClient(conMgr, params);
		}
		return httpClient;
	}
	
	/**
	 * 获取消息体
	 * @param resp
	 * @return String 消息体
	 */
	public static String getHttpResponseBody(HttpResponse resp){
		int status = resp.getStatusLine().getStatusCode();
		String result = "";
		if(status == 200){
			try {
				result = EntityUtils.toString(resp.getEntity());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			result = "Error : "+status;
		}
		return result;
	}
	
	/**
	 * 获取返回消息头
	 * @param resp
	 * @return 200: map 否则返回 null
	 */
	public static Map<String, String> getHttpResponseHeader(HttpResponse resp){
		int status = resp.getStatusLine().getStatusCode();
		Map<String, String> headers = null;
		if(status == 200){
			headers = new HashMap<String, String>();
			Header[] allHeader = resp.getAllHeaders();
			for(int i = 0; i < allHeader.length; i++){
				headers.put(allHeader[i].getName(), allHeader[i].getValue());
			}
		}
		return headers;
	}
}
