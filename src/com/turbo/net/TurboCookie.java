package com.turbo.net;

import java.util.Date;

import org.apache.http.cookie.Cookie;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Cookie信息封装类
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class TurboCookie{

	private Cookie cookie;
	
	
	private int version; 
	private String name;
	private String value;
	private String domain;
	private String path;
	private Date expiry;
	
	private TurboCookie(){}
	public TurboCookie(Cookie cookie){
		this.cookie = cookie;
		initCookie();
	}
	
	private void initCookie() {
		this.version = cookie.getVersion();
		this.name = cookie.getName();
		this.value = cookie.getValue();
		this.domain = cookie.getDomain();
		this.path = cookie.getPath();
		this.expiry = cookie.getExpiryDate();
	}
	@Override
	public String toString() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("version",version);
			obj.put("name", name);
			obj.put("value", value);
			obj.put("domain", domain);
			obj.put("path", path);
			obj.put("expiry", expiry);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
	}
	
	public static TurboCookie parse(String jsonStr){
		TurboCookie cookie = new TurboCookie();
		try {
			JSONObject obj = new JSONObject(jsonStr);
			if(!obj.has("version"))return null;
			if(!obj.has("name"))return null;
			if(!obj.has("value"))return null;
			if(!obj.has("domain"))return null;
			if(!obj.has("domain"))return null;
			if(!obj.has("expiry"))return null;
			cookie.version = obj.getInt("version");
			cookie.name = obj.getString("name");
			cookie.value = obj.getString("value");
			cookie.domain = obj.getString("domain");
			cookie.path = obj.getString("path");
			cookie.expiry = (Date) obj.get("expiry");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return cookie;
	}
	
}
