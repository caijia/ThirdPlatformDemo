package com.jetsun.thirdPlatform.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.jetsun.thirdPlatform.Platform;
import com.jetsun.thirdPlatform.event.OnAuthListener;
import com.jetsun.thirdPlatform.event.OnUserInfoListener;
import com.jetsun.thirdPlatform.model.AuthResult;
import com.jetsun.thirdPlatform.model.UserInfo;
import com.jetsun.thirdPlatform.net.RspHandler;
import com.jetsun.thirdPlatform.parser.userInfo.SinaUserInfoParser;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

/**
 * https://github.com/sinaweibosdk/weibo_android_sdk
 * Created by cai.jia on 2017/6/12 0012
 */
class SinaApiHelper implements PlatformApi {

    /**
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     */
    private static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    private static final String SCOPE = "all";
    private static volatile SinaApiHelper instance;
    private String appId;
    private String appSecret;
    private SinaHttpApi httpApi;
    private SsoHandler ssoHandler;

    private SinaApiHelper() {
        httpApi = new SinaHttpApi();
    }

    public static SinaApiHelper getInstance() {
        if (instance == null) {
            synchronized (SinaApiHelper.class) {
                if (instance == null) {
                    instance = new SinaApiHelper();
                }
            }
        }
        return instance;
    }

    @Override
    public void init(Context context, String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
    }

    @Override
    public void destroy(Context context) {
        ssoHandler = null;
    }

    private void checkInit() {
        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("please invoke init method");
        }
    }

    @Override
    public void doAuth(@NonNull Activity act, @Nullable String scope,
                       @NonNull final OnAuthListener authListener) {
        doAuth(act, null, scope, authListener);
    }

    @Override
    public void doAuth(@NonNull Fragment f, @Nullable String scope,
                       @NonNull final OnAuthListener authListener) {
        doAuth(null, f, scope, authListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void handleIntent(Activity activity, Intent intent) {

    }

    @Override
    public void getUserInfo(@NonNull Activity act, @Nullable String scope,
                            @NonNull final OnUserInfoListener listener) {
        getUserInfo(act, null, scope, listener);
    }

    @Override
    public void getUserInfo(@NonNull Fragment f, @Nullable String scope,
                            @NonNull final OnUserInfoListener listener) {
        getUserInfo(null, f, scope, listener);
    }

    private void getUserInfo(@Nullable Activity act, @Nullable Fragment f, @Nullable String scope,
                             @NonNull final OnUserInfoListener listener) {
        checkInit();
        doAuth(act, f, scope, new OnAuthListener() {
            @Override
            public void onAuthStart(int platform) {
                listener.onUserInfoStart(platform);
            }

            @Override
            public void onAuthError(int platform) {
                listener.onUserInfoError(platform);
            }

            @Override
            public void onAuthComplete(int platform, AuthResult result) {
                httpApi.getUserInfo(result.getToken(), result.getOpenId(),
                        createAndDispatchRspHandler(listener,result));
            }
        });
    }

    private RspHandler createAndDispatchRspHandler(final OnUserInfoListener listener,
                                                   final AuthResult result) {
        return new RspHandler() {
            @Override
            public void onSuccess(String s) {
                UserInfo userInfo = UserInfo.fromJson(s, new SinaUserInfoParser());
                listener.onUserInfoComplete(Platform.SINA, userInfo,result);
            }

            @Override
            public void onFailure(String s) {
                listener.onUserInfoError(Platform.SINA);
            }
        };
    }

    private void doAuth(@Nullable Activity act, @Nullable Fragment f, @Nullable String scope,
                        @NonNull final OnAuthListener authListener) {
        checkInit();
        Context context = act != null ? act : (f != null ? f.getContext() : null);
        if (context == null) {
            return;
        }
        final String realScope = TextUtils.isEmpty(scope) ? SCOPE : scope;
        final AuthInfo authInfo = new AuthInfo(context, appId, REDIRECT_URL, realScope);
        WbSdk.install(context, authInfo);

        Activity wbAct = act != null ? act : f.getActivity();
        authListener.onAuthStart(Platform.SINA);
        ssoHandler = new SsoHandler(wbAct);
        ssoHandler.authorize(new WbAuthListener() {
            @Override
            public void onSuccess(Oauth2AccessToken result) {
                AuthResult authResult = new AuthResult();
                authResult.setAll(result.getToken(), result.getUid());
                authListener.onAuthComplete(Platform.SINA, authResult);
            }

            @Override
            public void cancel() {
                authListener.onAuthError(Platform.SINA);
            }

            @Override
            public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
                authListener.onAuthError(Platform.SINA);
            }
        });
    }

    @Override
    public void share(Context context, int type, String title, String desc, String imageUrl,
                      String webUrl) {

    }
}
