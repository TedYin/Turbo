package com.turbo.download;

import com.turbo.pool.TurboTaskQueue;

/**
 * 下载任务队列
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
/*public*/ class TurboDownloadTaskQueue extends TurboTaskQueue{
    
    private static TurboDownloadTaskQueue queue;
    
    private TurboDownloadTaskQueue(int minPool,int maxPool){
        super(minPool, maxPool);
    }
    
    private TurboDownloadTaskQueue(){
        super(3, 8);
    }
    
    /**
     * 获取任务队列单例
     * @return
     */
    public static TurboDownloadTaskQueue newInstance(){
        if(queue == null){
            queue = new TurboDownloadTaskQueue();
        }
        return queue;
    }
    
    /**
     * 获取任务队列单例
     * @param minPool
     * @param maxPool
     * @return
     */
    public static TurboDownloadTaskQueue newInstance(int minPool,int maxPool){
        if(queue == null){
            queue = new TurboDownloadTaskQueue(minPool,maxPool);
        }
        return queue;
    }
}
