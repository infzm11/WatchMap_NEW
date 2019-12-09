package com.android.hoinnet.highde;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.android.hoinnet.highde.activity.WaitActivity;
import com.android.hoinnet.highde.activity.WalkNaviActivity;
import com.android.hoinnet.highde.utils.Constant;
import com.iflytek.clientadapter.aidl.PoiInfo;
import com.iflytek.clientadapter.aidl.RouteInfo;
import com.iflytek.sdk.client.CustomNaviClient;
import com.orhanobut.logger.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SmartNavigation extends CustomNaviClient implements AMapLocationListener {
    private static final int PAGETOAL = 30;
    private static SmartNavigation mSmartNavigation;
    private boolean isNavi = false;
    /* access modifiers changed from: private */
    public Context mContext = null;
    /* access modifiers changed from: private */
    public int mCurrPage = 0;
    private String mCurrentAddress = null;
    /* access modifiers changed from: private */
    public String mCurrentCity = null;
    private String mCurrentName = null;
    private final ExecutorService mExecutorService;
    private LatLng mLastLocation = null;
    public AMapLocationClientOption mLocationOption = null;
    /* access modifiers changed from: private */
    public PoiSearch mPoiSearch;
    /* access modifiers changed from: private */
    public PoiSearch.Query mQuery;
    /* access modifiers changed from: private */
    public final RouteSearch mRouteSearch;
    public AMapLocationClient mlocationClient;

    class QuerCallable implements Callable<List<PoiInfo>> {
        private String keyWord;

        public QuerCallable(String str) {
            this.keyWord = str;
        }

        public List<PoiInfo> call() throws Exception {
            try {
                PoiSearch.Query unused = SmartNavigation.this.mQuery = new PoiSearch.Query(this.keyWord, "", TextUtils.isEmpty(SmartNavigation.this.mCurrentCity) ? "" : SmartNavigation.this.mCurrentCity);
                SmartNavigation.this.mQuery.setPageSize(30);
                SmartNavigation.this.mQuery.setPageNum(SmartNavigation.this.mCurrPage);
                SmartNavigation.this.mQuery.setCityLimit(false);
                PoiSearch unused2 = SmartNavigation.this.mPoiSearch = new PoiSearch(SmartNavigation.this.mContext, SmartNavigation.this.mQuery);
                PoiResult searchPOI = SmartNavigation.this.mPoiSearch.searchPOI();
                if (searchPOI == null) {
                    return null;
                }
                ArrayList arrayList = new ArrayList();
                ArrayList<PoiItem> pois = searchPOI.getPois();
                if (pois == null || pois.size() <= 0) {
                    Toast.makeText(SmartNavigation.this.mContext, R.string.no_result, 0).show();
                    return null;
                }
                for (PoiItem next : pois) {
                    PoiInfo poiInfo = new PoiInfo();
                    poiInfo.setCategory(0);
                    poiInfo.setLocationTime(0);
                    poiInfo.setDistance(next.getDistance());
                    if (next.getEnter() != null) {
                        poiInfo.setLatitude(next.getEnter().getLatitude());
                        poiInfo.setLongitude(next.getEnter().getLongitude());
                    } else {
                        poiInfo.setLatitude(next.getLatLonPoint().getLatitude());
                        poiInfo.setLongitude(next.getLatLonPoint().getLongitude());
                    }
                    poiInfo.setPoiName(next.getTitle());
                    poiInfo.setPoiAddress(next.getSnippet());
                    Logger.d("地址：" + next.getSnippet());
                    arrayList.add(poiInfo);
                }
                return arrayList;
            } catch (AMapException e) {
                e.printStackTrace();
            }
        }
    }

    class QuerCallableRide implements Callable<List<RouteInfo>> {
        private PoiInfo endInfo;
        private PoiInfo startInfo;

        public QuerCallableRide(PoiInfo poiInfo, PoiInfo poiInfo2) {
            this.startInfo = poiInfo;
            this.endInfo = poiInfo2;
        }

        public List<RouteInfo> call() throws Exception {
            RouteSearch.FromAndTo fromAndTo;
            if (this.startInfo == null || this.endInfo == null) {
                fromAndTo = new RouteSearch.FromAndTo();
            } else {
                fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(this.startInfo.getLatitude(), this.startInfo.getLongitude()), new LatLonPoint(this.endInfo.getLatitude(), this.endInfo.getLongitude()));
            }
            try {
                RideRouteResult calculateRideRoute = SmartNavigation.this.mRouteSearch.calculateRideRoute(new RouteSearch.RideRouteQuery(fromAndTo));
                if (calculateRideRoute == null) {
                    return null;
                }
                LatLonPoint startPos = calculateRideRoute.getStartPos();
                LatLonPoint targetPos = calculateRideRoute.getTargetPos();
                if (startPos == null || targetPos == null) {
                    return null;
                }
                ArrayList arrayList = new ArrayList();
                RouteInfo routeInfo = new RouteInfo();
                routeInfo.setFromPoiLatitude(startPos.getLatitude());
                routeInfo.setFromPoiLongitude(startPos.getLongitude());
                routeInfo.setToPoiLatitude(targetPos.getLatitude());
                routeInfo.setToPoiLongitude(targetPos.getLongitude());
                arrayList.add(routeInfo);
                return arrayList;
            } catch (AMapException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static SmartNavigation getInstance(Context context) {
        if (mSmartNavigation == null) {
            mSmartNavigation = new SmartNavigation(context);
        }
        return mSmartNavigation;
    }

    private SmartNavigation(Context context) {
        this.mContext = context;
        this.mExecutorService = Executors.newSingleThreadExecutor();
        this.mlocationClient = new AMapLocationClient(this.mContext);
        this.mLocationOption = new AMapLocationClientOption();
        this.mlocationClient.setLocationListener(this);
        this.mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        this.mLocationOption.setInterval(2000);
        this.mlocationClient.setLocationOption(this.mLocationOption);
        this.mlocationClient.startLocation();
        this.mRouteSearch = new RouteSearch(this.mContext);
    }

    public boolean open() {
        Logger.d("-----------------open-------------------------");
        try {
            Intent intent = new Intent();
            intent.setClass(this.mContext, WaitActivity.class);
            intent.addFlags(268435456);
            this.mContext.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean close() {
        Logger.d("-----------------close-------------------------");
        if (this.mlocationClient == null) {
            return false;
        }
        Intent intent = new Intent();
        intent.setAction(Constant.EXITACTION);
        this.mContext.sendBroadcast(intent);
        this.mlocationClient.stopLocation();
        this.mlocationClient.onDestroy();
        return false;
    }

    public boolean changeScale(int i) {
        Logger.d("-----------------changeScale-------------------------");
        return false;
    }

    public boolean changeMode(int i) {
        Logger.d("-----------------changeMode-------------------------");
        return false;
    }

    public boolean changeView(int i) {
        Logger.d("-----------------changeView-------------------------");
        return false;
    }

    public boolean showTraffic(boolean z) {
        Logger.d("-----------------showTraffic-------------------------");
        return false;
    }

    public PoiInfo getCurrentPoi() {
        Logger.d("-----------------getCurrentPoi-------------------------");
        Logger.d(" --------getCurrentPoi --------");
        if (this.mlocationClient != null) {
            this.mlocationClient.startLocation();
            return null;
        }
        PoiInfo poiInfo = new PoiInfo();
        poiInfo.setLatitude(this.mLastLocation.latitude);
        poiInfo.setLongitude(this.mLastLocation.longitude);
        poiInfo.setPoiName(this.mCurrentName);
        poiInfo.setPoiAddress(this.mCurrentAddress);
        return poiInfo;
    }

    public boolean showPoi(PoiInfo poiInfo) {
        Logger.d("-----------------showPoi-------------------------");
        return false;
    }

    public boolean setHomePoi(PoiInfo poiInfo) {
        Logger.d("-----------------setHomePoi-------------------------");
        return false;
    }

    public PoiInfo getHomePoi() {
        Logger.d("-----------------getHomePoi-------------------------");
        return null;
    }

    public boolean setCompanyPoi(PoiInfo poiInfo) {
        Logger.d("-----------------setCompanyPoi-------------------------");
        return false;
    }

    public PoiInfo getCompanyPoi() {
        Logger.d("-----------------getCompanyPoi-------------------------");
        return null;
    }

    public List<PoiInfo> search(String str, PoiInfo poiInfo, int i) {
        Logger.d("-----------------search-------------------------");
        Logger.d("MyNavi---search:" + str + poiInfo.toString() + i);
        return doSearchQuery(str);
    }

    public List<PoiInfo> searchByRoute(String str, int i) {
        Logger.d("-----------------searchByRoute-------------------------");
        return null;
    }

    public RouteInfo getCurrentRouteInfo() {
        Logger.d("-----------------getCurrentRouteInfo-------------------------");
        return null;
    }

    public boolean previewRoute() {
        Logger.d("-----------------getCurrentRouteInfo-------------------------");
        return false;
    }

    public List<RouteInfo> planRoute(PoiInfo poiInfo, PoiInfo poiInfo2, List<PoiInfo> list) {
        Logger.d("-----------------planRoute-------------------------");
        return null;
    }

    public boolean navigate(RouteInfo routeInfo) {
        LatLng latLng;
        Logger.d("---------------navigate--------------");
        if (routeInfo == null) {
            return false;
        }
        if (routeInfo.getFromPoiLatitude() == 0.0d && routeInfo.getFromPoiLongitude() == 0.0d) {
            latLng = new LatLng(this.mLastLocation.latitude, this.mLastLocation.longitude);
        } else {
            latLng = new LatLng(routeInfo.getFromPoiLatitude(), routeInfo.getFromPoiLongitude());
        }
        statNavi(latLng, new LatLng(routeInfo.getToPoiLatitude(), routeInfo.getToPoiLongitude()), -1);
        return false;
    }

    public boolean navigate(PoiInfo poiInfo, PoiInfo poiInfo2, List<PoiInfo> list) {
        LatLng latLng;
        Logger.d("---------------navigate(PoiInfo poiInfo, PoiInfo poiInfo1, List<PoiInfo> list)--------------");
        if (poiInfo == null) {
            latLng = new LatLng(this.mLastLocation.latitude, this.mLastLocation.longitude);
        } else if (poiInfo.getLatitude() == 0.0d && poiInfo.getLongitude() == 0.0d) {
            latLng = new LatLng(this.mLastLocation.latitude, this.mLastLocation.longitude);
        } else {
            latLng = new LatLng(poiInfo.getLatitude(), poiInfo.getLongitude());
        }
        statNavi(latLng, poiInfo2 != null ? new LatLng(poiInfo2.getLatitude(), poiInfo2.getLongitude()) : null, -1);
        return false;
    }

    public boolean replanRoute(int i) {
        Logger.d("---------------replanRoute()--------------");
        return false;
    }

    public boolean cancelNavigation() {
        Logger.d("-------------cancelNavigation----------------");
        if (this.mContext == null) {
            return false;
        }
        Logger.d("-------------cancelNavigation--2222222--------------");
        Intent intent = new Intent();
        intent.setAction(Constant.ACTION_FINISH);
        this.mContext.sendBroadcast(intent);
        this.isNavi = false;
        return true;
    }

    public boolean simulateNavigation(RouteInfo routeInfo) {
        Logger.d("---------------simulateNavigation()--------------");
        return false;
    }

    public boolean simulateNavigation(PoiInfo poiInfo, PoiInfo poiInfo2, List<PoiInfo> list) {
        Logger.d("---------------simulateNavigation()--------------");
        return false;
    }

    public int getTimeLeft() {
        Logger.d("---------------getTimeLeft()--------------");
        return 0;
    }

    public int getDistanceLeft() {
        Logger.d("---------------getDistanceLeft()--------------");
        return 0;
    }

    public boolean changeVolume(int i) {
        Logger.d("---------------changeVolume()--------------");
        return false;
    }

    public boolean changeBroadcastFrequency(int i) {
        Logger.d("---------------changeBroadcastFrequency()--------------");
        return false;
    }

    public boolean isNavigationGuide() {
        Logger.d("---------------isNavigationGuide()--------------");
        return this.isNavi;
    }

    public void clearPoiInfo() {
        Logger.d("---------------clearPoiInfo()--------------");
    }

    private List<PoiInfo> doSearchQuery(String str) {
        try {
            Future submit = this.mExecutorService.submit(new QuerCallable(str));
            if (submit != null) {
                return (List) submit.get();
            }
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return;
        }
        if (aMapLocation == null || aMapLocation.getErrorCode() != 0) {
            String str = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
            Logger.e("AmapErr" + str, new Object[0]);
            return;
        }
        this.mLastLocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        this.mCurrentCity = aMapLocation.getCity();
        this.mCurrentName = aMapLocation.getPoiName();
        this.mCurrentAddress = aMapLocation.getAddress();
        this.mlocationClient.stopLocation();
    }

    private void statNavi(LatLng latLng, LatLng latLng2, int i) {
        Logger.d("------statNavi-------");
        Intent intent = new Intent();
        intent.setClass(this.mContext, WalkNaviActivity.class);
        intent.putExtra(Constant.START_LATLNG, latLng);
        intent.putExtra(Constant.END_LATLNG, latLng2);
        if (i == -1 || i < 0 || i > 2) {
            intent.putExtra(Constant.NAVI_MODE, 2);
        } else {
            intent.putExtra(Constant.NAVI_MODE, i);
        }
        intent.addFlags(268435456);
        this.isNavi = true;
        this.mContext.startActivity(intent);
    }
}
