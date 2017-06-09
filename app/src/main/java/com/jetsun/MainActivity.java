package com.jetsun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jetsun.thirdplatformdemo.api.OnRspListener;
import com.jetsun.thirdplatformdemo.api.wx.WxApiHelper;

/**
 * 微信：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=1417751808&token=&lang=zh_CN
 * QQ:  http://wiki.open.qq.com/wiki/mobile/SDK%E4%B8%8B%E8%BD%BD
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnRspListener {

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

                break;
            }

            case R.id.sina_login_btn:{

                break;
            }
        }
    }

    private void wxLogin() {
        WxApiHelper.getInstance().getUserInfo(this);
    }

    @Override
    public void onPlatformStart() {
        System.out.println("start");
    }

    @Override
    public void onPlatformFinish(int platform, boolean success, String error, Object result) {
        if (success) {
            System.out.println(result.toString());
        }
    }
}
