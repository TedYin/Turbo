package com.turbo.download;

import com.turbo.TurboException;

import java.io.File;

/**
 * 下载回调接口（根据不同状态来回调）
 * @author Ted
 *
 */
public interface ITurboDownloadCallBack {

    /**
     * 取消下载
     * @param outFile
     */
    public void onCancel(File outFile);
    
    /**
     * 暂停下载
     * @param outFile
     */
    public void onPause(File outFile);
    
    /**
     * 下载完成
     * @param outFile
     */
    public void onFinish(File outFile);
    
    /**
     * 正在下载
     * @param progress
     */
    public void onDownloading(int progress);
    
    /**
     * 下载出错
     * @param e
     */
    public void onError(TurboException e);
}
