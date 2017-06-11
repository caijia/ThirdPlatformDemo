package com.jetsun.thirdplatformdemo.api.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.jetsun.thirdplatformdemo.Platform;
import com.jetsun.thirdplatformdemo.api.qq.model.QQAuth;
import com.jetsun.thirdplatformdemo.api.qq.model.QQUserInfo;
import com.jetsun.thirdplatformdemo.event.OnAuthListener;
import com.jetsun.thirdplatformdemo.event.OnUserInfoListener;
import com.jetsun.thirdplatformdemo.net.RspHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * QQ:  http://wiki.open.qq.com/wiki/mobile/SDK%E4%B8%8B%E8%BD%BD
 * Created by cai.jia on 2017/6/11.
 */

public class QQApiHelper {

    private static volatile QQApiHelper instance;
    private String appId;
    private String appSecret;
    private Tencent tencent;
    private QQHttpApi httpApi;
    private static final String SCOPE_USER_INFO = "get_user_info,add_t";

    private QQApiHelper() {
        httpApi = new QQHttpApi();
    }

    public static QQApiHelper getInstance() {
        if (instance == null) {
            synchronized (QQApiHelper.class) {
                if (instance == null) {
                    instance = new QQApiHelper();
                }
            }
        }
        return instance;
    }

    public void init(Context context, String appId, String appSecret, boolean isInstall) {
        this.appId = appId;
        this.appSecret = appSecret;
        tencent = Tencent.createInstance(appId, context);
    }

    public void destroy(Context context) {
        checkInit();
        tencent.logout(context);
        tencent = null;
        authListener = null;
    }

    private void checkInit() {
        if (tencent == null) {
            throw new RuntimeException("please invoke init method");
        }
    }

    private OnAuthListener authListener;

    /**
     * SCOPE = "get_user_info,add_t",所有权限用"all"
     * @param scope
     * @param authListener
     */
    private void doAuth(@Nullable Activity act,@Nullable Fragment f,@Nullable String scope,
                        @NonNull final OnAuthListener authListener) {
        checkInit();
        this.authListener = authListener;
        if (!tencent.isSessionValid()) {
            String realScope = TextUtils.isEmpty(scope) ? SCOPE_USER_INFO : scope;
            if (act != null) {
                tencent.login(act, realScope, createAndDispatchUiListener());

            } else if (f != null) {
                tencent.login(f, realScope, createAndDispatchUiListener());
            }
        }
    }

    public void doAuth(@NonNull Activity act, @Nullable String scope,
                       @NonNull OnAuthListener authListener) {
        doAuth(act, null, scope, authListener);
    }

    public void doAuth(@NonNull Fragment f, @Nullable String scope,
                      @NonNull OnAuthListener authListener) {
        doAuth(null, f, scope, authListener);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkInit();
        Tencent.onActivityResultData(requestCode, resultCode, data, createAndDispatchUiListener());
    }

    private void getUserInfo(@Nullable Activity act,@Nullable Fragment f,@Nullable String scope,
                            @NonNull final OnUserInfoListener listener) {
        checkInit();
        if (!tencent.isReady()) {
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
                public void onAuthComplete(int platform, Object result) {
                    httpApi.getUserInfo(appId,tencent.getAccessToken(),tencent.getOpenId(),
                            new RspHandler(){
                                @Override
                                public void onSuccess(String s) {
                                    QQUserInfo userInfo = QQUserInfo.fromJson(s);
                                    listener.onUserInfoComplete(Platform.QQ,userInfo);
                                }

                                @Override
                                public void onFailure(String s) {
                                    listener.onUserInfoError(Platform.QQ);
                                }
                            });
                }
            });
        }
    }

    public void getUserInfo(@NonNull Activity act,@Nullable String scope,
                             @NonNull final OnUserInfoListener listener) {
        getUserInfo(act, null, scope, listener);
    }

    public void getUserInfo(@NonNull Fragment f,@Nullable String scope,
                            @NonNull final OnUserInfoListener listener) {
        getUserInfo(null, f, scope, listener);
    }

    private IUiListener createAndDispatchUiListener() {
        return new IUiListener() {
            @Override
            public void onComplete(Object o) {
                if (o != null) {
                    QQAuth qqAuth = QQAuth.fromJson(o.toString());
                    tencent.setAccessToken(qqAuth.getToken(),qqAuth.getExpires());
                    tencent.setOpenId(qqAuth.getOpenId());
                    if (authListener != null) {
                        authListener.onAuthComplete(Platform.QQ, qqAuth);
                    }
                }
            }

            @Override
            public void onError(UiError uiError) {
                if (authListener != null) {
                    authListener.onAuthError(Platform.QQ);
                }
            }

            @Override
            public void onCancel() {
                if (authListener != null) {
                    authListener.onAuthError(Platform.QQ);
                }
            }
        };
    }
}
