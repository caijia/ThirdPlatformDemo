package com.jetsun.thirdPlatform.parser.authResult;

import com.jetsun.thirdPlatform.model.AuthResult;
import com.jetsun.thirdPlatform.parser.AuthResultParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cai.jia on 2017/6/12 0012
 */

public class QQAuthParser implements AuthResultParser {

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
