package com.turbo;

import com.turbo.net.TurboNetTaskQueue;
import com.turbo.pool.IQueue;
import com.turbo.pool.TurboTaskQueue;

/**
 * Turbo功能类
 * @author Ted
 *
 */
public class Turbo {
	public static IQueue newTaskQueue(){
		TurboTaskQueue queue = new TurboTaskQueue();
		return queue;
	}
	
	public static IQueue newNetTaskQueue(){
		TurboTaskQueue queue = new TurboTaskQueue();
		return queue;
	}
	
	public static IQueue newTurboNetTaskQueue(){
		TurboNetTaskQueue queue = new TurboNetTaskQueue();
		return queue;
	}
}
