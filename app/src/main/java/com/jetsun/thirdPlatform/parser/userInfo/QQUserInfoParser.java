package com.jetsun.thirdPlatform.parser.userInfo;

import android.text.TextUtils;

import com.jetsun.thirdPlatform.model.UserInfo;
import com.jetsun.thirdPlatform.parser.UserInfoParser;

import org.json.JSONObject;

/**
 * Created by cai.jia on 2017/6/12 0012
 */

public class QQUserInfoParser implements UserInfoParser {

    @Override
    public UserInfo fromJson(String json) {
        UserInfo userInfo = new UserInfo();
        try {
            JSONObject jo = new JSONObject(json);
            String gender = jo.optString("gender");
            int genderInt = TextUtils.equals("男", gender) ? UserInfo.MAN : UserInfo.WOMAN;
            String nickName = jo.optString("nickname");
            String qqImg40 = jo.optString("figureurl_qq_1");
            String qqImg100 = jo.optString("figureurl_qq_2");
            String imageUrl = TextUtils.isEmpty(qqImg100) ? qqImg40 : qqImg100;
            userInfo.setAll(genderInt, nickName, imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
