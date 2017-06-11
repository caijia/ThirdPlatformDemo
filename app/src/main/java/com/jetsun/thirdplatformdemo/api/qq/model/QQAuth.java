package com.jetsun.thirdplatformdemo.api.qq.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * {
 * "ret":0,
 * "pay_token":"xxxxxxxxxxxxxxxx",
 * "pf":"openmobile_android",
 * "expires_in":"7776000",
 * "openid":"xxxxxxxxxxxxxxxxxxx",
 * "pfkey":"xxxxxxxxxxxxxxxxxxx",
 * "msg":"sucess",
 * "access_token":"xxxxxxxxxxxxxxxxxxxxx"
 * Created by cai.jia on 2017/6/11.
 */

public class QQAuth {

    private int ret;
    private String payToken;
    private String platform;
    private String expires;
    private String openId;
    private String platformKey;
    private String msg;
    private String token;

    public static QQAuth fromJson(String json) {
        QQAuth qqAuth = new QQAuth();
        try {
            JSONObject jo = new JSONObject(json);
            int ret = jo.optInt("ret");
            String payToken = jo.optString("pay_token");
            String platform = jo.optString("pf");
            String expires = jo.optString("expires_in");
            String openId = jo.optString("openid");
            String platformKey = jo.optString("pfkey");
            String msg = jo.optString("msg");
            String token = jo.optString("access_token");
            qqAuth.setAll(ret, payToken, platform, expires, openId, platformKey, msg, token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return qqAuth;
    }

    private void setAll(int ret, String payToken, String platform, String expires, String openId,
                        String platformKey, String msg, String token) {
        this.ret = ret;
        this.payToken = payToken;
        this.platform = platform;
        this.expires = expires;
        this.openId = openId;
        this.platformKey = platformKey;
        this.msg = msg;
        this.token = token;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getPayToken() {
        return payToken;
    }

    public void setPayToken(String payToken) {
        this.payToken = payToken;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPlatformKey() {
        return platformKey;
    }

    public void setPlatformKey(String platformKey) {
        this.platformKey = platformKey;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "QQAuth{" +
                "ret=" + ret +
                ", payToken='" + payToken + '\'' +
                ", platform='" + platform + '\'' +
                ", expires='" + expires + '\'' +
                ", openId='" + openId + '\'' +
                ", platformKey='" + platformKey + '\'' +
                ", msg='" + msg + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
