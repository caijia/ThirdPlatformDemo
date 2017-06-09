package com.jetsun.thirdplatformdemo.api.wx;

import android.support.annotation.NonNull;

import com.jetsun.thirdplatformdemo.api.OnRspListener;
import com.jetsun.thirdplatformdemo.api.wx.model.WxUserInfo;
import com.jetsun.thirdplatformdemo.net.RspHandler;
import com.jetsun.thirdplatformdemo.net.SimpleHttpClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cai.jia on 2017/6/9 0009
 */

class WxHttpApi {

    private static final String URL_WX_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private static final String URL_WX_USER_INFO = "https://api.weixin.qq.com/sns/userinfo";

    /**
     * "access_token":"ACCESS_TOKEN",
     * "expires_in":7200,
     * "refresh_token":"REFRESH_TOKEN",
     * "openid":"OPENID",
     * "scope":"SCOPE"
     *
     * @param code
     * @param rspHandler
     */
    void getWxToken(String code, @NonNull RspHandler rspHandler) {
        WxApiHelper instance = WxApiHelper.getInstance();
        Map<String, Object> params = new HashMap<>();
        params.put("appid", instance.getAppId());
        params.put("secret", instance.getAppSecret());
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        SimpleHttpClient.getInstance().get(URL_WX_TOKEN, params, rspHandler);
    }

    void getUserInfo(String code, final @NonNull OnRspListener listener) {
        getWxToken(code, new RspHandler() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String token = jo.optString("access_token");
                    String openId = jo.optString("openid");
                    getUserInfo(token, openId, new RspHandler() {

                        @Override
                        public void onSuccess(String s) {
                            WxUserInfo wxUserInfo = WxUserInfo.fromJson(s);
                            listener.onPlatformFinish(1, true, "", wxUserInfo);
                        }

                        @Override
                        public void onFailure(String s) {
                            listener.onPlatformFinish(1, false, "", s);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onPlatformFinish(1, false, "", null);
                }
            }

            @Override
            public void onFailure(String s) {
                listener.onPlatformFinish(1, false, "", s);
            }
        });
    }

    void getUserInfo(String token, String openId, RspHandler rspHandler) {
        Map<String, Object> params = new HashMap<>();
        params.put("openid", openId);
        params.put("access_token", token);
        SimpleHttpClient.getInstance().get(URL_WX_USER_INFO, params, rspHandler);
    }

}
