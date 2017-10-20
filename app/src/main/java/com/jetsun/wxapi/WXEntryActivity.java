package com.jetsun.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.caijia.social.api.PlatformApiHelper;


/**
 * Created by cai.jia on 2017/6/9 0009
 */

public class WXEntryActivity extends Activity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlatformApiHelper.getInstance().handleIntent(this,getIntent());
    }

}
