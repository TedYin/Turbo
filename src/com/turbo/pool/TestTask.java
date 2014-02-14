package com.turbo.pool;

import com.turbo.TurboLog;

/**
 * 测试任务
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class TestTask extends TurboBaseTask {

	@Override
	public Object execute() {
		TurboLog.e(Thread.currentThread().getName() + "Task is execute !");
		while (!isCanceled()) {
			try {
				if (isPaused()) {
					this.wait();
				}
				Thread.sleep(4000);
				TurboLog.e(Thread.currentThread().getName()
						+ " is executing !");
			} catch (InterruptedException e) {
				continue;
			}
		}
		return null;
	}

	@Override
	public Object complete(Object obj) {
		TurboLog.e(Thread.currentThread().getName() + " Task is complete !");
		return obj;
	}
}
