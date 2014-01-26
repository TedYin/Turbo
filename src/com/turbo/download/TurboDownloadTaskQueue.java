package com.turbo.download;

import com.turbo.pool.TurboTaskQueue;

public class TurboDownloadTaskQueue extends TurboTaskQueue{
    public TurboDownloadTaskQueue(int minPool,int maxPool){
        super(minPool, maxPool);
    }
    
    public TurboDownloadTaskQueue(){
        super(3, 8);
    }
}
