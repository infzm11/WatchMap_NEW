package com.android.hoinnet.highde.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.model.NaviLatLng;
import com.android.hoinnet.highde.BasicNaviActivity;
import com.android.hoinnet.highde.R;
import com.android.hoinnet.highde.dialog.RxDialogSureCancel;
import com.android.hoinnet.highde.utils.Constant;
import com.orhanobut.logger.Logger;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import java.util.ArrayList;
import java.util.List;

public class WalkNaviActivity extends BasicNaviActivity implements View.OnClickListener {
    private List<NaviLatLng> eList = new ArrayList();
    private AMap mAMap;
    private Button mButtonConContinueNavigation;
    private Button mButtonFullView;
    private CancleBroadcastReceiver mCancleBroadcastReceiver;
    private LatLng mLatLngEnd = null;
    private LatLng mLatLngStart = null;
    private LinearLayout mLinearLayout;
    private int mNaviMode = 1;
    private List<NaviLatLng> mWayPointList = new ArrayList();
    private List<NaviLatLng> sList = new ArrayList();

    class CancleBroadcastReceiver extends BroadcastReceiver {
        CancleBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.d("action:" + action);
            if (Constant.ACTION_FINISH.equals(action)) {
                WalkNaviActivity.this.finish();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_navi_walk);
        Slidr.attach(this, new SlidrConfig.Builder().edge(true).build());
        initViw(bundle);
        initData();
    }

    private void initViw(Bundle bundle) {
        this.mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        this.mAMapNaviView.onCreate(bundle);
        this.mAMapNaviView.setAMapNaviViewListener(this);
        this.mLinearLayout = (LinearLayout) findViewById(R.id.navi_walk_llayout);
        this.mButtonFullView = (Button) findViewById(R.id.bt_full_view);
        this.mButtonConContinueNavigation = (Button) findViewById(R.id.bt_continue_navigation);
        this.mButtonFullView.setOnClickListener(this);
        this.mButtonConContinueNavigation.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        this.mLatLngStart = (LatLng) intent.getParcelableExtra(Constant.START_LATLNG);
        this.mLatLngEnd = (LatLng) intent.getParcelableExtra(Constant.END_LATLNG);
        this.mNaviMode = intent.getIntExtra(Constant.NAVI_MODE, 1);
        Logger.d(Integer.valueOf(this.mNaviMode));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_FINISH);
        this.mCancleBroadcastReceiver = new CancleBroadcastReceiver();
        registerReceiver(this.mCancleBroadcastReceiver, intentFilter);
        AMapNaviViewOptions viewOptions = this.mAMapNaviView.getViewOptions();
        viewOptions.setLayoutVisible(false);
        if (2 != this.mNaviMode || this.mLatLngStart == null || this.mLatLngEnd == null) {
            this.mAMapNaviView.setNaviMode(1);
            viewOptions.setSensorEnable(true);
        } else {
            this.mAMapNaviView.setNaviMode(0);
            this.sList.add(new NaviLatLng(this.mLatLngStart.latitude, this.mLatLngStart.longitude));
            NaviLatLng naviLatLng = new NaviLatLng(this.mLatLngEnd.latitude, this.mLatLngEnd.longitude);
            this.eList.add(naviLatLng);
            this.mWayPointList.add(naviLatLng);
            viewOptions.setCrossDisplayShow(false);
            viewOptions.setRealCrossDisplayShow(false);
            viewOptions.setTrafficBarEnabled(false);
        }
        this.mAMapNaviView.setViewOptions(viewOptions);
        this.mAMap = this.mAMapNaviView.getMap();
    }

    public void onInitNaviSuccess() {
        int i;
        super.onInitNaviSuccess();
        if (this.mLatLngStart == null || this.mLatLngEnd == null) {
            Toast.makeText(this, getString(R.string.location_fail), Toast.LENGTH_SHORT).show();
            finish();
        } else if (2 == this.mNaviMode) {
            try {
                i = this.mAMapNavi.strategyConvert(true, false, false, true, false);
            } catch (Exception e) {
                e.printStackTrace();
                i = 0;
            }
            this.mAMapNavi.calculateDriveRoute(this.sList, this.eList, this.mWayPointList, i);
        } else if (this.mNaviMode == 0) {
            this.mAMapNavi.calculateWalkRoute(new NaviLatLng(this.mLatLngStart.latitude, this.mLatLngStart.longitude), new NaviLatLng(this.mLatLngEnd.latitude, this.mLatLngEnd.longitude));
        } else if (1 == this.mNaviMode) {
            this.mAMapNavi.calculateRideRoute(new NaviLatLng(this.mLatLngStart.latitude, this.mLatLngStart.longitude), new NaviLatLng(this.mLatLngEnd.latitude, this.mLatLngEnd.longitude));
        }
    }

    public void onCalculateRouteSuccess(int[] iArr) {
        super.onCalculateRouteSuccess(iArr);
        this.mAMapNavi.startNavi(1);
    }

    public void onLockMap(boolean z) {
        super.onLockMap(z);
        if (!z) {
            this.mLinearLayout.setVisibility(View.VISIBLE);
        } else {
            this.mLinearLayout.setVisibility(View.GONE);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_continue_navigation:
                this.mAMapNaviView.recoverLockMode();
                return;
            case R.id.bt_full_view:
                this.mAMapNaviView.displayOverview();
                return;
            default:
                return;
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (4 != i) {
            return super.onKeyDown(i, keyEvent);
        }
        showDialogSureCancel();
        return true;
    }

    private void showDialogSureCancel() {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel((Activity) this);
        rxDialogSureCancel.setContent(getString(R.string.ok_to_exit));
        rxDialogSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                rxDialogSureCancel.cancel();
                WalkNaviActivity.this.finish();
            }
        });
        rxDialogSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.show();
    }

    public void onClickExit(View view) {
        showDialogSureCancel();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mCancleBroadcastReceiver);
    }
}
