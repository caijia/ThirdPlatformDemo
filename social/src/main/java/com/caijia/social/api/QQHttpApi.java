package com.caijia.social.api;

import com.caijia.social.net.RspHandler;
import com.caijia.social.net.SimpleHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cai.jia on 2017/6/11.
 */

class QQHttpApi {

    private static final String URL_USE_INFO = "https://openmobile.qq.com/user/get_simple_userinfo";
    private static final String URL_QQ_UNIONID = "https://graph.qq.com/oauth2.0/me";

    void getUserInfo(String appId, final String token, String openId, final RspHandler rspHandler) {
        Map<String, Object> params = new HashMap<>();
        params.put("oauth_consumer_key", appId);
        params.put("access_token", token);
        params.put("openid", openId);
        params.put("format", "json");
        SimpleHttpClient.getInstance().get(URL_USE_INFO, params, rspHandler);
    }

    void getQQUnionId(String token, RspHandler rspHandler) {
        Map<String, Object> params = new HashMap<>();
        params.put("access_token", token);
        params.put("unionid", 1);
        SimpleHttpClient.getInstance().get(URL_QQ_UNIONID, params, rspHandler);
    }
}
