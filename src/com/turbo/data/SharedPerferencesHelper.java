package com.turbo.data;

import com.turbo.app.TurboBaseApp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPerferences助手
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class SharedPerferencesHelper {
	
	private static final String PERFERENCE_FILE_NAEME = "DEFAULT";
	private static SharedPreferences perferences;
	private static SharedPreferences.Editor edit;

	private static class Holder {
		private static final SharedPreferences perferences = TurboBaseApp.getAppContext()
				.getSharedPreferences(PERFERENCE_FILE_NAEME,
						Context.MODE_PRIVATE);
		private static final SharedPreferences.Editor edit = perferences.edit();

		private static final SharedPerferencesHelper helper = new SharedPerferencesHelper();
	}

	private SharedPerferencesHelper() {
	}

	/**
	 * 获得Helper对象并对SharedPerferences对象和Editor对象进行初始化
	 * 
	 * @return
	 */
	public static SharedPerferencesHelper newInstance() {
		perferences = Holder.perferences;
		edit = Holder.edit;
		return Holder.helper;
	}
	
	/**
	 * 写入boolean
	 * @param context
	 * @param key
	 * @param value
	 */
	public void writeBoolean(String key, boolean value) {
		edit.putBoolean(key, value);
		edit.commit();
	}

	/**
	 * 读取boolean
	 * 
	 * @param context
	 * @param key
	 */
	public boolean readBoolean(String key) {
		return perferences.getBoolean(key, false);
	}

	/**
	 * 写入String
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public void writeString(String key, String value) {
		edit.putString(key, value);
		edit.commit();
	}

	/**
	 * 读取String
	 * 
	 * @param context
	 * @param key
	 */
	public String readString(String key) {
		return perferences.getString(key, "");
	}

	/**
	 * 写入Long
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public void writeLong(String key, long value) {
		edit.putLong(key, value);
		edit.commit();
	}

	/**
	 * 读取Long
	 * 
	 * @param context
	 * @param key
	 */
	public long readLong(String key) {
		return perferences.getLong(key, 0);
	}

	/**
	 * 写入int
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public void writeInt(String key, int value) {
		edit.putInt(key, value);
		edit.commit();
	}

	/**
	 * 读取int
	 * 
	 * @param context
	 * @param key
	 */
	public int readInt(String key) {
		return perferences.getInt(key, 0);
	}

}
