package com.android.hoinnet.highde.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.amap.api.services.core.PoiItem;
import com.android.hoinnet.highde.BaseActivity;
import com.android.hoinnet.highde.R;
import com.android.hoinnet.highde.utils.Constant;
import com.r0adkll.slidr.Slidr;

public class SelectNaviModeActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout mDrive;
    private int mIndex = -1;
    private PoiItem mPoiItem = null;
    private LinearLayout mRide;
    private LinearLayout mWalk;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_selectnavimode);
        Slidr.attach(this);
        this.mIndex = getIntent().getIntExtra(Constant.INTERT_KEY_SELECT_INDEX, -1);
        this.mPoiItem = (PoiItem) getIntent().getParcelableExtra(Constant.INTERT_KEY_POIBEAN);
        initView();
    }

    private void initView() {
        this.mWalk = (LinearLayout) findViewById(R.id.select_walk);
        this.mRide = (LinearLayout) findViewById(R.id.select_ride);
        this.mDrive = (LinearLayout) findViewById(R.id.select_drive);
        this.mWalk.setOnClickListener(this);
        this.mRide.setOnClickListener(this);
        this.mDrive.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_drive /*2131230906*/:
                setResults(2);
                return;
            case R.id.select_ride /*2131230907*/:
                setResults(1);
                return;
            case R.id.select_walk /*2131230908*/:
                setResults(0);
                return;
            default:
                return;
        }
    }

    private void setResults(int i) {
        Intent intent = new Intent();
        intent.putExtra(Constant.INTERT_KEY_SELECTMODE, i);
        intent.putExtra(Constant.INTERT_KEY_SELECT_INDEX, this.mIndex);
        intent.putExtra(Constant.INTERT_KEY_POIBEAN, this.mPoiItem);
        setResult(-1, intent);
        finish();
    }
}
