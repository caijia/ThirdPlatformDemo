package com.jetsun.thirdPlatform.net;

import android.text.TextUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static com.jetsun.thirdPlatform.net.HttpUtils.streamToString;

/**
 * Created by cai.jia on 2017/6/9 0009
 */

public class ReqPost implements HttpRequest {

    private static final String POST = "POST";
    private static final int SUCCESS_CODE = 200;

    @Override
    public String request(String url, Map<String, Object> params) throws Exception {
        HttpURLConnection conn = null;
        try {
            URL urlConn = new URL(url);
            conn = (HttpURLConnection) urlConn.openConnection();
            conn.setRequestMethod(POST);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream out = conn.getOutputStream();
            String data = HttpUtils.getParamString(params);
            if (!TextUtils.isEmpty(data)) {
                out.write(data.getBytes());
            }

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
