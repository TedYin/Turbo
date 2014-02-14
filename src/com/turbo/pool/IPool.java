package com.turbo.pool;

/**
 * 线程池接口
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public interface IPool {
	/**
	 * Pool动态扩容 
	 */
	public void autoEnlargePool();
	
	/**
	 * Pool动态缩容
	 */
	public void autoReducePool();
	
	/**
	 * 启动线程池
	 */
	public void start();
	
//	/**
//	 * 结束该任务
//	 * @param task
//	 */
//	public void cancel(TurboBaseTask task);
//	
//	/**
//	 * 暂停该任务
//	 * @param task
//	 */
//	public void pause(TurboBaseTask task);
//	
//	/**
//	 * 继续该任务
//	 * @param task
//	 */
//	public void resume(TurboBaseTask task);
	
	/**
	 * 暂停所有任务
	 */
	public void pauseAll();
	
	/**
	 * 继续所有任务
	 */
	public void resumeAll();
	
	/**
	 * 取消所有任务
	 */
	public void cancelAll();
	
	/**
	 * 停止所有任务,并销毁线程池
	 */
	public void destory();
}
