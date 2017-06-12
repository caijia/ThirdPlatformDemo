package com.jetsun.thirdPlatform.event;

import com.jetsun.thirdPlatform.api.PlatformManager;
import com.jetsun.thirdPlatform.model.AuthResult;
import com.jetsun.thirdPlatform.model.UserInfo;

/**
 * Created by cai.jia on 2017/6/11.
 */

public interface OnUserInfoListener {

    void onUserInfoStart(@PlatformManager.PlatformType int platform);

    void onUserInfoError(@PlatformManager.PlatformType int platform);

    void onUserInfoComplete(@PlatformManager.PlatformType int platform, UserInfo result,
                            AuthResult authResult);
}
