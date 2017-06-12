package com.jetsun.thirdPlatform.model;

import com.jetsun.thirdPlatform.parser.AuthResultParser;

/**
 * Created by cai.jia on 2017/6/12 0012
 */

public class AuthResult {

    private String token;
    private String openId;

    public static AuthResult fromJson(String s, AuthResultParser parser) {
        return parser.fromJson(s);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setAll(String token, String openId) {
        this.token = token;
        this.openId = openId;
    }

    @Override
    public String toString() {
        return "AuthResult{" +
                "token='" + token + '\'' +
                ", openId='" + openId + '\'' +
                '}';
    }
}
