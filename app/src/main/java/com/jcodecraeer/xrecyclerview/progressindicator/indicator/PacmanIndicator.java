package com.jcodecraeer.xrecyclerview.progressindicator.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.animation.LinearInterpolator;
import java.util.ArrayList;
import java.util.List;

public class PacmanIndicator extends BaseIndicatorController {
    /* access modifiers changed from: private */
    public int alpha;
    /* access modifiers changed from: private */
    public float degrees1;
    /* access modifiers changed from: private */
    public float degrees2;
    /* access modifiers changed from: private */
    public float translateX;

    public void draw(Canvas canvas, Paint paint) {
        drawPacman(canvas, paint);
        drawCircle(canvas, paint);
    }

    private void drawPacman(Canvas canvas, Paint paint) {
        float width = (float) (getWidth() / 2);
        float height = (float) (getHeight() / 2);
        canvas.save();
        canvas.translate(width, height);
        canvas.rotate(this.degrees1);
        paint.setAlpha(255);
        float f = (-width) / 1.7f;
        float f2 = (-height) / 1.7f;
        float f3 = width / 1.7f;
        float f4 = height / 1.7f;
        Canvas canvas2 = canvas;
        Paint paint2 = paint;
        canvas2.drawArc(new RectF(f, f2, f3, f4), 0.0f, 270.0f, true, paint2);
        canvas.restore();
        canvas.save();
        canvas.translate(width, height);
        canvas.rotate(this.degrees2);
        paint.setAlpha(255);
        canvas2.drawArc(new RectF(f, f2, f3, f4), 90.0f, 270.0f, true, paint2);
        canvas.restore();
    }

    private void drawCircle(Canvas canvas, Paint paint) {
        paint.setAlpha(this.alpha);
        canvas.drawCircle(this.translateX, (float) (getHeight() / 2), (float) (getWidth() / 11), paint);
    }

    public List<Animator> createAnimation() {
        ArrayList arrayList = new ArrayList();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{((float) getWidth()) - ((float) (getWidth() / 11)), (float) (getWidth() / 2)});
        ofFloat.setDuration(650);
        ofFloat.setInterpolator(new LinearInterpolator());
        ofFloat.setRepeatCount(-1);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float unused = PacmanIndicator.this.translateX = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                PacmanIndicator.this.postInvalidate();
            }
        });
        ofFloat.start();
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{255, 122});
        ofInt.setDuration(650);
        ofInt.setRepeatCount(-1);
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int unused = PacmanIndicator.this.alpha = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                PacmanIndicator.this.postInvalidate();
            }
        });
        ofInt.start();
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{0.0f, 45.0f, 0.0f});
        ofFloat2.setDuration(650);
        ofFloat2.setRepeatCount(-1);
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float unused = PacmanIndicator.this.degrees1 = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                PacmanIndicator.this.postInvalidate();
            }
        });
        ofFloat2.start();
        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(new float[]{0.0f, -45.0f, 0.0f});
        ofFloat3.setDuration(650);
        ofFloat3.setRepeatCount(-1);
        ofFloat3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float unused = PacmanIndicator.this.degrees2 = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                PacmanIndicator.this.postInvalidate();
            }
        });
        ofFloat3.start();
        arrayList.add(ofFloat);
        arrayList.add(ofInt);
        arrayList.add(ofFloat2);
        arrayList.add(ofFloat3);
        return arrayList;
    }
}
