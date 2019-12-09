package com.jcodecraeer.xrecyclerview.progressindicator.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.animation.LinearInterpolator;
import java.util.ArrayList;
import java.util.List;

public class CubeTransitionIndicator extends BaseIndicatorController {
    float degrees;
    float scaleFloat = 1.0f;
    float[] translateX = new float[2];
    float[] translateY = new float[2];

    public void draw(Canvas canvas, Paint paint) {
        float width = (float) (getWidth() / 5);
        float height = (float) (getHeight() / 5);
        for (int i = 0; i < 2; i++) {
            canvas.save();
            canvas.translate(this.translateX[i], this.translateY[i]);
            canvas.rotate(this.degrees);
            canvas.scale(this.scaleFloat, this.scaleFloat);
            canvas.drawRect(new RectF((-width) / 2.0f, (-height) / 2.0f, width / 2.0f, height / 2.0f), paint);
            canvas.restore();
        }
    }

    public List<Animator> createAnimation() {
        ArrayList arrayList = new ArrayList();
        float width = (float) (getWidth() / 5);
        float height = (float) (getHeight() / 5);
        for (final int i = 0; i < 2; i++) {
            this.translateX[i] = width;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{width, ((float) getWidth()) - width, ((float) getWidth()) - width, width, width});
            if (i == 1) {
                ofFloat = ValueAnimator.ofFloat(new float[]{((float) getWidth()) - width, width, width, ((float) getWidth()) - width, ((float) getWidth()) - width});
            }
            ofFloat.setInterpolator(new LinearInterpolator());
            ofFloat.setDuration(1600);
            ofFloat.setRepeatCount(-1);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    CubeTransitionIndicator.this.translateX[i] = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    CubeTransitionIndicator.this.postInvalidate();
                }
            });
            ofFloat.start();
            this.translateY[i] = height;
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{height, height, ((float) getHeight()) - height, ((float) getHeight()) - height, height});
            if (i == 1) {
                ofFloat2 = ValueAnimator.ofFloat(new float[]{((float) getHeight()) - height, ((float) getHeight()) - height, height, height, ((float) getHeight()) - height});
            }
            ofFloat2.setDuration(1600);
            ofFloat2.setInterpolator(new LinearInterpolator());
            ofFloat2.setRepeatCount(-1);
            ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    CubeTransitionIndicator.this.translateY[i] = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    CubeTransitionIndicator.this.postInvalidate();
                }
            });
            ofFloat2.start();
            arrayList.add(ofFloat);
            arrayList.add(ofFloat2);
        }
        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(new float[]{1.0f, 0.5f, 1.0f, 0.5f, 1.0f});
        ofFloat3.setDuration(1600);
        ofFloat3.setInterpolator(new LinearInterpolator());
        ofFloat3.setRepeatCount(-1);
        ofFloat3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                CubeTransitionIndicator.this.scaleFloat = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                CubeTransitionIndicator.this.postInvalidate();
            }
        });
        ofFloat3.start();
        ValueAnimator ofFloat4 = ValueAnimator.ofFloat(new float[]{0.0f, 180.0f, 360.0f, 540.0f, 720.0f});
        ofFloat4.setDuration(1600);
        ofFloat4.setInterpolator(new LinearInterpolator());
        ofFloat4.setRepeatCount(-1);
        ofFloat4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                CubeTransitionIndicator.this.degrees = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                CubeTransitionIndicator.this.postInvalidate();
            }
        });
        ofFloat4.start();
        arrayList.add(ofFloat3);
        arrayList.add(ofFloat4);
        return arrayList;
    }
}
