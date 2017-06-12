package com.jetsun.wxapi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jetsun.thirdPlatform.api.PlatformApiHelper;


/**
 * Created by cai.jia on 2017/6/9 0009
 */

public class WXEntryActivity extends AppCompatActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlatformApiHelper.getInstance().handleIntent(this,getIntent());
    }

}
