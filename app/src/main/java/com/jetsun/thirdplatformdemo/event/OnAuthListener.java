package com.jetsun.thirdplatformdemo.event;

/**
 * Created by cai.jia on 2017/6/11.
 */

public interface OnAuthListener {

    void onAuthStart(int platform);

    void onAuthError(int platform);

    void onAuthComplete(int platform,Object result);
}
