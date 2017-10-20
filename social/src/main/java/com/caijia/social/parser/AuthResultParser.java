package com.caijia.social.parser;

import com.caijia.social.model.AuthResult;

/**
 * Created by cai.jia on 2017/6/12 0012
 */

public interface AuthResultParser {

    AuthResult fromJson(String json);
}
