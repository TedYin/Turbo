package com.turbo.app;

import android.app.Application;
import android.content.Context;

import com.turbo.net.VolleyNetHelper;

public class TurboBaseApp extends Application {
	private static Context appContext;
	private static VolleyNetHelper helper;

	@Override
	public void onCreate() {
		super.onCreate();
		appContext = this;
		helper = VolleyNetHelper.newInstance(appContext);
	}

	/**
	 * 获取全局Context
	 * @return
	 */
	public static Context getAppContext() {
		return appContext;
	}
	
	/**
	 * 获取网络助手类
	 * @return
	 */
	public static VolleyNetHelper getNetHellper() {
		try {
			if (helper == null)
				throw new Exception("VolleyNetHelper还未初始化！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return helper;
	}
}
