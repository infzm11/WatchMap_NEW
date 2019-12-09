package com.android.hoinnet.highde.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class CircularRelativeLayout extends RelativeLayout {
    private static final int HEIGHT = 1;
    private static final int NONE = 2;
    private static final int WIDTH = 0;
    private Path ovalPath;
    @Dimension
    private int primaryDimension;

    @interface Dimension {
    }

    public CircularRelativeLayout(Context context) {
        super(context);
        init();
    }

    public CircularRelativeLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public CircularRelativeLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        this.primaryDimension = 0;
        this.ovalPath = new Path();
    }

    /* access modifiers changed from: protected */
    @TargetApi(21)
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        switch (this.primaryDimension) {
            case 0:
                getLayoutParams().height = getMeasuredWidth();
                break;
            case 1:
                getLayoutParams().width = getMeasuredHeight();
                break;
        }
        this.ovalPath.reset();
        this.ovalPath.addOval(0.0f, 0.0f, (float) getMeasuredWidth(), (float) getMeasuredHeight(), Path.Direction.CCW);
        this.ovalPath.close();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.clipPath(this.ovalPath);
        super.onDraw(canvas);
    }

    public int getPrimaryDimension() {
        return this.primaryDimension;
    }

    public void setPrimaryDimension(@Dimension int i) {
        this.primaryDimension = i;
    }
}
