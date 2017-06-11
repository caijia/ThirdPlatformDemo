package com.jetsun.thirdplatformdemo.api.wx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.jetsun.thirdplatformdemo.Platform;
import com.jetsun.thirdplatformdemo.api.wx.model.WXAuth;
import com.jetsun.thirdplatformdemo.api.wx.model.WXToken;
import com.jetsun.thirdplatformdemo.event.OnAuthListener;
import com.jetsun.thirdplatformdemo.event.OnUserInfoListener;
import com.jetsun.thirdplatformdemo.net.RspHandler;
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

public class WXApiHelper {

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

    public void init(Context context, String appId, String appSecret, boolean isInstall) {
        this.appId = appId;
        this.appSecret = appSecret;
        iwxapi = WXAPIFactory.createWXAPI(context, appId, isInstall);
        iwxapi.registerApp(appId);
    }

    private void checkInit() {
        if (iwxapi == null) {
            throw new RuntimeException("please invoke init method");
        }
    }

    public void destroy(Context context) {
        if (iwxapi != null) {
            iwxapi.unregisterApp();
            authRspListener = null;
            iwxapi = null;
        }
    }

    public void handleIntent(final Activity activity, Intent intent) {
        checkInit();
        iwxapi.handleIntent(intent, new IWXAPIEventHandler() {
            @Override
            public void onReq(BaseReq baseReq) {

            }

            @Override
            public void onResp(BaseResp baseResp) {
                SendAuth.Resp resp = (SendAuth.Resp) baseResp;
                WXAuth wxAuthResult = new WXAuth(resp.errCode, resp.code, resp.state,
                        resp.lang, resp.country);
                switch (resp.errCode) {
                    case SendAuth.Resp.ErrCode.ERR_OK: {
                        if (authRspListener != null) {
                            authRspListener.onAuthComplete(Platform.WX, wxAuthResult);
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
        req.scope = scope;
        req.state = System.currentTimeMillis() + "";
        iwxapi.sendReq(req);
        if (onRspListener != null) {
            onRspListener.onAuthStart(Platform.WX);
        }
    }

    private void getUserInfo(final String code, @Nullable String scope,
                             final @NonNull OnUserInfoListener onRspListener) {
        boolean needAuth = TextUtils.isEmpty(code);
        if (needAuth) {
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
                public void onAuthComplete(int platform, Object result) {
                    WXAuth wxAuth = (WXAuth) result;
                    httpApi.getUserInfo(wxAuth.getCode(), appId, appSecret, onRspListener);
                }
            });

        } else {
            httpApi.getUserInfo(code, appId, appSecret, onRspListener);
        }
    }

    public void getUserInfo(@Nullable String scope, @NonNull OnUserInfoListener onRspListener) {
        getUserInfo("", scope, onRspListener);
    }

    public void getUserInfo(@NonNull Activity act,@Nullable String scope, @NonNull OnUserInfoListener onRspListener) {
        getUserInfo(scope, onRspListener);
    }

    public void getUserInfo(@NonNull Fragment f,@Nullable String scope, @NonNull OnUserInfoListener onRspListener) {
        getUserInfo(scope, onRspListener);
    }


    public void doAuth(@NonNull Activity act, @Nullable String scope,
                       @NonNull final OnAuthListener onRspListener) {
        doAuth(act, null, scope, onRspListener);
    }

    public void doAuth(@NonNull Fragment f, @Nullable String scope,
                       @NonNull final OnAuthListener onRspListener) {
        doAuth(null, f, scope, onRspListener);
    }

    private void doAuth(@Nullable Activity act, @Nullable Fragment f,@Nullable String scope,
                       @NonNull final OnAuthListener onRspListener) {
        checkInit();
        String realScope = TextUtils.isEmpty(scope) ? SCOPE_USER_INFO : scope;
        getCode(realScope, new OnAuthListener() {
            @Override
            public void onAuthStart(int platform) {
                onRspListener.onAuthStart(platform);
            }

            @Override
            public void onAuthError(int platform) {
                onRspListener.onAuthError(platform);
            }

            @Override
            public void onAuthComplete(final int platform, Object result) {
                WXAuth wxAuth = (WXAuth) result;
                httpApi.getWxToken(wxAuth.getCode(), appId, appSecret, new RspHandler() {

                    @Override
                    public void onSuccess(String s) {
                        WXToken wxToken = WXToken.fromJson(s);
                        onRspListener.onAuthComplete(platform, wxToken);
                    }

                    @Override
                    public void onFailure(String s) {
                        onRspListener.onAuthError(platform);
                    }
                });
            }
        });
    }
}
