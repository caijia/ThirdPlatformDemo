package com.jetsun.thirdplatformdemo.api.wx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.jetsun.thirdplatformdemo.api.OnRspListener;
import com.jetsun.thirdplatformdemo.api.wx.model.WxAuthResult;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by cai.jia on 2017/6/9 0009
 */

public class WxApiHelper {

    private static final String SCOPE_USER_INFO = "snsapi_userinfo";
    private static volatile WxApiHelper instance;
    private String appId;
    private String appSecret;
    private IWXAPI iwxapi;
    private WxHttpApi httpApi;

    private WxApiHelper() {
        httpApi = new WxHttpApi();
    }

    public static WxApiHelper getInstance() {
        if (instance == null) {
            synchronized (WxApiHelper.class) {
                if (instance == null) {
                    instance = new WxApiHelper();
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

    private void checkWxApi() {
        if (iwxapi == null) {
            throw new RuntimeException("please invoke init method");
        }
    }

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void destroy() {
        if (iwxapi != null) {
            iwxapi.unregisterApp();
            authRspListener = null;
            iwxapi = null;
        }
    }

    public void handleIntent(final Activity activity, Intent intent) {
        checkWxApi();
        iwxapi.handleIntent(intent, new IWXAPIEventHandler() {
            @Override
            public void onReq(BaseReq baseReq) {

            }

            @Override
            public void onResp(BaseResp baseResp) {
                SendAuth.Resp resp = (SendAuth.Resp) baseResp;
                WxAuthResult wxAuthResult = new WxAuthResult(resp.errCode, resp.code, resp.state,
                        resp.lang, resp.country);
                if (authRspListener != null) {
                    authRspListener.onPlatformFinish(1,resp.errCode == 0 ,"",wxAuthResult);
                }
                activity.finish();
            }
        });
    }

    private OnRspListener authRspListener;

    private void getCode(String scope,OnRspListener onRspListener) {
        checkWxApi();
        this.authRspListener = onRspListener;
        SendAuth.Req req = new SendAuth.Req();
        req.scope = scope;
        req.state = System.currentTimeMillis() + "";
        iwxapi.sendReq(req);
        if (onRspListener != null) {
            onRspListener.onPlatformStart();
        }
    }

    public void getToken() {

    }

    public void getUserInfo(String code,final @NonNull OnRspListener onRspListener) {
        boolean needAuth = TextUtils.isEmpty(code);
        if (needAuth) {
            getCode(SCOPE_USER_INFO, new OnRspListener() {
                @Override
                public void onPlatformStart() {
                    onRspListener.onPlatformStart();
                }

                @Override
                public void onPlatformFinish(int platform, boolean success, String error, Object result) {
                    WxAuthResult authResult = (WxAuthResult) result;
                    if (success) {
                        httpApi.getUserInfo(authResult.getCode(),onRspListener);

                    }else{
                        onRspListener.onPlatformFinish(1, false, "", null);
                    }
                }
            });

        }else{
            httpApi.getUserInfo(code,onRspListener);
        }
    }

    public void getUserInfo(@NonNull OnRspListener onRspListener) {
        getUserInfo("",onRspListener);
    }
}
