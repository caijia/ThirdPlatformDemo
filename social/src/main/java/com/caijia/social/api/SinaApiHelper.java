package com.caijia.social.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;

import com.caijia.social.Platform;
import com.caijia.social.event.OnAuthListener;
import com.caijia.social.event.OnShareListener;
import com.caijia.social.event.OnUserInfoListener;
import com.caijia.social.model.AuthResult;
import com.caijia.social.model.UserInfo;
import com.caijia.social.net.BitmapUtil;
import com.caijia.social.net.RspHandler;
import com.caijia.social.net.SimpleHttpClient;
import com.caijia.social.parser.userInfo.SinaUserInfoParser;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

import java.io.File;

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
        final AuthInfo authInfo = new AuthInfo(context, appId, REDIRECT_URL, SCOPE);
        WbSdk.install(context, authInfo);
    }

    @Override
    public void destroy(Context context) {
        ssoHandler = null;
        shareHandler = null;
        shareListener = null;
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
        if (shareHandler != null) {
            shareHandler.doResultIntent(intent, new WbShareCallback() {
                @Override
                public void onWbShareSuccess() {
                    if (shareListener != null) {
                        shareListener.onShareSuccess(Platform.SINA);
                    }
                }

                @Override
                public void onWbShareCancel() {
                    if (shareListener != null) {
                        shareListener.onShareError(Platform.SINA);
                    }
                }

                @Override
                public void onWbShareFail() {
                    if (shareListener != null) {
                        shareListener.onShareError(Platform.SINA);
                    }
                }
            });
        }
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
                        createAndDispatchRspHandler(listener, result));
            }
        });
    }

    private RspHandler createAndDispatchRspHandler(final OnUserInfoListener listener,
                                                   final AuthResult result) {
        return new RspHandler() {
            @Override
            public void onSuccess(String s) {
                UserInfo userInfo = UserInfo.fromJson(s, new SinaUserInfoParser());
                listener.onUserInfoComplete(Platform.SINA, userInfo, result);
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
        Activity wbAct = act != null ? act : (f != null ? f.getActivity() : null);
        if (wbAct == null || wbAct.isFinishing()) {
            authListener.onAuthError(Platform.SINA);
            return;
        }
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

    private WbShareHandler shareHandler;
    private OnShareListener shareListener;
    private static final int MAX_SIZE = 32 * 1024;

    private void share(@Nullable Activity act, @Nullable Fragment f, int type, final String title,
                       final String desc, final String imageUrl, final String webUrl,
                       @Nullable OnShareListener onShareListener) {
        final Activity a = act != null ? act : (f != null ? f.getActivity() : null);
        if (a == null || a.isFinishing()) {
            if (onShareListener != null) {
                onShareListener.onShareError(type);
            }
            return;
        }

        this.shareListener = onShareListener;
        shareHandler = new WbShareHandler(a);
        shareHandler.registerApp();

        final WeiboMultiMessage multiMessage = new WeiboMultiMessage();
        SimpleHttpClient.getInstance().downloadImage(a, imageUrl, new RspHandler() {
            @Override
            public void onSuccess(String path) {
                WebpageObject webpageObject = new WebpageObject();
                int imageSize = dpToPx(a, 48);
                File file = new File(path);
                byte[] bytes = BitmapUtil.bitmapToByte(file, imageSize, imageSize, 100);
                if (bytes.length <= MAX_SIZE) {
                    webpageObject.thumbData = bytes;

                } else {
                    int percent = (int) (MAX_SIZE / (bytes.length * 1f));
                    webpageObject.thumbData = BitmapUtil.bitmapToByte(file, imageSize, imageSize, percent);
                }

                webpageObject.title = title;
                webpageObject.description = desc;
                webpageObject.actionUrl = webUrl;
                multiMessage.mediaObject = webpageObject;
                shareHandler.shareMessage(multiMessage, false);
            }
        });
    }

    @Override
    public void share(@NonNull Activity act, int type, final String title, final String desc,
                      final String imageUrl, final String webUrl,
                      @Nullable OnShareListener onShareListener) {
        share(act, null,type, title, desc, imageUrl, webUrl, onShareListener);
    }

    @Override
    public void share(@NonNull Fragment f, int type, String title, String desc, String imageUrl,
                      String webUrl, @Nullable OnShareListener onShareListener) {
        share(null, f,type, title, desc, imageUrl, webUrl, onShareListener);
    }

    private int dpToPx(Context context, float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics()));
    }
}
