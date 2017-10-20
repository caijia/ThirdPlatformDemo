package com.caijia.social.parser.authResult;

import com.caijia.social.model.AuthResult;
import com.caijia.social.parser.AuthResultParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cai.jia on 2017/6/12 0012
 */

public class SinaAuthParser implements AuthResultParser {

    @Override
    public AuthResult fromJson(String json) {
        AuthResult authResult = new AuthResult();
        try {
            JSONObject jo = new JSONObject(json);
            String token = jo.optString("access_token");
            String openId = jo.optString("openid");
            authResult.setAll(token, openId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return authResult;
    }
}
