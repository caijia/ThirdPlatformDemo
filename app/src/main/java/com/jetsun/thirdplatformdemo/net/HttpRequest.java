package com.jetsun.thirdplatformdemo.net;

import java.util.Map;

/**
 * Created by cai.jia on 2017/6/9 0009
 */

public interface HttpRequest {

    String request(String url, Map<String, Object> params) throws Exception;
}
