package com.jetsun.thirdPlatform.event;

import com.jetsun.thirdPlatform.api.PlatformManager;
import com.jetsun.thirdPlatform.model.AuthResult;

/**
 * Created by cai.jia on 2017/6/11.
 */

public interface OnAuthListener {

    void onAuthStart(@PlatformManager.PlatformType int platform);

    void onAuthError(@PlatformManager.PlatformType int platform);

    void onAuthComplete(@PlatformManager.PlatformType int platform,AuthResult result);
}
