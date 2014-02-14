package com.turbo.view;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.tedyin.turbo.R;

/**
 * Loading加载对话框
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class TurboLoadingDialog extends Dialog{

	/**
	 * Loading回调接口
	 * @author Ted
	 */
	public interface LoadingCallBack extends OnCancelListener{
		/** 加载超时回调*/
		public void onOutDate(String errorMsg);
		/** Loading界面Dismiss时回调*/
		public void onDimss();
		/** Loading界面Show（显示）时回调*/
		public void onShow();
		/** 设置一些Dialog的属性*/
		public void onInitDialogStyle(TurboLoadingDialog dialog);
		/** 取消loading操作*/
		@Override
		public void onCancel(DialogInterface dialog);
	}
	
	private static TurboLoadingDialog dialog;
	private static Activity mActivity;
	private static LoadingCallBack mCallBack;
	
	/** 计时器，到达最大等待时间后，进行出错处理*/
	private static Timer timer;
	private static TimerTask timerTask;
	private static int DELAY_TIME = 15000;
	
	
	private TurboLoadingDialog(Activity activity) {
		super(activity);
	}
	
	private TurboLoadingDialog(Activity activity, int theme) {
		super(activity, theme);
	}
	
	/**
	 * 初始化Loading
	 * @param activity
	 * @param contentViewId
	 * @param msg
	 * @param loadTextViewId 加载文字框Id
	 */
	private static void init(Activity activity,int contentViewId,String msg,int loadTextViewId){
		mActivity = activity;
		if(dialog == null){
			dialog = new TurboLoadingDialog(activity,R.style.LoadingDialogTheme);
			dialog.setContentView(contentViewId);
			dialog.setCanceledOnTouchOutside(false);
			if(mCallBack != null)
				dialog.setOnCancelListener(mCallBack);
			TextView loadText = (TextView) dialog.findViewById(loadTextViewId);
			if(loadText != null){
				loadText.setText(msg);
			}
		}else{
			dialog.dismiss();
			dialog = null;
		}
		initTimer(activity);
	}
	
	/**
	 * 初始化Loading
	 * @param activity
	 * @param contentView
	 * @param msg
	 * @param loadTextViewId 文字加载框Id
	 */
	private static void init(Activity activity, View contentView,String msg,int loadTextViewId){
		mActivity = activity;
		if(dialog == null){
			dialog = new TurboLoadingDialog(activity,R.style.LoadingDialogTheme);
			dialog.setContentView(contentView);
			dialog.setCanceledOnTouchOutside(false);
			if(mCallBack != null)
				dialog.setOnCancelListener(mCallBack);
			TextView loadText = (TextView) dialog.findViewById(loadTextViewId);
			if(loadText != null){
				loadText.setText(msg);
			}
		}else{
			dialog.dismiss();
			dialog = null;
		}
		initTimer(activity);
	}
	
	/**
	 * 初始化计时器
	 * @param activity
	 */
	private static void initTimer(Activity activity){
		if(timer == null){
			timer = new Timer(false);
		}else{
			timer.cancel();
			timer = null;
		}
		
		if(timerTask == null){
			timerTask = new TimerTask() {
				@Override
				public void run() {
					if(DELAY_TIME <= 0){
						endLoading();
						if(mCallBack != null)
							mCallBack.onOutDate(DELAY_TIME + "秒倒计时结束！");
						mCallBack = null;
					}else
						DELAY_TIME--;
				}
			};
		}else{
			timerTask.cancel();
			timerTask = null;
		}
	}
	
	/**
	 * 启动自定义Loading
	 * @param activity
	 * @param msg
	 * @param contentViewId
	 * @param loadTextViewId
	 */
	public static void customLoading(Activity activity, String msg, int contentViewId, int loadTextViewId){
		DELAY_TIME = 15000;
		init(activity, contentViewId, msg,loadTextViewId);
		if(mCallBack != null)
			mCallBack.onInitDialogStyle(dialog);
		mActivity.runOnUiThread(loadingAction);
		timer.schedule(timerTask, 1000, 1000);
	}
	
	/**
	 * 启动自定义Loading
	 * @param activity
	 * @param msg
	 * @param contentView
	 * @param loadTextViewId
	 */
	public static void customLoading(Activity activity, String msg,View contentView, int loadTextViewId){
		DELAY_TIME = 15000;
		init(activity, contentView, msg, loadTextViewId);
		if(mCallBack != null)
			mCallBack.onInitDialogStyle(dialog);
		mActivity.runOnUiThread(loadingAction);
		timer.schedule(timerTask, 1000,1000);
	}
	
	/**
	 * 启动默认Loading
	 * @param activity
	 * @param msg
	 */
	public static void loading(Activity activity,String msg){
		DELAY_TIME = 15000;
		init(activity, R.layout.turbo_view_loading_dialog, msg,R.id.turbo_view_loading_dialog_loadingText);
		initDialogStyle();
		mActivity.runOnUiThread(loadingAction);
		timer.schedule(timerTask, 1000, 1000);
	}
	
	/**
	 * 初始化一些Dialog的熟悉
	 */
	private static void initDialogStyle(){
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}
	
	
	/**
	 * 添加Loading回调
	 * @param callBack
	 */
	public static void addLoadingCallBack(LoadingCallBack callBack){
		mCallBack = callBack;
	}
	
	/**
	 * 结束Loading加载
	 */
	public static void endLoading() {
		if(timer != null){
			timer.cancel();
			timer = null;
		}
		
		if(timerTask != null){
			timerTask.cancel();
			timerTask = null;
		}
		if(mActivity != null)
		    mActivity.runOnUiThread(endLoadingAction);
		DELAY_TIME = 0;
	}
	
	/** 启动Loading的动作*/
	private static Runnable loadingAction = new Runnable() {
		@Override
		public void run() {
			if(dialog!= null && !dialog.isShowing()){
				dialog.show();
				if(mCallBack != null)
					mCallBack.onShow();
			}
		}
	};
	
	/** 停止Loading的动作*/
	private static Runnable endLoadingAction = new Runnable() {
		@Override
		public void run() {
		    if(dialog != null && dialog.isShowing()){
	            dialog.dismiss();
	            dialog = null;
	            if(mCallBack != null)
	                mCallBack.onDimss();
	        }
		}
	};
	
	@Override
	public void onBackPressed() {
		endLoading();
		super.onBackPressed();
	};
}
