package com.jetsun.thirdplatformdemo.api.wx.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * "access_token":"ACCESS_TOKEN",
 * "expires_in":7200,
 * "refresh_token":"REFRESH_TOKEN",
 * "openid":"OPENID",
 * "scope":"SCOPE"
 * Created by cai.jia on 2017/6/11.
 */

public class WXToken {

    private String token;
    private String expires;
    private String refreshToken;
    private String openId;
    private String scope;

    public static WXToken fromJson(String json) {
        WXToken tokenResult = new WXToken();
        try {
            JSONObject jo = new JSONObject(json);
            String token = jo.optString("access_token");
            String openId = jo.optString("openid");
            String refreshToken = jo.optString("refresh_token");
            String expires = jo.optString("expires_in");
            String scope = jo.optString("scope");
            tokenResult.setAll(token, openId, refreshToken, expires, scope);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tokenResult;
    }

    private void setAll(String token, String openId, String refreshToken, String expires,
                        String scope) {
        this.token = token;
        this.openId = openId;
        this.refreshToken = refreshToken;
        this.expires = expires;
        this.scope = scope;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
