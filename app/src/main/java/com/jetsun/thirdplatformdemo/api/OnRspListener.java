package com.jetsun.thirdplatformdemo.api;

/**
 * Created by cai.jia on 2017/6/9 0009
 */

public interface OnRspListener {

    void onPlatformStart();

    void onPlatformFinish(int platform, boolean success,String error,Object result);
}
