package com.jetsun.thirdplatformdemo.event;

/**
 * Created by cai.jia on 2017/6/11.
 */

public interface OnUserInfoListener {

    void onUserInfoStart(int platform);

    void onUserInfoError(int platform);

    void onUserInfoComplete(int platform, Object result);
}
