package com.turbo.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.turbo.sensor.TurboSensorHelper.ActionType;

/**
 * 重力加速感应事件监听器
 * @author Ted
 * @date 2013-06-04
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class AcceleratorListener implements SensorEventListener {
	
	private final int DOTA = 14;      		//加速度传感器阈值
	private ISensorCallBack callBack;		//回调函数
	private int actionType;					//动作类型
	private float x = 0, y = 0, z = 0;		//传感器的三轴

	public AcceleratorListener(ISensorCallBack callBack, int actionType) {
		this.callBack = callBack;
		this.actionType = actionType;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// 当传感器精度改变时回调该方法
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// values[0]:X轴，values[1]：Y轴，values[2]：Z轴
		x = event.values[0];
		y = event.values[1];
		z = event.values[2];

		switch (actionType) {
		case ActionType.ACTION_SHAKE:
			shakePhone();
			break;
		case ActionType.ACTION_SHAKE_HEAD:
			shakeHeadPhone();
			break;
		case ActionType.ACTION_USERS:
			shakeUser(event);
			break;
		default:
			break;
		}
		callBack.onSensorChangedCallBack();
	}
	
	/**
	 * 摇一摇
	 */
	public void shakePhone() {
		if(Math.abs(x) > DOTA || Math.abs(y) > DOTA || Math.abs(z) > DOTA){
			callBack.onSensorChangedCallBack();
		}
	}
	
	/**
	 * 向前甩出手机
	 */
	public void shakeHeadPhone() {
		//当y<-14时，用户向前甩出手机
		if(y<-DOTA){
			callBack.onSensorChangedCallBack();
		}
	}
	
	/**
	 * 用户自定义动作
	 */
	public void shakeUser(SensorEvent event) {
		callBack.userDefinCallBack(event);
	}
}