package com.turbo.view;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tedyin.turbo.R;

public class TurboToast {

	/**
	 * show message 
	 * @param context
	 * @param msg
	 */
	public static void showMsg(Activity activity,String msg) {
		showMsg(activity, Gravity.TOP, msg);
	}	
	
	/**
	 * show message with gravity
	 * @param context
	 * @param gravity
	 * @param msg
	 */
	public static void showMsg(final Activity activity,final int gravity,final String msg){
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
				LayoutInflater li = LayoutInflater.from(activity);
				View toastView = (LinearLayout) li.inflate(R.layout.turbo_view_toast, null);
				initToastView(toastView, 0, msg);
				toast.setGravity(gravity, 0, 10);
				toast.setView(toastView);
				toast.show();
			}
		});
	}

	/**
	 * 显示带Icon的toast
	 * @param context
	 * @param icon
	 * @param msg
	 */
	public static void showMsgWithIcon(final Activity activity,final int iconId,final String msg) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT); 
				LayoutInflater li = LayoutInflater.from(activity);
				View toastView = li.inflate(R.layout.turbo_view_toast, null);
				initToastView(toastView, iconId, msg);
				toast.setView(toastView);
				toast.show();
			}
		});
	}
	
	/**
	 * 设置设置
	 * @param toastView
	 * @param iconId
	 * @param msg
	 */
	private static void initToastView(View toastView,int iconId,String msg){
		ImageView image =  (ImageView) toastView.findViewById(R.id.toast_view_imageView);
		TextView text = (TextView) toastView.findViewById(R.id.toast_view_textView);
		if(image != null && iconId > 0){
			image.setVisibility(View.VISIBLE);
			image.setBackgroundResource(iconId);
		}
		if(text != null && msg != null)
			text.setText(msg);
	}
}
