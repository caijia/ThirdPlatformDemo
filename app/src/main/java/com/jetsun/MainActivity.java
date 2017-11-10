package com.jetsun;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.caijia.social.Platform;
import com.caijia.social.api.PlatformApiHelper;
import com.caijia.social.api.PlatformManager;
import com.caijia.social.event.OnShareListener;
import com.caijia.social.event.OnUserInfoListener;
import com.caijia.social.model.AuthResult;
import com.caijia.social.model.UserInfo;

/**
 * 微信：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=1417751808&token=&lang=zh_CN
 * QQ:  http://wiki.open.qq.com/wiki/mobile/SDK%E4%B8%8B%E8%BD%BD
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnUserInfoListener, OnShareListener {

    private static final String IMAGE_URL = "http://pic28.nipic.com/20130424/11588775_115415688157_2.jpg";
    private static final String TARGET_URL = "https://www.6383.com";
    private TextView infoTv;

    /**
     * 设置状态栏透明
     */
    public static void setTranslucentStatus(Activity activity) {
        // 5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTranslucentStatus(this);
        infoTv = (TextView) findViewById(R.id.info_tv);
        Button wxLoginBtn = (Button) findViewById(R.id.wx_login_btn);
        Button qqLoginBtn = (Button) findViewById(R.id.qq_login_btn);
        Button sinaLoginBtn = (Button) findViewById(R.id.sina_login_btn);

        Button wxShareBtn = (Button) findViewById(R.id.wx_share_btn);
        Button wxCircleShareBtan = (Button) findViewById(R.id.wx_circle_share_btn);
        Button wxCollectShareBtn = (Button) findViewById(R.id.wx_collect_share_btn);
        Button qqShareBtn = (Button) findViewById(R.id.qq_share_btn);
        Button qZoneShareBtn = (Button) findViewById(R.id.qzone_share_btn);
        Button sinaShareBtn = (Button) findViewById(R.id.sina_share_btn);
        Button shareDialog = (Button) findViewById(R.id.btn_share_dialog);

        wxLoginBtn.setOnClickListener(this);
        qqLoginBtn.setOnClickListener(this);
        sinaLoginBtn.setOnClickListener(this);

        wxShareBtn.setOnClickListener(this);
        wxCircleShareBtan.setOnClickListener(this);
        wxCollectShareBtn.setOnClickListener(this);
        qqShareBtn.setOnClickListener(this);
        qZoneShareBtn.setOnClickListener(this);
        sinaShareBtn.setOnClickListener(this);
        shareDialog.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wx_login_btn: {
                PlatformApiHelper.getInstance().getUserInfo(Platform.WX, this, null, this);
                break;
            }

            case R.id.qq_login_btn: {
                PlatformApiHelper.getInstance().getUserInfo(Platform.QQ, this, null, this);
                break;
            }

            case R.id.sina_login_btn: {
                PlatformApiHelper.getInstance().getUserInfo(Platform.SINA, this, null, this);
                break;
            }

            case R.id.wx_share_btn: {
                PlatformApiHelper.getInstance().share(this, Platform.WX, "title", "desc",
                        IMAGE_URL, TARGET_URL, this);
                break;
            }

            case R.id.wx_circle_share_btn: {
                PlatformApiHelper.getInstance().share(this, Platform.WX_CIRCLE, "好波通", "好波通好波通",
                        IMAGE_URL, TARGET_URL, this);
                break;
            }

            case R.id.wx_collect_share_btn: {
                PlatformApiHelper.getInstance().share(this, Platform.WX_COLLECT, "title", "desc",
                        IMAGE_URL, TARGET_URL, this);
                break;
            }

            case R.id.qq_share_btn: {
                PlatformApiHelper.getInstance().share(this, Platform.QQ, "title", "desc",
                        IMAGE_URL, TARGET_URL, this);
                break;
            }

            case R.id.qzone_share_btn: {
                PlatformApiHelper.getInstance().share(this, Platform.QZONE, "title", "desc",
                        IMAGE_URL, TARGET_URL, this);
                break;
            }

            case R.id.sina_share_btn: {
                PlatformApiHelper.getInstance().share(this, Platform.SINA, "title", "desc",
                        IMAGE_URL, TARGET_URL, this);
                break;
            }

            case R.id.btn_share_dialog:{
                Intent i = ShareDialog.getIntent(this,"title", "text", IMAGE_URL, "http://www.baidu.com");
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0);
                ActivityCompat.startActivity(this, i, compat.toBundle());
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        PlatformApiHelper.getInstance().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        PlatformApiHelper.getInstance().handleIntent(this, intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        //防止内存泄漏
        PlatformManager.getInstance().destroy(this);
        super.onDestroy();
    }

    @Override
    public void onUserInfoStart(@PlatformManager.PlatformType int platform) {

    }

    @Override
    public void onUserInfoError(@PlatformManager.PlatformType int platform) {

    }

    @Override
    public void onUserInfoComplete(@PlatformManager.PlatformType int platform, UserInfo result,
                                   AuthResult authResult) {
        String info = String.format("platform = %s--userInfo=%s--authResult=%s", platform + "",
                result.toString(), authResult.toString());
        infoTv.setText(info);
    }

    @Override
    public void onShareSuccess(int platform) {
        infoTv.setText("分享成功" + platform);
    }

    @Override
    public void onShareError(int platform) {
        infoTv.setText("分享失败" + platform);
    }
}
