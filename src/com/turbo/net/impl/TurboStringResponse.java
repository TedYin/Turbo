package com.turbo.net.impl;

import com.turbo.net.TurboBaseResponse;

import org.apache.http.HttpResponse;

public class TurboStringResponse extends TurboBaseResponse<String>{

    public TurboStringResponse(HttpResponse resp) {
        super(resp);
    }
    
    @Override
    public String take() {
        return getBody();
    }

}
