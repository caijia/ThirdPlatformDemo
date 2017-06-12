package com.jetsun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jetsun.thirdPlatform.Platform;
import com.jetsun.thirdPlatform.api.PlatformApiHelper;
import com.jetsun.thirdPlatform.api.PlatformManager;
import com.jetsun.thirdPlatform.event.OnUserInfoListener;
import com.jetsun.thirdPlatform.model.AuthResult;
import com.jetsun.thirdPlatform.model.UserInfo;

/**
 * 微信：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=1417751808&token=&lang=zh_CN
 * QQ:  http://wiki.open.qq.com/wiki/mobile/SDK%E4%B8%8B%E8%BD%BD
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnUserInfoListener {

    private TextView infoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoTv = (TextView) findViewById(R.id.info_tv);
        Button wxLoginBtn = (Button) findViewById(R.id.wx_login_btn);
        Button qqLoginBtn = (Button) findViewById(R.id.qq_login_btn);
        Button sinaLoginBtn = (Button) findViewById(R.id.sina_login_btn);

        wxLoginBtn.setOnClickListener(this);
        qqLoginBtn.setOnClickListener(this);
        sinaLoginBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wx_login_btn: {
                wxLogin();
                break;
            }

            case R.id.qq_login_btn: {
                qqLogin();
                break;
            }

            case R.id.sina_login_btn: {
                sinaLogin();
                break;
            }
        }
    }

    private void sinaLogin() {
        PlatformApiHelper.getInstance().getUserInfo(Platform.SINA, this, null, this);
    }

    private void qqLogin() {
        PlatformApiHelper.getInstance().getUserInfo(Platform.QQ, this, null, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        PlatformApiHelper.getInstance().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        //防止内存泄漏
        PlatformManager.getInstance().destroy(this);
        super.onDestroy();
    }

    private void wxLogin() {
        PlatformApiHelper.getInstance().getUserInfo(Platform.WX, this, null, this);
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
}
