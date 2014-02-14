package com.turbo.net.impl;

import com.turbo.net.TurboBaseResponse;

import org.apache.http.HttpResponse;

/**
 * 字符串请求返回
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class TurboStringResponse extends TurboBaseResponse<String>{

    public TurboStringResponse(HttpResponse resp) {
        super(resp);
    }
    
    @Override
    public String take() {
        return getBody();
    }

}
