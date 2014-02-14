package com.turbo.app;

import android.app.Application;
import android.content.Context;

import com.turbo.net.TurboNetHelper;

/**
 * Application基础类，应用必须以该类为Application类
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
/* 该类包括了Turbo的一些初始化操作*/
public class TurboBaseApp extends Application {
	private static Context appContext;
	private static TurboNetHelper helper;

	@Override
	public void onCreate() {
		super.onCreate();
		appContext = this;
		helper = TurboNetHelper.newInstance(appContext);
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
	public static TurboNetHelper getNetHellper() {
		try {
			if (helper == null)
				throw new Exception("VolleyNetHelper还未初始化！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return helper;
	}
}
