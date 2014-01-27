package com.turbo.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.LruCache;

import com.turbo.common.URLHelper;
import com.turbo.data.FileHelper;
import com.turbo.net.impl.TurboJSONRequest;
import com.turbo.net.impl.TurboJSONResponse;
import com.turbo.net.impl.TurboStringRequest;
import com.turbo.net.impl.TurboStringResponse;
import com.turbo.pool.IQueue;
import com.turbo.volley.Request.Method;
import com.turbo.volley.RequestQueue;
import com.turbo.volley.Response.ErrorListener;
import com.turbo.volley.Response.Listener;
import com.turbo.volley.VolleyError;
import com.turbo.volley.toolbox.ImageLoader;
import com.turbo.volley.toolbox.ImageLoader.ImageCache;
import com.turbo.volley.toolbox.ImageRequest;
import com.turbo.volley.toolbox.JsonObjectRequest;
import com.turbo.volley.toolbox.StringRequest;
import com.turbo.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 对于Volley包的封装
 * 
 * @author Ted
 */
public class VolleyNetHelper {

	// 默认时长1分钟
//	private static final int SO_TIME_OUT = 60000;

	private static VolleyNetHelper instance;
	private static RequestQueue mQueue;
	private static boolean isInitVolley = false;
	
	private static ImageLoader loader;
	private static LruCache<String, Bitmap> lruCache;

//	private static HttpClient httpClient;
	
	private static TurboNetTaskQueue netTaskQueue;
	private static boolean isInitTurboNetTaskQueue = false;

	private VolleyNetHelper(Context context) {
		initVolley(context);
//		getHttpClient();
	}

	private void initVolley(Context context) {
		if (!isInitVolley) {
			if (mQueue == null)
				mQueue = Volley.newRequestQueue(context);
			mQueue.start();
			isInitVolley = true;
		}
	}

	/**
	 * 得到VolleyNetHelper实例
	 * 
	 * @param context
	 * @return
	 */
	public static VolleyNetHelper newInstance(Context context) {
		if (null == instance)
			instance = new VolleyNetHelper(context);
		return instance;
	}

	/**
	 * 得到Imageloader SDK version > 3.0
	 * 
	 * @param context
	 * @return
	 */
	@SuppressLint("NewApi")
	public ImageLoader getImageLoader(Context context) {
		if (lruCache == null) {
			lruCache = new LruCache<String, Bitmap>(50);
		}
		if (loader == null) {
			loader = new ImageLoader(getRequestQueue(context),
					new ImageCache() {
						@Override
						public void putBitmap(String key, Bitmap value) {
							lruCache.put(key, value);
						}

						@Override
						public Bitmap getBitmap(String key) {
							return lruCache.get(key);
						}
					});
		}
		return loader;
	}

	/**
	 * 做Post请求获取字符串 
	 * 
	 * @param context
	 * @param url
	 * @param callBack
	 */
	public void doStringPost(Context context, String url,
			final NetCallBack<String> callBack) {
		StringRequest req = new StringRequest(Method.POST, url,
				new Listener<String>() {
					@Override
					public void onResponse(String resp) {
						callBack.onSuccess(resp);
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						callBack.onError(error.getMessage());
					}
				});
		getRequestQueue(context).add(req);
	}

	/**
	 * 做Post请求获取Json字符串
	 * 
	 * @param context
	 * @param url
	 * @param requestJsonObj
	 * @param callBack
	 */
	public void doJsonPost(Context context, String url,
			JSONObject requestJsonObj, final NetCallBack<JSONObject> callBack) {
		JsonObjectRequest req = new JsonObjectRequest(Method.POST, url,
				requestJsonObj, new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject responseObj) {
						callBack.onSuccess(responseObj);
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						callBack.onError(error.getMessage());
					}
				});
		getRequestQueue(context).add(req);

	}

	/**
	 * 做Get请求获取字符串
	 * 
	 * @param context
	 * @param url
	 * @param callBack
	 */
	public void doStringGet(Context context, String url,
			final NetCallBack<String> callBack) {
		StringRequest req = new StringRequest(Method.GET, url,
				new Listener<String>() {
					@Override
					public void onResponse(String resp) {
						callBack.onSuccess(resp);
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						callBack.onError(error.getMessage());
					}
				});
		getRequestQueue(context).add(req);
	}

	/**
	 * 做Get请求获取Json对象
	 * 
	 * @param context
	 * @param url
	 * @param requestJsonObj
	 * @param callBack
	 */
	public void doJsonGet(Context context, String url,
			JSONObject requestJsonObj, final NetCallBack<JSONObject> callBack) {
		
		JsonObjectRequest req = new JsonObjectRequest(Method.GET, url,
				requestJsonObj, new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject responseObj) {
						callBack.onSuccess(responseObj);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						callBack.onError(error.getMessage());
					}
				});
		getRequestQueue(context).add(req);
	}

	/**
	 * 从网络中获取图片
	 * 
	 * @param context
	 * @param url
	 * @param maxWidth
	 * @param maxHeight
	 * @param decodeConfig
	 * @param callBack
	 */
	public void getImage(Context context, String url, int maxWidth,
			int maxHeight, Config decodeConfig,
			final NetCallBack<Bitmap> callBack) {
		ImageRequest req = new ImageRequest(url, new Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap bitmap) {
				callBack.onSuccess(bitmap);
			}
		}, maxWidth, maxHeight, decodeConfig, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				callBack.onError(error.getMessage());
			}
		});
		getRequestQueue(context).add(req);
	}

	/**
	 * 从网络中获取图片，使用默认的图片配置
	 * 
	 * @param context
	 * @param url
	 * @param callBack
	 */
	public void getImage(Context context, String url,
			final NetCallBack<Bitmap> callBack) {
		getImage(context, url, 0, 0, null, callBack);
	}

	/**
	 * 初始化请求队列
	 * 
	 * @param context
	 * @return
	 */
	private RequestQueue getRequestQueue(Context context) {
		initVolley(context);
		return mQueue;
	}

	/**
	 * 网络回调接口
	 * 
	 * @author Ted
	 * @param <T>
	 */
	public interface NetCallBack<T> {
		public void onSuccess(T resp);

		public void onError(String errorMsg);
	}

	/*******************************************************************************************
	 * 使用HttpClient实现异步网络请求
	 *******************************************************************************************/

//	private HttpClient getHttpClient() {
//		if (httpClient == null) {
//			httpClient = new HttpClient();
//			initHttpClient();
//		}
//		return httpClient;
//	}
//
//	private void initHttpClient() {
//		HttpClientParams params = new HttpClientParams();
//		params.setSoTimeout(SO_TIME_OUT);
//		httpClient.setParams(params);
//	}

//	/**
//	 * 进行基础的Post请求，返回HttpResponse对象
//	 * @param url
//	 * @param params
//	 * @param callBack
//	 */
//	public void doSyncBasePost(String url,Map<String, String> params,NetCallBack<HttpResponse> callBack){
//        HttpPost httpPost = new HttpPost(url); 
//        List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>(); 
//        for(String name : params.keySet()){
//        	pairs.add(new BasicNameValuePair(name, String.valueOf(params.get(name))));
//        }
//        HttpResponse httpResponse = null; 
//        try { 
//        	HttpEntity entity = new UrlEncodedFormEntity(pairs, HTTP.UTF_8);
//            httpPost.setEntity(entity);
//            DefaultHttpClient client = new DefaultHttpClient();
//            httpResponse = client.execute(httpPost);
//            int status = httpResponse.getStatusLine().getStatusCode();
//            if (status == 200) {
//            	List<Cookie> list = client.getCookieStore().getCookies();
//            	for(Cookie cookie : list){
//            		httpResponse.addHeader(cookie.getName(),new TurboCookie(cookie).toString());
//            	}
//                callBack.onSuccess(httpResponse);
//            }else {
//				callBack.onError("状态码： "+status);
//			} 
//        } catch (ClientProtocolException e) { 
//            e.printStackTrace(); 
//            callBack.onError(e.getMessage());
//        } catch (IOException e) { 
//            e.printStackTrace();
//            callBack.onError(e.getMessage());
//        } 
//	}
//	
//	/**
//	 * 做基础的Get请求，成功时回传HttpResponse对象
//	 * @param url
//	 * @param params
//	 * @param callBack
//	 */
//	public void doSyncBaseGet(String url,Map<String, String> params,final NetCallBack<HttpResponse> callBack){
//		HttpGet httpGet = new HttpGet(URLHelper.buildUrl(url, params));
//		HttpResponse httpResponse = null;
//		try {
//			DefaultHttpClient client = new DefaultHttpClient();
//			httpResponse = client.execute(httpGet);
//			int status = httpResponse.getStatusLine().getStatusCode();
//			if(200 == status){
//				
//				List<Cookie> list = client.getCookieStore().getCookies();
//            	for(Cookie cookie : list){
//            		httpResponse.addHeader(cookie.getName(),new TurboCookie(cookie).toString());
//            	}
//				callBack.onSuccess(httpResponse);
//			}else{
//				callBack.onError("错误状态码： "+status);
//			}
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//			callBack.onError(e.getMessage());
//		} catch (IOException e) {
//			e.printStackTrace();
//			callBack.onError(e.getMessage());
//		}
//	}
//	
//	
//	/**
//	 * 做Get请求，返回String对象
//	 * @param url
//	 * @param params
//	 * @param callBack
//	 */
//	public void  doSyncStringGet(String url,Map<String, String> params,final NetCallBack<String> callBack){
//		doSyncBaseGet(url, params, new NetCallBack<HttpResponse>() {
//			@Override
//			public void onSuccess(HttpResponse resp) {
//				HttpEntity httpEntity = resp.getEntity();
//				try {
//					String result = EntityUtils.toString(httpEntity, HTTP.UTF_8);
//					if(result != null){
//						callBack.onSuccess(result);
//					}else{
//						callBack.onError("Response的消息体为空！");
//					}
//				} catch (ParseException e) {
//					e.printStackTrace();
//					callBack.onError(e.getMessage());
//				} catch (IOException e) {
//					e.printStackTrace();
//					callBack.onError(e.getMessage());
//				}
//			}
//
//			@Override
//			public void onError(String errorMsg) {
//				callBack.onError(errorMsg);
//			}
//		});
//	}
//	
//	/**
//	 * 做Post请求，返回String数据
//	 * @param url
//	 * @param params
//	 * @param callBack
//	 * @return
//	 */
//	public void doSyncStringPost(String url,Map<String, String> params,final NetCallBack<String> callBack){
//		doSyncBasePost(url, params, new NetCallBack<HttpResponse>() {
//			@Override
//			public void onSuccess(HttpResponse resp) {
//                try {
//                	HttpEntity httpEntity = resp.getEntity();
//					String result = EntityUtils.toString(httpEntity);
//					callBack.onSuccess(result);
//				} catch (ParseException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			@Override
//			public void onError(String errorMsg) {
//				callBack.onError(errorMsg);
//			}
//		});
//	}
	
	/**
	 * 使用HttpURLConnection访问网络
	 * @param spec
	 * @param params
	 * @param callBack
	 */
	public void doConnectionBaseGet(String spec,Map<String, String> params, NetCallBack<HttpURLConnection> callBack){
		HttpURLConnection conn = null;
		try {
			URL url = new URL(spec);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			URLHelper.buildConnectionParams(conn, params);
			conn.connect();
			int respCode = conn.getResponseCode();
			if(respCode == HttpStatus.SC_OK){
				callBack.onSuccess(conn);
			}else{
				callBack.onError("状态码： " + respCode);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
	}
	
	/**
	 * 使用HttpURLConnection访问网络
	 * @param spec
	 * @param params
	 * @param callBack
	 */
	public void doConnectionBasePost(String spec,Map<String, String> params, NetCallBack<HttpURLConnection> callBack){
		HttpURLConnection conn = null;
		try {
			URL url = new URL(spec);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			URLHelper.buildConnectionParams(conn, params);
			conn.connect();
			int respCode = conn.getResponseCode();
			if(respCode == HttpStatus.SC_OK){
				callBack.onSuccess(conn);
			}else{
				callBack.onError("状态码： " + respCode);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
	}
	
	/**
	 * 使用HttpUrlConnection获取String
	 * @param spec
	 * @param params
	 * @param callBack
	 */
	public void doConnectionStringGet(String spec,Map<String, String> params, final NetCallBack<String> callBack){
		doConnectionBaseGet(spec, params, new NetCallBack<HttpURLConnection>() {
			@Override
			public void onSuccess(HttpURLConnection resp) {
				try {
					InputStream is = resp.getInputStream();
					String result = FileHelper.inputStream2String(is);
					callBack.onSuccess(result);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onError(String errorMsg) {
				callBack.onError(errorMsg);
			}
		});
	}
	
	/**
	 * 使用HttpUrlConnection获取String
	 * @param spec
	 * @param params
	 * @param callBack
	 */
	public void doConnectionStringPost(String spec,Map<String, String> params, final NetCallBack<String> callBack){
		doConnectionBasePost(spec, params, new NetCallBack<HttpURLConnection>() {
			@Override
			public void onSuccess(HttpURLConnection resp) {
				try {
					InputStream is = resp.getInputStream();
					String result = FileHelper.inputStream2String(is);
					callBack.onSuccess(result);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onError(String errorMsg) {
				callBack.onError(errorMsg);
			}
		});
	}
	
	/**
	 * 获取网络资源的大小
	 * @param spec
	 * @param params
	 * @return
	 */
	public long getDownloadFileLengthByURL(String spec){
		HttpURLConnection conn = null;
		long length = 0L;
		try {
			URL url = new URL(spec);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10000);
			conn.connect();
			length = conn.getContentLength();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return length;
	}
	
	
	/***************************************************************************************** 
	 * 								使用TurboPool来调度网络请求
	 *****************************************************************************************/
	
	/**
	 * 初始化请求队列
	 * @return
	 */
	private IQueue getNetTaskQueue(){
		if (!isInitTurboNetTaskQueue) {
			if (netTaskQueue == null)
				netTaskQueue = new TurboNetTaskQueue();
			isInitTurboNetTaskQueue = true;
		}
		return netTaskQueue;
	}
	
	/**
	 * 使用Get方法异步获取数据
	 * @param context
	 * @param url
	 * @param headers
	 * @param callBack
	 */
	public void doTurboStringGet(String url,Map<String,String> headers,
			final NetCallBack<TurboStringResponse> callBack){
		TurboStringRequest req = new TurboStringRequest(url,null,headers,callBack);
		getNetTaskQueue().inQueue(req);
	}
	
	/**
	 * 使用Post方法异步获取数据
	 * @param url      请求URL
	 * @param params   请求消息体
	 * @param headers  请求Head
	 * @param callBack 请求回调
	 */
	public void doTurboStringPost(String url, Map<String, String> params,Map<String,String> headers,
			final NetCallBack<TurboStringResponse> callBack){
	    List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>(); 
        for(String name : params.keySet()){
            pairs.add(new BasicNameValuePair(name, String.valueOf(params.get(name))));
        }
        try {
            HttpEntity entity = new UrlEncodedFormEntity(pairs, HTTP.UTF_8);
            TurboStringRequest req = new TurboStringRequest(url,entity,headers,TurboNetworkUtil.METHOD_POST,callBack);
            getNetTaskQueue().inQueue(req);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * 使用Get方法异步获取数据
	 * @param url
	 * @param headers
	 * @param callBack
	 */
	public void doTurboJSONGet(String url,Map<String,String> headers,
			final NetCallBack<TurboJSONResponse> callBack){
		TurboJSONRequest req = new TurboJSONRequest(url,null,headers,callBack);
		getNetTaskQueue().inQueue(req);
	}
	
	/**
	 * 使用Post方法异步获取数据
	 * @param context
	 * @param url
	 * @param params
	 * @param headers
	 * @param callBack
	 */
	public void doTurboJSONPost(String url,Map<String, String> params,Map<String,String> headers,
			final NetCallBack<TurboJSONResponse> callBack){
	    
	    List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>(); 
        for(String name : params.keySet()){
            pairs.add(new BasicNameValuePair(name, String.valueOf(params.get(name))));
        }
        try {
            HttpEntity entity = new UrlEncodedFormEntity(pairs, HTTP.UTF_8);
            TurboJSONRequest req = new TurboJSONRequest(url,entity,headers,TurboNetworkUtil.METHOD_POST,callBack);
            getNetTaskQueue().inQueue(req);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
	}
}
