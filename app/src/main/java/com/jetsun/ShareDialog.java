package com.jetsun;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.caijia.social.Platform;
import com.caijia.social.api.PlatformApiHelper;
import com.caijia.social.api.PlatformManager;
import com.caijia.social.event.OnShareListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 分享页面
 * Created by cai.jia on 2017/10/24 0024.
 */

public class ShareDialog extends AppCompatActivity {

    private static final String SHARE_TITLE = "params:shareTitle";
    private static final String SHARE_TEXT = "params:shareText";
    private static final String SHARE_IMAGE_URL = "params:shareImageUrl";
    private static final String SHARE_TARGET_URL = "params:shareTargetUrl";

    @BindView(R.id.v_shadow)
    View vShadow;
    @BindView(R.id.lly_share_content)
    LinearLayout llyShareContent;

    private String title;
    private String text;
    private String imageUrl;
    private String targetUrl;

    public static Intent getIntent(Context context,String title, String text, String imageUrl, String targetUrl) {
        Intent i = new Intent(context, ShareDialog.class);
        i.putExtra(SHARE_TITLE, title);
        i.putExtra(SHARE_TEXT, text);
        i.putExtra(SHARE_IMAGE_URL, imageUrl);
        i.putExtra(SHARE_TARGET_URL, targetUrl);
        return i;
    }

    private int shareContentHeight ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        View view = LayoutInflater.from(this).inflate(R.layout.common_dialog_share, viewGroup, false);
        setContentView(view);
        setTranslucentStatus(this);
        ButterKnife.bind(this);
        Intent i = getIntent();
        title = i.getStringExtra(SHARE_TITLE);
        text = i.getStringExtra(SHARE_TEXT);
        imageUrl = i.getStringExtra(SHARE_IMAGE_URL);
        targetUrl = i.getStringExtra(SHARE_TARGET_URL);
        vShadow.setAlpha(0);
        llyShareContent.setAlpha(0);

        llyShareContent.post(new Runnable() {
            @Override
            public void run() {
                shareContentHeight = llyShareContent.getHeight();
                openShareDialog();
            }
        });
    }

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

    private void openShareDialog() {
        if (!isAnimStart) {
            animator(shareContentHeight, 0);
        }
    }

    private void closeShareDialog(){
        if (!isAnimStart) {
            animator(0, shareContentHeight);
        }
    }

    private boolean isAnimStart;
    private boolean isOpen;

    private void animator(final int start, final int end) {
        isAnimStart = true;
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                llyShareContent.setTranslationY(value);
                float alpha = (shareContentHeight - llyShareContent.getTranslationY()) / shareContentHeight;
                vShadow.setAlpha(alpha);
                llyShareContent.setAlpha(alpha);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimStart = false;
                isOpen = end == 0;

                if (!isOpen) {
                    finish();
                }
            }
        });
        animator.start();
    }

    @Override
    public void onBackPressed() {
        if (isOpen) {
            closeShareDialog();
        }
    }

    @OnClick({R.id.ll_share_qq, R.id.ll_share_wx, R.id.ll_share_friend_circle,
            R.id.ll_share_qq_zone, R.id.tv_share_cancle,R.id.v_shadow})
    public void onViewClicked(View view) {
        int type = -1;
        switch (view.getId()) {
            case R.id.ll_share_wx:
                type = Platform.WX;
                break;

            case R.id.ll_share_friend_circle:
                type = Platform.WX_CIRCLE;
                break;

            case R.id.ll_share_qq:
                type = Platform.QQ;
                break;

            case R.id.ll_share_qq_zone:
                type = Platform.QZONE;
                break;

            case R.id.v_shadow:{
                closeShareDialog();
                break;
            }
        }

        if (type != -1) {
            PlatformApiHelper.getInstance().share(this, type, title, text,
                    imageUrl, targetUrl, new OnShareListener() {
                        @Override
                        public void onShareSuccess(int platform) {
                            ToastManager.getInstance(ShareDialog.this).showToast("onShareSuccess");
                        }

                        @Override
                        public void onShareError(int platform) {
                            ToastManager.getInstance(ShareDialog.this).showToast("onShareError");
                        }
                    });
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
}
