package com.turbo.pool;

import java.util.LinkedList;

/**
 * 默认任务队列
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class TurboTaskQueue implements IQueue{

	private int DEFAULT_MIN = Runtime.getRuntime().availableProcessors();
	private int DEFAULT_MAX = 10;
	private IPool pool = null; 
	private LinkedList<TurboBaseTask> list = new LinkedList<TurboBaseTask>();
	
	/**
	 * 构造函数
	 * 默认线程池中，最大线程数为8，最小为3 
	 */
	public TurboTaskQueue(){
		pool = new TurboThreadPool(DEFAULT_MIN,DEFAULT_MAX,this);
		pool.start();
	}
	
	/**
	 * 构造函数
	 * @param minPool 最小线程数，应大于 0
	 * @param maxPool 最大线程数，应大于minPool
	 */
	public TurboTaskQueue(int minPool, int maxPool){
		if(minPool > 0 && maxPool > minPool){
			this.DEFAULT_MAX = maxPool;
			this.DEFAULT_MIN = minPool;
			pool = new TurboThreadPool(DEFAULT_MIN,DEFAULT_MAX,this);
			pool.start();
		}else{
			throw new RuntimeException("minPool或maxPool,参数不合法!");
		}
	}
	
	@Override
	public IPool getPool() {
		return pool;
	}

	@Override
	public int getPositionByPriority(int priority) {
		//TODO:此处效率极差，待优化！
		int position = 0;
		for(int i = 0; i<list.size();i++){
			if(list.get(i).getTaskPriority() == priority+1)
				return position; 
		}
		return 0;
	}

	@Override
	public synchronized void inQueue(TurboBaseTask task) {
		list.add(task);
		pool.autoEnlargePool();
		this.notifyAll();
	}

	@Override
	public synchronized TurboBaseTask outQueue() throws InterruptedException {
		while(list.size() <= 0){
			this.wait();
		}
		TurboBaseTask task = list.removeFirst();
		pool.autoReducePool();
		return task;
	}

	@Override
	public TurboBaseTask peek() {
		return list.element();
	}

	@Override
	public synchronized TurboBaseTask remove(int position) {
		return list.remove(position);
	}

	@Override
	public synchronized void add(TurboBaseTask task, int priority) {
		int location = getPositionByPriority(priority);
		list.add(location, task);
	}

	@Override
	public synchronized void destory() {
		list.clear();
		pool.destory();
	}
	
	@Override
	public int size() {
		return list.size();
	}

	@Override
	public synchronized TurboBaseTask[] toArray() {
		return (TurboBaseTask[]) list.toArray();
	}
}
