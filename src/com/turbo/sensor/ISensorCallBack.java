package com.turbo.sensor;

import android.hardware.SensorEvent;

/**
 * 传感器回调函数
 * @author Ted
 * @date 2013-06-04
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public interface ISensorCallBack {

	/** 传感器发生变化时回调*/
	public void onSensorChangedCallBack();
	
	/**
	 * 用户自定义回调
	 * @param event
	 */
	public void userDefinCallBack(SensorEvent event);
	
}
