package com.turbo.net;

import android.os.Handler;
import android.os.Looper;

import com.turbo.net.VolleyNetHelper.NetCallBack;
import com.turbo.pool.TurboBaseTask;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Map;

/**
 * 基础的请求封装
 * @author Ted
 */
public abstract class TurboBaseRequest<T> extends TurboBaseTask{
	
	private String url;
	private int method = TurboNetworkUtil.METHOD_GET;
	private Map<String, String> headers;
	private NetCallBack<T> callBack;
	private static Handler handler = new Handler(Looper.getMainLooper());
	private HttpEntity entity;
	protected String getUrl() {
		return url;
	}

	protected void setUrl(String url) {
		this.url = url;
	}

	protected int getMethod() {
		return method;
	}

	protected void setMethod(int method) {
		this.method = method;
	}

	protected Map<String, String> getHeaders() {
		return headers;
	}

	protected void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	protected void setCallBack(NetCallBack<T> callBack) {
		this.callBack = callBack;
	}
	
    protected HttpEntity getEntity() {
        return entity;
    }

    protected void setEntity(HttpEntity entity) {
        this.entity = entity;
    }

    /**
	 * 处理成功
	 * @param resp
	 */
	protected void postSuccess(final T resp){
		handler.post(new Runnable() {
			@Override
			public void run() {
				callBack.onSuccess(resp);
			}
		});
	} 
	
	/**
	 * 处理错误数据
	 * @param errorMsg
	 */
	protected void postError(final String errorMsg) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				callBack.onError(errorMsg);
			}
		});
	}

	/**
	 * 做Post请求
	 */
	protected abstract T doPost(DefaultHttpClient httpClient)throws ClientProtocolException, IOException;
	
	
	/**
	 * 做Get请求 
	 */
	protected abstract T doGet(DefaultHttpClient httpClient)throws ClientProtocolException, IOException;
}
