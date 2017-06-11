package com.jetsun.thirdplatformdemo.api.qq;

import com.jetsun.thirdplatformdemo.net.RspHandler;
import com.jetsun.thirdplatformdemo.net.SimpleHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cai.jia on 2017/6/11.
 */

public class QQHttpApi {

    private static final String URL_USE_INFO = "https://openmobile.qq.com/user/get_simple_userinfo";

    public void getUserInfo(String appId, String token, String openId, RspHandler rspHandler) {
        Map<String, Object> params = new HashMap<>();
        params.put("oauth_consumer_key", appId);
        params.put("access_token", token);
        params.put("openid", openId);
        params.put("format", "json");
        SimpleHttpClient.getInstance().get(URL_USE_INFO, params, rspHandler);
    }
}
