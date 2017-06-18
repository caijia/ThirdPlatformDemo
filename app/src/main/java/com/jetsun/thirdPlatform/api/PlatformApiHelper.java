package com.jetsun.thirdPlatform.api;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.jetsun.thirdPlatform.Platform;
import com.jetsun.thirdPlatform.event.OnAuthListener;
import com.jetsun.thirdPlatform.event.OnShareListener;
import com.jetsun.thirdPlatform.event.OnUserInfoListener;

/**
 * Created by cai.jia on 2017/6/12 0012
 */

public class PlatformApiHelper {

    private static volatile PlatformApiHelper instance;

    private PlatformApiHelper() {

    }

    public static PlatformApiHelper getInstance() {
        if (instance == null) {
            synchronized (PlatformApiHelper.class) {
                if (instance == null) {
                    instance = new PlatformApiHelper();
                }
            }
        }
        return instance;
    }

    private PlatformApi getPlatform(@PlatformManager.PlatformType int platform) {
        return PlatformManager.getInstance().getPlatform(platform);
    }

    public void doAuth(@PlatformManager.PlatformType int platform, @NonNull Activity act,
                       @Nullable String scope, @NonNull OnAuthListener authListener) {
        getPlatform(platform).doAuth(act, scope, authListener);
    }

    public void doAuth(@PlatformManager.PlatformType int platform, @NonNull Fragment f,
                       @Nullable String scope, @NonNull OnAuthListener authListener) {
        getPlatform(platform).doAuth(f, scope, authListener);
    }

    public void getUserInfo(@PlatformManager.PlatformType int platform, @NonNull Activity act,
                            @Nullable String scope, @NonNull OnUserInfoListener listener) {
        getPlatform(platform).getUserInfo(act, scope, listener);
    }

    public void getUserInfo(@PlatformManager.PlatformType int platform, @NonNull Fragment f,
                            @Nullable String scope, @NonNull OnUserInfoListener listener) {
        getPlatform(platform).getUserInfo(f, scope, listener);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        PlatformManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    public void handleIntent(Activity activity, Intent intent) {
        PlatformManager.getInstance().handleIntent(activity, intent);
    }

    public void share(@NonNull Activity act, @PlatformManager.ShareType int shareType, String title,
                      String desc, String imageUrl, String webUrl,
                      @Nullable OnShareListener onShareListener) {
        switch (shareType) {
            case Platform.WX:
            case Platform.WX_CIRCLE:
            case Platform.WX_COLLECT: {
                getPlatform(Platform.WX).share(act, shareType, title, desc, imageUrl, webUrl,
                        onShareListener);
                break;
            }

            case Platform.QQ:
            case Platform.QZONE: {
                getPlatform(Platform.QQ).share(act, shareType, title, desc, imageUrl, webUrl,
                        onShareListener);
                break;
            }

            case Platform.SINA:{
                getPlatform(Platform.SINA).share(act, shareType, title, desc, imageUrl, webUrl,
                        onShareListener);
                break;
            }
        }
    }
}
