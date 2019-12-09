package com.android.hoinnet.highde.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.hoinnet.highde.BaseActivity;
import com.android.hoinnet.highde.R;
import com.android.hoinnet.highde.utils.Constant;
import com.android.hoinnet.highde.utils.SPUtils;
import com.r0adkll.slidr.Slidr;

public class TipActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout mLayoutNo;
    private RelativeLayout mLayoutYes;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_tip);
        Slidr.attach(this).lock();
        initView();
    }

    private void initView() {
        this.mLayoutNo = (RelativeLayout) findViewById(R.id.llayout_no);
        this.mLayoutYes = (RelativeLayout) findViewById(R.id.llayout_yes);
        this.mLayoutNo.setOnClickListener(this);
        this.mLayoutYes.setOnClickListener(this);
        ((TextView) findViewById(R.id.tip_tv)).setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (4 == i) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llayout_no:
                Intent intent = new Intent();
                intent.putExtra("tip_status", false);
                setResult(-1, intent);
                finish();
                return;
            case R.id.llayout_yes:
                SPUtils.getInstance().put(Constant.FIRST_BOOT, false);
                Intent intent2 = new Intent();
                intent2.putExtra("tip_status", true);
                setResult(-1, intent2);
                finish();
                return;
            default:
                return;
        }
    }
}
