package com.turbo.common;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;

/**
 * 封装对URL的拼装等操作
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class URLHelper {
	/**
	 * 组装URL
	 * @param url
	 * @param params
	 * @return
	 */
	public static String buildUrl(String url, Map<String, String> params) {
		StringBuilder urlStr = new StringBuilder(url);
		if (url.indexOf('?') < 0) {
			urlStr.append('?');
		}

		try {
			for (String name : params.keySet()) {
				urlStr.append("&");
				urlStr.append(name);
				urlStr.append("=");
				urlStr.append(URLEncoder.encode(
						String.valueOf(params.get(name)), HTTP.UTF_8));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return urlStr.toString().replace("?&", "?");
	}
	
	/**
	 * 设置HttpURLConnectionRequest的属性信息
	 * @param conn
	 * @param params
	 * @return
	 */
	public static HttpURLConnection buildConnectionParams(HttpURLConnection conn, Map<String, String> params){
		if(params == null) return conn;
		for (String field : params.keySet()) {
			conn.addRequestProperty(field, params.get(field));
		}
		return conn;
	}
	
	/**
	 * 为HttpGet添加Header
	 * @param httpGet
	 * @param headerMap
	 * @return
	 */
	public static HttpGet addHttpGetHeader(HttpGet httpGet,Map<String, String> headerMap){
		if(headerMap == null) return httpGet;
		for(String key: headerMap.keySet()){
			httpGet.addHeader(key, headerMap.get(key));
		}
		return httpGet;
	}
	
	/**
	 * 为HttpPost添加Header
	 * @param httpPost
	 * @param headerMap
	 * @return
	 */
	public static HttpPost addHttpPostHeader(HttpPost httpPost,Map<String, String> headerMap){
		if(headerMap == null) return httpPost;
		for(String key: headerMap.keySet()){
			httpPost.addHeader(key, headerMap.get(key));
		}
		return httpPost;
	}

}
