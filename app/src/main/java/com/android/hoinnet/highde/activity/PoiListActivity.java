package com.android.hoinnet.highde.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.android.hoinnet.highde.BaseActivity;
import com.android.hoinnet.highde.R;
import com.android.hoinnet.highde.adapter.XRecyclerViewAdapter;
import com.android.hoinnet.highde.dialog.RxDialogShapeLoading;
import com.android.hoinnet.highde.utils.Constant;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.logger.Logger;
import com.r0adkll.slidr.Slidr;
import java.util.ArrayList;

public class PoiListActivity extends BaseActivity implements XRecyclerView.LoadingListener, PoiSearch.OnPoiSearchListener {
    private String mCurrCity;
    private int mCurrPage = 0;
    private LatLng mLastLocation = null;
    /* access modifiers changed from: private */
    public ArrayList<PoiItem> mList;
    private PoiResult mPoiResult;
    private PoiSearch mPoiSearch;
    private PoiSearch.Query mQuery;
    private RxDialogShapeLoading mRxDialogShapeLoading = null;
    private String mVcieText;
    private XRecyclerView mXRecyclerView;
    private XRecyclerViewAdapter mXRecyclerViewAdapter;
    private XRecyclerViewAdapter.XRecyclerViewOnItemClick mXRecyclerViewOnItemClick = new XRecyclerViewAdapter.XRecyclerViewOnItemClick() {
        public void XRecyclerViewOnClickItem(View view, int i) {
            PoiItem poiItem = (PoiItem) PoiListActivity.this.mList.get(i);
            if (poiItem != null) {
                Intent intent = new Intent();
                intent.putExtra(Constant.INTERT_KEY_SELECT_INDEX, i);
                intent.putExtra(Constant.INTERT_KEY_POIBEAN, poiItem);
                intent.setClass(PoiListActivity.this, SelectNaviModeActivity.class);
                PoiListActivity.this.startActivityForResult(intent, 105);
            }
        }
    };

    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }

    public void onRefresh() {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_listpoi);
        Slidr.attach(this);
        initView();
        this.mVcieText = getIntent().getStringExtra(Constant.INTERT_KEY_VICETEXT);
        this.mCurrCity = getIntent().getStringExtra(Constant.INTERT_KEY_CURRCITY);
        this.mCurrPage = getIntent().getIntExtra(Constant.INTERT_KEY_CURRPAGE, 0);
        this.mLastLocation = (LatLng) getIntent().getParcelableExtra(Constant.INTERT_KEY_LASTLOCATION);
        doSearchQuery(this.mVcieText);
    }

    private void initView() {
        this.mXRecyclerView = (XRecyclerView) findViewById(R.id.xrecyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        this.mXRecyclerView.setLayoutManager(linearLayoutManager);
        this.mList = new ArrayList<>();
        this.mXRecyclerViewAdapter = new XRecyclerViewAdapter(this, this.mList);
        this.mXRecyclerView.setAdapter(this.mXRecyclerViewAdapter);
        this.mXRecyclerViewAdapter.setXRecyclerViewOnItemClick(this.mXRecyclerViewOnItemClick);
        this.mXRecyclerView.setPullRefreshEnabled(false);
        this.mXRecyclerView.setLoadingListener(this);
        this.mRxDialogShapeLoading = new RxDialogShapeLoading((Activity) this);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 105 && i2 == -1) {
            if (this.mRxDialogShapeLoading != null) {
                this.mRxDialogShapeLoading.show();
            }
            startNaviActivity((PoiItem) intent.getParcelableExtra(Constant.INTERT_KEY_POIBEAN), intent.getIntExtra(Constant.INTERT_KEY_SELECTMODE, 2));
        }
    }

    private void startNaviActivity(PoiItem poiItem, int i) {
        LatLng latLng;
        Intent intent = new Intent();
        intent.setClass(this, WalkNaviActivity.class);
        LatLonPoint enter = poiItem.getEnter();
        if (enter != null) {
            latLng = new LatLng(enter.getLatitude(), enter.getLongitude());
        } else {
            LatLonPoint latLonPoint = poiItem.getLatLonPoint();
            latLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
        }
        intent.putExtra(Constant.START_LATLNG, this.mLastLocation);
        intent.putExtra(Constant.END_LATLNG, latLng);
        intent.putExtra(Constant.NAVI_MODE, i);
        startActivity(intent);
        finish();
    }

    public void onLoadMore() {
        if (this.mQuery != null && this.mPoiSearch != null && this.mPoiResult != null) {
            if (this.mPoiResult.getPageCount() - 1 > this.mCurrPage) {
                this.mCurrPage++;
                this.mQuery.setPageNum(this.mCurrPage);
                this.mPoiSearch.searchPOIAsyn();
                return;
            }
            Toast.makeText(this, R.string.no_data, Toast.LENGTH_SHORT).show();
        }
    }

    /* access modifiers changed from: protected */
    public void doSearchQuery(String str) {
        if (TextUtils.isEmpty(str)) {
            finish();
            return;
        }
        if (this.mRxDialogShapeLoading != null) {
            this.mRxDialogShapeLoading.show();
        }
        this.mQuery = new PoiSearch.Query(str, "", TextUtils.isEmpty(this.mCurrCity) ? "" : this.mCurrCity);
        this.mQuery.setPageSize(15);
        this.mQuery.setPageNum(this.mCurrPage);
        this.mQuery.setCityLimit(false);
        this.mPoiSearch = new PoiSearch(this, this.mQuery);
        this.mPoiSearch.setOnPoiSearchListener(this);
        this.mPoiSearch.searchPOIAsyn();
    }

    public void onPoiSearched(PoiResult poiResult, int i) {
        if (this.mRxDialogShapeLoading != null && this.mRxDialogShapeLoading.isShowing()) {
            this.mRxDialogShapeLoading.cancel();
        }
        if (this.mXRecyclerView != null) {
            this.mXRecyclerView.loadMoreComplete();
        }
        if (i != 1000) {
            Toast.makeText(this, R.string.no_result, Toast.LENGTH_SHORT).show();
            Logger.d(getString(R.string.err_code, new Object[]{Integer.valueOf(i)}));
        } else if (poiResult == null || poiResult.getQuery() == null) {
            Toast.makeText(this, R.string.no_result, Toast.LENGTH_SHORT).show();
        } else if (poiResult.getQuery().equals(this.mQuery)) {
            this.mPoiResult = poiResult;
            ArrayList<PoiItem> pois = this.mPoiResult.getPois();
            if (pois == null || pois.size() <= 0) {
                Toast.makeText(this, R.string.no_result, Toast.LENGTH_SHORT).show();
                return;
            }
            if (this.mList != null) {
                this.mList.addAll(pois);
            }
            if (this.mList.size() > 0) {
                this.mXRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mRxDialogShapeLoading != null && this.mRxDialogShapeLoading.isShowing()) {
            this.mRxDialogShapeLoading.cancel();
        }
    }
}
