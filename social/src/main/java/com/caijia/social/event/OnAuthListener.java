package com.caijia.social.event;

import com.caijia.social.api.PlatformManager;
import com.caijia.social.model.AuthResult;

/**
 * Created by cai.jia on 2017/6/11.
 */

public interface OnAuthListener {

    void onAuthStart(@PlatformManager.PlatformType int platform);

    void onAuthError(@PlatformManager.PlatformType int platform);

    void onAuthComplete(@PlatformManager.PlatformType int platform,AuthResult result);
}
