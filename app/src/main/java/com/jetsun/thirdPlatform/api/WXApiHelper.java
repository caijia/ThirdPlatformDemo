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
import com.jetsun.thirdPlatform.parser.authResult.WeChatAuthParser;
import com.jetsun.thirdPlatform.parser.userInfo.WeChatUserInfoParser;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=1417751808&token=&lang=zh_CN
 * Created by cai.jia on 2017/6/9 0009
 */

class WXApiHelper implements PlatformApi {

    private static final String SCOPE_USER_INFO = "snsapi_userinfo";
    private static volatile WXApiHelper instance;
    private String appId;
    private String appSecret;
    private IWXAPI iwxapi;
    private WXHttpApi httpApi;
    private OnAuthListener authRspListener;

    private WXApiHelper() {
        httpApi = new WXHttpApi();
    }

    public static WXApiHelper getInstance() {
        if (instance == null) {
            synchronized (WXApiHelper.class) {
                if (instance == null) {
                    instance = new WXApiHelper();
                }
            }
        }
        return instance;
    }

    @Override
    public void init(Context context, String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
        iwxapi = WXAPIFactory.createWXAPI(context, appId, false);
        iwxapi.registerApp(appId);
    }

    private void checkInit() {
        if (iwxapi == null) {
            throw new RuntimeException("please invoke init method");
        }
    }

    @Override
    public void destroy(Context context) {
        authRspListener = null;
    }

    @Override
    public void handleIntent(final Activity activity, Intent intent) {
        checkInit();
        iwxapi.handleIntent(intent, new IWXAPIEventHandler() {
            @Override
            public void onReq(BaseReq baseReq) {

            }

            @Override
            public void onResp(BaseResp baseResp) {
                final SendAuth.Resp resp = (SendAuth.Resp) baseResp;
                switch (resp.errCode) {
                    case SendAuth.Resp.ErrCode.ERR_OK: {
                        if (authRspListener != null) {
                            httpApi.getWxToken(resp.code,appId,appSecret,new RspHandler(){
                                @Override
                                public void onSuccess(String s) {
                                    AuthResult result = AuthResult.fromJson(s, new WeChatAuthParser());
                                    authRspListener.onAuthComplete(Platform.WX,result);
                                }

                                @Override
                                public void onFailure(String s) {
                                    authRspListener.onAuthError(Platform.WX);
                                }
                            });
                        }
                        break;
                    }

                    default: {
                        if (authRspListener != null) {
                            authRspListener.onAuthError(Platform.WX);
                        }
                    }
                }

                activity.finish();
            }
        });
    }

    private void getCode(String scope, OnAuthListener onRspListener) {
        checkInit();
        this.authRspListener = onRspListener;
        SendAuth.Req req = new SendAuth.Req();
        String realScope = TextUtils.isEmpty(scope) ? SCOPE_USER_INFO : scope;
        req.scope = realScope;
        req.state = System.currentTimeMillis() + "";
        iwxapi.sendReq(req);
        if (onRspListener != null) {
            onRspListener.onAuthStart(Platform.WX);
        }
    }

    private void getUserInfo(@Nullable String scope, @NonNull final OnUserInfoListener onRspListener) {
        String realScope = TextUtils.isEmpty(scope) ? SCOPE_USER_INFO : scope;
        getCode(realScope, new OnAuthListener() {
            @Override
            public void onAuthStart(int platform) {
                onRspListener.onUserInfoStart(platform);
            }

            @Override
            public void onAuthError(int platform) {
                onRspListener.onUserInfoError(platform);
            }

            @Override
            public void onAuthComplete(final int platform, AuthResult result) {
                httpApi.getUserInfo(result.getToken(), result.getOpenId(),
                        createAndDispatchEvent(onRspListener,result));
            }
        });
    }

    private RspHandler createAndDispatchEvent(@NonNull final OnUserInfoListener listener,
                                              final AuthResult result) {
        return new RspHandler() {
            @Override
            public void onSuccess(String s) {
                UserInfo userInfo = UserInfo.fromJson(s, new WeChatUserInfoParser());
                listener.onUserInfoComplete(Platform.WX, userInfo,result);
            }

            @Override
            public void onFailure(String s) {
                listener.onUserInfoError(Platform.WX);
            }
        };
    }

    @Override
    public void getUserInfo(@NonNull Activity act,@Nullable String scope,
                            @NonNull OnUserInfoListener onRspListener) {
        getUserInfo(scope, onRspListener);
    }

    @Override
    public void getUserInfo(@NonNull Fragment f,@Nullable String scope,
                            @NonNull OnUserInfoListener onRspListener) {
        getUserInfo(scope, onRspListener);
    }

    @Override
    public void doAuth(@NonNull Activity act, @Nullable String scope,
                       @NonNull final OnAuthListener onRspListener) {
        doAuth(act, null, scope, onRspListener);
    }

    @Override
    public void doAuth(@NonNull Fragment f, @Nullable String scope,
                       @NonNull final OnAuthListener onRspListener) {
        doAuth(null, f, scope, onRspListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void doAuth(@Nullable Activity act, @Nullable Fragment f, @Nullable String scope,
                        @NonNull final OnAuthListener onAuthListener) {
        checkInit();
        String realScope = TextUtils.isEmpty(scope) ? SCOPE_USER_INFO : scope;
        getCode(realScope, onAuthListener);
    }
}