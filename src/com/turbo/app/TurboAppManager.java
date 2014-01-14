package com.turbo.app;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;

/**
 * Activity Service Receiver 管理类
 * 
 * @author Ted
 */
public class TurboAppManager {
	private static TurboAppManager instance = null;
	private static Stack<Object> stack = new Stack<Object>();

	private TurboAppManager() {
	}

	public static TurboAppManager newInstance() {
		if (instance == null)
			instance = new TurboAppManager();
		return instance;
	}

	/**
	 * 将Context组件加入管理（Activity或Service）
	 * 
	 * @param context
	 */
	public void add(Object obj) {
		if (stack == null) {
			stack = new Stack<Object>();
		}
		if (obj != null) {
			stack.push(obj);
		}
	}

	/**
	 * 结束Context组件
	 * 
	 * @param context
	 */
	public void finish(Object obj) {
		if (obj instanceof Activity)
			finishActivity((Activity) obj);
		else if (obj instanceof Service)
			finishService((Service) obj);
	}

	/**
	 * 结束指定类名的Service
	 * 
	 * @param clazz
	 */
	public void finishService(Class<?> clazz) {
		for (Object obj : stack) {
			Service service = (Service) obj;
			if (service.getClass().equals(clazz))
				finishService(service);
		}
	}

	/**
	 * 结束指定类名的Activity
	 * 
	 * @param clazz
	 */
	public void finishActivity(Class<?> clazz) {
		for (Object obj : stack) {
			Activity activity = (Activity) obj;
			if (activity.getClass().equals(clazz))
				finishActivity(activity);
		}
	}

	/**
	 * 取得栈顶的Activity
	 * 
	 * @return
	 */
	public Activity getCurrentActivity() {
		Activity activity = null;
		if (stack != null) {
			for (Object obj : stack) {
				if (obj != null && obj instanceof Activity) {
					activity = (Activity) obj;
					stack.remove(obj);
					break;
				}
			}
		}
		return activity;
	}

	/**
	 * 获取栈顶的Service
	 * 
	 * @return
	 */
	public Service getCurrentService() {
		Service service = null;
		if (stack != null) {
			for (Object obj : stack) {
				if (obj != null && obj instanceof Service) {
					service = (Service) obj;
					stack.remove(obj);
					break;
				}
			}
		}
		return service;
	}

	/**
	 * 结束特定的Service
	 * 
	 * @param service
	 */
	private void finishService(Service service) {
		if (service != null) {
			stack.remove(service);
			service.stopSelf();
			service = null;
		}
	}

	/**
	 * 结束特定的Activity
	 * 
	 * @param activity
	 */
	private void finishActivity(Activity activity) {
		if (activity != null) {
			stack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束所有的Activity和Service
	 */
	public void finishAll() {
		for (Object obj : stack) {
			if (null != obj) {
				finish(obj);
			}
		}
		stack.clear();
	}

	/**
	 * 退出程序
	 */
	public void exitApp() {
		try {
			finishAll();
			Context context = TurboBaseApp.getAppContext();
			ActivityManager am = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			am.killBackgroundProcesses(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
		}
	}

	// /**
	// * 注销监听器
	// * @param context
	// * @param receiver
	// */
	// private void finishReceiver(Context context,BroadcastReceiver receiver){
	// if(receiver != null){
	// stack.remove(receiver);
	// context.unregisterReceiver(receiver);
	// receiver = null;
	// }
	// }
}
