package com.jetsun.thirdPlatform.net;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.Map;

/**
 * Created by cai.jia on 2017/6/9 0009
 */

public interface HttpRequest {

    String request(@Nullable Context context, String url, @Nullable Map<String, Object> params) throws Exception;
}
