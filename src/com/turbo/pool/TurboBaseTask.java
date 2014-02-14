package com.turbo.pool;

import com.turbo.TurboLog;


/**
 * 任务接口
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public abstract class TurboBaseTask {
	private boolean isCanceled = false;
	private boolean isPaused = false;
	
	private int mTaskPriority = TurboThreadPriority.NORMAL;
	
	/**
	 * Task的优先级
	 * @author Ted
	 */
	public interface TurboThreadPriority{
		public static final int INSTANCE = 0;	//立即执行
		public static final int HIGH = 0x1;		//高优先级
		public static final int NORMAL = 0x2;	//普通级别
		public static final int LOW = 0x3;		//低优先级
	}
	
	/**
	 * 执行
	 */
	public abstract Object execute();
	
	/**
	 * 处理执行结果
	 * @param obj
	 * @return
	 */
	public abstract Object complete(Object obj);
	
	/**
	 * 暂停任务
	 */
	public synchronized void pause() {
		TurboLog.e(Thread.currentThread().getName() + " is pause !");
		isPaused = true;
	}
	
	/**
	 * 继续任务
	 */
	public synchronized void resume() {
		isPaused = false;
		TurboLog.e(Thread.currentThread().getName() + " is resume !");
	}
	
	/**
	 * 取消任务，只有当任务未被执行时才可以取消，如果任务已经开始执行则不能取消
	 */
	public synchronized void cancel() {
		isCanceled = true;
		TurboLog.e(Thread.currentThread().getName() + " is cancel !");
	}
	
	/**
	 * 任务是否被取消
	 * @return true被取消，false未被取消
	 */
	public synchronized boolean isCanceled() {
		return isCanceled;
	}

	/**
	 * 任务是否被暂停
	 * @return true 被暂停，false未被暂停
	 */
	public synchronized boolean isPaused() {
		return isPaused;
	}

	/**
	 * 得到该任务的优先级，默认为正常优先级
	 * @return
	 */
	public int getTaskPriority(){
		return mTaskPriority;
	}
	
	/**
	 * 设置任务的优先级，默认为正常优先级
	 * @param priority
	 */
	public void setTaskPriority(int priority){
		this.mTaskPriority = priority;
	}
}
