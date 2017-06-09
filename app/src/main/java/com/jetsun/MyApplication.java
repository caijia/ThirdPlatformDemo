package com.jetsun;

import android.app.Application;

import com.jetsun.thirdplatformdemo.api.wx.WxApiHelper;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by cai.jia on 2017/6/9 0009
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        WxApiHelper.getInstance().init(this, Constants.WX_APP_ID, Constants.WX_SECRET, true);
    }

    @Override
    public void onTerminate() {
        WxApiHelper.getInstance().destroy();
        super.onTerminate();
    }
}
