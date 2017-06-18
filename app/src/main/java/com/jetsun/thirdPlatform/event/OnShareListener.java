package com.jetsun.thirdPlatform.event;

/**
 * Created by cai.jia on 2017/6/17.
 */

public interface OnShareListener {

    void onShareSuccess(int platform);

    void onShareError(int platform);
}
