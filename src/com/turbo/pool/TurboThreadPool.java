package com.turbo.pool;

import java.util.Vector;

/**
 * 线程池
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
/*public*/class TurboThreadPool implements IPool {

	private static int minPoolSize;
	private static int maxPoolSize;
	private static Vector<TurboWorkerThread> vector;
	private static IQueue mQueue;

	/**
	 * 系数因子，每个线程负责处理DOTA个Task 超过该阈值则自动增加新的线程来处理
	 * */
	private static int DOTA = 5;

	/**
	 * 构造函数
	 * 
	 * @param min
	 *            线程池的最小线程数
	 * @param max
	 *            线程池的最大线程数
	 * @param queue
	 *            任务队列
	 * @param maxDealTaskNum
	 *            每个线程最多处理的Task数量,超过该阈值，则自动增加新的线程来处理
	 */
	public TurboThreadPool(int min, int max, IQueue queue, int maxDealTaskNum) {
		minPoolSize = min;
		maxPoolSize = max;
		mQueue = queue;
		DOTA = maxDealTaskNum;
		vector = new Vector<TurboWorkerThread>(max);
	}

	/**
	 * 构造函数
	 * 
	 * @param min
	 *            线程池的最小线程数
	 * @param max
	 *            线程池的最大线程数
	 * @param queue
	 *            任务队列
	 */
	public TurboThreadPool(int min, int max, IQueue queue) {
		minPoolSize = min;
		maxPoolSize = max;
		mQueue = queue;
		vector = new Vector<TurboWorkerThread>(max);
	}

	/***
	 * 每DOTA个Task由一个WorkerThread处理 如果Queue中的Request数量>线程数量*DOTA，则进行动态扩容，每次扩容量为
	 * request数量/（线程数*DOTA）四舍五入-当前 线程数，最大不超过线程池最大数量。
	 */
	@Override
	public void autoEnlargePool() {
		int enlargeNum = 0;
		if (mQueue.size() > vector.size() * DOTA) {
			enlargeNum = Math.round(mQueue.size() / (vector.size() * DOTA))
					- vector.size();
			if (enlargeNum > 0 && enlargeNum + vector.size() < maxPoolSize) {
				enlagrePool(mQueue, enlargeNum); // 扩容
			}
		}
	}

	/**
	 * 每DOTA个Task由一个WorkerThread处理
	 * 如果Queue中的Request数量/DOTA（四舍五入）<线程数量,则进行动态缩减，每次缩容的数量为：
	 * 当前线程数量-（request数量/DOTA（向下取整））
	 */
	@Override
	public void autoReducePool() {

		// 多余的线程数量
		int extraThreadNum = 0;
		// 当前线程池中理论上应该存活的线程数
		int avalibleThread = Math.round(mQueue.size() / DOTA);
		if (avalibleThread < vector.size() && avalibleThread >= minPoolSize) {
			// 防止重复修改extraThreadNum，多次删除Thread
			synchronized (vector) {
				extraThreadNum = vector.size() - avalibleThread;
				if (extraThreadNum > 0) {
					reducePool(extraThreadNum, vector);
				}
			}
		}
	}

	@Override
	public void start() {
		enlagrePool(mQueue, minPoolSize);
	}

	@Override
	public void pauseAll() {
		for (TurboBaseTask task : mQueue.toArray()) {
			task.pause();
		}
	}

	@Override
	public void resumeAll() {
		for (TurboBaseTask task : mQueue.toArray()) {
			task.resume();
		}
	}

	@Override
	public void cancelAll() {
		for (TurboBaseTask task : mQueue.toArray()) {
			task.cancel();
		}
	}

	@Override
	public void destory() {
		cancelAll();
		for (TurboWorkerThread work : vector) {
			work.stopWork();
			work = null;
		}
		vector.clear();
		vector = null;
	}

	/**
	 * 向Pool中添加线程并启动
	 * 
	 * @param queue
	 * @param num
	 */
	private void enlagrePool(IQueue queue, int num) {
		for (int i = 1; i <= num; i++) {
			TurboWorkerThread worker = new TurboWorkerThread(queue);
			vector.add(worker);
			worker.start();
		}
	}

	/**
	 * 从Pool中删除线程
	 * 
	 * @param extraThreadNum
	 * @param vector
	 */
	private void reducePool(int extraThreadNum, Vector<TurboWorkerThread> vector) {
		for (TurboWorkerThread worker : vector) {
			if (extraThreadNum > 0 && vector.size() > minPoolSize) {
				if (!worker.isWorking()) {
					worker.stopWork();
					extraThreadNum--;
					vector.remove(worker);
					continue;
				}
			} else
				break;
		}
	}
}
