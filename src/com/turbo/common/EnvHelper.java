package com.turbo.common;

import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

/**
 * 手机状态工具类
 * @author Ted
 * @date 2013-04-06
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class EnvHelper {
	/**
	 * 有如下功能： SD卡是否挂载检查（实现） 手机音量是否正常 摄像头是否可用 是否处于听筒模式（实现）
	 * 是否插耳机（耳机插拔动作系统会发出广播来通知，只需实现一个Receiver来监听系统广播即可） <action
	 * android:name="android.intent.action.HEADSET_PLUG"
	 * android:enabled="true"></action> 是移动网络还是WIFI网络（实现） 是否有Root权限
	 */

	/**
	 * 网络类型
	 * @author Ted
	 * @date 2013-04-06
	 */
	public static enum NetType {
		NO_NET,			//无网络 
		WIFI, 			//WIFI网络  
		WAP, 			//Wap网络 
		NET,			//Net网络
		MOBILE,			//移动网络
		
		/* 注: 对于移动网络，如果是Net网络则可直接使用，如果是Wap网络则只能使用Http协议类型
		 * 需要通过设置代理来使得在Wap网络下可以使用多种网络协议
		 * */
	};

	/**
	 * 获取当前的网络类型
	 * 
	 * @param context
	 * @return
	 */
	public static NetType getNetworkType(Context context) {
		NetType netType = NetType.NO_NET; 
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo == null)
			return netType = NetType.NO_NET;

		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			netType = NetType.MOBILE;
			if (networkInfo.getExtraInfo().toLowerCase().contains("wap")) {
				netType = NetType.WAP;
			} else {
				netType = NetType.NET;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NetType.WIFI;
		}
		return netType;
	}
	
	/**
	 * 是否挂载了sdcard
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 检测当前系统声音是否为正常模式
	 * 
	 * @return
	 */
	public static boolean isAudioNormal(Context context) {
		AudioManager mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
	}
}