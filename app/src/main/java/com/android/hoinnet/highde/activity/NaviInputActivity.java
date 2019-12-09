package com.android.hoinnet.highde.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.hoinnet.highde.BaseActivity;
import com.android.hoinnet.highde.R;
import com.android.hoinnet.highde.view.LeftRightButton;
import com.iflytek.clientadapter.constant.FocusType;
import com.r0adkll.slidr.Slidr;

public class NaviInputActivity extends BaseActivity implements LeftRightButton.LeftRightListener, View.OnClickListener {
    private static final String INTENT_START_VOICE_ASSIST = "com.hmct.wearable.voiceassist.start";
    private static final int REQUEST_CODE_VOICE = 1;
    private static final String RESULT_VOICE_TEXT = "com.hmct.wearable.voiceassist.result";
    private TextView mEnterTip;
    private ImageView mInputGoingIv;
    private LeftRightButton mInputLeftrightbutton;
    private LinearLayout mInputLine;
    private EditText mRecipientedittextview;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_input);
        Slidr.attach(this);
        assignViews();
    }

    private void assignViews() {
        this.mEnterTip = (TextView) findViewById(R.id.tv_enter_tip);
        this.mInputLine = (LinearLayout) findViewById(R.id.input_line);
        this.mRecipientedittextview = (EditText) findViewById(R.id.recipientedittextview);
        this.mInputGoingIv = (ImageView) findViewById(R.id.input_going_iv);
        this.mInputGoingIv.setOnClickListener(this);
        this.mInputLeftrightbutton = (LeftRightButton) findViewById(R.id.input_leftrightbutton);
        this.mInputLeftrightbutton.setLeftRightListener(this);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1 && i2 == -1) {
            String stringExtra = intent.getStringExtra(RESULT_VOICE_TEXT);
            Intent intent2 = new Intent();
            intent2.putExtra("text", stringExtra);
            setResult(-1, intent2);
            finish();
        }
    }

    public void onLeftClick(View view) {
        try {
            Intent intent = new Intent(INTENT_START_VOICE_ASSIST);
            intent.putExtra("extra_package_type", FocusType.map);
            jumpActivityForResult(intent, 1);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.not_find_voice), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void onRightClick(View view) {
        if (this.mInputLine.getVisibility() != View.VISIBLE) {
            this.mEnterTip.setVisibility(View.GONE);
            this.mInputLine.setVisibility(View.VISIBLE);
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.input_going_iv) {
            startResultText();
        }
    }

    private void startResultText() {
        if (this.mRecipientedittextview != null) {
            String obj = this.mRecipientedittextview.getText().toString();
            if (!TextUtils.isEmpty(obj)) {
                Intent intent = new Intent();
                intent.putExtra("text", obj);
                setResult(-1, intent);
                finish();
                return;
            }
            Toast.makeText(this, getString(R.string.input_destination), Toast.LENGTH_SHORT).show();
        }
    }
}
