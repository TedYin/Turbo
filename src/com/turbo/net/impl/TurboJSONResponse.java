package com.turbo.net.impl;

import com.turbo.net.TurboBaseResponse;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class TurboJSONResponse extends TurboBaseResponse<JSONObject>{

    public TurboJSONResponse(HttpResponse resp) {
        super(resp);
    }

    @Override
    public JSONObject take() {
        JSONObject obj = null;
        try {
            obj = new JSONObject(getBody());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
