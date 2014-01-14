package com.turbo.download;

import android.os.Handler;
import android.os.Looper;

import com.turbo.TurboException;
import com.turbo.data.BufferedRandomAccessFile;
import com.turbo.pool.TurboBaseTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TurboDownloadTask extends TurboBaseTask{
	private int READ_TIME_OUT = 10000;// 读取超时时间
	private static Handler handler = new Handler(Looper.getMainLooper());
	private TurboDownloadRequestBean bean;
	private ITurboDownloadCallBack callBack;
	
	public TurboDownloadTask(TurboDownloadRequestBean bean, ITurboDownloadCallBack callBack){
		this.bean = bean;
		this.callBack = callBack;
	}
	
	@Override
	public Object execute() {
		//下载，在下载过程中需要检查是否被终止等task的状态
		HttpURLConnection conn = null;
		InputStream is = null;
		BufferedRandomAccessFile ras = null;
		try {
			URL url = new URL(bean.getSpec());
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setReadTimeout(READ_TIME_OUT);
			conn.setRequestProperty("Range", "bytes=" + bean.getStartPosition()+ "-");
			conn.connect();
			/**
			 * 执行下载写入操作
			 */
			is = conn.getInputStream();
			ras = new BufferedRandomAccessFile(bean.getOutFile(),"rw");
			ras.seek(bean.getStartPosition());
			byte[] buf = new byte[4*1024];
			int len = -1;
			int current = 0;
			while ((len = is.read(buf)) != -1) {
			    //检查下载状态
			    if(!isDownloading(ras,is)){
			        break;
			    }
				ras.write(buf, 0, len);
				current += len;
				//更新UI
				postOnDownloading((int) (current/bean.getTotoalSrcSize() *100));
			}
			
			
			
			if (current == bean.getTotoalSrcSize()){
			    com.turbo.TurboLog.e("下载完成！");
				//下载完成
			    ras.close();
	            is.close();
			    postOnFinish(bean.getOutFile());
			} else {
			    //下载出错
				if(!isCanceled() && !isPaused()){
				    com.turbo.TurboLog.e("下载未完成处理");
				    postOnError(new TurboException("任务被暂停或被终止！"));
				}else
				    postOnError(new TurboException("下载失败，请重试！"));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(is != null){
					is.close();
					is = null;
				}
				if(ras != null){
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
	 * 完成下载
	 * @param outFile
	 */
	private void postOnFinish(final File outFile){
	    handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onFinish(outFile);
            }
        });
	}
	
	/**
	 * 取消下载
	 * @param outFile
	 */
	private void postOnCancel(final File outFile){
	    handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onCancel(outFile);
            }
        });
	}
	
	/**
	 * 暂停下载
	 * @param outFile
	 */
	private void postOnPause(final File outFile){
	    handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onPause(outFile);
            }
        });
	}
	
	/**
	 * 正在下载
	 * @param progress
	 */
	private void postOnDownloading(final int progress){
	    handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onDownloading(progress);
            }
        });
	}
	
	/**
	 * 下载出错
	 * @param e
	 */
	private void postOnError(final TurboException e){
	    handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(e);
            }
        });
	}
	
	/**
	 * 检查下载状态
	 * @param ras
	 * @param is
	 * @throws IOException
	 */
	private boolean isDownloading(BufferedRandomAccessFile ras, InputStream is) throws IOException{
	    if(isCanceled()){
	        ras.close();
	        is.close();
	        postOnCancel(bean.getOutFile());
	        return false;
	    }
	    
	    if(isPaused()){
	        ras.close();
            is.close();
            postOnPause(bean.getOutFile());
            return false;
	    }
	    return true;
	}
	
	
	
}
