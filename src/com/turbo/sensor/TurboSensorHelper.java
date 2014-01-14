package com.turbo.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * 手机硬件帮助类
 * 
 * @author Ted
 * @date 2013-06-04
 */
public class TurboSensorHelper {
	
	/**
	 * 动作种类
	 * @author Ted
	 * @date 2013-06-04
	 */
	public interface ActionType {
		/** 自定义操作 */
		public static final int ACTION_USERS = 0x0;

		/** 摇一摇功能 */
		public static final int ACTION_SHAKE = 0x1;

		/** 将手机向前甩出 */
		public static final int ACTION_SHAKE_HEAD = 0x2;
	}

	
	/********************************************************	
	 ***************	加速度传感器模块  	********************* 		
	 *********************************************************/
	
	
	/**
	 * 添加摇一摇功能
	 * 
	 * @param context
	 */
	public static void addShakeModel(Context context,ISensorCallBack callBack){
		addAcceleratorL(context, callBack, ActionType.ACTION_SHAKE);
	}
	
	/**
	 * 添加向前甩出功能
	 * @param context
	 * @param callBack
	 */
	public static void addShakeHeadModel(Context context, ISensorCallBack callBack){
		addAcceleratorL(context, callBack, ActionType.ACTION_SHAKE_HEAD);
	}

	/**
	 * 使用加速的传感器监听
	 * @param context
	 * @param callBack
	 * @param actionType
	 */
	private static void addAcceleratorL(Context context, ISensorCallBack callBack, int actionType){
		SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sm.registerListener(new AcceleratorListener(callBack, actionType), sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
		
	/********************************************************	
	 ***************	温度传感器模块 	  	********************* 		
	 *********************************************************/

	
	
	/********************************************************	
	 ***************	光线传感器模块	  	********************* 		
	 *********************************************************/
	
	
	
	/********************************************************	
	 ***************	距离传感器模块  	  	********************* 		
	 *********************************************************/
}
