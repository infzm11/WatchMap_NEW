package com.android.hoinnet.highde.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.hoinnet.highde.BaseActivity;
import com.android.hoinnet.highde.R;
import com.android.hoinnet.highde.utils.Constant;
import com.android.hoinnet.highde.utils.NetworkUtils;
import com.android.hoinnet.highde.utils.SPUtils;
import com.orhanobut.logger.Logger;
import com.r0adkll.slidr.Slidr;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaitActivity extends BaseActivity {
    private static final int TIP_REQUESTCODE = 13;
    private static final int WHAT_NET = 1;
    /* access modifiers changed from: private */
    public boolean isNetState;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 1) {
                boolean unused = WaitActivity.this.isNetState = ((Boolean) message.obj).booleanValue();
                if (!WaitActivity.this.isNetState) {
                    WaitActivity.this.mImageView.clearAnimation();
                    WaitActivity.this.mImageView.setVisibility(View.GONE);
                    WaitActivity.this.mLinearLayout.setVisibility(View.VISIBLE);
                    return;
                }
                Intent intent = new Intent();
                if (WaitActivity.this.mHoinnetBundle != null) {
                    intent.putExtra(Constant.HOINNET_NAVI_DATA, WaitActivity.this.mHoinnetBundle);
                }
                intent.setClass(WaitActivity.this, MainActivity.class);
                WaitActivity.this.startActivity(intent);
                WaitActivity.this.finish();
            }
        }
    };
    /* access modifiers changed from: private */
    public Bundle mHoinnetBundle = null;
    /* access modifiers changed from: private */
    public ImageView mImageView;
    /* access modifiers changed from: private */
    public LinearLayout mLinearLayout;
    private Runnable mRunnable = new Runnable() {
        public void run() {
            boolean isAvailableByPing = NetworkUtils.isAvailableByPing();
            Logger.i("net Ping:" + isAvailableByPing, new Object[0]);
            Message message = new Message();
            message.what = 1;
            message.obj = Boolean.valueOf(isAvailableByPing);
            WaitActivity.this.mHandler.sendMessage(message);
        }
    };
    private ExecutorService mSingleThreadExecutor;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_wait);
        Slidr.attach(this);
        initView();
    }

    private void initView() {
        this.mHoinnetBundle = getIntent().getBundleExtra(Constant.HOINNET_NAVI_DATA);
        this.mLinearLayout = (LinearLayout) findViewById(R.id.net_tip_linearly);
        this.mImageView = (ImageView) findViewById(R.id.init_img);
        this.mSingleThreadExecutor = Executors.newSingleThreadExecutor();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        if (SPUtils.getInstance().getBoolean(Constant.FIRST_BOOT, true)) {
            Intent intent = new Intent();
            intent.setClass(this, TipActivity.class);
            startActivityForResult(intent, 13);
        } else if (this.mLinearLayout == null || this.mLinearLayout.getVisibility() != View.VISIBLE) {
            loadAnimation();
        } else {
            Log.d(this.TAG, "onStart: is Net Error");
        }
    }

    private void loadAnimation() {
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.tip_rotate);
        loadAnimation.setInterpolator(new LinearInterpolator());
        if (this.mImageView != null) {
            this.mImageView.startAnimation(loadAnimation);
        }
        if (NetworkUtils.getDataEnabled() || NetworkUtils.getWifiEnabled()) {
            this.mSingleThreadExecutor.execute(this.mRunnable);
            return;
        }
        this.mImageView.clearAnimation();
        this.mImageView.setVisibility(View.GONE);
        this.mLinearLayout.setVisibility(View.VISIBLE);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (-1 != i2 || 13 != i) {
            return;
        }
        if (intent.getBooleanExtra("tip_status", false)) {
            loadAnimation();
        } else {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }
}
