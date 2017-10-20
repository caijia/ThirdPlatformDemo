package com.caijia.social.net;

import android.content.Context;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static com.caijia.social.net.HttpUtils.getUrlWithQueryString;
import static com.caijia.social.net.HttpUtils.streamToString;

/**
 * Created by cai.jia on 2017/6/9 0009
 */

public class ReqGet implements HttpRequest {

    private static final String GET = "GET";
    private static final int SUCCESS_CODE = 200;

    @Override
    public String request(Context context, String url, Map<String, Object> params) throws Exception {
        HttpURLConnection conn = null;
        try {
            String realUrl = getUrlWithQueryString(url, params);
            URL urlConn = new URL(realUrl);
            conn = (HttpURLConnection) urlConn.openConnection();
            conn.setRequestMethod(GET);
            if (conn.getResponseCode() == SUCCESS_CODE) {
                InputStream in = conn.getInputStream();
                return streamToString(in);
            }

        } catch (Exception e) {

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        throw new RuntimeException("error");
    }

}
