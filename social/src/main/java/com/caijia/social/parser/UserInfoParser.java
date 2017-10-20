package com.caijia.social.parser;


import com.caijia.social.model.UserInfo;

/**
 * Created by cai.jia on 2017/6/12 0012
 */

public interface UserInfoParser {

    UserInfo fromJson(String json);
}
