package com.android.hoinnet.highde;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import com.android.hoinnet.highde.utils.Constant;

public class BaseActivity extends AppCompatActivity {
    private final int JUMP_ACTIVITY = 77;
    private final int JUMP_ACTIVITY_FOR_RESULT = 78;
    private final int JUMP_DELAY = 300;
    protected String TAG = getClass().getSimpleName();
    private ExitReceiver exitReceiver = new ExitReceiver();
    protected Context mContext;
    protected Handler mJumpHandler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 77:
                    BaseActivity.this.startActivity((Intent) message.obj);
                    return;
                case 78:
                    int i = message.arg1;
                    BaseActivity.this.startActivityForResult((Intent) message.obj, i);
                    return;
                default:
                    return;
            }
        }
    };

    class ExitReceiver extends BroadcastReceiver {
        ExitReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (Constant.EXITACTION.equals(intent.getAction())) {
                BaseActivity.this.finish();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = this;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.EXITACTION);
        registerReceiver(this.exitReceiver, intentFilter);
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.exitReceiver);
    }

    /* access modifiers changed from: protected */
    public void jumpActivity(Intent intent) {
        this.mJumpHandler.removeMessages(77);
        Message obtainMessage = this.mJumpHandler.obtainMessage();
        obtainMessage.what = 77;
        obtainMessage.obj = intent;
        this.mJumpHandler.sendMessageDelayed(obtainMessage, 300);
    }

    /* access modifiers changed from: protected */
    public void jumpActivityForResult(Intent intent, int i) {
        this.mJumpHandler.removeMessages(78);
        Message obtainMessage = this.mJumpHandler.obtainMessage();
        obtainMessage.what = 78;
        obtainMessage.obj = intent;
        obtainMessage.arg1 = i;
        this.mJumpHandler.sendMessageDelayed(obtainMessage, 300);
    }
}
