package com.jetsun.thirdPlatform.parser;

import com.jetsun.thirdPlatform.model.UserInfo;

/**
 * Created by cai.jia on 2017/6/12 0012
 */

public interface UserInfoParser {

    UserInfo fromJson(String json);
}
