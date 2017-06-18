package com.jetsun.thirdPlatform.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;

import com.jetsun.thirdPlatform.Platform;
import com.jetsun.thirdPlatform.event.OnAuthListener;
import com.jetsun.thirdPlatform.event.OnShareListener;
import com.jetsun.thirdPlatform.event.OnUserInfoListener;
import com.jetsun.thirdPlatform.model.AuthResult;
import com.jetsun.thirdPlatform.model.UserInfo;
import com.jetsun.thirdPlatform.net.BitmapUtil;
import com.jetsun.thirdPlatform.net.RspHandler;
import com.jetsun.thirdPlatform.net.SimpleHttpClient;
import com.jetsun.thirdPlatform.parser.authResult.WeChatAuthParser;
import com.jetsun.thirdPlatform.parser.userInfo.WeChatUserInfoParser;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;
import java.util.UUID;

import static com.jetsun.thirdPlatform.net.BitmapUtil.bitmapToByte;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneFavorite;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline;

/**
 * 微信：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=1417751808&token=&lang=zh_CN
 * Created by cai.jia on 2017/6/9 0009
 */

class WXApiHelper implements PlatformApi {

    private static final int MAX_SIZE = 32 * 1024;
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
        onShareListener = null;
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
                if (baseResp instanceof SendAuth.Resp) {
                    final SendAuth.Resp resp = (SendAuth.Resp) baseResp;
                    switch (resp.errCode) {
                        case SendAuth.Resp.ErrCode.ERR_OK: {
                            if (authRspListener != null) {
                                httpApi.getWxToken(resp.code, appId, appSecret, new RspHandler() {
                                    @Override
                                    public void onSuccess(String s) {
                                        AuthResult result = AuthResult.fromJson(s, new WeChatAuthParser());
                                        authRspListener.onAuthComplete(Platform.WX, result);
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

                } else if (baseResp instanceof SendMessageToWX.Resp) {
                    SendMessageToWX.Resp resp = (SendMessageToWX.Resp) baseResp;
                    String type = resp.transaction.substring(resp.transaction.lastIndexOf("/") + 1);
                    int typeInt = Integer.parseInt(type);
                    if (resp.errCode == SendMessageToWX.Resp.ErrCode.ERR_OK) {
                        if (onShareListener != null) {
                            onShareListener.onShareSuccess(typeInt);
                        }

                    }else{
                        if (onShareListener != null) {
                            onShareListener.onShareSuccess(typeInt);
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
        req.scope = TextUtils.isEmpty(scope) ? SCOPE_USER_INFO : scope;
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
                        createAndDispatchEvent(onRspListener, result));
            }
        });
    }

    private RspHandler createAndDispatchEvent(@NonNull final OnUserInfoListener listener,
                                              final AuthResult result) {
        return new RspHandler() {
            @Override
            public void onSuccess(String s) {
                UserInfo userInfo = UserInfo.fromJson(s, new WeChatUserInfoParser());
                listener.onUserInfoComplete(Platform.WX, userInfo, result);
            }

            @Override
            public void onFailure(String s) {
                listener.onUserInfoError(Platform.WX);
            }
        };
    }

    @Override
    public void getUserInfo(@NonNull Activity act, @Nullable String scope,
                            @NonNull OnUserInfoListener onRspListener) {
        getUserInfo(scope, onRspListener);
    }

    @Override
    public void getUserInfo(@NonNull Fragment f, @Nullable String scope,
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

    @Override
    public void share(@NonNull Activity act, int type, String title, String desc, String imageUrl,
                      String webUrl, @Nullable OnShareListener onShareListener) {
        share(act, null, type, title, desc, imageUrl, webUrl, onShareListener);
    }

    @Override
    public void share(@NonNull Fragment f, int type, String title, String desc, String imageUrl,
                      String webUrl, @Nullable OnShareListener onShareListener) {
        share(null, f, type, title, desc, imageUrl, webUrl, onShareListener);
    }

    private OnShareListener onShareListener;

    public void share(@Nullable Activity act, @Nullable Fragment f, final int type, String title,
                      String des, String imageUrl, String targetUrl,
                      @Nullable OnShareListener shareListener) {
        final Activity a = act != null ? act : (f != null ? f.getActivity() : null);
        if (a == null || a.isFinishing()) {
            if (shareListener != null) {
                shareListener.onShareError(type);
            }
            return;
        }

        if (!iwxapi.isWXAppInstalled()) {
            if (shareListener != null) {
                shareListener.onShareError(type);
            }
            return;
        }

        onShareListener = shareListener;

        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = targetUrl;
        final WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = title;
        msg.description = des;

        SimpleHttpClient.getInstance().downloadImage(a, imageUrl, new RspHandler() {
            @Override
            public void onSuccess(String s) {
                File saveFile = new File(s);
                setImageByte(a, msg, saveFile);
                sendToWX(msg, type);
            }
        });
    }

    private void setImageByte(Context context, WXMediaMessage msg, File file) {
        final int imageSize = dpToPx(context, 48);
        byte[] bytes = bitmapToByte(file, imageSize, imageSize, 100);
        if (bytes.length <= MAX_SIZE) {
            msg.thumbData = bytes;

        } else {
            int percent = (int) (MAX_SIZE / (bytes.length * 1f));
            msg.thumbData = BitmapUtil.bitmapToByte(file, imageSize, imageSize, percent);
        }
    }

    private void sendToWX(WXMediaMessage msg, int type) {
        if (msg == null || iwxapi == null) {
            return;
        }
        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = UUID.randomUUID().toString() + "/" + type; // transaction字段用于唯一标识一个请求,后面type只是方便回调时拿type值
        req.message = msg;
        req.scene = getWxType(type);

        // 调用api接口发送数据到微信
        iwxapi.sendReq(req);
    }

    private int getWxType(int type) {
        switch (type) {
            case Platform.WX:
                return WXSceneSession;

            case Platform.WX_CIRCLE:
                return WXSceneTimeline;

            case Platform.WX_COLLECT:
                return WXSceneFavorite;
        }
        return 0;
    }

    private int dpToPx(Context context, float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics()));
    }
}
