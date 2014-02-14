package com.turbo.download;

import com.turbo.TurboException;

/**
 * 下载回调接口（根据不同状态来回调）
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public interface ITurboDownloadCallBack {

    /**
     * 开始下载
     * @param bean
     */
    public void onStart(TurboDownloadBean bean);
    
    /**
     * 正在下载
     * @param progress
     */
    public void onDownloading(float progress);
    
    /**
     * 下载完成
     * @param bean
     */
    public void onFinish(TurboDownloadBean bean);
    
    /**
     * 取消下载
     * @param bean
     */
    public void onCancel(TurboDownloadBean bean);
    
    /**
     * 暂停下载
     * @param bean
     */
    public void onPause(TurboDownloadBean bean);
    
    /**
     * 下载出错
     * @param e
     */
    public void onError(TurboException e);
}
