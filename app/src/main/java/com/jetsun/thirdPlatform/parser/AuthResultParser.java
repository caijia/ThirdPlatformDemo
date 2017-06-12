package com.jetsun.thirdPlatform.parser;

import com.jetsun.thirdPlatform.model.AuthResult;

/**
 * Created by cai.jia on 2017/6/12 0012
 */

public interface AuthResultParser {

    AuthResult fromJson(String json);
}
