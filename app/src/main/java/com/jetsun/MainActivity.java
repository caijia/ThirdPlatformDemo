package com.jetsun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jetsun.thirdplatformdemo.api.qq.QQApiHelper;
import com.jetsun.thirdplatformdemo.api.wx.WXApiHelper;
import com.jetsun.thirdplatformdemo.event.OnUserInfoListener;

/**
 * 微信：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=1417751808&token=&lang=zh_CN
 * QQ:  http://wiki.open.qq.com/wiki/mobile/SDK%E4%B8%8B%E8%BD%BD
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnUserInfoListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            case R.id.wx_login_btn:{
                wxLogin();
                break;
            }

            case R.id.qq_login_btn:{
                qqLogin();
                break;
            }

            case R.id.sina_login_btn:{

                break;
            }
        }
    }

    private void qqLogin() {
        QQApiHelper.getInstance().getUserInfo(this, null, new OnUserInfoListener() {
            @Override
            public void onUserInfoStart(int platform) {

            }

            @Override
            public void onUserInfoError(int platform) {

            }

            @Override
            public void onUserInfoComplete(int platform, Object result) {
                System.out.println(result.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        QQApiHelper.getInstance().onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode,resultCode,data);
    }

    private void wxLogin() {
        WXApiHelper.getInstance().getUserInfo(null,this);
    }


    @Override
    public void onUserInfoStart(int platform) {
        System.out.println("start");
    }

    @Override
    public void onUserInfoError(int platform) {
        System.out.println("Error");
    }

    @Override
    public void onUserInfoComplete(int platform, Object result) {
        System.out.println(result.toString());
    }
}
