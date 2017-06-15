package com.jetsun.thirdPlatform.net;

import android.content.Context;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cai.jia on 2017/6/9 0009
 */

public class SimpleHttpClient {

    private static volatile SimpleHttpClient client;
    private ExecutorService executorService;

    private SimpleHttpClient() {
        executorService = Executors.newCachedThreadPool();
    }

    public static SimpleHttpClient getInstance() {
        if (client == null) {
            synchronized (SimpleHttpClient.class) {
                if (client == null) {
                    client = new SimpleHttpClient();
                }
            }
        }
        return client;
    }

    public void get(String url, Map<String, Object> params, RspHandler handler) {
        ReqGet reqGet = new ReqGet();
        AsyncHttpRequest request = new AsyncHttpRequest(null,reqGet, url, params, handler);
        executorService.submit(request);
    }

    public void post(String url, Map<String, Object> params, RspHandler handler) {
        ReqPost reqPost = new ReqPost();
        AsyncHttpRequest request = new AsyncHttpRequest(null,reqPost, url, params, handler);
        executorService.submit(request);
    }

    public void downloadImage(Context context,String url,RspHandler handler) {
        ReqDownload reqPost = new ReqDownload();
        AsyncHttpRequest request = new AsyncHttpRequest(context,reqPost, url, null, handler);
        executorService.submit(request);
    }
}
