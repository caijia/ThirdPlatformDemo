package com.jetsun;

import android.app.Application;

import com.caijia.social.Platform;
import com.caijia.social.api.PlatformManager;
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
        PlatformManager.getInstance().init(Platform.WX,this, Constants.WX_APP_ID, Constants.WX_SECRET);
        PlatformManager.getInstance().init(Platform.QQ,this, Constants.QQ_APP_ID, Constants.QQ_SECRET);
        PlatformManager.getInstance().init(Platform.SINA,this, Constants.SINA_APP_ID, Constants.SINA_SECRET);
    }

    @Override
    public void onTerminate() {
        PlatformManager.getInstance().destroy(this);
        super.onTerminate();
    }
}
