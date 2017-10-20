package com.caijia.social.parser.userInfo;

import android.text.TextUtils;

import com.caijia.social.model.UserInfo;
import com.caijia.social.parser.UserInfoParser;

import org.json.JSONObject;


/**
 * Created by cai.jia on 2017/6/12 0012
 */

public class SinaUserInfoParser implements UserInfoParser {

    @Override
    public UserInfo fromJson(String json) {
        UserInfo userInfo = new UserInfo();
        try {
            JSONObject jo = new JSONObject(json);
            String uId = jo.optString("idstr");
            String gender = jo.optString("gender");
            int genderInt = TextUtils.equals("f", gender) ? UserInfo.WOMAN : UserInfo.MAN;
            String nickName = jo.optString("screen_name");
            String imageUrl = jo.optString("avatar_large");
            userInfo.setAll(genderInt, nickName, imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
