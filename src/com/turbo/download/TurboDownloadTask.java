
package com.turbo.download;

import android.os.Handler;
import android.os.Looper;

import com.turbo.TurboException;
import com.turbo.TurboLog;
import com.turbo.data.BufferedRandomAccessFile;
import com.turbo.pool.TurboBaseTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * 下载执行类
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class TurboDownloadTask extends TurboBaseTask {
    private int READ_TIME_OUT = 20000;// 读取超时时间
    private static Handler handler = new Handler(Looper.getMainLooper());
    private TurboDownloadBean bean;
    private ITurboDownloadCallBack callBack;

    public TurboDownloadTask(TurboDownloadBean bean, ITurboDownloadCallBack callBack) {
        this.bean = bean;
        this.callBack = callBack;
    }

    @Override
    public Object execute() {
        //开始下载
        postOnStart(bean);
        //判断是否已经下载完成 
        if(bean.getOutFile() != null&& bean.getOutFile().length() == bean.getTotoalSrcSize()){
            // 下载完成
            com.turbo.TurboLog.e("文件已存在，下载完成！");
            postOnFinish(bean);
            return bean;
        }
        
        // 下载，在下载过程中需要检查是否被终止等task的状态
        HttpURLConnection conn = null;
        InputStream is = null;
        BufferedRandomAccessFile ras = null;
        try {
            URL url = new URL(bean.getSpec());
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setReadTimeout(READ_TIME_OUT);
            conn.setRequestProperty("Range", "bytes=" + bean.getStartPosition() + "-");
            conn.connect();
            
            /**
             * 执行下载写入操作
             */
            int respCode = conn.getResponseCode();
            TurboLog.e("respCode : " + respCode);
            if (respCode != 206 && respCode != 200) {
                postOnError(new TurboException("错误状态：打开链接失败，错误码：" + conn.getResponseCode()));
                return null;
            }
            is = conn.getInputStream();
            ras = new BufferedRandomAccessFile(bean.getOutFile(),
                    BufferedRandomAccessFile.MODE.WRITE);
            ras.seek(bean.getStartPosition());
            byte[] buf = new byte[2 * 1024];
            int len = -1;
            float current = bean.getOutFile().length();
            while ((len = is.read(buf)) != -1) {
                // 检查下载状态
                if (!isDownloading(ras, is))
                    break;
                //写入
                ras.write(buf, 0, len);
                current += len;
                // 更新UI
                postOnDownloading((int) (current / bean.getTotoalSrcSize() * 100));
            }

            if (current == bean.getTotoalSrcSize()) {
                com.turbo.TurboLog.e("下载完成！");
                // 下载完成
                ras.close();
                is.close();
                postOnFinish(bean);
            } else {
                // 下载出错
                if(isCanceled()){
                    postOnCancel(bean);
                }else if(isPaused()){
                    postOnPause(bean);
                }else{
                    postOnError(new TurboException("下载失败，请重试！"));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            postOnError(new TurboException(e.toString()));
        } catch (IOException e) {
            e.printStackTrace();
            postOnError(new TurboException(e.toString()));
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
                if (ras != null) {
                    ras.close();
                    ras = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    @Override
    public Object complete(Object obj) {
        return obj;
    }
    
    /**
     * 开始下载
     * 
     * @param bean
     */
    private void postOnStart(final TurboDownloadBean bean) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onStart(bean);
            }
        });
    }

    /**
     * 完成下载
     * 
     * @param bean
     */
    private void postOnFinish(final TurboDownloadBean bean) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onFinish(bean);
            }
        });
    }

    /**
     * 取消下载
     * 
     * @param bean
     */
    private void postOnCancel(final TurboDownloadBean bean) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onCancel(bean);
            }
        });
    }

    /**
     * 暂停下载
     * 
     * @param bean
     */
    private void postOnPause(final TurboDownloadBean bean) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onPause(bean);
            }
        });
    }

    /**
     * 正在下载
     * 
     * @param progress
     */
    private void postOnDownloading(final float progress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onDownloading(progress);
            }
        });
    }

    /**
     * 下载出错
     * 
     * @param e
     */
    private void postOnError(final TurboException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(e);
            }
        });
    }

    /**
     * 检查下载状态
     * 
     * @param ras
     * @param is
     * @throws IOException
     */
    private boolean isDownloading(BufferedRandomAccessFile ras, InputStream is) throws IOException {
        if (isCanceled()) {
            ras.close();
            is.close();
            postOnCancel(bean);
            return false;
        }

        if (isPaused()) {
            ras.close();
            is.close();
            postOnPause(bean);
            return false;
        }
        return true;
    }

}
