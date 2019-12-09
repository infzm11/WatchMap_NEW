package com.android.hoinnet.highde.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.WearMapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.android.hoinnet.highde.BaseActivity;
import com.android.hoinnet.highde.R;
import com.android.hoinnet.highde.utils.Constant;
import com.android.hoinnet.highde.utils.JsonParser;
import com.android.hoinnet.highde.utils.SensorEventHelper;
import com.android.hoinnet.highde.view.popView.RxPopupView;
import com.android.hoinnet.highde.view.popView.RxPopupViewManager;
import com.carlos.voiceline.mylibrary.VoiceLineView;
/*import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;*/
import com.orhanobut.logger.Logger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseActivity implements LocationSource,
        AMapLocationListener, View.OnClickListener, AMap.OnCameraChangeListener,
        RxPopupViewManager.TipListener, AMap.OnMapClickListener {
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private static final int SPEECH_LAST_WHAT = 108;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final String TAG = "MainActivity";
    private final int NAVI_REQUEST_CODE = 2;
    private AMap mAMap;
    private RxPopupView.Builder mBuilder;
    private Circle mCircle;
    private ContentObserver mContentObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean z) {
            super.onChange(z);
            if (MainActivity.this.mLocationManager != null && MainActivity.this.mIvGPS != null) {
                boolean isProviderEnabled = MainActivity.this.mLocationManager.isProviderEnabled(GeocodeSearch.GPS);
                Logger.d("gps change:" + isProviderEnabled);
                MainActivity.this.mIvGPS.setSelected(isProviderEnabled);
            }
        }
    };
    /* access modifiers changed from: private */
    public String mCurrentCity = null;
    private String mCurrentName = null;
    private int mCurrentPage;
    private BitmapDescriptor mDescriptor;
    private ExecutorService mExecutorService;
    private boolean mFirstFix = false;
    /* access modifiers changed from: private */
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 0) {
                if (MainActivity.this.mSpeechTips.getVisibility() == View.VISIBLE) {
                    Logger.d("当前音量：" + message.arg1);
                    MainActivity.this.mVoiceLineView.setVolume(message.arg1);
                }
            } else if (message.what == 1) {
                Toast.makeText(MainActivity.this, MainActivity.this.getString(message.arg1), Toast.LENGTH_SHORT).show();

            }
        }
    };
    /* access modifiers changed from: private */
    public Bundle mHoinnetBundle = null;
    private HashMap<String, String> mIatResults = new LinkedHashMap();
    /* access modifiers changed from: private */
    public ImageView mImageViewLocation;
    private ImageView mImageViewSpeech;
    /* access modifiers changed from: private */
    public ImageView mImageViewVoice;
    /*private InitListener mInitListener = new InitListener() {
        public void onInit(int i) {
            Logger.d("语音识别初始码:" + i);
            if (i != 0) {
                Logger.d("初始化失败，错误码：" + i);
            }
        }
    };*/
    /* access modifiers changed from: private */
    public ImageView mIvGPS;
    /* access modifiers changed from: private */
    public LatLng mLastLocation = null;
    private LayoutInflater mLayoutInflater;
    private RelativeLayout mLayoutPantView;
    private WindowManager.LayoutParams mLayoutParams;
    private Marker mLocMarker;
    /* access modifiers changed from: private */
    public LocationManager mLocationManager;
    private AMapLocationClientOption mLocationOption;
    private View mPopupView;
   /* private RecognizerListener mRecognizerListener = new RecognizerListener() {
        public void onEvent(int i, int i2, int i3, Bundle bundle) {
        }

        public void onVolumeChanged(int i, byte[] bArr) {
            Message obtainMessage = MainActivity.this.mHandler.obtainMessage();
            obtainMessage.what = 0;
            obtainMessage.arg1 = i;
            MainActivity.this.mHandler.sendMessage(obtainMessage);
        }

        public void onBeginOfSpeech() {
            MainActivity.this.mSpeechTips.setVisibility(View.VISIBLE);
        }

        public void onEndOfSpeech() {
            MainActivity.this.mSpeechTips.setVisibility(View.GONE);
        }

        public void onResult(RecognizerResult recognizerResult, boolean z) {
            MainActivity.this.printResult(recognizerResult, z);
            Logger.d(recognizerResult.getResultString() + "     isLast:" + z);
        }

        public void onError(SpeechError speechError) {
            if (10118 == speechError.getErrorCode()) {
                Toast.makeText(mContext, R.string.no_input, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, speechError.getPlainDescription(true), Toast.LENGTH_SHORT).show();
            }
            MainActivity.this.mSpeechTips.setVisibility(View.GONE);
        }
    };*/
    private Runnable mRunnable = new Runnable() {
        /* JADX WARNING: Removed duplicated region for block: B:18:0x00be A[Catch:{ AMapException -> 0x0170 }] */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x0117 A[Catch:{ AMapException -> 0x0170 }] */
        /* JADX WARNING: Removed duplicated region for block: B:31:0x013a A[Catch:{ AMapException -> 0x0170 }] */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x0140 A[Catch:{ AMapException -> 0x0170 }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r11 = this;
                com.android.hoinnet.highde.activity.MainActivity r0 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                android.os.Bundle r0 = r0.mHoinnetBundle     // Catch:{ AMapException -> 0x0170 }
                if (r0 != 0) goto L_0x0009
                return
            L_0x0009:
                com.android.hoinnet.highde.activity.MainActivity r0 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                android.os.Bundle r0 = r0.mHoinnetBundle     // Catch:{ AMapException -> 0x0170 }
                java.lang.String r1 = "_start"
                java.lang.String r2 = ""
                java.lang.String r0 = r0.getString(r1, r2)     // Catch:{ AMapException -> 0x0170 }
                com.android.hoinnet.highde.activity.MainActivity r1 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                android.os.Bundle r1 = r1.mHoinnetBundle     // Catch:{ AMapException -> 0x0170 }
                java.lang.String r2 = "_end"
                java.lang.String r3 = ""
                java.lang.String r1 = r1.getString(r2, r3)     // Catch:{ AMapException -> 0x0170 }
                com.android.hoinnet.highde.activity.MainActivity r2 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                android.os.Bundle r2 = r2.mHoinnetBundle     // Catch:{ AMapException -> 0x0170 }
                java.lang.String r3 = "_navi"
                r4 = 0
                int r2 = r2.getInt(r3, r4)     // Catch:{ AMapException -> 0x0170 }
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ AMapException -> 0x0170 }
                r3.<init>()     // Catch:{ AMapException -> 0x0170 }
                java.lang.String r5 = "start:"
                r3.append(r5)     // Catch:{ AMapException -> 0x0170 }
                r3.append(r0)     // Catch:{ AMapException -> 0x0170 }
                java.lang.String r5 = "    end:"
                r3.append(r5)     // Catch:{ AMapException -> 0x0170 }
                r3.append(r1)     // Catch:{ AMapException -> 0x0170 }
                java.lang.String r5 = "      mode:"
                r3.append(r5)     // Catch:{ AMapException -> 0x0170 }
                r3.append(r2)     // Catch:{ AMapException -> 0x0170 }
                java.lang.String r3 = r3.toString()     // Catch:{ AMapException -> 0x0170 }
                com.orhanobut.logger.Logger.d(r3)     // Catch:{ AMapException -> 0x0170 }
                boolean r3 = android.text.TextUtils.isEmpty(r0)     // Catch:{ AMapException -> 0x0170 }
                r5 = 5
                r6 = 0
                if (r3 != 0) goto L_0x00b7
                com.amap.api.services.poisearch.PoiSearch$Query r3 = new com.amap.api.services.poisearch.PoiSearch$Query     // Catch:{ AMapException -> 0x0170 }
                java.lang.String r7 = ""
                com.android.hoinnet.highde.activity.MainActivity r8 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                java.lang.String r8 = r8.mCurrentCity     // Catch:{ AMapException -> 0x0170 }
                boolean r8 = android.text.TextUtils.isEmpty(r8)     // Catch:{ AMapException -> 0x0170 }
                if (r8 == 0) goto L_0x0071
                java.lang.String r8 = ""
                goto L_0x0077
            L_0x0071:
                com.android.hoinnet.highde.activity.MainActivity r8 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                java.lang.String r8 = r8.mCurrentCity     // Catch:{ AMapException -> 0x0170 }
            L_0x0077:
                r3.<init>(r0, r7, r8)     // Catch:{ AMapException -> 0x0170 }
                r3.setPageSize(r5)     // Catch:{ AMapException -> 0x0170 }
                r3.setPageNum(r4)     // Catch:{ AMapException -> 0x0170 }
                com.amap.api.services.poisearch.PoiSearch r0 = new com.amap.api.services.poisearch.PoiSearch     // Catch:{ AMapException -> 0x0170 }
                com.android.hoinnet.highde.activity.MainActivity r7 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                r0.<init>(r7, r3)     // Catch:{ AMapException -> 0x0170 }
                com.amap.api.services.poisearch.PoiResult r0 = r0.searchPOI()     // Catch:{ AMapException -> 0x0170 }
                if (r0 == 0) goto L_0x00b7
                java.util.ArrayList r3 = r0.getPois()     // Catch:{ AMapException -> 0x0170 }
                int r3 = r3.size()     // Catch:{ AMapException -> 0x0170 }
                if (r3 <= 0) goto L_0x00b7
                java.util.ArrayList r0 = r0.getPois()     // Catch:{ AMapException -> 0x0170 }
                java.lang.Object r0 = r0.get(r4)     // Catch:{ AMapException -> 0x0170 }
                com.amap.api.services.core.PoiItem r0 = (com.amap.api.services.core.PoiItem) r0     // Catch:{ AMapException -> 0x0170 }
                com.amap.api.maps.model.LatLng r3 = new com.amap.api.maps.model.LatLng     // Catch:{ AMapException -> 0x0170 }
                com.amap.api.services.core.LatLonPoint r7 = r0.getLatLonPoint()     // Catch:{ AMapException -> 0x0170 }
                double r7 = r7.getLatitude()     // Catch:{ AMapException -> 0x0170 }
                com.amap.api.services.core.LatLonPoint r0 = r0.getLatLonPoint()     // Catch:{ AMapException -> 0x0170 }
                double r9 = r0.getLongitude()     // Catch:{ AMapException -> 0x0170 }
                r3.<init>(r7, r9)     // Catch:{ AMapException -> 0x0170 }
                goto L_0x00b8
            L_0x00b7:
                r3 = r6
            L_0x00b8:
                boolean r0 = android.text.TextUtils.isEmpty(r1)     // Catch:{ AMapException -> 0x0170 }
                if (r0 != 0) goto L_0x0117
                com.amap.api.services.poisearch.PoiSearch$Query r0 = new com.amap.api.services.poisearch.PoiSearch$Query     // Catch:{ AMapException -> 0x0170 }
                java.lang.String r7 = ""
                com.android.hoinnet.highde.activity.MainActivity r8 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                java.lang.String r8 = r8.mCurrentCity     // Catch:{ AMapException -> 0x0170 }
                boolean r8 = android.text.TextUtils.isEmpty(r8)     // Catch:{ AMapException -> 0x0170 }
                if (r8 == 0) goto L_0x00d1
                java.lang.String r8 = ""
                goto L_0x00d7
            L_0x00d1:
                com.android.hoinnet.highde.activity.MainActivity r8 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                java.lang.String r8 = r8.mCurrentCity     // Catch:{ AMapException -> 0x0170 }
            L_0x00d7:
                r0.<init>(r1, r7, r8)     // Catch:{ AMapException -> 0x0170 }
                r0.setPageSize(r5)     // Catch:{ AMapException -> 0x0170 }
                r0.setPageNum(r4)     // Catch:{ AMapException -> 0x0170 }
                com.amap.api.services.poisearch.PoiSearch r1 = new com.amap.api.services.poisearch.PoiSearch     // Catch:{ AMapException -> 0x0170 }
                com.android.hoinnet.highde.activity.MainActivity r5 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                r1.<init>(r5, r0)     // Catch:{ AMapException -> 0x0170 }
                com.amap.api.services.poisearch.PoiResult r0 = r1.searchPOI()     // Catch:{ AMapException -> 0x0170 }
                if (r0 == 0) goto L_0x0132
                java.util.ArrayList r1 = r0.getPois()     // Catch:{ AMapException -> 0x0170 }
                int r1 = r1.size()     // Catch:{ AMapException -> 0x0170 }
                if (r1 <= 0) goto L_0x0132
                java.util.ArrayList r0 = r0.getPois()     // Catch:{ AMapException -> 0x0170 }
                java.lang.Object r0 = r0.get(r4)     // Catch:{ AMapException -> 0x0170 }
                com.amap.api.services.core.PoiItem r0 = (com.amap.api.services.core.PoiItem) r0     // Catch:{ AMapException -> 0x0170 }
                com.amap.api.maps.model.LatLng r1 = new com.amap.api.maps.model.LatLng     // Catch:{ AMapException -> 0x0170 }
                com.amap.api.services.core.LatLonPoint r4 = r0.getLatLonPoint()     // Catch:{ AMapException -> 0x0170 }
                double r4 = r4.getLatitude()     // Catch:{ AMapException -> 0x0170 }
                com.amap.api.services.core.LatLonPoint r0 = r0.getLatLonPoint()     // Catch:{ AMapException -> 0x0170 }
                double r7 = r0.getLongitude()     // Catch:{ AMapException -> 0x0170 }
                r1.<init>(r4, r7)     // Catch:{ AMapException -> 0x0170 }
                goto L_0x0133
            L_0x0117:
                com.android.hoinnet.highde.activity.MainActivity r0 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                android.os.Handler r0 = r0.mHandler     // Catch:{ AMapException -> 0x0170 }
                android.os.Message r0 = r0.obtainMessage()     // Catch:{ AMapException -> 0x0170 }
                r1 = 1
                r0.what = r1     // Catch:{ AMapException -> 0x0170 }
                r1 = 2131492913(0x7f0c0031, float:1.8609291E38)
                r0.arg1 = r1     // Catch:{ AMapException -> 0x0170 }
                com.android.hoinnet.highde.activity.MainActivity r1 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                android.os.Handler r1 = r1.mHandler     // Catch:{ AMapException -> 0x0170 }
                r1.sendMessage(r0)     // Catch:{ AMapException -> 0x0170 }
            L_0x0132:
                r1 = r6
            L_0x0133:
                android.content.Intent r0 = new android.content.Intent     // Catch:{ AMapException -> 0x0170 }
                r0.<init>()     // Catch:{ AMapException -> 0x0170 }
                if (r3 == 0) goto L_0x0140
                java.lang.String r4 = "_start"
                r0.putExtra(r4, r3)     // Catch:{ AMapException -> 0x0170 }
                goto L_0x014b
            L_0x0140:
                java.lang.String r3 = "_start"
                com.android.hoinnet.highde.activity.MainActivity r4 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                com.amap.api.maps.model.LatLng r4 = r4.mLastLocation     // Catch:{ AMapException -> 0x0170 }
                r0.putExtra(r3, r4)     // Catch:{ AMapException -> 0x0170 }
            L_0x014b:
                java.lang.String r3 = "_end"
                r0.putExtra(r3, r1)     // Catch:{ AMapException -> 0x0170 }
                java.lang.String r1 = "_navi"
                r0.putExtra(r1, r2)     // Catch:{ AMapException -> 0x0170 }
                com.android.hoinnet.highde.activity.MainActivity r1 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                java.lang.Class<com.android.hoinnet.highde.activity.WalkNaviActivity> r2 = com.android.hoinnet.highde.activity.WalkNaviActivity.class
                r0.setClass(r1, r2)     // Catch:{ AMapException -> 0x0170 }
                com.android.hoinnet.highde.activity.MainActivity r1 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                r1.startActivity(r0)     // Catch:{ AMapException -> 0x0170 }
                com.android.hoinnet.highde.activity.MainActivity r0 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                android.os.Bundle r0 = r0.mHoinnetBundle     // Catch:{ AMapException -> 0x0170 }
                r0.clear()     // Catch:{ AMapException -> 0x0170 }
                com.android.hoinnet.highde.activity.MainActivity r0 = com.android.hoinnet.highde.activity.MainActivity.this     // Catch:{ AMapException -> 0x0170 }
                android.os.Bundle unused = r0.mHoinnetBundle = r6     // Catch:{ AMapException -> 0x0170 }
                goto L_0x0174
            L_0x0170:
                r0 = move-exception
                r0.printStackTrace()
            L_0x0174:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.hoinnet.highde.activity.MainActivity.AnonymousClass5.run():void");
        }
    };
    private RxPopupViewManager mRxPopupViewManager;
    private SensorEventHelper mSensorHelper;
    //private SpeechRecognizer mSpeechRecognizer;
    /* access modifiers changed from: private */
    public View mSpeechTips;
    /* access modifiers changed from: private */
    public VoiceLineView mVoiceLineView;
    /* access modifiers changed from: private */
    public WearMapView mWearMapView;
    private WindowManager mWindowManager;
    private AMapLocationClient mlocationClient;

    public void onCameraChange(CameraPosition cameraPosition) {
    }
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        this.mHoinnetBundle = getIntent().getBundleExtra(Constant.HOINNET_NAVI_DATA);
        this.mExecutorService = Executors.newSingleThreadExecutor();
        initView();
        this.mWearMapView.onCreate(bundle);
        init();
    }

    private void initView() {
        this.mLayoutPantView = findViewById(R.id.main_pantview);
        this.mWearMapView = findViewById(R.id.main_wearmapview);
        this.mImageViewVoice = findViewById(R.id.img_voice);
        this.mImageViewLocation = findViewById(R.id.img_locaion);
        this.mIvGPS = findViewById(R.id.img_gps);
        this.mIvGPS.setOnClickListener(this);
        this.mImageViewVoice.setOnClickListener(this);
        this.mImageViewLocation.setOnClickListener(this);
        this.mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        this.mLayoutInflater = LayoutInflater.from(this);
        createView();
        this.mRxPopupViewManager = new RxPopupViewManager(this);
        this.mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.mIvGPS.setSelected(this.mLocationManager.isProviderEnabled(GeocodeSearch.GPS));
    }

    private void createView() {
        this.mSpeechTips = this.mLayoutInflater.inflate(R.layout.bd_asr_popup_speech, null);
        this.mImageViewSpeech = this.mSpeechTips.findViewById(R.id.imageView);
        this.mVoiceLineView = this.mSpeechTips.findViewById(R.id.voicLine);
        this.mLayoutParams = new WindowManager.LayoutParams();
        this.mLayoutParams.gravity = 17;
        this.mLayoutParams.type = AMapException.CODE_AMAP_SERVICE_MAINTENANCE;
        this.mLayoutParams.format = 1;
        this.mLayoutParams.flags = 8;
        this.mLayoutParams.width = -2;
        this.mLayoutParams.height = -2;
        this.mWindowManager.addView(this.mSpeechTips, this.mLayoutParams);
        this.mSpeechTips.setVisibility(View.GONE);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.mWearMapView.onSaveInstanceState(bundle);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mWearMapView.onResume();
        if (this.mSensorHelper != null) {
            this.mSensorHelper.registerSensorListener();
        }
        if (this.mIvGPS != null && this.mLocationManager != null) {
            this.mIvGPS.setSelected(this.mLocationManager.isProviderEnabled(GeocodeSearch.GPS));
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        getContentResolver().registerContentObserver(Settings.Secure.getUriFor("location_mode"), false, this.mContentObserver);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        getContentResolver().unregisterContentObserver(this.mContentObserver);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mWearMapView.onPause();
    }

    private void init() {
        this.mWearMapView.setOnDismissCallbackListener(new WearMapView.OnDismissCallback() {
            public void onDismiss() {
                MainActivity.this.mWearMapView.onDismiss();
                MainActivity.this.finish();
            }

            public void onNotifySwipe() {
                if (MainActivity.this.mImageViewLocation != null && MainActivity.this.mImageViewLocation.getVisibility() == View.VISIBLE) {
                    MainActivity.this.mImageViewLocation.setVisibility(View.GONE);
                }
                if (MainActivity.this.mImageViewVoice != null && MainActivity.this.mImageViewVoice.getVisibility() == View.VISIBLE) {
                    MainActivity.this.mImageViewVoice.setVisibility(View.GONE);
                }
            }
        });
        if (this.mAMap == null) {
            this.mAMap = this.mWearMapView.getMap();
            setUpMap();
        }
        this.mSensorHelper = new SensorEventHelper(this);
        if (this.mSensorHelper != null) {
            this.mSensorHelper.registerSensorListener();
        }
        this.mAMap.setOnCameraChangeListener(this);
        this.mAMap.setOnMapClickListener(this);
        this.mSpeechRecognizer = SpeechRecognizer.createRecognizer(this, this.mInitListener);
        setParamSpeechRecognizer();
    }

    public void setParamSpeechRecognizer() {
        this.mSpeechRecognizer.setParameter(SpeechConstant.PARAMS, null);
        this.mSpeechRecognizer.setParameter("engine_type", "cloud");
        this.mSpeechRecognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");
        this.mSpeechRecognizer.setParameter("language", "zh_cn");
        this.mSpeechRecognizer.setParameter("accent", "mandarin");
        this.mSpeechRecognizer.setParameter("vad_bos", "4000");
        this.mSpeechRecognizer.setParameter("vad_eos", "1000");
        this.mSpeechRecognizer.setParameter("asr_ptt", "0");
    }

    private void setUpMap() {
        AMap aMap = this.mAMap;
        this.mAMap.setLocationSource(this);
        this.mAMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.mAMap.getUiSettings().setZoomControlsEnabled(false);
        this.mAMap.setMyLocationEnabled(true);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        motionEvent.getAction();
        return super.onTouchEvent(motionEvent);
    }

    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        if (this.mlocationClient == null) {
            this.mlocationClient = new AMapLocationClient(this);
            this.mLocationOption = new AMapLocationClientOption();
            this.mlocationClient.setLocationListener(this);
            this.mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            this.mlocationClient.setLocationOption(this.mLocationOption);
            this.mlocationClient.startLocation();
        }
    }

    public void deactivate() {
        if (this.mlocationClient != null) {
            this.mlocationClient.stopLocation();
            this.mlocationClient.onDestroy();
        }
        this.mlocationClient = null;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mSensorHelper != null) {
            this.mSensorHelper.unRegisterSensorListener();
            this.mSensorHelper.setCurrentMarker(null);
            this.mSensorHelper = null;
        }
        this.mWearMapView.onDestroy();
        if (this.mlocationClient != null) {
            this.mlocationClient.unRegisterLocationListener(this);
            this.mlocationClient.onDestroy();
        }
        if (this.mDescriptor != null) {
            this.mDescriptor.recycle();
        }
        if (this.mSpeechRecognizer != null) {
            this.mSpeechRecognizer.cancel();
            this.mSpeechRecognizer.destroy();
        }
        this.mFirstFix = false;
        this.mLastLocation = null;
        this.mCurrentCity = null;
        this.mCurrentName = null;
        Process.killProcess(Process.myPid());
    }

    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            boolean z = false;
            if (aMapLocation == null || aMapLocation.getErrorCode() != 0) {
                String str = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Logger.e("AmapErr" + str);
                return;
            }
            LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            if (this.mLastLocation != null && this.mFirstFix && latLng.latitude == this.mLastLocation.latitude && latLng.longitude == this.mLastLocation.longitude) {
                z = true;
            }
            this.mLastLocation = latLng;
            this.mCurrentCity = aMapLocation.getCity();
            this.mCurrentName = aMapLocation.getPoiName();
            if (!this.mFirstFix) {
                this.mFirstFix = true;
                addCircle(latLng, (double) aMapLocation.getAccuracy());
                addMarker(latLng);
                this.mSensorHelper.setCurrentMarker(this.mLocMarker);
            } else {
                this.mCircle.setCenter(latLng);
                this.mCircle.setRadius((double) aMapLocation.getAccuracy());
                this.mLocMarker.setPosition(latLng);
            }
            if (!z) {
                this.mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
            }
            this.mlocationClient.stopLocation();
            if (this.mHoinnetBundle != null) {
                startNavi();
            }
        }
    }

    private void startNavi() {
        this.mExecutorService.execute(this.mRunnable);
    }

    private void addCircle(LatLng latLng, double d) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.strokeWidth(1.0f);
        circleOptions.fillColor(FILL_COLOR);
        circleOptions.strokeColor(STROKE_COLOR);
        circleOptions.center(latLng);
        circleOptions.radius(d);
        this.mCircle = this.mAMap.addCircle(circleOptions);
    }

    private void addMarker(LatLng latLng) {
        if (this.mDescriptor != null) {
            this.mDescriptor.recycle();
        }
        this.mDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.navi_map_gps_50_locked);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(this.mDescriptor);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.position(latLng);
        this.mLocMarker = this.mAMap.addMarker(markerOptions);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_gps:
                try {
                    Intent intent = new Intent();
                    intent.setAction(Constant.GPS_ACTION);
                    jumpActivity(intent);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            case R.id.img_locaion:
                cilckLocation();
                return;
            case R.id.img_voice:
                try {
                    jumpActivityForResult(new Intent(this.mContext, NaviInputActivity.class), 2);
                    return;
                } catch (Exception e2) {
                    Toast.makeText(this, getString(R.string.not_find_voice), Toast.LENGTH_SHORT).show();
                    e2.printStackTrace();
                    return;
                }
            default:
                return;
        }
    }

    private void testCode() {
        Intent intent = new Intent();
        intent.setClass(this, PoiListActivity.class);
        intent.putExtra(Constant.INTERT_KEY_CURRCITY, this.mCurrentCity);
        intent.putExtra(Constant.INTERT_KEY_CURRPAGE, this.mCurrentPage);
        intent.putExtra(Constant.INTERT_KEY_LASTLOCATION, this.mLastLocation);
        intent.putExtra(Constant.INTERT_KEY_VICETEXT, "世界之窗");
        jumpActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 2 && i2 == -1) {
            String stringExtra = intent.getStringExtra("text");
            if (TextUtils.isEmpty(this.mCurrentCity)) {
                this.mCurrentCity = "";
            }
            this.mCurrentPage = 0;
            if (!TextUtils.isEmpty(stringExtra) && !"null".equals(stringExtra) && stringExtra.length() > 0) {
                Intent intent2 = new Intent();
                intent2.setClass(this, PoiListActivity.class);
                intent2.putExtra(Constant.INTERT_KEY_CURRCITY, this.mCurrentCity);
                intent2.putExtra(Constant.INTERT_KEY_CURRPAGE, this.mCurrentPage);
                intent2.putExtra(Constant.INTERT_KEY_LASTLOCATION, this.mLastLocation);
                intent2.putExtra(Constant.INTERT_KEY_VICETEXT, stringExtra);
                startActivity(intent2);
            }
        }
    }

    private void startSpeechRecognizer() {
        if (this.mPopupView != null && this.mRxPopupViewManager.isVisible(this.mPopupView)) {
            this.mRxPopupViewManager.clear();
        }
        if (this.mSpeechRecognizer.startListening((com.iflytek.speech.RecognizerListener) this.mRecognizerListener) != 0) {
            Logger.d("听写失败");
        } else {
            Logger.d("听写开始");
        }
    }

    /* access modifiers changed from: private */
    public void printResult(RecognizerResult recognizerResult, boolean z) {
        String str;
        String parseIatResult = JsonParser.parseIatResult(recognizerResult.getResultString());
        try {
            str = new JSONObject(recognizerResult.getResultString()).optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
            str = null;
        }
        this.mIatResults.put(str, parseIatResult);
        if (z) {
            StringBuffer stringBuffer = new StringBuffer();
            for (String str2 : this.mIatResults.keySet()) {
                stringBuffer.append(this.mIatResults.get(str2));
            }
            String stringBuffer2 = stringBuffer.toString();
            Logger.d("结果:" + stringBuffer2);
            if (!TextUtils.isEmpty(stringBuffer2)) {
                this.mRxPopupViewManager.findAndDismiss(this.mImageViewVoice);
                RxPopupView.Builder builder = new RxPopupView.Builder(this, this.mImageViewVoice, this.mLayoutPantView, stringBuffer2, 0);
                this.mBuilder = builder;
                this.mBuilder.setAlign(0);
                this.mPopupView = this.mRxPopupViewManager.show(this.mBuilder.build());
                return;
            }
            Toast.makeText(this, R.string.recognition_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private void cilckLocation() {
        if (this.mlocationClient != null) {
            AMapLocation lastKnownLocation = this.mlocationClient.getLastKnownLocation();
            if (lastKnownLocation != null) {
                this.mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 17.0f));
            }
            this.mlocationClient.startLocation();
            return;
        }
        Logger.d("定位失败");
        if (this.mAMap != null) {
            this.mAMap.clear();
        }
        setUpMap();
    }

    public void onMapClick(LatLng latLng) {
        if (this.mPopupView != null && this.mRxPopupViewManager.isVisible(this.mPopupView)) {
            this.mRxPopupViewManager.clear();
        }
    }

    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (this.mPopupView != null && this.mRxPopupViewManager.isVisible(this.mPopupView)) {
            this.mRxPopupViewManager.clear();
        }
        if (this.mImageViewVoice != null && this.mImageViewVoice.getVisibility() == View.GONE) {
            this.mImageViewVoice.setVisibility(View.VISIBLE);
        }
        if (this.mImageViewLocation != null && this.mImageViewLocation.getVisibility() == View.GONE) {
            this.mImageViewLocation.setVisibility(View.VISIBLE);
        }
    }

    public void onTipDismissed(View view, int i, boolean z) {
        Logger.d("   onTipDismissed   " + z);
        if (z && (view instanceof TextView)) {
            String charSequence = ((TextView) view).getText().toString();
            if (TextUtils.isEmpty(this.mCurrentCity)) {
                this.mCurrentCity = "";
            }
            this.mCurrentPage = 0;
            if (!TextUtils.isEmpty(charSequence) && !"null".equals(charSequence) && charSequence.length() > 0) {
                Intent intent = new Intent();
                intent.setClass(this, PoiListActivity.class);
                intent.putExtra(Constant.INTERT_KEY_CURRCITY, this.mCurrentCity);
                intent.putExtra(Constant.INTERT_KEY_CURRPAGE, this.mCurrentPage);
                intent.putExtra(Constant.INTERT_KEY_LASTLOCATION, this.mLastLocation);
                intent.putExtra(Constant.INTERT_KEY_VICETEXT, charSequence);
                jumpActivity(intent);
            }
        }
    }
}
