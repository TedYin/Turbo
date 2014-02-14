package com.turbo.net;

import com.turbo.pool.TurboTaskQueue;

/**
 * 网络请求队列
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class TurboNetTaskQueue extends TurboTaskQueue{
	
	public TurboNetTaskQueue(int minPool,int maxPool){
		super(minPool, maxPool);
	}
	
	public TurboNetTaskQueue(){
		super(3, 8);
	}
}
