package com.jetsun.thirdplatformdemo.api.wx;

import android.support.annotation.NonNull;

import com.jetsun.thirdplatformdemo.Platform;
import com.jetsun.thirdplatformdemo.api.wx.model.WXToken;
import com.jetsun.thirdplatformdemo.api.wx.model.WXUserInfo;
import com.jetsun.thirdplatformdemo.event.OnUserInfoListener;
import com.jetsun.thirdplatformdemo.net.RspHandler;
import com.jetsun.thirdplatformdemo.net.SimpleHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cai.jia on 2017/6/9 0009
 */

class WXHttpApi {

    private static final String URL_WX_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private static final String URL_WX_USER_INFO = "https://api.weixin.qq.com/sns/userinfo";

    /**
     * @param code
     * @param rspHandler
     */
    void getWxToken(String code, String appId,String appSecret,@NonNull RspHandler rspHandler) {
        Map<String, Object> params = new HashMap<>();
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        SimpleHttpClient.getInstance().get(URL_WX_TOKEN, params, rspHandler);
    }

    void getUserInfo(String code,String appId,String appSecret,
                     final @NonNull OnUserInfoListener listener) {
        getWxToken(code,appId,appSecret,new RspHandler() {
            @Override
            public void onSuccess(String s) {
                try {
                    WXToken wxToken = WXToken.fromJson(s);
                    getUserInfo(wxToken.getToken(), wxToken.getOpenId(), new RspHandler() {

                        @Override
                        public void onSuccess(String s) {
                            WXUserInfo wxUserInfo = WXUserInfo.fromJson(s);
                            listener.onUserInfoComplete(Platform.WX, wxUserInfo);
                        }

                        @Override
                        public void onFailure(String s) {
                            listener.onUserInfoError(Platform.WX);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onUserInfoError(Platform.WX);
                }
            }

            @Override
            public void onFailure(String s) {
                listener.onUserInfoError(Platform.WX);
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
