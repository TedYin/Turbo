package com.turbo.net;

import com.turbo.pool.TurboTaskQueue;

public class TurboNetTaskQueue extends TurboTaskQueue{
	
	public TurboNetTaskQueue(int minPool,int maxPool){
		super(minPool, maxPool);
	}
	
	public TurboNetTaskQueue(){
		super(3, 8);
	}
}
