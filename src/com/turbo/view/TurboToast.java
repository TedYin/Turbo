package com.turbo.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tedyin.turbo.R;
import com.turbo.app.TurboBaseApp;

/**
 * 自定义Toast
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class TurboToast {
    
    //异步处理UI操作
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Context mContext = TurboBaseApp.getAppContext();
    /**
     * @param msg
     */
    public static void showMsg(String msg){
        showMsg(Gravity.TOP, msg);
    }

	/**
	 * show message with gravity
	 * @param gravity
	 * @param msg
	 */
	public static void showMsg(final int gravity,final String msg){
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
				LayoutInflater li = LayoutInflater.from(mContext);
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
	public static void showMsgWithIcon(final int iconId,final String msg) {
	    handler.post(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT); 
				LayoutInflater li = LayoutInflater.from(mContext);
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
