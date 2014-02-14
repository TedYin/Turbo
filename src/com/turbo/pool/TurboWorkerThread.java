package com.turbo.pool;

import com.turbo.TurboException;
import com.turbo.TurboLog;

/**
 * 工作线程模型
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
/* public */class TurboWorkerThread extends Thread {

	private volatile boolean toKill = false;
	private IQueue mQueue;
	private TurboBaseTask mTask;

	public TurboWorkerThread(IQueue queue) {
		mQueue = queue;
	}

	public TurboWorkerThread(IQueue queue, String name) {
		this.mQueue = queue;
		this.setName(name);
	}

	public TurboWorkerThread(IQueue queue, String name, boolean isDaemon) {
		this.mQueue = queue;
		this.setName(name);
		this.setDaemon(isDaemon);
	}

	@Override
	public final void run() {
		Object obj = null;
		while (!toKill) {
			try {
				if (mTask == null) {
					mTask = mQueue.outQueue();
				}

				if (mTask.isCanceled()) {
					// 任务被取消
					mTask.complete(new TurboException("任务被取消！")); // TODO:有待完善
					return;
				}

				if (mTask.isPaused()) {
					// 任务被暂停
					this.wait();
				}
				
				// 处理任务
				obj = mTask.execute();
				mTask.complete(obj);
				mTask = null; // 执行完毕后将Task置空

			} catch (InterruptedException e) {
				if (toKill) {
					TurboLog.e(this.getName() + " 工作线程被结束 !");
					mTask.complete(new TurboException("工作线程被结束 ,任务被终止！"));
					return;
				} else if (mTask.isCanceled()) {
					// 任务被取消
					TurboLog.e(this.getName() + " 任务被取消!");
					mTask.complete(new TurboException("任务被取消！"));
					return;
				} else {
					continue;
				}
			} finally {
				if (mTask != null) {
					mTask.complete(obj);
					mTask = null; // 执行完毕后将Task置空
				}
			}
		}
	}

	/**
	 * 结束线程
	 */
	public synchronized void stopWork() {
		// 2段式中断，先设置结束标记，在检测中断状态
		toKill = true;
		this.interrupt();
	}

	/**
	 * 获取线程的工作状态，若有Task在处理则为工作中
	 * 
	 * @return false 为休息，true为工作中
	 */
	public boolean isWorking() {
		return mTask == null ? false : true;
	}

}
