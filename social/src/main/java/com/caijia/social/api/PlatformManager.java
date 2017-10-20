package com.caijia.social.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.v4.util.ArrayMap;

import com.caijia.social.Platform;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by cai.jia on 2017/6/12 0012
 */

public class PlatformManager {

    private static volatile PlatformManager instance;
    private ArrayMap<Integer, PlatformApi> apiMap;

    private PlatformManager() {
        apiMap = new ArrayMap<>();
    }

    public static PlatformManager getInstance() {
        if (instance == null) {
            synchronized (PlatformManager.class) {
                if (instance == null) {
                    instance = new PlatformManager();
                }
            }
        }
        return instance;
    }

    public PlatformApi getPlatform(@PlatformType int platform) {
        PlatformApi platformApi = apiMap.get(platform);
        if (platformApi == null) {
            throw new RuntimeException("PlatformApi is null,please config platform info");
        }
        return platformApi;
    }

    public void init(@PlatformType int platform, Context context, String appId, String appSecret) {
        PlatformApi instance = null;
        switch (platform) {
            case Platform.QQ: {
                instance = QQApiHelper.getInstance();
                break;
            }

            case Platform.SINA: {
                instance = SinaApiHelper.getInstance();
                break;
            }

            case Platform.WX: {
                instance = WXApiHelper.getInstance();
                break;
            }
        }
        if (instance != null) {
            instance.init(context, appId, appSecret);
        }
        apiMap.put(platform, instance);
    }

    public void destroy(Context context) {
        for (PlatformApi platformApi : apiMap.values()) {
            platformApi.destroy(context);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (PlatformApi platformApi : apiMap.values()) {
            platformApi.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void handleIntent(Activity activity, Intent intent) {
        for (PlatformApi platformApi : apiMap.values()) {
            platformApi.handleIntent(activity, intent);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Platform.QQ, Platform.SINA, Platform.WX})
    public @interface PlatformType {

    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Platform.QQ, Platform.SINA, Platform.WX,Platform.WX_CIRCLE,Platform.WX_COLLECT,
            Platform.QZONE})
    public @interface ShareType{

    }
}
