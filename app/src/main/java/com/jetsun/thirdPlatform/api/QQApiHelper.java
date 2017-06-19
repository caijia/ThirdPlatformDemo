package com.jetsun.thirdPlatform.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.jetsun.thirdPlatform.Platform;
import com.jetsun.thirdPlatform.event.OnAuthListener;
import com.jetsun.thirdPlatform.event.OnShareListener;
import com.jetsun.thirdPlatform.event.OnUserInfoListener;
import com.jetsun.thirdPlatform.model.AuthResult;
import com.jetsun.thirdPlatform.model.UserInfo;
import com.jetsun.thirdPlatform.net.RspHandler;
import com.jetsun.thirdPlatform.parser.authResult.QQAuthParser;
import com.jetsun.thirdPlatform.parser.userInfo.QQUserInfoParser;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * QQ:  http://wiki.open.qq.com/wiki/mobile/SDK%E4%B8%8B%E8%BD%BD
 * Created by cai.jia on 2017/6/11.
 */

class QQApiHelper implements PlatformApi {

    private static final String SCOPE_USER_INFO = "get_user_info,add_t";
    private static volatile QQApiHelper instance;
    private String appId;
    private String appSecret;
    private Tencent tencent;
    private QQHttpApi httpApi;
    private OnAuthListener authListener;

    private QQApiHelper() {
        httpApi = new QQHttpApi();
    }

    public static PlatformApi getInstance() {
        if (instance == null) {
            synchronized (QQApiHelper.class) {
                if (instance == null) {
                    instance = new QQApiHelper();
                }
            }
        }
        return instance;
    }

    @Override
    public void init(Context context, String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
        tencent = Tencent.createInstance(appId, context);
    }

    @Override
    public void destroy(Context context) {
        checkInit();
        authListener = null;
    }

    private void checkInit() {
        if (tencent == null) {
            throw new RuntimeException("please invoke init method");
        }
    }

    /**
     * SCOPE = "get_user_info,add_t",所有权限用"all"
     *
     * @param scope
     * @param authListener
     */
    private void doAuth(@Nullable Activity act, @Nullable Fragment f, @Nullable String scope,
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

    @Override
    public void doAuth(@NonNull Activity act, @Nullable String scope,
                       @NonNull OnAuthListener authListener) {
        doAuth(act, null, scope, authListener);
    }

    @Override
    public void doAuth(@NonNull Fragment f, @Nullable String scope,
                       @NonNull OnAuthListener authListener) {
        doAuth(null, f, scope, authListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkInit();
        Tencent.onActivityResultData(requestCode, resultCode, data, createAndDispatchUiListener());
    }

    @Override
    public void handleIntent(Activity activity, Intent intent) {

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
            public void onAuthComplete(int platform, final AuthResult result) {
                httpApi.getUserInfo(appId, result.getToken(), result.getOpenId(),
                        new RspHandler() {
                            @Override
                            public void onSuccess(String s) {
                                UserInfo userInfo = UserInfo.fromJson(s, new QQUserInfoParser());
                                listener.onUserInfoComplete(Platform.QQ, userInfo,result);
                            }

                            @Override
                            public void onFailure(String s) {
                                listener.onUserInfoError(Platform.QQ);
                            }
                        });
            }
        });
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

    private IUiListener createAndDispatchUiListener() {
        return new IUiListener() {
            @Override
            public void onComplete(Object o) {
                if (o != null) {
                    AuthResult authResult = AuthResult.fromJson(o.toString(), new QQAuthParser());
                    if (authListener != null) {
                        authListener.onAuthComplete(Platform.QQ, authResult);
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

            }
        };
    }

    private void share(@Nullable Activity act,@Nullable Fragment f, int type, String title,
                       String desc, String imageUrl, String webUrl,
                       @Nullable OnShareListener shareListener) {
        Activity a = act != null ? act : (f != null ? f.getActivity() : null);
        if (a == null || a.isFinishing()) {
            return;
        }

        if (!tencent.isSupportSSOLogin(a)) {
            return;
        }

        switch (type) {
            case Platform.QQ:{
                final Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, desc);
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,webUrl);
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,imageUrl);
                tencent.shareToQQ(a, params, dispatchShareListener(type, shareListener));
                break;
            }

            case Platform.QZONE:{
                final Bundle params = new Bundle();
                params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填
                params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, desc);//选填
                params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,webUrl);//必填
                ArrayList<String> imageUrls = new ArrayList<>();
                imageUrls.add(imageUrl);
                params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
                tencent.shareToQzone(a, params, dispatchShareListener(type, shareListener));
                break;
            }
        }
    }

    private IUiListener dispatchShareListener(final int type,@Nullable final OnShareListener shareListener){
        return new IUiListener() {
            @Override
            public void onComplete(Object o) {
                if (shareListener != null ) {
                    shareListener.onShareSuccess(type);
                }
            }

            @Override
            public void onError(UiError uiError) {
                if (shareListener != null) {
                    shareListener.onShareError(type);
                }
            }

            @Override
            public void onCancel() {

            }
        };
    }

    @Override
    public void share(@NonNull Activity act, int type, String title, String desc, String imageUrl,
                      String webUrl, @Nullable OnShareListener onShareListener) {
        share(act, null,type, title, desc, imageUrl, webUrl, onShareListener);
    }

    @Override
    public void share(@NonNull Fragment f, int type, String title, String desc, String imageUrl,
                      String webUrl, @Nullable OnShareListener onShareListener) {
        share(null, f,type, title, desc, imageUrl, webUrl, onShareListener);
    }
}
