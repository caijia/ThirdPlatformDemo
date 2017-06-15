package com.jetsun.thirdPlatform.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.jetsun.thirdPlatform.event.OnAuthListener;
import com.jetsun.thirdPlatform.event.OnUserInfoListener;

/**
 * Created by cai.jia on 2017/6/12 0012
 */

public interface PlatformApi {
    void init(Context context, String appId, String appSecret);

    void destroy(Context context);

    void doAuth(@NonNull Activity act, @Nullable String scope,
                @NonNull OnAuthListener authListener);

    void doAuth(@NonNull Fragment f, @Nullable String scope,
                @NonNull OnAuthListener authListener);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void getUserInfo(@NonNull Activity act, @Nullable String scope,
                     @NonNull OnUserInfoListener listener);

    void getUserInfo(@NonNull Fragment f, @Nullable String scope,
                     @NonNull OnUserInfoListener listener);

    void handleIntent(final Activity activity, Intent intent);

    void share(Context context,int type, String title, String desc, String imageUrl, String webUrl);
}
