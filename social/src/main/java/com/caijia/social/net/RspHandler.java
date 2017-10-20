package com.caijia.social.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by cai.jia on 2017/6/9 0009
 */

public class RspHandler {

    private static final int MESSAGE_SUCCESS = 200;
    private static final int MESSAGE_FAILURE = 404;

    private Handler handler;

    public RspHandler() {
        handler = new InternalHandler(this);
    }

    public void onSuccess(String s){

    }

    public void onFailure(String s){

    }

    private static class InternalHandler extends Handler{

        WeakReference<RspHandler> ref;

        InternalHandler(RspHandler handler) {
            super(Looper.getMainLooper());
            ref = new WeakReference<>(handler);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SUCCESS:{
                    if (ref.get() != null) {
                        String s = (String) msg.obj;
                        ref.get().onSuccess(s);
                    }
                    break;
                }

                case MESSAGE_FAILURE:{
                    if (ref.get() != null) {
                        String s = (String) msg.obj;
                        ref.get().onFailure(s);
                    }
                    break;
                }
            }
        }
    }

    public void sendSuccessMessage(String result) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_SUCCESS, result));
    }

    public void sendFailureMessage(String error) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_FAILURE, error));
    }
}
