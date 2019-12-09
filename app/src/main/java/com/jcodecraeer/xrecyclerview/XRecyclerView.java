package com.jcodecraeer.xrecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.jcodecraeer.xrecyclerview.AppBarStateChangeListener;
import java.util.ArrayList;
import java.util.List;

public class XRecyclerView extends RecyclerView {
    private static final float DRAG_RATE = 3.0f;
    private static final int HEADER_INIT_INDEX = 10002;
    private static final int TYPE_FOOTER = 10001;
    private static final int TYPE_REFRESH_HEADER = 10000;
    /* access modifiers changed from: private */
    public static List<Integer> sHeaderTypes = new ArrayList();
    /* access modifiers changed from: private */
    public AppBarStateChangeListener.State appbarState;
    private boolean isLoadingData;
    private boolean isNoMore;
    /* access modifiers changed from: private */
    public boolean loadingMoreEnabled;
    private final RecyclerView.AdapterDataObserver mDataObserver;
    /* access modifiers changed from: private */
    public View mEmptyView;
    /* access modifiers changed from: private */
    public View mFootView;
    /* access modifiers changed from: private */
    public ArrayList<View> mHeaderViews;
    private float mLastY;
    private LoadingListener mLoadingListener;
    private int mLoadingMoreProgressStyle;
    private int mPageCount;
    /* access modifiers changed from: private */
    public ArrowRefreshHeader mRefreshHeader;
    private int mRefreshProgressStyle;
    /* access modifiers changed from: private */
    public WrapAdapter mWrapAdapter;
    private boolean pullRefreshEnabled;

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        private DataObserver() {
        }

        public void onChanged() {
            if (XRecyclerView.this.mWrapAdapter != null) {
                XRecyclerView.this.mWrapAdapter.notifyDataSetChanged();
            }
            if (XRecyclerView.this.mWrapAdapter != null && XRecyclerView.this.mEmptyView != null) {
                int headersCount = 1 + XRecyclerView.this.mWrapAdapter.getHeadersCount();
                if (XRecyclerView.this.loadingMoreEnabled) {
                    headersCount++;
                }
                if (XRecyclerView.this.mWrapAdapter.getItemCount() == headersCount) {
                    XRecyclerView.this.mEmptyView.setVisibility(VISIBLE);
                    XRecyclerView.this.setVisibility(GONE);
                    return;
                }
                XRecyclerView.this.mEmptyView.setVisibility(GONE);
                XRecyclerView.this.setVisibility(VISIBLE);
            }
        }

        public void onItemRangeInserted(int i, int i2) {
            XRecyclerView.this.mWrapAdapter.notifyItemRangeInserted(i, i2);
        }

        public void onItemRangeChanged(int i, int i2) {
            XRecyclerView.this.mWrapAdapter.notifyItemRangeChanged(i, i2);
        }

        public void onItemRangeChanged(int i, int i2, Object obj) {
            XRecyclerView.this.mWrapAdapter.notifyItemRangeChanged(i, i2, obj);
        }

        public void onItemRangeRemoved(int i, int i2) {
            XRecyclerView.this.mWrapAdapter.notifyItemRangeRemoved(i, i2);
        }

        public void onItemRangeMoved(int i, int i2, int i3) {
            XRecyclerView.this.mWrapAdapter.notifyItemMoved(i, i2);
        }
    }

    public class DividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;
        private int mOrientation;

        public DividerItemDecoration(Drawable drawable) {
            this.mDivider = drawable;
        }

        public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
            if (this.mOrientation == 0) {
                drawHorizontalDividers(canvas, recyclerView);
            } else if (this.mOrientation == 1) {
                drawVerticalDividers(canvas, recyclerView);
            }
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            super.getItemOffsets(rect, view, recyclerView, state);
            if (recyclerView.getChildAdapterPosition(view) > XRecyclerView.this.mWrapAdapter.getHeadersCount() + 1) {
                this.mOrientation = ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation();
                if (this.mOrientation == 0) {
                    rect.left = this.mDivider.getIntrinsicWidth();
                } else if (this.mOrientation == 1) {
                    rect.top = this.mDivider.getIntrinsicHeight();
                }
            }
        }

        private void drawHorizontalDividers(Canvas canvas, RecyclerView recyclerView) {
            int paddingTop = recyclerView.getPaddingTop();
            int height = recyclerView.getHeight() - recyclerView.getPaddingBottom();
            int childCount = recyclerView.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View childAt = recyclerView.getChildAt(i);
                int right = childAt.getRight() + ((RecyclerView.LayoutParams) childAt.getLayoutParams()).rightMargin;
                this.mDivider.setBounds(right, paddingTop, this.mDivider.getIntrinsicWidth() + right, height);
                this.mDivider.draw(canvas);
            }
        }

        private void drawVerticalDividers(Canvas canvas, RecyclerView recyclerView) {
            int paddingLeft = recyclerView.getPaddingLeft();
            int width = recyclerView.getWidth() - recyclerView.getPaddingRight();
            int childCount = recyclerView.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View childAt = recyclerView.getChildAt(i);
                int bottom = childAt.getBottom() + ((RecyclerView.LayoutParams) childAt.getLayoutParams()).bottomMargin;
                this.mDivider.setBounds(paddingLeft, bottom, width, this.mDivider.getIntrinsicHeight() + bottom);
                this.mDivider.draw(canvas);
            }
        }
    }

    public interface LoadingListener {
        void onLoadMore();

        void onRefresh();
    }

    private class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private RecyclerView.Adapter adapter;

        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            public SimpleViewHolder(View view) {
                super(view);
            }
        }

        public boolean isRefreshHeader(int i) {
            return i == 0;
        }

        public WrapAdapter(RecyclerView.Adapter adapter2) {
            this.adapter = adapter2;
        }

        public RecyclerView.Adapter getOriginalAdapter() {
            return this.adapter;
        }

        public boolean isHeader(int i) {
            return i >= 1 && i < XRecyclerView.this.mHeaderViews.size() + 1;
        }

        public boolean isFooter(int i) {
            if (!XRecyclerView.this.loadingMoreEnabled || i != getItemCount() - 1) {
                return false;
            }
            return true;
        }

        public int getHeadersCount() {
            return XRecyclerView.this.mHeaderViews.size();
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            if (i == 10000) {
                return new SimpleViewHolder(XRecyclerView.this.mRefreshHeader);
            }
            if (XRecyclerView.this.isHeaderType(i)) {
                return new SimpleViewHolder(XRecyclerView.this.getHeaderViewByType(i));
            }
            if (i == 10001) {
                return new SimpleViewHolder(XRecyclerView.this.mFootView);
            }
            return this.adapter.onCreateViewHolder(viewGroup, i);
        }

        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (!isHeader(i) && !isRefreshHeader(i)) {
                int headersCount = i - (getHeadersCount() + 1);
                if (this.adapter != null && headersCount < this.adapter.getItemCount()) {
                    this.adapter.onBindViewHolder(viewHolder, headersCount);
                }
            }
        }

        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i, List<Object> list) {
            if (!isHeader(i) && !isRefreshHeader(i)) {
                int headersCount = i - (getHeadersCount() + 1);
                if (this.adapter != null && headersCount < this.adapter.getItemCount()) {
                    if (list.isEmpty()) {
                        this.adapter.onBindViewHolder(viewHolder, headersCount);
                    } else {
                        this.adapter.onBindViewHolder(viewHolder, headersCount, list);
                    }
                }
            }
        }

        public int getItemCount() {
            if (XRecyclerView.this.loadingMoreEnabled) {
                if (this.adapter != null) {
                    return getHeadersCount() + this.adapter.getItemCount() + 2;
                }
                return getHeadersCount() + 2;
            } else if (this.adapter != null) {
                return getHeadersCount() + this.adapter.getItemCount() + 1;
            } else {
                return getHeadersCount() + 1;
            }
        }

        public int getItemViewType(int i) {
            int headersCount = i - (getHeadersCount() + 1);
            if (isRefreshHeader(i)) {
                return 10000;
            }
            if (isHeader(i)) {
                return ((Integer) XRecyclerView.sHeaderTypes.get(i - 1)).intValue();
            } else if (isFooter(i)) {
                return 10001;
            } else {
                if (this.adapter == null || headersCount >= this.adapter.getItemCount()) {
                    return 0;
                }
                int itemViewType = this.adapter.getItemViewType(headersCount);
                if (!XRecyclerView.this.isReservedItemViewType(itemViewType)) {
                    return itemViewType;
                }
                throw new IllegalStateException("XRecyclerView require itemViewType in adapter should be less than 10000 ");
            }
        }

        public long getItemId(int i) {
            if (this.adapter == null || i < getHeadersCount() + 1) {
                return -1;
            }
            int headersCount = i - (getHeadersCount() + 1);
            if (headersCount < this.adapter.getItemCount()) {
                return this.adapter.getItemId(headersCount);
            }
            return -1;
        }

        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    public int getSpanSize(int i) {
                        if (WrapAdapter.this.isHeader(i) || WrapAdapter.this.isFooter(i) || WrapAdapter.this.isRefreshHeader(i)) {
                            return gridLayoutManager.getSpanCount();
                        }
                        return 1;
                    }
                });
            }
            this.adapter.onAttachedToRecyclerView(recyclerView);
        }

        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            this.adapter.onDetachedFromRecyclerView(recyclerView);
        }

        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            super.onViewAttachedToWindow(viewHolder);
            ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
            if (layoutParams != null && (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) && (isHeader(viewHolder.getLayoutPosition()) || isRefreshHeader(viewHolder.getLayoutPosition()) || isFooter(viewHolder.getLayoutPosition()))) {
                ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
            }
            this.adapter.onViewAttachedToWindow(viewHolder);
        }

        public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder) {
            this.adapter.onViewDetachedFromWindow(viewHolder);
        }

        public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
            this.adapter.onViewRecycled(viewHolder);
        }

        public boolean onFailedToRecycleView(RecyclerView.ViewHolder viewHolder) {
            return this.adapter.onFailedToRecycleView(viewHolder);
        }

        public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
            this.adapter.unregisterAdapterDataObserver(adapterDataObserver);
        }

        public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
            this.adapter.registerAdapterDataObserver(adapterDataObserver);
        }
    }

    public XRecyclerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public XRecyclerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public XRecyclerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isLoadingData = false;
        this.isNoMore = false;
        this.mRefreshProgressStyle = -1;
        this.mLoadingMoreProgressStyle = -1;
        this.mHeaderViews = new ArrayList<>();
        this.mLastY = -1.0f;
        this.pullRefreshEnabled = true;
        this.loadingMoreEnabled = true;
        this.mPageCount = 0;
        this.mDataObserver = new DataObserver();
        this.appbarState = AppBarStateChangeListener.State.EXPANDED;
        init();
    }

    private void init() {
        if (this.pullRefreshEnabled) {
            this.mRefreshHeader = new ArrowRefreshHeader(getContext());
            this.mRefreshHeader.setProgressStyle(this.mRefreshProgressStyle);
        }
        LoadingMoreFooter loadingMoreFooter = new LoadingMoreFooter(getContext());
        loadingMoreFooter.setProgressStyle(this.mLoadingMoreProgressStyle);
        this.mFootView = loadingMoreFooter;
        this.mFootView.setVisibility(GONE);
    }

    public void addHeaderView(View view) {
        sHeaderTypes.add(Integer.valueOf(10002 + this.mHeaderViews.size()));
        this.mHeaderViews.add(view);
        if (this.mWrapAdapter != null) {
            this.mWrapAdapter.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    public View getHeaderViewByType(int i) {
        if (!isHeaderType(i)) {
            return null;
        }
        return this.mHeaderViews.get(i - 10002);
    }

    /* access modifiers changed from: private */
    public boolean isHeaderType(int i) {
        return this.mHeaderViews.size() > 0 && sHeaderTypes.contains(Integer.valueOf(i));
    }

    /* access modifiers changed from: private */
    public boolean isReservedItemViewType(int i) {
        return i == 10000 || i == 10001 || sHeaderTypes.contains(Integer.valueOf(i));
    }

    public void setFootView(View view) {
        this.mFootView = view;
    }

    public void loadMoreComplete() {
        this.isLoadingData = false;
        if (this.mFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) this.mFootView).setState(1);
        } else {
            this.mFootView.setVisibility(GONE);
        }
    }

    public void setNoMore(boolean z) {
        this.isLoadingData = false;
        this.isNoMore = z;
        if (this.mFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) this.mFootView).setState(this.isNoMore ? 2 : 1);
        } else {
            this.mFootView.setVisibility(GONE);
        }
    }

    public void refresh() {
        if (this.pullRefreshEnabled && this.mLoadingListener != null) {
            this.mRefreshHeader.setState(2);
            this.mLoadingListener.onRefresh();
        }
    }

    public void reset() {
        setNoMore(false);
        loadMoreComplete();
        refreshComplete();
    }

    public void refreshComplete() {
        this.mRefreshHeader.refreshComplete();
        setNoMore(false);
    }

    public void setRefreshHeader(ArrowRefreshHeader arrowRefreshHeader) {
        this.mRefreshHeader = arrowRefreshHeader;
    }

    public void setPullRefreshEnabled(boolean z) {
        this.pullRefreshEnabled = z;
    }

    public void setLoadingMoreEnabled(boolean z) {
        this.loadingMoreEnabled = z;
        if (!z && (this.mFootView instanceof LoadingMoreFooter)) {
            ((LoadingMoreFooter) this.mFootView).setState(1);
        }
    }

    public void setRefreshProgressStyle(int i) {
        this.mRefreshProgressStyle = i;
        if (this.mRefreshHeader != null) {
            this.mRefreshHeader.setProgressStyle(i);
        }
    }

    public void setLoadingMoreProgressStyle(int i) {
        this.mLoadingMoreProgressStyle = i;
        if (this.mFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) this.mFootView).setProgressStyle(i);
        }
    }

    public void setArrowImageView(int i) {
        if (this.mRefreshHeader != null) {
            this.mRefreshHeader.setArrowImageView(i);
        }
    }

    public void setEmptyView(View view) {
        this.mEmptyView = view;
        this.mDataObserver.onChanged();
    }

    public View getEmptyView() {
        return this.mEmptyView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(this.mWrapAdapter);
        adapter.registerAdapterDataObserver(this.mDataObserver);
        this.mDataObserver.onChanged();
    }

    public RecyclerView.Adapter getAdapter() {
        if (this.mWrapAdapter != null) {
            return this.mWrapAdapter.getOriginalAdapter();
        }
        return null;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        super.setLayoutManager(layoutManager);
        if (this.mWrapAdapter != null && (layoutManager instanceof GridLayoutManager)) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                public int getSpanSize(int i) {
                    if (XRecyclerView.this.mWrapAdapter.isHeader(i) || XRecyclerView.this.mWrapAdapter.isFooter(i) || XRecyclerView.this.mWrapAdapter.isRefreshHeader(i)) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    public void onScrollStateChanged(int i) {
        int i2;
        super.onScrollStateChanged(i);
        if (i == 0 && this.mLoadingListener != null && !this.isLoadingData && this.loadingMoreEnabled) {
            RecyclerView.LayoutManager layoutManager = getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                i2 = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] iArr = new int[staggeredGridLayoutManager.getSpanCount()];
                staggeredGridLayoutManager.findLastVisibleItemPositions(iArr);
                i2 = findMax(iArr);
            } else {
                i2 = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager.getChildCount() > 0 && i2 >= layoutManager.getItemCount() - 1 && layoutManager.getItemCount() > layoutManager.getChildCount() && !this.isNoMore && this.mRefreshHeader.getState() < 2) {
                this.isLoadingData = true;
                if (this.mFootView instanceof LoadingMoreFooter) {
                    ((LoadingMoreFooter) this.mFootView).setState(0);
                } else {
                    this.mFootView.setVisibility(VISIBLE);
                }
                this.mLoadingListener.onLoadMore();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mLastY == -1.0f) {
            this.mLastY = motionEvent.getRawY();
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mLastY = motionEvent.getRawY();
        } else if (action != 2) {
            this.mLastY = -1.0f;
            if (isOnTop() && this.pullRefreshEnabled && this.appbarState == AppBarStateChangeListener.State.EXPANDED && this.mRefreshHeader.releaseAction() && this.mLoadingListener != null) {
                this.mLoadingListener.onRefresh();
            }
        } else {
            float rawY = motionEvent.getRawY() - this.mLastY;
            this.mLastY = motionEvent.getRawY();
            if (isOnTop() && this.pullRefreshEnabled && this.appbarState == AppBarStateChangeListener.State.EXPANDED) {
                this.mRefreshHeader.onMove(rawY / 3.0f);
                if (this.mRefreshHeader.getVisibleHeight() > 0 && this.mRefreshHeader.getState() < 2) {
                    return false;
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    private int findMax(int[] iArr) {
        int i = iArr[0];
        for (int i2 : iArr) {
            if (i2 > i) {
                i = i2;
            }
        }
        return i;
    }

    private boolean isOnTop() {
        return this.mRefreshHeader.getParent() != null;
    }

    public void setLoadingListener(LoadingListener loadingListener) {
        this.mLoadingListener = loadingListener;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        AppBarLayout appBarLayout;
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        while (parent != null && !(parent instanceof CoordinatorLayout)) {
            parent = parent.getParent();
        }
        if (parent instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) parent;
            int childCount = coordinatorLayout.getChildCount() - 1;
            while (true) {
                if (childCount < 0) {
                    appBarLayout = null;
                    break;
                }
                View childAt = coordinatorLayout.getChildAt(childCount);
                if (childAt instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) childAt;
                    break;
                }
                childCount--;
            }
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State state) {
                        AppBarStateChangeListener.State unused = XRecyclerView.this.appbarState = state;
                    }
                });
            }
        }
    }
}
