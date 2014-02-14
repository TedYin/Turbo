package com.turbo.download;

import com.turbo.app.TurboBaseApp;
import com.turbo.data.FileHelper;

import java.io.File;

/**
 * 下载助手类
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class TurboDownloadHelper {
    
    //下载任务引用
    private static TurboDownloadTask task;

    /**
     * 下载
     * @param spec
     * @param callBack
     * @return
     */
    public static TurboDownloadTask download(String spec,ITurboDownloadCallBack callBack){
        String[] str = spec.split("/");
        download(spec, callBack, str[str.length-1]);
        return task;
    }
    
    /**
     * 下载
     * @param spec
     * @param callBack
     * @param fileName
     * @return
     */
    public static TurboDownloadTask download(final String spec,final ITurboDownloadCallBack callBack,final String fileName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long totalSrcSize = TurboBaseApp.getNetHellper().getDownloadFileLengthByURL(spec);
                File outFile = FileHelper.createExternalCacheFile(fileName);
                TurboDownloadBean bean = new TurboDownloadBean(spec,totalSrcSize, outFile);
                task = new TurboDownloadTask(bean, callBack);
                TurboDownloadTaskQueue.newInstance().inQueue(task);
            }
        }).start();
        return task;
    }
    
    /**
     * 下载
     * @param spec
     * @param callBack
     * @param outFile
     * @return
     */
    public static TurboDownloadTask download(final String spec,final ITurboDownloadCallBack callBack, final File outFile){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long totalSrcSize = TurboBaseApp.getNetHellper().getDownloadFileLengthByURL(spec);
                TurboDownloadBean bean = new TurboDownloadBean(spec,totalSrcSize, outFile);
                task = new TurboDownloadTask(bean, callBack);
                TurboDownloadTaskQueue.newInstance().inQueue(task);
            }
        }).start();
        return task;
    }
}
