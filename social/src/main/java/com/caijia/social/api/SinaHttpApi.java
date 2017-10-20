package com.caijia.social.api;

import com.caijia.social.net.RspHandler;
import com.caijia.social.net.SimpleHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cai.jia on 2017/6/12 0012
 */

class SinaHttpApi {

    private static final String URL_USE_INFO = "https://api.weibo.com/2/users/show.json";

    void getUserInfo(String token, String uId, RspHandler rspHandler) {
        Map<String, Object> params = new HashMap<>();
        params.put("access_token", token);
        params.put("uid", uId);
        SimpleHttpClient.getInstance().get(URL_USE_INFO, params, rspHandler);
    }
}
