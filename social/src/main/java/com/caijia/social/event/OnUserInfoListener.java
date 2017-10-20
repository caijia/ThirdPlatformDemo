package com.caijia.social.event;

import com.caijia.social.api.PlatformManager;
import com.caijia.social.model.AuthResult;
import com.caijia.social.model.UserInfo;

/**
 * Created by cai.jia on 2017/6/11.
 */

public interface OnUserInfoListener {

    void onUserInfoStart(@PlatformManager.PlatformType int platform);

    void onUserInfoError(@PlatformManager.PlatformType int platform);

    void onUserInfoComplete(@PlatformManager.PlatformType int platform, UserInfo result,
                            AuthResult authResult);
}
