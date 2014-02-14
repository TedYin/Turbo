package com.turbo.net.impl;

import android.graphics.Bitmap;

import com.turbo.common.URLHelper;
import com.turbo.net.TurboBaseRequest;
import com.turbo.net.TurboNetworkUtil;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Map;

/**
 * 获取网络图片
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class TurboImageRequest extends TurboBaseRequest<Bitmap>{
	
	/**
	 * TODO:思考缓存问题，此类目前不可用！
	 */
	
	
	public TurboImageRequest(String url, Map<String, String> params,
			Map<String, String> headers, int method) {
		setUrl(URLHelper.buildUrl(url, params));
		setHeaders(headers);
		setMethod(method);
	}
	
	public TurboImageRequest(String url, Map<String, String> params,int method) {
		setUrl(URLHelper.buildUrl(url, params));
		setMethod(method);
	}
	
	public TurboImageRequest(String url, Map<String, String> params) {
		setUrl(URLHelper.buildUrl(url, params));
	}
	
	@Override
	protected Bitmap doPost(DefaultHttpClient httpClient)
			throws ClientProtocolException, IOException {
		
		return null;
	}

	@Override
	protected Bitmap doGet(DefaultHttpClient httpClient)
			throws ClientProtocolException, IOException {
		//先从缓存中取图片，如果没有再从网路获取
		return null;
	}

	@Override
	public Object execute() {
		Bitmap bitmap = null;
		try {
			if(isCanceled() || isPaused()) return null;
			DefaultHttpClient httpClient = TurboNetworkUtil.getHttpClient();
			
			if (getMethod() == TurboNetworkUtil.METHOD_GET) {
				bitmap = doGet(httpClient);
			} else if (getMethod() == TurboNetworkUtil.METHOD_POST) {
				bitmap = doPost(httpClient);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	@Override
	public Object complete(Object obj) {
		return null;
	}
}
