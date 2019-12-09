package com.android.hoinnet.highde.view;

import android.animation.ArgbEvaluator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import com.android.hoinnet.highde.R;

public class RxShapeView extends View {
    private static final float genhao3 = 1.7320508f;
    private static final float mTriangle2Circle = 0.25555554f;
    private float mAnimPercent;
    private ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();
    private int mCircleColor;
    private float mControlX = 0.0f;
    private float mControlY = 0.0f;
    private Interpolator mInterpolator = new DecelerateInterpolator();
    public boolean mIsLoading = false;
    private float mMagicNumber = 0.5522848f;
    private Paint mPaint;
    private int mRectColor;
    private Shape mShape = Shape.SHAPE_CIRCLE;
    private int mTriangleColor;

    public enum Shape {
        SHAPE_TRIANGLE,
        SHAPE_RECT,
        SHAPE_CIRCLE
    }

    public RxShapeView(Context context) {
        super(context);
        init();
    }

    public RxShapeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public RxShapeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @TargetApi(21)
    public RxShapeView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init();
    }

    private void init() {
        this.mPaint = new Paint();
        this.mPaint.setColor(getResources().getColor(R.color.triangle));
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        setBackgroundColor(getResources().getColor(R.color.transparent));
        this.mTriangleColor = getResources().getColor(R.color.triangle);
        this.mCircleColor = getResources().getColor(R.color.circle);
        this.mRectColor = getResources().getColor(R.color.triangle);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        super.onDraw(canvas);
        if (getVisibility() != 8) {
            switch (this.mShape) {
                case SHAPE_TRIANGLE:
                    if (this.mIsLoading) {
                        this.mAnimPercent = (float) (((double) this.mAnimPercent) + 0.1611113d);
                        this.mPaint.setColor(((Integer) this.mArgbEvaluator.evaluate(this.mAnimPercent, Integer.valueOf(this.mTriangleColor), Integer.valueOf(this.mCircleColor))).intValue());
                        Path path = new Path();
                        path.moveTo(relativeXFromView(0.5f), relativeYFromView(0.0f));
                        if (this.mAnimPercent >= 1.0f) {
                            this.mShape = Shape.SHAPE_CIRCLE;
                            this.mIsLoading = false;
                            this.mAnimPercent = 1.0f;
                        }
                        float relativeXFromView = this.mControlX - (relativeXFromView(this.mAnimPercent * mTriangle2Circle) * genhao3);
                        float relativeYFromView = this.mControlY - relativeYFromView(this.mAnimPercent * mTriangle2Circle);
                        path.quadTo(relativeXFromView(1.0f) - relativeXFromView, relativeYFromView, relativeXFromView(0.9330127f), relativeYFromView(0.75f));
                        path.quadTo(relativeXFromView(0.5f), relativeYFromView((2.0f * this.mAnimPercent * mTriangle2Circle) + 0.75f), relativeXFromView(0.066987306f), relativeYFromView(0.75f));
                        path.quadTo(relativeXFromView, relativeYFromView, relativeXFromView(0.5f), relativeYFromView(0.0f));
                        path.close();
                        canvas2.drawPath(path, this.mPaint);
                        invalidate();
                        return;
                    }
                    Path path2 = new Path();
                    this.mPaint.setColor(getResources().getColor(R.color.triangle));
                    path2.moveTo(relativeXFromView(0.5f), relativeYFromView(0.0f));
                    path2.lineTo(relativeXFromView(1.0f), relativeYFromView(0.8660254f));
                    path2.lineTo(relativeXFromView(0.0f), relativeYFromView(0.8660254f));
                    this.mControlX = relativeXFromView(0.28349364f);
                    this.mControlY = relativeYFromView(0.375f);
                    this.mAnimPercent = 0.0f;
                    path2.close();
                    canvas2.drawPath(path2, this.mPaint);
                    return;
                case SHAPE_CIRCLE:
                    if (this.mIsLoading) {
                        float f = this.mMagicNumber + this.mAnimPercent;
                        this.mAnimPercent = (float) (((double) this.mAnimPercent) + 0.12d);
                        if (this.mAnimPercent + f >= 1.9f) {
                            this.mShape = Shape.SHAPE_RECT;
                            this.mIsLoading = false;
                        }
                        this.mPaint.setColor(((Integer) this.mArgbEvaluator.evaluate(this.mAnimPercent, Integer.valueOf(this.mCircleColor), Integer.valueOf(this.mRectColor))).intValue());
                        Path path3 = new Path();
                        path3.moveTo(relativeXFromView(0.5f), relativeYFromView(0.0f));
                        float f2 = f / 2.0f;
                        float f3 = 0.5f + f2;
                        float f4 = 0.5f - f2;
                        Path path4 = path3;
                        path4.cubicTo(relativeXFromView(f3), relativeYFromView(0.0f), relativeXFromView(1.0f), relativeYFromView(f4), relativeXFromView(1.0f), relativeYFromView(0.5f));
                        path4.cubicTo(relativeXFromView(1.0f), relativeXFromView(f3), relativeXFromView(f3), relativeYFromView(1.0f), relativeXFromView(0.5f), relativeYFromView(1.0f));
                        path4.cubicTo(relativeXFromView(f4), relativeXFromView(1.0f), relativeXFromView(0.0f), relativeYFromView(f3), relativeXFromView(0.0f), relativeYFromView(0.5f));
                        path4.cubicTo(relativeXFromView(0.0f), relativeXFromView(f4), relativeXFromView(f4), relativeYFromView(0.0f), relativeXFromView(0.5f), relativeYFromView(0.0f));
                        path3.close();
                        canvas2.drawPath(path3, this.mPaint);
                        invalidate();
                        return;
                    }
                    this.mPaint.setColor(getResources().getColor(R.color.circle));
                    Path path5 = new Path();
                    float f5 = this.mMagicNumber;
                    path5.moveTo(relativeXFromView(0.5f), relativeYFromView(0.0f));
                    float f6 = f5 / 2.0f;
                    float f7 = 0.5f + f6;
                    Path path6 = path5;
                    path6.cubicTo(relativeXFromView(f7), 0.0f, relativeXFromView(1.0f), relativeYFromView(f6), relativeXFromView(1.0f), relativeYFromView(0.5f));
                    path6.cubicTo(relativeXFromView(1.0f), relativeXFromView(f7), relativeXFromView(f7), relativeYFromView(1.0f), relativeXFromView(0.5f), relativeYFromView(1.0f));
                    float f8 = 0.5f - f6;
                    path6.cubicTo(relativeXFromView(f8), relativeXFromView(1.0f), relativeXFromView(0.0f), relativeYFromView(f7), relativeXFromView(0.0f), relativeYFromView(0.5f));
                    path6.cubicTo(relativeXFromView(0.0f), relativeXFromView(f8), relativeXFromView(f8), relativeYFromView(0.0f), relativeXFromView(0.5f), relativeYFromView(0.0f));
                    this.mAnimPercent = 0.0f;
                    path5.close();
                    canvas2.drawPath(path5, this.mPaint);
                    return;
                case SHAPE_RECT:
                    if (this.mIsLoading) {
                        this.mAnimPercent = (float) (((double) this.mAnimPercent) + 0.15d);
                        if (this.mAnimPercent >= 1.0f) {
                            this.mShape = Shape.SHAPE_TRIANGLE;
                            this.mIsLoading = false;
                            this.mAnimPercent = 1.0f;
                        }
                        this.mPaint.setColor(((Integer) this.mArgbEvaluator.evaluate(this.mAnimPercent, Integer.valueOf(this.mRectColor), Integer.valueOf(this.mTriangleColor))).intValue());
                        Path path7 = new Path();
                        path7.moveTo(relativeXFromView(this.mAnimPercent * 0.5f), 0.0f);
                        path7.lineTo(relativeYFromView(1.0f - (0.5f * this.mAnimPercent)), 0.0f);
                        float f9 = this.mControlX * this.mAnimPercent;
                        float relativeYFromView2 = (relativeYFromView(1.0f) - this.mControlY) * this.mAnimPercent;
                        path7.lineTo(relativeXFromView(1.0f) - f9, relativeYFromView(1.0f) - relativeYFromView2);
                        path7.lineTo(relativeXFromView(0.0f) + f9, relativeYFromView(1.0f) - relativeYFromView2);
                        path7.close();
                        canvas2.drawPath(path7, this.mPaint);
                        invalidate();
                        return;
                    }
                    this.mPaint.setColor(getResources().getColor(R.color.rect));
                    this.mControlX = relativeXFromView(0.066987306f);
                    this.mControlY = relativeYFromView(0.75f);
                    Path path8 = new Path();
                    path8.moveTo(relativeXFromView(0.0f), relativeYFromView(0.0f));
                    path8.lineTo(relativeXFromView(1.0f), relativeYFromView(0.0f));
                    path8.lineTo(relativeXFromView(1.0f), relativeYFromView(1.0f));
                    path8.lineTo(relativeXFromView(0.0f), relativeYFromView(1.0f));
                    path8.close();
                    this.mAnimPercent = 0.0f;
                    canvas2.drawPath(path8, this.mPaint);
                    return;
                default:
                    return;
            }
        }
    }

    private float relativeXFromView(float f) {
        return ((float) getWidth()) * f;
    }

    private float relativeYFromView(float f) {
        return ((float) getHeight()) * f;
    }

    public void changeShape() {
        this.mIsLoading = true;
        invalidate();
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        if (i == 0) {
            invalidate();
        }
    }

    public Shape getShape() {
        return this.mShape;
    }
}
