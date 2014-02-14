package com.turbo.pool;

/**
 * 任务队列接口
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public interface IQueue {
	
	/**
	 * 获取执行该Queue的中任务的Pool
	 * @return
	 */
	public IPool getPool();
	
	/**
	 * 取得Queue中最后一个优先级为Priority的任务的位置
	 * @param priority
	 * @return
	 */
	public int getPositionByPriority(int priority);
	
	/**
	 * 添加任务
	 * @param task
	 */
	public void inQueue(TurboBaseTask task);
	
	/**
	 * 取得任务
	 * @throws InterruptedException 
	 */
	public TurboBaseTask outQueue() throws InterruptedException;
	
	/**
	 * 获取当前队首的任务
	 */
	public TurboBaseTask peek();
	
	/**
	 * 删除position的task
	 * @param position
	 * @return
	 */
	public TurboBaseTask remove(int position);
	
	/**
	 * 添加任务
	 * @param task
	 * @param priority
	 */
	public void add(TurboBaseTask task, int priority);
	
	/**
	 * 销毁队列 以及其Pool
	 */
	public void destory();
	
	/**
	 * 得到请求队列的大小
	 * @return
	 */
	public int size();
	
	/**
	 * 提供Queue的遍历接口
	 * @return
	 */
	public TurboBaseTask[] toArray();
}
