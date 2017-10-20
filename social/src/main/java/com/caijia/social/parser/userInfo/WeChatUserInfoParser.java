package com.caijia.social.parser.userInfo;

import android.text.TextUtils;

import com.caijia.social.model.UserInfo;
import com.caijia.social.parser.UserInfoParser;

import org.json.JSONObject;

/**
 * Created by cai.jia on 2017/6/12 0012
 */

public class WeChatUserInfoParser implements UserInfoParser {

    @Override
    public UserInfo fromJson(String json) {
        UserInfo userInfo = new UserInfo();
        try {
            JSONObject jo = new JSONObject(json);
            String sex = jo.optString("sex");
            int genderInt = TextUtils.equals("1", sex) ? UserInfo.MAN : UserInfo.WOMAN;
            String nickName = jo.optString("nickname");
            String imageUrl = jo.optString("headimgurl");
            String unionid = jo.optString("unionid");
            userInfo.setAll(genderInt, nickName, imageUrl);
            userInfo.setUnionid(unionid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
