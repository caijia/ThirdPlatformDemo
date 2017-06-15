package com.jetsun.thirdPlatform.net;

import android.content.Context;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by cai.jia on 2017/6/9 0009
 */

public class AsyncHttpRequest implements Callable<Boolean> {

    private RspHandler rspHandler;
    private HttpRequest request;
    private String url;
    private Map<String, Object> params;
    private Context context;

    public AsyncHttpRequest(Context context,HttpRequest request, String url,
                            Map<String,Object> params, RspHandler handler) {
        this.context = context;
        this.request = request;
        this.url = url;
        this.params = params;
        this.rspHandler = handler;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            String rsp = request.request(context,url, params);
            rspHandler.sendSuccessMessage(rsp);
        } catch (Exception e) {
            rspHandler.sendFailureMessage(e.getMessage());
        }
        return true;
    }
}
