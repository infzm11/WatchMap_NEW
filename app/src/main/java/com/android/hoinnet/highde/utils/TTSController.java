package com.android.hoinnet.highde.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import java.util.LinkedList;

public class TTSController implements AMapNaviListener, ICallBack {
    public static TTSController ttsManager;
    private final int CHECK_TTS_PLAY = 2;
    private final int TTS_PLAY = 1;
    /* access modifiers changed from: private */
    public Handler handler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    if (TTSController.this.tts != null && TTSController.this.wordList.size() > 0) {
                        TTSController.this.tts.playText((String) TTSController.this.wordList.removeFirst());
                        return;
                    }
                    return;
                case 2:
                    if (!TTSController.this.tts.isPlaying()) {
                        TTSController.this.handler.obtainMessage(1).sendToTarget();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private IFlyTTS iflyTTS = null;
    private Context mContext;
    private SystemTTS systemTTS;
    /* access modifiers changed from: private */
    public TTS tts = null;
    /* access modifiers changed from: private */
    public LinkedList<String> wordList = new LinkedList<>();

    public enum TTSType {
        IFLYTTS,
        SYSTEMTTS
    }

    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
    }

    @Deprecated
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
    }

    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfoArr) {
    }

    public void hideCross() {
    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

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

    public void onGpsOpenStatus(boolean z) {
    }

    public void onInitNaviFailure() {
    }

    public void onInitNaviSuccess() {
    }

    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
    }

    public void onNaviInfoUpdate(NaviInfo naviInfo) {
    }

    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
    }

    public void onPlayRing(int i) {
    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

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

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
    }

    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
    }

    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfoArr) {
    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    public void onCompleted(int i) {
        if (this.handler != null) {
            this.handler.obtainMessage(1).sendToTarget();
        }
    }

    public void setTTSType(TTSType tTSType) {
        if (tTSType == TTSType.SYSTEMTTS) {
            this.tts = this.systemTTS;
        } else {
            this.tts = this.iflyTTS;
        }
        this.tts.setCallback(this);
    }

    private TTSController(Context context) {
        this.mContext = context.getApplicationContext();
        this.systemTTS = SystemTTS.getInstance(this.mContext);
        this.iflyTTS = IFlyTTS.getInstance(this.mContext);
        this.tts = this.iflyTTS;
    }

    public void init() {
        if (this.systemTTS != null) {
            this.systemTTS.init();
        }
        if (this.iflyTTS != null) {
            this.iflyTTS.init();
        }
        this.tts.setCallback(this);
    }

    public static TTSController getInstance(Context context) {
        if (ttsManager == null) {
            ttsManager = new TTSController(context);
        }
        return ttsManager;
    }

    public void stopSpeaking() {
        if (this.systemTTS != null) {
            this.systemTTS.stopSpeak();
        }
        if (this.iflyTTS != null) {
            this.iflyTTS.stopSpeak();
        }
        this.wordList.clear();
    }

    public void destroy() {
        if (this.systemTTS != null) {
            this.systemTTS.destroy();
        }
        if (this.iflyTTS != null) {
            this.iflyTTS.destroy();
        }
        ttsManager = null;
    }

    public void onCalculateRouteFailure(int i) {
        if (this.wordList != null) {
            this.wordList.addLast("路线规划失败");
        }
    }

    public void onReCalculateRouteForTrafficJam() {
        if (this.wordList != null) {
            this.wordList.addLast("前方路线拥堵，路线重新规划");
        }
    }

    public void onReCalculateRouteForYaw() {
        if (this.wordList != null) {
            this.wordList.addLast("路线重新规划");
        }
    }

    public void onGetNavigationText(String str) {
        if (this.wordList != null) {
            this.wordList.addLast(str);
        }
        this.handler.obtainMessage(2).sendToTarget();
    }
}
