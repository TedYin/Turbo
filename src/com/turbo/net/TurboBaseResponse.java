
package com.turbo.net;

import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;

import java.util.List;
import java.util.Map;

/**
 * 返回数据封装基础类
 * 
 * @author Ted
 */
public abstract class TurboBaseResponse<T> {
    private HttpResponse resp;
    private Map<String, String> headers;
    private String body;
    private List<Cookie> cookies;

    public TurboBaseResponse(HttpResponse resp) {
        this.resp = resp;
    }

    public HttpResponse getResp() {
        return resp;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    /**
     * 获取指定类型的数据
     * 
     * @return
     */
    public abstract T take();
}
