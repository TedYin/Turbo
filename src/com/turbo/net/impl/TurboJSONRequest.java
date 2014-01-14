
package com.turbo.net.impl;

import com.turbo.common.URLHelper;
import com.turbo.net.TurboBaseRequest;
import com.turbo.net.TurboNetworkUtil;
import com.turbo.net.VolleyNetHelper.NetCallBack;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Map;

/**
 * 做JSON请求
 * @author Ted
 *
 */
public class TurboJSONRequest extends TurboBaseRequest<TurboJSONResponse> {

    public static final String KEY_HEADER = "HEADER";
    
    
    public TurboJSONRequest(String url, HttpEntity entity, Map<String, String> params,
            Map<String, String> headers, int method, NetCallBack<TurboJSONResponse> callBack) {
        setUrl(URLHelper.buildUrl(url, params));
        setEntity(entity);
        setHeaders(headers);
        setMethod(method);
        setCallBack(callBack);
    }

    public TurboJSONRequest(String url, HttpEntity entity, Map<String, String> headers, int method,
            NetCallBack<TurboJSONResponse> callBack) {
        setUrl(url);
        setEntity(entity);
        setHeaders(headers);
        setMethod(method);
        setCallBack(callBack);
    }

    public TurboJSONRequest(String url, HttpEntity entity, Map<String, String> headers,
            NetCallBack<TurboJSONResponse> callBack) {
        setUrl(url);
        setEntity(entity);
        setHeaders(headers);
        setCallBack(callBack);
    }

    @Override
    protected TurboJSONResponse doPost(DefaultHttpClient httpClient)
            throws ClientProtocolException, IOException {
        HttpPost post = new HttpPost(getUrl());
        post.setEntity(getEntity());
        HttpResponse resp = httpClient.execute(URLHelper.addHttpPostHeader(post, getHeaders()));
        TurboJSONResponse jsonResp = new TurboJSONResponse(resp);
        //设置Cookie
        jsonResp.setCookies(httpClient.getCookieStore().getCookies());
        //设置Body
        jsonResp.setBody(TurboNetworkUtil.getHttpResponseBody(resp));
        //设置Head
        jsonResp.setHeaders(TurboNetworkUtil.getHttpResponseHeader(resp));
        return jsonResp;
    }   

    @Override
    protected TurboJSONResponse doGet(DefaultHttpClient httpClient)
            throws ClientProtocolException, IOException {
        HttpGet get = new HttpGet(getUrl());
        HttpResponse resp = httpClient.execute(URLHelper.addHttpGetHeader(get, getHeaders()));
        TurboJSONResponse jsonResp = new TurboJSONResponse(resp);
        //设置Cookie
        jsonResp.setCookies(httpClient.getCookieStore().getCookies());
        //设置Body
        jsonResp.setBody(TurboNetworkUtil.getHttpResponseBody(resp));
        //设置Head
        jsonResp.setHeaders(TurboNetworkUtil.getHttpResponseHeader(resp));
        return jsonResp;
    }

    @Override
    public Object execute() {
        TurboJSONResponse result = null;
        try {
            DefaultHttpClient httpClient = TurboNetworkUtil.getHttpClient();
            if (isCanceled() || isPaused()) {
                return null;
            }
            if (getMethod() == TurboNetworkUtil.METHOD_GET) {
                result = doGet(httpClient);
            } else if (getMethod() == TurboNetworkUtil.METHOD_POST) {
                result = doPost(httpClient);
            }
            postSuccess(result);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            postError(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            postError(e.getMessage());
        }
        return result;
    }

    @Override
    public Object complete(Object obj) {
        return obj;
    }

}
