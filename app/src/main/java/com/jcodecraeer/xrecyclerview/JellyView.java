package com.jcodecraeer.xrecyclerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class JellyView extends View implements BaseRefreshHeader {
    private int jellyHeight = 0;
    private int minimumHeight = 0;
    Paint paint;
    Path path;

    public void refreshComplete() {
    }

    public boolean releaseAction() {
        return false;
    }

    public JellyView(Context context) {
        super(context);
        init();
    }

    public JellyView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public JellyView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    @TargetApi(21)
    public JellyView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            this.path = new Path();
            this.paint = new Paint();
            this.paint.setColor(getContext().getResources().getColor(17170459));
            this.paint.setAntiAlias(true);
        }
    }

    public void setJellyColor(int i) {
        this.paint.setColor(i);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.path.reset();
        this.path.lineTo(0.0f, (float) this.minimumHeight);
        this.path.quadTo((float) (getMeasuredWidth() / 2), (float) (this.minimumHeight + this.jellyHeight), (float) getMeasuredWidth(), (float) this.minimumHeight);
        this.path.lineTo((float) getMeasuredWidth(), 0.0f);
        canvas.drawPath(this.path, this.paint);
    }

    public void setMinimumHeight(int i) {
        this.minimumHeight = i;
    }

    public void setJellyHeight(int i) {
        this.jellyHeight = i;
    }

    public int getMinimumHeight() {
        return this.minimumHeight;
    }

    public int getJellyHeight() {
        return this.jellyHeight;
    }

    public void onMove(float f) {
        this.jellyHeight += (int) f;
        Log.i("jellyHeight", "delta = " + f);
        invalidate();
    }
}
