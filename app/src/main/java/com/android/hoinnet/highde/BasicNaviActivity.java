package com.android.hoinnet.highde;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.android.hoinnet.highde.utils.Constant;
import com.android.hoinnet.highde.utils.TTSController;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.orhanobut.logger.Logger;

public class BasicNaviActivity extends AppCompatActivity implements AMapNaviListener, AMapNaviViewListener {
    private ExitReceiver exitReceiver = new ExitReceiver();
    protected AMapNavi mAMapNavi;
    protected AMapNaviView mAMapNaviView;
    protected TTSController mTtsManager;

    class ExitReceiver extends BroadcastReceiver {
        ExitReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (Constant.EXITACTION.equals(intent.getAction())) {
                BasicNaviActivity.this.finish();
            }
        }
    }

    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
    }

    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
    }

    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfoArr) {
    }

    public void hideCross() {
    }

    public void hideLaneInfo() {
    }

    public void notifyParallelRoad(int i) {
    }

    public void onArriveDestination() {
    }

    public void onArrivedWayPoint(int i) {
    }

    public void onCalculateRouteSuccess(int[] iArr) {
    }

    public void onEndEmulatorNavi() {
    }

    public void onGetNavigationText(int i, String str) {
    }

    public void onGetNavigationText(String str) {
    }

    public void onGpsOpenStatus(boolean z) {
    }

    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
    }

    public boolean onNaviBackClick() {
        return true;
    }

    public void onNaviInfoUpdate(NaviInfo naviInfo) {
    }

    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
    }

    public void onNaviMapMode(int i) {
    }

    public void onNaviSetting() {
    }

    public void onNaviTurnClick() {
    }

    public void onNextRoadClick() {
    }

    public void onPlayRing(int i) {
    }

    public void onReCalculateRouteForTrafficJam() {
    }

    public void onReCalculateRouteForYaw() {
    }

    public void onScanViewButtonClick() {
    }

    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfoArr) {
    }

    public void onStartNavi(int i) {
    }

    public void onTrafficStatusUpdate() {
    }

    public void showCross(AMapNaviCross aMapNaviCross) {
    }

    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfoArr, byte[] bArr, byte[] bArr2) {
    }

    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
    }

    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
    }

    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfoArr) {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mTtsManager = TTSController.getInstance(getApplicationContext());
        this.mTtsManager.init();
        this.mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        this.mAMapNavi.addAMapNaviListener(this);
        this.mAMapNavi.addAMapNaviListener(this.mTtsManager);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.EXITACTION);
        registerReceiver(this.exitReceiver, intentFilter);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mAMapNaviView.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mAMapNaviView.onPause();
        this.mTtsManager.stopSpeaking();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.mAMapNaviView.onDestroy();
        this.mAMapNavi.stopNavi();
        this.mAMapNavi.destroy();
        this.mTtsManager.destroy();
        unregisterReceiver(this.exitReceiver);
    }

    public void onInitNaviFailure() {
        Logger.d("导航初始化失败！！！！！");
    }

    public void onInitNaviSuccess() {
        Logger.d("导航初始化成功！！！！！");
    }

    public void onCalculateRouteFailure(int i) {
        Toast.makeText(this, getString(R.string.route_failed), Toast.LENGTH_SHORT).show();
        Logger.d("路线计算失败：错误码=" + i + "\n错误码详细链接见：http://lbs.amap.com/api/android-navi-sdk/guide/tools/errorcode/");
    }

    public void onNaviCancel() {
        finish();
    }

    public void onLockMap(boolean z) {
        Logger.d("锁地图状态发生变化时回调:" + z);
    }

    public void onNaviViewLoaded() {
        Logger.d("导航页面加载成功");
    }
}
