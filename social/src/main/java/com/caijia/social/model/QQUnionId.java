package com.caijia.social.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cai.jia on 2017/6/27 0027
 */

public class QQUnionId {

    /**
     * client_id : 100808012
     * openid : 2C940DB344814A3F6EA5AD67EF0D926F
     * unionid : UID_DF79F3599F94A64EFEDB7C2FB172A28D
     */

    private String client_id;
    private String openid;
    private String unionid;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public QQUnionId(String client_id, String openid, String unionid) {
        this.client_id = client_id;
        this.openid = openid;
        this.unionid = unionid;
    }

    public QQUnionId() {
    }

    public static QQUnionId parser(String s) {
        try {
            Pattern p = Pattern.compile("callback\\((.*)\\);");
            Matcher matcher = p.matcher(s);
            if (matcher.find() && matcher.groupCount() > 0) {
                String json = matcher.group(1);
                JSONObject jo = new JSONObject(json);
                String clientId = jo.optString("client_id");
                String openId = jo.optString("openid");
                String unionId = jo.optString("unionid");
                return new QQUnionId(clientId, openId, unionId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
